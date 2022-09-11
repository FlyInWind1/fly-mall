package fly.spring.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import fly.spring.common.constant.DatePattern
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.format.DateTimeFormatter

@Configuration
@ConditionalOnClass(ObjectMapper::class)
@AutoConfigureBefore(JacksonAutoConfiguration::class)
class JacksonConfiguration {

    @Bean
    @Primary
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return builder.createXmlMapper(false).build<ObjectMapper>()
    }

    @Bean
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder ->

            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)

            val dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)
            val dateFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)
            val timeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)
            builder.serializers(
                    LocalDateTimeSerializer(dateTimeFormatter),
                    LocalDateSerializer(dateFormatter),
                    LocalTimeSerializer(timeFormatter))
            builder.deserializers(
                    LocalDateTimeDeserializer(dateTimeFormatter),
                    LocalDateDeserializer(dateFormatter),
                    LocalTimeDeserializer(timeFormatter))
        }
    }

}