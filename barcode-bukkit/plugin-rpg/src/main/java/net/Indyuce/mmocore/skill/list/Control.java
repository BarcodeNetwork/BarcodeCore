package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.version.VersionMaterial;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.TargetSkillMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Control extends Skill {
    public Control() {
        super();
        setMaterial(VersionMaterial.MAGENTA_DYE.toMaterial());
        setLore("Your target is temporarily slowed for &8{duration} &7seconds.", "As soon as you left click, it gets", "pushed back where you are looking at.", "Knockback force: &f{knockback}%", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(18, -.3, 10, 20));
        addModifier("mana", new LinearValue(15, 1.5));
        addModifier("knockback", new LinearValue(30, 3));
        addModifier("duration", new LinearValue(2, 0));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        TargetSkillMetadata cast = new TargetSkillMetadata(caster, skill, 7);
        if (!cast.isSuccessful())
            return cast;

        caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
        cast.getTarget().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 0));
        new TelekinesyRunnable(caster, cast.getTarget(), cast.getModifier("knockback") / 100, cast.getModifier("duration"));
        return cast;
    }

    public static class TelekinesyRunnable extends BukkitRunnable implements Listener {
        private final LivingEntity entity;
        private final CasterMetadata caster;

        private final double f, d;

        private int j;

        public TelekinesyRunnable(CasterMetadata caster, LivingEntity entity, double force, double duration) {
            this.entity = entity;
            this.caster = caster;

            d = duration * 20;
            f = force;

            runTaskTimer(MMOCore.plugin, 0, 1);
            Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
        }

        @EventHandler
        public void a(PlayerInteractEvent event) {
            if (event.getPlayer().equals(caster.getPlayer()) && event.getAction().name().contains("LEFT_CLICK")) {
                Vector vec = caster.getPlayer().getEyeLocation().getDirection().multiply(3 * f);
                vec.setY(Math.max(.5, vec.getY() / 2));
                entity.setVelocity(vec);

                /*
                 * try not to interfere with other potion effects
                 */
                PotionEffect effect = entity.getPotionEffect(PotionEffectType.SLOW);
                if (effect.getDuration() < d && effect.getAmplifier() == 0)
                    entity.removePotionEffect(PotionEffectType.SLOW);

                entity.getWorld().spawnParticle(Particle.SPELL_WITCH, entity.getLocation().add(0, entity.getHeight() / 2, 0), 16);
                entity.getWorld().playSound(entity.getLocation(), VersionSound.ENTITY_FIREWORK_ROCKET_BLAST.toSound(), 2, 1);
                close();
            }
        }

        @Override
        public void run() {
            if (!caster.getPlayerData().isOnline() || entity.isDead() || j++ >= d) {
                close();
                return;
            }

            double a = (double) j / 3;
            entity.getWorld().spawnParticle(Particle.SPELL_WITCH, entity.getLocation().add(Math.cos(a), entity.getHeight() / 2, Math.sin(a)), 0);
        }

        private void close() {
            cancel();
            HandlerList.unregisterAll(this);
        }
    }
}
