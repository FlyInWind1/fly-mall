package fly.spring.common.cdc.util;

import io.debezium.util.Strings;

public class StringUtil {

    /**
     * 字符串驼峰转下划线格式
     * {@link com.baomidou.mybatisplus.core.toolkit.StringUtils#camelToUnderline}
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (Strings.isNullOrBlank(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
