package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.version.VersionMaterial;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.ParabolicProjectile;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.TargetSkillMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Telekinesy extends Skill {
    public Telekinesy() {
        super();
        setMaterial(VersionMaterial.MAGENTA_DYE.toMaterial());
        setLore("You take the control over your target", "for &9{duration} &7seconds. Left click to throw him.", "Knockback force: &f{knockback}%", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(20, -.3, 10, 20));
        addModifier("mana", new LinearValue(20, 2));
        addModifier("knockback", new LinearValue(50, 10));
        addModifier("duration", new LinearValue(3, .1, 3, 6));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        TargetSkillMetadata cast = new TargetSkillMetadata(caster, skill, 7);
        if (!cast.isSuccessful())
            return cast;

        caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
        new TelekinesyRunnable(caster.getPlayerData(), cast.getTarget(), cast.getModifier("duration"), cast.getModifier("knockback") / 100);
        return cast;
    }

    public static class TelekinesyRunnable extends BukkitRunnable implements Listener {
        private final Entity entity;
        private final PlayerData data;

        private final long duration;
        private final double d, f;

        private int j;

        public TelekinesyRunnable(PlayerData data, Entity entity, double duration, double force) {
            this.entity = entity;
            this.data = data;

            d = data.getPlayer().getLocation().distance(entity.getLocation());
            f = force;
            this.duration = (long) (20 * duration);

            runTaskTimer(MMOCore.plugin, 0, 1);
            Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
        }

        @EventHandler
        public void a(PlayerInteractEvent event) {
            if (event.getPlayer().equals(data.getPlayer()) && event.getAction().name().contains("LEFT_CLICK")) {
                entity.setVelocity(data.getPlayer().getEyeLocation().getDirection().multiply(1.5 * f));
                entity.getWorld().playSound(entity.getLocation(), VersionSound.ENTITY_FIREWORK_ROCKET_BLAST.toSound(), 2, 1);
                entity.getWorld().spawnParticle(Particle.SPELL_WITCH, entity.getLocation().add(0, entity.getHeight() / 2, 0), 16);
                close();
            }
        }

        @Override
        public void run() {
            if (!data.isOnline() || entity.isDead() || j++ >= duration) {
                close();
                return;
            }

            if (j % 8 == 0)
                new ParabolicProjectile(data.getPlayer().getEyeLocation(), entity.getLocation().add(0, entity.getHeight() / 2, 0), Particle.SPELL_WITCH);

            Location loc = data.getPlayer().getEyeLocation().add(data.getPlayer().getEyeLocation().getDirection().multiply(d));
            entity.setVelocity(loc.subtract(entity.getLocation().add(0, entity.getHeight() / 2, 0)).toVector().multiply(2));
            entity.setFallDistance(0);
        }

        private void close() {
            cancel();
            HandlerList.unregisterAll(this);
        }
    }
}
