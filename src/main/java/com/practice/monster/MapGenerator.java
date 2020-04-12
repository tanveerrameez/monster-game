package com.practice.monster;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.practice.monster.exceptions.InvalidMapFormatException;
import com.practice.monster.model.City;
import com.practice.monster.model.enums.Direction;

/**
 * This class manages loading generation and saving of city maps
 * The map format is as specified in instructions.md
 * @author Tanveer Rameez Ali
 */
public class MapGenerator {
	
	//private final static Logger log = Logger.getLogger(MapGenerator.class);
	private static final String WHITE_SPACE = " ";
	private static final String EQUAL = "=";
	

	/**
	 * Generates Map based on data from file
	 * The file format is as per instructions.md
	 * The cities are stored in a HashMap with the name of the city as the key
	 * @param filePath path of the file containing the map details
	 * @return the city map created from the file
	 * @throws IOException 
	 */
	public Map<String, City> generateMap(Path filePath) throws IOException {
		Map<String, City> cityMap = new ConcurrentHashMap<>();
		try (Stream<String> lines = Files.lines(filePath)) {
			lines.forEach(line -> {
				String[] words = line.split(WHITE_SPACE);
				String cityName = words[0];
				City city = getCityFromMap(cityMap, cityName);
				City nextCity;
				for(int i=1; i<words.length; i++) {
					String[] directionAnCity = words[i].split(EQUAL);
					if(directionAnCity.length != 2) throw new InvalidMapFormatException(
							"City map for "+cityName+ " does not have direction=city format!");
					String direction = directionAnCity[0];
					nextCity = getCityFromMap(cityMap, directionAnCity[1]);
					city.addNeighbouringCity(nextCity, Direction.valueOf(direction) );
				}
			});
		}
		return cityMap;
	}
	
	/**
	 * Saves the map of active cities in the format specified in instructions.md
	 * @param cityMap The city map containing the active cities to save
	 * @param saveFilePath The file path of the output file
	 * @throws IOException
	 */
	public void saveMap(Map<String, City> cityMap, String saveFilePath) throws IOException{
		List<String> linesList = 
				cityMap.entrySet().stream().filter(entry -> entry.getValue().isActive()).
				map(entry -> entry.getValue())
				.map(city -> {
					String line = city.getName() + MapGenerator.WHITE_SPACE +
					city.getNeighbouringCities().stream().filter(neighbour -> neighbour.getCity().isActive()).map(neighbour -> {
						return neighbour.getDirection()+MapGenerator.EQUAL+neighbour.getCity().getName();
					}).collect(Collectors.joining(MapGenerator.WHITE_SPACE));
					return line;
				}).collect(Collectors.toList());
		
		Files.write(Paths.get(saveFilePath), linesList,Charset.defaultCharset());
		
	}
	
	/**
	 * Gets the city from the map if found, or create a new city and insert into the map
	 * @param cityMap Map from which to get the city or insert a new city
	 * @param cityName Name of the city
	 * @return city  
	 */
	private City getCityFromMap(Map<String, City> cityMap, String cityName) {
		City city;
		if((city = cityMap.get(cityName)) == null) {
			//if city is not in the Map, create and place one in the map
			city = new City(cityName);
			cityMap.putIfAbsent(cityName, city);
		}
		return city;
	}

}
