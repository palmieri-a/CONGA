package org.huawei.conga.matrix.sample;

import org.chocosolver.solver.Model;

public class SimpleGameModified extends SimpleGame {

	@Override
	public void buildModel(Model s) {
		super.buildModel(s);
		s.member(s.retrieveIntVars(false)[0], new int[] { 1 }).post();
		;
	}

}
