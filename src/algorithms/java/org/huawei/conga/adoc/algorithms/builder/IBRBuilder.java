package org.huawei.conga.adoc.algorithms.builder;

import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithms.Executable;
import org.huawei.conga.algorithms.IBRAlgorithm;

public class IBRBuilder implements ExecutableBuilder {

	@Override
	public Executable build(CongaSolver cg) {
		return new IBRAlgorithm(cg);
	}

}
