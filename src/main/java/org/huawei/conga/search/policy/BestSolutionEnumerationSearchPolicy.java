package org.huawei.conga.search.policy;

import org.chocosolver.solver.Cause;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.ESat;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;

public class BestSolutionEnumerationSearchPolicy implements ISearchPolicy {

	private ObjPropagator cstrObj;
	

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
		while (playerSolver.solve())
			;
	}

	protected void findAllOptimalSolutions(final Agent agent, final Solver playerSolver,
			final ISolutionRecorder solutionRecorder) {
		
		final Variable agentObjective = agent.getObjectiveCSPPlayer()[0].getVariable();
		playerSolver.getModel().setObjective(agent.isMaximisation(), agentObjective);
		playerSolver.showSolutions();
		if ((playerSolver.solve())) {
			while (playerSolver.solve()) {
			}
			;// find optimal solution
			final Number optValLB = playerSolver.getObjectiveManager().getBestLB();
			final Number optValUB = playerSolver.getObjectiveManager().getBestUB();
			playerSolver.getModel().clearObjective();
			cstrObj = new ObjPropagator(agent.getTupleVarPlayerCSP());
			this.cstrObj.init(agentObjective, optValLB, optValUB);
			playerSolver.reset();
			Constraint constraint = new Constraint("obj",cstrObj);
			playerSolver.getModel().post(constraint);
			playerSolver.plugMonitor(solutionRecorder);
			while (playerSolver.solve());
			playerSolver.getModel().unpost(constraint);
		}
	}

	private class ObjPropagator extends Propagator<Variable> {

		private Variable agentObjective;
		private Number optValLB;
		private Number optValUB;

		boolean hasPropagate = false;

		public void init(Variable agentObjective, Number optValLB, Number optValUB) {
			this.agentObjective = agentObjective;
			this.optValLB = optValLB;
			this.optValUB = optValUB;
		}

		public ObjPropagator(Variable[] scope) {
			super(scope, PropagatorPriority.UNARY, false);
		}

		@Override
		public void propagate(int evtmask) throws ContradictionException {
			if ((agentObjective.getTypeAndKind() & Variable.REAL) != 0) {
				((RealVar) agentObjective).updateBounds(optValLB.doubleValue(), optValUB.doubleValue(), Cause.Null);
			} else {
				((IntVar) agentObjective).updateBounds(optValLB.intValue(), optValUB.intValue(), Cause.Null);
			}
			setPassive();
		}

		@Override
		public ESat isEntailed() {
			if (!hasPropagate) {
				return ESat.UNDEFINED;
			}
			return ESat.TRUE;
		}

	}

}
