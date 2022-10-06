package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.comp.target.TargetRestriction;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class EntityManager {
    public final Set<TargetRestriction> restrictions = new HashSet<>();

    /**
     * See {@link TargetRestriction} for more information. This should be
     * called as soon as MythicLib enablesby plugins implementing player sets
     * like parties, friends, factions.... any set that could support friendly fire.
     * <p>
     * This is also helpful to prevent players from interacting with
     * specific invulnerable entities like NPCs
     *
     * @param restriction New restriction for entities
     */
    public void registerRestriction(TargetRestriction restriction) {
        restrictions.add(restriction);
    }

    /**
     * Called whenever a player tries to damage OR buff an entity.
     * <p>
     * This should be used by:
     * - plugins which implement friendly fire player sets like parties, guilds, nations, factions....
     * - plugins which implement custom invulnerable entities like NPCs, sentinels....
     *
     * @param source      Player targeted another entity
     * @param target      Entity being targeted
     * @param interaction Type of interaction, whether it's positive (buff, heal) or negative (offense skill, attack)
     * @return If false, any interaction should be cancelled!
     */
    public boolean canTarget(@NotNull Player source, @NotNull Entity target, @NotNull InteractionType interaction) {

        // Simple checks
        if (source.equals(target) || target.isDead() || !(target instanceof LivingEntity) || target instanceof ArmorStand)
            return false;

        // PvP flag
        if (interaction.isOffense() && (target instanceof Player) && !MythicLib.plugin.getFlags().isPvpAllowed(target.getLocation()))
            return false;


        // Player ability damage for PvE servers
        if (interaction == InteractionType.OFFENSE_SKILL && !MythicLib.plugin.getMMOConfig().playerAbilityDamage)
            return false;

        LivingEntity livingEntity = (LivingEntity) target;
        for (TargetRestriction restriction : restrictions)
            if (!restriction.canTarget(source, livingEntity, interaction))
                return false;

        return true;
    }
}
