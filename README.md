# Project Title
A Monsters attacking a city game
Please see instructions.md for description of the problem

# Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
This is a spring Boot project

# Prerequisites
1. JDK 1.8 
2. Maven
2. You need to set JAVA_HOME variable in your host environment variables where you need to run the program.
3. Also you need to set PATH variable of your host environment variable.

# Assumptions:
1. The city name is case sensitive and will be consistently in the same case throughout the map
2. City Name will be just one word (i.e. no 'New York') without any white space
3. The neighboring cities are provided as direction=city format always.
4. Direction will only be one of east, west, north, south (case sensitive)
5. Maximum number of monsters allowed is twice the number of cities
6. The whole flow of the program is in a single thread. Hence the monsters travel from one city to another sequentially.

Direction to run:
1. Load the source code in an editor, for eg. Eclipse STS or IntelliJ. You can import it as a maven project.
2. To run the test, the tests are available in src/test/java
3. You can run the main method in com.practice.monster.MonstersApplication.java from your Java IDE.
5. In command prompt, when you are in 'monsters' directory,
    a. build the project using 'mvn clean install'.
    b. cd target/classes 
	c. java com.practice.monster.MonstersApplication [number of monster] [input map file] [output map file]
	where
	[number of monster] , defaults to 10
	[input map file] , defaults to map.txt
	[output map file], defaults to output.txt
	
	d. for example: java com.practice.monster.MonstersApplication 1000 ../../map.txt ../../out.txt
	
    Please ensure the input map file is correctly referenced. The map.txt is in the 'monsters' directory, 
    hence when running using command prompt from target/classes directory, the path should be ../../map.txt from target/classes
    
#Author
Tanveer Rameez (tanveerrameez@yahoo.com)
