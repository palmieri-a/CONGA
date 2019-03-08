package org.huawei.utils.provider;

public interface Provider<T> {

	T get();

	void set(T elem);

	boolean isNull();

}
