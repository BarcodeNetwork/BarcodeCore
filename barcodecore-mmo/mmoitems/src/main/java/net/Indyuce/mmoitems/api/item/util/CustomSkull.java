package net.Indyuce.mmoitems.api.item.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class CustomSkull extends ConfigItem {
	private final String textureValue;

	public CustomSkull(String id, String textureValue) {
		this(id, textureValue, null);
	}

	public CustomSkull(String id, String textureValue, String name, String... lore) {
		super(id, VersionMaterial.PLAYER_HEAD.toMaterial(), name, lore);

		this.textureValue = textureValue;
	}

	public void updateItem() {
		setItem(VersionMaterial.PLAYER_HEAD.toItem());
		ItemMeta meta = getItem().getItemMeta();
		meta.setDisplayName(MythicLib.plugin.parseColors(getName()));
		meta.addItemFlags(ItemFlag.values());

		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
		gameProfile.getProperties().put("textures", new Property("textures", textureValue));
		try {
			Field profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, gameProfile);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
			MMOItems.plugin.getLogger().log(Level.WARNING, "Could not load skull texture");
		}

		if (hasLore()) {
			List<String> lore = new ArrayList<>();
			getLore().forEach(str -> lore.add(ChatColor.GRAY + MythicLib.plugin.parseColors(str)));
			meta.setLore(lore);
		}

		getItem().setItemMeta(meta);
		setItem(MythicLib.plugin.getVersion().getWrapper().getNBTItem(getItem()).addTag(new ItemTag("ItemId", getId())).toItem());
	}
}
