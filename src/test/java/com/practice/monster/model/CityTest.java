package com.practice.monster.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.practice.monster.exceptions.InvalidActivityException;
import com.practice.monster.exceptions.TooManyMonstersException;
import com.practice.monster.model.enums.State;

class CityTest {

	private static final String CITY1="city1";
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@DisplayName("Test initialisation")
	@Test
	void initialiseCityTest() {
		City city = new City(CITY1);
		assertTrue(city.isActive());
		assertNull(city.getMonsters());
	}
	
	@DisplayName("Test adding of monsters to the city")
	@ParameterizedTest
	@MethodSource("getCityAndMonster")
	void addMonsterTest(City city, Monster monster, List<Monster> monsters, boolean active, boolean expectedActive, int expectedMonsterCount) {
		assertEquals(active, city.isActive());
		assertEquals(active, monster.isActive());
		city.addMonster(monster);
		assertEquals(expectedActive, city.isActive());
		assertNotNull(city.getMonsters());
		assertEquals(expectedMonsterCount,city.getMonsters().size());
		city.getMonsters().forEach( m -> {
			assertEquals(expectedActive, m.isActive());
			assertEquals(city, m.getCurrentCity());
			monsters.contains(m);	
		});
	}
	
	static Stream<Arguments> getCityAndMonster() {
		Monster monster1 = new Monster(1);
		Monster monster2 = new Monster(2);
		City city = new City(CITY1);
		return Stream.of(arguments(city, monster1, Arrays.asList(monster1, monster2), true, true, 1),
				arguments(city, monster2, Arrays.asList(monster1, monster2), true, false, 2));
	}
	
	@Test void tooManyMonstersExceptionTest() {
		City city = new City(CITY1);
		city.addMonster(new Monster(1));
		city.addMonster(new Monster(2));
		city.setState(State.ACTIVE);
		assertThrows(TooManyMonstersException.class, () -> city.addMonster(new Monster(3)));
	}
	
	
	@Test void addMonsterToDestroyedCityExceptionTest() {
		City city = new City(CITY1);
		city.addMonster(new Monster(1));
		city.addMonster(new Monster(2));
		assertThrows(InvalidActivityException.class, () -> city.addMonster(new Monster(3)));
	}
	
	@Test void duplicateMonsterExceptionTest() {
		City city = new City(CITY1);
		city.addMonster(new Monster(1));
		assertThrows(InvalidActivityException.class, () -> city.addMonster(new Monster(1)));
	}
	
	@Test void cityEqualsTest() {
		City city1 = new City(CITY1);
		City city2 = new City(CITY1);
		assertTrue(city1.equals(city2));
	}
	
	@Test void cityToStringTest() {
		City city1 = new City(CITY1);
		assertEquals(CITY1, city1.toString());
	}
	
	@Test void destroyCityTest() {
		City city = new City(CITY1);
		assertTrue(city.isActive());
		city.addMonster(new Monster(1));
		assertTrue(city.isActive());
		city.addMonster(new Monster(2));
		assertFalse(city.isActive());
		city.getMonsters().forEach( m -> {
			assertFalse(m.isActive());
			assertEquals(city, m.getCurrentCity());
		});
	}
}
