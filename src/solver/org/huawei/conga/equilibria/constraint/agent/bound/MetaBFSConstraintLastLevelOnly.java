package org.huawei.conga.equilibria.constraint.agent.bound;

import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.events.IntEventType;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.nash.applyer.BFSRecorderApplyer;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.utils.provider.Provider;

public class MetaBFSConstraintLastLevelOnly extends MetaBFSConstraint {

	public MetaBFSConstraintLastLevelOnly(Provider<IEquilibriumConcept> eqConcept, Agent a, PropagatorPriority prio,
			BFSRecorderApplyer recorder, ISearchPolicy policy, VarHelper vh) {
		super(eqConcept, a, prio, recorder, policy, vh);
	}
	
	
	@Override
	public void propagate(int idxVarInProp, int mask) throws ContradictionException {
		boolean instantiated = vars[0].isInstantiated() ;
		
		if(!instantiated && checkAllVarsPlayerInstantiated()){
			instantiated=true;
		}
		
		if (instantiated && seekSupport(idxVarInProp) ) {
			filter(false);
		}
	}
	
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		if(checkAllVarsPlayerInstantiated()){
			filter(false);
		}
	}
	
	
	

}
