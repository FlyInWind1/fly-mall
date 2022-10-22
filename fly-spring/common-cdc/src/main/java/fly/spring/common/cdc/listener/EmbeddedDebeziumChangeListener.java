package fly.spring.common.cdc.listener;

import fly.spring.common.cdc.domain.Payload;
import fly.spring.common.cdc.interfaces.ChangeHandler;
import fly.spring.common.cdc.properties.EmbeddedDebeziumChangeListenerProperties;

import java.util.List;

/**
 * 基于嵌入式 debezium 的表变化监听器
 *
 * @author FlyInWind
 * @see AbstractEmbeddedDebeziumChangeListener
 * @since 2022/08/09
 */
public class EmbeddedDebeziumChangeListener<T> extends AbstractEmbeddedDebeziumChangeListener<T> {
    protected final ChangeHandler<T> changeHandler;

    protected EmbeddedDebeziumChangeListener(EmbeddedDebeziumChangeListenerProperties embeddedDebeziumChangeListenerProperties, ChangeHandler<T> changeHandler) {
        super(embeddedDebeziumChangeListenerProperties);
        this.changeHandler = changeHandler;
    }

    @Override
    public void handleEntityBatch(List<Payload<T>> payloads) {
        changeHandler.handleEntityBatch(payloads);
    }
}
