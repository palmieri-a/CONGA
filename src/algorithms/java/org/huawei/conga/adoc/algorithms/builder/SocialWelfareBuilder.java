package org.huawei.conga.adoc.algorithms.builder;

import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithm.welfare.function.IWelfareCreator;
import org.huawei.conga.algorithms.Executable;
import org.huawei.conga.algorithms.SocialWelfare;

public class SocialWelfareBuilder implements ExecutableBuilder {

	private IWelfareCreator iwc;

	public SocialWelfareBuilder(IWelfareCreator iwc) {
		this.iwc=iwc;
	}
	
	@Override
	public Executable build(CongaSolver cg) {
		return new SocialWelfare(cg, iwc,iwc.isMaximisation());
	}
}