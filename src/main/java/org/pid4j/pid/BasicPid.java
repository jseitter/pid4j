package org.pid4j.pid;

public class BasicPid extends AbstractPid implements Pid {
	public Double compute(Double value) {
		Double error = setPoint - value;
		
		// P term
		Double pTerm = error;
		
		// I term
		iTerm += error;
		iTerm = getValueWithinLimits(iTerm);
		
		// D term
		Double dTerm = value - lastInput;
		lastInput = value;
		
		output = direction.getValue() * (kp * pTerm + ki * iTerm - kd * dTerm);
		output = getValueWithinLimits(output);
		return output;
	}

}