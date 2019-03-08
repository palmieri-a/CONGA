package org.huawei.conga.example.graphical;

import java.io.File;
import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.factory.ConstraintFactory;
import org.huawei.datastructure.graph.IGraph;
import org.huawei.datastructure.graph.builder.FileGraphBuilder;

/***
 * Consider a situation where a player prefers to coordinate his or her
 * action*with others players,and his or her payoff is related to the fraction
 * of*neighbors who play the same strategy**
 * 
 * @author a80048597
 *
 */
public class GraphicalCoordinationGame extends AbstractGameModel {
	private final int nbStrat;
	private IGraph graph;
	private IntVar[] choice;

	public GraphicalCoordinationGame( IGraph graph,final int nbActions) {
		super(graph.getNbNodes());
		this.nbStrat = nbActions;
		this.graph=graph;
	}
	
	public GraphicalCoordinationGame( IGraph graph) {
		super(graph.getNbNodes());
		this.nbStrat = graph.getNbNodes();
		this.graph=graph;
	}

	@Override
	public void buildModel(Model s) {
		choice = s.intVarArray("Choice", players.length - 1, 0, nbStrat - 1);
		IntVar Objectives[] = s.intVarArray("objective", players.length - 1, 1, players.length-1);
		
		IntVar maxChoices = s.intVar("maxChoices", 1, players.length-1);
		s.max(maxChoices, Objectives).post();
		for (int i = 0; i < players.length-1; i++) {
			s.count(choice[i], createArraysVar(graph.getNeigbors(i),i), Objectives[i]).post();
		}

		for (int i = 1; i < players.length; i++) {
			players[i].own(choice[i - 1]);
			players[i].setObjective(ResolutionPolicy.MAXIMIZE,Objectives[i - 1]);
		}
	}
	
	
	private IntVar[] createArraysVar(ArrayList<Integer> neigbors, int value) {
		IntVar[] varsArrays = new IntVar[neigbors.size()];
		int indexVarArray = 0;
		for (int i = 0; i < neigbors.size(); i++) {
			varsArrays[indexVarArray++] = choice[neigbors.get(i)];
		}

		return varsArrays;
	}
	
	public static void main(String[] args) {
		CongaSolver cg = new CongaSolver(new GraphicalCoordinationGame(new FileGraphBuilder(
				new File("C:\\Users\\anthony\\Code\\GroovyBenchmarks\\DataSet\\graph\\bipartite\\.\\bipartite_8.txt"))
						.getGraph()));
		// C:\Users\anthony\Code\GroovyBenchmarks\DataSet\graph\circle\.\circle_50.txt
		// C:\\Users\\anthony\\Code\\GroovyBenchmarks\\DataSet\\graph\\bipartite\\bipartite_20.txt
		cg.setConstraintBuilder(ConstraintFactory.BOUND_CONSTRAINT);

		Solver prepareAndGetSolver = cg.prepareAndGetSolver();
//		prepareAndGetSolver.showShortStatistics();
//		prepareAndGetSolver.showSolutions();
		long t1 = System.currentTimeMillis();

		while (prepareAndGetSolver.solve())
			;
		
		prepareAndGetSolver.showShortStatistics();
		System.out.println(System.currentTimeMillis() - t1);
	}

}
