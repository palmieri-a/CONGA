package org.huawei.conga.adoc.algorithms.builder;


import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithm.welfare.function.Fairness;
import org.huawei.conga.algorithm.welfare.function.Utilitarian;
import org.huawei.conga.algorithms.Executable;

public enum AlgorithmFactory implements ExecutableBuilder{
	IBR(new IBRBuilder() ),
	
	PARETO_NASH(new ParetoNashBuilder()),
	
	PRICE_OF_ANARCHY_UTILITARIAN(new PriceOfAnarchyBuilder(new Utilitarian(true)) ),
	PRICE_OF_ANARCHY_FAIRNESS(new PriceOfAnarchyBuilder(new Fairness(true)) ),
	PRICE_OF_STABILITY_UTILITARIAN(new PriceOfStabilityBuilder(new Utilitarian(true))),
	PRICE_OF_STABILITY_FAIRNESS(new PriceOfStabilityBuilder(new Fairness(true))),

	COMPLETE_SEARCH(new CompleteSearchBuilder());
	

	private ExecutableBuilder eb;
	private AlgorithmFactory(ExecutableBuilder eb) {
		this.eb=eb;
	}
	@Override
	public Executable build(CongaSolver cg) {
		return eb.build(cg);
	}
	
	public static ExecutableBuilder retrieveAlgo(String string) {
		ExecutableBuilder cb = COMPLETE_SEARCH;
		for (int i = 0; i < AlgorithmFactory.values().length; i++) {
			if (AlgorithmFactory.values()[i].name().equals(string)) {
				return AlgorithmFactory.values()[i];
			}
		}
		return cb;
	}
	
}

