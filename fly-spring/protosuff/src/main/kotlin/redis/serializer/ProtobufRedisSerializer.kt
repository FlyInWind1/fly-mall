package redis.serializer

import io.protostuff.LinkedBuffer
import io.protostuff.ProtostuffIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import org.springframework.data.redis.serializer.RedisSerializer

@ExperimentalStdlibApi
class ProtobufRedisSerializer : RedisSerializer<Any> {

    companion object {
        private val flag = " j;".encodeToByteArray()
        private val flagSize = flag.size
        private val flagFirst = flag[0]
        private val flagSecond = flag[1]
        private val flagThird = flag[2]

        //        private val flagFour = flag[3]
        private val flag2 = Byte.MIN_VALUE
        private val offside = flag2 + 1

        private val emptyByteArray = byteArrayOf()
    }

    override fun serialize(t: Any?): ByteArray? {
        if (t == null) return null

        val classNameBytes = t.javaClass.name.encodeToByteArray()
        val classNameByteSize = classNameBytes.size

        val schema = RuntimeSchema.getSchema(t.javaClass)

        val bytes = ProtostuffIOUtil.toByteArray(t, schema, LinkedBuffer.allocate(4096))
        val byteSize = bytes.size

        val result = ByteArray(classNameByteSize + flagSize + byteSize)

        System.arraycopy(classNameBytes, 0, result, 0, classNameByteSize)
        System.arraycopy(flag, 0, result, classNameByteSize, flagSize)
        System.arraycopy(bytes, 0, result, classNameByteSize + flagSize, byteSize)

        return result
    }

    override fun deserialize(bytes: ByteArray?): Any? {
        if (bytes == null) return null

        val byteSize = bytes.size
        if (byteSize <= flagSize) return null

        var classNameBytes = emptyByteArray
        var body = emptyByteArray
        val whileLimit = byteSize - flagSize + 1
        var i = -1
        while ((++i) < whileLimit) {
            if (flagFirst == bytes[i]
                    && flagSecond == bytes[++i]
                    && flagThird == bytes[++i]
            ) {
                classNameBytes = bytes.copyOfRange(0, i)
                body = bytes.copyOfRange(i + 3, byteSize)
            }
        }

        if (classNameBytes.isEmpty()) return null
        val className = classNameBytes.decodeToString()

        @Suppress("unchecked")
        val schema = RuntimeSchema.getSchema(Class.forName(className)) as Schema<Any>
        return ProtostuffIOUtil.mergeFrom(body, schema.newMessage(), schema)
    }
}
