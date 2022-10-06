package io.lumine.mythic.lib.comp.target;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CitizensTargetRestriction implements TargetRestriction {
    //public static final boolean sentinels = Bukkit.getPluginManager().getPlugin("Sentinel") != null;

    @Override 
    public boolean canTarget(Player source, LivingEntity entity, InteractionType interaction) {
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);

        // Just fucking cancel everything or it will sent crap ass packet errors
        return npc == null || !npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true);
    }
}
