package org.huawei.conga.equilibria.constraint.agent;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.utils.provider.Provider;

public abstract class AbstractAgentPropagator extends Propagator<Variable> {
	final protected Agent agent;
	final protected Provider<IEquilibriumConcept> concept;
	final protected ISearchPolicy policy;
	final protected VarHelper vh;

	protected AbstractAgentPropagator(Variable[] dvars,Provider<IEquilibriumConcept> eqConcept, ISearchPolicy policy, Agent a, VarHelper vh, PropagatorPriority prio, boolean isIncr) {
		super(dvars,prio,isIncr);
		this.agent = a;
		this.concept = eqConcept;
		this.policy=policy;
		this.vh=vh;
	}
	
}
