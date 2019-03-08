package org.huawei.conga.graph.generator;

/**
 * 
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.huawei.datastructure.graph.AdjacencyListGraph;
import org.huawei.datastructure.graph.IGraph;

import princeton.MinPQ;

/**
 * @author a80048597
 *
 */
public enum Generator {

	INSTANCE;
	public enum TYPE implements GeneratorType {
		CIRCLE {
			@Override
			public IGraph Generate(int nbNodes, boolean isDirected) {
				IGraph graph = new AdjacencyListGraph(nbNodes);
				for (int i = 0; i < nbNodes; i++) {
					graph.addLink(i, (i + 1) % nbNodes);
					if (!isDirected) {
						graph.addLink((i + 1) % nbNodes, i);
					}
				}
				return graph;
			}
		}

		,
		TREE {

			@Override
			public IGraph Generate(int player, boolean isDirected) {
				IGraph G = null;
				do {
					G = new AdjacencyListGraph(player);
					Random r = new Random(System.nanoTime());
					// special case

					if (player == 1)
						return G;
					// Cayley's theorem: there are V^(V-2) labeled trees on V
					// vertices
					// Prufer sequence: sequence of V-2 values between 0 and V-1
					// Prufer's proof of Cayley's theorem: Prufer sequences are
					// in 1-1
					// with labeled trees on V vertices
					int[] prufer = new int[player - 2];
					int maxDeg = r.nextInt(player);
					for (int i = 0; i < player - 2; i++)
						prufer[i] = r.nextInt(player);

					int index = r.nextInt(prufer.length);

					for (int i = index / 2; (i < index / 2 + maxDeg) && i < prufer.length; i++) {
						prufer[i] = prufer[index];
					}

					// degree of vertex v = 1 + number of times it appers in
					// Prufer sequence
					int[] degree = new int[player];
					for (int v = 0; v < player; v++)
						degree[v] = 1;
					for (int i = 0; i < player - 2; i++)
						degree[prufer[i]]++;

					// pq contains all vertices of degree 1
					MinPQ<Integer> pq = new MinPQ<Integer>();
					for (int v = 0; v < player; v++)
						if (degree[v] == 1)
							pq.insert(v);

					// repeatedly delMin() degree 1 vertex that has the minimum
					// index
					for (int i = 0; i < player - 2; i++) {
						int v = pq.delMin();
						G.addLink(v, prufer[i]);
						if (!isDirected) {
							G.addLink(prufer[i], v);
						}
						degree[v]--;
						degree[prufer[i]]--;
						if (degree[prufer[i]] == 1)
							pq.insert(prufer[i]);
					}
					int v1 = pq.delMin();
					int v2 = pq.delMin();
					G.addLink(v1, v2);
					G.addLink(v2, v1);
				} while (evalMaxDegree(G) != MAX_DEGREE);
				return G;
			}

			private int evalMaxDegree(IGraph g) {
				int maxD = 0;
				for (int i = 0; i < g.getNbNodes(); i++) {
					maxD = Math.max(g.degreeOut(i), maxD);
				}
				MAX_DEGREE = maxD;
				return maxD;
			}
		},

