package com.maxtree.imageprocessor.services;

/**
 * 
 * @author chens
 *
 */
public class Command {
	
	/**
	 * 
	 * @param option
	 * @param value
	 */
	public Command(String option, double... value) {
		this.option = option;
		this.value = value;
	}
	
	public Command(String option, String shadowsArg) {
		this.option = option;
		this.shadowsArg = shadowsArg;
	}
	
	public String option;
	public double[] value;
	public String shadowsArg;
}
