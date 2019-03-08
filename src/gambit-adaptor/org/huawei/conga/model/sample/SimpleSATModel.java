package org.huawei.conga.model.sample;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class SimpleSATModel extends AbstractGameModel {

	public SimpleSATModel() {
		super(2);
	}

	@Override
	public void buildModel(Model s) {

		// all variables
		IntVar x = s.intVar("x", 3, 5);
		IntVar y = s.intVar("y", 3, 5);
		// Variables controled by players
		players[1].own(x);
		players[2].own(y);
		// constraint of strategies possibilities
		s.allDifferent((new IntVar[] { x, y})).post();
		// preferences modelisation
		players[1].addPreference(s.arithm(y, ">", 4));
	}

}

