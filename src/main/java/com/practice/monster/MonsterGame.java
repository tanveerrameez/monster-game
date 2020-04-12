package com.practice.monster;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.practice.monster.exceptions.TooManyMonstersException;
import com.practice.monster.model.City;
import com.practice.monster.model.GameStats;
import com.practice.monster.model.Monster;

public class MonsterGame {
	// private final static Logger log = Logger.getLogger(MonsterGame.class);
	private static final int MAX_ITERATION = 10000;
	private MonsterActivity monsterActivity = new MonsterActivity();
	private MapGenerator mapGenerator = new MapGenerator();
	private Map<String, City> cityMap;
	private String outputFilePath;
	

	public MonsterGame(String mapFilePath, String outputFilePath) throws Exception {
		Path filePath = Paths.get(mapFilePath);
		cityMap = mapGenerator.generateMap(filePath);
		this.outputFilePath = outputFilePath;
	}

	public GameStats conquer(int monsterNum) throws IOException {
		
		if (monsterNum > cityMap.size() * 2)
			throw new TooManyMonstersException("Number of Monsters:" + monsterNum + " Number of cities: "
					+ cityMap.size() + ". Maximum number of monster allowed:" + (cityMap.size() * 2));
		if (monsterNum < 1)
			throw new IllegalArgumentException("Number of Monster(s) cannot be negative or zero");
		
		Random rand = new Random();
		List<Monster> monsters = new ArrayList<>();
		List<String> availableCityNames = new ArrayList<>(cityMap.keySet());
		// initialise monsters: create the monsters and drop them in  random cities
		for (int monsterIndex = 0; monsterIndex < monsterNum; monsterIndex++) {
			Monster monster = new Monster(monsterIndex);
			monsters.add(monster);
			do {
				// assign random city that allows addition of monster, out of available (active) cities
				int randomIndex = rand.nextInt(availableCityNames.size());
				City cityToVisit = cityMap.get(availableCityNames.get(randomIndex));
				monsterActivity.travelToCity(cityToVisit, monster, availableCityNames, randomIndex);
			//continue loop if monster is not able to travel to a city or all active cities filled up
			} while (monster.getCurrentCity() == null && availableCityNames.size() > 0);

		}
		availableCityNames = null;
		//move the monsters around
		long numberOfCitiesDestroyed = startTravelling(monsters);
		//save the remaining cities
		mapGenerator.saveMap(cityMap, outputFilePath);

		GameStats gameStats = new GameStats(monsterNum, monsters.stream().filter(m -> !m.isActive()).count(),
				cityMap.size(), numberOfCitiesDestroyed);
		System.out.println("Game Over !");
		return gameStats;
	}
	
	/**
	 * Move the monsters around and when there are two monsters in one city, they fight till death 
	 * and destroy the city.
	 * Continue moving till all monsters are dead or they have moved 10,000 times
	 */
	private long startTravelling(List<Monster> monsters) {
		long activeMonsterCount = 0;
		int interationCount = 0;
		do {
			// shuffle monster list so that order in which monsters travel are random for each iteration
			Collections.shuffle(monsters);
			//Only active and untrapped monster can travel to another city
			monsters.stream().filter(m -> m.isActive() && !m.isTrapped()).forEach(activeMonster -> {
				monsterActivity.travelToRandomCity(activeMonster);
			});
			// find how may Monsters are still active after one iteration of monster travel
			activeMonsterCount = monsters.stream().filter(Monster::isActive).count();
			interationCount++;

			//continue loop till no monster is alive or iteration has reached  MAX_ITERATION (10000)
		} while (activeMonsterCount > 0 && interationCount < MAX_ITERATION);
		
		long numberOfCitiesDestroyed = cityMap.values().stream().filter(city -> !city.isActive()).count();
		// log.info("Number of Monsters active :"+activeMonsterCount+" total iteration:"+interationCount);
		System.out.println("Number of Monsters still alive :"+activeMonsterCount+", Number of cities destroyed :"+ 
		        numberOfCitiesDestroyed+", Total iteration:"+interationCount);
	    return numberOfCitiesDestroyed;
	}

}
