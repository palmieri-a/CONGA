package org.huawei.conga.tests.gambit;


import org.junit.Test;

public class TravelerDilemmaTest extends AbstractValidityGambitTestClass {
	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "TravelerDilemmaGame/";

	@Test
	public final void testTravelerDilemma2_3() {
		this.testGame(PREFIX + "travelerDilemma2_3.nfg");
	}

	@Test
	public final void testTravelerDilemma2_4() {
		this.testGame(PREFIX + "travelerDilemma2_4.nfg");
	}

	@Test
	public final void testTravelerDilemma2_5() {
		this.testGame(PREFIX + "travelerDilemma2_5.nfg");
	}

	@Test
	public final void testTravelerDilemma3_2() {
		this.testGame(PREFIX + "travelerDilemma3_2.nfg");
	}

}
