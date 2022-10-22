package fly.spring.common.cdc.domain;

import com.fasterxml.jackson.databind.JavaType;
import io.debezium.relational.TableId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableTypeMeta {

    protected final TableId tableId;

    protected final JavaType entityType;

    protected final JavaType payloadType;

}
