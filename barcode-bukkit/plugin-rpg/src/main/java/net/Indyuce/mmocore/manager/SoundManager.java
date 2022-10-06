package net.Indyuce.mmocore.manager;

import net.Indyuce.mmocore.api.SoundObject;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
	private final Map<SoundEvent, SoundObject> sounds = new HashMap<>();

	public SoundManager(FileConfiguration config) {
		for(SoundEvent sound : SoundEvent.values())
			sounds.put(sound, new SoundObject(config.getString(sound.name().replace("_", "-").toLowerCase())));
	}


	public void play(Block block, SoundEvent event) {
		play(block, block.getLocation(), event);
	}

	public void play(Block block, Location loc, SoundEvent event) {
		SoundObject sound = sounds.get(event);
		if(sound.hasSound())
			block.getWorld().playSound(loc, sound.getSound(), sound.getVolume(), sound.getPitch());
		else block.getWorld().playSound(loc, sound.getKey(), sound.getVolume(), sound.getPitch());
	}

	public void play(Player player, SoundEvent event) {
		play(player, player.getLocation(), event);
	}

	public void play(Player player, Location loc, SoundEvent event) {
		SoundObject sound = sounds.get(event);
		if(sound.hasSound())
			player.playSound(loc, sound.getSound(), sound.getVolume(), sound.getPitch());
		else player.playSound(loc, sound.getKey(), sound.getVolume(), sound.getPitch());
	}

	public void play(Player player, SoundEvent event, float volume, float pitch) {
		play(player, player.getLocation(), event, volume, pitch);
	}

	public void play(Player player, Location loc, SoundEvent event, float volume, float pitch) {
		SoundObject sound = sounds.get(event);
		if(sound.hasSound())
			player.playSound(loc, sound.getSound(), volume, pitch);
		else player.playSound(loc, sound.getKey(), volume, pitch);
	}

	public enum SoundEvent {
		LEVEL_UP,
		HOTBAR_SWAP,
		SPELL_CAST_BEGIN,
		SPELL_CAST_END,
		CANT_SELECT_CLASS,
		SELECT_CLASS,
		LEVEL_ATTRIBUTE,
		RESET_ATTRIBUTES,
		NOT_ENOUGH_POINTS,
	}
}
