package org.huawei.conga.tests.gambit;

import org.junit.Test;

public class NPlayerChikenTest extends AbstractValidityGambitTestClass {
	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "NPlayerChikenGame/";


	@Test
	public final void testNPlayerChicken_2_2() {
		this.testGame(PREFIX + "NPlayerChicken_2_2.nfg");
	}

	@Test
	public final void testNPlayerChicken_3_3() {
		this.testGame(PREFIX + "NPlayerChicken_3_3.nfg");
	}

	@Test
	public final void testNPlayerChicken_4_4() {
		this.testGame(PREFIX + "NPlayerChicken_4_4.nfg");
	}

}
