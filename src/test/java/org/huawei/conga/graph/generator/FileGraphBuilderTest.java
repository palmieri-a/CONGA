package org.huawei.conga.graph.generator;

import java.io.File;

import org.huawei.datastructure.graph.builder.FileGraphBuilder;
import org.junit.Before;
import org.junit.Test;

public class FileGraphBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testBuildSparseGraph() {
		final String path ="resources/graph/rnd20_Pat1.dat.dataGraph.txt";
		FileGraphBuilder fgb = new FileGraphBuilder(new File(path));
		fgb.build();

	}

}
