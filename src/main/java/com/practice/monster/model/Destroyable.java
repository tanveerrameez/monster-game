package com.practice.monster.model;

/**
 * Destroyable interface to be implemented by entities that can have states - active and destroyed
 * @author Tanveer Rameez Ali
 */
import com.practice.monster.model.enums.State;

public interface Destroyable {

	State getState();
	void setState(State state);
	void destroy();
	boolean isActive();
}
