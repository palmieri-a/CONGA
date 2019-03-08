package org.huawei.conga.tests.graphical;

import static org.junit.Assert.*;

import java.io.File;

import org.huawei.conga.CongaSolver;
import org.huawei.conga.adoc.algorithms.builder.AlgorithmFactory;
import org.huawei.conga.algorithms.Executable;
import org.huawei.conga.example.graphical.GraphicalCoordinationGame;
import org.huawei.conga.graph.generator.Generator;
import org.huawei.conga.model.beans.updater.GraphDependenciesUpdater;
import org.huawei.datastructure.graph.IGraph;
import org.huawei.datastructure.graph.builder.FileGraphBuilder;
import org.junit.Test;

/**
 * This is a collaboration game but each player wants to be aligned on the strategy with a subset of players.
 * These tests show how to specify graphical game.
 * Note that no space is gained from that, but it has a computational advantage by reducing the number of call to the propagators.
 * 
 * @author a80048597
 *
 */
public class TestGraphicalCoordinationGame {

	
	@Test
	public void testOnACirlce(){
		
		IGraph dependencies = Generator.INSTANCE.generateCircle(10, true);
		GraphicalCoordinationGame gcg = new GraphicalCoordinationGame(dependencies,2);
		CongaSolver cg = new CongaSolver(gcg);
		GraphDependenciesUpdater dependenciesUpdater = new GraphDependenciesUpdater(dependencies);//Here we provide the graph to restrict players'dependencies.
		cg.setDependenciesProvider(dependenciesUpdater);
		Executable algo = AlgorithmFactory.COMPLETE_SEARCH.build(cg);
		algo.execute();
		assertEquals(cg.getSolver().getMeasures().getSolutionCount(),2);
		//The solution at the end here are easy. 
		//It is when players agreed on the same action even because of the topology.
	
	}
	
	
	@Test
	public void testOnLoadedGraph(){
		//We show here how to load a graph from a file.
		//The file format is the following:
		//the first line is the number of nodes then each line determine the adjency list of each node.
		//Note that the graph interface provides also a way to write graph into a file.
		
		final String path ="resources/graph/bipartite_9.txt";
		FileGraphBuilder fgb = new FileGraphBuilder(new File(path));
		fgb.build();
		GraphicalCoordinationGame gcg = new GraphicalCoordinationGame(fgb.getGraph(),2);
		CongaSolver cg = new CongaSolver(gcg);
		
		GraphDependenciesUpdater dependenciesUpdater = new GraphDependenciesUpdater(fgb.getGraph());//Here we provide the graph to restrict players'dependencies.
		cg.setDependenciesProvider(dependenciesUpdater);
		cg.getSolver().showSolutions();
		Executable algo = AlgorithmFactory.IBR.build(cg);
		algo.execute();
	}
	
}
