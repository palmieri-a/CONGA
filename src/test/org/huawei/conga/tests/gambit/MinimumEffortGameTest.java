package org.huawei.conga.tests.gambit;

import org.junit.Test;

public class MinimumEffortGameTest extends AbstractValidityGambitTestClass {
	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "MinimumEffortGame/";


	@Test
	public final void testMinimumEffort_2_2_3_25_15() {
		this.testGame(PREFIX + "MinimumEffort_2_2_3_25_15.nfg");
	}

	@Test
	public final void testMinimumEffort_2_5_3_25_15() {
		this.testGame(PREFIX + "MinimumEffort_2_5_3_25_15.nfg");
	}

	@Test
	public final void testMinimumEffort_4_2_3_25_15() {
		this.testGame(PREFIX + "MinimumEffort_4_2_3_25_15.nfg");
	}

	@Test
	public final void testMinimumEffort_5_2_3_25_15() {
		this.testGame(PREFIX + "MinimumEffort_5_2_3_25_15.nfg");
	}

}
