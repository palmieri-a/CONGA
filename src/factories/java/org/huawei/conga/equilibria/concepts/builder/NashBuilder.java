package org.huawei.conga.equilibria.concepts.builder;

import org.chocosolver.solver.Solver;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.concepts.NashEquilibriumConcept;
import org.huawei.conga.equilibria.nash.brrecorder.builder.ISolutionRecorderBuilder;

public class NashBuilder implements EqConceptProvider {

	@Override
	public IEquilibriumConcept build(Solver cspPlayer,CongaSolver conga, ISolutionRecorderBuilder recorderB) {
		return new NashEquilibriumConcept(cspPlayer, conga, recorderB.build());
	}

}
