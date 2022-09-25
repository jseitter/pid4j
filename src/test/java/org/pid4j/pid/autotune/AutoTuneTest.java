package org.pid4j.pid.autotune;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.pid4j.pid.DefaultPid;
import org.pid4j.pid.Pid;

@Disabled
public class AutoTuneTest {
	byte autoTuneModeRemember = 2;

	Double input = 80.0, output = 50.0, setpoint = 180.0;
	Double kp = 2.0, ki = 0.5, kd = 2.0;

	Double kpmodel = 1.5, taup = 100.0;
	Double theta[] = new Double[50];
	Double outputStart = 5.0;
	Double autoTuneStep = 50.0, autoTuneNoise = 1.0, autoTuneStartValue = 100.0;
	int autoTuneLookBack = 20;

	double autoTuneOutputStep = 2.0;
	long modelTime = 0, serialTime = 0;

	boolean tuning = false;
	AutoTune autoTune;
	Pid pid;

	@BeforeEach
	public void before() {
		autoTune = new AutoTune();
		pid = new DefaultPid();
		pid.setKpid(kp, ki, kd);
		Arrays.fill(theta, outputStart);
//		autoTune.setNoiseBand(autoTuneNoise);
//		autoTune.setLookbackSec(autoTuneLookBack);
//		autoTune.setOutputStep(autoTuneOutputStep);
		modelTime = 0;
		serialTime = 0;
		if (tuning) {
			tuning = false;
			changeAutoTune();
			tuning = true;
		}
		// ((DefaultPid) pid).setSampleTime(10);
	}

	@Test
	public void testRuntime() {
		long start = System.currentTimeMillis();

		// Begin loop
		while (true) {
			long now = System.currentTimeMillis() - start;

			if (tuning) {
				boolean result = autoTune.runtime(input, output);
				if (result == true) {
					tuning = false;
				}
				if (!tuning) {
					kp = autoTune.getKp();
					ki = autoTune.getKi();
					kd = autoTune.getKd();
					pid.setKpid(kp, ki, kd);
					System.out.println("FINALLY FOUND BEST PARAMETERS: kp=" + kp + "; ki=" + ki + "; kd=" + kd);
				}
			} else {
				output = pid.compute(input);
			}

			theta[30] = output;
			if (now >= modelTime) {
				modelTime += 100;
				doModel();
			}

			// send-receive with processing if it's time
			if (System.currentTimeMillis() - start > serialTime) {
				serialReceive();
				serialSend();
				serialTime += 500;
			}
		}

		// END LOOP
		// while (autoTune.runtime(input, output) == false) {
		// output = pid.compute(input);
		// input = input + (-0.5 + (Math.random()));
		// if (input < 60.5) {
		// input = 60.5;
		// } else if (input > 62.5) {
		// input = 62.5;
		// }
		// }
		// double kP = autoTune.getKp();
		// double kI = autoTune.getKi();
		// double kD = autoTune.getKd();

	}

	private void serialReceive() {
		int b;
		try {
			if (System.in.available() > 0) {
				b = System.in.read();
				if ((b == '1' && !tuning) || (b != '1' && tuning))
					changeAutoTune();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void serialSend() {
		System.out.print("setpoint: ");
		System.out.print(setpoint);
		System.out.print(" ");
		System.out.print("input: ");
		System.out.print(input);
		System.out.print(" ");
		System.out.print("output: ");
		System.out.print(output);
		System.out.print(" ");
		if (tuning) {
			System.out.println("tuning mode");
		} else {
			System.out.print("kp: ");
			System.out.print(pid.getKp());
			System.out.print(" ");
			System.out.print("ki: ");
			System.out.print(pid.getKi());
			System.out.print(" ");
			System.out.print("kd: ");
			System.out.print(pid.getKd());
			System.out.println();
		}
	}

	private void changeAutoTune() {
		if (!tuning) {
			// Set the output to the desired starting frequency.
			output = autoTuneStartValue;
			autoTune.setNoiseBand(autoTuneNoise);
			autoTune.setOutputStep(autoTuneStep);
			autoTune.setLookbackSec((int) autoTuneLookBack);
			tuning = true;
		} else { // cancel autotune
			autoTune.cancel();
			tuning = false;
		}
	}

	private void doModel() {
		// cycle the dead time
		for (byte i = 0; i < 49; i++) {
			theta[i] = theta[i + 1];
		}
		// compute the input
		input = (kpmodel / taup) * (theta[0] - outputStart) + input * (1 - 1 / taup) + ((float) (-10 + (20 * Math.random()))) / 100.0;

	}
}
