package org.huawei.conga.equilibria.nash.brrecorder.builder;

import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.equilibria.nash.brrecorder.SolutionRecorderImpl;

public class ISolutionRecorderImplBuilder implements ISolutionRecorderBuilder {

	@Override
	public ISolutionRecorder build() {
		return new SolutionRecorderImpl();
	}

}
