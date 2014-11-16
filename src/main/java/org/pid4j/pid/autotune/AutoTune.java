package org.pid4j.pid.autotune;

import java.util.Arrays;

public class AutoTune {
	private boolean isMax, isMin;
	private Double setpoint;
	private Double noiseBand;
	private int controlType;
	private boolean running;
	private long peak1, peak2, lastTime;
	private int sampleTime;
	private int nLookBack;
	private int peakType;
	private Double[] lastInputs = new Double[101];
	private Double[] peaks = new Double[10];
	private int peakCount;
	private boolean justchanged;
	private boolean justevaled;
	private Double absMax, absMin;
	private Double oStep;
	private Double outputStart;
	private Double Ku, Pu;

	public AutoTune() {
		Arrays.fill(lastInputs, 0.0);
		Arrays.fill(peaks, 0.0);
		controlType = 0; // default to PI
		noiseBand = 0.5;
		running = false;
		oStep = 30.0;
		setLookbackSec(10);
		lastTime = System.currentTimeMillis();
	}

	public void cancel() {
		running = false;
	}

	public boolean runtime(Double input, Double output) {
		justevaled = false;
		if (peakCount > 9 && running) {
			running = false;
			finishUp(output);
			return true;
		}
		long now = System.currentTimeMillis();

		if ((now - lastTime) < sampleTime)
			return false;
		lastTime = now;
		Double refVal = input;
		justevaled = true;
		if (!running) { // initialize working variables the first time around
			peakType = 0;
			peakCount = 0;
			justchanged = false;
			absMax = refVal;
			absMin = refVal;
			setpoint = refVal;
			running = true;
			outputStart = output;
			output = outputStart + oStep;
		} else {
			if (refVal > absMax)
				absMax = refVal;
			if (refVal < absMin)
				absMin = refVal;
		}

		// oscillate the output base on the input's relation to the setpoint

		if (refVal > setpoint + noiseBand)
			output = outputStart - oStep;
		else if (refVal < setpoint - noiseBand)
			output = outputStart + oStep;

		System.out.println("input=" + input + " output=" + output);
		isMax = true;
		isMin = true;
		// id peaks
		for (int i = nLookBack - 1; i >= 0; i--) {
			Double val = lastInputs[i];
			if (isMax)
				isMax = refVal > val;
			if (isMin)
				isMin = refVal < val;
			lastInputs[i + 1] = lastInputs[i];
		}
		lastInputs[0] = refVal;
		if (nLookBack < 9) { // we don't want to trust the maxes or mins until
								// the inputs array has been filled
			return false;
		}

		if (isMax) {
			if (peakType == 0)
				peakType = 1;
			if (peakType == -1) {
				peakType = 1;
				justchanged = true;
				peak2 = peak1;
			}
			peak1 = now;
			peaks[peakCount] = refVal;

		} else if (isMin) {
			if (peakType == 0)
				peakType = -1;
			if (peakType == 1) {
				peakType = -1;
				peakCount++;
				justchanged = true;
			}

			if (peakCount < 10)
				peaks[peakCount] = refVal;
		}

		if (justchanged && peakCount > 2) { // we've transitioned. check if we
											// can autotune based on the last
											// peaks
			Double avgSeparation = (Math.abs(peaks[peakCount - 1]
					- peaks[peakCount - 2]) + Math.abs(peaks[peakCount - 2]
					- peaks[peakCount - 3])) / 2;
			if (avgSeparation < 0.05 * (absMax - absMin)) {
				finishUp(output);
				running = false;
				return true;

			}
		}
		justchanged = false;
		return false;
	}

	public void finishUp(Double output) {
		output = outputStart;
		// we can generate tuning parameters!
		Ku = 4 * (2 * oStep) / ((absMax - absMin) * 3.14159);
		Pu = (double) (peak1 - peak2) / 1000.0;
	}

	public Double getKp() {
		return controlType == 1 ? 0.6 * Ku : 0.4 * Ku;
	}

	public Double getKi() {
		return controlType == 1 ? 1.2 * Ku / Pu : 0.48 * Ku / Pu; // Ki = Kc/Ti
	}

	public Double getKd() {
		return controlType == 1 ? 0.075 * Ku * Pu : 0; // Kd = Kc * Td
	}

	public void setOutputStep(Double step) {
		oStep = step;
	}

	public Double getOutputStep() {
		return oStep;
	}

	public void setControlType(int type) {  // 0=PI, 1=PID
		controlType = type;
	}

	public int getControlType() {
		return controlType;
	}

	public void setNoiseBand(Double band) {
		noiseBand = band;
	}

	public Double getNoiseBand() {
		return noiseBand;
	}

	public void setLookbackSec(int value) {
		if (value < 1)
			value = 1;

		if (value < 25) {
			nLookBack = value * 4;
			sampleTime = 250;
		} else {
			nLookBack = 100;
			sampleTime = value * 10;
		}
	}

	public int getLookbackSec() {
		return nLookBack * sampleTime / 1000;
	}

}
