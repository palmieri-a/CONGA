package org.huawei.conga.tests.gambit;


import org.junit.Test;

public class GuessTwoThirdAveTest extends AbstractValidityGambitTestClass {
	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "GuessTwoThirdAverageGame/";

	

	@Test
	public final void testGuessTwoThirdsAve_2_4() {
		this.testGame(PREFIX + "GuessTwoThirdsAve_2_4.nfg");
	}

	@Test
	public final void testGuessTwoThirdsAve_2_8() {
		this.testGame(PREFIX + "GuessTwoThirdsAve_2_8.nfg");
	}

	@Test
	public final void testGuessTwoThirdsAve_3_3() {
		this.testGame(PREFIX + "GuessTwoThirdsAve_3_3.nfg");
	}

	@Test
	public final void testGuessTwoThirdsAve_3_8() {
		this.testGame(PREFIX + "GuessTwoThirdsAve_3_8.nfg");
	}

	@Test
	public final void testGuessTwoThirdsAve_4_4() {
		this.testGame(PREFIX + "GuessTwoThirdsAve_4_4.nfg");
	}

	@Test
	public final void testGuessTwoThirdsAve_4_8() {
		this.testGame(PREFIX + "GuessTwoThirdsAve_4_8.nfg");
	}
	
//	@Test
//	public final void testGuessTwoThirdsAve_10_10() {
//		this.testGame(PREFIX + "GuessTwoThirdsAve_10_10.nfg");
//	}
}
