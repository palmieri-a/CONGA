package org.huawei.conga.model.beans;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.objective.ObjectiveFactory;
import org.chocosolver.solver.search.loop.monitors.ISearchMonitor;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.criteria.Criterion;

public class Player implements Agent {

	protected static final int INIT_SIZE = 10;
	protected boolean isConstraintModified = true;
	protected boolean isVariableModified = true;

	/**
	 * The constraint own by the player. I.e those that are only applicable to
	 * him. Can be seen like a preference.
	 */
	protected ArrayList<Constraint> constraints = new ArrayList<Constraint>();

	protected Constraint arrayConstraint[];

	/**
	 * Variable which are under control from the player
	 */

	protected ArrayList<Variable> variables = new ArrayList<Variable>();
	protected ArrayList<IntVar> intVariables = new ArrayList<IntVar>();
	protected ArrayList<RealVar> realVariables = new ArrayList<RealVar>();

	protected Variable vars_ownCSP[];
	protected Variable vars_ownCSPPlayer[];

	protected IntVar[] int_vars_ownCSPPlayer;
	protected IntVar[] int_vars_ownCSP;

	protected RealVar[] real_vars_ownCSPPlayer;
	protected RealVar[] real_vars_ownCSP;

	/**
	 * Var to optimize according to the policy
	 */
	protected VariableDecorator[] objectives;

	/**
	 * Policy of the specified var
	 */
	protected ResolutionPolicy objectivePolicy;

	/**
	 * Criterion to stop the search
	 */

	protected ArrayList<Criterion> criterions = new ArrayList<Criterion>();

	protected int ID;
	protected AbstractStrategy<Variable> strategies;

	protected Variable[] tupleVar = null;
	protected Variable[] tupleVarCSP_Player = null;

	protected IntVar[] intTupleVar = null;
	protected IntVar[] intTupleVarCSP_Player = null;

	protected RealVar[] realTupleVar = null;
	protected RealVar[] realTupleVarCSP_Player;

	protected ArrayList<ISearchMonitor> monitors = new ArrayList<>();
	protected ArrayList<Variable> variablesCSPPlayer = new ArrayList<Variable>();

	protected ArrayList<IntVar> intVariablesCSPPlayer = new ArrayList<IntVar>();
	protected ArrayList<RealVar> realVariablesCSPPlayer = new ArrayList<RealVar>();

	protected VariableDecorator[] objectivesPlayer = null;
	// private AbstractStrategy defaultStrategy;

	public Player(int i) {
		this.ID = i;
	}

	@Override
	public void post(Constraint c) {
		isConstraintModified = true;
		constraints.add(c);
	}

	@Override
	public Constraint[] getConstraints() {
		if (isConstraintModified) {
			this.arrayConstraint = this.constraints.toArray(new Constraint[constraints.size()]);
		}
		return this.arrayConstraint;
	}

	@Override
	public void own(final Variable v) {
		boolean contains = false;
		boolean containsCSPPlayer = false;
		for (int i = 0; i < variables.size(); i++) {
			if (variables.get(i).getId() == v.getId()) {
				contains = true;
				if (v.getModel() == variables.get(i).getModel()) {
					return;
				}
				break;
			}
		}
		for (int i = 0; i < variablesCSPPlayer.size(); i++) {
			if (variablesCSPPlayer.get(i).getId() == v.getId()) {
				containsCSPPlayer = true;
				break;
			}
		}
		if (!contains && !containsCSPPlayer) {
			assert v.getModel().getName().equals("CSP");
			isVariableModified = true;
			variables.add(v);
			if ((v.getTypeAndKind() & Variable.INT) != 0) {
				intVariables.add((IntVar) v);
			} else if ((v.getTypeAndKind() & Variable.REAL) != 0) {
				realVariables.add((RealVar) v);
			}
		} else if (contains && !containsCSPPlayer) {
			assert v.getModel().getName().equals("Players");
			variablesCSPPlayer.add(v);
			if ((v.getTypeAndKind() & Variable.INT) != 0) {
				intVariablesCSPPlayer.add((IntVar) v);
			} else if ((v.getTypeAndKind() & Variable.REAL) != 0) {
				realVariablesCSPPlayer.add((RealVar) v);
			}
		}
	}

	@Override
	public void own(Variable[] v) {
		for (int i = 0; i < v.length; i++) {
			own(v[i]);
		}
	}

	@Override
	public Variable[] getVariables() {
		if (isVariableModified) {
			createsArrays();

		}
		return this.vars_ownCSP;
	}

