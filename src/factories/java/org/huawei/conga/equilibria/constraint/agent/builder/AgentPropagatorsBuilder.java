package org.huawei.conga.equilibria.constraint.agent.builder;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.search.policy.builder.ISearchPolicyBuilder;
import org.huawei.utils.provider.Provider;

@FunctionalInterface
public interface AgentPropagatorsBuilder {

	Propagator<Variable>[] buildConstraintsAgent(Agent[] agents,
			Provider<IEquilibriumConcept> concept, VarHelper vh, ISearchPolicyBuilder isb);

}
