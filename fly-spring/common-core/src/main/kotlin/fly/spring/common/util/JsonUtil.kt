package fly.spring.common.util

import cn.hutool.extra.spring.SpringUtil
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import fly.spring.common.config.JacksonConfiguration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


class JsonUtil {
    companion object {
        @JvmStatic
        val objectMapper: ObjectMapper

        /** 下划线转驼峰  */
        @JvmStatic
        val objectMapperSnakeCase: ObjectMapper

        /** 添加@class类型信息  */
        @JvmStatic
        val objectMapperDefaultTyping: ObjectMapper

        /** 以美观的方式打印  */
        @JvmStatic
        val objectWriterPretty: ObjectWriter

        init {
            if (SpringUtil.getBeanFactory() != null) {
                objectMapper = SpringUtil.getBean(JacksonConfiguration.OBJECT_MAPPER)
                objectMapperSnakeCase = SpringUtil.getBean(JacksonConfiguration.OBJECT_MAPPER_SNAKE_CASE)
                objectMapperDefaultTyping = SpringUtil.getBean(JacksonConfiguration.OBJECT_MAPPER_DEFAULT_TYPING)
                objectWriterPretty = SpringUtil.getBean(JacksonConfiguration.OBJECT_WRITER_PRETTY)
            } else {
                // 非spring环境
                val configuration = JacksonConfiguration()
                val builder = Jackson2ObjectMapperBuilder()
                objectMapper = configuration.objectMapper(builder)
                objectMapperSnakeCase = configuration.objectMapperSnakeCase(builder)
                objectMapperDefaultTyping = configuration.objectMapperDefaultTyping(builder)
                objectWriterPretty = configuration.objectWriterPretty(objectMapper)
            }
        }

        @JvmStatic
        fun writeValueAsString(o: Any): String {
            return objectMapper.writeValueAsString(o)
        }

        /**
         * 美观打印，生成的json字符串会自动换行
         *
         * @param o o
         * @return [String]
         * @throws JsonProcessingException json处理异常
         */
        @JvmStatic
        fun writeValueAsStringPretty(o: Any): String {
            return objectWriterPretty.writeValueAsString(o)
        }

        @JvmStatic
        fun readTree(content: String): JsonNode {
            return objectMapper.readTree(content)
        }

        @JvmStatic
        fun <T> readTree(jsonNode: JsonNode, clazz: Class<T>): T {
            return objectMapper.treeToValue(jsonNode, clazz)
        }

        @JvmStatic
        fun <T> readValue(content: String, clazz: Class<T>): T {
            return objectMapper.readValue(content, clazz)
        }

        @JvmStatic
        fun <T> readValue(content: String, typeReference: TypeReference<T>): T {
            return objectMapper.readValue(content, typeReference)
        }

        @JvmStatic
        fun <T> readValue(content: String, javaType: JavaType): T {
            return objectMapper.readValue(content, javaType)
        }

        @JvmStatic
        fun constructType(typeReference: TypeReference<*>): JavaType {
            return objectMapper.typeFactory.constructType(typeReference)
        }

    }
}
