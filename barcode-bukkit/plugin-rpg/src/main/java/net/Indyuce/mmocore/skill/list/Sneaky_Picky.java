package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.damage.DamageType;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.PassiveSkill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;

public class Sneaky_Picky extends PassiveSkill {
    public Sneaky_Picky() {
        super();
        setMaterial(Material.DIAMOND_SWORD);
        setLore("Your attack is empowered by &f{extra}% &7when", "delivering the first blow during a fight.", "", "&9Costs {mana} {mana_name}");
        setPassive();

        addModifier("cooldown", new LinearValue(0, 0));
        addModifier("mana", new LinearValue(8, 1));
        addModifier("extra", new LinearValue(50, 20));

        Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
    }

    @EventHandler
    public void a(PlayerAttackEvent event) {
        PlayerData data = PlayerData.get(event.getData().getUniqueId());
        if (!event.getAttack().getDamage().hasType(DamageType.WEAPON) || data.isInCombat() || !data.getProfess().hasSkill(this))
            return;

        SkillMetadata cast = data.cast(this);
        if (!cast.isSuccessful())
            return;

        data.cast(cast.getInfo());

        event.getAttack().getDamage().multiply(1 + cast.getModifier("extra") / 100, DamageType.WEAPON);
        LivingEntity target = event.getEntity();
        target.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation().add(0, target.getHeight() / 2, 0), 64, 0, 0, 0, .05);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 2);
    }
}
