package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.ParabolicProjectile;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.TargetSkillMetadata;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Fire_Storm extends Skill {
	public Fire_Storm() {
		super();
		setMaterial(Material.BLAZE_POWDER);
		setLore("Casts a flurry of 6 fire projectiles onto", "nearby enemies, proritizing the initial",
				"target. Each projectile deals &c{damage} &7damage", "and ignite the target for &c{ignite} &7seconds.", "",
				"&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

		addModifier("mana", new LinearValue(15, 2));
		addModifier("damage", new LinearValue(5, 3));
		addModifier("ignite", new LinearValue(2, .1));
		addModifier("cooldown", new LinearValue(5, -.1, 1, 5));
	}

	@Override
	public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
		TargetSkillMetadata cast = new TargetSkillMetadata(caster, skill, 20);
		if (!cast.isSuccessful()) return cast;

		LivingEntity target = cast.getTarget();

		final double damage = cast.getModifier("damage");
		final int ignite = (int) (20 * cast.getModifier("ignite"));

		caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), VersionSound.ENTITY_FIREWORK_ROCKET_BLAST.toSound(), 1, 1);
		new BukkitRunnable() {
			int j = 0;

			@Override
			public void run() {
				if (j++ > 5 || caster.getPlayer().isDead() || !caster.getPlayer().isOnline() || target.isDead() || !caster.getPlayer().getWorld()
						.equals(target.getWorld())) {
					cancel();
					return;
				}

				// TODO dynamic target location

				caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1, 1);
				new ParabolicProjectile(caster.getPlayer().getLocation().add(0, 1, 0), target.getLocation().add(0, target.getHeight() / 2, 0),
						randomVector(caster.getPlayer()), () -> {
					target.getWorld().playSound(target.getLocation(), VersionSound.ENTITY_FIREWORK_ROCKET_TWINKLE.toSound(), 1, 2);
					target.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation().add(0, target.getHeight() / 2, 0), 8, 0, 0, 0, .15);
					caster.attack(target, damage, DamageType.SKILL, DamageType.PROJECTILE, DamageType.MAGIC);
					target.setFireTicks(ignite);

				}, 2, Particle.FLAME);
			}
		}.runTaskTimer(MMOCore.plugin, 0, 4);
		return cast;
	}

	private Vector randomVector(Player player) {
		double a = Math.toRadians(player.getEyeLocation().getYaw() + 90);
		a += (random.nextBoolean() ? 1 : -1) * (random.nextDouble() * 2 + 1) * Math.PI / 6;
		return new Vector(Math.cos(a), .8, Math.sin(a)).normalize().multiply(.4);
	}
}
