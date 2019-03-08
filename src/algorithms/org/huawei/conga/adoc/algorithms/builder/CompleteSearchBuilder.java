package org.huawei.conga.adoc.algorithms.builder;

import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithms.Executable;
import org.huawei.conga.algorithms.Nash;

public class CompleteSearchBuilder implements ExecutableBuilder {

	@Override
	public Executable build(CongaSolver cg) {
		return new Nash(cg);
	}

}
