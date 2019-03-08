package org.huawei.conga.model.beans.updater;

import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;

public class NaiveDependenciesUpdater implements PlayerDependenciesUpdater {

	@Override
	public void update(Agent[] agents, VarHelper vh) {
		for (int i = 1; i < agents.length; i++) {
			int pos = 0;
			Variable[] tupleCSP = new Variable[vh.getNBDecisionVars() - agents[i].getNbVars()];
			Variable[] tupleCSPPlayer = new Variable[vh.getNBDecisionVars() - agents[i].getNbVars()];

			for (int j = 1; j < agents.length; j++) {
				if (j != i) {
					Variable[] variables = agents[j].getVariables();

					variables =removeShared(variables,vh);
					System.arraycopy(variables, 0, tupleCSP, pos, variables.length);
				
					
					Variable[] variablesCSPPlayer = agents[j].getVariablesCSPPlayer();
					variablesCSPPlayer=removeShared(variablesCSPPlayer,vh);
					
					System.arraycopy(variablesCSPPlayer, 0, tupleCSPPlayer, pos, variablesCSPPlayer.length);
					pos += variables.length;
				}
			}
			Variable[]tupleCSP_ = new Variable[pos];
			Variable[]tupleCSP_Player = new Variable[pos];
			
			System.arraycopy(tupleCSP, 0, tupleCSP_, 0, pos);
			System.arraycopy(tupleCSPPlayer, 0, tupleCSP_Player, 0, pos);
			agents[i].setTupleVarCSP(tupleCSP_);
			agents[i].setTupleVarCSPPlayer(tupleCSP_Player);
		}
	}

	private Variable[] removeShared(Variable[] variables, VarHelper vh) {
		int size = variables.length;
		for (int i = 0; i < variables.length; i++) {
			if(vh.isSharedVariable(variables[i].getId())){
				size--;
			}
		}
		Variable othersVars []= new Variable[size];
		int offset= 0;
		for (int i = 0; i < variables.length; i++) {
			if(vh.isSharedVariable(variables[i].getId())){
				offset++;
				continue;
			}
			othersVars[i-offset]=variables[i];
		}
		return othersVars;
	}
}
