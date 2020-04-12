package com.practice.monster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.practice.monster.exceptions.TooManyMonstersException;
import com.practice.monster.model.City;
import com.practice.monster.model.GameStats;

class MonsterGameTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@DisplayName("Test for correct execution of the game")
	@ParameterizedTest
	@CsvSource({"10,12citiesmap.txt,minimapoutput.txt",
	           "18,12citiesmap.txt,minimapoutput.txt",
	           "24,12citiesmap.txt,minimapoutput.txt",
		        "1,12citiesmap.txt,minimapoutput.txt",
	            "1000, map.txt, outputmap.txt"           
	})
	void playGameTest(int numberOfMonster, String inputMapfile, String outputMapfile) throws Exception {
		
			MonsterGame monsterGame = new MonsterGame(inputMapfile, outputMapfile);
			GameStats gameStats = monsterGame.conquer(numberOfMonster);
			//GameStats contains information about some game data
			assertEquals(numberOfMonster, gameStats.getNumOfMonstersCreated());
			//ratio of monsters killed:city destroyed is 2:1, so assert this ratio
			assertEquals(gameStats.getNumOfMonstersKilled()/2, gameStats.getNumOfCitiesDestroyed());
			
			//load the final output map
			MapGenerator mapGenerator = new MapGenerator();
			Map<String, City> cityMap = mapGenerator.generateMap(Paths.get(outputMapfile));
			//compare if number of cities in final output map is equal to (total number of cities - cities destroyed)
			assertEquals(gameStats.getTotalNumberOfCities() - gameStats.getNumOfCitiesDestroyed(), cityMap.size() );
	}
	
	@DisplayName("Test for too many monster error (more than double the number of cities)")
	@ParameterizedTest
	@CsvSource({"25,12citiesmap.txt,minimapoutput.txt",
                "180,12citiesmap.txt,minimapoutput.txt"
        })
	void gameWithTooManyMonsterTest(final int numberOfMonster, String inputMapfile, String outputMapfile) throws Exception {
			MonsterGame monsterGame = new MonsterGame(inputMapfile, outputMapfile);
			assertThrows(TooManyMonstersException.class, () -> monsterGame.conquer(numberOfMonster));
	}
	
	@DisplayName("Test for 0 or negative number of monster")
	@ParameterizedTest
	@CsvSource({"-5,12citiesmap.txt,minimapoutput.txt",
		        "0,12citiesmap.txt,minimapoutput.txt",
        })
	void gameWithNegativeMonsterTest(final int numberOfMonster, String inputMapfile, String outputMapfile) throws Exception {
		MonsterGame monsterGame = new MonsterGame(inputMapfile, outputMapfile);
		assertThrows(IllegalArgumentException.class, () -> monsterGame.conquer(numberOfMonster));
    }

}
