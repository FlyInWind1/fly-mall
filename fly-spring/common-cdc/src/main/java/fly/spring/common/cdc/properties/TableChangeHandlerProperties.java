package fly.spring.common.cdc.properties;

import fly.spring.common.cdc.handler.AbstractTableChangeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * debezium表更新属性
 *
 * @author FlyInWind
 * @date 2022/01/11
 */
@Data
@AllArgsConstructor
@SuppressWarnings("JavadocReference")
public class TableChangeHandlerProperties {
    /** 表名 */
    private String tableName;

    public TableChangeHandlerProperties(String tableName) {
        this.tableName = tableName;
    }

    public TableChangeHandlerProperties(String tableName, String... keyFields) {
        this.tableName = tableName;
        this.keyFields = Arrays.asList(keyFields);
    }

    /** id字段名，参考{@link AbstractTableChangeHandler#readItemId} */
    private String idProperty = "id";

    /**
     * key实体类字段名，如t_fonds表是fondsCode。
     * 有多个字段的，可以通过重写{@link AbstractTableChangeHandler#readItemKey}实现。
     * <p>
     * 请时刻注意字段的顺序。
     */
    private List<String> keyFields = Collections.singletonList("id");

    public TableChangeHandlerProperties setKeyFields(String... keyFields) {
        this.keyFields = Arrays.asList(keyFields);
        return this;
    }

    /** deleteFlag字段名，没有逻辑删除的请忽略这个字段 */
    private String deleteFlagProperty = "deleteFlag";
}
