package fly.spring.common.cdc.interfaces;

import fly.spring.common.cdc.domain.Payload;

import java.util.List;

/**
 * 修改事件处理接口
 *
 * @author FlyInWind
 * @since 2022/08/08
 */
public interface ChangeHandler<T> {

    /**
     * 处理变化的记录
     *
     * @param payloads 变化的记录
     */
    void handleEntityBatch(List<Payload<T>> payloads);

}
