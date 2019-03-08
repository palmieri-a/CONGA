package org.huawei.conga.graph.generator;


import org.huawei.datastructure.graph.IGraph;

public interface GeneratorType {
	
	IGraph Generate(int player,boolean isDirected);

}
