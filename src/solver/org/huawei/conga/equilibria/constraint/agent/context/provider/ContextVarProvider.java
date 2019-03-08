package  org.huawei.conga.equilibria.constraint.agent.context.provider;

import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.beans.Agent;

public interface ContextVarProvider {

	Variable[][] instanciedVarProvider(Agent agent);
	public final static short LB =1;
	public final static short UB =2;
	public final static short LB_UB =3;
	
}
