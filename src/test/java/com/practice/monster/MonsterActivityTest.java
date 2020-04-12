package com.practice.monster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.practice.monster.model.City;
import com.practice.monster.model.Monster;
import com.practice.monster.model.enums.Direction;

class MonsterActivityTest {

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@DisplayName("Test whether monster can visit the city : canVisit")
	@ParameterizedTest
	@MethodSource("getCitiesAndExpectedBoolean")
	public void canVisitTest(City city, boolean expectedCanVisitBoolean) {
		MonsterActivity monsterActivity = new MonsterActivity();
		assertEquals(expectedCanVisitBoolean, monsterActivity.canVisit(city));
	}

	static Stream<Arguments> getCitiesAndExpectedBoolean() {
		Monster monster1 = new Monster(1);
		Monster monster2 = new Monster(2);
		City cityWithNoMonster = new City("CityWithNoMonster");
		City cityWithOneMonster = new City("CityWithOneMonster");
		cityWithOneMonster.addMonster(monster1);
		City cityWithTwoMonster = new City("CityWithOneMonster");
		cityWithTwoMonster.addMonster(monster1);
		cityWithTwoMonster.addMonster(monster2);
		City destroyedCity = new City("DestroyedCity");
		destroyedCity.destroy();
		return Stream.of(arguments(cityWithNoMonster, true), arguments(cityWithOneMonster, true),
				arguments(cityWithTwoMonster, false), arguments(destroyedCity, false));
	}

	@DisplayName("Test monster travelling to city : travelToCity")
	@ParameterizedTest
	@MethodSource("getCitiesAndMonsterAndExpectedBoolean")
	public void travelToCityTest(City cityToVisit, Monster monster, List<City> cities, int index,
			boolean expectedCityActive) {
		int initialCityListSize = cities.size();
		List<City> modifiableList = new ArrayList<>(cities);
		MonsterActivity monsterActivity = new MonsterActivity();
		monsterActivity.travelToCity(cityToVisit, monster, modifiableList, index);
		assertEquals(expectedCityActive, cityToVisit.isActive());
		assertEquals(expectedCityActive, monster.isActive());
		if (expectedCityActive)
			assertEquals(initialCityListSize, modifiableList.size());
		else
			assertEquals(initialCityListSize - 1, modifiableList.size());
	}

	static Stream<Arguments> getCitiesAndMonsterAndExpectedBoolean() {
		Monster monster1 = new Monster(1);
		Monster monster2 = new Monster(2);
		City city1 = new City("City1");
		City city2 = new City("City2");
		City city3 = new City("City3");
		return Stream.of(arguments(city1, monster1, Arrays.asList(city1, city2, city3), 0, true),
				arguments(city1, monster2, Arrays.asList(city1, city2, city3), 0, false));
	}

	@DisplayName("Test monster travelling to random city : travelToRandomCity")
	@ParameterizedTest
	@MethodSource("getMonsters")
	public void travelToRandomCityTest(Monster monster) {
		
		MonsterActivity monsterActivity = new MonsterActivity();
		City previousCity = monster.getCurrentCity();
		//collect the list of city that are neighbours of previous city
		List<City> neighbouringCities = previousCity.getNeighbouringCities().stream()
				.map(neighbour -> neighbour.getCity()).filter(City::isActive).collect(Collectors.toList());
		if (monster.isActive()) {
			monsterActivity.travelToRandomCity(monster);
			assertNotNull(monster.getCurrentCity());
			assertNotEquals(previousCity, monster.getCurrentCity());
			if (monster.isActive()) {
				assertTrue(monster.getCurrentCity().isActive());
			} else {
				assertFalse(monster.getCurrentCity().isActive());
				assertEquals(0, monster.getCurrentCity().getMonsters().stream().filter(Monster::isActive).count());
			}
			assertTrue(neighbouringCities.contains(monster.getCurrentCity()));
		} else {//monster not active (already killed)
			monsterActivity.travelToRandomCity(monster);
			assertEquals(previousCity, monster.getCurrentCity()); //Dead Monster cannot be moved to another city
			assertFalse(monster.getCurrentCity().isActive());
			//All monster for the current city are killed
			assertEquals(0, monster.getCurrentCity().getMonsters().stream().filter(Monster::isActive).count());
		}
	}

	static Stream<Arguments> getMonsters() {
		Monster monster1 = new Monster(1);
		Monster monster2 = new Monster(2);
		City city1 = new City("City1");
		City city2 = new City("City2");
		City city3 = new City("City3");
		City city4 = new City("City4");
		city1.addNeighbouringCity(city2, Direction.north);
		city1.addNeighbouringCity(city3, Direction.east);
		city1.addNeighbouringCity(city4, Direction.west);
		city1.addMonster(monster1);
		city2.addNeighbouringCity(city1, Direction.south);
		city2.addMonster(monster2);
		return Stream.of(arguments(monster1), arguments(monster2));
	}

}
