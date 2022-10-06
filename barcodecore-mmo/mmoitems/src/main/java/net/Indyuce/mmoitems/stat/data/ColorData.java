package net.Indyuce.mmoitems.stat.data;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;

import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;

public class ColorData implements StatData, RandomStatData {
	private final int red, green, blue;

	public ColorData(String string) {
		String[] split = string.split(" ");
		Validate.isTrue(split.length > 2, "Must specify 3 numbers for red, green and blue");

		red = Math.min(255, Math.max(0, Integer.parseInt(split[0])));
		green = Math.min(255, Math.max(0, Integer.parseInt(split[1])));
		blue = Math.min(255, Math.max(0, Integer.parseInt(split[2])));
	}

	public ColorData(Color color) {
		this(color.getRed(), color.getGreen(), color.getBlue());
	}

	public ColorData(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public Color getColor() {
		return Color.fromRGB(red, green, blue);
	}

	@Override
	public String toString() {
		return "{Red=" + red + ",Green=" + green + ",Blue=" + blue + "}";
	}

	@Override
	public StatData randomize(MMOItemBuilder builder) {
		return this;
	}
}
