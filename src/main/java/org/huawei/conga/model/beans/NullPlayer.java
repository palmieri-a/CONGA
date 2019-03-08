package org.huawei.conga.model.beans;

import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;

/**
 * NullObject design pattern for the player 0.
 * 
 * @author anthony
 *
 */
public class NullPlayer extends Player {

	/**
	 * Variable which are under control from the player
	 */

	public NullPlayer() {
		super(ID);
	}

	private final static int ID = 0;

	public int getID() {
		return ID;
	}

	public void reset() {
		super.ID = 0;
		super.constraints.clear();
		super.vars_ownCSP = new IntVar[0];
		super.variables.clear();
		super.isConstraintModified = false;
		super.isVariableModified = false;
		super.arrayConstraint = null;
		super.variables.clear();
	}
	
	@Override
	public void own(Variable v) {
		boolean contains = false;
		boolean containsCSPPlayer = false;
		for (int i = 0; i < variables.size(); i++) {
			if (variables.get(i).getId() == v.getId()) {
				contains = true;
				if (v.getModel() == variables.get(i).getModel()) {
					return;
				}
				break;
			}
		}
		for (int i = 0; i < variablesCSPPlayer.size(); i++) {
			if (variablesCSPPlayer.get(i).getId() == v.getId()) {
				containsCSPPlayer = true;
				break;
			}
		}
		if (!contains && !containsCSPPlayer) {
			isVariableModified = true;
			variables.add(v);
		} else if (contains && !containsCSPPlayer) {
			variablesCSPPlayer.add( v);
		}
	}

}
