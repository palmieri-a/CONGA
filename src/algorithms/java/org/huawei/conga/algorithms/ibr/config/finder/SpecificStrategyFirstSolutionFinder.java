package org.huawei.conga.algorithms.ibr.config.finder;

import org.chocosolver.solver.Solver;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.search.builder.ISearchBuilder;
import org.huawei.conga.search.builder.RandomBuilder;

/**
 * This class is a IBR parameter to define how the first configuration is sought.
 * By default a random search is done. However knowing the problem, it is often 
 * better to change this first strategy with a one specific to it.
 * @author anthony
 *
 */
public class SpecificStrategyFirstSolutionFinder implements IBRFirstConfigurationFinder {

	private final ISearchBuilder stratBuilder;

	public SpecificStrategyFirstSolutionFinder(final ISearchBuilder stratBuilder) {
		this.stratBuilder = stratBuilder;
	}
	
	public SpecificStrategyFirstSolutionFinder() {
		this.stratBuilder= new RandomBuilder();
	}

	@Override
	public void findFirstConfig(CongaSolver cg) {
		Solver s = cg.getSolver();
		// find a first solution
		s.setSearch(stratBuilder.buildSearchStrat(cg));
		if (!s.solve()) {
			System.err.println("no first reachable config");
		}
	}

}
