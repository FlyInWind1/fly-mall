package fly.spring.common.bean

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class BeanUtilTest {

    @Test
    fun copyNonSettable() {
        val obj = TestClass1()
        obj.a1 = "1"
        val copy = BeanUtil.copyNonSettable(obj)
        assertThrows<IllegalAccessError> { copy.a1 = "2" }
    }


}
