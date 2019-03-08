package org.huawei.conga.example.game;



import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.factory.ConstraintFactory;

/**
 * This game is taken from Gamut. Each players must choose a strategy. The
 * reward correspond to the number of players which have chosen the same
 * strategy.
 * 
 * @author a80048597
 *
 */
public class CollaborationGame extends AbstractGameModel {

	private final int nbStrat;

	public CollaborationGame(int nbPlayers, int nbStrat) {
		super(nbPlayers);
		this.nbStrat = nbStrat;
	}

	@Override
	public void buildModel(Model s) {
		IntVar Choice[] = s.intVarArray("Choice", players.length - 1, 0, nbStrat - 1);
		IntVar Objectives[] = s.intVarArray("objective", players.length - 1, 1, players.length);

		BoolVar values[][] = s.boolVarMatrix("choice_channeling", players.length - 1, nbStrat);
		BoolVar reverseValue[][] = new BoolVar[nbStrat][players.length - 1];

		for (int j = 0; j < reverseValue.length; j++) {
			for (int i = 0; i < reverseValue[j].length; i++) {
				reverseValue[j][i] = values[i][j];
			}
		}

		// Constraints
		for (int i = 0; i < values.length; i++) {
			s.boolsIntChanneling(values[i], Choice[i], 0).post();
		}
		
		for (int i = 0; i < nbStrat; i++) {
			for (int j = 0; j < players.length - 1; j++) {
				s.ifThen(s.arithm(Choice[j], "=", i), s.sum(reverseValue[i], "=", Objectives[j]));
			}
		}

		for (int i = 1; i < players.length; i++) {
			players[i].own(Choice[i - 1]);
			players[i].setObjective(ResolutionPolicy.MAXIMIZE,Objectives[i - 1]);
		}
	}
	
	public static void main(String[] args) {
		CongaSolver cg = new CongaSolver(new CollaborationGame(6,6));
		cg.setConstraintBuilder(ConstraintFactory.BOUND_CONSTRAINT
				);
		Solver s =cg.prepareAndGetSolver();
		s.setSearch(Search.inputOrderLBSearch(cg.retrieveIntDecisionVar(true)));
		long t1  = System.currentTimeMillis();
		s.showSolutions();
		while (s.solve()) {	}
		System.out.println("t:"+(System.currentTimeMillis()-t1));
	}
	

}
