package org.huawei.conga;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.equilibria.constraint.builder.ConstraintBuilder;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.datastructure.table.TableTree;

public abstract class ValidityTest {

	protected Integer[] retrieveSolution(Solver s, IntVar[] decisionVars) {
		Integer[] tuple = new Integer[decisionVars.length];
		for (int i = 0; i < tuple.length; i++) {
			tuple[i] = decisionVars[i].getValue();
		}
		return tuple;
	}



	public void run(TableTree sol, ConstraintBuilder cb, final AbstractGameModel model ) {
		CongaSolver cg = new CongaSolver(model);
		cg.setConstraintBuilder(cb);
		Solver s = cg.prepareAndGetSolver();
		s.showSolutions();
		while (s.solve()) {
			System.out.println(Arrays.deepToString(retrieveSolution(s,
			 cg.retrieveIntDecisionVar(true))));
			 assertTrue(sol.contains(retrieveSolution(s,
			 cg.retrieveIntDecisionVar(true))));
		}
		assertEquals(sol.getNbTuple(), s.getSolutionCount());
		return;
	}
	
	
	

}
