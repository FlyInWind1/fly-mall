import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Test
import java.util.*

class Test1 {
    @Test
    fun test1(): Unit {
        val properties = Properties().apply {
            put("bootstrap.servers", "localhost:9092")
            put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//            put("acks", "all")
//            put("bootstrap.servers", "localhost:9092")
        }
        val producer = KafkaProducer<String,String>(properties)
        for (i in 1..100)
            producer.send(ProducerRecord("test", i.toString()))
        producer.close()
    }

    @Test
    fun a(): Unit {
        val objectMapper = ObjectMapper()
        objectMapper.activateDefaultTyping(
            objectMapper.polymorphicTypeValidator,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )
//        val string = objectMapper.writeValueAsString(objectMapper.createObjectNode().put("a", "a"))
        val string = objectMapper.writeValueAsString(arrayListOf("1","b"))
        print(string)
    }

}
