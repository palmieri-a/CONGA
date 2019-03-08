package org.huawei.conga.model.sample;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.model.AbstractGameModel;

public class SimpleModelSharedDecisionVarIntObjective extends AbstractGameModel {

	private static final int NB_P = 2;

	public SimpleModelSharedDecisionVarIntObjective() {
		super(NB_P);
	}

	@Override
	public void buildModel(Model s) {
		IntVar[] deVar = s.intVarArray(2, new int[]{1,2,4,8,9});
		for (int i = 0; i < deVar.length; i++) {
			players[i+1].own(deVar[i]);
		}
		
		players[1].setObjective(ResolutionPolicy.MAXIMIZE, deVar[0]);
	}

}
