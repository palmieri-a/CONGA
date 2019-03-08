package org.huawei.conga.model.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.huawei.conga.equilibria.concepts.IEquilibriumConcept;
import org.huawei.conga.equilibria.constraint.agent.builder.AgentPropagatorsBuilder;
import org.huawei.conga.equilibria.constraint.builder.ConstraintBuilder;
import org.huawei.conga.equilibria.constraint.builder.MetaConstraintBuilder;
import org.huawei.conga.equilibria.constraint.builder.NoConstraintBuilder;
import org.huawei.conga.equilibria.nash.verificator.VerificationStrategy;
import org.huawei.conga.model.beans.Agent;
import org.huawei.conga.model.helper.VarHelper;
import org.huawei.utils.provider.Provider;

public enum ConstraintFactory implements ConstraintBuilder {

	NO_CONSTRAINT(NoConstraintBuilder.class), // usefull to debug models
	LAST_LEVEL(MetaConstraintBuilder.class), 
	BOUND_CONSTRAINT(MetaConstraintBuilder.class);

	private boolean incrUSed = false;
	private boolean incr;

	ConstraintFactory(Class<? extends ConstraintBuilder> cb) {
		this.cb = cb;
		this.incrUSed = false;
	}
//823536
	ConstraintFactory(Class<? extends ConstraintBuilder> cb, boolean incr) {
		this.cb = cb;
		this.incr = incr;
		this.incrUSed = true;
	}

	public ConstraintBuilder getInstance() {
		if (incrUSed) {

			try {
				Constructor<? extends ConstraintBuilder> cstr = cb.getConstructor(Boolean.TYPE);
				cstr.setAccessible(true);
				return cstr.newInstance(incr);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		}
		try {
			return cb.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Class<? extends ConstraintBuilder> cb;

	@Override
	public void reset() {
	}

	@Override
	public Constraint[] build(Agent[] agents, Provider<IEquilibriumConcept> equilibriumConcept,
			Provider<VerificationStrategy> strategiVerif, Solver cspPlayer, VarHelper varHelper,
			AgentPropagatorsBuilder apb,  long seed) {
		return getInstance().build(agents, equilibriumConcept, strategiVerif, cspPlayer, varHelper, apb, seed);
	}

	public static ConstraintBuilder retrieveAlgo(String string, String constraints) {
		ConstraintBuilder cb = NO_CONSTRAINT.getInstance();
		for (int i = 0; i < ConstraintFactory.values().length; i++) {
			if (ConstraintFactory.values()[i].name().equals(string)) {
				ConstraintBuilder instance = ConstraintFactory.values()[i].getInstance();
				instance.initParams(constraints);
				return instance;
			}
		}

		return cb;
	}

}
