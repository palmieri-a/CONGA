package org.huawei.conga.equilibria.constraint.agent;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.ESat;

/**
 * The class PrefixFixer if used to retrieve same space state between the two solvers.
 * @author a80048597
 *
 */
public class PrefixFixer extends Propagator<Variable> {

	private IntVar[] prefixInt;
	private IntVar[] prefixIntVar;
	private RealVar[] prefixRealVar;
	private RealVar[] prefixReal;
	private int sizeRealVars;
	private int sizeIntVars;

	public PrefixFixer(Variable[] dVars) {
		super(dVars, PropagatorPriority.UNARY, false);

	}

	@Override
	public void propagate(int evtmask) throws ContradictionException {
		instantiateInt();
		instantiateReal();
		setPassive();
	}


	private void instantiateReal() throws ContradictionException {
		if (prefixRealVar == null || prefixRealVar.length == 0) {
			return;
		}
		for (int i = 0; i < prefixRealVar.length; i++) {
			prefixRealVar[i].updateBounds(prefixReal[i].getLB(), prefixReal[i].getUB(), this);
		}
	}

	private void instantiateInt() throws ContradictionException {
		if (prefixInt == null || prefixIntVar.length == 0) {
			return;
		}
		for (int i = 0; i < prefixIntVar.length; i++) {
			prefixIntVar[i].updateBounds(prefixInt[i].getLB(), prefixInt[i].getUB(), this);// instantiateTo(prefixInt[i].getValue(),
																							// this);
		}
	}

	@Override
	public ESat isEntailed() {
		if (prefixIntVar.length == 0 && prefixReal.length == 0) {
			return ESat.UNDEFINED;
		}
		if (sizeIntVars == 0 && sizeRealVars == 0) {
			return ESat.TRUE;
		}
		if (prefixIntVar[0].isInstantiatedTo(prefixInt[0].getValue())) {
			return ESat.TRUE;
		} else if (prefixIntVar[0].contains(prefixInt[0].getValue())) {
			return ESat.UNDEFINED;
		}
		return ESat.FALSE;
	}

	public void setPrefix(IntVar[] varCSP, RealVar realVarsCSP[], IntVar[] varsCSPPlayer, RealVar[] realVarsCSPPlayer) {
		this.prefixInt = varCSP;
		this.prefixReal = realVarsCSP;
		this.prefixIntVar = varsCSPPlayer;
		this.prefixRealVar = realVarsCSPPlayer;
		assert vars[0].getModel() == varsCSPPlayer[0].getModel();

	}

}
