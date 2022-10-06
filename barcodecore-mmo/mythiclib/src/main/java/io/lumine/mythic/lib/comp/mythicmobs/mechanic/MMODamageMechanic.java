package io.lumine.mythic.lib.comp.mythicmobs.mechanic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.core.skills.damage.DamagingMechanic;
import io.lumine.mythic.core.utils.annotations.MythicMechanic;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

@MythicMechanic(
        author = "Indyuce",
        name = "mmodamage",
        aliases = {"mmod"},
        description = "Deals damage to the target (compatible with MMO)"
)
public class MMODamageMechanic extends DamagingMechanic implements ITargetedEntitySkill {
    protected final PlaceholderDouble amount;
    protected final DamageType[] types;

    public MMODamageMechanic(String line, MythicLineConfig config) {
        super(MythicBukkit.inst().getSkillManager(), line, config);

        this.amount = PlaceholderDouble.of(config.getString(new String[]{"amount", "a"}, "1", new String[0]));
        String[] typesFormat = config.getString(new String[]{"type", "t"}, "SKILL,MAGIC", new String[0]).split("\\,");
        this.types = Arrays.asList(typesFormat).stream().map(str -> DamageType.valueOf(str.toUpperCase())).collect(Collectors.toList()).toArray(new DamageType[0]);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {

        if (!target.isDead() && target.getBukkitEntity() instanceof LivingEntity && !data.getCaster().isUsingDamageSkill() && (!target.isLiving() || !(target.getHealth() <= 0.0D))) {

            double damage = amount.get(data, target) * data.getPower();

            StatMap.CachedStatMap statMap = data.getVariables().has("MMOStatMap") ? (StatMap.CachedStatMap) data.getVariables().get("MMOStatMap").get()
                    : MMOPlayerData.get(data.getCaster().getEntity().getUniqueId()).getStatMap().cache(EquipmentSlot.MAIN_HAND);
            AttackMetadata attackMeta = new AttackMetadata(new DamageMetadata(damage, types), statMap);

            // Cooler damage types yeah
            MythicLib.plugin.getDamage().damage(attackMeta, (LivingEntity) target.getBukkitEntity(), !this.preventKnockback, this.preventImmunity);

            MythicLogger.debug(MythicLogger.DebugLevel.MECHANIC, "+ MMODamageMechanic fired for {0} with {1} power", new Object[]{damage, data.getPower()});
            return SkillResult.SUCCESS;
        } else {
            return SkillResult.INVALID_TARGET;
        }
    }
}
