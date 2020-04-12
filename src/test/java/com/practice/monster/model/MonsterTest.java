package com.practice.monster.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MonsterTest {
	
	private final String Monster1ToString = Monster.MONSTER+" "+1;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@DisplayName("Test initialisation")
	@Test
	void initialiseMonsterTest() {
		Monster monster = new Monster(1);
		assertTrue(monster.isActive());
		assertNull(monster.getCurrentCity());
	}
	
	@Test void monsterEqualsTest() {
		int id =100;
		Monster monster1 = new Monster(id);
		Monster monster2 = new Monster(id);
		assertTrue(monster1.equals(monster2));
	}
	
	@Test void monsterToStringTest() {
		Monster monster = new Monster(1);
		assertEquals(Monster1ToString, monster.toString());
	}
	
	@Test void destroyMonsterTest() {
		Monster monster = new Monster(1);
		assertTrue(monster.isActive());
		monster.destroy();
		assertFalse(monster.isActive());
	}
	
}
