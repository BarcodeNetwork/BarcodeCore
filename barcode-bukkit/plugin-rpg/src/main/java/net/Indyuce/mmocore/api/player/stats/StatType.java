package net.Indyuce.mmocore.api.player.stats;

import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmocore.api.ConfigFile;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;

public enum StatType {

    // Vanilla stats
    ATTACK_DAMAGE,
    ATTACK_SPEED,
    MAX_HEALTH,
    HEALTH_REGENERATION,
    MAX_HEALTH_REGENERATION,

    // Misc
    MOVEMENT_SPEED,
    SPEED_MALUS_REDUCTION,
    KNOCKBACK_RESISTANCE,

    // Mana
    MAX_MANA,
    MANA_REGENERATION,
    MAX_MANA_REGENERATION,

    // Stamina
    MAX_STAMINA,
    STAMINA_REGENERATION,
    MAX_STAMINA_REGENERATION,

    // Vanilla armor stats
    ARMOR,
    ARMOR_TOUGHNESS,

    // Critical strikes
    CRITICAL_STRIKE_CHANCE,
    CRITICAL_STRIKE_POWER,
    SKILL_CRITICAL_STRIKE_CHANCE,
    SKILL_CRITICAL_STRIKE_POWER,

    // Mitigation
    DEFENSE,
    BLOCK_POWER,
    BLOCK_RATING,
    BLOCK_COOLDOWN_REDUCTION,
    DODGE_RATING,
    DODGE_COOLDOWN_REDUCTION,
    PARRY_RATING,
    PARRY_COOLDOWN_REDUCTION,

    // Utility
    ADDITIONAL_EXPERIENCE,
    COOLDOWN_REDUCTION,

    // Damage-type based stats
    MAGIC_DAMAGE,
    PHYSICAL_DAMAGE,
    PROJECTILE_DAMAGE,
    WEAPON_DAMAGE,
    SKILL_DAMAGE,

    // Misc damage stats
    PVP_DAMAGE,
    PVE_DAMAGE,

    // Damage reduction stats
    DAMAGE_REDUCTION,
    MAGIC_DAMAGE_REDUCTION,
    PHYSICAL_DAMAGE_REDUCTION,
    PROJECTILE_DAMAGE_REDUCTION,
    WEAPON_DAMAGE_REDUCTION,
    SKILL_DAMAGE_REDUCTION;

    private LinearValue defaultInfo;
    private DecimalFormat format;

    StatType() {
        // Completely custom stat
    }

    public LinearValue getDefault() {
        return defaultInfo;
    }

    public String format(double value) {
        return format.format(value);
    }

    public static void load() {
        FileConfiguration config = new ConfigFile("stats").getConfig();
        for (StatType stat : values()) {
            stat.defaultInfo = config.contains("default." + stat.name()) ? new LinearValue(config.getConfigurationSection("default." + stat.name())) : new LinearValue(0, 0);
            stat.format = MythicLib.plugin.getMMOConfig().newFormat(config.contains("decimal-format." + stat.name()) ? config.getString("decimal-format." + stat.name()) : "0.#");
        }
    }
}
