package fly.spring.common.core.util;

import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.commons.collections4.IteratorUtils.asIterator;

/**
 * iterable工具
 *
 * @author FlyInWind
 * @date 2021/07/26
 */
public class IterableUtil extends org.apache.commons.collections4.IterableUtils {
    /**
     * 映射iterable
     *
     * @param iterable 可迭代的
     * @param mapper   映射器
     * @return {@link Iterable<O>}
     */
    public static <I, O> Iterable<O> mappedIterable(Iterable<I> iterable, Function<I, O> mapper) {
        return () -> IteratorUtil.mappedIterator(iterable.iterator(), mapper);
    }

    /**
     * 参考{@link org.apache.commons.collections4.IterableUtils#chainedIterable}
     *
     * @param iterables iterable
     * @return {@link Iterable}<{@link T}>
     */
    public static <T> Iterable<T> chainedIterable(Iterable<? extends Iterable<T>> iterables) {
        return () -> IteratorUtil.chainedIterator(IteratorUtil.mappedIterator(iterables.iterator(), Iterable::iterator));
    }

    /**
     * 分批处理
     *
     * @param iterable  可迭代的
     * @param batchSize 批量大小
     * @param consumer  消费者
     */
    public static <T> void consumeCollected(Iterable<T> iterable, int batchSize, Consumer<List<T>> consumer) {
        IteratorUtil.consumeCollected(iterable.iterator(), batchSize, consumer);
    }

    /**
     * 分批处理
     *
     * @param iterable     可迭代的
     * @param batchSize    批量大小
     * @param consumer     消费者
     * @param listSupplier 供应商列表
     */
    public static <T> void consumeCollected(Iterable<T> iterable, int batchSize, Consumer<List<T>> consumer, Supplier<List<T>> listSupplier) {
        IteratorUtil.consumeCollected(iterable.iterator(), batchSize, consumer, listSupplier);
    }

    /**
     * 分批处理
     *
     * @param iterable     可迭代的
     * @param batchSize    批量大小
     * @param consumer     消费者
     */
    public static <T> void consumeCollected(Iterable<T> iterable, int batchSize, int totalCount, Consumer<List<T>> consumer) {
        IteratorUtil.consumeCollected(iterable.iterator(), batchSize, totalCount, consumer);
    }

    /**
     * 分批处理
     *
     * @param iterable  可迭代的
     * @param batchSize 批量大小
     */
    public static <T> Iterable<List<T>> collectedIterable(Iterable<T> iterable, int batchSize) {
        return () -> IteratorUtil.collectedIterator(iterable.iterator(), batchSize);
    }

    /**
     * 分批处理
     *
     * @param iterable  可迭代的
     * @param batchSize 批量大小
     */
    public static <T> Iterable<List<T>> collectedIterable(Iterable<T> iterable, int batchSize, int totalCount) {
        return () -> IteratorUtil.collectedIterator(iterable.iterator(), batchSize, totalCount);
    }

    /**
     * 分批处理 {@link Iterable}，与{@link #consumeCollected}相似，但功能少，不额外消耗内存
     *
     * @param iterable  可迭代的
     * @param batchSize 批量大小
     * @param consumer  消费者
     */
    public static <T> void consumeBatched(Iterable<T> iterable, int batchSize, Consumer<Iterable<T>> consumer) {
        IteratorUtil.consumeBatched(iterable.iterator(), batchSize, iterator -> consumer.accept(() -> iterator));
    }

    /**
     * 将 {@link Enumeration} 转为 {@link Iterable}，与 {@link #asIterable(Enumeration)} 相比，可以多次获取 Iterator
     *
     * @param supplier 枚举
     * @return {@link Iterable}<{@link T}>
     */
    public static <T> Iterable<T> asIterable(Supplier<Enumeration<T>> supplier) {
        return () -> asIterator(supplier.get());
    }

    /**
     * 将 {@link Enumeration} 转为 {@link Iterable}
     *
     * @param enumeration 枚举
     * @return {@link Iterable}<{@link T}>
     */
    public static <T> Iterable<T> asIterable(Enumeration<T> enumeration) {
        return () -> asIterator(enumeration);
    }
}
