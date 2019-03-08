package org.huawei.conga.search.builder;

import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.huawei.conga.CongaSolver;

public enum SearchStrategyBuilder implements ISearchBuilder {
	LEX_ORDER_MIN(new LexMinBuilder()),
	LEX_ORDER_MAX(new LexMaxBuilder()),
	DOM_W_DEG(new DomWDegBuilder()),
	IMPACT(new ImpactSearchBuilder()),
	ACTIVITY(new ActivityBuilder()),
	FIRST_FAIL(new FirstFailBuilder()),
	RANDOM(new RandomBuilder()),
	MINDOM_LB(new MinDomLBBuilder()),
	MINDOM_UB(new MinDomUBBuilder());
	
	
	

	
	private ISearchBuilder builder;
	
	private SearchStrategyBuilder(ISearchBuilder builder) {
		this.builder=builder;
	}
	
	@Override
	public AbstractStrategy buildSearchStrat(CongaSolver cg) {
		return builder.buildSearchStrat(cg);
	}
	
	public static SearchStrategyBuilder retrieveSearch(String elem){
		for (SearchStrategyBuilder iterable_element : SearchStrategyBuilder.values()) {
			if(elem.equals(iterable_element.name())){
				return iterable_element;
			}
		}
		return null;
	}

}
