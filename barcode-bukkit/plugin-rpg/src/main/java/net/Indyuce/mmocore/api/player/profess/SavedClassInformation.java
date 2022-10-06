package net.Indyuce.mmocore.api.player.profess;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.Indyuce.mmocore.MMOCore;
import org.bukkit.configuration.ConfigurationSection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttribute;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.manager.data.PlayerDataManager.DefaultPlayerData;

public class SavedClassInformation {
	private final int level, experience, skillPoints, attributePoints, attributeReallocationPoints;
	private final Map<String, Integer> attributes;
	private final Map<String, Integer> skills;

	public SavedClassInformation(ConfigurationSection config) {
		level = config.getInt("level");
		experience = config.getInt("experience");
		skillPoints = config.getInt("skill-points");
		attributePoints = config.getInt("attribute-points");
		attributeReallocationPoints = config.getInt("attribute-realloc-points");

		attributes = new HashMap<>();
		if (config.contains("attribute"))
			config.getConfigurationSection("attribute").getKeys(false).forEach(key -> attributes.put(key, config.getInt("attribute." + key)));
		skills = new HashMap<>();
		if (config.contains("skill"))
			config.getConfigurationSection("skill").getKeys(false).forEach(key -> skills.put(key, config.getInt("skill." + key)));
	}

	public SavedClassInformation(JsonObject json) {
		level = json.get("level").getAsInt();
		experience = json.get("experience").getAsInt();
		skillPoints = json.get("skill-points").getAsInt();
		attributePoints = json.get("attribute-points").getAsInt();
		attributeReallocationPoints = json.get("attribute-realloc-points").getAsInt();

		attributes = new HashMap<>();
		if (json.has("attribute"))
			for (Entry<String, JsonElement> entry : json.getAsJsonObject("attribute").entrySet())
				attributes.put(entry.getKey(), entry.getValue().getAsInt());
		skills = new HashMap<>();
		if (json.has("skill"))
			for (Entry<String, JsonElement> entry : json.getAsJsonObject("skill").entrySet())
				skills.put(entry.getKey(), entry.getValue().getAsInt());

	}

	public SavedClassInformation(PlayerData player) {
		this(player.getLevel(), player.getExperience(), player.getSkillPoints(), player.getAttributePoints(), player.getAttributeReallocationPoints(),
				player.getAttributes().mapPoints(), player.mapSkillLevels());
	}

	public SavedClassInformation(DefaultPlayerData data) {
		this(data.getLevel(), 0, data.getSkillPoints(), data.getAttributePoints(), data.getAttrReallocPoints());
	}

	public SavedClassInformation(int level, int experience, int skillPoints, int attributePoints, int attributeReallocationPoints) {
		this(level, experience, skillPoints, attributePoints, attributeReallocationPoints, new HashMap<>(), new HashMap<>());
	}

	private SavedClassInformation(int level, int experience, int skillPoints, int attributePoints, int attributeReallocationPoints,
			Map<String, Integer> attributes, Map<String, Integer> skills) {
		this.level = level;
		this.experience = experience;
		this.skillPoints = skillPoints;
		this.attributePoints = attributePoints;
		this.attributeReallocationPoints = attributeReallocationPoints;
		this.attributes = attributes;
		this.skills = skills;
	}

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return experience;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public int getAttributePoints() {
		return attributePoints;
	}

	public int getAttributeReallocationPoints() {
		return attributeReallocationPoints;
	}

	public Set<String> getSkillKeys() {
		return skills.keySet();
	}

	public int getSkillLevel(Skill skill) {
		return getSkillLevel(skill.getId());
	}

	public int getSkillLevel(String id) {
		return skills.get(id);
	}

	public void registerSkillLevel(Skill skill, int level) {
		registerSkillLevel(skill.getId(), level);
	}

	public void registerSkillLevel(String attribute, int level) {
		skills.put(attribute, level);
	}

	public Set<String> getAttributeKeys() {
		return attributes.keySet();
	}

	public int getAttributeLevel(PlayerAttribute attribute) {
		return getAttributeLevel(attribute.getId());
	}

	public int getAttributeLevel(String id) {
		return attributes.get(id);
	}

	public void registerAttributeLevel(PlayerAttribute attribute, int level) {
		registerAttributeLevel(attribute.getId(), level);
	}

	public void registerAttributeLevel(String attribute, int level) {
		attributes.put(attribute, level);
	}

	public void load(PlayerClass profess, PlayerData player) {

		/*
		 * saves current class info inside a SavedClassInformation, only if the
		 * class is a real class and not the default one.
		 */
		if (!player.getProfess().hasOption(ClassOption.DEFAULT) || MMOCore.plugin.configManager.saveDefaultClassInfo)
			player.applyClassInfo(player.getProfess(), new SavedClassInformation(player));

		/*
		 * resets information which much be reset after everything is saved.
		 */
		player.mapSkillLevels().forEach((skill, level) -> player.resetSkillLevel(skill));
		player.getAttributes().getInstances().forEach(ins -> ins.setBase(0));
		while (player.hasSkillBound(0))
			player.unbindSkill(0);

		/*
		 * reads this class info, applies it to the player. set class after
		 * changing level so the player stats can be calculated based on new
		 * level.
		 */
		player.setLevel(level);
		player.setExperience(experience);
		player.setSkillPoints(skillPoints);
		player.setAttributePoints(attributePoints);
		player.setAttributeReallocationPoints(attributeReallocationPoints);

		skills.forEach(player::setSkillLevel);
		attributes.forEach((id, pts) -> player.getAttributes().setBaseAttribute(id, pts));

		/*
		 * unload current class information and set the new profess once
		 * everything is changed
		 */
		player.setClass(profess);
		player.unloadClassInfo(profess);

		// Updates level on exp bar
		player.refreshVanillaExp();
	}
}
