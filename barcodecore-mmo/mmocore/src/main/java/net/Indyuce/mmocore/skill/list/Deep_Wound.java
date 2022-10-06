package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.damage.DamageType;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.TargetSkillMetadata;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class Deep_Wound extends Skill {
    public Deep_Wound() {
        super();
        setMaterial(Material.REDSTONE);
        setLore("You puncture your target, dealing &c{damage} &7damage.", "Damage is increased up to &c+{extra}% &7based",
                "on your target's missing health.", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(20, -.1, 5, 20));
        addModifier("mana", new LinearValue(8, 3));
        addModifier("damage", new LinearValue(5, 1.5));
        addModifier("extra", new LinearValue(50, 20));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        TargetSkillMetadata cast = new TargetSkillMetadata(caster, skill, 3);
        if (!cast.isSuccessful())
            return cast;

        LivingEntity target = cast.getTarget();
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 2, 2);
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, target.getHeight() / 2, 0), 32, 0, 0, 0, .7);
        target.getWorld().spawnParticle(Particle.BLOCK_CRACK, target.getLocation().add(0, target.getHeight() / 2, 0), 32, 0, 0, 0, 2,
                Material.REDSTONE_BLOCK.createBlockData());

        double max = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double ratio = (max - target.getHealth()) / max;

        double damage = cast.getModifier("damage") * (1 + cast.getModifier("extra") * ratio / 100);
        caster.attack(target, damage, DamageType.SKILL, DamageType.PHYSICAL);
        return cast;
    }
}
