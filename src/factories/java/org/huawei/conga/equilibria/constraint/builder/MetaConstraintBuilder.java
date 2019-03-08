package org.huawei.conga.equilibria.constraint.builder;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.constraint.agent.bound.MetaBFSConstraint;
import org.huawei.conga.equilibria.constraint.agent.bound.MetaBFSConstraintLastLevelOnly;
import org.huawei.conga.equilibria.constraint.agent.builder.AgentPropagatorsBuilder;
import org.huawei.conga.equilibria.nash.applyer.BFSRecorderApplyer;
import org.huawei.conga.equilibria.nash.verificator.VerificationStrategy;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.search.policy.BFS_Policy;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.utils.provider.Provider;

/**
 * The meta constraint contains at least the checkers constraints
 * 
 * @author a80048597
 *
 */
public class MetaConstraintBuilder implements ConstraintBuilder {

	private Constraint[] allConstraint;
	private ISearchPolicy policy;
	
	
	
	@Override
	public Constraint[] build(Agent[] agents, Provider<IEquilibriumConcept> equilibriumConcept,
			Provider<VerificationStrategy> strategiVerif, Solver cspPlayer, VarHelper varHelper,
			AgentPropagatorsBuilder apb, long seed) {

		allConstraint= new Constraint[agents.length-1];
 	
		for (int i = 1; i < agents.length; i++) {
			Agent agent = agents[i];
			policy = new BFS_Policy();
			BFSRecorderApplyer recorder = new BFSRecorderApplyer();
			recorder.init(agents[i]);
			allConstraint[i-1]= new Constraint("metaConstraint_agent_"+agents[i].getID(), new MetaBFSConstraint(equilibriumConcept, agent, PropagatorPriority.VERY_SLOW, recorder, policy, varHelper));
		}
		
		return allConstraint;
	}

	@Override
	public void reset() {}


}
