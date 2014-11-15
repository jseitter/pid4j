package org.pid4j.pid;

public class DefaultPid extends AbstractPid implements Pid {
//	private long lastTime = System.currentTimeMillis();
	private Double sampleTime = 100.0;  // default = 0.1 seconds = 100 milliseconds
	
	public Double compute(Double value) {
		Double error = setPoint - value;
		
		// P term
		Double pTerm = error;
		// I term
//		long now = System.currentTimeMillis();
//		long deltaTime = now - lastTime;
//		deltaTime = (deltaTime <= 0) ? 1 : deltaTime;
		
		double sampleTimeInSeconds = sampleTime / 1000.0;
		
//		lastTime = now;
		iTerm += (error * sampleTimeInSeconds);
		iTerm = getValueWithinLimits(iTerm);
		// D term
		Double dTerm = 0.0;
		if (initialized) {
			dTerm = (value - lastInput) / sampleTimeInSeconds;
		}
		
		lastInput = value;
		output = direction.getValue() * (kp * pTerm + ki * iTerm - kd * dTerm);
		output = getValueWithinLimits(output);
		
		initialized = true;
		return output;
	}

	public Double getSampleTime() {
		return sampleTime;
	}

	public void setSampleTime(Double sampleTime) {
		this.sampleTime = sampleTime;
	}
}
