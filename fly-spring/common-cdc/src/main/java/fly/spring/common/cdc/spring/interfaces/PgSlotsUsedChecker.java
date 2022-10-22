package fly.spring.common.cdc.spring.interfaces;

import java.util.function.Predicate;

/**
 * 用于校验 slot 槽是否被占用
 */
public interface PgSlotsUsedChecker extends Predicate<String> {

}
