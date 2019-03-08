package org.huawei.conga.search.policy;


import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;

public class BetterSolutionSearchPolicy implements ISearchPolicy {
	@Override
	public void launchSearch(Solver csp, Solver playerSolver, Agent agent, ISolutionRecorder solutionRecorder) {
		if (agent.hasObjectif()) {
			findAllOptimalSolutions(agent, playerSolver, solutionRecorder);
		} else {
			playerSolver.plugMonitor(solutionRecorder);
			playerSolver.findAllSolutions();
		}

		long solutionCount = playerSolver.getMeasures().getSolutionCount();
		if (solutionCount == 0) {
			onIndifference(csp, playerSolver, agent, solutionRecorder);
		} else {
			agent.reset(playerSolver, csp);
		}
	}

	protected void onIndifference(Solver csp, Solver playerSolver, Agent agent, ISolutionRecorder solutionRecorder) {
		playerSolver.reset();
		agent.reset(playerSolver, csp);
		playerSolver.plugMonitor(solutionRecorder);
		if(playerSolver.solve()){
			while(playerSolver.solve());
		}else{
		}
	}

	protected void findAllOptimalSolutions(final Agent agent, final Solver playerSolver,
			final ISolutionRecorder solutionRecorder) {
		final Variable agentObjective = agent.getObjectiveCSPPlayer()[0].getVariable();
		playerSolver.getModel().setObjective(agent.isMaximisation(), agentObjective);
		boolean canContinue = true;
		boolean isBranchValid = true;
		Number bestbound=null;
		while (isBranchValid && canContinue) {
			canContinue = playerSolver.solve();
			bestbound=playerSolver.getBestSolutionValue();
			isBranchValid = agent.isMaximisation()
					? agent.getObjectiveCSP()[0].getUB() >= agent.getObjectiveCSPPlayer()[0].getLB()
					: agent.getObjectiveCSP()[0].getLB() <= agent.getObjectiveCSPPlayer()[0].getUB();
			if ((canContinue )&& solutionRecorder.getData()!=null) {
				solutionRecorder.getData().clear();
				solutionRecorder.getData().add(getTuple(agent.getIntVariablesCSPPlayer()));
			}
		}
		
		if(isBranchValid&& solutionRecorder.getData()!=null && agent.getObjectiveCSP()[0].isInstantiated() ){
			if(agent.getObjectiveCSP()[0].isInteger()&& bestbound.intValue() == agent.getObjectiveCSP()[0].getLB()){
				solutionRecorder.getData().add(getTuple(agent.getIntVariables()));
			}else if( !agent.getObjectiveCSP()[0].isInteger()&& (bestbound.doubleValue()== agent.getObjectiveCSP()[0].getLB() || bestbound.doubleValue()== agent.getObjectiveCSP()[0].getUB()) ){
				solutionRecorder.getData().add(getTuple(agent.getIntVariables()));
			}
		}
	}

	protected Integer[] getTuple(IntVar[] v) {
		Integer[] tuple = new Integer[v.length];
		for (int i = 0; i < tuple.length; i++) {
			tuple[i] = v[i].getValue();
		}
		return tuple;
	}
}
