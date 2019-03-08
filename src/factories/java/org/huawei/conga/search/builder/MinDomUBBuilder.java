package org.huawei.conga.search.builder;

import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.huawei.conga.CongaSolver;

public class MinDomUBBuilder implements ISearchBuilder {

	@Override
	public AbstractStrategy buildSearchStrat(CongaSolver cg) {
		return Search.minDomUBSearch(cg.retrieveIntDecisionVar(true));
	}

}
