package fly.spring.common.cdc.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fly.spring.common.cdc.emus.Operation;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Payload<T> {
    private Operation op;

    private Source source;

    private T before;

    private T after;

    /**
     * 当 {@link #after} 不为 null 时 返回 {@link #after}，否则返回 {@link #before}
     *
     * @return {@link T}
     */
    public T getAfterNullBackBefore() {
        if (after != null) {
            return after;
        }
        return before;
    }

    @Data
    public static class Source {
        private String db;

        private String schema;

        private String table;
    }

}
