package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.comp.target.InteractionType;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.SmallParticleEffect;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.TargetSkillMetadata;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class Minor_Healings extends Skill {
    public Minor_Healings() {
        super();
        setMaterial(Material.GOLDEN_APPLE);
        setLore("Instantly grants &a{heal} &7HP to the", "target. Sneak to cast it on yourself.", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("mana", new LinearValue(4, 2));
        addModifier("heal", new LinearValue(4, 2));
        addModifier("cooldown", new LinearValue(9, -.1, 1, 5));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        SkillMetadata cast = caster.getPlayer().isSneaking() ? new SkillMetadata(caster, skill) : new TargetSkillMetadata(caster, skill, 50, InteractionType.SUPPORT_SKILL);
        if (!cast.isSuccessful())
            return cast;

        LivingEntity target = cast instanceof TargetSkillMetadata ? ((TargetSkillMetadata) cast).getTarget() : caster.getPlayer();

        double max = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        target.setHealth(Math.min(max, target.getHealth() + cast.getModifier("heal")));

        new SmallParticleEffect(target, Particle.HEART, 1);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
        return cast;
    }
}
