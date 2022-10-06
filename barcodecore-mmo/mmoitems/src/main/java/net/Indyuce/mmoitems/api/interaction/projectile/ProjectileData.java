package net.Indyuce.mmoitems.api.interaction.projectile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.damage.AttackMetadata;
import net.Indyuce.mmoitems.api.ItemAttackMetadata;
import net.Indyuce.mmoitems.stat.data.PotionEffectData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

public class ProjectileData {
	private final NBTItem sourceItem;
	private final ItemAttackMetadata attackMeta;
	private final boolean customWeapon;

	public ProjectileData(NBTItem sourceItem, ItemAttackMetadata attackMeta, boolean customWeapon) {
		this.attackMeta = attackMeta;
		this.sourceItem = sourceItem;
		this.customWeapon = customWeapon;
	}

	public NBTItem getSourceItem() {
		return sourceItem;
	}

	public ItemAttackMetadata getAttackMetadata() {
		return attackMeta;
	}

	/*
	 * if the item is an item from MMOItems, apply on-hit effects like critical
	 * strikes, pvp/pve damage and elemental damage
	 */
	public boolean isCustomWeapon() {
		return customWeapon;
	}

	public void applyPotionEffects(LivingEntity target) {
		if (sourceItem.hasTag("MMOITEMS_ARROW_POTION_EFFECTS"))
			for (JsonElement entry : MythicLib.plugin.getJson().parse(sourceItem.getString("MMOITEMS_ARROW_POTION_EFFECTS"), JsonArray.class)) {
				if (!entry.isJsonObject())
					continue;

				JsonObject object = entry.getAsJsonObject();
				target.addPotionEffect(new PotionEffectData(PotionEffectType.getByName(object.get("type").getAsString()),
						object.get("duration").getAsDouble(), object.get("level").getAsInt()).toEffect());
			}
	}
}
