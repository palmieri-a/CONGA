
package org.huawei.conga.model;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.beans.NullPlayer;
import org.huawei.conga.model.beans.Player;
import org.huawei.conga.model.helper.VarHelper;

/**
 * TODO refactor => must extend Choco model. getSolver must call build model and
 * return solver
 * 
 * @author a80048597
 *
 */
public abstract class AbstractGameModel {

	protected Agent[] players;
	protected long tInit;

	public AbstractGameModel(int nbPlayers) {
		this.tInit = System.currentTimeMillis();
		this.players = new Player[nbPlayers + 1];
		players[0] = new NullPlayer();
		for (int i = 1; i < nbPlayers + 1; i++) {
			players[i] = new Player(i);
		}
	}

	public abstract void buildModel(Model s);

	public void init(CongaSolver conga) {
		VarHelper varHelper = conga.getVarHelper();
		ArrayList<Variable> tmpDecisionVar = new ArrayList<>(varHelper.getNBVars());
		ArrayList<Variable> tmpDecisionVarCSPPlayer = new ArrayList<>(varHelper.getNBVars());
		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < players[i].getVariables().length; j++) {
				Variable v = players[i].getVariables()[j];
				varHelper.setOwner(v, i);
				if (i != 0) {
					tmpDecisionVar.add(v);
					tmpDecisionVarCSPPlayer.add(v);
				}
			}
		}
		Variable decisionVariables[] = new Variable[tmpDecisionVar.size()];
		Variable decisionVariablesCSPPlayer[] = new Variable[tmpDecisionVarCSPPlayer.size()];
		tmpDecisionVar.toArray(decisionVariables);
		tmpDecisionVarCSPPlayer.toArray(decisionVariablesCSPPlayer);

		if (tmpDecisionVar.isEmpty()) {
			System.err.println("No decision variable defined");
			System.exit(-1);
		}

		varHelper.updateDecisionVars(decisionVariables);
//		varHelper.flushOwnership(this, players[0]);
		conga.initAgents(players);
	}
	
	/**
	 * This method can be redefined to specify a way to optimize a nash equilibria
	 * @param s
	 */
	public void defineObjective(Model s){}

	public void reset() {
		for (int i = 1; i < players.length; i++) {
			players[i].reset();
		}
	}

	public final long gettInit() {
		return tInit;
	}

	public int getNBPlayers() {
		return players.length - 1;
	}
}
