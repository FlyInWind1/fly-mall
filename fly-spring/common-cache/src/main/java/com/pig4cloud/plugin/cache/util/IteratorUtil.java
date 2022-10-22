package com.pig4cloud.plugin.cache.util;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author FlyInWind
 */
public class IteratorUtil {

	public static <I, O> Iterator<O> mappedIterator(Iterator<I> iterator, Function<I, O> mapper) {
		return new MappedIterator<>(iterator, mapper);
	}

	@AllArgsConstructor
	protected static class MappedIterator<I, O> implements Iterator<O> {

		protected final Iterator<I> originalIterator;

		protected final Function<I, O> mapper;

		@Override
		public boolean hasNext() {
			return originalIterator.hasNext();
		}

		@Override
		public O next() {
			return mapper.apply(originalIterator.next());
		}

	}

}
