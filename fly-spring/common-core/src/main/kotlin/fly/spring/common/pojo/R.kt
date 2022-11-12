package fly.spring.common.pojo

import com.fasterxml.jackson.annotation.JsonIgnore
import fly.spring.common.constant.CommonConstants
import java.io.Serializable

/**
 * base response entity
 */
open class R<T> : Serializable {
    open var code: Int? = null
    open var msg: String? = null
    open var data: T? = null

    companion object {
        private const val serialVersionUID = 1L

        @JvmStatic
        fun <T> create(data: T, msg: String?): R<T> {
            return R<T>().apply {
                this.msg = msg
                this.data = data
            }
        }

        @JvmStatic
        fun <T> create(data: T, code: Int?, msg: String?): R<T> {
            return R<T>().apply {
                this.code = code
                this.msg = msg
                this.data = data
            }
        }

        @JvmStatic
        fun <T> ok(): R<T?> {
            return create(null, null)
        }

        @JvmStatic
        fun <T> okc(): R<T?> {
            @Suppress("UNCHECKED_CAST")
            return OK as R<T?>
        }

        @JvmStatic
        fun <T> ok(data: T): R<T> {
            return create(data, null)
        }

        @JvmStatic
        fun <T> ok(data: T, msg: String): R<T> {
            return create(data, msg)
        }

        @JvmStatic
        fun <T> fail(): R<T?> {
            return create(null, CommonConstants.FAIL, null)
        }

        @JvmStatic
        fun <T> failc(): R<T?> {
            @Suppress("UNCHECKED_CAST")
            return FAIL as R<T?>
        }

        @JvmStatic
        fun <T> fail(data: T): R<T> {
            return create(data, CommonConstants.FAIL, null)
        }

        @JvmStatic
        fun <T> fail(data: T, msg: String): R<T> {
            return create(data, CommonConstants.FAIL, msg)
        }

        @Suppress("UNUSED_PARAMETER")
        class RC() : R<Any>() {
            constructor(code: Int?) : this() {
                super.code = code
            }

            override var code: Int?
                get() = null
                set(value) = throw UnsupportedOperationException()

            override var msg: String?
                @JsonIgnore
                get() = null
                set(value) = throw UnsupportedOperationException()

            override var data: Any?
                @JsonIgnore
                get() = null
                set(value) = throw UnsupportedOperationException()
        }

        private val OK = RC()

        private val FAIL = RC(CommonConstants.FAIL)

    }
}
