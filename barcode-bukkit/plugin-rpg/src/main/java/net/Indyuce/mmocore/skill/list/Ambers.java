package net.Indyuce.mmocore.skill.list;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.damage.DamageType;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.PlayerResourceUpdateEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.stats.StatType;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.api.util.math.particle.ParabolicProjectile;
import net.Indyuce.mmocore.skill.PassiveSkill;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

public class Ambers extends PassiveSkill {
    public Ambers() {
        super();

        setMaterial(Material.EMERALD);
        setLore("Dealing magic damage has &630% &7chance", "of dropping an amber on the ground.", "", "When picked up, ambers stack and",
                "refund &9{percent}% &7of your missing mana.", "", "&oAmbers can be used in other damaging skills.",
                "&oThe more you collect, the more powerful the skills.", "", "&e{cooldown}s Cooldown");
        setPassive();

        addModifier("cooldown", new LinearValue(3, -.1, 1, 3));
        addModifier("percent", new LinearValue(10, .1, 10, 20));

        Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
    }

    @EventHandler
    public void a(PlayerAttackEvent event) {
        PlayerData data = PlayerData.get(event.getData().getUniqueId());
        if (!event.getAttack().getDamage().hasType(DamageType.SKILL) || !data.getProfess().hasSkill(this))
            return;

        SkillMetadata cast = data.cast(this);
        if (!cast.isSuccessful())
            return;

        Location loc = event.getEntity().getLocation();
        double a = random.nextDouble() * 2 * Math.PI;

        new Amber(data, loc.add(0, event.getEntity().getHeight() / 2, 0), loc.clone().add(4 * Math.cos(a), 0, 4 * Math.sin(a)), cast.getModifier("percent"));
    }

    public static class Amber extends BukkitRunnable {
        private final Location loc;
        private final PlayerData data;
        private final double percent;

        private int j;

        private Amber(PlayerData data, Location source, Location loc, double percent) {
            this.loc = loc;
            this.data = data;
            this.percent = percent / 100;

            final Amber amber = this;
            new ParabolicProjectile(source, loc, Particle.REDSTONE, () -> amber.runTaskTimer(MMOCore.plugin, 0, 3), 1, Color.ORANGE, 1.3f);
        }

        @Override
        public void run() {
            if (!data.isOnline()) return;
            if (j++ > 66 || !data.getPlayer().getWorld().equals(loc.getWorld())) {
                cancel();
                return;
            }

            if (data.getPlayer().getLocation().distanceSquared(loc) < 2) {

                data.getPlayer().playSound(data.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
                // data.getSkillData().ambers++;
                data.giveMana((data.getStats().getStat(StatType.MAX_MANA) - data.getMana()) * percent, PlayerResourceUpdateEvent.UpdateReason.SKILL_REGENERATION);

                cancel();
                return;
            }

            for (int j = 0; j < 5; j++)
                loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 0, 1, 0.647, 0, 1);
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(Color.ORANGE, 1.3f));
        }
    }
}
