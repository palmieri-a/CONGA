package org.huawei.conga.model.beans.updater;

import java.util.ArrayList;
import java.util.Collections;

import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.datastructure.graph.IGraph;
import org.huawei.datastructure.graph.builder.GraphBuilder;

public class GraphDependenciesUpdater implements PlayerDependenciesUpdater {

	private IGraph dependencies;

	public GraphDependenciesUpdater(GraphBuilder dependenciesProvider) {
		dependenciesProvider.build();
		this.dependencies = dependenciesProvider.getGraph();
	}
	
	public GraphDependenciesUpdater(IGraph graph) {
		this.dependencies=graph;
	}

	@Override
	public void update(Agent[] agents, VarHelper vh) {
		for (int k = 1; k < agents.length; k++) {
			Agent agent = agents[k];
			ArrayList<Integer> neigbors = dependencies.getNeigbors(agent.getID() - 1);
			Collections.sort(neigbors);
			int nbVarDependent = 0;
			for (int i = 0; i < neigbors.size(); i++) {
				nbVarDependent += agents[neigbors.get(i) + 1].getNbVars();
			}
			Variable[] tupleVarCSPPlayer = new Variable[nbVarDependent];
			Variable[] tupleVarCSP = new Variable[nbVarDependent];

			int indexTupleVars = 0;
			for (int i = 0; i < neigbors.size(); i++) {
				int index = neigbors.get(i) + 1;
				if (index != agent.getID()) {
					Agent a = agents[index];
					for (int j = 0; j < a.getNbVars(); j++) {
						tupleVarCSP[indexTupleVars] = a.getVariables()[j];
						tupleVarCSPPlayer[indexTupleVars++] = a.getVariablesCSPPlayer()[j];
					}
				}
			}

			agent.setTupleVarCSPPlayer(tupleVarCSPPlayer);
			agent.setTupleVarCSP(tupleVarCSP);

		}
	}

}
