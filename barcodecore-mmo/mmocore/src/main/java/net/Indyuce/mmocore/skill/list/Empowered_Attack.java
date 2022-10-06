package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.version.VersionMaterial;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.SmallParticleEffect;
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
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class Empowered_Attack extends Skill {
    private static final double perb = 5;

    public Empowered_Attack() {
        super();
        setMaterial(VersionMaterial.BONE_MEAL.toMaterial());
        setLore("You charge your weapon with lightning.", "Your next attack deals &f{extra}% &7extra damage", "and spreads to enemies within &f{radius} &7blocks", "for &f{ratio}% &7of the initial damage.", "", "&e{cooldown}s Cooldown", "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(10, -.2, 5, 10));
        addModifier("mana", new LinearValue(4, 1));
        addModifier("radius", new LinearValue(4, 0));
        addModifier("ratio", new LinearValue(30, 10, 30, 100));
        addModifier("extra", new LinearValue(30, 8));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        SkillMetadata cast = new SkillMetadata(caster, skill);
        if (!cast.isSuccessful())
            return cast;

        caster.getPlayer().playSound(caster.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
        new EmpoweredAttack(caster, cast.getModifier("extra"), cast.getModifier("ratio"), cast.getModifier("radius"));
        return cast;
    }

    private void drawVector(Location loc, Vector vec) {

        double steps = vec.length() * perb;
        Vector v = vec.clone().normalize().multiply((double) 1 / perb);

        for (int j = 0; j < Math.min(steps, 124); j++)
            loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.add(v), 0);
    }

    public class EmpoweredAttack implements Listener {
        private final CasterMetadata caster;
        private final double c, r, rad;

        public EmpoweredAttack(CasterMetadata caster, double extra, double ratio, double radius) {
            this.caster = caster;
            this.c = 1 + extra / 100;
            this.r = ratio / 100;
            this.rad = radius;

            if (caster.getPlayerData().isOnline())
                new SmallParticleEffect(caster.getPlayer(), Particle.FIREWORKS_SPARK);

            Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
            Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCore.plugin, this::close, 80);
        }

        private void close() {
            PlayerAttackEvent.getHandlerList().unregister(this);
        }

        @EventHandler
        public void a(PlayerAttackEvent event) {
            if (!caster.getPlayerData().isOnline()) return;
            if (event.getPlayer().equals(caster.getPlayer()) && event.getAttack().getDamage().hasType(DamageType.WEAPON)) {
                close();

                Entity target = event.getEntity();

                /*
                 * play lightning effect
                 */
                final Location loc = target.getLocation().add(0, target.getHeight() / 2, 0);
                for (int j = 0; j < 3; j++) {
                    Location clone = loc.clone();
                    double a = random.nextDouble() * Math.PI * 2;
                    loc.add(Math.cos(a), 5, Math.sin(a));
                    drawVector(clone, loc.clone().subtract(clone).toVector());
                }

                target.getWorld().playSound(target.getLocation(), VersionSound.ENTITY_FIREWORK_ROCKET_BLAST.toSound(), 2, .5f);
                target.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, target.getLocation().add(0, target.getHeight() / 2, 0), 32, 0, 0, 0, .2);

                double sweep = event.getAttack().getDamage().getDamage() * r;
                Location src = target.getLocation().add(0, target.getHeight() / 2, 0);

                for (Entity entity : target.getNearbyEntities(rad, rad, rad))
                    if (MMOCoreUtils.canTarget(caster.getPlayerData(), entity)) {
                        drawVector(src, entity.getLocation().add(0, entity.getHeight() / 2, 0).subtract(src).toVector());
                        caster.attack((LivingEntity) entity, sweep, DamageType.PHYSICAL, DamageType.SKILL);
                    }

                /*
                 * Apply damage afterwards otherwise the damage dealt to nearby
                 * entities scale with the extra ability damage.
                 */
                event.getAttack().getDamage().multiply(1 + c, DamageType.WEAPON);
            }
        }
    }
}
