package com.practice.monster;

import java.nio.file.NoSuchFileException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.practice.monster.model.GameStats;

@SpringBootApplication
public class MonstersApplication {

	/**
	 * Main method that can be run from command line usage: MonsterGame [number of
	 * monster] [input map file] [output map file] input map file (optional),
	 * defaults to map.txt if not provided output map file (optional), defaults to
	 * outputmap.txt if not provided
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("usage: MonstersApplication [number of monster] [input map file] [output map file]");
			System.out.println("[number of monster] , defaults to 10");
			System.out.println("input map file (optional), defaults to map.txt if not provided");
			System.out.println("output map file (optional), defaults to outputmap.txt if not provided");
			System.out.println("-----------------------------------------------");
		}
		try {
			MonsterGame monsterGame = new MonsterGame(args != null && args.length >= 2 ? args[1] : "map.txt",
					args != null && args.length >= 3 ? args[2] : "outputmap.txt");
			GameStats gameStats = monsterGame.conquer(args.length >0 ? Integer.parseInt(args[0]) : 10);
		} catch(NoSuchFileException nsfe) {
			System.out.println(nsfe.getClass()+": "+nsfe.getMessage()+" not found or incorrect file path provided!");
			//nsfe.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
