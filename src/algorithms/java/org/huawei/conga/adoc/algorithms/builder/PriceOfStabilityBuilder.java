package org.huawei.conga.adoc.algorithms.builder;

import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithm.welfare.function.IWelfareCreator;
import org.huawei.conga.algorithms.Executable;
import org.huawei.conga.algorithms.PriceAlgorithm;

public class PriceOfStabilityBuilder implements ExecutableBuilder {
	private IWelfareCreator iwc;

	public PriceOfStabilityBuilder (IWelfareCreator iwc) {
		this.iwc=iwc;
	}
	
	@Override
	public Executable build(CongaSolver cg) {
		return new PriceAlgorithm(cg, iwc, false);
	}

}
