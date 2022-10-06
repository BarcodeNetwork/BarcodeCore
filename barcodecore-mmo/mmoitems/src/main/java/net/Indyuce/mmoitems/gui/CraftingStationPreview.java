package net.Indyuce.mmoitems.gui;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.LegacyComponent;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.mythic.utils.adventure.text.Component;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.api.crafting.ingredient.CheckedIngredient;
import net.Indyuce.mmoitems.api.crafting.recipe.CheckedRecipe;
import net.Indyuce.mmoitems.api.crafting.recipe.CraftingRecipe;
import net.Indyuce.mmoitems.api.crafting.recipe.UpgradingRecipe;
import net.Indyuce.mmoitems.api.item.util.ConfigItems;
import net.Indyuce.mmoitems.api.util.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CraftingStationPreview extends PluginInventory {
	private final CraftingStationView previous;
	private final CheckedRecipe recipe;

	private final List<ItemStack> ingredients = new ArrayList<>();

	private static final int[] slots = { 12, 13, 14, 21, 22, 23, 30, 31, 32 },
			fill = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 15, 17, 18, 19, 25, 26, 27, 29, 33, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44 };

	public CraftingStationPreview(CraftingStationView previous, CheckedRecipe recipe) {
		super(previous.getPlayer());

		this.previous = previous;
		this.recipe = recipe;
	}

	@Override
	public Inventory getInventory() {

		// Create inventory of a nice size (5x6)
		Inventory inv = Bukkit.createInventory(this, 45, Message.RECIPE_PREVIEW.formatRaw(ChatColor.RESET));
		ingredients.clear();

		// Include each ingredient
		for (CheckedIngredient ing : recipe.getIngredients()) {

			// Amount exceeds 64?
			if (ing.getIngredient().getAmount() > 64) {

				// Generate new item for display
				ItemStack sample = ing.getIngredient().generateItemStack(playerData.getRPG());
				sample.setAmount(64);

				/*
				 * Time to calculate the stacks and put through the crafting station space.
				 */
				int amount = ing.getIngredient().getAmount();
				int stacks = SilentNumbers.floor(amount / 64D);

				// Add what must be added
				while (amount > 0) {

					// Still too large?
					if (amount > 64) {

						// Put a whole stack
						ingredients.add(sample.clone());

						// Subtract a whole stack
						amount-= 64;

					// No longer greater than 64, :sleep:
					} else {

						// Add remaining amount
						sample.setAmount(amount);
						ingredients.add(sample.clone());

						// Done
						amount-=amount;

					} }

			// Not greater than 64, just put it like that.
			} else {

				ingredients.add(ing.getIngredient().generateItemStack(playerData.getRPG()));
			}
		}

		int min = (page - 1) * slots.length, max = page * slots.length;
		for (int j = min; j < max; j++) {
			if (j >= ingredients.size())
				break;
			inv.setItem(slots[j - min], ingredients.get(j));
		}

		for (int slot : fill)
			inv.setItem(slot, ConfigItems.FILL.getItem());

		if (recipe.getRecipe() instanceof CraftingRecipe) {
			ItemStack item = ((CraftingRecipe) recipe.getRecipe()).getOutput().getPreview();
			item.setAmount(((CraftingRecipe) recipe.getRecipe()).getOutput().getAmount());
			inv.setItem(16, item);
		}
		if (recipe.getRecipe() instanceof UpgradingRecipe) {
			NBTItem nbtItem = NBTItem.get(((UpgradingRecipe) recipe.getRecipe()).getItem().getPreview());
			nbtItem.setDisplayNameComponent(LegacyComponent.parse(
					nbtItem.toItem().getItemMeta().getDisplayName() + ChatColor.GREEN + "+1!"));

			inv.setItem(16, nbtItem.toItem());
		}

		inv.setItem(10, ConfigItems.BACK.getItem());
		inv.setItem(34, ConfigItems.CONFIRM.getItem());
		ItemStack bookStack = recipe.display();

		bookStack.setType(Material.KNOWLEDGE_BOOK);
		bookStack.setAmount(1);

		ItemMeta meta = bookStack.getItemMeta();

		for (Enchantment ench : meta.getEnchants().keySet())
			meta.removeEnchant(ench);
		bookStack.setItemMeta(meta);

		NBTItem book = NBTItem.get(bookStack);

		List<Component> lore = book.getLoreComponents();
		book.setLoreComponents(lore.subList(0, lore.size() - 3));

		inv.setItem(28, book.toItem());

		inv.setItem(20, page > 1 ? ConfigItems.PREVIOUS_PAGE.getItem() : ConfigItems.FILL.getItem());
		inv.setItem(24, max < ingredients.size() ? ConfigItems.NEXT_PAGE.getItem() : ConfigItems.FILL.getItem());

		return inv;
	}

	@Override
	public void whenClicked(InventoryClickEvent event) {
		event.setCancelled(true);
		if (!MMOUtils.isMetaItem(event.getCurrentItem(), false))
			return;

		NBTItem nbtItem = MythicLib.plugin.getVersion().getWrapper().getNBTItem(event.getCurrentItem());
		switch (nbtItem.getString("ItemId")) {
			case "CONFIRM":
				previous.processRecipe(recipe);
				previous.open();
				break;
			case "PREVIOUS_PAGE":
				page--;
				open();
				break;
			case "NEXT_PAGE":
				page++;
				open();
				break;
			case "BACK":
				previous.open();
				break;
		}
	}
}
