package org.huawei.conga.equilibria.nash.verificator;

import org.huawei.conga.model.beans.Agent;

public class NaiveLexVerificationOrder implements VerificationStrategy {
	private int i = 0;
	private Agent[] agents;

	public NaiveLexVerificationOrder(Agent[] agents) {
		this.agents = agents;
	}

	@Override
	public Agent next() {
		i++;
		return this.agents[i % agents.length];
	}

	@Override
	public void reset() {
		i = 0;
	}

}
