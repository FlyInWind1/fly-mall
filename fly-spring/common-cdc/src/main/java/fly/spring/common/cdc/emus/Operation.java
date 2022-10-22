package fly.spring.common.cdc.emus;

/**
 * {@link io.debezium.data.Envelope.Operation}
 */
public enum Operation {
    r,
    /** 新增 */ c,
    /** 更新 */ u,
    /** 删除 */ d,
    t,
    m,
}
