package org.huawei.conga.equilibria.constraint.agent.builder;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.constraint.agent.NaiveCheckerPropagator;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.search.policy.builder.ISearchPolicyBuilder;
import org.huawei.utils.provider.Provider;

public class AgentPropagatorCheckerBuilder implements AgentPropagatorsBuilder {
	public AgentPropagatorCheckerBuilder() {
	}

	@Override
	public Propagator<Variable>[] buildConstraintsAgent(Agent[] agents, Provider<IEquilibriumConcept> concept,
			 VarHelper vh, ISearchPolicyBuilder isb) {
		Propagator<Variable>[] agentPropagators = new NaiveCheckerPropagator[agents.length];
		for (int i = 1; i < agents.length; i++) {
			agentPropagators[i] = new NaiveCheckerPropagator(concept, agents[i], isb.build(agents[i]), vh);
		}

		return agentPropagators;
	}
}
