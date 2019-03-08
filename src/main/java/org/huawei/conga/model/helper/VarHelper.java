package org.huawei.conga.model.helper;

import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.beans.Agent;

public interface VarHelper {

	int getOwner(final int varIndex);

	void init(final Variable[] vars, final Variable[] varsCSPPlayer, final  int nbAgents);

	int getOwnerByVarID(final int ID);

	int getIndex(final Variable v);

	Variable[] getVars();

	Variable getVar(final int ID);

	void setOwner(final Variable v,final  int owner);

	void flushOwnership(final AbstractGameModel abstractGameModel,final Agent agent);

	void updateDecisionVars(final Variable[] decisionVariables);

	int getNBDecisionVars();
	
	boolean isSharedVariable(final int index)	;

	int getNBVars();

	Variable[] getDecisionVars();

	void addDecisionObjective(final int i,final int id,final int kind);
	
	int isObjectiveShared(int id);

	int getNbSharedObjective(final int agent);

	int getNbSharedIntObjective(int agent);

	int getNbSharedRealObjective(int agent);

	int getNbSharedIntDecisionVar(int agent);

	int getNbSharedRealDecisionVar(int agent);
}
