package net.Indyuce.mmocore.skill;

import java.util.HashMap;
import java.util.Map;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.skill.Skill.SkillInfo;

/**
 * Note: any method which return longs returns milliseconds.
 * 
 * @author cympe
 */
public class PlayerSkillData {
	private final Map<String, Long> cooldowns = new HashMap<>();
	private final PlayerData data;

	public PlayerSkillData(PlayerData data) {
		this.data = data;
	}

	public long getCooldown(SkillInfo skill) {
		return Math.max(0, lastCast(skill.getSkill()) - System.currentTimeMillis()
				+ (long) (1000. * skill.getModifier("cooldown", data.getSkillLevel(skill.getSkill()))));
	}

	/**
	 * @param skill
	 *            Skill that was cast
	 * @return Last time stamp the skill was cast or 0 if never
	 */
	public long lastCast(Skill skill) {
		return cooldowns.containsKey(skill.getId()) ? cooldowns.get(skill.getId()) : 0;
	}

	/**
	 * Sets the last time the player cast the skill at current time
	 * 
	 * @param skill
	 *            Skill being cast
	 */
	public void setLastCast(Skill skill) {
		setLastCast(skill, System.currentTimeMillis());
	}

	/**
	 * Sets the last time the player cast the skill at given time
	 * 
	 * @param ms
	 *            Time stammp
	 * @param skill
	 *            Skill being cast
	 */
	public void setLastCast(Skill skill, long ms) {
		cooldowns.put(skill.getId(), ms);
	}

	/**
	 * Reduces the remaining cooldown of a specific skill
	 * 
	 * @param skill
	 *            Skill cast
	 * @param value
	 *            Amount of skill cooldown instant reduction.
	 * @param relative
	 *            If the cooldown reduction is relative to the remaining
	 *            cooldown. If set to true, instant reduction is equal to
	 *            (value) * (skill cooldown). If set to false, instant reduction
	 *            is the given flat value
	 */
	public void reduceCooldown(SkillInfo skill, double value, boolean relative) {
		long reduction = (long) (relative ? value * (double) getCooldown(skill) : value * 1000.);
		cooldowns.put(skill.getSkill().getId(), lastCast(skill.getSkill()) + reduction);
	}
}
