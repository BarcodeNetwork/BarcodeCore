package net.Indyuce.mmocore.api.util.math.formula;

import java.util.Random;

public class RandomAmount {
	private final double min, max;

	private static final Random random = new Random();

	public RandomAmount(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public RandomAmount(String value) {
		String[] split = value.split("-");
		min = Double.parseDouble(split[0]);
		max = split.length > 1 ? Double.parseDouble(split[1]) : 0;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public double calculate() {
		return max > 0 ? random.nextDouble() * Math.abs((max - min)) + Math.min(max, min) : min;
	}

	public int calculateInt() {
		return (int) (max > 0 ? (random.nextInt((int) (max - min + 1)) + min) : min);
	}
}
