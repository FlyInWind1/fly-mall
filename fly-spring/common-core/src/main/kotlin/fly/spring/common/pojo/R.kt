package fly.spring.common.pojo

import fly.spring.common.constant.CommonConstants
import java.io.Serializable
import java.lang.UnsupportedOperationException

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

        fun <T> ok(data: T): R<T> {
            return create(data, null)
        }

        fun <T> ok(data: T, msg: String): R<T> {
            return create(data, msg)
        }

        fun <T> fail(): R<T?> {
            return create(null, CommonConstants.FAIL, null)
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
                get() = null
                set(value) = throw UnsupportedOperationException()

            override var data: Any?
                get() = null
                set(value) = throw UnsupportedOperationException()
        }

        val ROK: R<Any> = RC()

        val RFAIL: R<Any> = RC(CommonConstants.FAIL)

    }
}
