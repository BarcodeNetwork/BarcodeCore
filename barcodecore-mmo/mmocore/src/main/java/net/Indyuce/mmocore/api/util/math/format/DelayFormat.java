package net.Indyuce.mmocore.api.util.math.format;

public class DelayFormat {
	private int display;

	private static final long[] millisArray = {31557081600L, 2629756800L, 86400000L, 3600000L, 60000L, 1000L};
	private static final String[] charArray = { "y", "M", "d", "h", "m", "s" };

	public DelayFormat() {
		this(charArray.length);
	}

	public DelayFormat(int display) {
		this.display = Math.min(display, charArray.length);
	}

	public String format(long ms) {
		StringBuilder format = new StringBuilder();

		for (int j = 0; j < charArray.length && display > 0; j++)
			if (ms > millisArray[j]) {
				format.append(ms / millisArray[j]).append(charArray[j]).append(" ");
				ms = ms % millisArray[j];
				display--;
			}

		return format.toString().equals("") ? "Now!" : format.substring(0, format.length() - 1);
	}
}
