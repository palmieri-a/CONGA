package org.huawei.conga.model.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class SimpleExample4_9 extends AbstractGameModel {

	public SimpleExample4_9() {
		super(3);
	}

	@Override
	public void buildModel(Model s) {
		IntVar[] x = s.intVarArray("X", 3, 1, 3);

		// Hard constraint

		// x <= y <= z
		s.arithm(x[0], "<=", x[1]).post();
		;
		s.arithm(x[1], "<=", x[2]).post();

		for (int i = 1; i < players.length; i++) {
			players[i].own(x[i - 1]);
		}

		//
		// Preferences
		//

		// x= y + z
		players[1].addPreference(s.sum(new IntVar[] { x[1], x[2] }, "=", x[0]));

		// x >= z >= y
		players[2].addPreference(s.arithm(x[0], ">=", x[2]));
		players[2].addPreference(s.arithm(x[2], ">=", x[1]));

		// x = y = z
		players[3].addPreference(s.arithm(x[2], "=", x[1]));
		players[3].addPreference(s.arithm(x[0], "=", x[1]));

	}
}
