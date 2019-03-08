package org.huawei.conga.algorithms;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithm.welfare.function.IWelfareCreator;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.factory.ConstraintFactory;

public abstract class PricingComputer implements Executable {
	protected CongaSolver centralizedCongaSolver;
	protected CongaSolver cg;
	protected IWelfareCreator iwc;
	protected double best=0.0;
	

	public PricingComputer(CongaSolver cg, IWelfareCreator iwc) {
		this.cg = cg;
		this.iwc = iwc;

	}

	protected void computeBestCentralizedValue(AbstractGameModel g) {
		centralizedCongaSolver = new CongaSolver(g);
		centralizedCongaSolver.setConstraintBuilder(ConstraintFactory.NO_CONSTRAINT);
		Solver s = centralizedCongaSolver.prepareAndGetSolver();
		Variable centralizedOptVar = iwc.buildFunction(cg.getAgents(), s.getModel());
		s.getModel().setObjective(iwc.isMaximisation(), centralizedOptVar);
		while (s.solve());
		if ((centralizedOptVar.getTypeAndKind() & Variable.INT) != 0) {
			best= s.getBestSolutionValue().intValue();
		}else{
			best= s.getBestSolutionValue().doubleValue();
		}
	}
	
	public static void main(String[] args) {
			
	}
}
