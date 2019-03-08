package org.huawei.conga.search.builder;

import java.time.ZonedDateTime;

import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.huawei.conga.CongaSolver;

public class RandomBuilder implements ISearchBuilder {

	@Override
	public AbstractStrategy buildSearchStrat(CongaSolver cg) {
		return Search.randomSearch(cg.retrieveIntDecisionVar(true), ZonedDateTime.now().toEpochSecond());
	}

}
