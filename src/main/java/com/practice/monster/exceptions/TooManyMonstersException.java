package com.practice.monster.exceptions;

/**
 * User defined exception class
 * @author Tanveer Rameez Ali
 */
public class TooManyMonstersException extends RuntimeException{
	public TooManyMonstersException(String message) {
		super(message);
	}
}
