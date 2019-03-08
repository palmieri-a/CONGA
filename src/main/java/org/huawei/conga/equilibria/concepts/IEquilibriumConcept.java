package org.huawei.conga.equilibria.concepts;

import org.chocosolver.solver.Solver;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.datastructure.table.TableTree;

public interface IEquilibriumConcept {



	void setSolutionMonitor(ISolutionRecorder solutionMonitor);

	void initCSP(Solver csp, Solver playerSolver);

	ISolutionRecorder getSolutionRecorder();

	TableTree check(int owner);

	TableTree check(int owner, ISearchPolicy policy);
}
