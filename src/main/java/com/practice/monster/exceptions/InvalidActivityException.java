package com.practice.monster.exceptions;

/**
 * User defined Exception class
 * @author Tanveer Rameez Ali
 */
public class InvalidActivityException extends RuntimeException{

	public InvalidActivityException(String message) {
		super(message);
	}
	
}
