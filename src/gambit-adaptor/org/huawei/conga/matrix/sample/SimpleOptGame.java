package org.huawei.conga.matrix.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class SimpleOptGame extends AbstractGameModel {

	public SimpleOptGame(int nbPlayers) {
		super(nbPlayers);
	}

	@Override
	public void buildModel(Model s) {
		IntVar vars[]= s.intVarArray(getNBPlayers(), 0,10);
		for (int i = 0; i < vars.length; i++) {
			players[i+1].own(vars[i]);
			players[i+1].setObjective(ResolutionPolicy.MAXIMIZE, vars[i]);
		}

	}

}
