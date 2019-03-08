package org.huawei.conga.search.policy;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.strategy.IntStrategy;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;

public class LastLevelSearchPolicy implements ISearchPolicy {

	private final IntVar vars[];
	private IntStrategy inputOrderUB, inputOrderLB;

	public LastLevelSearchPolicy( Agent a) {
		vars = new IntVar[a.getNbVars() + 1];
		vars[0] =(IntVar) a.getObjectiveCSPPlayer()[0].getVariable();
		System.arraycopy(a.getIntVariablesCSPPlayer(),0, vars, 1, a.getNbVars());
		inputOrderUB = Search.inputOrderUBSearch(vars);
		inputOrderLB = Search.inputOrderLBSearch(vars);
	}

	@Override
	public void launchSearch(Solver csp, Solver playerSolver, Agent a, ISolutionRecorder solutionMonitor) {
		playerSolver.plugMonitor(solutionMonitor);
		playerSolver.setSearch(a.isMaximisation()? inputOrderUB : inputOrderLB);
		playerSolver.getModel().setObjective(a.isMaximisation(), a.getObjectiveCSPPlayer()[0].getVariable());
		playerSolver.solve();
		a.reset(playerSolver, csp);
	}
}
