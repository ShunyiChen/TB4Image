package com.maxtree.imageprocessor.services;

/**
 * 
 * @author chens
 *
 */
public class ImageProcessorException extends Exception {

	// Parameterless Constructor
	public ImageProcessorException() {}
	
	// Constructor that accepts a message
	public ImageProcessorException(String msg) {
		super(msg);
	}
}
