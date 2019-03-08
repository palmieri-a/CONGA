package org.huawei.conga.equilibria.constraint.agent.bound;

import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.decision.DecisionPath;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;
import org.chocosolver.util.tools.ArrayUtils;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.constraint.agent.AbstractAgentPropagator;
import org.huawei.conga.equilibria.constraint.agent.context.provider.ContextVarProviderImpl;
import org.huawei.conga.equilibria.nash.applyer.BFSRecorderApplyer;
import org.huawei.conga.equilibria.nash.brrecorder.ISolutionRecorder;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.conga.search.policy.LastLevelSearchPolicy;
import org.huawei.utils.provider.Provider;

public class MetaBFSConstraint extends AbstractAgentPropagator {
	protected ContextVarProviderImpl contextProvider;
	protected Variable[][] scope;
	protected ISolutionRecorder basicSolutionRecorder;
	protected Variable[] cspPVar;
	protected BFSRecorderApplyer recorder;
	protected ISearchPolicy lastLevel;
	protected int curIdx = 0;
	protected int lastLevelChecked=-1;
	
	public static long timeLastLevel=0;
	public static long time =0;
	public static long nbCallEarlyFiltering=0;
	
	public static long nbCALL = 0;

	public MetaBFSConstraint(Provider<IEquilibriumConcept> eqConcept, Agent a, PropagatorPriority prio, BFSRecorderApplyer recorder, ISearchPolicy policy, VarHelper vh) {
		super(ArrayUtils.append(
				a.hasObjectif() ? new Variable[] { a.getObjectiveCSP()[0].getVariable() } : new Variable[] {},
				a.getTupleVarCSP()), eqConcept, policy, a,  vh, prio, true);
		contextProvider = new ContextVarProviderImpl();
		this.recorder = recorder;
		lastLevel = new LastLevelSearchPolicy(a);
	}

	public void filter(boolean prepare) throws ContradictionException {
		prepare();
		recorder.reset();
		// tp.setScope(agent);
		concept.get().setSolutionMonitor(recorder);
		if (!prepare) {
			long t1 = System.currentTimeMillis();
			concept.get().check(agent.getID(), lastLevel);
			concept.get().setSolutionMonitor(basicSolutionRecorder);
			restaure();
			timeLastLevel+=System.currentTimeMillis()-t1;
			recorder.apply();
		} else {
 			nbCallEarlyFiltering++;
			long t1 = System.currentTimeMillis();
			concept.get().check(agent.getID(), policy);
			time+=System.currentTimeMillis()-t1;
			recorder.apply();
		}
	}

	// prepare the context, players and dependants vars..
	protected void prepare() {
		basicSolutionRecorder = concept.get().getSolutionRecorder();
	}

	protected void restaure() {
		concept.get().setSolutionMonitor(basicSolutionRecorder);
	}

	@Override
	public ESat isEntailed() {
		return ESat.UNDEFINED;
	}

	@Override
	public void propagate(int idxVarInProp, int mask) throws ContradictionException {
		boolean instantiated = vars[0].isInstantiated() ;
		
		//check that the last variable does not belong to the player
		if ((idxVarInProp > 0 && !instantiated) || (idxVarInProp > 0 && mask != IntEventType.instantiation())  ) {
			return;
		}
		if(!instantiated && checkAllVarsPlayerInstantiated()){
			instantiated=true;
		}
		nbCALL++;
		
		if (instantiated && seekSupport(idxVarInProp) ) {
			filter(false);
		}
		else if (idxVarInProp == 0) {
			filter(true);
		}
		
	}

	protected boolean checkAllVarsPlayerInstantiated() {
		for (int i = 0; i < agent.getIntVariables().length; i++) {
			if(!agent.getIntVariables()[i].isInstantiated()){
				return false;
			}
		};
		return true;
	}

	/**
	 * Return false is the propagation can be triggered i.e last variable affected doens't belong to the player
	 * @param idxVarInProp
	 * @return
	 */
	protected boolean checkUseFullNessOfPropagation(int idxVarInProp) {
		
		DecisionPath decisionPath = this.model.getSolver().getDecisionPath();
		if(decisionPath.size()<=2){
			return false;
		}
		if(this.vh.getOwner(decisionPath.getDecision(decisionPath.size()-2).getDecisionVariable().getId()) == agent.getID() && this.vh.getOwner(decisionPath.getLastDecision().getDecisionVariable().getId()) == agent.getID()){
			return true;
		}else{
			return false;
		}
		
	}

	protected boolean seekSupport(int idxVarInProp) {
		int elems = 0;
		int length = agent.getTupleVarCSP().length;
		do {
			if (agent.getTupleVarCSP()[curIdx].isInstantiated()) {
				elems++;
				curIdx = (curIdx + 1) % length;
			} else {
				return false;
			}
		} while (elems != length);
		return true;
	}

	@Override
	public int getPropagationConditions(int vIdx) {
		// if it s the objective
		if (vIdx == 0) {
			// see with bounds
			return IntEventType.all();
		}
		return IntEventType.instantiation();
	}

	@Override
	public void propagate(int evtmask) throws ContradictionException {
//		filter(false);
	}

	
	@Override
	public String toString() {
		return MetaBFSConstraint.nbCALL+";"+MetaBFSConstraint.nbCallEarlyFiltering+";"+MetaBFSConstraint.time+";"+MetaBFSConstraint.timeLastLevel+";";
	}
}
