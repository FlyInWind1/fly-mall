package fly.spring.common.cdc.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpringCacheItem {
    private String cacheName;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private Object key;
}
