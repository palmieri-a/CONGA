package org.huawei.conga.algorithms;

import static org.chocosolver.util.tools.VariableUtils.searchSpaceSize;

import java.util.Arrays;
import java.util.Random;

import org.chocosolver.solver.Cause;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.selectors.variables.ImpactBased;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.huawei.conga.CongaSolver;
import org.huawei.conga.algorithms.ibr.config.finder.IBRFirstConfigurationFinder;
import org.huawei.conga.algorithms.ibr.config.finder.SpecificStrategyFirstSolutionFinder;
import org.huawei.conga.algorithms.ibr.selector.AgentSelector;
import org.huawei.conga.algorithms.ibr.selector.RandomSelector;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.constraint.agent.bound.MetaBFSConstraint;
import org.huawei.conga.equilibria.nash.brrecorder.SolutionRecorderImpl;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.sample.SimpleGame;
import org.huawei.conga.search.policy.BestSolutionEnumerationSearchPolicy;
import org.huawei.datastructure.table.TableTree;
import org.huawei.datastructure.table.TreeNode;
import org.huawei.utils.provider.Provider;

public class IBRAlgorithm implements Executable {

	private final int MAX_IT;
	private CongaSolver cg;
	private Provider<IEquilibriumConcept> concept;
	private Random r;
	private Model model;
	private Number[] tuple;
	private int finalIT;
	private boolean hasFoundSol;
	private IntVar[] dvars;
	private Solver s;
	private Constraint[] cstrs;
	private BestSolutionEnumerationSearchPolicy policy;
	private Solver solverPlayer;
	private SolutionRecorderImpl solutionMonitor;
	private IntVar[] dvarsCSPP;
	private final IBRFirstConfigurationFinder fSolFinder;
	private AgentSelector select;
	private Number bestSolutionFound;

	public IBRAlgorithm(CongaSolver cg, final int MAX_IT) {
		this(cg, MAX_IT, new SpecificStrategyFirstSolutionFinder(), new RandomSelector());
	}

	public IBRAlgorithm(final CongaSolver cg, final int MAX_IT, final IBRFirstConfigurationFinder fSolFinder,
			AgentSelector select) {
		this.fSolFinder = fSolFinder;
		this.cg = cg;
		this.MAX_IT = MAX_IT;
		this.concept = cg.getEquilibriumConcept();
		dvars = cg.retrieveIntDecisionVar(false);
		dvarsCSPP = cg.retrieveIntDecisionVarCSPPlayer();
		tuple = new Number[cg.getVarHelper().getDecisionVars().length];
		r = new Random(cg.getSeed());
		this.policy = new BestSolutionEnumerationSearchPolicy();
		cstrs = new Constraint[dvars.length];
		solutionMonitor = new SolutionRecorderImpl();
		s = cg.getSolver();
		model = s.getModel();
		this.select = select;
	}

	public IBRAlgorithm(CongaSolver cg) {
		this(cg, Integer.MAX_VALUE);
	}

	public Constraint[] getCstrs() {
		return cstrs;
	}

	public Number[] getTuple() {
		return tuple;
	}

	@Override
	public void execute() {
		fSolFinder.findFirstConfig(cg);
		retrieveTuple();
		s.reset();
		cg.prepareAndGetSolver();
		solverPlayer = cg.getAgents()[1].getTupleIntVarPlayerCSP()[0].getModel().getSolver();
		concept.get().initCSP(s, solverPlayer);
		int nextP = r.nextInt(cg.getAgents().length - 1);

		int it = 0;
		boolean hasFailed = true;
		while (hasFailed && it < MAX_IT) {
			model.getEnvironment().worldPush();
			try {
				for (int i = 0; i < dvars.length; i++) {
					dvars[i].instantiateTo(tuple[i].intValue(), Cause.Null);
				}
				model.getSolver().getEngine().propagate();
				hasFailed=false;
			} catch (ContradictionException e) {
				model.getSolver().getEngine().flush();
				model.getEnvironment().worldPop();
				nextP = select.nextAgent(cg.getAgents());
				changePlayerStrat(nextP);
				s.reset();
				it++;
			}
		}
	}


	public void initPrefix(int nextP) {
		retrieveTuple();
	}

	public void retrieveTuple() {
		for (int i = 0; i < dvars.length; i++) {
			assert dvars[i].isInstantiated();
			tuple[i] = dvars[i].getValue();
		}
	}

	public boolean isHasFoundSol() {
		return hasFoundSol;
	}

	/**
	 * Should change the tuple ( which indicate variable) of the player idx to
	 * one of its best response (randomly chosen)
	 * 
	 * @param i
	 */
	public void changePlayerStrat(int i) {
		// retrieveTuple();
		updateDecisionVar(i);
	}

	public void updateDecisionVar(int p) {
		TableTree data = check(p, policy);// concept.get().check(p, policy);
		assert data.getNbTuple() > 0;
		TreeNode<Number> cNode = data.getRoot().getChildren().get(r.nextInt(data.getRoot().getNbChild()));

		int nextInt = 0;
		for (int i = 0; i < dvars.length; i++) {

			if (cg.getVarHelper().getOwnerByVarID(dvars[i].getId()) == p) {
				tuple[i] = cNode.getElement();
				if (tuple[i] == null) {
					assert tuple[i] != null;
				}
				int nbChild = cNode.getNbChild();
				nextInt = nbChild > 0 ? r.nextInt(nbChild) : 0;
				if (!cNode.isLeaf()) {
					cNode = cNode.getChildren().get(nextInt);
				}
			}

		}
		System.out.println("change to " + Arrays.deepToString(tuple));
	}

	public TableTree check(int p, BestSolutionEnumerationSearchPolicy policy2) {
		Agent agent = this.cg.getAgents()[p];
		agent.initSolver(this.solverPlayer);
		Constraint[] local = new Constraint[agent.getTupleIntVarPlayerCSP().length];
		IntVar[] local_vars = agent.getTupleIntVarPlayerCSP();
		int idx = 0;
		for (int i = 0; i < agent.getTupleIntVarPlayerCSP().length; i++) {
			int owner = cg.getVarHelper().getOwner(cg.getVarHelper().getIndex(local_vars[i]));
			if (owner != p && owner != 0) {
				local[idx] = solverPlayer.getModel().arithm(local_vars[i], "=", tuple[i].intValue());
				local[idx++].post();
			}
		}
		solutionMonitor.init(agent);

		policy.launchSearch(s, solverPlayer, agent, solutionMonitor);
		solverPlayer.unplugMonitor(solutionMonitor);
		for (int i = 0; i < local.length; i++) {
			if (local[i] != null) {
				solverPlayer.getModel().unpost(local[i]);
			}
		}
		solverPlayer.reset();

		return solutionMonitor.getData();
	}

	public static void main(String[] args) {
		CongaSolver cg = new CongaSolver(new SimpleGame());
		cg.getSolver().showSolutions();
		IBRAlgorithm ibr = new IBRAlgorithm(cg);
		ibr.execute();
	}

	@Override
	public String toString() {
		return ";" + this.finalIT + ";;;";
	}

	public Model getModel() {
		return this.model;
	}

	public Number getBestSolutionFound() {
		return bestSolutionFound;
	}

}
