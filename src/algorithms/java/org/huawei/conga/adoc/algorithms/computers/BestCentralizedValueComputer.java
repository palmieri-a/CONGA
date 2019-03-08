package org.huawei.conga.adoc.algorithms.computers;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithm.welfare.function.IWelfareCreator;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.factory.ConstraintFactory;

public class BestCentralizedValueComputer implements IComputer{
	public IWelfareCreator iwc;
	public double best;
	private AbstractGameModel game;

	public BestCentralizedValueComputer(AbstractGameModel game,IWelfareCreator iwc) {
		this.game=game;
		this.iwc=iwc;
	}

	@Override
	public double getValue() {
		return best;
	}

	@Override
	public void execute() {
		CongaSolver centralizedCongaSolver = new CongaSolver(game);
		centralizedCongaSolver.setConstraintBuilder(ConstraintFactory.NO_CONSTRAINT);
		Solver s = centralizedCongaSolver.prepareAndGetSolver();
		Variable centralizedOptVar = iwc.buildFunction(centralizedCongaSolver.getAgents(), s.getModel());
		s.getModel().setObjective(iwc.isMaximisation(), centralizedOptVar);
		while (s.solve());
		if ((centralizedOptVar.getTypeAndKind() & Variable.INT) != 0) {
			best= s.getBestSolutionValue().intValue();
		}else{
			best= s.getBestSolutionValue().doubleValue();
		}
	}
	
	
	
}