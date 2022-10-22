package fly.spring.common.cdc.util;

import io.debezium.relational.TableId;
import io.debezium.util.Strings;

public class TableIdUtil {

    /**
     * 保证表名有 public. 前缀
     *
     * @param tableName 表名
     * @return {@link TableId}
     */
    public static TableId parseTableIdWithPublicPrefix(String tableName) {
        TableId tableId = TableId.parse(tableName);
        if (Strings.isNullOrBlank(tableId.schema())) {
            tableId = new TableId(tableId.catalog(), "public", tableId.table());
        }
        return tableId;
    }

    public static String getTableNameWithOutPublicPrefix(TableId tableId) {
        if ("public".equals(tableId.schema())) {
            return tableId.table();
        } else {
            return tableId.toString();
        }
    }

}
