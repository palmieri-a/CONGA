package org.huawei.conga.equilibria.concepts.builder;

import org.chocosolver.solver.Solver;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.nash.brrecorder.builder.ISolutionRecorderBuilder;

@FunctionalInterface
public interface EqConceptProvider {
	IEquilibriumConcept build(Solver csp,CongaSolver conga, ISolutionRecorderBuilder recorderB);
}
