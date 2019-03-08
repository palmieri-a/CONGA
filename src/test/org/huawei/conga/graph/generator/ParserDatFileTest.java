package org.huawei.conga.graph.generator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.huawei.datastructure.graph.IGraph;
import org.huawei.datastructure.graph.builder.ParserDatFile;
import org.junit.Test;

public class ParserDatFileTest {

	@Test
	public void testReadSimpleFile() {
		final String FILE_NAME = "usecase-7-node-4lsp.dat";
		ParserDatFile parser = new ParserDatFile("src/test/resources/" + FILE_NAME);
		IGraph graph = parser.getGraph();
		assertEquals(graph.getNbNodes(), 7);
		assertTrue(graph.isLinked(0, 1));
		assertTrue(graph.isLinked(0, 1));
		
		assertTrue(graph.isLinked(1, 6));
		assertTrue(graph.isLinked(6, 1));	

		assertTrue(graph.isLinked(6, 4));
		assertTrue(graph.isLinked(4, 6));
		
		assertTrue(graph.isLinked(4, 5));
		assertTrue(graph.isLinked(5, 4));
		
		assertTrue(graph.isLinked(6, 3));
		assertTrue(graph.isLinked(3, 6));
		
		assertTrue(graph.isLinked(0, 2));
		assertTrue(graph.isLinked(2, 0));
		
		assertTrue(graph.isLinked(2, 4));
		assertTrue(graph.isLinked(4, 2));
	}
}
