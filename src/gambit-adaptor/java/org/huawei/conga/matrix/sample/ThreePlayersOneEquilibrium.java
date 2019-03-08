package org.huawei.conga.matrix.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class ThreePlayersOneEquilibrium extends AbstractGameModel {

	public ThreePlayersOneEquilibrium() {
		super(3);
	}

	@Override
	public void buildModel(Model s) {
		IntVar[] strat = s.intVarArray("strat_" + s.getName(), players.length - 1, 1, players.length - 1);
		IntVar[] utilities = s.intVarArray("utils_" + s.getName(), players.length - 1, 0, 9);

		Tuples allowed = new Tuples(true);
		// tuple allowed s1, s2, s3, u1, u2, u3
		allowed.add(new int[] { 1, 1, 1, 0, 9, 0 }, new int[] { 2, 1, 1, 0, 0, 0 }, new int[] { 3, 1, 1, 9, 0, 0 },
				new int[] { 1, 2, 1, 0, 0, 0 }, new int[] { 2, 2, 1, 0, 0, 0 }, new int[] { 3, 2, 1, 0, 0, 0 },
				new int[] { 1, 3, 1, 9, 0, 0 }, new int[] { 2, 3, 1, 0, 0, 0 }, new int[] { 3, 3, 1, 0, 9, 0 },

		new int[] { 1, 1, 2, 0, 9, 0 }, new int[] { 2, 1, 2, 0, 0, 0 }, new int[] { 3, 1, 2, 9, 0, 0 },
				new int[] { 1, 2, 2, 0, 0, 0 }, new int[] { 2, 2, 2, 0, 0, 0 }, new int[] { 3, 2, 2, 0, 0, 0 },
				new int[] { 1, 3, 2, 9, 0, 0 }, new int[] { 2, 3, 2, 0, 0, 0 }, new int[] { 3, 3, 2, 0, 9, 0 },

		new int[] { 1, 1, 3, 0, 9, 0 }, new int[] { 2, 1, 3, 0, 0, 0 }, new int[] { 3, 1, 3, 9, 9, 9 },
				new int[] { 1, 2, 3, 0, 0, 0 }, new int[] { 2, 2, 3, 0, 0, 9 }, new int[] { 3, 2, 3, 9, 0, 0 },
				new int[] { 1, 3, 3, 9, 0, 0 }, new int[] { 2, 3, 3, 0, 0, 0 }, new int[] { 3, 3, 3, 0, 9, 0 });
		s.table(new IntVar[] { strat[0], strat[1], strat[2], utilities[0], utilities[1], utilities[2] }, allowed)
				.post();
		;

		for (int i = 0; i < players.length - 1; i++) {
			players[i + 1].own(strat[i]);
			players[i + 1].setObjective(ResolutionPolicy.MINIMIZE,utilities[i]);
		}
	}
}
