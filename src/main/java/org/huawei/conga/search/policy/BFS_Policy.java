package org.huawei.conga.search.policy;

import org.chocosolver.solver.Solver;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;

public class BFS_Policy implements ISearchPolicy {

	@Override
	public void launchSearch(Solver csp, Solver playerSolver, Agent a, ISolutionRecorder solutionMonitor) {
		 
		a.addAllStopCriterion(playerSolver);
		playerSolver.plugMonitor(solutionMonitor);
		boolean canContinue = true;
		boolean isBranchValid = true;
		while (isBranchValid && canContinue) {
			canContinue = playerSolver.solve();
			isBranchValid=a.isMaximisation()? a.getObjectiveCSP()[0].getUB() >= a.getObjectiveCSPPlayer()[0].getLB():a.getObjectiveCSP()[0].getLB() <= a.getObjectiveCSPPlayer()[0].getUB();
		}
		a.reset(playerSolver, csp);
	}

}