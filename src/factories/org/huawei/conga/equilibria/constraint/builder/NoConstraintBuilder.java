package  org.huawei.conga.equilibria.constraint.builder;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.constraint.agent.UseLessPropagator;
import org.huawei.conga.equilibria.constraint.agent.builder.AgentPropagatorsBuilder;
import org.huawei.conga.equilibria.nash.verificator.VerificationStrategy;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.utils.provider.Provider;

public class NoConstraintBuilder implements ConstraintBuilder{

	@Override
	public void reset() {
	}

	@Override
	public Constraint[] build(Agent[] agents, Provider<IEquilibriumConcept> equilibriumConcept,
			Provider<VerificationStrategy> strategiVerif, Solver cspPlayer, VarHelper varHelper,
			AgentPropagatorsBuilder apb, long seed) {
		return new Constraint[]{new Constraint("useLessC",new UseLessPropagator(agents[1].getIntVariables()))};
	}

}
