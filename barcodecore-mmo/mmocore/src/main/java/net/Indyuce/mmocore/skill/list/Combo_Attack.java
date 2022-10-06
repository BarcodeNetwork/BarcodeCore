package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.damage.DamageType;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.TargetSkillMetadata;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class Combo_Attack extends Skill {
    public Combo_Attack() {
        super();

        setMaterial(Material.IRON_SWORD);
        setLore("Violenty slashes your target &8{count}", "times for a total of &8{damage} &7damage.", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(20, -.1, 5, 20));
        addModifier("damage", new LinearValue(9, .3));
        addModifier("count", new LinearValue(3, .2));
        addModifier("mana", new LinearValue(10, -.1, 3, 5));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        TargetSkillMetadata cast = new TargetSkillMetadata(caster, skill, 3);
        if (!cast.isSuccessful())
            return cast;

        new BukkitRunnable() {
            final int count = (int) cast.getModifier("count");
            final double damage = cast.getModifier("damage") / count;
            final LivingEntity target = cast.getTarget();

            int c;

            @Override
            public void run() {
                if (c++ > count || caster.getPlayerData().isOnline()) {
                    cancel();
                    return;
                }

                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 2);
                target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, target.getHeight() / 2, 0), 24, 0, 0, 0, .7);
                caster.attack(target, damage, DamageType.SKILL, DamageType.PHYSICAL);
            }
        }.runTaskTimer(MMOCore.plugin, 0, 5);
        return cast;
    }
}
