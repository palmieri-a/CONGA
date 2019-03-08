package org.huawei.conga.search.builder;

import org.chocosolver.solver.search.strategy.selectors.variables.ImpactBased;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.huawei.conga.CongaSolver;

public class ImpactSearchBuilder implements ISearchBuilder{

	@Override
	public AbstractStrategy buildSearchStrat(CongaSolver cg) {
		return new ImpactBased(cg.retrieveIntDecisionVar(true), false);//.inputOrderUBSearch(cg.getVarHelper().getDecisionVars());
	}

}
