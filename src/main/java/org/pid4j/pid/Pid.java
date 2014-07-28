package org.pid4j.pid;

public interface Pid {
	public Double compute(Double value);
	
	public Double getOutput();
	
	public void setDirection(Direction direction);
	public Direction getDirection();
	
	public void setSetPoint(Double value);
	public Double getSetPoint();
	
	public void setOutputLimits(Double min, Double max);
	public Double getMinOutputLimit();
	public Double getMaxOutputLimit();
	
	public void setKpid(Double kp, Double ki, double kd);
	
	public Double getKp();
	public void setKp(Double kp);

	public Double getKi();
	public void setKi(Double ki);

	public Double getKd();
	public void setKd(Double kd);
}
