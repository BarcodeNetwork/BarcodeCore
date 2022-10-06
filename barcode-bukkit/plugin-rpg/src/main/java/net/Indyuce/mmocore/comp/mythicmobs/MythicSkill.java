package net.Indyuce.mmocore.comp.mythicmobs;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import io.lumine.mythic.core.skills.SkillTriggers;
import io.lumine.mythic.lib.api.util.EnumUtils;
import io.lumine.mythic.lib.player.cooldown.CooldownInfo;
import io.lumine.mythic.lib.skill.metadata.TriggerMetadata;
import io.lumine.mythic.lib.skill.trigger.PassiveSkill;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import io.lumine.mythic.lib.skill.trigger.TriggeredSkill;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.PlayerPostCastSkillEvent;
import net.Indyuce.mmocore.api.event.PlayerPreCastSkillEvent;
import net.Indyuce.mmocore.api.event.PlayerResourceUpdateEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.stats.StatType;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.api.util.math.formula.IntegerLinearValue;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.comp.anticheat.CheatType;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class MythicSkill extends Skill implements TriggeredSkill {
    private final io.lumine.mythic.api.skills.Skill skill;
    private final Map<CheatType, Integer> antiCheat = new HashMap<>();
    private final PassiveSkill mythicLibSkill;

    public MythicSkill(String id, FileConfiguration config) {
        super(id);

        String mmId = config.getString("mythicmobs-skill-id");
        Validate.notNull(mmId, "Could not find MM skill ID");

        Optional<io.lumine.mythic.api.skills.Skill> opt = MythicBukkit.inst().getSkillManager().getSkill(mmId);
        Validate.isTrue(opt.isPresent(), "Could not find MM skill " + mmId);
        skill = opt.get();

        String format = config.getString("material");
        Validate.notNull(format, "Could not load skill material");
        setIcon(MMOCoreUtils.readIcon(format));

        setName(config.getString("name"));
        setLore(config.getStringList("lore"));

        for (String key : config.getKeys(false)) {
            Object mod = config.get(key);
            if (mod instanceof ConfigurationSection)
                addModifier(key, readLinearValue((ConfigurationSection) mod));
        }

        if (config.isConfigurationSection("disable-anti-cheat"))
            for (String key : config.getKeys(false)) {
                Optional<CheatType> cheatType = EnumUtils.getIfPresent(CheatType.class, key.toUpperCase());
                if (cheatType.isPresent() && config.isInt("disable-anti-cheat." + key))
                    antiCheat.put(cheatType.get(), config.getInt("disable-anti-cheat." + key));
                else
                    MMOCore.log(Level.WARNING, "Invalid Anti-Cheat configuration for '" + id + "'!");
            }

        if (config.isString("passive-type")) {
            Optional<TriggerType> passiveType = EnumUtils.getIfPresent(TriggerType.class, config.getString("passive-type").toUpperCase());
            Validate.isTrue(passiveType.isPresent(), "Invalid passive skill type");
            setPassive();
            mythicLibSkill = new PassiveSkill("MMOCorePassiveSkill", passiveType.get(), this);
        } else
            mythicLibSkill = null;
    }

    public Map<CheatType, Integer> getAntiCheat() {
        return antiCheat;
    }

    public io.lumine.mythic.api.skills.Skill getSkill() {
        return skill;
    }

    public PassiveSkill toMythicLib() {
        return mythicLibSkill;
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        SkillMetadata cast = new SkillMetadata(caster, skill);
        if (isPassive() || !cast.isSuccessful())
            return cast;

        // Gather MythicMobs skill info
        HashSet<AbstractEntity> targetEntities = new HashSet<>();
        HashSet<AbstractLocation> targetLocations = new HashSet<>();

        AbstractEntity trigger = BukkitAdapter.adapt(caster.getPlayer());
        SkillCaster skillCaster = new GenericCaster(trigger);
        io.lumine.mythic.api.skills.SkillMetadata skillMeta = new SkillMetadataImpl(SkillTriggers.CAST, skillCaster, trigger, BukkitAdapter.adapt(caster.getPlayer().getLocation()), targetEntities, targetLocations, 1);

        // Disable anticheat
        if (MMOCore.plugin.hasAntiCheat())
            MMOCore.plugin.antiCheatSupport.disableAntiCheat(caster.getPlayer(), antiCheat);

        // Place cast skill info in a variable
        skillMeta.getVariables().putObject("MMOSkill", cast);
        skillMeta.getVariables().putObject("MMOStatMap", caster.getStats());

        // Yo is that me or the second argument is f***ing useless
        if (this.skill.isUsable(skillMeta, SkillTriggers.CAST))
            this.skill.execute(skillMeta);
        else
            cast.abort();

        return cast;
    }

    /**
     * Used to load double modifiers from the config with a specific type, since
     * modifiers have initially a type for mmocore default skills
     */
    private LinearValue readLinearValue(ConfigurationSection section) {
        return section.getBoolean("int") ? new IntegerLinearValue(section) : new LinearValue(section);
    }

    @Override
    public void execute(TriggerMetadata triggerMeta) {
        PlayerData playerData = PlayerData.get(triggerMeta.getAttack().getPlayer().getUniqueId());
        if (!playerData.getProfess().hasSkill(this))
            return;

        // Check for Bukkit pre cast event
        SkillInfo skill = playerData.getProfess().getSkill(this);
        PlayerPreCastSkillEvent preEvent = new PlayerPreCastSkillEvent(playerData, skill);
        Bukkit.getPluginManager().callEvent(preEvent);
        if (preEvent.isCancelled())
            return;

        // Gather MMOCore skill info
        CasterMetadata caster = new CasterMetadata(playerData);
        SkillMetadata cast = new SkillMetadata(caster, skill);
        if (!cast.isSuccessful())
            return;

        // Gather MythicMobs skill info
        HashSet<AbstractEntity> targetEntities = new HashSet<>();
        HashSet<AbstractLocation> targetLocations = new HashSet<>();

        // The only difference
        if (triggerMeta.getTarget() != null)
            targetEntities.add(BukkitAdapter.adapt(triggerMeta.getTarget()));

        AbstractEntity trigger = BukkitAdapter.adapt(caster.getPlayer());
        SkillCaster skillCaster = new GenericCaster(trigger);
        io.lumine.mythic.api.skills.SkillMetadata skillMeta = new SkillMetadataImpl(SkillTriggers.API, skillCaster, trigger, BukkitAdapter.adapt(caster.getPlayer().getEyeLocation()), targetEntities, targetLocations, 1);

        // Check if the MythicMobs skill can be cast
        if (!this.skill.isUsable(skillMeta, SkillTriggers.CAST)) {
            cast.abort();
            return;
        }

        // Disable anticheat
        if (MMOCore.plugin.hasAntiCheat())
            MMOCore.plugin.antiCheatSupport.disableAntiCheat(caster.getPlayer(), antiCheat);

        // Place cast skill info in a variable
        skillMeta.getVariables().putObject("MMOSkill", cast);
        skillMeta.getVariables().putObject("MMOStatMap", caster.getStats());

        // Apply cooldown, mana and stamina costs
        if (!playerData.noCooldown) {

            // Cooldown
            double flatCooldownReduction = Math.max(0, Math.min(1, playerData.getStats().getStat(StatType.COOLDOWN_REDUCTION) / 100));
            CooldownInfo cooldownHandler = playerData.getCooldownMap().applyCooldown(cast.getSkill(), cast.getCooldown());
            cooldownHandler.reduceInitialCooldown(flatCooldownReduction);

            // Mana and stamina cost
            playerData.giveMana(-cast.getManaCost(), PlayerResourceUpdateEvent.UpdateReason.SKILL_COST);
            playerData.giveStamina(-cast.getStaminaCost(), PlayerResourceUpdateEvent.UpdateReason.SKILL_COST);
        }

        // Execute the MythicMobs skill
        this.skill.execute(skillMeta);

        Bukkit.getPluginManager().callEvent(new PlayerPostCastSkillEvent(playerData, skill, cast));
    }
}
