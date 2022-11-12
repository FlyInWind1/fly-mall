package fly.spring.common.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
@ConditionalOnClass(ObjectMapper::class)
class JacksonConfiguration {

    companion object {
        const val OBJECT_MAPPER = "objectMapper"
        const val OBJECT_MAPPER_DEFAULT_TYPING = "objectMapperDefaultTyping"
        const val OBJECT_MAPPER_SNAKE_CASE = "objectMapperSnakeCase"
        const val OBJECT_WRITER_PRETTY = "objectWriterPretty"
    }

    @Primary
    @Bean(OBJECT_MAPPER)
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return builder.build<ObjectMapper>()
    }


    @Bean(OBJECT_MAPPER_DEFAULT_TYPING)
    fun objectMapperDefaultTyping(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        val objectMapper = builder.build<ObjectMapper>()
        objectMapper.activateDefaultTyping(
            objectMapper.polymorphicTypeValidator,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )
        return objectMapper
    }

    @Bean(OBJECT_MAPPER_SNAKE_CASE)
    fun objectMapperSnakeCase(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        val objectMapper = builder.build<ObjectMapper>()
        objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
        return objectMapper
    }

    @Bean(OBJECT_WRITER_PRETTY)
    fun objectWriterPretty(objectMapper: ObjectMapper): ObjectWriter {
        return objectMapper.writerWithDefaultPrettyPrinter()
    }
}
