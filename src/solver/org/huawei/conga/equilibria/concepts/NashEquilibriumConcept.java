package org.huawei.conga.equilibria.concepts;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.equilibria.constraint.agent.PrefixFixer;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.datastructure.table.TableTree;

public class NashEquilibriumConcept implements IEquilibriumConcept {

	protected Solver cspPlayer, csp;
	protected CongaSolver conga;
	protected ISolutionRecorder solutionMonitor;
	private PrefixFixer prefixProg;

	public NashEquilibriumConcept(Solver cspPlayer, CongaSolver conga, ISolutionRecorder solutionRecorderBuilder) {
		this.cspPlayer = cspPlayer;
		this.csp = conga.getSolver();
		this.conga = conga;
		solutionMonitor = solutionRecorderBuilder;
		prefixProg = new PrefixFixer(cspPlayer.getModel().getVars());
		cspPlayer.getModel().post(new Constraint("prefix", prefixProg));
	}

	@Override
	public void initCSP(Solver csp, Solver playerSolver) {
		this.csp = csp;
		this.cspPlayer = playerSolver;
	}

	@Override
	public TableTree check(int owner) {
		return check(owner, conga.getBrPolicies(owner));
	}

	@Override
	public TableTree check(int owner, ISearchPolicy policy) {
		Agent agent = conga.getAgents()[owner];
		agent.initSolver(this.cspPlayer);
		prefixProg.setPrefix(agent.getTupleIntVarCSP(), agent.getTupleRealVarCSP(), agent.getTupleIntVarPlayerCSP(),
				agent.getTupleRealVarPlayerCSP());
		solutionMonitor.init(agent);
		policy.launchSearch(csp, cspPlayer, agent, solutionMonitor);
		cspPlayer.unplugMonitor(solutionMonitor);
		cspPlayer.reset();
		return solutionMonitor.getData();
	}

	@Override
	public void setSolutionMonitor(ISolutionRecorder solutionMonitor) {
		this.solutionMonitor = solutionMonitor;
	}

	@Override
	public ISolutionRecorder getSolutionRecorder() {
		return this.solutionMonitor;
	}
}
