package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.version.VersionMaterial;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.api.util.math.VectorRotation;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Fire_Rage extends Skill {
    public Fire_Rage() {
        super();
        setMaterial(VersionMaterial.FIRE_CHARGE.toMaterial());
        setLore("For {duration} seconds, you slow down and are able", "to cast up to {count} fireballs by left clicking.", "", "Fireballs deal &c{damage} &7damage upon contact", "and ignite your target for &c{ignite} &7seconds.", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("duration", new LinearValue(8, 0));
        addModifier("count", new LinearValue(4, 0));
        addModifier("mana", new LinearValue(15, 1));
        addModifier("damage", new LinearValue(5, 3));
        addModifier("ignite", new LinearValue(2, .1));
        addModifier("cooldown", new LinearValue(9, -.1, 1, 5));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        SkillMetadata cast = new SkillMetadata(caster, skill);
        if (!cast.isSuccessful())
            return cast;

        new Rage(caster, cast);
        return cast;
    }

    public class Rage extends BukkitRunnable implements Listener {
        private final CasterMetadata caster;
        private final StatMap.CachedStatMap cachedStats;
        private final int count, ignite;
        private final double damage;

        private int c;
        private double b;
        private long last = System.currentTimeMillis();

        /*
         * time the player needs to wait before firing two fireballs.
         */
        private static final long timeOut = 700;

        public Rage(CasterMetadata caster, SkillMetadata cast) {
            this.caster = caster;
            this.cachedStats = caster.getPlayerData().getMMOPlayerData().getStatMap().cache(EquipmentSlot.MAIN_HAND);
            this.ignite = (int) (20 * cast.getModifier("ignite"));
            this.damage = cast.getModifier("damage");
            c = count = (int) cast.getModifier("count");

            Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
            Bukkit.getScheduler().runTaskLater(MMOCore.plugin, this::close, (long) (cast.getModifier("duration") * 20));
            runTaskTimer(MMOCore.plugin, 0, 1);
        }

        @EventHandler
        public void a(PlayerInteractEvent event) {
            if (event.getPlayer().equals(caster.getPlayer()) && event.getAction().name().contains("LEFT_CLICK") && (System.currentTimeMillis() - last) > timeOut) {
                last = System.currentTimeMillis();
                castEffect();
                fireball(c < 2);
                if (c-- < 2)
                    close();
            }
        }

        private void castEffect() {
            VectorRotation rotation = new VectorRotation(caster.getPlayer().getEyeLocation());
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 13) {
                Vector vec = rotation.rotate(new Vector(Math.cos(a), Math.sin(a), 0)).add(caster.getPlayer().getEyeLocation().getDirection().multiply(.5)).multiply(.3);
                caster.getPlayer().getWorld().spawnParticle(Particle.FLAME, caster.getPlayer().getLocation().add(0, 1.3, 0).add(caster.getPlayer().getEyeLocation().getDirection().multiply(.5)), 0, vec.getX(), vec.getY(), vec.getZ(), .3);
            }
        }

        private void close() {
            if (isCancelled())
                return;

            cancel();
            HandlerList.unregisterAll(this);
        }

        private void fireball(boolean last) {
            if (last) {
                caster.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                caster.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            }

            caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), VersionSound.ENTITY_FIREWORK_ROCKET_BLAST.toSound(), 1, last ? 0 : 1);
            new BukkitRunnable() {
                int j = 0;
                final Vector vec = caster.getPlayer().getEyeLocation().getDirection();
                final Location loc = caster.getPlayer().getLocation().add(0, 1.3, 0);

                public void run() {
                    if (j++ > 40)
                        cancel();

                    loc.add(vec);

                    if (j % 2 == 0)
                        loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 2, 1);
                    loc.getWorld().spawnParticle(Particle.FLAME, loc, 4, .1, .1, .1, 0);
                    loc.getWorld().spawnParticle(Particle.LAVA, loc, 0);

                    for (Entity target : MMOCoreUtils.getNearbyChunkEntities(loc))
                        if (MythicLib.plugin.getVersion().getWrapper().isInBoundingBox(target, loc) && MMOCoreUtils.canTarget(caster.getPlayerData(), target)) {
                            loc.getWorld().spawnParticle(Particle.LAVA, loc, 8);
                            loc.getWorld().spawnParticle(Particle.FLAME, loc, 32, 0, 0, 0, .1);
                            loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_HURT, 2, 1);
                            target.setFireTicks(target.getFireTicks() + ignite);
                            caster.attack((LivingEntity) target, damage, DamageType.SKILL, DamageType.PROJECTILE, DamageType.MAGIC);
                            cancel();
                        }
                }
            }.runTaskTimer(MMOCore.plugin, 0, 1);
        }

        @Override
        public void run() {
            if (caster.getPlayer().isDead() || !caster.getPlayer().isOnline()) {
                close();
                return;
            }

            b += Math.PI / 30;
            for (int j = 0; j < c; j++) {
                double a = Math.PI * 2 * j / count + b;
                caster.getPlayer().spawnParticle(Particle.FLAME, caster.getPlayer().getLocation().add(Math.cos(a) * 1.5, 1 + Math.sin(a * 1.5) * .7, Math.sin(a) * 1.5), 0);
            }
        }
    }
}
