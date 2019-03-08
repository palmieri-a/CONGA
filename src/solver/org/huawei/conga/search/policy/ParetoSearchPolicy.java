package org.huawei.conga.search.policy;

import java.util.Arrays;
import java.util.List;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.choco.addon.constraint.SolverMethodsProviders;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.beans.VariableDecorator;
import org.huawei.conga.number.Interval;
import org.huawei.datastructure.table.TableTree;

public class ParetoSearchPolicy implements ISearchPolicy {


	@Override
	public void launchSearch(Solver csp, Solver playerSolver, Agent a, ISolutionRecorder solutionMonitor) {
		List<Solution> solutions = SolverMethodsProviders.findParetoFront(retrieveObjectives(a.getObjectiveCSPPlayer()),
				a.isMaximisation());
		add(solutionMonitor.getData(), solutions, a.getIntVariables(), a.getRealVariables());
		a.reset(playerSolver, csp);
	}

	private void add(TableTree data, List<Solution> solutions, IntVar[] intVars, RealVar[] realVars) {
		
		int sizeIntVars = (null!=intVars)?intVars.length:0;
		int sizeRealVars = (null!=realVars)?realVars.length:0;
		Number[] sol = new Number[sizeIntVars  + sizeRealVars];
		for (int i = 0; i < solutions.size(); i++) {
			for (int j = 0; j < sizeIntVars; j++) {
				sol[j] = solutions.get(i).getIntVal(intVars[j]);
			}
			for (int j = 0; j < sizeRealVars; j++) {
				double[] realBounds = solutions.get(i).getRealBounds(realVars[j]);
				sol[j] = new Interval(realBounds[0],realBounds[1]);
			}
			data.add(sol);
		}

	}

	private Variable[] retrieveObjectives(VariableDecorator[] objectiveCSPPlayer) {
		Variable vars[] = new Variable[objectiveCSPPlayer.length];
		for (int i = 0; i < objectiveCSPPlayer.length; i++) {
			vars[i] = objectiveCSPPlayer[i].getVariable();
		}
		return vars;
	}

}
