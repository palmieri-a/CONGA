package org.huawei.conga.equilibria.constraint.agent;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.ESat;

public class UseLessPropagator extends Propagator<Variable> {
	
	public UseLessPropagator(Variable[] scope) {
		super(scope);
	}

	@Override
	public void propagate(int evtmask) throws ContradictionException {}

	@Override
	public ESat isEntailed() {
		return ESat.TRUE;
	}
}
