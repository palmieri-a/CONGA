package org.huawei.conga.example.game;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.binary.PropEqualX_Y;
import org.chocosolver.solver.constraints.nary.count.PropCountVar;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.factory.ConstraintFactory;

/**
 * Dispersion game model with constraint games.
 * This is an antogonist version of the collaboration game: all the players try to play a different strategy.
 * @author a80048597
 *
 */
public class DispersionGame extends AbstractGameModel {

	private int nbStrat;

	public DispersionGame(int nbPlayers, int nbStrat) {
		super(nbPlayers);
		this.nbStrat = nbStrat;
	}

	@Override
	public void buildModel(Model s) {
		IntVar pChoice[] = s.intVarArray("choice",getNBPlayers(), 1, nbStrat);
		IntVar Objectives[] = s.intVarArray("objective", players.length - 1, 0, players.length );

		for (int i = 0; i < players.length - 1; i++) {
			IntVar Evalue = s.intVar(s.generateName("COUNT_"), pChoice[i].getLB(), pChoice[i].getUB(), false);
			
			new Constraint("Count",
					new PropEqualX_Y(Evalue, pChoice[i]),
					new PropCountVar(pChoice, Evalue, Objectives[i])).post();;
		}

		for (int i = 1; i < players.length; i++) {
			players[i].own(pChoice[i - 1]);
			players[i].setObjective(ResolutionPolicy.MAXIMIZE,s.intMinusView(Objectives[i - 1]));
		}
	}

	public static void main(String[] args) {
		System.out.println("dispersion");
		CongaSolver cg = new CongaSolver(new DispersionGame(2, 3));
		cg.setConstraintBuilder(ConstraintFactory.LAST_LEVEL);

		long t1 = System.currentTimeMillis();
		Solver s = cg.prepareAndGetSolver();
		s.showSolutions();
		while (s.solve()) {
		}
		System.out.println("t:" + (System.currentTimeMillis() - t1));
	}

}
