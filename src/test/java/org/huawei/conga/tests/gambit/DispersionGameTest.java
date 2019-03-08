package org.huawei.conga.tests.gambit;


import org.junit.Test;

public class DispersionGameTest extends AbstractValidityGambitTestClass {
	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "DispersionGame/";

	@Test
	public final void testDispersionGame_2_2() {
		this.testGame(PREFIX + "DispersionGame_2_2.nfg");
	}

	@Test
	public final void testDispersionGame_2_5() {
		this.testGame(PREFIX + "DispersionGame_2_5.nfg");
	}

	@Test
	public final void testDispersionGame_3_5() {
		this.testGame(PREFIX + "DispersionGame_3_5.nfg");
	}

	@Test
	public final void testDispersionGame_4_4() {
		this.testGame(PREFIX + "DispersionGame_4_4.nfg");
	}
}
