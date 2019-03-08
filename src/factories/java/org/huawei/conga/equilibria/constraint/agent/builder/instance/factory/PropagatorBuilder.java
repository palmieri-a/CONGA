package org.huawei.conga.equilibria.constraint.agent.builder.instance.factory;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.utils.provider.Provider;

public interface PropagatorBuilder {
	
	Propagator<Variable> createInstance(Provider<IEquilibriumConcept> concept, Agent agents,VarHelper vh);

	default String getName(){return this.getClass().getName();	}
}
