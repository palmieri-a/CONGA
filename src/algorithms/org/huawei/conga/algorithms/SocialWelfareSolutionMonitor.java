package org.huawei.conga.algorithms;

import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;

public class SocialWelfareSolutionMonitor implements IMonitorSolution {

	private Variable v;
	private double lb;
	private double ub;

	public SocialWelfareSolutionMonitor(Variable v) {
		this.v = v;
		lb = Integer.MAX_VALUE;
		ub = Integer.MIN_VALUE;
	}

	@Override
	public void onSolution() {
		if(lb == Integer.MAX_VALUE || ub == Integer.MIN_VALUE){
			if ((v.getTypeAndKind() & Variable.INT) != 0) {
					ub = ((IntVar) v).getValue();
					lb = ((IntVar) v).getValue();
			} else {
					ub = ((RealVar) v).getUB();
					lb = ((RealVar) v).getLB();
			}
			return;
		}else 
		if ((v.getTypeAndKind() & Variable.INT) != 0) {
			if (((IntVar) v).getValue() > ub) {
				ub = ((IntVar) v).getValue();
			} else if (((IntVar) v).getValue() < lb) {
				lb = ((IntVar) v).getValue();
			}
		} else {
			if (((RealVar) v).getLB() > ub) {
				ub = ((RealVar) v).getUB();
			} else if (((RealVar) v).getUB() < lb) {
				lb = ((RealVar) v).getLB();
			}
		}
	}

	public double getLb() {
		return lb;
	}

	public double getUb() {
		return ub;
	}
}