		REGULAR_TREE {
			@Override
			public IGraph Generate(int player, boolean isDirected) {
				IGraph graph = new AdjacencyListGraph(player);
				createChild(graph, 0, LEVEL, MAX_DEGREE, 0, isDirected);
				return graph;
			}

			private int createChild(final IGraph graph, int level, int levelMax, final int deg, final int currentChild,
					final boolean isDirected) {
				int currentElem = currentChild + 1;
				if (level + 1 < levelMax) {
					for (int i = 0; i < deg; i++) {
						if (!isDirected) {
							System.out.println(currentChild + "->" + currentElem);
							graph.addLink(currentChild, currentElem);
							graph.addLink(currentElem, currentChild);
						}
						currentElem = createChild(graph, level + 1, levelMax, deg, currentElem, isDirected);
					}
				}
				return currentElem;
			}
		},
		GRID {
			@Override
			public IGraph Generate(int player, boolean isDirected) {
				int height = player / 4;
				int width = 4;
				IGraph graph = new AdjacencyListGraph(height * width);
				for (int i = 0; i < height * width; i++) {
					int previous = i - 1;
					int next = i + 1;
					int bottom = i + width;
					int top = i - width;
					// exist previous
					if (previous >= 0 && (i % width) != 0) {
						graph.addLink(i, previous);

					}
					// have next?
					if (next < height * width && ((i + 1) % (width)) != 0) {
						graph.addLink(i, next);

					}
					// bottom?
					if (bottom > 0 && i < width * (height - 1)) {
						graph.addLink(i, bottom);
					}
					// on top?
					if (i >= width && i >= width) {
						graph.addLink(i, top);
					}

				}
				return graph;
			}
		},
		BIPARTITE {
			@Override
			public IGraph Generate(int player, boolean isDirected) {

				int nbNodes_gp1 = player / 2;
				int nbNodes_gp2 = player - nbNodes_gp1;
				IGraph graph = new AdjacencyListGraph(nbNodes_gp1 + nbNodes_gp2);// [0;nbNodes_gp1+nbNodes_gp2[

				// for each node in group 1
				for (int i = 0; i < nbNodes_gp1; i++) {
					// for each node in group 2
					for (int j = nbNodes_gp1; j < player; j++) {
						if (j != i) {
							graph.addLink(i, j);
						}
					}
				}
				// for each node in group 2
				for (int i = nbNodes_gp1; i < graph.getNbNodes(); i++) {
					// for each node in group 1
					for (int j = 0; j < nbNodes_gp1; j++) {
						if (j != i) {
							graph.addLink(i, j);
						}

					}
				}
				return graph;
			}
		},
		STAR {
			@Override
			public IGraph Generate(int nbNodes, boolean isDirected) {
				IGraph graph = new AdjacencyListGraph(nbNodes);
				for (int i = 1; i < nbNodes; i++) {
					graph.addLink(0, i);
					if (!isDirected) {
						graph.addLink(i, 0);
					}
				}
				return graph;
			}
		},
		RANDOM_DAG {
			@Override
			public IGraph Generate(int nbNodes, boolean isDirected) {
				IGraph graph = new AdjacencyListGraph(nbNodes);
				Random r = new Random(System.nanoTime());
				final int E = r.nextInt(nbNodes * (nbNodes - 1) / 2);
				int edges = 0;
				while (edges < E) {
					int v = r.nextInt(nbNodes);
					int w = r.nextInt(nbNodes);
					if ((v < w) && !graph.isLinked(v, w)) {
						graph.addLink(v, w);
						edges++;
					}
				}
				return graph;
			}
		},
		RANDOM {
			int nbLinks;

			@Override
			public IGraph Generate(int nbNodes, boolean isDirected) {
				IGraph graph = new AdjacencyListGraph(nbNodes);
				nbLinks = nbNodes * (nbNodes / 2);
				Random r = new Random(System.nanoTime());
				for (int i = 0; i < nbLinks; i++) {
					int src = r.nextInt(nbNodes);
					int dst = r.nextInt(nbNodes);
					while (src == dst || graph.isLinked(src, dst)) {
						src = r.nextInt(nbNodes);
						dst = r.nextInt(nbNodes);
					}
					// while (graph.getNeigbors(src).size() == nbLinks) {
					// src = r.nextInt(nbNodes);
					// }
					System.out.println(src + "->" + dst);
					graph.addLink(src, dst);
				}
				return graph;
			}
		};

	}

	public IGraph generateRandom(int nbNodes, boolean isDirected) {
		return TYPE.RANDOM.Generate(nbNodes, isDirected);
	}

	public IGraph generateGrid(int player, boolean isDirected) {
		return TYPE.GRID.Generate(player, isDirected);
	}

	public IGraph generateCircle(int nbNodes, boolean isDirected) {
		return TYPE.CIRCLE.Generate(nbNodes, isDirected);
	}

	public IGraph generateBiPartite(int player, boolean isDirected) {
		return TYPE.BIPARTITE.Generate(player, isDirected);
	}

	public IGraph generateStar(int nbNodes, boolean isDirected) {
		return TYPE.STAR.Generate(nbNodes, isDirected);
	}

	public IGraph generateRandomDag(int nbNodes, boolean isDirected) {
		return TYPE.RANDOM_DAG.Generate(nbNodes, isDirected);
	}

	public IGraph generateTree(int nbNodes, boolean isDirected) {
		return TYPE.TREE.Generate(nbNodes, isDirected);
	}

	public IGraph generateTreeRegular(int nbNodes, boolean isDirected) {
		return TYPE.REGULAR_TREE.Generate(nbNodes, isDirected);
	}

	public void writeInFile(File f, IGraph graph) {
		try {
			FileWriter fw = new FileWriter(f, true); // the true will append the
														// new data
			fw.write(graph.getNbNodes() + System.lineSeparator());
			for (int i = 0; i < graph.getNbNodes(); i++) {
				for (int k = 0; k < graph.getNeigbors(i).size(); k++) {
					fw.write(graph.getNeigbors(i).get(k) + " ");
				}
				fw.write(System.lineSeparator());
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	protected static int MAX_DEGREE = 10;
	protected static int  LEVEL= 3;

}
