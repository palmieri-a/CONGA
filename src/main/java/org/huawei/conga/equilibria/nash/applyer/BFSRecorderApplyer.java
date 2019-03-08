package org.huawei.conga.equilibria.nash.applyer;

import org.chocosolver.solver.Cause;
import org.chocosolver.solver.exception.ContradictionException;
import org.huawei.conga.equilibria.nash.brrecorder.SolutionRecorderImpl;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.beans.VariableDecorator;

public class BFSRecorderApplyer extends SolutionRecorderImpl implements NBRRemover {

	protected double LB;
	protected double UB;
	protected boolean isMaximisation;
	protected VariableDecorator obj;
	protected Agent agent;

	@Override
	public void init(Agent owner) {
		obj = owner.getObjectiveCSPPlayer()[0];
		LB =obj.getLB()-1;
		UB = obj.getUB()+1;
		isMaximisation = owner.isMaximisation();
		this.agent = owner;
	}

	public double getLB() {
		return LB;
	}

	public double getUB() {
		return UB;
	}

	@Override
	public void apply() throws ContradictionException {
		updateObjective();
	}

	protected void updateObjective() throws ContradictionException {
		if (isMaximisation) {
			agent.getObjectiveCSP()[0].updateLowerBound(LB, Cause.Null);
		 
		} else {
			agent.getObjectiveCSP()[0].updateUpperBound(UB, Cause.Null);
		}
		
	}

	@Override
	public void reset() {
		LB= Integer.MIN_VALUE;
		UB=Integer.MAX_VALUE;
	}
	@Override
	public void onSolution() {
		if (isMaximisation) {
		//case maximization
			if (LB < obj.getLB()) {
				this.LB = obj.getLB();
				try {
					obj.updateLowerBound(LB, Cause.Null);
				} catch (ContradictionException e) {
					e.printStackTrace();
				}
			}
		} else {
			//case minimization
			if (UB > obj.getUB()) {
				this.UB = obj.getUB();
				try {
					obj.updateUpperBound(UB, Cause.Null);
				} catch (ContradictionException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
