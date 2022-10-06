package net.Indyuce.mmocore.comp;

import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.comp.target.TargetRestriction;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MMOCoreTargetRestriction implements TargetRestriction {

    @Override
    public boolean canTarget(Player player, LivingEntity target, InteractionType interaction) {

        if (interaction.isOffense() && target instanceof Player && PlayerData.has(target.getUniqueId())) {
            PlayerData targetData = PlayerData.get(target.getUniqueId());

//            // Check for the same party TODO: party restriction
//            if (targetData.hasParty() && targetData.getParty().getMembers().has(player))
//                return false;
        }

        return true;
    }
}
