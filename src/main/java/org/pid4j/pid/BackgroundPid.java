package org.pid4j.pid;

import org.pid4j.pid.actuator.PidActuator;
import org.pid4j.pid.sensor.PidSensor;

public class BackgroundPid extends AbstractPid implements Runnable {
	private long lastTime = 0;
	private PidSensor sensor;
	private PidActuator actuator;
	private long waitMilliseconds;
	private Thread pidThread;
	private boolean stopThread;
	
	public BackgroundPid(Double setPoint, Double kp, Double ki, Double kd, PidSensor pidSensor, PidActuator pidActuator, long waitMilliseconds) {
		this.setPoint = setPoint;
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.sensor = pidSensor;
		this.actuator = pidActuator;
		this.waitMilliseconds = waitMilliseconds;
		pidThread = new Thread(this);
	}
	
	public void start() {
		if (lastTime == 0) {
			lastTime = getTime();
		}
		this.stopThread = false;
		this.pidThread.start();
	}
	
	public void stop() {
		// Try to stop nicely
		this.stopThread = true;
	}
	
	public Double compute(Double value) {
		double sampleTimeInSeconds = ((double) waitMilliseconds) / 1000;
		
		Double error = setPoint - value;
		
		// P term
		Double pTerm = error;
		
		// I term
		long now = getTime();
		long deltaTime = (now - lastTime);
		deltaTime = (deltaTime <= 0) ? 1 : deltaTime;
		lastTime = now;
		iTerm += (error / deltaTime);
		iTerm = getValueWithinLimits(iTerm);
		
		// D term
		Double dTerm = (value - lastInput) / lastTime;
		lastInput = value;
		
		output = direction.getValue() * (kp * pTerm + (ki * sampleTimeInSeconds * iTerm) - ((kd * dTerm) / sampleTimeInSeconds));
		output = getValueWithinLimits(output);
		return output;
	}
	
	public void run() {
		while (!stopThread) {
			double value = sensor.getValue();
			double actuatorValue = compute(value);
			actuator.setValue(actuatorValue);

			try {
				Thread.sleep(waitMilliseconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected long getTime() {
		return System.currentTimeMillis();
	}
}
