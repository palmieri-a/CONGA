package org.huawei.conga.algorithms;

import org.chocosolver.solver.Solver;
import org.huawei.conga.CongaSolver;

public class Nash implements Executable {

	private CongaSolver cg;

	public Nash(CongaSolver cg) {
		this.cg = cg;
	}

	@Override
	public void execute() {
		Solver s =cg.prepareAndGetSolver();
		if(s.solve()){
			while(s.solve()){
			}
		}
	}
	//Model[CSP], 1 Solutions, Resolution time 0,104s, 95 Nodes (910,9 n/s), 189 Backtracks, 94 Fails, 0 Restarts
	
	@Override
	public String toString() {
		return ";;;;";
	}

}
