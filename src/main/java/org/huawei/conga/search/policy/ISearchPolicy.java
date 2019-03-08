package org.huawei.conga.search.policy;

import org.chocosolver.solver.Solver;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;

@FunctionalInterface
public interface ISearchPolicy {

	void launchSearch(Solver csp, Solver playerSolver, Agent a, ISolutionRecorder solutionMonitor);
	
	default boolean hasFailedOnIndiference(){return false;}
}
