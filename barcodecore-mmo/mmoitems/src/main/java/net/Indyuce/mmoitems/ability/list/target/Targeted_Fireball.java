package net.Indyuce.mmoitems.ability.list.target;

import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.ability.TargetAbility;
import net.Indyuce.mmoitems.ability.metadata.TargetAbilityMetadata;
import io.lumine.mythic.lib.damage.AttackMetadata;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Targeted_Fireball extends TargetAbility {
    public Targeted_Fireball() {
        super();

        addModifier("cooldown", 10);
        addModifier("mana", 0);
        addModifier("stamina", 0);
        addModifier("ignite", 4);
        addModifier("damage", 4);
    }

    @Override
    public void whenCast(AttackMetadata attack, TargetAbilityMetadata ability) {
        LivingEntity target = ability.getTarget();

        new BukkitRunnable() {
            final Location loc = attack.getPlayer().getLocation().add(0, 1.3, 0);
            int j = 0;

            public void run() {
                j++;
                if (target.isDead() || !target.getWorld().equals(loc.getWorld()) || j > 200) {
                    cancel();
                    return;
                }

                Vector dir = target.getLocation().add(0, target.getHeight() / 2, 0).subtract(loc).toVector().normalize();
                loc.add(dir.multiply(.6));

                loc.setDirection(dir);
                for (double a = 0; a < Math.PI * 2; a += Math.PI / 6) {
                    Vector rotated = MMOUtils.rotateFunc(new Vector(Math.cos(a), Math.sin(a), 0), loc);
                    loc.getWorld().spawnParticle(Particle.FLAME, loc, 0, rotated.getX(), rotated.getY(), rotated.getZ(), .06);
                }

				loc.getWorld().playSound(loc, VersionSound.BLOCK_NOTE_BLOCK_HAT.toSound(), 1, 1);
				if (target.getLocation().add(0, target.getHeight() / 2, 0).distanceSquared(loc) < 1.3) {
                    loc.getWorld().spawnParticle(Particle.LAVA, loc, 8);
                    loc.getWorld().spawnParticle(Particle.FLAME, loc, 32, 0, 0, 0, .1);
                    loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_HURT, 2, 1);
                    target.setFireTicks((int) (target.getFireTicks() + ability.getModifier("ignite") * 20));
                    new AttackMetadata(new DamageMetadata(ability.getModifier("damage"), DamageType.SKILL, DamageType.MAGIC, DamageType.PROJECTILE), attack.getStats()).damage(target);
                    cancel();
                }
			}
		}.runTaskTimer(MMOItems.plugin, 0, 1);
	}
}