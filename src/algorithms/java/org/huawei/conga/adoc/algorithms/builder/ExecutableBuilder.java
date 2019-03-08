package org.huawei.conga.adoc.algorithms.builder;

import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithms.Executable;

public interface ExecutableBuilder {

	
	public Executable build(CongaSolver cg);
}
