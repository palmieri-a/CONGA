package org.huawei.conga.tests.gambit;

import org.junit.Test;

public class CollaborationGameValidityTest extends AbstractValidityGambitTestClass {

	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "CollaborationGame/";

	@Test
	public final void testCollaboration_2() {
		this.testGame(PREFIX + "CollaborationGame_2.nfg");
	}

	@Test
	public final void testCollaboration_3() {
		this.testGame(PREFIX + "CollaborationGame_3.nfg");
	}

	@Test
	public final void testCollaboration_4() {
		this.testGame(PREFIX + "CollaborationGame_4.nfg");
	}

}
