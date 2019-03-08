package org.huawei.datastructure.graph;

import java.util.Arrays;

public class WeightedGraph {

	private final int nodes;
	private final int[] edgesBegin;
	private final int[] edgeTo;
	private final double[] costs;
	private final int cap[];

	// for iterator
	private int nodesIdx = -1;
	private int neighbordsIdx = -1;

	// on peut transformer ca en datastrure etc.. peut etre utile pour ?
	public WeightedGraph(int nbNodes, double costs[], int cap[], int edgeTo[], int[] edgesBegin) {
		nodes = nbNodes;
		this.edgesBegin = edgesBegin;
		this.edgeTo = edgeTo;
		this.cap = cap;
		this.costs = costs;
	}

	public int getCapacity(final int src, final int dst) {
		for (int i = edgesBegin[src]; i < edgesBegin[src + 1]; i++) {
			if (edgeTo[i] == dst) {
				return cap[i];
			}
		}
		return 0;
		
	}

	public double getCost(final int src, final int dst) {
		assert getCapacity(src, dst) > 0;
		for (int i = edgesBegin[src]; i < edgesBegin[src + 1]; i++) {
			if (edgeTo[i] == dst) {
				return costs[i];
			}
		}
		return 0;
	}

	public int getIdx(final int src, final int dst) {
		assert getCapacity(src, dst) > 0;
		for (int i = edgesBegin[src]; i < edgesBegin[src + 1]; i++) {
			if (edgeTo[i] == dst) {
				return i;
			}
		}
		return -1;
	}

	public void iterateOverNeighbordsOf(final int vertex) {
		this.nodesIdx = vertex;
		this.neighbordsIdx = 0;
	}

	public boolean hasNext() {
		return neighbordsIdx != -1 && edgesBegin[nodesIdx] + neighbordsIdx < edgesBegin[nodesIdx + 1];
	}

	public int getCurrent() {
		return this.neighbordsIdx + edgesBegin[nodesIdx];
	}

	public int next() {
		assert hasNext();
		return this.edgeTo[neighbordsIdx++ + edgesBegin[nodesIdx]];
	}

	public double nextCost() {
		return this.costs[neighbordsIdx + edgesBegin[nodesIdx]];
	}

	public int nextCap() {
		return this.cap[neighbordsIdx + edgesBegin[nodesIdx]];
	}

	public int getNbNodes() {
		return nodes;
	}

	public int[] getNeighbords(final int n) {
		return Arrays.copyOfRange(edgeTo, edgesBegin[n], edgesBegin[n + 1]);
	}

	public double[] getCosts() {
		return costs;
	}

	public int[] getCapacities() {
		return cap;
	}

	public int getNBedges() {
		return this.cap.length;
	}

	public int[] getEdgeTo() {
		return edgeTo;
	}

	public int[] getEdgesBegin() {
		return edgesBegin;
	}

	public int getIndexBegin(int v) {
		return edgesBegin[v];
	}

	public int getIndexEnd(int v) {
		return edgesBegin[v + 1];
	}

	public int getNbNeighbors(int v) {
		return edgesBegin[v + 1] - edgesBegin[v];
	}

	public int retrieveIdxNodeSrc(int idxEdge) {
		for (int i = 0; i < edgesBegin.length; i++) {
			if(idxEdge>=edgesBegin[i] && idxEdge<edgesBegin[i+1] ){
				return i;
			}
		}
		return -1;
	}

}
