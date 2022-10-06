package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.ParabolicProjectile;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;

public class Warp extends Skill {
    public Warp() {
        super();
        setMaterial(Material.ENDER_PEARL);
        setLore("Teleports you to target location.", "Max. Range: &5{range}", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(15, -.3, 2, 15));
        addModifier("mana", new LinearValue(8, 3));
        addModifier("range", new LinearValue(16, 1, 0, 100));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        WarpSkillMetadata cast = new WarpSkillMetadata(caster, skill);
        if (!cast.isSuccessful())
            return cast;

        caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 2);

        Location loc = cast.block.getLocation().add(0, 1, 0);
        loc.setYaw(caster.getPlayer().getLocation().getYaw());
        loc.setPitch(caster.getPlayer().getLocation().getPitch());

        new ParabolicProjectile(caster.getPlayer().getLocation().add(0, 1, 0), loc.clone().add(0, 1, 0), () -> {
            if (caster.getPlayer().isOnline() && !caster.getPlayer().isDead()) {
                caster.getPlayer().teleport(loc);
                caster.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, caster.getPlayer().getLocation().add(0, 1, 0), 0);
                caster.getPlayer().getWorld().spawnParticle(Particle.SPELL_INSTANT, caster.getPlayer().getLocation().add(0, 1, 0), 32, 0, 0, 0, .1);
                caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), VersionSound.ENTITY_ENDERMAN_TELEPORT.toSound(), 1, 1);
            }
        }, 2, Particle.SPELL_INSTANT);
        return cast;
    }

    private class WarpSkillMetadata extends SkillMetadata {
        private Block block;

        public WarpSkillMetadata(CasterMetadata caster, SkillInfo skill) {
            super(caster, skill);

            if (isSuccessful() && (block = caster.getPlayer().getTargetBlock(null, (int) getModifier("range"))) == null)
                abort();
        }
    }
}
