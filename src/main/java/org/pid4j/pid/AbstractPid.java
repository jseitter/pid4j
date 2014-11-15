package org.pid4j.pid;

public abstract class AbstractPid implements Pid {
	protected Double kp = 1.0, ki = 1.0, kd = 1.0;
	protected Double setPoint = 1.0;
	protected Double output = 0.0;
	protected Double min = 0.0, max = 255.0;
	protected Double iTerm = 0.0;
	protected Double lastInput = 0.0;
	protected Direction direction = Direction.DIRECT;
	protected boolean initialized = false;
	
	public abstract Double compute(Double value);
	
	protected Double getValueWithinLimits(Double value) {
		return (value < min) ? min : (value > max) ? max : value;
	}
	
	public Double getOutput() {
		return output;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	
	public void setSetPoint(Double value) {
		this.setPoint = value;
	}
	
	public Double getSetPoint() {
		return setPoint;
	}
	
	public void setOutputLimits(Double min, Double max) {
		this.min = min;
		this.max = max;
		output = getValueWithinLimits(output);
	}
	
	public Double getMinOutputLimit() {
		return min;
	}
	
	public Double getMaxOutputLimit() {
		return max;
	}
	
	public void setKpid(Double kp, Double ki, double kd) {
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
	}
	public Double getKp() {
		return kp;
	}
	
	public void setKp(Double kp) {
		this.kp = kp;
	}
	
	public Double getKi() {
		return ki;
	}
	
	public void setKi(Double ki) {
		this.ki = ki;
	}
	
	public Double getKd() {
		return kd;
	}
	
	public void setKd(Double kd) {
		this.kd = kd;
	}
}
