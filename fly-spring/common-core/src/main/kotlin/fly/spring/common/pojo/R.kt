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

        fun <T> create(data: T, msg: String?): R<T> {
            return R<T>().apply {
                this.msg = msg
                this.data = data
            }
        }

        fun <T> create(data: T, code: Int?, msg: String?): R<T> {
            return R<T>().apply {
                this.code = code
                this.msg = msg
                this.data = data
            }
        }

        fun <T> ok(): R<T?> {
            return create(null, null)
        }

        fun <T> okc(): R<T?> {
            @Suppress("UNCHECKED_CAST")
            return OK as R<T?>
        }

        fun <T> ok(data: T): R<T> {
            return create(data, null)
        }

        fun <T> ok(data: T, msg: String): R<T> {
            return create(data, msg)
        }

        fun <T> fail(): R<T?> {
            return create(null, CommonConstants.FAIL, null)
        }

        fun <T> failc(): R<T?> {
            @Suppress("UNCHECKED_CAST")
            return FAIL as R<T?>
        }

        fun <T> fail(data: T): R<T> {
            return create(data, CommonConstants.FAIL, null)
        }

        fun <T> fail(data: T, msg: String): R<T> {
            return create(data, CommonConstants.FAIL, msg)
        }

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
