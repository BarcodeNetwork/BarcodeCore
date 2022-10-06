package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.damage.DamageType;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.SmallParticleEffect;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.TargetSkillMetadata;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

public class Furtive_Strike extends Skill {
    public Furtive_Strike() {
        super();
        setMaterial(Material.COAL);
        setLore("Deals &c{damage} &7damage, increased by &c{extra}% &7if", "there are not any enemies within &c{radius} &7blocks.", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(20, -.1, 5, 20));
        addModifier("mana", new LinearValue(8, 3));
        addModifier("damage", new LinearValue(5, 1.5));
        addModifier("extra", new LinearValue(50, 20));
        addModifier("radius", new LinearValue(7, 0));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        TargetSkillMetadata cast = new TargetSkillMetadata(caster, skill, 3);
        if (!cast.isSuccessful())
            return cast;

        LivingEntity target = cast.getTarget();
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 1.5f);
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, target.getHeight() / 2, 0), 32, 0, 0, 0, .5);
        target.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation().add(0, target.getHeight() / 2, 0), 64, 0, 0, 0, .08);

        double damage = cast.getModifier("damage");

        double radius = cast.getModifier("radius");
        if (target.getNearbyEntities(radius, radius, radius).stream().allMatch(entity -> entity.equals(caster.getPlayer()))) {
            new SmallParticleEffect(target, Particle.SPELL_WITCH);
            damage *= 1 + cast.getModifier("extra") / 100;
        }

        caster.attack(target, damage, DamageType.SKILL, DamageType.PHYSICAL);
        return cast;
    }
}
