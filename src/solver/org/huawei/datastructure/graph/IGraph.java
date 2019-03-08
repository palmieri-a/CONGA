package org.huawei.datastructure.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public interface IGraph {

	void addLink(int src, int dst);

	boolean isLinked(int src, int dst);

	int degreeOut(int node);

	int getNbNodes();

	ArrayList<Integer>[] getDegreeOutToNode();

	ArrayList<Integer>[] getDegreeInToNode();
	
	ArrayList<Integer> getNeigbors(int node);

	int degreeIn(int node);

	ArrayList<Integer>[] getReverseNeighbors();

	void writeInto(File f);
	
	default void writeFileBench(String f){
		Path file=Paths.get(f);
		StringBuilder sb= new StringBuilder();
		sb.append(this.getNbNodes());
		sb.append(System.lineSeparator());
		for (int i = 0; i < this.getNbNodes(); i++) {
			toString(sb,this.getNeigbors(i));
			sb.append(System.lineSeparator());
		}
		try {
			Files.createFile(file);
			Files.write(file, sb.toString().getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	default void toString(StringBuilder sb, ArrayList<Integer> neigbors){
		for (int i = 0; i < neigbors.size(); i++) {
			sb.append(neigbors.get(i));
			sb.append(" ");
		}
	}

}
