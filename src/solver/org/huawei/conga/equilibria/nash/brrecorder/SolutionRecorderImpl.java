package org.huawei.conga.equilibria.nash.brrecorder;

import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.number.Interval;
import org.huawei.datastructure.table.TableTree;

public class SolutionRecorderImpl implements ISolutionRecorder {

	protected IntVar[] scopeInt;
	protected RealVar[] scopeReal;
	protected TableTree data;
	protected Number[] tuple;
	
	@Override
	public void onSolution() {
		computeTuple();
		data.add(tuple);
	}

	protected void computeTuple() {
		if ((null != scopeInt)) {
			for (int i = 0; i < scopeInt.length; i++) {
				tuple[i] = scopeInt[i].getValue();
			}
		}

		if ((null != scopeReal)) {
			for (int i = 0; i < scopeReal.length; i++) {
				tuple[i + scopeInt.length] = new Interval(scopeReal[i].getLB(), scopeReal[i].getUB());
			}
		}
	}

	@Override
	public TableTree getData() {
		return data;
	}

	@Override
	public void init(Agent owner) {
		this.data = new TableTree(owner.getNbVars());
		tuple = new Number[owner.getNbVars()];
		scopeInt = owner.getIntVariablesCSPPlayer();
		scopeReal = owner.getRealVariablesCSPPlayer();
	}

	@Override
	public void reset() {
		tuple = null;
		this.data.resetRootNode();
	}
}
