package org.huawei.conga.example.game;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;
import org.huawei.conga.model.AbstractGameModel;

/**
 * A group of n ice cream vendors would like to choose a location numbered from
 * 1 to m for their stand in a street. Each vendor i wants to find a location
 * l_i. He already has fixed the price of his ice cream to pi and we assume
 * there is a customer’s house at each location. The customers choose their
 * vendor by minimizing the sum of the distance between their house and the
 * vendor plus the price of the ice cream. Because no two vendors can stand at
 * the same places, one need add a global constraints in the hard constraints
 * set of the game
 * http://www.planetizen.com/node/65765
 * @author anthony
 *
 */
public class LocationGame extends AbstractGameModel {

	private final int size;

	public LocationGame(int nbPlayers, int size) {
		super(nbPlayers);
		this.size = size;
	}

	@Override
	public void buildModel(Model s) {
		/** Benefits **/
		IntVar b[] = s.intVarArray("b", players.length - 1, 0, size * size * size);
		/** Location **/
		IntVar l[] = s.intVarArray("l", players.length - 1, 0, size - 1);

		BoolVar choiceLex[][] = s.boolVarMatrix("choice", players.length - 1, size);
		BoolVar reverseChoiceLex[][] = ArrayUtils.transpose(choiceLex);

		IntVar[] indexMin = s.intVarArray(size, 0, players.length);

		// no two vendor at the same place
		s.allDifferent(l).post();

		IntVar[][] reverse_cost = s.intVarMatrix(size, players.length - 1, 0, size * size);
		// forall i, forall j cost[i][j] = | j - l_i| * u_j + p_i
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < players.length - 1; i++) {
				s.distance(l[i], s.intVar(j), "=", reverse_cost[j][i]).post();
			}
		}
		IntVar minCosts[] = s.intVarArray("minCost", size, 0, size * size);

		// BREAK ties => the player with the min index win the customer if it is
		// the same distance.
		for (int i = 0; i < size; i++) {
			for (int j1 = 0; j1 < players.length - 1; j1++) {
				for (int j2 = j1+1; j2 < players.length - 1; j2++) {
//					if (j1 < j2) {
						s.ifThen(
								s.and(s.arithm(minCosts[i], "=", reverse_cost[i][j2]),
										s.arithm(minCosts[i], "=", reverse_cost[i][j1])),
								s.arithm(choiceLex[j1][i], "=", 1));
//					}
				}
			}
		}

		// //constraint to choice which vendor to go.

		for (int i = 0; i < minCosts.length; i++) {
			s.min(minCosts[i], reverse_cost[i]).post();
			s.element(minCosts[i], reverse_cost[i], indexMin[i], 0).post();
			s.boolsIntChanneling(reverseChoiceLex[i], indexMin[i], 0).post();
		}

		for (int i = 1; i < players.length; i++) {
			// each player chose a location
			players[i].own(l[i - 1]);
			s.sum(choiceLex[i - 1], "=", b[i - 1]).post();
			players[i].setObjective(ResolutionPolicy.MAXIMIZE,b[i - 1]);
		}
	}

}
