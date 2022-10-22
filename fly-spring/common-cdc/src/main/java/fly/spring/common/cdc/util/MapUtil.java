package fly.spring.common.cdc.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link org.apache.commons.collections4.MapUtils}
 *
 * @author FlyInWind
 * @since 2022/08/08
 */
public class MapUtil {
    /**
     * Null-safe check if the specified map is empty.
     * <p>
     * Null returns true.
     *
     * @param map the map to check, may be null
     * @return true if empty or null
     * @since 3.2
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Null-safe check if the specified map is not empty.
     * <p>
     * Null returns false.
     *
     * @param map the map to check, may be null
     * @return true if non-null and non-empty
     * @since 3.2
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !MapUtil.isEmpty(map);
    }

    public static <K, V> Map<K, V> fromCollection(Collection<V> collection, Function<V, K> kMapper) {
        return collection.stream()
                .collect(Collectors.toMap(kMapper, Function.identity()));
    }
}
