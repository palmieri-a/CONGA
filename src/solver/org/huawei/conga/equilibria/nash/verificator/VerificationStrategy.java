package org.huawei.conga.equilibria.nash.verificator;

import org.huawei.conga.model.beans.Agent;

/**
 * Define an order to verify the equilibrium of players The verification reach
 * the end when the NullPlayer is returned
 * 
 * @author anthony
 *
 */
public interface VerificationStrategy {

	Agent next();

	void reset();

	default void updateReference(final boolean[] presentPlayer) {
	}

}
