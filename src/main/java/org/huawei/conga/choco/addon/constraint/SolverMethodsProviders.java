package org.huawei.conga.choco.addon.constraint;

import java.util.List;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;

public interface SolverMethodsProviders {

	
	static public List<Solution> findParetoFront(RealVar[] objectives, boolean maximisation){
		ParetoFrontRealVar pareto = new ParetoFrontRealVar(maximisation, objectives);
		
		while (objectives[0].getModel().getSolver().solve()) {
			pareto.onSolution();
		}
		return pareto.getParetoFront();
	}
	
	
	static public List<Solution> findParetoFront(Variable[] objectives, boolean maximisation){
		ParetoFrontMixedVar pareto = new ParetoFrontMixedVar(maximisation, objectives);
		while (objectives[0].getModel().getSolver().solve()) {
			pareto.onSolution();
		}
		return pareto.getParetoFront();
	}
	
	static public List<Solution> findParetoFront(IntVar[] objectives, boolean maximisation){
		return objectives[0].getModel().getSolver().findParetoFront(objectives, maximisation);
//		ParetoFront pareto = new ParetoFront(maximisation, objectives);
//		while (objectives[0].getModel().getSolver().solve()) {
//			pareto.onSolution();
//		}
//		return pareto.getParetoFront();
	}
}