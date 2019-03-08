package org.huawei.conga.model.beans;

import org.chocosolver.solver.Cause;
import org.chocosolver.solver.ICause;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;

public class VariableDecorator {

	private IntVar intVar;
	private RealVar realVar;
	private final boolean isReal;

	public VariableDecorator(IntVar v) {
		this.intVar = v;
		isReal = false;
	}

	public VariableDecorator(Variable v) {
		final int type = v.getTypeAndKind();
		if ((type & Variable.INT) != 0) {
			isReal = false;
			this.intVar = (IntVar) v;
		} else if ((type & Variable.REAL) != 0) {
			this.realVar = (RealVar) v;
			isReal = true;
		}else{
			isReal = false;
		}
	}

	public VariableDecorator(RealVar v) {
		this.realVar = v;
		isReal = true;
	}

	public Variable getVariable() {
		return isReal ? realVar : intVar;
	}
	
	public boolean isInteger() {
		return !isReal;
	}


	public double getLB() {
		return isReal ? realVar.getLB() : intVar.getLB();
	}

	public double getUB() {
		return isReal ? realVar.getUB() : intVar.getUB();
	}

	public boolean isInstantiated() {
		return isReal ? realVar.isInstantiated() : intVar.isInstantiated();
	}

	public int getId() {
		return isReal ? realVar.getId() : intVar.getId();
	}

	public Model getModel() {
		return isReal ? realVar.getModel() : intVar.getModel();
	}

	public void updateLowerBound(double lB, ICause null1) throws ContradictionException {
		if (isReal) {
			this.realVar.updateLowerBound(lB, null1);
		} else {
			this.intVar.updateLowerBound((int) lB, null1);
		}
	}

	public void updateUpperBound(double uB, ICause null1) throws ContradictionException {
		if (isReal) {
			this.realVar.updateUpperBound(uB, null1);
		} else {
			this.intVar.updateUpperBound((int) uB, null1);
		}
	}
	
	public void updateBounds(double lB,double UB, ICause null1) throws ContradictionException {
		if (isReal) {
			this.realVar.updateBounds(lB, UB, null1);
		} else {
			this.intVar.updateBounds((int)lB,(int) UB, null1);
		}
	}

}
