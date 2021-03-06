package org.huawei.conga.model.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class SimpleGame extends AbstractGameModel {

	public SimpleGame() {
		super(2);
	}

	@Override
	public void buildModel(Model s) {

		// all variables
		IntVar x = s.intVar("x", 0, 9);
		IntVar y = s.intVar("y", 0, 9);

		// Variables controled by players
		players[1].own(x);
		players[2].own(y);
		// constraint of strategies possibilities
		s.allDifferent((new IntVar[] { x, y/*, z, w, k*/ })).post();

		// preferences modelisation
		players[1].addPreference(s.arithm(y, ">", 4));

		// Can add objective to a player
		players[1].setObjective(ResolutionPolicy.MINIMIZE,x);
	}

}
