package net.Indyuce.mmocore.skill;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CasterMetadata {
    private final Player player;
    private final PlayerData caster;
    private final StatMap.CachedStatMap stats;

    /**
     * Instantiated every time a player casts a skill. This is
     * used to temporarily cache the player's statistics
     *
     * @param caster Player casting the skill
     */
    public CasterMetadata(PlayerData caster) {
        this.player = caster.getPlayer();
        this.caster = caster;
        this.stats = caster.getMMOPlayerData().getStatMap().cache(EquipmentSlot.MAIN_HAND);
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerData getPlayerData() {
        return caster;
    }

    public StatMap.CachedStatMap getStats() {
        return stats;
    }

    /**
     * Utility method that makes a player deal damage to a specific
     * entity. This creates the attackMetadata based on the data
     * stored by the CasterMetadata, and calls it using MythicLib
     * damage manager
     *
     * @param target Target entity
     * @param damage Damage dealt
     * @param types  Type of target
     * @return
     */
    public void attack(LivingEntity target, double damage, DamageType... types) {
        AttackMetadata attackMeta = new AttackMetadata(new DamageMetadata(damage, types), stats);
        MythicLib.plugin.getDamage().damage(attackMeta, target);
    }
}
