package fly.spring.common.cdc.listener

import kotlin.test.Test

class Test1 {
    @Test
    fun test1() {
        val listener = Test1Listener("test1")
        listener.run()
    }


    @Test
    fun test2() {
        val listener = Test1Listener("test2")
        listener.run()
    }
}
