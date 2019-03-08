package org.huawei.conga.equilibria.constraint.agent;


import org.chocosolver.memory.IStateBool;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;
import org.chocosolver.util.tools.ArrayUtils;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.conga.search.policy.ISearchPolicy;
import org.huawei.conga.utils.tuple.provider.BasicTupleProvider;
import org.huawei.conga.utils.tuple.provider.TupleProvider;
import org.huawei.datastructure.table.TableTree;
import org.huawei.utils.provider.Provider;

public class NaiveCheckerPropagator extends AbstractAgentPropagator /*implements IVariableMonitor<Variable>*/ {

	protected boolean passed = false;
	protected TupleProvider tp;
	protected IStateBool hasFailed;
	protected IStateBool hasPassed;

	public NaiveCheckerPropagator(Provider<IEquilibriumConcept> eqConcept, Agent a, ISearchPolicy policy,
			VarHelper vh) {
		super(ArrayUtils.append(a.getVariables(),a.getTupleVarCSP()), eqConcept, policy, a,  vh, PropagatorPriority.VERY_SLOW, false);
		tp = new BasicTupleProvider(a, vh);

		
	}


	@Override
	public ESat isEntailed() {
			return ESat.UNDEFINED;
	}

	@Override
	public void propagate(int evtmask) throws ContradictionException {
		if (seekSupport()) {
			passed = true;
			TableTree data = concept.get().check(agent.getID());
			Number[] andCheckFullPostTuple = tp.getAndCheckFullPostTuple();
			short status = (short) ((data.contains(andCheckFullPostTuple)) ? 1 : 0);
			boolean isFail = status==0;
			if (isFail) {
				this.fails();
			}
		}
	}
	

	protected int curIdx = 0;
	
	protected boolean seekSupport() {
		int elems = 0;
		int length = vars.length;
		do {
			if (vars[curIdx].isInstantiated()) {
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
		return IntEventType.instantiation();
	}

	
	
	


}
