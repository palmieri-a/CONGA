package org.huawei.conga.matrix.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class TwoPAllSolutionSimpleGame extends AbstractGameModel {

	public TwoPAllSolutionSimpleGame() {
		super(2);
	}

	@Override
	public void buildModel(Model s) {
		IntVar[] strat = s.intVarArray("strat_" + s.getName(), players.length - 1, 1, 2);
		IntVar[] utilities = s.intVarArray("utils_" + s.getName(), players.length - 1, 0, 1);

		Tuples allowed = new Tuples(true);
		// tuple allowed s1, s2, u1, 22
		allowed.add(new int[] { 1, 1, 1, 1 }, new int[] { 1, 2, 1, 1 }, new int[] { 2, 1, 1, 1 },
				new int[] { 2, 2, 1, 1 });
		s.table(new IntVar[] { strat[0], strat[1], utilities[0], utilities[1] }, allowed).post();
		;

		players[1].own(strat[0]);
		players[2].own(strat[1]);
		// players[2].addPreference(ICF.table(new
		// IntVar[]{strat[1],strat[0],utilities[1]}, allowed, "AC2001"));

		players[1].setObjective(ResolutionPolicy.MINIMIZE,utilities[0]);
		players[2].setObjective(ResolutionPolicy.MINIMIZE,utilities[1]);

	}
}
