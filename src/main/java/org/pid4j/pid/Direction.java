package org.pid4j.pid;

/**
 * The direction of the PID. 
 * A DIRECT direction means that more output will lead to more input (eg. when heating a room with a heater: when the heating element uses more energy (more output), the temperature will raise (more input)).
 * A REVERSE direction means that more output will lead to less input (eg. a refrigerator: when the refrigerator uses more energy (more output) the temperature will reduce (less input)).
 * @author Stijn Deknudt
 *
 */
public enum Direction {
	DIRECT(1), REVERSE(-1);
	private final int value;
	
	Direction(int direction) {
		this.value = direction;
	}
	
	public int getValue() {
		return this.value;
	}
}
