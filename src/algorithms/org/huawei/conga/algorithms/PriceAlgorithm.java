package org.huawei.conga.algorithms;

import org.huawei.conga.CongaSolver;
import org.huawei.conga.adoc.algorithms.computers.BestCentralizedValueComputer;
import org.huawei.conga.adoc.algorithms.computers.BestEquilibriumComputer;
import org.huawei.conga.adoc.algorithms.computers.IComputer;
import org.huawei.conga.algorithm.welfare.function.IWelfareCreator;

/**
 * Given a social welfare function, The Price of Anarchy is then defined as the
 * ratio between the optimal 'centralized' solution and the 'worst equilibrium'.
 * Price of stability has the same definition but is computed with the best
 * equilibrium.
 * 
 * @author anthony
 *
 */
public class PriceAlgorithm extends PricingComputer implements Executable {

	private double equilibriumValue = 0.0;
	private double poa;
	private boolean isArnarchy;

	public PriceAlgorithm(CongaSolver cg, IWelfareCreator iwc, boolean isArnarchy) {
		super(cg, iwc);
		this.isArnarchy = isArnarchy;
	}

	@Override
	public void execute() {
		// find the minimal nash equilibria according this function
		equilibriumValue = computeEq();
		// reset
		cg.getGameModel().reset();
		// compute the best centralized solution
		best = computeBestCentralizedValue();
		poa = equilibriumValue / best;
	}

	private double computeBestCentralizedValue() {
		IComputer bestVComputer = new BestCentralizedValueComputer(cg.getGameModel(), iwc);
		bestVComputer.execute();
		return bestVComputer.getValue();
	}

	private double computeEq() {
		IComputer bestEqComputer = new BestEquilibriumComputer(cg, iwc, !isArnarchy);
		bestEqComputer.execute();
		return bestEqComputer.getValue();
	}

	public double getPoa() {
		return poa;
	}

	public double getEquilibrium() {
		return equilibriumValue;
	}

	public double getBestCentralized() {
		return best;
	}

	@Override
	public String toString() {
		return ";" + equilibriumValue + ";;" + best + ";";
	}
}
