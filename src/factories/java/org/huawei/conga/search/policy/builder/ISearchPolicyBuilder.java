package org.huawei.conga.search.policy.builder;

import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.search.policy.ISearchPolicy;

public interface ISearchPolicyBuilder {
	public ISearchPolicy build(Agent agent);
}
