package org.pid4j.pid;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.pid4j.pid.sensor.SensorAndActuatorMock;

public class BackgroundPidTest {
	private static final int WAIT_MILLISECONDS = 100;
	private BackgroundPid pid;
	
	@Before
	public void setUp() throws Exception {
		this.pid = new BackgroundPid(10.0, 1.0, 2.0, 3.0, new SensorAndActuatorMock(), new SensorAndActuatorMock(), WAIT_MILLISECONDS);
	}
	
	@Test
	public void testCompute() {
		this.pid.start();
		//fail("Not yet implemented");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
