package org.huawei.conga.search.policy.builder;

import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.search.policy.BetterSolutionSearchPolicy;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.conga.search.policy.ParetoSearchPolicy;

public class AllBRBuilder implements ISearchPolicyBuilder {

	@Override
	public ISearchPolicy build(Agent a) {
		assert a!=null;
		if (a.getID()== 0 || null==a.getObjectiveCSP() || a.getObjectiveCSP().length <= 1) {
			return new BetterSolutionSearchPolicy();
		} else {
			return new ParetoSearchPolicy();
		}

	}

}
