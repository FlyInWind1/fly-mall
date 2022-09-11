package fly.spring.common.util

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.Phaser
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer

/**
 * 多线程处理任务的工具类
 * <br></br>
 * 内置一个线程池，默认线程数是cpu线程数的一半，线程数可以在spring中设置 thread-pool.thread-count 来控制
 * <br></br>
 * 当线程池没有空闲线程处理任务时，会判断往线程池添加任务的线程是否是属于线程池，
 * 如果属于线程池就就会从任务队列窃取一个任务来执行，执行完后再次尝试添加任务，
 * 如果不属于线程池就堵住线程，直到有空位可以添加任务
 *
 * @author FlyInWind
 * @date 2021/01/17
 */
@Component
@Lazy(false)
class MultiThreadUtil constructor(@Value("\${thread-pool.thread-count:-1}") corePoolSize: Int) {
    /**
     * 自定义拒绝策略
     */
    private class RejectHandler : RejectedExecutionHandler {
        override fun rejectedExecution(r: Runnable, executor: ThreadPoolExecutor) {
            val currentThread = Thread.currentThread()
            val threadGroup = currentThread.threadGroup
            if (!executor.isShutdown) {
                //借助线程组判断当前线程来自线程池还是别处
                if (threadGroup === THREAD_GROUP) {
                    //如果来自线程池，就从任务队列窃取一个任务执行，执行完后再次尝试添加任务
                    do {
                        runOneTaskInPool()
                    } while (!queue.offer(r))
                } else {
                    //如果来自别处，尝试获取任务队列的putLock
                    queuePutLock.lock()
                    try {
                        //等到队列有空位时重新插入
                        while (queueCount.get() >= QUEUE_CAPACITY) {
                            queueNotFull.await()
                        }
                        executor.execute(r)
                    } finally {
                        queuePutLock.unlock()
                    }
                }
            }
        }
    }

    class TaskNumLimiter(private val maxTask: Int) {
        private val runningTask = AtomicInteger()
        private fun getRunningTask(): Int {
            return runningTask.get()
        }

        private fun arriveLimit(): Boolean {
            return getRunningTask() >= maxTask
        }

        fun execute(runnable: Runnable) {
            var pushed = false
            while (!pushed) {
                synchronized(this) {
                    if (!arriveLimit()) {
                        runningTask.incrementAndGet()
                        Companion.execute {
                            try {
                                runnable.run()
                            } finally {
                                runningTask.decrementAndGet()
                            }
                        }
                        pushed = true
                    }
                }
                if (!pushed) {
                    runOneTaskInPool()
                }
            }
        }
    }

    companion object {
        val log = KotlinLogging.logger { }

        /**
         * 线程数，队列被严格限制
         * 如果线程池无法处理任务，使用调用者的线程
         */
        var executor: ThreadPoolTaskExecutor = ThreadPoolTaskExecutor()
        var corePoolSize = 0

        /**
         * [.executor] 内的线程都属于这个线程组
         * <br></br>
         * 利用线程组判断线程是来自 [.executor] 还是别处
         */
        val THREAD_GROUP = ThreadGroup(MultiThreadUtil::class.java.name)

        /**
         * 线程池的任务队列
         */
        private lateinit var queue: LinkedBlockingQueue<Runnable>

        /**
         * 线程池的任务队列里的一些私有属性，用反射获取
         * <br></br>
         * 用来堵住当前线程，直到队列有空位可插入任务
         */
        private lateinit var queueCount: AtomicInteger
        private lateinit var queuePutLock: ReentrantLock
        private lateinit var queueNotFull: Condition

        /**
         * 队列大小
         */
        private const val QUEUE_CAPACITY = Byte.MAX_VALUE.toInt()

        /**
         * 多线程消费list
         * 线程数为cpu线程数
         * 在消费完之前不会返回
         *
         * @param iterable 可迭代的
         * @param consumer 消费者
         * @return iterable 数量
         */
        fun <T> consume(iterable: Iterable<T>?, consumer: Consumer<T>): Int {
            Objects.requireNonNull(consumer, "消费者不能为空")
            if (iterable == null) {
                return 0
            }
            val phaser = Phaser(1)
            for (item in iterable) {
                phaser.register()
                executor.execute {
                    try {
                        consumer.accept(item)
                    } finally {
                        phaser.arrive()
                    }
                }
            }
            phaser.arrive()
            while (phaser.phase != 1) {
                runOneTaskInPool()
            }
            return phaser.registeredParties - 1
        }

        fun execute(runnable: Runnable) {
            executor.execute(runnable)
        }

        fun execute(limit: TaskNumLimiter, runnable: Runnable) {
            limit.execute(runnable)
        }

        fun runOneTaskInPool(): Boolean {
            val runnable: Runnable = queue.poll()
            runnable.run()
            return true
        }

        fun newTaskNumLimiter(): TaskNumLimiter {
            return TaskNumLimiter(corePoolSize)
        }

        /**
         * 修剪在线程池中使用ByteBufUtils创建的ByteBuf所占用的内存空间
         */
        /*fun trimByteBufCache() {
            val countDownLatch = CountDownLatch(corePoolSize)
            for (i in 0 until corePoolSize) {
                executor.execute(Runnable {
                    try {
                        ByteBufUtils.ALLOCATOR.trimCurrentThreadCache()
                        countDownLatch.countDown()
                        countDownLatch.await()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                })
            }
        }*/
    }

    init {
        var localCorePoolSize = corePoolSize
        if (localCorePoolSize == -1) {
            localCorePoolSize = Runtime.getRuntime().availableProcessors()
        }
        Companion.corePoolSize = localCorePoolSize

        //自定义线程工厂，以设置线程的线程组
        val rejectHandler = RejectHandler()
        executor.corePoolSize = localCorePoolSize
        executor.maxPoolSize = localCorePoolSize
        executor.keepAliveSeconds = 0
        executor.setQueueCapacity(QUEUE_CAPACITY)
        executor.setThreadNamePrefix("pooled-thread-")
        executor.threadGroup = THREAD_GROUP
        executor.setRejectedExecutionHandler(rejectHandler)
        executor.setTaskDecorator { runnable: Runnable ->
            Runnable {
                try {
                    runnable.run()
                } catch (e: Exception) {
                    log.warn("未处理异常", e)
                }
            }
        }
        executor.initialize()
        val threadPoolExecutor: ThreadPoolExecutor = executor.threadPoolExecutor
        val workQueue: Field = ThreadPoolExecutor::class.java.getDeclaredField("workQueue")
        workQueue.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        queue = workQueue[threadPoolExecutor] as LinkedBlockingQueue<Runnable>
        //反射获取队列的私有属性
        val count: Field = LinkedBlockingQueue::class.java.getDeclaredField("count")
        count.isAccessible = true
        queueCount = count[queue] as AtomicInteger
        val putLock: Field = LinkedBlockingQueue::class.java.getDeclaredField("putLock")
        putLock.isAccessible = true
        queuePutLock = putLock[queue] as ReentrantLock
        val notFull: Field = LinkedBlockingQueue::class.java.getDeclaredField("notFull")
        notFull.isAccessible = true
        queueNotFull = notFull[queue] as Condition
    }
}
