package org.pid4j.pid;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BasicPidTest {
	private Pid pid;
	
	@Before
	public void setUp() throws Exception {
		pid = new BasicPid();
		pid.setKpid(5.0, 2.0, 3.0);
		pid.setOutputLimits(0.0, 255.0);
	}
	
	@Test
	public void testComputeTemperature() {
		pid.setSetPoint(22.0);
		
		Double output = pid.compute(25.0);
		assertEquals(0.0, output);
		output = pid.compute(24.0);
		assertEquals(0.0, output);
		output = pid.compute(22.0);
		assertEquals(6.0, output);
		output = pid.compute(22.0);
		assertEquals(0.0, output);
		output = pid.compute(18.0);
		assertEquals(40.0, output);
		output = pid.compute(20.0);
		assertEquals(16.0, output);
	}
}
