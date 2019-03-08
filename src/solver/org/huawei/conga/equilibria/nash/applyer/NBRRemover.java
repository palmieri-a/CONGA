package org.huawei.conga.equilibria.nash.applyer;

import org.chocosolver.solver.exception.ContradictionException;

public interface NBRRemover {

	public void apply() throws ContradictionException;
}
