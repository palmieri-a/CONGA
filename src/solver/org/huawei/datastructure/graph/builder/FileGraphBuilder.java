
package org.huawei.datastructure.graph.builder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.huawei.datastructure.graph.AdjacencyListGraph;
import org.huawei.datastructure.graph.IGraph;

public class FileGraphBuilder implements GraphBuilder {

	private IGraph graph;
	private File file;
	int scc[][];

	public FileGraphBuilder(File f) {
		this.file = f;
	}

	@Override
	public void build() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			@SuppressWarnings("resource")
			Scanner s = new Scanner(line);
			this.graph = new AdjacencyListGraph(s.nextInt());
			int src = 0;
			line = br.readLine();
			while (line != null) {
				@SuppressWarnings("resource")
				Scanner lineScanner = new Scanner(line);
				while (lineScanner.hasNextInt()) {
					graph.addLink(src, lineScanner.nextInt());
				}
				src++;
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IGraph getGraph() {
		if(this.graph==null){
			this.build();
		}
		return this.graph;
	}

	public void writeInFile(File f) {
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.graph.getNbNodes() + System.lineSeparator());
			for (int i = 0; i < this.graph.getNbNodes(); i++) {
				for (int j = 0; j < graph.getNeigbors(i).size(); j++) {
					bw.write(this.graph.getNeigbors(i).get(j));
				}
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
