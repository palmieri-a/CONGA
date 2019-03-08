package org.huawei.datastructure.graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdjacencyListGraph implements IGraph {

	// neigbor[3] give all neigbor of node 3
	protected ArrayList<Integer>[] neighbor;
	// reverseNeighbord[3] give the node which have 3 of neighbor
	protected ArrayList<Integer>[] reverseNeighbors;
	protected ArrayList<Integer>[] degreeOut;
	protected ArrayList<Integer>[] degreeIn;

	public AdjacencyListGraph(int nbNodes) {
		this.neighbor = new ArrayList[nbNodes];
		this.reverseNeighbors = new ArrayList[nbNodes];

		this.degreeIn = new ArrayList[nbNodes];
		degreeOut = new ArrayList[nbNodes];
		for (int i = 0; i < neighbor.length; i++) {
			neighbor[i] = new ArrayList<>();
			reverseNeighbors[i] = new ArrayList<>();
			degreeOut[i] = new ArrayList<>();
			degreeOut[0].add(i);
			degreeIn[i] = new ArrayList<>();
		}
	}

	@Override
	public void addLink(int src, int dst) {
		assert src < this.getNbNodes();
		assert dst < this.getNbNodes();
		assert neighbor != null;
		assert src < neighbor.length;
		neighbor[src].add(dst);
		reverseNeighbors[dst].add(src);
		assert neighbor[src].size() > 0;
		degreeOut[neighbor[src].size() - 1].remove(new Integer(src));
		degreeOut[neighbor[src].size()].add(src);
		degreeIn[dst].add(src);
	}

	@Override
	public boolean isLinked(int src, int dst) {
		return neighbor[src].contains(dst);

	}

	@Override
	public int degreeOut(int node) {
		return neighbor[node].size();
	}

	@Override
	public int degreeIn(int node) {
		return reverseNeighbors[node].size();
	}

	@Override

	public ArrayList<Integer>[] getReverseNeighbors() {
		return reverseNeighbors;
	}

	@Override
	public int getNbNodes() {
		return neighbor.length;
	}

	@Override
	public ArrayList<Integer>[] getDegreeOutToNode() {
		return degreeOut;
	}

	@Override
	public ArrayList<Integer> getNeigbors(int node) {
		return neighbor[node];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("#Node:" + this.getNbNodes());
		for (int i = 0; i < getNbNodes(); i++) {
			sb.append(Arrays.toString(this.neighbor[i].toArray()) + System.lineSeparator());
		}
		;
		return sb.toString();
	}

	@Override
	public void writeInto(File f) {
		try {
			FileWriter fw = new FileWriter(f, true); // the true will append the
														// new data
			fw.write("digraph graphname {" + System.lineSeparator());// appends
																		// the
																		// string
																		// to
																		// the
																		// file
			for (int i = 0; i < getNbNodes(); i++) {
				fw.write(i + ";" + System.lineSeparator());
			}
			for (int i = 0; i < getNbNodes(); i++) {
				for (int j = 0; j < this.neighbor[i].size(); j++) {
					fw.write(i + " -> " + this.neighbor[i].get(j) + ";" + System.lineSeparator());
				}
			}
			fw.write("}");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	@Override
	public ArrayList<Integer>[] getDegreeInToNode() {
		return degreeIn;
	}

}
