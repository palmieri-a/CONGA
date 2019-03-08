package org.huawei.conga.model.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class SimpleProblem extends AbstractGameModel {

	public SimpleProblem() {
		super(3);
	}

	@Override
	public void buildModel(Model s) {
		IntVar X[] = s.intVarArray("s", players.length - 1, 0, players.length - 1);
		for (int i = 1; i < players.length; i++) {
			players[i].own(X[i - 1]);
		}
	}

}
