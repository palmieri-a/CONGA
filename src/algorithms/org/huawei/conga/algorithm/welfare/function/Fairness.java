package org.huawei.conga.algorithm.welfare.function;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.beans.Agent;

/**
 * 
 * The fairness value corresponds to the minimal value of the utilities of the
 * player
 *
 * @author anthony
 *
 */
public class Fairness implements IWelfareCreator {

	private boolean isMax;

	public Fairness(boolean isMax) {
		this.isMax = isMax;
	}

	@Override
	public Variable buildFunction(Agent[] agents, Model m) {
		IntVar varInt;
		RealVar varReal = null;
		if (isAllIntegerVar(agents)) {

			varInt = m.intVar("welfare", (int) computeBounds(agents, false) , (int) computeBounds(agents, true));
			createAndPostIntMin(agents, m, varInt);
			return varInt;
		} else {
			varReal = m.realVar("welfare", computeBounds(agents, false), computeBounds(agents, true),
					((RealVar) agents[1].getObjectiveCSP()[0].getVariable()).getPrecision());
			createAndPostRealSum(agents, m, varReal);
			return varReal;
		}
	}

	private void createAndPostRealSum(Agent[] agents, Model m, RealVar varReal) {
		RealVar[] objs = new RealVar[agents.length - 1];
		String fct;
		if (agents[1 ].isMaximisation()) {
			fct = "{0}=min(";
		} else {
			fct = "{0}=max(";
		}
		for (int i = 1; i < agents.length; i++) {
			if (agents[i - 1].isMaximisation()) {
				fct += "{" + i + "} ";
			} else {
				fct += "-{" + i + "} ";
			}
			
			if (i < agents.length - 1) {
				fct += " , ";
			}
		}
		
		m.realIbexGenericConstraint(fct + ")", objs).post();
	}

	private void createAndPostIntMin(Agent[] agents, Model m, IntVar varInt) {
		IntVar[] obj = new IntVar[agents.length - 1];
		for (int i = 1; i < agents.length; i++) {
			if (agents[i ].isMaximisation()) {
				obj[i - 1] = (IntVar) agents[i].getObjectiveCSP()[0].getVariable();
			} else {
				obj[i - 1] = m.intMinusView((IntVar) agents[i].getObjectiveCSP()[0].getVariable());
			}
			
		}
		if (agents[1 ].isMaximisation()) {
			m.min(varInt, obj).post();
		} else {
			m.max(varInt, obj).post();
		}
		
	}

	@Override
	public boolean isMaximisation() {
		return isMax;
	}

}
