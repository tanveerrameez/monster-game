package com.practice.monster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.practice.monster.exceptions.InvalidMapFormatException;
import com.practice.monster.model.City;
import com.practice.monster.model.enums.Direction;
import com.practice.monster.model.enums.State;

class MapGeneratorTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@DisplayName("Test loading of partial map : generateMap")
	@ParameterizedTest
	@ValueSource(strings = {"20citiesIncompleteMap.txt"})
	void loadPartialMapTest(String mapFilePath) throws IOException{
		Path filePath = Paths.get(mapFilePath);
		MapGenerator mapGenerator = new MapGenerator();
		Map<String, City> cityMap = mapGenerator.generateMap(filePath);
		assertNotNull(cityMap);
	}
	
	@DisplayName("Test loading of complete map: generateMap")
	@ParameterizedTest
	@ValueSource(strings = {"12citiesmap.txt","map.txt"})
	void loadCompleteMapTest(String mapFilePath) throws IOException{
		Path filePath = Paths.get(mapFilePath);
		long numberOfLines =0;
		try (Stream<String> lines = Files.lines(filePath)){
			numberOfLines = lines.count();
		}
		MapGenerator mapGenerator = new MapGenerator();
		Map<String, City> cityMap = mapGenerator.generateMap(filePath);
		assertNotNull(cityMap);
		assertEquals(numberOfLines, cityMap.size());
		Set<String> cityNamesSet = cityMap.keySet();
		cityNamesSet.forEach(cityName -> {
			City city = cityMap.get(cityName);
			assertTrue(city.isActive());
			//assert all neighbouring cities are correctly linked
			city.getNeighbouringCities().forEach(neighbouringCity -> {
				assertNotNull(cityMap.get(neighbouringCity.getCity().getName()));
			});
		});
	}
	
	@DisplayName("Test saving of City Map (active cities) into file: saveMap")
	@ParameterizedTest
	@MethodSource("getCitiesMap")
	void savePathTest(Map<String, City> cityMap, String saveFilePath, int destroyedCityIndex) throws Exception{

		List<City> destroyedCities = cityMap.values().stream().filter(city -> !city.isActive()).collect(Collectors.toList());
		
		MapGenerator mapGenerator = new MapGenerator();
		//save the Map
		mapGenerator.saveMap(cityMap, saveFilePath);
		
		//load the map
		Map<String, City> cityMapFromFile = mapGenerator.generateMap(Paths.get(saveFilePath));
		assertNotNull(cityMapFromFile);
		assertEquals(cityMap.size()-destroyedCities.size(),cityMapFromFile.size());
		//compare with initial map
		cityMapFromFile.entrySet().forEach(entry -> {
			//check if city in map from file exists in original city map
			City originalCity = cityMap.get(entry.getKey());
			City cityFromFile = entry.getValue();
			assertNotNull(originalCity);
			assertEquals(originalCity, cityFromFile);
			if(cityFromFile.getNeighbouringCities() != null) {
				//match the neighbours from map in file with initial map
				cityFromFile.getNeighbouringCities().forEach(neighbourFromFile ->{
					//compares both neighbouring city as well as direction
					assertTrue(originalCity.getNeighbouringCities().contains(neighbourFromFile));
				});
			}
		});
		destroyedCities.forEach(destroyedCity -> {
			//should not contain the destroyed city
			assertNull(cityMapFromFile.get(destroyedCity.getName()));
		});
	}
	
	static Stream<Arguments> getCitiesMap(){
		//Create Map
		City[] cities = new City[10];
		for(int i=0; i<cities.length; i++) {
			cities[i] = new City("City"+i);
		}
		cities[0].addNeighbouringCity(cities[1], Direction.north);
		cities[0].addNeighbouringCity(cities[2], Direction.south);
		cities[0].addNeighbouringCity(cities[3], Direction.east);
		cities[0].addNeighbouringCity(cities[4], Direction.west);
		cities[1].addNeighbouringCity(cities[0], Direction.south);
		cities[2].addNeighbouringCity(cities[0], Direction.north);
		cities[2].addNeighbouringCity(cities[8], Direction.west);
		cities[2].addNeighbouringCity(cities[9], Direction.south);
		cities[2].addNeighbouringCity(cities[6], Direction.east);
		cities[3].addNeighbouringCity(cities[0], Direction.west);
		cities[3].addNeighbouringCity(cities[5], Direction.north);
		cities[3].addNeighbouringCity(cities[6], Direction.south);
		cities[3].addNeighbouringCity(cities[7], Direction.east);
		cities[4].addNeighbouringCity(cities[0], Direction.east);
		cities[5].addNeighbouringCity(cities[3], Direction.south);
		cities[6].addNeighbouringCity(cities[2], Direction.west);
		cities[6].addNeighbouringCity(cities[3], Direction.north);
		cities[7].addNeighbouringCity(cities[3], Direction.west);
		cities[8].addNeighbouringCity(cities[2], Direction.east);
		cities[9].addNeighbouringCity(cities[2], Direction.north);
		cities[5].setState(State.DESTROYED);
		Map<String, City> cityMap = new HashMap<>();
		for(int i=0; i<cities.length; i++) {
			cityMap.put(cities[i].getName(), cities[i]);
		}
		return Stream.of(arguments(cityMap, "outputmap.txt", 5));
	}
	
	@DisplayName("Test for invalid Map")
	@ParameterizedTest
	@ValueSource(strings = {"invalidmap.txt"})
	void invalidMapTest(String inputMapfile) throws Exception {
		MapGenerator mapGenerator = new MapGenerator();
			assertThrows(InvalidMapFormatException.class, () -> mapGenerator.generateMap(Paths.get(inputMapfile)));
	}
	
	@DisplayName("Test for non existent Map")
	@ParameterizedTest
	@ValueSource(strings = {"nonexistent_map.txt"})
	void nonExistentMapTest(String inputMapfile) throws Exception {
		MapGenerator mapGenerator = new MapGenerator();
			assertThrows(NoSuchFileException.class, () -> mapGenerator.generateMap(Paths.get(inputMapfile)));
	}

}
