package fly.spring.common.core.util;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import fly.spring.common.core.cglib.CglibUtil;
import lombok.Data;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.map.UnmodifiableEntrySet;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.proxy.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 为了 {@link fly.spring.common.cache.support.RedisCaffeineCache#unmodifiableBean} 而设计
 */
public class UnmodifiableUtil {
    static long modifiableDuration = TimeUnit.SECONDS.toMillis(10);
    private static volatile long modifiableTimeStamp = -1;

    /**
     * 10 秒内允许修改对象
     */
    public static void modifiableTmp() {
        modifiableTimeStamp = System.currentTimeMillis() + modifiableDuration;
    }

    /**
     * 取消 10 秒内允许修改对象
     */
    public static void unModifiableTmp() {
        modifiableTimeStamp = -1;
    }

    public static boolean isUnModifiableTmp() {
        return modifiableTimeStamp < System.currentTimeMillis();
    }

    /**
     * 创建一个拷贝对象，调用该对象的所有 setXxx 方法，或者修改修改集合对象，将发生异常，用于禁止修改对象属性
     *
     * @param obj obj
     * @param <T> obj type
     * @return 拷贝对象，调用
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmodifiable(T obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Unmodifiable) {
            return obj;
        }
        Class<?> clazz = obj.getClass();
        if (Modifier.isFinal(clazz.getModifiers())) {
            // 不能继承 final 的类
            return obj;
        }
        if (obj instanceof List) {
            List<?> list = ((List<?>) obj).stream()
                    .map(UnmodifiableUtil::unmodifiable)
                    .collect(Collectors.toList());
            return (T) unmodifiableList(list);
        }
        if (obj instanceof Set) {
            Set<?> set = ((Set<?>) obj).stream()
                    .map(UnmodifiableUtil::unmodifiable)
                    .collect(Collectors.toSet());
            return (T) unmodifiableSet(set);
        }
        if (obj instanceof Map) {
            Map<?, ?> source = (Map<?, ?>) obj;
            HashMap<Object, Object> target = MapUtil.newHashMap(source.size());
            source.forEach((k, v) -> target.put(k, unmodifiable(v)));
            return (T) new UnmodifiableMap<>(target);
        }
        String className = clazz.getName();
        if (className.startsWith("java")) {
            return obj;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setInterfaces(new Class[]{UnmodifiableBean.class});
        enhancer.setCallbackFilter(CONVERT_NOT_SETTABLE_FILTER);
        UnmodifiableCallback callback = new UnmodifiableCallback();
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, callback});
        //noinspection unchecked
        T result = (T) enhancer.create();
        modifiableTmp();
        CglibUtil.copy(obj, result, clazz, clazz, CONVERTER);
        return result;
    }

    static final Converter CONVERTER = (o, aClass, o1) -> unmodifiable(o);

    static final String SET = "set";

    static final CallbackFilter CONVERT_NOT_SETTABLE_FILTER = method -> {
        if (method.getName().startsWith(SET)) {
            return 1;
        }
        return 0;
    };

    @Data
    static class UnmodifiableCallback implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
            if (isUnModifiableTmp()) {
                throw new UnsupportedOperationException("对象禁止修改");
            }
            return methodProxy.invokeSuper(o, params);
        }
    }

    public interface UnmodifiableBean extends Unmodifiable {
    }

    /**
     * @serial include
     */
    static class UnmodifiableCollection<E> implements Collection<E>, Serializable, Unmodifiable {

        @SuppressWarnings("serial") // Conditionally serializable
        final Collection<E> c;

        @JsonCreator
        UnmodifiableCollection(Collection<? extends E> c) {
            if (c == null)
                throw new NullPointerException();
            this.c = (Collection<E>) c;
        }

        public int size() {
            return c.size();
        }

        public boolean isEmpty() {
            return c.isEmpty();
        }

        public boolean contains(Object o) {
            return c.contains(o);
        }

        public Object[] toArray() {
            return c.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return c.toArray(a);
        }

        /*public <T> T[] toArray(IntFunction<T[]> f) {
            return c.toArray(f);
        }*/

        public String toString() {
            return c.toString();
        }

        public Iterator<E> iterator() {
            return new Iterator<E>() {
                private final Iterator<? extends E> i = c.iterator();

                public boolean hasNext() {
                    return i.hasNext();
                }

                public E next() {
                    return i.next();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void forEachRemaining(Consumer<? super E> action) {
                    // Use backing collection version
                    i.forEachRemaining(action);
                }
            };
        }

        public boolean add(E e) {
            if (isUnModifiableTmp()) {
                throw new UnsupportedOperationException("对象禁止修改");
            }
            return c.add(unmodifiable(e));
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection<?> coll) {
            return c.containsAll(coll);
        }

        public boolean addAll(Collection<? extends E> coll) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> coll) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> coll) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        // Override default methods in Collection
        @Override
        public void forEach(Consumer<? super E> action) {
            c.forEach(action);
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Spliterator<E> spliterator() {
            return (Spliterator<E>) c.spliterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Stream<E> stream() {
            return (Stream<E>) c.stream();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Stream<E> parallelStream() {
            return (Stream<E>) c.parallelStream();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        if (list.getClass() == UnmodifiableList.class || list.getClass() == UnmodifiableRandomAccessList.class) {
            return (List<T>) list;
        }

        return (list instanceof RandomAccess ?
                new UnmodifiableRandomAccessList<>(list) :
                new UnmodifiableList<>(list));
    }

    /**
     * @serial include
     */
    static class UnmodifiableList<E> extends UnmodifiableCollection<E>
            implements List<E> {
        @SuppressWarnings("serial") // Conditionally serializable
        final List<? extends E> list;

        @JsonCreator
        UnmodifiableList(List<? extends E> list) {
            super(list);
            this.list = list;
        }

        public boolean equals(Object o) {
            return o == this || list.equals(o);
        }

        public int hashCode() {
            return list.hashCode();
        }

        public E get(int index) {
            return list.get(index);
        }

        public E set(int index, E element) {
            throw new UnsupportedOperationException();
        }

        public void add(int index, E element) {
            throw new UnsupportedOperationException();
        }

        public E remove(int index) {
            throw new UnsupportedOperationException();
        }

        public int indexOf(Object o) {
            return list.indexOf(o);
        }

        public int lastIndexOf(Object o) {
            return list.lastIndexOf(o);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(UnaryOperator<E> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sort(Comparator<? super E> c) {
            throw new UnsupportedOperationException();
        }

        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        public ListIterator<E> listIterator(final int index) {
            return new ListIterator<E>() {
                private final ListIterator<? extends E> i
                        = list.listIterator(index);

                public boolean hasNext() {
                    return i.hasNext();
                }

                public E next() {
                    return i.next();
                }

                public boolean hasPrevious() {
                    return i.hasPrevious();
                }

                public E previous() {
                    return i.previous();
                }

                public int nextIndex() {
                    return i.nextIndex();
                }

                public int previousIndex() {
                    return i.previousIndex();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                public void set(E e) {
                    throw new UnsupportedOperationException();
                }

                public void add(E e) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void forEachRemaining(Consumer<? super E> action) {
                    i.forEachRemaining(action);
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            return new UnmodifiableList<>(list.subList(fromIndex, toIndex));
        }

        /**
         * UnmodifiableRandomAccessList instances are serialized as
         * UnmodifiableList instances to allow them to be deserialized
         * in pre-1.4 JREs (which do not have UnmodifiableRandomAccessList).
         * This method inverts the transformation.  As a beneficial
         * side-effect, it also grafts the RandomAccess marker onto
         * UnmodifiableList instances that were serialized in pre-1.4 JREs.
         * <p>
         * Note: Unfortunately, UnmodifiableRandomAccessList instances
         * serialized in 1.4.1 and deserialized in 1.4 will become
         * UnmodifiableList instances, as this method was missing in 1.4.
         */
        //@java.io.Serial
        private Object readResolve() {
            return (list instanceof RandomAccess
                    ? new UnmodifiableRandomAccessList<>(list)
                    : this);
        }
    }

    /**
     * @serial include
     */
    static class UnmodifiableRandomAccessList<E> extends UnmodifiableList<E>
            implements RandomAccess {
        @JsonCreator
        UnmodifiableRandomAccessList(List<? extends E> list) {
            super(list);
        }

        public List<E> subList(int fromIndex, int toIndex) {
            return new UnmodifiableRandomAccessList<>(
                    list.subList(fromIndex, toIndex));
        }

        //@java.io.Serial
        private static final long serialVersionUID = -2542308836966382001L;

        /**
         * Allows instances to be deserialized in pre-1.4 JREs (which do
         * not have UnmodifiableRandomAccessList).  UnmodifiableList has
         * a readResolve method that inverts this transformation upon
         * deserialization.
         */
        //@java.io.Serial
        private Object writeReplace() {
            return new UnmodifiableList<>(list);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> unmodifiableSet(Set<? extends T> s) {
        // Not checking for subclasses because of heap pollution and information leakage.
        if (s.getClass() == UnmodifiableSet.class) {
            return (Set<T>) s;
        }
        return new UnmodifiableSet<>(s);
    }

    /**
     * @serial include
     */
    static class UnmodifiableSet<E> extends UnmodifiableCollection<E>
            implements Set<E>, Serializable {
        //@java.io.Serial
        private static final long serialVersionUID = -9215047833775013803L;

        @JsonCreator
        UnmodifiableSet(Set<? extends E> s) {
            super(s);
        }

        public boolean equals(Object o) {
            return o == this || c.equals(o);
        }

        public int hashCode() {
            return c.hashCode();
        }
    }

    /**
     * @see Collections#unmodifiableMap
     */
    private static final class UnmodifiableMap<K, V> implements Map<K, V>, Serializable, Unmodifiable {

        private final Map<K, V> m;

        @JsonCreator
        UnmodifiableMap(Map<? extends K, ? extends V> m) {
            if (m == null)
                throw new NullPointerException();
            this.m = (Map<K, V>) m;
        }

        public int size() {
            return m.size();
        }

        public boolean isEmpty() {
            return m.isEmpty();
        }

        public boolean containsKey(Object key) {
            return m.containsKey(key);
        }

        public boolean containsValue(Object val) {
            return m.containsValue(val);
        }

        public V get(Object key) {
            return m.get(key);
        }

        public V put(K key, V value) {
            if (isUnModifiableTmp()) {
                throw new UnsupportedOperationException("对象禁止修改");
            }
            return m.put(key, value);
        }

        public V remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        private transient Set<K> keySet;
        private transient Set<Entry<K, V>> entrySet;
        private transient Collection<V> values;

        public Set<K> keySet() {
            if (keySet == null)
                keySet = unmodifiableSet(m.keySet());
            return keySet;
        }

        public Set<Entry<K, V>> entrySet() {
            if (entrySet == null)
                entrySet = UnmodifiableEntrySet.unmodifiableEntrySet(m.entrySet());
            return entrySet;
        }

        public Collection<V> values() {
            if (values == null)
                values = new UnmodifiableCollection<>(m.values());
            return values;
        }

        public boolean equals(Object o) {
            return o == this || m.equals(o);
        }

        public int hashCode() {
            return m.hashCode();
        }

        public String toString() {
            return m.toString();
        }

        // Override default methods in Map
        @Override
        @SuppressWarnings("unchecked")
        public V getOrDefault(Object k, V defaultValue) {
            // Safe cast as we don't change the value
            return ((Map<K, V>) m).getOrDefault(k, defaultValue);
        }

        @Override
        public void forEach(BiConsumer<? super K, ? super V> action) {
            m.forEach(action);
        }

        @Override
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V putIfAbsent(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V replace(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
            if (isUnModifiableTmp()) {
                throw new UnsupportedOperationException("对象禁止修改");
            }
            return m.computeIfAbsent(key, mappingFunction);
        }

        @Override
        public V computeIfPresent(K key,
                                  BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V compute(K key,
                         BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V merge(K key, V value,
                       BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
