package fly.spring.common.cdc.util;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

public class MybatisPlusUtil {
    public static String findTableByMyBatisPlus(Class<?> clazz) {
        try {
            Class.forName(TableInfoHelper.class.getName());
            TableInfo table = TableInfoHelper.getTableInfo(clazz);
            if (table != null) {
                return table.getTableName();
            }
        } catch (ClassNotFoundException e) {
            // ignore
        }
        return null;
    }
}
