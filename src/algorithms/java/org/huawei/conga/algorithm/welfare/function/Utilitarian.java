package org.huawei.conga.algorithm.welfare.function;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.beans.Agent;
/**
 * The utilarian function corresponds to the sum
 * of the utilties of the players
 * @author anthony
 *
 */
public class Utilitarian implements IWelfareCreator {
	
	private boolean isMax;

	public Utilitarian(boolean isMax) {
		this.isMax = isMax;
	}

	@Override
	public Variable buildFunction(Agent[] agents, Model m) {
		IntVar varInt;
		RealVar varReal = null;
		if(isAllIntegerVar(agents)){
			varInt = m.intVar("welfare", (int)computeBounds(agents,false), (int)computeBounds(agents,true));
			createAndPostIntSum(agents,m,varInt);
			return varInt;
		}else{
			varReal = m.realVar("welfare", computeBounds(agents,false), computeBounds(agents,true),((RealVar)  agents[1].getObjectiveCSP()[0].getVariable()).getPrecision());
			createAndPostRealSum(agents,m,varReal);
			return varReal;
		}
	}

	private void createAndPostRealSum(Agent[] agents, Model m, RealVar varReal) {
		RealVar[] objs = new RealVar[agents.length-1];
		String fct = "{0}=";
		for (int i = 1; i < agents.length; i++) {
			objs[i]=(RealVar) agents[i-1].getObjectiveCSP()[0].getVariable();
			fct+="{"+i+"} ";
			if(i<agents.length-1){
				fct+=	" + ";
			}
		}
		m.realIbexGenericConstraint(fct, objs).post();
	}

	private void createAndPostIntSum(Agent[] agents, Model m, IntVar varInt) {
		IntVar[] obj = new IntVar[agents.length-1];
		for (int i = 1; i < agents.length; i++) {
			if (agents[i].isMaximisation()) {
				obj[i - 1] = (IntVar) agents[i].getObjectiveCSP()[0].getVariable();
			} else {
				obj[i - 1] = m.intMinusView((IntVar) agents[i].getObjectiveCSP()[0].getVariable());
			}
		}
		m.sum(obj, "=", varInt).post();
	}

	@Override
	public boolean isMaximisation() {
		return this.isMax;
	}
}
