package org.huawei.conga.choco.addon.constraint;

import java.util.LinkedList;
import java.util.List;

import org.chocosolver.sat.PropSat;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Operator;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;

public class ParetoFrontMixedVar implements IMonitorSolution {

	// ***********************************************************************************
	// VARIABLES
	// ***********************************************************************************

	// Set of incomparable and Pareto-best solutions
	private LinkedList<Solution> paretoFront;

	private Model model;

	// Allow to recycle (dominated) Solution objects
	private LinkedList<Solution> pool = new LinkedList<>();

	// objective function
	private boolean maximize;
	private Variable[] objectives;
	private int n;

	// to post dynamical constraints
	private double[] vals;
	private int lits[];
	private PropSat psat;

	// ***********************************************************************************
	// CONSTRUCTOR
	// ***********************************************************************************

	/**
	 * Create an object to compute the Pareto front of a multi-objective
	 * problem. Maintain the set of dominating solutions and posts constraints
	 * dynamically to prevent search from computing dominated ones. This object
	 * must be used as follows:
	 *
	 * model.getSolver().plugMonitor(paretoRecorder);
	 * while(model.getSolver().solve()); List<Solution> paretoFront =
	 * paretoRecorder.getParetoFront();
	 *
	 * The Solutions store decision variables (those declared in the search
	 * strategy) BEWARE: requires the objectives to be declared in the search
	 * strategy
	 *
	 * @param maximize
	 *            whether to maximize or minimize the objectives
	 * @param objectives
	 *            objective variables (must all be optimized in the same
	 *            direction)
	 */
	public ParetoFrontMixedVar(final boolean maximize, final Variable[] objectives) {
		this.paretoFront = new LinkedList<>();
		this.objectives = objectives.clone();
		this.maximize = maximize;
		n = objectives.length;
		model = objectives[0].getModel();
		psat = model.getMinisat().getPropSat();
		vals = new double[n];
		lits = new int[n];
	}

	// ***********************************************************************************
	// METHODS
	// ***********************************************************************************

	@Override
	public void onSolution() {
		// get objective values
		
		for (int i = 0; i < n; i++) {
			if((objectives[i].getTypeAndKind()&Variable.INT)!=0){
				vals[i] = ((IntVar)objectives[i]).getValue();
			}else{
				RealVar realVar = (RealVar) objectives[i];
				vals[i] = (!maximize)?realVar.getUB():realVar.getLB() -realVar.getPrecision() ;	
			}
		}
		// remove dominated solutions
		for (int i = paretoFront.size() - 1; i >= 0; i--) {
			if (isDominated(paretoFront.get(i), vals)) {
				pool.add(paretoFront.remove(i));
			}
		}
		// store current solution
		if (pool.isEmpty()) {
			paretoFront.add(new Solution(model).record());
		} else {
			Solution solution = pool.remove();

			solution.record();
			paretoFront.add(solution);
		}
		// post dynamical constraints to prevent search from computing dominated
		// solutions
		Operator symbol = Operator.GE;
		if (!maximize) {
			symbol = Operator.LE;
		}
		for (int i = 0; i < n; i++) {
			if((objectives[i].getTypeAndKind()&Variable.INT)!=0){
				lits[i] = psat.makeLiteral(model.arithm((IntVar)objectives[i], symbol.toString(), (int) vals[i]).reify(), true);
			}else{
				RealVar var =(RealVar) objectives[i];
				lits[i] = psat.makeLiteral(model.realIbexGenericConstraint("{0}"+symbol.toString()+(vals[i]-var.getPrecision()), var).reify(), true);
			}
			//arithm(objectives[i], symbol.toString(), vals[i])
		}
		psat.addLearnt(lits);
	}

	/**
	 * @return the set of Pareto-best (possibly optimal) solutions found so far
	 */
	public List<Solution> getParetoFront() {
		return paretoFront;
	}

	private boolean isDominated(Solution solution, double[] vals) {
	int idx=maximize?1:0;
		for (int i = 0; i < n; i++) {
			double delta = 0;
			if((objectives[i].getTypeAndKind()&Variable.INT)!=0){
				delta = solution.getIntVal((IntVar) objectives[i]);
			}else{
				delta = solution.getRealBounds((RealVar) objectives[i])[idx] - vals[i];
			}
			if ((delta > 0 && maximize) || (delta < 0 && !maximize)) {
				return false;
			}
		}
		return true;
	}

}