	private void createsArrays() {
		this.vars_ownCSP = new Variable[variables.size()];
		this.vars_ownCSP = this.variables.toArray(vars_ownCSP);

		this.vars_ownCSPPlayer = new Variable[variablesCSPPlayer.size()];
		this.vars_ownCSPPlayer = this.variablesCSPPlayer.toArray(vars_ownCSPPlayer);

		this.int_vars_ownCSP = new IntVar[intVariables.size()];
		this.int_vars_ownCSP = intVariables.toArray(int_vars_ownCSP);

		this.int_vars_ownCSPPlayer = new IntVar[intVariablesCSPPlayer.size()];
		this.int_vars_ownCSPPlayer = intVariablesCSPPlayer.toArray(int_vars_ownCSPPlayer);

		this.real_vars_ownCSPPlayer = new RealVar[realVariablesCSPPlayer.size()];
		this.real_vars_ownCSPPlayer = realVariablesCSPPlayer.toArray(real_vars_ownCSPPlayer);

		isVariableModified = false;
	}

	@Override
	public Variable[] getVariablesCSPPlayer() {
		if (isVariableModified) {
			createsArrays();
		}
		return this.vars_ownCSPPlayer;
	}

	@Override
	public void setObjective(ResolutionPolicy policy, Variable... intVar) {
		objectivePolicy = policy;
		if (intVar[0].getModel().getName().equals("CSP")) {
			objectives = createVarDecorator(intVar);
		} else if (intVar[0].getModel().getName().equals("Players")) {
			objectivesPlayer = createVarDecorator(intVar);
		}
	}

