package org.huawei.conga.equilibria.constraint.builder;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.constraint.agent.builder.AgentPropagatorsBuilder;
import org.huawei.conga.equilibria.nash.verificator.VerificationStrategy;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.utils.provider.Provider;

public interface ConstraintBuilder {

	Constraint[] build(Agent[] agents, Provider<IEquilibriumConcept> equilibriumConcept,
			Provider<VerificationStrategy> strategiVerif, Solver cspPlayer, VarHelper varHelper, AgentPropagatorsBuilder apb,long seed);

	/**
	 * Remove constraints created
	 */
	void reset();
	
	/**
	 * Init the parameters of the constraint. When there are many parameters it should be under the form <$param1,$param2...>
	 * @param params
	 */
	default void initParams(String params) {}
	
}
