package org.huawei.conga.example.graphical;


import java.io.File;
import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.equilibria.constraint.agent.bound.MetaBFSConstraint;
import org.huawei.conga.model.AbstractGameModel;
import org.huawei.conga.model.beans.updater.GraphDependenciesUpdater;
import org.huawei.conga.model.factory.ConstraintFactory;
import org.huawei.datastructure.graph.IGraph;
import org.huawei.datastructure.graph.builder.FileGraphBuilder;

public class PublicGoodGame extends AbstractGameModel {

	private IntVar[] actions;
	private IntVar[] rewards;
	private IntVar[] sumActions;
	private IGraph graph;

	public PublicGoodGame(IGraph graph) {
		super(graph.getNbNodes());
		this.graph=graph;
	}

	@Override
	public void buildModel(Model s) {
		int MAX = 100;
		actions = s.intVarArray("actions", players.length - 1, new int[] { 0, MAX });
		rewards = s.intVarArray("rewards", players.length - 1, -MAX, MAX);
		sumActions = s.intVarArray("sumAction", players.length - 1, 0, actions.length * MAX);
		for (int i = 1; i < players.length; i++) {
			players[i].setObjective(ResolutionPolicy.MAXIMIZE,rewards[i - 1]);
			players[i].own(actions[i - 1]);
			Constraint takeAction = s.arithm(actions[i - 1], "=", MAX);
			ArrayList<Integer> neigbors = graph.getNeigbors(i - 1);
			s.sum(createArraysVar(neigbors), "=", sumActions[i - 1]).post();
			// if a neighbors have taken the action 100 and player havent taken
			// the action then its reward is maximal
			Constraint neigborsActions = s.arithm(sumActions[i - 1], ">", 0);
			s.ifOnlyIf(s.and(neigborsActions, takeAction.getOpposite()), s.arithm(rewards[i - 1], "=", MAX));
			// if player doesn't take action and no neigbors have taken the
			// aciton then the reward is 0.
			s.ifOnlyIf(s.and(neigborsActions.getOpposite(), takeAction.getOpposite()),
					s.arithm(rewards[i - 1], "=", 0));
			// if player take the action then its reward is equal to the
			s.ifOnlyIf(takeAction, s.arithm(rewards[i - 1], "=", MAX - 60));
		}
	}

	private IntVar[] createArraysVar(ArrayList<Integer> neigbors) {
		IntVar[] varsArrays = new IntVar[neigbors.size()];
		int indexVarArray = 0;
		for (int i = 0; i < neigbors.size(); i++) {
			varsArrays[indexVarArray++] = actions[neigbors.get(i)];
		}
		return varsArrays;
	}

	public static void main(String[] args) {
		System.out.println("bipartite11");
		FileGraphBuilder fileGraphBuilder = new FileGraphBuilder(
				//57 7 3
//				new File("C:\\Users\\anthony\\Code\\GroovyBenchmarks\\DataSet\\graph\\circle_undirect\\circle_undirected_15.txt"));

				new File("C:\\Users\\anthony\\Code\\GroovyBenchmarks\\DataSet\\graph\\tree_regular\\tree_regular_73_8_3_0.txt"));
		CongaSolver cg = new CongaSolver(new PublicGoodGame(fileGraphBuilder
						.getGraph()));
		// C:\Users\anthony\Code\GroovyBenchmarks\DataSet\graph\circle\.\circle_50.txt
		// C:\\Users\\anthony\\Code\\GroovyBenchmarks\\DataSet\\graph\\bipartite\\bipartite_20.txt
		cg.setConstraintBuilder(ConstraintFactory.BOUND_CONSTRAINT);
		cg.setDependenciesProvider(new GraphDependenciesUpdater( fileGraphBuilder));
		Solver prepareAndGetSolver = cg.prepareAndGetSolver();
//		prepareAndGetSolver.showSolutions();375667

		prepareAndGetSolver.setSearch(Search.inputOrderLBSearch(cg.retrieveIntDecisionVar(true)));
		long t1 = System.currentTimeMillis();
//		prepareAndGetSolver.showShortStatistics();
		//Model[CSP], 100197 Solutions, Resolution time 2,785s, 200393 Nodes (71 957,3 n/s), 200393 Backtracks, 0 Fails, 0 Restarts
		while (prepareAndGetSolver.solve());
		prepareAndGetSolver.showShortStatistics();
		System.out.println(System.currentTimeMillis() - t1);
		System.out.println(prepareAndGetSolver.getMeasures().toOneLineString());
		System.out.println("nbCall= "+MetaBFSConstraint.nbCALL);
		
	}

}
