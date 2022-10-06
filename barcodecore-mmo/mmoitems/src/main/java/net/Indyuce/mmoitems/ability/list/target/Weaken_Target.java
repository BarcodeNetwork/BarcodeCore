package net.Indyuce.mmoitems.ability.list.target;

import io.lumine.mythic.lib.version.VersionSound;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.ability.TargetAbility;
import net.Indyuce.mmoitems.ability.metadata.TargetAbilityMetadata;
import io.lumine.mythic.lib.damage.AttackMetadata;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Weaken_Target extends TargetAbility implements Listener {
    public final Map<UUID, WeakenedInfo> marked = new HashMap<>();

    public Weaken_Target() {
        super();

        addModifier("duration", 4);
        addModifier("extra-damage", 40);
        addModifier("cooldown", 10);
        addModifier("mana", 0);
        addModifier("stamina", 0);
	}

    @Override
    public void whenCast(AttackMetadata attack, TargetAbilityMetadata ability) {
        LivingEntity target = ability.getTarget();

        marked.put(target.getUniqueId(), new WeakenedInfo(ability.getModifier("extra-damage")));
        effect(target.getLocation());
        target.getWorld().playSound(target.getLocation(), VersionSound.ENTITY_ENDERMAN_HURT.toSound(), 2, 1.5f);

        /*
         * display particles until the entity is hit again and eventually remove
         * the mark from the entity
         */
        new BukkitRunnable() {
			final long duration = (long) (ability.getModifier("duration") * 1000);

			public void run() {
				if (!marked.containsKey(target.getUniqueId()) || marked.get(target.getUniqueId()).date + duration < System.currentTimeMillis()) {
					cancel();
					return;
				}

				for (double j = 0; j < Math.PI * 2; j += Math.PI / 18)
					target.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation().clone().add(Math.cos(j) * .7, .1, Math.sin(j) * .7),
							0);
			}
		}.runTaskTimer(MMOItems.plugin, 0, 20);
	}

	@EventHandler
	public void a(EntityDamageEvent event) {
		if (event.getCause() != DamageCause.ENTITY_ATTACK && event.getCause() != DamageCause.ENTITY_EXPLOSION
				&& event.getCause() != DamageCause.PROJECTILE)
			return;

		Entity entity = event.getEntity();
		if (marked.containsKey(entity.getUniqueId())) {
			event.setDamage(event.getDamage() * (1 + marked.get(entity.getUniqueId()).extraDamage));
			effect(entity.getLocation());
			marked.remove(entity.getUniqueId());
			entity.getWorld().playSound(entity.getLocation(), VersionSound.ENTITY_ENDERMAN_DEATH.toSound(), 2, 2);
		}
	}

	@EventHandler
	public void b(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (item.getType() == Material.MILK_BUCKET && marked.containsKey(player.getUniqueId())) {
			marked.remove(player.getUniqueId());
			player.getWorld().playSound(player.getLocation(), VersionSound.ENTITY_ENDERMAN_DEATH.toSound(), 2, 2);
		}
	}

	private void effect(Location loc) {
		new BukkitRunnable() {
			double y = 0;

			public void run() {
				for (int j = 0; j < 3; j++) {
					y += .07;
					for (int k = 0; k < 3; k++)
						loc.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().add(Math.cos(y * Math.PI + (k * Math.PI * 2 / 3)) * (3 - y) / 2.5,
								y, Math.sin(y * Math.PI + (k * Math.PI * 2 / 3)) * (3 - y) / 2.5), 1, new Particle.DustOptions(Color.BLACK, 1));
				}
				if (y > 3)
					cancel();
			}
		}.runTaskTimer(MMOItems.plugin, 0, 1);
	}

	public static class WeakenedInfo {
		private final long date = System.currentTimeMillis();
		private final double extraDamage;

		public WeakenedInfo(double extraDamage) {
			this.extraDamage = extraDamage / 100;
		}
	}
}
