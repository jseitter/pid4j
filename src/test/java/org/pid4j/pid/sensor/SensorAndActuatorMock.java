package org.pid4j.pid.sensor;

import org.pid4j.pid.actuator.PidActuator;

public class SensorAndActuatorMock implements PidActuator, PidSensor {
	private Double value = 1.0;
	
	public void setValue(Double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}
}
