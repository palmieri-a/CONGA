package org.huawei.datastructure.graph.builder;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.huawei.datastructure.graph.AdjacencyListGraph;
import org.huawei.datastructure.graph.IGraph;

public class ParserDatFile implements GraphBuilder {

	private String path;
	private IGraph graph;

	public ParserDatFile(String file) {
		this.path = file;
	}

	@Override
	public void build() {
		Path file = java.nio.file.Paths.get(path);
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
			String line = reader.readLine();
			Scanner s = new Scanner(line);
			// read first line
			int nbNodes = s.nextInt();
			int nbEdges = s.nextInt();
			// for each node
			for (int i = 0; i < nbNodes; i++) {
				reader.readLine();
			}
			// for each edges
			this.graph = new AdjacencyListGraph(nbNodes);
			for (int i = 0; i < nbEdges; i++) {
				int src, dst;
				line = reader.readLine();
				s = new Scanner(line);
				src = s.nextInt()-1;
				dst = s.nextInt()-1;
				graph.addLink(src, dst);
				graph.addLink(dst, src);
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	@Override
	public IGraph getGraph() {
		if (this.graph == null) {
			build();
		}
		return this.graph;
	}

}
