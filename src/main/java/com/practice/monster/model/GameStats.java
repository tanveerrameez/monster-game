package com.practice.monster.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class GameStats {

	private long numOfMonstersCreated;
	private long numOfMonstersKilled;
	private long totalNumberOfCities;
	private long numOfCitiesDestroyed;
}
