package org.huawei.conga.search.builder;

import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.huawei.conga.CongaSolver;

public interface ISearchBuilder {

	AbstractStrategy buildSearchStrat(CongaSolver cg);
}
