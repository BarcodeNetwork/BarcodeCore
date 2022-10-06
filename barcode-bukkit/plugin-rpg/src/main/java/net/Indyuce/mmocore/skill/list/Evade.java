package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.SmallParticleEffect;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Evade extends Skill {
    public Evade() {
        super();
        setMaterial(Material.LEATHER_BOOTS);
        setLore("You become imune to damage for &8{duration} &7seconds.", "Cancels when dealing weapon damage.", "", "&e{cooldown}s Cooldown",
                "&9Costs {mana} {mana_name}");

        addModifier("cooldown", new LinearValue(20, 0));
        addModifier("mana", new LinearValue(8, 3));
        addModifier("duration", new LinearValue(2, .3, 2, 10));
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata caster, SkillInfo skill) {
        SkillMetadata cast = new SkillMetadata(caster, skill);
        if (!cast.isSuccessful())
            return cast;

            caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), VersionSound.ENTITY_ENDERMAN_TELEPORT.toSound(), 1, 2);
            new SmallParticleEffect(caster.getPlayer(), Particle.CLOUD);
        new EvadeSkill(caster.getPlayerData(), cast.getModifier("duration"));
        return cast;
    }

    private static class EvadeSkill extends BukkitRunnable implements Listener {
        private final PlayerData data;

        public EvadeSkill(PlayerData data, double duration) {
            this.data = data;

            Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
            runTaskTimer(MMOCore.plugin, 0, 1);
            Bukkit.getScheduler().runTaskLater(MMOCore.plugin, this::close, (long) (duration * 20));
        }

        private void close() {
            cancel();
            EntityDamageEvent.getHandlerList().unregister(this);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void a(EntityDamageEvent event) {
            if (!data.isOnline()) return;
            if (event.getEntity().equals(data.getPlayer()))
                event.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void b(PlayerAttackEvent event) {
            if (event.getAttack().getDamage().hasType(DamageType.WEAPON) && !event.isCancelled() && PlayerData.get(event.getData().getUniqueId()).equals(data))
                close();
        }

        @Override
        public void run() {
            if (!data.isOnline() || data.getPlayer().isDead())
                close();
            else
                data.getPlayer().getWorld().spawnParticle(Particle.CLOUD, data.getPlayer().getLocation().add(0, 1, 0), 0);
        }
    }
}
