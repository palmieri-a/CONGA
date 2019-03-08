package org.huawei.conga.example.graphical;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.factory.ConstraintFactory;

public class WolfLambCabbage extends AbstractGameModel {

	private final Short WOLF_ID = 1, LAMB_ID = 2, CABBAGE_ID = 3;

	public WolfLambCabbage() {
		super(3);
	}

	@Override
	public void buildModel(Model s) {
		BoolVar[] isComing = s.boolVarArray("coming", players.length - 1);
		IntVar[] utilities = s.intVarArray(players.length - 2, 0, 2);

		for (int i = 0; i < isComing.length; i++) {
			players[i + 1].own(isComing[i]);
		}

		players[WOLF_ID].setObjective(ResolutionPolicy.MAXIMIZE,utilities[WOLF_ID - 1]);
		players[LAMB_ID].setObjective(ResolutionPolicy.MAXIMIZE,utilities[LAMB_ID - 1]);

		Constraint lamb_coming = s.arithm(isComing[LAMB_ID - 1], "=", 1);
		Constraint cabbage_coming = s.arithm(isComing[CABBAGE_ID - 1], "=", 1);
		Constraint wolf_coming = s.arithm(isComing[WOLF_ID - 1], "=", 1);

		s.arithm(s.and(wolf_coming, lamb_coming).reify(), "=", utilities[WOLF_ID - 1]).post();

		s.sum(new BoolVar[] { s.and(wolf_coming.getOpposite(), lamb_coming, cabbage_coming).reify(),
				s.and(wolf_coming, lamb_coming.getOpposite()).reify() }, "=", utilities[LAMB_ID - 1]).post();

	}
	
	public static void main(String[] args) {
		CongaSolver cg = new CongaSolver(new WolfLambCabbage());
		cg.setConstraintBuilder(ConstraintFactory.LAST_LEVEL);
		Solver s =cg.prepareAndGetSolver();
		s.solve();
	}

}
