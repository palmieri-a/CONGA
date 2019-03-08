package org.huawei.conga.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.choco.addon.constraint.SolverMethodsProviders;
import org.huawei.conga.model.beans.VariableDecorator;

public class ParetoNashAlgorithm implements Executable {

	private CongaSolver cg;
	private List<Solution> solutions;

	public ParetoNashAlgorithm(CongaSolver cg) {
		this.cg=cg;
	}

	@Override
	public void execute() {
		cg.prepareAndGetSolver();
//		solutions = SolverMethodsProviders.findParetoFront(getPlayersObjectives(cg.getSolver().getModel()), true);
		solutions = SolverMethodsProviders.findParetoFront(getPlayersIntObjectives(cg.getSolver().getModel()), true);
		
	}
	
	protected Variable[]getPlayersObjectives(Model model){
		List<Variable> objectives = new ArrayList<Variable>();
		for (int i = 1; i < cg.getAgents().length; i++) {
			VariableDecorator[] objectiveCSP = cg.getAgents()[i].getObjectiveCSP();
			for (int j = 0; j < objectiveCSP.length; j++) {
				if(cg.getAgents()[i].isMaximisation()){
					objectives.add(objectiveCSP[j].getVariable());
				}else{
					if(objectiveCSP[j].isInteger()){
						objectives.add(model.intMinusView((IntVar) objectiveCSP[j].getVariable()));
					}else{
						RealVar reverseobj = model.realVar(-objectiveCSP[j].getUB(), -objectiveCSP[j].getLB(),((RealVar) objectiveCSP[j].getVariable()).getPrecision());
						model.realIbexGenericConstraint("{0}=-{1}", reverseobj,((RealVar) objectiveCSP[j].getVariable())).post();
						objectives.add(reverseobj);
					}
				}
			}
		}
		Variable[] variables = new Variable[objectives.size()];
		objectives.toArray(variables);
		return variables;
	}
	
	protected IntVar[]getPlayersIntObjectives(Model model){
		List<Variable> objectives = new ArrayList<Variable>();
		for (int i = 1; i < cg.getAgents().length; i++) {
			VariableDecorator[] objectiveCSP = cg.getAgents()[i].getObjectiveCSP();
			for (int j = 0; j < objectiveCSP.length; j++) {
				if(cg.getAgents()[i].isMaximisation()){
					objectives.add(objectiveCSP[j].getVariable());
				}else{
					if(objectiveCSP[j].isInteger()){
						objectives.add(model.intMinusView((IntVar) objectiveCSP[j].getVariable()));
					}else{
						RealVar reverseobj = model.realVar(-objectiveCSP[j].getUB(), -objectiveCSP[j].getLB(),((RealVar) objectiveCSP[j].getVariable()).getPrecision());
						model.realIbexGenericConstraint("{0}=-{1}", reverseobj,((RealVar) objectiveCSP[j].getVariable())).post();
						objectives.add(reverseobj);
					}
				}
			}
		}
		IntVar[] variables = new IntVar[objectives.size()];
		objectives.toArray(variables);
		return variables;
	}
	
	@Override
	public String toString() {
		return ";"+solutions.size()+";;;";
	}
}
