package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.MMORayTraceResult;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.version.VersionMaterial;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Fireball extends Skill {
    public Fireball() {
        super();
        setMaterial(VersionMaterial.FIRE_CHARGE.toMaterial());
        setLore("Casts a deadly fireball onto your", "target, dealing &c{damage} &7damage upon contact", "and igniting it for &c{ignite} &7seconds.", "", "Shatters into 3 blazing hot shards which stick", "to walls and explode 3 seconds later, dealing", "&c{ratio}% &7of the initial spell damage.", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("mana", new LinearValue(15, 1));
        addModifier("damage", new LinearValue(5, 3));
        addModifier("ignite", new LinearValue(2, .1));
        addModifier("ratio", new LinearValue(50, 3));
        addModifier("cooldown", new LinearValue(9, -.1, 1, 5));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        SkillMetadata cast = new SkillMetadata(caster, skill);
        if (!cast.isSuccessful())
            return cast;

        caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), VersionSound.ENTITY_FIREWORK_ROCKET_BLAST.toSound(), 1, 1);
        new BukkitRunnable() {
            int j = 0;
            final Vector vec = caster.getPlayer().getEyeLocation().getDirection();
            final Location loc = caster.getPlayer().getLocation().add(0, 1.3, 0);

            public void run() {
                if (j++ > 40) {
                    cancel();
                    return;
                }

                loc.add(vec);

                if (j % 3 == 0)
                    loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 2, 1);
                loc.getWorld().spawnParticle(Particle.FLAME, loc, 4, .02, .02, .02, 0);
                loc.getWorld().spawnParticle(Particle.LAVA, loc, 0);

                for (Entity target : MMOCoreUtils.getNearbyChunkEntities(loc))
                    if (MythicLib.plugin.getVersion().getWrapper().isInBoundingBox(target, loc) && MMOCoreUtils.canTarget(caster.getPlayerData(), target)) {
                        loc.getWorld().spawnParticle(Particle.LAVA, loc, 8);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc, 32, 0, 0, 0, .1);
                        loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_HURT, 2, 1);
                        target.setFireTicks((int) (target.getFireTicks() + cast.getModifier("ignite") * 20));
                        double damage = cast.getModifier("damage");
                        caster.attack((LivingEntity) target, damage, DamageType.SKILL, DamageType.PROJECTILE, DamageType.MAGIC);

                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                if (i++ > 2) {
                                    cancel();
                                    return;
                                }

                                double range = 2.5 * (1 + random.nextDouble());
                                Vector dir = randomDirection();
                                loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_HURT, 2, 1.5f);

                                MMORayTraceResult result = MythicLib.plugin.getVersion().getWrapper().rayTrace(loc, dir, range, entity -> MMOCoreUtils.canTarget(caster.getPlayerData(), entity));
                                if (result.hasHit())
                                    caster.attack(result.getHit(), damage, DamageType.SKILL, DamageType.PROJECTILE, DamageType.MAGIC);
                                result.draw(loc.clone(), dir, 8, tick -> tick.getWorld().spawnParticle(Particle.FLAME, tick, 0));

                            }
                        }.runTaskTimer(MMOCore.plugin, 3, 3);

                        cancel();
                        return;
                    }
            }
        }.runTaskTimer(MMOCore.plugin, 0, 1);
        return cast;
    }

    private Vector randomDirection() {
        double x = random.nextDouble() - .5, y = (random.nextDouble() - .2) / 2, z = random.nextDouble() - .5;
        Vector dir = new Vector(x, y, z);
        return dir.lengthSquared() == 0 ? new Vector(1, 0, 0) : dir.normalize();
    }
}