	private VariableDecorator[] createVarDecorator(Variable[] intVar) {
		VariableDecorator[] vars = new VariableDecorator[intVar.length];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = new VariableDecorator(intVar[i]);
		}
		return vars;
	}

	@Override
	public VariableDecorator[] getObjectiveCSPPlayer() {
		return this.objectivesPlayer;
	}

	@Override
	public void setStrategy(AbstractStrategy strat) {
		this.strategies = strat;
	}

	@Override
	public void addPreference(Constraint c) {
		if (c.getPropagators()[0].getModel().getName().equals("Players")) {
			isConstraintModified = true;
			constraints.add(c);
		}
	}

	@Override
	public void initSolver(Solver cspPlayer) {
		if (this.constraints.size() > 0) {
			Constraint[] local_constraints = this.getConstraints();
			for (int i = 0; i < local_constraints.length; i++) {
				cspPlayer.getModel().post(local_constraints[i]);
			}
		}
		for (int i = 0; i < this.monitors.size(); i++) {
			cspPlayer.plugMonitor(monitors.get(i));
		}

		
		initStrat(cspPlayer);
		
	

	}

	private void initStrat(Solver cspPlayer) {
		if (strategies != null) {
			cspPlayer.setSearch(strategies);
		} else {
			AbstractStrategy s = null;
			if (int_vars_ownCSPPlayer.length > 0 && real_vars_ownCSPPlayer.length > 0) {
				cspPlayer.setSearch(Search.sequencer(Search.inputOrderLBSearch(int_vars_ownCSPPlayer),
						Search.realVarSearch((real_vars_ownCSPPlayer))));
			} else if (int_vars_ownCSPPlayer.length > 0) {
				s = Search.inputOrderLBSearch(int_vars_ownCSPPlayer);
			} else if (real_vars_ownCSPPlayer.length > 0) {
				s = Search.realVarSearch(real_vars_ownCSPPlayer);
			}
			this.strategies=s;
			cspPlayer.setSearch(s);
		}

	}

	@Override
	public void reset(Solver cspPlayer, Solver origin) {
		//unpost constraints
		if (this.constraints.size() > 0) {
			Constraint[] local_constraints = this.getConstraints();
			for (int i = 0; i < local_constraints.length; i++) {
				cspPlayer.getModel().unpost(local_constraints[i]);
			}
		}
		//remove criterion
		int size = criterions.size();
		for (int i = 0; i < size; i++) {
			cspPlayer.removeStopCriterion(criterions.get(i));
		}
		//unplug and reset monitor
		int size2 = getMonitors().size();
		for (int i = 0; i < size2; i++) {
//			if (getMonitors().get(i) instanceof IIncompleteStrategy) {
//				((IIncompleteStrategy) getMonitors().get(i)).reset();
//			}
			cspPlayer.unplugMonitor(getMonitors().get(i));
		}
		
		cspPlayer.setObjectiveManager(ObjectiveFactory.SAT());

	}

	@Override
	public void setTupleVarCSP(Variable[] tupleVar) {
		this.tupleVar = tupleVar;
		dispatchCSP(tupleVar);
	}

	private void dispatchCSP(Variable[] tupleVar2) {
		ArrayList<RealVar> reals = new ArrayList<>();
		ArrayList<IntVar> ints = new ArrayList<>();
		for (int i = 0; i < tupleVar2.length; i++) {
			if ((tupleVar2[i].getTypeAndKind() & Variable.INT) != 0) {
				ints.add((IntVar) tupleVar2[i]);
			} else if ((tupleVar2[i].getTypeAndKind() & Variable.REAL) != 0) {
				reals.add((RealVar) tupleVar2[i]);
			}
		}
		this.intTupleVar = new IntVar[ints.size()];
		intTupleVar = ints.toArray(intTupleVar);

		this.realTupleVar = new RealVar[reals.size()];
		realTupleVar = reals.toArray(realTupleVar);
	}

	@Override
	public Variable[] getTupleVarCSP() {
		return this.tupleVar;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public VariableDecorator[] getObjectiveCSP() {
		return this.objectives;
	}

	@Override
	public Variable[] getTupleVarPlayerCSP() {
		return this.tupleVarCSP_Player;
	}

	@Override
	public void setTupleVarCSPPlayer(Variable[] tupleVarCSP) {
		this.tupleVarCSP_Player = tupleVarCSP;
		dispatchCSPPlayer(tupleVarCSP);

	}

	private void dispatchCSPPlayer(Variable[] tupleVarCSPPlayer) {
		ArrayList<RealVar> reals = new ArrayList<>();
		ArrayList<IntVar> ints = new ArrayList<>();
		for (int i = 0; i < tupleVarCSPPlayer.length; i++) {
			if ((tupleVarCSPPlayer[i].getTypeAndKind() & Variable.INT) != 0) {
				ints.add((IntVar) tupleVarCSPPlayer[i]);
			} else if ((tupleVarCSPPlayer[i].getTypeAndKind() & Variable.REAL) != 0) {
				reals.add((RealVar) tupleVarCSPPlayer[i]);
			}
		}
		this.intTupleVarCSP_Player = new IntVar[ints.size()];
		intTupleVarCSP_Player = ints.toArray(intTupleVarCSP_Player);

		this.realTupleVarCSP_Player = new RealVar[reals.size()];
		realTupleVarCSP_Player = reals.toArray(realTupleVarCSP_Player);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Agent " + this.getID());
		for (int i = 0; i < variables.size(); i++) {
			sb.append(variables.get(i));
		}
		sb.append(", objective =: " + this.objectives + System.lineSeparator());
		return sb.toString();
	}

	@Override
	public void addMonitor(ISearchMonitor ism) {
		this.monitors.add(ism);
	}

	@Override
	final public boolean isMaximisation() {
		return ResolutionPolicy.MAXIMIZE == this.objectivePolicy;
	}

	@Override
	public void reset() {
		this.constraints.clear();
		this.variables.clear();
		this.arrayConstraint = null;
		this.vars_ownCSP = null;
		this.isConstraintModified = true;
		this.isVariableModified = true;
		this.monitors.clear();
		this.objectives = null;
		this.objectivePolicy = ResolutionPolicy.SATISFACTION;
		this.strategies = null;
		this.tupleVar = null;
		this.tupleVarCSP_Player = null;
		this.variablesCSPPlayer.clear();
		this.vars_ownCSPPlayer = null;
	}

	@Override
	public IntVar[] getIntVariables() {
		if (isVariableModified) {
			createsArrays();
		}
		return this.int_vars_ownCSP;
	}

	@Override
	public RealVar[] getRealVariables() {
		if (isVariableModified) {
			createsArrays();
		}
		return this.real_vars_ownCSP;
	}

	@Override
	public IntVar[] getIntVariablesCSPPlayer() {
		if (isVariableModified) {
			createsArrays();
		}
		return this.int_vars_ownCSPPlayer;
	}

	@Override
	public RealVar[] getRealVariablesCSPPlayer() {
		if (isVariableModified) {
			createsArrays();
		}
		return this.real_vars_ownCSPPlayer;
	}

	@Override
	public IntVar[] getTupleIntVarCSP() {
		return this.intTupleVar;
	}

	@Override
	public RealVar[] getTupleRealVarCSP() {
		return this.realTupleVar;
	}

	@Override
	public IntVar[] getTupleIntVarPlayerCSP() {
		return intTupleVarCSP_Player;
	}

	@Override
	public RealVar[] getTupleRealVarPlayerCSP() {
		return realTupleVarCSP_Player;
	}

	@Override
	public void addStopCriterion(Criterion... c) {
		for (int i = 0; i < c.length; i++) {
			this.criterions.add(c[i]);
		}
	}

	@Override
	public void remove(Criterion... c) {
		for (int i = 0; i < c.length; i++) {
			this.criterions.remove(c[i]);
		}
	}

	@Override
	public void addAllStopCriterion(Solver s) {
		for (int i = 0; i < criterions.size(); i++) {
			s.addStopCriterion(criterions.get(i));
		}
	}

	@Override
	public List<ISearchMonitor> getMonitors() {
		return monitors;
	}
	
	@Override
	public void addStopCriterion(Solver s){
		for (int i = 0; i <  criterions.size(); i++) {
			s.addStopCriterion(criterions.get(i));
		}

	}
}
