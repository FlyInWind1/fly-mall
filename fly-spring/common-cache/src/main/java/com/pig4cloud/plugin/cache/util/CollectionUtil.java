package com.pig4cloud.plugin.cache.util;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author FlyInWind
 */
public class CollectionUtil {

	/**
	 * {@link Iterable}转为{@link Collection}<br>
	 * 首先尝试强转，强转失败则构建一个新的{@link ArrayList}
	 * @param <E> 集合元素类型
	 * @param iterable {@link Iterable}
	 * @return {@link Collection} 或者 {@link ArrayList}
	 */
	public static <E> Collection<E> toCollection(Iterable<E> iterable) {
		return (iterable instanceof Collection) ? (Collection<E>) iterable : newArrayList(iterable.iterator());
	}

	/**
	 * 新建一个ArrayList<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 * @param <T> 集合元素类型
	 * @param iterator {@link Iterator}
	 * @return ArrayList对象
	 */
	public static <T> List<T> newArrayList(Iterator<T> iterator) {
		final List<T> list = new ArrayList<>();
		if (null != iterator) {
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
		}
		return list;
	}

	/**
	 * 返回一个仅实现了{@link Collection#iterator()},{@link Collection#size()},{@link Collection#isEmpty()}方法的{@link Collection}。
	 * 不会实际分配内存存储元素，元素在迭代使才通过{@code mapper}计算。
	 * @param collection 集合
	 * @param mapper 映射器
	 * @return {@link Collection}<{@link O}>
	 */
	public static <I, O> Collection<O> mappedCollection(Collection<I> collection, Function<I, O> mapper) {
		return new MappedCollection<>(collection, mapper);
	}

	@AllArgsConstructor
	protected static class MappedCollection<I, O> extends AbstractSimpleCollection<O> {

		protected final Collection<I> originCollection;

		protected final Function<I, O> mapper;

		@Override
		public int size() {
			return originCollection.size();
		}

		@Override
		public boolean isEmpty() {
			return originCollection.isEmpty();
		}

		@Override
		public Iterator<O> iterator() {
			return IteratorUtil.mappedIterator(originCollection.iterator(), mapper);
		}

	}

	/**
	 * 子类只需要实现 {@link #size()} 和 {@link #iterator()} 方法
	 *
	 * @param <O>
	 */
	@SuppressWarnings("NullableProblems")
	protected abstract static class AbstractSimpleCollection<O> implements Collection<O> {

		@Override
		public boolean isEmpty() {
			return size() == 0;
		}

		@Override
		public boolean contains(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object[] toArray() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean add(O o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(Collection<? extends O> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

	}

}
