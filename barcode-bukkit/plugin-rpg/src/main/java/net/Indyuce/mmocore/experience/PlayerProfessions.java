package net.Indyuce.mmocore.experience;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.ConfigMessage;
import net.Indyuce.mmocore.api.event.PlayerExperienceGainEvent;
import net.Indyuce.mmocore.api.event.PlayerLevelUpEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.api.util.math.particle.SmallParticleEffect;
import net.Indyuce.mmocore.manager.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PlayerProfessions {
	private final Map<String, Integer> exp = new HashMap<>();
	private final Map<String, Integer> level = new HashMap<>();
	private final PlayerData playerData;

	public PlayerProfessions(PlayerData playerData) {
		this.playerData = playerData;
	}

	public PlayerProfessions load(ConfigurationSection config) {
		for (String key : config.getKeys(false))
			if (MMOCore.plugin.professionManager.has(key)) {
				exp.put(key, config.getInt(key + ".exp"));
				level.put(key, config.getInt(key + ".level"));
			}

		return this;
	}

	public void save(ConfigurationSection config) {
		for (String id : exp.keySet())
			config.set(id + ".exp", exp.get(id));
		for (String id : level.keySet())
			config.set(id + ".level", level.get(id));
	}

	public String toJsonString() {
		JsonObject json = new JsonObject();
		for (Profession profession : MMOCore.plugin.professionManager.getAll()) {
			JsonObject object = new JsonObject();
			object.addProperty("exp", getExperience(profession));
			object.addProperty("level", getLevel(profession));

			json.add(profession.getId(), object);
		}
		return json.toString();
	}

	public void load(String json) {
		Gson parser = new Gson();
		JsonObject jo = parser.fromJson(json, JsonObject.class);
		for (Entry<String, JsonElement> entry : jo.entrySet()) {
			if (MMOCore.plugin.professionManager.has(entry.getKey())) {
				exp.put(entry.getKey(), entry.getValue().getAsJsonObject().get("exp").getAsInt());
				level.put(entry.getKey(), entry.getValue().getAsJsonObject().get("level").getAsInt());
			}
		}
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public int getLevel(String profession) {
		return Math.max(1, level.getOrDefault(profession, 1));
	}

	public int getLevel(Profession profession) {
		return getLevel(profession.getId());
	}

	public int getExperience(String id) {
		return exp.getOrDefault(id, 0);
	}

	public int getExperience(Profession profession) {
		return getExperience(profession.getId());
	}

	public int getLevelUpExperience(Profession profession) {
		return profession.getExpCurve().getExperience(getLevel(profession) + 1);
	}

	public int getLevelUpExperience(String id) {
		return MMOCore.plugin.professionManager.has(id) ? MMOCore.plugin.professionManager.get(id).getExpCurve().getExperience(getLevel(id) + 1) : 0;
	}

	public void setLevel(Profession profession, int value) {
		level.put(profession.getId(), value);
	}

	public void takeLevels(Profession profession, int value) {
		int current = level.getOrDefault(profession.getId(), 1);
		level.put(profession.getId(), Math.max(1, current - value));
	}

	public void setExperience(Profession profession, int value) {
		exp.put(profession.getId(), value);
	}

	public void giveLevels(Profession profession, int value, EXPSource source) {
		int total = 0, level = getLevel(profession);
		while (value-- > 0)
			total += profession.getExpCurve().getExperience(level + value + 1);
		giveExperience(profession, total, source);
	}

	public void giveExperience(Profession profession, int value, EXPSource source) {
		giveExperience(profession, value, source, null);
	}

	public boolean hasReachedMaxLevel(Profession profession) {
		return profession.hasMaxLevel() && getLevel(profession) >= profession.getMaxLevel();
	}

	public void giveExperience(Profession profession, double value, EXPSource source, @Nullable Location hologramLocation) {
		if (hasReachedMaxLevel(profession)) {
			setExperience(profession, 0);
			return;
		}

		value = MMOCore.plugin.boosterManager.calculateExp(profession, value);

		// display hologram
		if (hologramLocation != null && playerData.isOnline())
			MMOCoreUtils.displayIndicator(hologramLocation.add(.5, 1.5, .5), MMOCore.plugin.configManager.getSimpleMessage("exp-hologram", "exp", "" + value).message());

		PlayerExperienceGainEvent event = new PlayerExperienceGainEvent(playerData, profession, (int) value, source);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

		exp.put(profession.getId(), exp.containsKey(profession.getId()) ? exp.get(profession.getId()) + event.getExperience() : event.getExperience());
		int needed, exp, level, oldLevel = getLevel(profession);

		/*
		 * loop for exp overload when leveling up, will continue looping until
		 * exp is 0 or max level has been reached
		 */
		boolean check = false;
		while ((exp = this.exp.get(profession.getId())) >= (needed = profession.getExpCurve().getExperience((level = getLevel(profession)) + 1))) {
			if (hasReachedMaxLevel(profession)) {
				setExperience(profession, 0);
				check = true;
				break;
			}

			this.exp.put(profession.getId(), exp - needed);
			this.level.put(profession.getId(), level + 1);
			check = true;
			playerData.giveExperience((int) profession.getExperience().calculate(level), null);
		}

		if (check && playerData.isOnline()) {
			Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(playerData, profession, oldLevel, level));
			new SmallParticleEffect(playerData.getPlayer(), Particle.SPELL_INSTANT);
			new ConfigMessage("profession-level-up").addPlaceholders("level", "" + level, "profession", profession.getName())
					.send(playerData.getPlayer());
			MMOCore.plugin.soundManager.play(playerData.getPlayer(), SoundManager.SoundEvent.LEVEL_UP);
			playerData.getStats().updateStats();
		}

		StringBuilder bar = new StringBuilder("" + ChatColor.BOLD);
		int chars = (int) ((double) exp / needed * 20);
		for (int j = 0; j < 20; j++)
			bar.append(j == chars ? "" + ChatColor.WHITE + ChatColor.BOLD : "").append("|");
		if (playerData.isOnline())
			MMOCore.plugin.configManager.getSimpleMessage("exp-notification", "profession", profession.getName(), "progress", bar.toString(), "ratio",
					MythicLib.plugin.getMMOConfig().decimal.format((double) exp / needed * 100)).send(playerData.getPlayer());
	}
}
