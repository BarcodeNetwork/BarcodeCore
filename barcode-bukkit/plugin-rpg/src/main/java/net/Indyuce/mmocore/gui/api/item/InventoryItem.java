package net.Indyuce.mmocore.gui.api.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.gui.api.GeneratedInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public abstract class InventoryItem<T extends GeneratedInventory> {
	private final String id, function;
	private final List<Integer> slots = new ArrayList<>();

	private final Material material;
	private final String name, texture;
	private final List<String> lore;
	private final int modelData;
	private final boolean placeholders, hideFlags;

	public InventoryItem(ConfigurationSection config) {
		this(Material.valueOf(config.getString("item", "").toUpperCase().replace(" ", "_").replace("-", "_")), config);
	}

	public InventoryItem(Material material, ConfigurationSection config) {
		this.id = config.getName();
		this.function = config.getString("function", "");

		this.material = material;
		this.name = config.getString("name");
		this.lore = config.getStringList("lore");
		this.hideFlags = config.getBoolean("hide-flags");
		this.texture = config.getString("texture");
		this.placeholders = config.getBoolean("placeholders");
		this.modelData = config.getInt("custom-model-data");

		config.getStringList("slots").forEach(str -> slots.add(Integer.parseInt(str)));
	}

	public String getId() {
		return id;
	}

	public String getFunction() {
		return function;
	}

	public boolean hasFunction() {
		return !function.isEmpty();
	}

	public List<Integer> getSlots() {
		return slots;
	}

	public Material getMaterial() {
		return material;
	}

	public boolean hideFlags() {
		return hideFlags;
	}

	public boolean hasName() {
		return name != null;
	}

	public String getName() {
		return name;
	}

	public boolean hasLore() {
		return lore != null && !lore.isEmpty();
	}

	public List<String> getLore() {
		return lore;
	}

	public int getModelData() {
		return modelData;
	}

	public void setDisplayed(Inventory inv, T generated) {
		generated.addLoaded(this);

		if (!hasDifferentDisplay()) {
			ItemStack display = display(generated);
			for (int slot : getSlots())
				inv.setItem(slot, display);
		} else
			for (int j = 0; j < slots.size(); j++)
				inv.setItem(slots.get(j), display(generated, j));

	}

	public boolean hasDifferentDisplay() {
		return false;
	}

	public boolean canDisplay(T inv) {
		return true;
	}

	public ItemStack display(T inv) {
		return display(inv, 0);
	}

	public ItemStack display(T inv, int n) {

		Placeholders placeholders = getPlaceholders(inv, n);
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		if (texture != null && meta instanceof SkullMeta)
			applyTexture(texture, (SkullMeta) meta);

		if (hasName())
			meta.setDisplayName(placeholders.apply(inv.getPlayer(), getName()));

		if (hideFlags())
			meta.addItemFlags(ItemFlag.values());

		if (hasLore()) {
			List<String> lore = new ArrayList<>();
			getLore().forEach(line -> lore.add(ChatColor.GRAY + placeholders.apply(inv.getPlayer(), line)));
			meta.setLore(lore);
		}

		if (MythicLib.plugin.getVersion().isStrictlyHigher(1, 13))
			meta.setCustomModelData(getModelData());

		item.setItemMeta(meta);
		return item;
	}

	private void applyTexture(String value, SkullMeta meta) {
		try {
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);
			profile.getProperties().put("textures", new Property("textures", value));

			Field profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
			MMOCore.log(Level.WARNING, "Could not apply item texture value of " + getId());
		}
	}

	public Placeholders getPlaceholders(T inv) {
		return getPlaceholders(inv, 0);
	}

	public abstract Placeholders getPlaceholders(T inv, int n);
}
