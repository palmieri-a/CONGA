package org.huawei.conga.utils.tuple.provider;

import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.number.Interval;

public class BasicTupleProvider implements TupleProvider {

	private IntVar[] scopePreInt;
	private Integer[] tuplePreInt;

	private RealVar[] scopePreReal;
	private Interval[] tuplePreReal;
	private Number[] tuplePre;

	private IntVar[] scopePostInt;
	private Integer[] tuplePostInt;

	private RealVar[] scopePostReal;
	private Interval[] tuplePostReal;
	private Number[] tuplePost;

	private final VarHelper vh;
	private int sizePreInt;
	private int sizePreReal;
	private int sizePostInt;
	private int sizePostReal;

	@Deprecated
	public BasicTupleProvider(final Agent a, final VarHelper vh) {
		this.vh = vh;
		init(a);
	}

	private void init(Agent a) {
		this.scopePreInt = a.getTupleIntVarCSP();
		this.scopePreReal = a.getTupleRealVarCSP();

		assert null == scopePreInt || 0 == scopePreInt.length || scopePreInt[0].getModel().getName().equals("CSP");
		assert null == scopePreReal || 0 == scopePreReal.length || scopePreReal[0].getModel().getName().equals("CSP");

		this.scopePostInt = a.getIntVariables();
		this.scopePostReal = a.getRealVariables();

		assert null == scopePostInt || 0 == scopePostInt.length || scopePostInt[0].getModel().getName().equals("CSP");
		assert null == scopePostReal || 0 == scopePostReal.length
				|| scopePostReal[0].getModel().getName().equals("CSP");

		sizePreInt = (null != scopePreInt) ? scopePreInt.length - vh.getNbSharedIntDecisionVar(a.getID()) : 0;
		sizePreReal = (null != scopePreReal) ? scopePreReal.length - vh.getNbSharedRealDecisionVar(a.getID()) : 0;

		sizePostInt = (null != scopePostInt) ? scopePostInt.length : 0;
		sizePostReal = (null != scopePostReal) ? scopePostReal.length : 0;

		this.tuplePreInt = new Integer[sizePreInt];
		this.tuplePreReal = new Interval[sizePreReal];

		this.tuplePostInt = new Integer[a.getNbIntVars()];
		this.tuplePostReal = new Interval[a.getNbRealVars()];

		this.tuplePre = new Number[sizePreInt + sizePreReal];
		this.tuplePost = new Number[sizePostInt + sizePostReal];

		for (int i = 0; i < sizePreReal; i++) {
			tuplePreReal[i] = new Interval(0.0, 0.0);
			tuplePre[sizePreInt + i] = tuplePreReal[i];
		}

		for (int i = 0; i < sizePostReal; i++) {
			tuplePostReal[i] = new Interval(0.0, 0.0);
			tuplePost[sizePreInt + i] = tuplePostReal[i];
		}

	}

	public void setScope(Agent a) {
		init(a);
	}

	@Override
	public Integer[] getTuplePreInt() {
		updateIntPreTuple();
		return this.tuplePreInt;
	}

	@Override
	public Integer[] getTuplePostInt() {
		updateIntPostTuple();
		return this.tuplePostInt;
	}

	@Override
	public Interval[] getTuplePreReal() {
		updateRealPreTuple();
		return this.tuplePreReal;
	}

	@Override
	public Interval[] getTuplePostReal() {
		updateRealPostTuple();
		return this.tuplePostReal;
	}

	@Override
	public Number[] getAndCheckFullPreTuple() {
		updateIntPreTuple();
		updateRealPreTuple();
		return this.tuplePre;
	}

	@Override
	public Number[] getAndCheckFullPostTuple() {
		updateIntPostTuple();
		updateRealPostTuple();
		return this.tuplePost;
	}

	@Override
	public Number[] getFullPreTuple() {
		return this.tuplePre;
	}

	@Override
	public Number[] getFullPostTuple() {
		return this.tuplePost;
	}

	private void updateIntPreTuple() {
		final int size = sizePreInt > 0 ? scopePreInt.length : sizePreInt;
		int offset = 0;
		for (int i = 0; i < size; i++) {
			if (!vh.isSharedVariable(scopePreInt[i].getId())) {
				tuplePre[i - offset] = scopePreInt[i].getValue();
				tuplePreInt[i - offset] = scopePreInt[i].getValue();
			} else {
				offset++;
			}
		}
	}

	public void updateIntPostTuple() {
		for (int i = 0; i < sizePostInt; i++) {
			tuplePost[i] = scopePostInt[i].getValue();
			tuplePostInt[i] = scopePostInt[i].getValue();
		}
	}

	private void updateRealPostTuple() {
		for (int i = 0; i < sizePostReal; i++) {
			tuplePostReal[i].setLb(scopePostReal[i].getLB());
			tuplePostReal[i].setUb(scopePostReal[i].getUB());
		}
	}

	private void updateRealPreTuple() {
		final int size = sizePreReal > 0 ? scopePreReal.length : sizePreReal;
		int offset = 0;
		for (int i = 0; i < size; i++) {
			if (!vh.isSharedVariable(scopePreReal[i].getId())) {
				tuplePreReal[i - offset].setLb(scopePreReal[i].getLB());
				tuplePreReal[i - offset].setUb(scopePreReal[i].getUB());
			} else {
				offset++;
			}
		}
	}

}
