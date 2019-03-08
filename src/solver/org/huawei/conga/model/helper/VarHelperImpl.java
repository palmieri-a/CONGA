package org.huawei.conga.model.helper;

import java.util.ArrayList;
import java.util.Arrays;

import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.beans.Agent;

/**
 * Class providing access to varVisitor by var ID
 * 
 * @author anthony
 *
 */
public class VarHelperImpl implements VarHelper {

	// use to retrieve the index
	protected int[] byID;
	// vars_players[1]=3 means vars 1 is owned by player 3, -1 means it is the
	// first shared vars, -2 the second..
	protected int[] vars_players;

	protected int[] sharedVarObjectiveDecision;

	protected int[] nbSharedIntObjective;

	protected int[] nbSharedRealObjective;
	
	protected int[] nbSharedRealDecisionVar;

	protected int[] nbSharedIntDecisionVar;
	
	protected int sharedVarsIndex = -1;

	private Variable[] vars;

	private ArrayList<ArrayList<Integer>> ownerSharedVars;

	private Variable[] decisionVariable;

	@Override
	public int getOwner(final int varIndex) {
		if(varIndex >=vars_players.length){
			return 0;
		}
		return vars_players[varIndex];
	}

	@Override
	public Variable getVar(final int ID) {
		return vars[byID[ID - 1]];
	}

	@Override
	public void init(final Variable[] vars, final Variable[] varsCSPPlayer, final int nbPlayers) {
		this.vars = vars;
		this.vars_players = new int[vars.length];
		this.sharedVarObjectiveDecision = new int[vars.length];
		this.nbSharedIntObjective = new int[nbPlayers];
		this.nbSharedRealObjective = new int[nbPlayers];
		this.nbSharedIntDecisionVar= new int[nbPlayers];
		this.nbSharedRealDecisionVar= new int[nbPlayers];
		this.byID = new int[vars[vars.length - 1].getId()];
		Arrays.fill(byID, -1);
		for (int i = 0; i < vars.length; i++) {
			byID[vars[i].getId() - 1] = i;
		}
		ownerSharedVars = new ArrayList<>(varsCSPPlayer.length);
	}

	@Override
	public int getIndex(final Variable v) {
		assert v != null;
		return byID[v.getId() - 1];
	}

	@Override
	public int getOwnerByVarID(final int ID) {
		return vars_players[byID[ID - 1]];
	}

	@Override
	public void setOwner(final Variable v, final int owner) {
		int index = vars_players[byID[v.getId() - 1]];
		if (index == 0) {
			vars_players[byID[v.getId() - 1]] = owner;
		} else if (index > 0) {
			// change index create and add
			if((v.getTypeAndKind() & Variable.REAL) !=0){
				this.nbSharedRealDecisionVar[owner-1]++;
				this.nbSharedRealDecisionVar[index-1]++;
			}else{
				this.nbSharedIntDecisionVar[owner-1]++;
				this.nbSharedIntDecisionVar[index-1]++;
			}
			ArrayList<Integer> arrayList = new ArrayList<>(vars_players.length);
			ownerSharedVars.add(-sharedVarsIndex - 1, arrayList);
			vars_players[byID[v.getId() - 1]] = sharedVarsIndex;
			sharedVarsIndex--;
		} else {
			// add owner
			ownerSharedVars.get(-index - 1).add(owner);
		}
	}

	@Override
	public Variable[] getVars() {
		return this.vars;
	}

	@Override
	public void flushOwnership(final AbstractGameModel game, final Agent agent) {
		for (int i = 0; i < this.vars_players.length; i++) {
			if (this.vars_players[i] == 0) {
				agent.own(vars[i]);
			}
		}
	}

	@Override
	public void updateDecisionVars(final Variable[] decisionVariables) {
		this.decisionVariable = decisionVariables;
	}

	@Override
	public int getNBDecisionVars() {
		return this.decisionVariable.length;
	}

	@Override
	public int getNBVars() {
		return this.vars.length;
	}

	@Override
	public Variable[] getDecisionVars() {
		return this.decisionVariable;
	}

	@Override
	public boolean isSharedVariable(final int ID) {
		return this.vars_players[byID[ID - 1]] < 0;
	}

	@Override
	public void addDecisionObjective(final int player, final int id, final int kind) {
		if ((kind & Variable.REAL) != 0) {
			nbSharedRealObjective[player - 1]++;
		} else {
			nbSharedIntObjective[player - 1]++;
		}
		sharedVarObjectiveDecision[byID[id-1]] = kind;
	}

	@Override
	public int isObjectiveShared(int id) {
		return sharedVarObjectiveDecision[byID[id]];
	}

	@Override
	public int getNbSharedObjective(final int agent) {
		return nbSharedIntObjective[agent - 1] + nbSharedRealObjective[agent - 1];
	}

	@Override
	public int getNbSharedIntObjective(final int agent) {
		return nbSharedIntObjective[agent - 1];
	}

	@Override
	public int getNbSharedRealObjective(final int agent) {
		return nbSharedRealObjective[agent - 1];
	}
	
	@Override
	public int getNbSharedIntDecisionVar(final int agent) {
		return nbSharedIntDecisionVar[agent - 1];
	}

	@Override
	public int getNbSharedRealDecisionVar(final int agent) {
		return nbSharedRealDecisionVar[agent - 1];
	}

}
