package fly.spring.common.cdc.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fly.spring.common.cdc.emus.Operation;
import fly.spring.common.core.util.IterableUtil;
import lombok.Data;
import org.apache.commons.collections4.IterableUtils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Payload<T> implements Iterable<T> {
    private Operation op;

    private Source source;

    private T before;

    private T after;

    /**
     * 当 {@link #after} 不为 null 时 返回 {@link #after}，否则返回 {@link #before}
     *
     * @return {@link T}
     */
    public T getAfterNullBackBefore() {
        if (after != null) {
            return after;
        }
        return before;
    }

    @Data
    public static class Source {
        private String db;

        private String schema;

        private String table;
    }

    /**
     * @return 先迭代 {@link #before} 再迭代 {@link #after}
     */
    @Override
    public Iterator<T> iterator() {
        return new EntityIterator();
    }

    protected class EntityIterator implements Iterator<T> {
        protected byte flag = 0;

        @Override
        public boolean hasNext() {
            return flag == 0 || flag == 1;
        }

        @Override
        public T next() {
            if (flag == 0) {
                flag++;
                return before;
            }
            if (flag == 1) {
                flag++;
                return after;
            }
            throw new NoSuchElementException();
        }
    }

    /**
     * 迭代所有非 null 的 {@link #before} 和 {@link #after}
     *
     * @param payloads payload
     * @param <T>      T
     * @return {@link Iterable}<{@link T}>
     */
    public static <T> Iterable<T> nonNullIterable(Iterable<Payload<T>> payloads) {
        Iterable<T> iterable = IterableUtil.chainedIterable(payloads);
        return IterableUtils.filteredIterable(iterable, Objects::nonNull);
    }
}
