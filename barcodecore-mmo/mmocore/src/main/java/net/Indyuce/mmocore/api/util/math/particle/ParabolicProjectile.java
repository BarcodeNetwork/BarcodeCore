package net.Indyuce.mmocore.api.util.math.particle;

import java.util.function.Consumer;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.mmocore.MMOCore;

public class ParabolicProjectile extends BukkitRunnable {
	private final Location target;
	private final Consumer<Location> display;
	private final Vector vec;
	private final Runnable end;
	private final int speed;

	// calculation
	private final Location loc;
	private int j;

	// private static final Random random = new Random();

	public ParabolicProjectile(Location source, Location target, Color color) {
		this(source, target, Particle.REDSTONE, () -> {
		}, 1, color, 1);
	}

	public ParabolicProjectile(Location source, Location target, Particle particle, Runnable end, int speed, Color color, float size) {
		this(source, target, target.clone().subtract(source).toVector().multiply(.1).setY(6).normalize().multiply(.3), end, speed,
				(loc) -> loc.getWorld().spawnParticle(particle, loc, 1, new Particle.DustOptions(color, size)));
	}

	public ParabolicProjectile(Location source, Location target, Runnable end, Color color) {
		this(source, target, target.clone().subtract(source).toVector().multiply(.1).setY(6).normalize().multiply(.3), end, 1,
				(loc) -> loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(color, 1f)));
	}

	public ParabolicProjectile(Location source, Location target, Particle particle) {
		this(source, target, target.clone().subtract(source).toVector().multiply(.1).setY(6).normalize().multiply(.3), () -> {
		}, 1, (loc) -> loc.getWorld().spawnParticle(particle, loc, 0));
	}

	public ParabolicProjectile(Location source, Location target, Runnable end, int speed, Particle particle) {
		this(source, target, target.clone().subtract(source).toVector().multiply(.1).setY(6).normalize().multiply(.3), end, speed,
				(loc) -> loc.getWorld().spawnParticle(particle, loc, 0));
	}

	public ParabolicProjectile(Location source, Location target, Vector vec, Runnable end, int speed, Particle particle) {
		this(source, target, vec, end, speed, (loc) -> loc.getWorld().spawnParticle(particle, loc, 0));
	}

	private ParabolicProjectile(Location source, Location target, Vector vec, Runnable end, int speed, Consumer<Location> display) {
		loc = source.clone();
		this.target = target;
		this.display = display;
		this.end = end;
		this.vec = vec;
		this.speed = Math.max(1, speed);

		runTaskTimer(MMOCore.plugin, 0, 1);
	}

	@Override
	public void run() {
		for (int k = 0; k < speed; k++) {
			if (j++ > 100 || loc.distanceSquared(target) < .8) {
				end.run();
				cancel();
			}

			double c = Math.min(1, (double) j / 40);
			display.accept(loc.add(target.clone().subtract(loc).toVector().normalize().multiply(c).add(vec.clone().multiply(1 - c))));
		}
	}
}
