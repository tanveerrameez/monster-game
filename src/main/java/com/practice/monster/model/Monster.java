package com.practice.monster.model;


/**
 * This class encapsulates the Monster, extends DestroyableModel
 * @author Tanveer Rameez Ali
 */

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor 
@EqualsAndHashCode(callSuper=false)
public class Monster extends DestroyableModel {
	
	@EqualsAndHashCode.Include
	private int id;
	@EqualsAndHashCode.Exclude
	private City currentCity;
	@EqualsAndHashCode.Exclude
	private boolean trapped;
	
	public static final String MONSTER = "Monster";
	
	public Monster(int id) {
		this.id = id;
	}
	
	public String toString() {
		return MONSTER+" " + id;
	}
	
}
