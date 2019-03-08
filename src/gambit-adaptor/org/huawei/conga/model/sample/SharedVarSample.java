package org.huawei.conga.model.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class SharedVarSample extends AbstractGameModel {

	public SharedVarSample() {
		super(3);
	}

	@Override
	public void buildModel(Model s) {
		IntVar sharedVar= s.intVar(0, 3);
		for (int i = 0; i < players.length-1; i++) {
			players[i+1].own(sharedVar);
		}
	}

}
