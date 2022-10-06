package net.Indyuce.mmocore.api;

import org.bukkit.Sound;

public class SoundObject {
	private final Sound sound;
	private final String key;
	private final float volume;
	private final float pitch;
	
	public SoundObject(String input) {
		String[] split = input.split(",");
		if(split.length > 2) {
			input = split[0];
			volume = Float.parseFloat(split[1]);
			pitch = Float.parseFloat(split[2]);
		} else {
			volume = 1;
			pitch = 1;
		}

		Sound sound = null;
		String key = "";
		try {
			sound = Sound.valueOf(input.toUpperCase().replace("-", "_"));
		} catch(Exception ignored) {
			key = input;
		}

		this.sound = sound;
		this.key = key;
	}
	
	public boolean hasSound() {
		return key.isEmpty();
	}
	
	public Sound getSound() {
		return sound;
	}

	public String getKey() {
		return key;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
	}
}
