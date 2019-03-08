package  org.huawei.conga.equilibria.constraint.agent.context.provider;

import org.chocosolver.solver.variables.Variable;
import org.huawei.conga.model.beans.Agent;

public class ContextVarProviderImpl implements ContextVarProvider {

	@Override
	public Variable[][] instanciedVarProvider(Agent agent) {
		Variable[] tupleCSPVar = agent.getTupleVarCSP();
		Variable[] tupleVarCSPPlayer = agent.getTupleVarPlayerCSP();
		
		
		Variable CSP_tmp[] = new Variable[tupleCSPVar.length];
		Variable CSPPlayers_tmp[] = new Variable[tupleCSPVar.length];
		int nbVars = 0;
		
		for (int i = 0; i < tupleCSPVar.length; i++) {
			if (tupleCSPVar[i].isInstantiated()) {
				CSP_tmp[nbVars] = tupleCSPVar[i];
				CSPPlayers_tmp[nbVars++] = tupleVarCSPPlayer[i];
			}
		}
		Variable[][] context = new Variable[2][];
		if (nbVars == tupleCSPVar.length) {
			context[0] = tupleCSPVar;
			context[1] = tupleVarCSPPlayer;
		} else {
			context[0] = new Variable[nbVars];
			context[1] = new Variable[nbVars];

			System.arraycopy(CSP_tmp, 0, context[0], 0, nbVars);
			System.arraycopy(CSPPlayers_tmp, 0, context[1], 0, nbVars);

		}
		return context;
	}
}
