package org.huawei.conga.utils.tuple.provider;

import org.huawei.conga.number.Interval;

public interface TupleProvider {

	Interval[] getTuplePreReal();

	Integer[] getTuplePreInt();

	Interval[] getTuplePostReal();

	Number[] getAndCheckFullPostTuple();

	Number[] getAndCheckFullPreTuple();

	Integer[] getTuplePostInt();

	Number[] getFullPreTuple();

	Number[] getFullPostTuple();
}
