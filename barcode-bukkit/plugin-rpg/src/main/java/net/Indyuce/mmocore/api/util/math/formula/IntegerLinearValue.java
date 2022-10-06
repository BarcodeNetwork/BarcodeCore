package net.Indyuce.mmocore.api.util.math.formula;

import org.bukkit.configuration.ConfigurationSection;

public class IntegerLinearValue extends LinearValue {
	public IntegerLinearValue(double base, double perLevel) {
		super(base, perLevel);
	}

	public IntegerLinearValue(double base, double perLevel, double min, double max) {
		super(base, perLevel, min, max);
	}

	public IntegerLinearValue(IntegerLinearValue value) {
		super(value);
	}

	public IntegerLinearValue(ConfigurationSection config) {
		super(config);
	}
	
	@Override
	public String getDisplay(int level) {
		return "" + (int) calculate(level);
	}
}
