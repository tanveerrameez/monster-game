package com.practice.monster.model;

/**
 * This abstract class implements the Destroyable interface
 * @author Tanveer Rameez Ali
 */

import com.practice.monster.model.enums.State;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@EqualsAndHashCode
public abstract class DestroyableModel implements Destroyable {
	@EqualsAndHashCode.Exclude
	private State state = State.ACTIVE;
	
	public boolean isActive() {
		return state == State.ACTIVE;
	}

	public void destroy() {
		state = State.DESTROYED;
	}

}
