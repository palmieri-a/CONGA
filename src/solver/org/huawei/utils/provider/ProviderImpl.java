package org.huawei.utils.provider;

/**
 * Proxy used to set easily accessed properties
 * 
 * @author a80048597
 *
 * @param <T>
 */
public class ProviderImpl<T> implements Provider<T> {

	private T elem;

	@Override
	public T get() {
		return elem;
	}

	@Override
	public void set(T elem) {
		this.elem = elem;
	}

	@Override
	public boolean isNull() {
		return elem == null;
	}

}
