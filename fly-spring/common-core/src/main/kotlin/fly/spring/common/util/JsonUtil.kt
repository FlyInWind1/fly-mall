package fly.spring.common.util

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import fly.spring.common.config.JacksonConfiguration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


class JsonUtil {
    companion object {
        val OBJECT_MAPPER: ObjectMapper

        /** 下划线转驼峰  */
        @JvmStatic
        val objectMapperSnakeCase: ObjectMapper

        /** 添加@class类型信息  */
        val OBJECT_MAPPER_DEFAULT_TYPING: ObjectMapper

        /** 以美观的方式打印  */
        val OBJECT_WRITER_PRETTY: ObjectWriter

        init {
            OBJECT_MAPPER = SpringUtil.getBean(JacksonConfiguration.OBJECT_MAPPER)
            val builder = SpringUtil.getBean(Jackson2ObjectMapperBuilder::class.java)
            objectMapperSnakeCase = builder.build()
            OBJECT_MAPPER_DEFAULT_TYPING = SpringUtil.getBean(JacksonConfiguration.OBJECT_MAPPER_DEFAULT_TYPING)

            //不序列化null字段
            objectMapperSnakeCase.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            OBJECT_MAPPER_DEFAULT_TYPING.activateDefaultTyping(
                OBJECT_MAPPER_DEFAULT_TYPING.polymorphicTypeValidator,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
            )
            OBJECT_WRITER_PRETTY = OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
        }

        fun writeValueAsString(o: Any): String {
            return OBJECT_MAPPER.writeValueAsString(o)
        }

        /**
         * 美观打印，生成的json字符串会自动换行
         *
         * @param o o
         * @return [String]
         * @throws JsonProcessingException json处理异常
         */
        fun writeValueAsStringPretty(o: Any): String {
            return OBJECT_WRITER_PRETTY.writeValueAsString(o)
        }

        fun readTree(content: String): JsonNode {
            return OBJECT_MAPPER.readTree(content)
        }

        fun <T> readTree(jsonNode: JsonNode, clazz: Class<T>): T {
            return OBJECT_MAPPER.treeToValue(jsonNode, clazz)
        }

        fun <T> readValue(content: String, clazz: Class<T>): T {
            return OBJECT_MAPPER.readValue(content, clazz)
        }

        fun <T> readValue(content: String, typeReference: TypeReference<T>): T {
            return OBJECT_MAPPER.readValue(content, typeReference)
        }

        fun <T> readValue(content: String, javaType: JavaType): T {
            return OBJECT_MAPPER.readValue(content, javaType)
        }

        fun constructType(typeReference: TypeReference<*>): JavaType {
            return OBJECT_MAPPER.typeFactory.constructType(typeReference)
        }

    }
}
