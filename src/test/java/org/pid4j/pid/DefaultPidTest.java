package org.pid4j.pid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultPidTest {
	private Pid pid;

	private void testCompute(double input, double expectedOutput) {
		Double output = pid.compute(input);
		assertEquals(expectedOutput, output, 0.01);
	}

	@BeforeEach
	public void setup() throws Exception {
		this.pid = new DefaultPid();
		pid.setOutputLimits(-255.0, 255.0);
		pid.setSetPoint(34.8);
	}
	
	@Test
	public void testP() throws InterruptedException {
		pid.setKpid(2.0, 0.0, 0.0);
		testCompute(23.47, 22.66);
		testCompute(29.4, 10.8);
		testCompute(38.3, -7);
		testCompute(34.8, 0);
	}
	
	@Test
	public void testPI() throws InterruptedException {
		pid.setKpid(2.0, 5.0, 0.0);
		((DefaultPid) pid).setSampleTime(10); 
		testCompute(23.47, 23.2265);
		testCompute(29.4, 11.6365);
		testCompute(38.3, -6.3385);
		testCompute(34.8, 0.6615);
	}

	@Test
	public void testPID() throws InterruptedException {
		pid.setKpid(2.0, 5.0, 0.06);
		((DefaultPid) pid).setSampleTime(10); 
		testCompute(23.47, 23.2265);
		testCompute(29.4, -23.9435);
		testCompute(38.3, -59.7385);
		testCompute(34.8, 21.6615);
	}
	
	@Test
	public void testPIDReverse() throws InterruptedException {
		pid.setKpid(2.0, 5.0, 0.06);
		pid.setDirection(Direction.REVERSE);
		((DefaultPid) pid).setSampleTime(10); 
		testCompute(23.47, -23.2265);
		testCompute(29.4, 23.9435);
		testCompute(38.3, 59.7385);
		testCompute(34.8, -21.6615);
	}
}
