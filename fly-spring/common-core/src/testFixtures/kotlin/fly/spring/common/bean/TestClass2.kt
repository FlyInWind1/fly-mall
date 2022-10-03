package fly.spring.common.bean

open class TestClass2 {
    open var a1: String? = null
    open var a2: Int? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestClass2) return false

        if (a1 != other.a1) return false
        if (a2 != other.a2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = a1?.hashCode() ?: 0
        result = 31 * result + (a2 ?: 0)
        return result
    }

}
