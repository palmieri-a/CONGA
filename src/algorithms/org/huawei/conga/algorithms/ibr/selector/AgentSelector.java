package org.huawei.conga.algorithms.ibr.selector;

import java.util.Random;

import org.huawei.conga.model.beans.Agent;

public interface AgentSelector {
	final Random r = new Random();
	
	default int nextAgent(Agent[]agents){
		return r.nextInt(agents.length - 1)+1;
	}

}
