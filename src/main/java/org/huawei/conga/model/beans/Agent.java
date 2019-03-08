package org.huawei.conga.model.beans;

import java.util.List;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.loop.monitors.ISearchMonitor;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.criteria.Criterion;

public interface Agent {

	void post(Constraint c);

	Constraint[] getConstraints();

	void own(Variable v);

	void own(Variable[] v);

	Variable[] getVariables();

	IntVar[] getIntVariables();

	RealVar[] getRealVariables();

	Variable[] getVariablesCSPPlayer();

	IntVar[] getIntVariablesCSPPlayer();

	RealVar[] getRealVariablesCSPPlayer();

	void setObjective(ResolutionPolicy policy, Variable... intVar);

	default boolean hasObjectif() {
		return this.getObjectiveCSP() != null;
	}

	void setStrategy(AbstractStrategy intStrategy);

	void addPreference(Constraint arithm);

	void initSolver(Solver cspPlayer);

	void reset(Solver cspPlayer, Solver origin);

	int getID();

	default int getNbVars() {
		return this.getVariables().length;
	}

	default int getNbRealVars() {
		return (null!=this.getRealVariables())?this.getRealVariables().length:0;
	}

	default int getNbIntVars() {
		return (null!=this.getIntVariables())?this.getIntVariables().length:0;
	}

	Variable[] getTupleVarPlayerCSP();

	IntVar[] getTupleIntVarPlayerCSP();

	RealVar[] getTupleRealVarPlayerCSP();
	
	void setTupleVarCSP(Variable[] tupleVar);

	void setTupleVarCSPPlayer(Variable[] tupleVarCSP);

	void addMonitor(ISearchMonitor ism);

	boolean isMaximisation();

	void reset();

	Variable[] getTupleVarCSP();

	IntVar[] getTupleIntVarCSP();

	RealVar[] getTupleRealVarCSP();

	VariableDecorator[] getObjectiveCSPPlayer();

	VariableDecorator[] getObjectiveCSP();
	
	void addStopCriterion(Criterion... c);
	
	void remove(Criterion... c);
	
	void addAllStopCriterion(Solver s);

	List<ISearchMonitor> getMonitors();

	void addStopCriterion(Solver s);
}
