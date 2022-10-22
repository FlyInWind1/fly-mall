package fly.spring.common.core.util;

import cn.hutool.core.collection.IterUtil;
import org.apache.commons.collections4.iterators.LazyIteratorChain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 对于{@link org.apache.commons.collections4.IterableUtils}功能的补充
 *
 * @author FlyInWind
 * @see IterableUtil
 * @since 2021/07/26
 */
public class IteratorUtil {

    public static <I, O> Iterator<O> mappedIterator(Iterator<I> iterator, Function<I, O> mapper) {
        return IterUtil.trans(iterator, mapper);
    }

    public static <T> void consumeCollected(Iterator<T> iterator, int batchSize, Consumer<List<T>> consumer) {
        consumeCollected(iterator, batchSize, consumer, () -> new ArrayList<>(batchSize));
    }

    public static <T> Iterator<T> chainedIterator(Iterator<? extends Iterator<T>> iterators) {
        return new LazyIteratorChain<T>() {
            @Override
            protected Iterator<? extends T> nextIterator(int count) {
                if (iterators.hasNext()) {
                    return iterators.next();
                } else {
                    return null;
                }
            }
        };
    }

    public static <T> void consumeCollected(Iterator<T> iterator, int batchSize, Consumer<List<T>> consumer, Supplier<List<T>> listSupplier) {
        Iterator<List<T>> listIterator = collectedIterator(iterator, batchSize, listSupplier);
        listIterator.forEachRemaining(consumer);
    }

    public static <T> void consumeCollected(Iterator<T> iterator, int batchSize, int totalCount, Consumer<List<T>> consumer) {
        Iterator<List<T>> listIterator = collectedIterator(iterator, batchSize, totalCount);
        listIterator.forEachRemaining(consumer);
    }

    public static <T> Iterator<List<T>> collectedIterator(Iterator<T> iterator, int batchSize) {
        return collectedIterator(iterator, batchSize, -1);
    }

    public static <T> Iterator<List<T>> collectedIterator(Iterator<T> iterator, int batchSize, int totalCount) {
        List<T> list;
        if (totalCount != -1) {
            list = new ArrayList<>(Math.min(batchSize, totalCount));
        } else {
            list = new ArrayList<>();
        }
        return collectedIterator(iterator, batchSize, () -> {
            list.clear();
            return list;
        });
    }

    protected static class CollectedIteratorListSupplier<T> implements Supplier<List<T>> {
        protected final int totalCount;

        protected final int batchSize;
        protected int remainderCount;

        protected final Function<Integer, List<T>> listFunction;

        public CollectedIteratorListSupplier(int totalCount, int batchSize) {
            this.totalCount = totalCount;
            this.batchSize = batchSize;
            this.remainderCount = totalCount;
            this.listFunction = ArrayList::new;
        }

        public CollectedIteratorListSupplier(int totalCount, int batchSize, Function<Integer, List<T>> listFunction) {
            this.totalCount = totalCount;
            this.batchSize = batchSize;
            this.remainderCount = totalCount;
            this.listFunction = listFunction;
        }

        @Override
        public List<T> get() {
            int min = Math.min(Math.min(totalCount, batchSize), remainderCount);
            remainderCount = totalCount - batchSize;
            if (remainderCount < 0) {
                remainderCount = 0;
            }
            return new ArrayList<>(min);
        }
    }

    public static <T> Iterator<List<T>> collectedIterator(Iterator<T> iterator, int batchSize, Supplier<List<T>> listSupplier) {
        return new Iterator<List<T>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public List<T> next() {
                List<T> list = listSupplier.get();
                while (list.size() < batchSize && iterator.hasNext()) {
                    T item = iterator.next();
                    list.add(item);
                }
                return list;
            }
        };
    }

    public static <T> void consumeBatched(Iterator<T> iterator, int batchSize, Consumer<Iterator<T>> consumer) {
        BatchedIterator<T> batchedIterator = new BatchedIterator<>(iterator, batchSize);
        while (iterator.hasNext()) {
            batchedIterator.iteratorIdx = 0;
            consumer.accept(batchedIterator);
            if (batchedIterator.iteratorIdx < batchSize) {
                for (int i = 0; i < batchSize - batchedIterator.iteratorIdx; i++) {
                    if (!iterator.hasNext()) {
                        break;
                    }
                    iterator.next();
                }
            }
        }
    }

    private static class BatchedIterator<T> implements Iterator<T> {
        int iteratorIdx = 0;
        final int batchSize;
        final Iterator<T> unBatchedIterator;

        public BatchedIterator(Iterator<T> unBatchedIterator, int batchSize) {
            this.unBatchedIterator = unBatchedIterator;
            this.batchSize = batchSize;
        }

        @Override
        public boolean hasNext() {
            return iteratorIdx != batchSize && unBatchedIterator.hasNext();
        }

        @Override
        public T next() {
            if (iteratorIdx == batchSize) {
                throw new NoSuchElementException();
            }
            iteratorIdx += 1;
            return unBatchedIterator.next();
        }
    }
}
