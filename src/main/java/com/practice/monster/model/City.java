package com.practice.monster.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.practice.monster.MonsterActivity;
import com.practice.monster.exceptions.InvalidActivityException;
import com.practice.monster.exceptions.TooManyMonstersException;
import com.practice.monster.model.enums.Direction;
import com.practice.monster.model.enums.State;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class encapsulates the city, extends DestroyableModel
 * @author Tanveer Rameez Ali
 */

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class City extends DestroyableModel{
	
	@EqualsAndHashCode.Include
	private String name;
	
	@EqualsAndHashCode.Exclude
	private List<NeighbouringCity> neighbouringCities;
	
	@EqualsAndHashCode.Exclude
	private Set<Monster> monsters;

	public City(String name) {
		this.name = name;
	}
	
	public void addMonster(Monster monster) {
		if(monsters == null) {
			monsters = new HashSet<>();
		}
		if(! isActive()) throw new InvalidActivityException("Cannot add monster to destroyed city "+name);
		
		//should not occur since the city should have already been destroyed when this situation occurs, but just in case
		if(monsters.size() == MonsterActivity.MAX_MONSTER_ALLOWED) {
			throw new TooManyMonstersException("City "+name+" already has "+MonsterActivity.MAX_MONSTER_ALLOWED+" monsters.");
		}
		
		//should not occur since the monster cannot travel to the city it is already in, but just in case of faulty map
		if(monsters.contains(monster)) {
			throw new InvalidActivityException("Monster "+ monster.getId()+" already exists in city "+ name);
		}
		monsters.add(monster);
		monster.setCurrentCity(this);
		if(monsters.size() == MonsterActivity.MAX_MONSTER_ALLOWED) {
			destroy();
		}
	}
	
	public void destroy() {
		if(monsters!=null)
		      monsters.forEach(Monster::destroy);
		super.destroy();
	}
	
	public void addNeighbouringCity(City city, Direction direction) {
		if(neighbouringCities == null) {
			neighbouringCities = new ArrayList<>();
		}
		neighbouringCities.add(new NeighbouringCity(city, direction));
	}
	
	public void removeMonster(Monster monster) {
		monster.setCurrentCity(null);
		monsters.remove(monster);
	}
	
	public String toString() {
		return name;
	}
}
