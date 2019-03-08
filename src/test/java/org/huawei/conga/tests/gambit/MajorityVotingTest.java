package org.huawei.conga.tests.gambit;

import org.junit.Test;

public class MajorityVotingTest extends AbstractValidityGambitTestClass {
	private static final String PREFIX = AbstractValidityGambitTestClass.PREFIX+  "MajorityVotingGame/";


	@Test
	public final void testMajorityVoting_2_2() {
		this.testGame(PREFIX + "MajorityVoting_2_2.nfg");
	}

	@Test
	public final void testMajorityVoting_3_3() {
		this.testGame(PREFIX + "MajorityVoting_3_3.nfg");
	}

	@Test
	public final void testMajorityVoting_3_7() {
		this.testGame(PREFIX + "MajorityVoting_3_7.nfg");
	}

	@Test
	public final void testMajorityVoting_4_4() {
		this.testGame(PREFIX + "MajorityVoting_2_2.nfg");
	}
}
