package net.Indyuce.mmocore.skill.list;

import java.util.Optional;

import net.Indyuce.mmocore.skill.PassiveSkill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.PlayerResourceUpdateEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import io.lumine.mythic.lib.version.VersionMaterial;

public class Neptune_Gift extends PassiveSkill {
	public Neptune_Gift() {
		super("NEPTUNE_GIFT");
		setName("Neptune's Gift");

		setMaterial(VersionMaterial.LILY_PAD.toMaterial());
		setLore("Resource regeneration is increased by &8{extra}% &7when standing in water.");
		setPassive();

		addModifier("extra", new LinearValue(30, 5));

		Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
	}

	@EventHandler
	public void a(PlayerResourceUpdateEvent event) {
		PlayerData data = event.getData();
		if (event.getPlayer().getLocation().getBlock().getType() == Material.WATER) {
			Optional<SkillInfo> skill = data.getProfess().findSkill(this);
			skill.ifPresent(skillInfo -> event.setAmount(event.getAmount() * (1 + skillInfo.getModifier("extra", data.getSkillLevel(this)) / 100)));
		}
	}
}
