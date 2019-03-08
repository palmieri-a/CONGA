package org.huawei.conga.tests.gambit;


import org.junit.Test;

public class GrabTheDollarTest extends AbstractValidityGambitTestClass {

	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "GrabTheDollarGame/";

	@Test
	public final void testGrabTheDollar_4() {
		this.testGame(PREFIX + "GrabTheDollar_4.nfg");
	}

	@Test
	public final void testGrabTheDollar_16() {
		this.testGame(PREFIX + "GrabTheDollar_16.nfg");
	}
}
