package org.pid4j.pid;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DefaultPidTest {
	private Pid pid;

	@Before
	public void setup() throws Exception {
		this.pid = new DefaultPid();
		pid.setKpid(5.0, 2.0, 3.0);
		pid.setOutputLimits(0.0, 255.0);
	}
	
	@Test
	public void testComputeTemperature() throws InterruptedException {
		pid.setSetPoint(22.0);
		
		Double output = pid.compute(25.0);
		assertEquals(0.0, output);
		Thread.sleep(1000);
		output = pid.compute(24.0);
		assertEquals(0.0, output);
		Thread.sleep(1000);
		output = pid.compute(22.0);
		assertEquals(6.0, output);
		Thread.sleep(1000);
		output = pid.compute(22.0);
		assertEquals(0.0, output);
		Thread.sleep(1000);
		output = pid.compute(18.0);
		assertEquals(40.0, output);
		Thread.sleep(1000);
		output = pid.compute(20.0);
		assertEquals(16.0, output);
	}

}
