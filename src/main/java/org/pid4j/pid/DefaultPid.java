package org.pid4j.pid;

public class DefaultPid extends AbstractPid implements Pid {
	private long lastTime = System.currentTimeMillis();
	
	public Double compute(Double value) {
		Double error = setPoint - value;
		
		// P term
		Double pTerm = error;
		
		// I term
		long now = System.currentTimeMillis();
		long deltaTime = now - lastTime;
		deltaTime = (deltaTime <= 0) ? 1 : deltaTime;
		lastTime = now;
		iTerm += (error / deltaTime);
		iTerm = getValueWithinLimits(iTerm);
		
		// D term
		Double dTerm = (value - lastInput) / lastTime;
		lastInput = value;
		
		output = direction.getValue() * (kp * pTerm + ki * iTerm - kd * dTerm);
		output = getValueWithinLimits(output);
		return output;
	}
	
}