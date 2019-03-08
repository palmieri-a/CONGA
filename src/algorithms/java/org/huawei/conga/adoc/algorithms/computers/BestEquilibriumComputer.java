package org.huawei.conga.adoc.algorithms.computers;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithm.welfare.function.IWelfareCreator;

public class BestEquilibriumComputer implements IComputer {

	private double best;
	private CongaSolver cg;
	private IWelfareCreator iwc;
	private boolean maximize;

	public BestEquilibriumComputer(CongaSolver cg, IWelfareCreator iwc, boolean maximize) {
		this.cg = cg;
		this.iwc = iwc;
		this.maximize = maximize;
	}

	@Override
	public void execute() {
		Solver s = cg.prepareAndGetSolver();
		Variable opt = iwc.buildFunction(cg.getAgents(), s.getModel());
		// if is arnarchy => min equilibria
		s.getModel().setObjective(maximize, opt);
		while (s.solve())
			;
		if ((opt.getTypeAndKind() & Variable.INT) != 0) {
			best = s.getBestSolutionValue().intValue();
		} else {
			best = s.getBestSolutionValue().doubleValue();
		}
	}

	@Override
	public double getValue() {
		return best;
	}

}
