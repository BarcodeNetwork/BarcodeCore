package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.PassiveSkill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Optional;

public class Fire_Berserker extends PassiveSkill {
    public Fire_Berserker() {
        super();
        setMaterial(Material.FLINT_AND_STEEL);
        setLore("You deal &c{extra}% &7more damage when on fire.");
        setPassive();

        addModifier("extra", new LinearValue(10, 5));
        // addModifier("duration", new LinearValue(10, .1));
        // addModifier("cooldown", new LinearValue(30, 0));

        Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void a(PlayerAttackEvent event) {
        PlayerData data = PlayerData.get(event.getData().getUniqueId());
        if (event.getPlayer().getFireTicks() > 0) {
            Optional<SkillInfo> skill = data.getProfess().findSkill(this);
            skill.ifPresent(skillInfo -> event.getAttack().getDamage().multiply(1 + skillInfo.getModifier("extra", data.getSkillLevel(this)) / 100));
        }
    }
}
