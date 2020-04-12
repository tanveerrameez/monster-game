package com.practice.monster.model;

import com.practice.monster.model.enums.Direction;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * This class encapsulates the neighbouring city 
 * @author Tanveer Rameez Ali
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NeighbouringCity {

	//@EqualsAndHashCode.Include
	private City city;
	
	private Direction direction;
	
}
