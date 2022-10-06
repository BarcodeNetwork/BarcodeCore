package net.Indyuce.mmoitems.ability.list.location;

import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.ability.LocationAbility;
import net.Indyuce.mmoitems.ability.metadata.LocationAbilityMetadata;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Black_Hole extends LocationAbility {
	public Black_Hole() {
		super();

		addModifier("radius", 2);
		addModifier("duration", 2);
		addModifier("cooldown", 35);
		addModifier("mana", 0);
		addModifier("stamina", 0);
	}

	@Override
	public void whenCast(AttackMetadata attack, LocationAbilityMetadata ability) {
		Location loc = ability.getTarget();

		double duration = ability.getModifier("duration") * 20;
		double radius = ability.getModifier("radius");

		loc.getWorld().playSound(loc, VersionSound.ENTITY_ENDERMAN_TELEPORT.toSound(), 3, 1);
		new BukkitRunnable() {
			int ti = 0;
			final double r = 4;

			public void run() {
				if (ti++ > Math.min(300, duration))
					cancel();

				loc.getWorld().playSound(loc, VersionSound.BLOCK_NOTE_BLOCK_HAT.toSound(), 2, 2);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
				for (int j = 0; j < 3; j++) {
					double ran = random.nextDouble() * Math.PI * 2;
					double ran_y = random.nextDouble() * 2 - 1;
					double x = Math.cos(ran) * Math.sin(ran_y * Math.PI * 2);
					double z = Math.sin(ran) * Math.sin(ran_y * Math.PI * 2);
					Location loc1 = loc.clone().add(x * r, ran_y * r, z * r);
					Vector v = loc.toVector().subtract(loc1.toVector());
					loc1.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc1, 0, v.getX(), v.getY(), v.getZ(), .1);
				}

				for (Entity entity : MMOUtils.getNearbyChunkEntities(loc))
					if (entity.getLocation().distanceSquared(loc) < Math.pow(radius, 2) && MMOUtils.canTarget(attack.getPlayer(), entity))
						entity.setVelocity(MMOUtils.normalize(loc.clone().subtract(entity.getLocation()).toVector()).multiply(.5));
			}
		}.runTaskTimer(MMOItems.plugin, 0, 1);
	}
}
