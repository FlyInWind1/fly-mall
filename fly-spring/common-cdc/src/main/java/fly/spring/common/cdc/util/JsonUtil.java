package fly.spring.common.cdc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;

public class JsonUtil {
    public static ObjectMapper objectMapper;
    public static ObjectWriter objectWriterPretty;

    static {
        objectMapper = fly.spring.common.util.JsonUtil.getObjectMapperSnakeCase();
        objectWriterPretty = objectMapper.writerWithDefaultPrettyPrinter();
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String writeValueAsStringPretty(Object obj) {
        return objectWriterPretty.writeValueAsString(obj);
    }
}
