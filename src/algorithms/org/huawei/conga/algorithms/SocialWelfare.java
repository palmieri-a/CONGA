package org.huawei.conga.algorithms;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithm.welfare.function.IWelfareCreator;

public class SocialWelfare implements Executable {

	private CongaSolver cg;
	private IWelfareCreator function;
	private boolean isMaximisation;
	private Variable opt;

	public SocialWelfare(CongaSolver cg, IWelfareCreator function, boolean isMaximisation) {
		this.cg = cg;
		this.function = function;
		this.isMaximisation = isMaximisation;
	}

	@Override
	public void execute() {
		Solver s = cg.prepareAndGetSolver();
		this.opt = function.buildFunction(cg.getAgents(), s.getModel());
		s.getModel().setObjective(isMaximisation, opt);
		while (s.solve());
	}

	public Variable getOpt() {
		return opt;
	}
}
