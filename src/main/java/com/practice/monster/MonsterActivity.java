package com.practice.monster;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.practice.monster.exceptions.InvalidActivityException;
import com.practice.monster.model.City;
import com.practice.monster.model.Monster;

/**
 * This class manages monster activities
 * @author Tanveer Rameez Ali
 */
public class MonsterActivity {

	//private final static Logger log = Logger.getLogger(MonsterActivity.class);
	public static final int MAX_MONSTER_ALLOWED = 2;
	private static final String destructionMessage = "%s has been destroyed by %s and %s !%n";
	private static final String monsterTrappedMessage = "%s is trapped in city %s !%n";

	
	/**
	 * Move the Monster from the current city to a neighbouring random city which is not destroyed (i.e. still active)
	 * If there exist already one monster in the city the current monster is travelling to, 
	 * both monsters kill each other and the city is destroyed.
	 * The current city neighbour list is updated to reflect the destroyed neighbouring city
	 * @param monster
	 */
	public void travelToRandomCity(Monster monster) {
		if (!monster.isActive()) {
			return;
		}
		City currentCity = monster.getCurrentCity();
		if (currentCity.getNeighbouringCities() != null) {//should not be null, but checked just in case of faulty map
			List<City> neighbouringCities = currentCity.getNeighbouringCities().stream().filter(neighbour -> neighbour.getCity().isActive()).
					map(neighbour -> neighbour.getCity()).collect(Collectors.toList());

			if (neighbouringCities.size() > 0) {// if atleast one neighbouring city is active
				Random rand = new Random();
				// de-link monster and city since Monster is leaving the current city
				currentCity.removeMonster(monster);
				do {//go to a random city from the neighbouringCities list (active cities)
					int randomIndex = rand.nextInt(neighbouringCities.size());
					City cityToVisit = neighbouringCities.get(randomIndex);
					if (cityToVisit == currentCity) {// should not occur, but just in case of faulty map
						throw new InvalidActivityException("City to visit and current City are same!!!:" + cityToVisit);
					}
					travelToCity(cityToVisit, monster, neighbouringCities, randomIndex);
				} while (monster.getCurrentCity() == null && neighbouringCities.size() > 0);

			} else {//no active neighbouring cities, so monster is trapped
				//log.warn(String.format(monsterTrappedMessage, monster, currentCity.getName()));
				monster.setTrapped(true);
			}
		}
	}

	/**
	 * Places the monster in the city if allowed (i.e. if none or only monster
	 * already exist in the city) If total number of monster after adding is now
	 * two, the monsters in the city are killed and city is destroyed.
	 * Destroyed cities are removed from list of available cities so that further visits does
	 * not include this city
	 * 
	 * @param cityToVisit
	 * @param monster
	 * @param cities
	 * @param index
	 */
	public void travelToCity(City cityToVisit, Monster monster, List<?> cities, int index) {
		if (canVisit(cityToVisit)) {
			cityToVisit.addMonster(monster);
			/*
			 *  AddMonster method might result in two Monsters in the city which will destroy both the monsters
			 *  as well as the city. so check if this has occurred and if so, print out a message
			 */
			if(!cityToVisit.isActive()) {
				Iterator<Monster> monsterIter = cityToVisit.getMonsters().iterator();
				//log.info(String.format(destructionMessage, cityToVisit.getName(),monsterIter.next(),monsterIter.next()));
				System.out.printf(destructionMessage, cityToVisit.getName(),monsterIter.next(),monsterIter.next());
				cities.remove(index);
			}
		} else // cannot visit the city, so remove it from available list of cities
			cities.remove(index);
	}


	/**
	 * Checks whether the Monster can visit the city or not.
	 * If there is more than MAX_MONSTER_ALLOWED monsters already in the city, then not allowed
	 * @param city to visit
	 * @return boolean
	 */
	public boolean canVisit(City city) {
		return city.isActive()
				&& (city.getMonsters() == null || city.getMonsters().size() < MonsterActivity.MAX_MONSTER_ALLOWED);
	}

}
