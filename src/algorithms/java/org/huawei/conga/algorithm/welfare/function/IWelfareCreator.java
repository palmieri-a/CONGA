package org.huawei.conga.algorithm.welfare.function;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.beans.Agent;

public interface IWelfareCreator {
	
	

	public Variable buildFunction(Agent[]agents,Model m);
	
	public boolean isMaximisation();
	
	default boolean isAllIntegerVar(Agent[] agents) {
		for (int i = 0; i < agents.length; i++) {
			if(!agents[i].hasObjectif()){
				continue;
			}
			if(!agents[i].getObjectiveCSP()[0].isInteger()){
				return false;
			}
		}
		return true;
	}
	

	default double computeBounds(Agent[] agents, boolean isUB) {
		int val =0;
		if(isUB){
			for (int i = 1; i < agents.length; i++) {
				if(!agents[i].hasObjectif()){
					continue;
				}else{
					if(agents[i].isMaximisation()){
						val+=agents[i].getObjectiveCSP()[0].getUB();
					}else{
						val-=agents[i].getObjectiveCSP()[0].getLB();
					}
				}
			}
		}else{
			for (int i = 1; i < agents.length; i++) {
				if(!agents[i].hasObjectif()){
					continue;
				}
				if(agents[i].isMaximisation()){
					val+=agents[i].getObjectiveCSP()[0].getLB();
				}else{
					val-=agents[i].getObjectiveCSP()[0].getUB();
				}
				
			}
		}
		return val;
	}
}
