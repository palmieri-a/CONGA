package org.huawei.conga.model.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class MultipleSharedVarSample extends AbstractGameModel {

	public MultipleSharedVarSample() {
		super(3);
	}

	@Override
	public void buildModel(Model s) {
		IntVar sharedVar = s.intVar(0, 3);
		for (int i = 0; i < players.length - 1; i++) {
			players[i + 1].own(sharedVar);
		}
		IntVar[] vars = s.intVarArray(10, new int[] { 1, 2 });

		IntVar sharedVar2 = s.intVar(0, 3);
		for (int i = 0; i < players.length - 1; i++) {
			players[i + 1].own(sharedVar2);
		}

		for (int i = 1; i < players.length; i++) {
			players[i].own(vars[i]);
		}
	}

}
