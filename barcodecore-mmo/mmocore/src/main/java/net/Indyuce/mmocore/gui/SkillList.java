package net.Indyuce.mmocore.gui;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.Skill.SkillInfo;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.gui.api.EditableInventory;
import net.Indyuce.mmocore.gui.api.GeneratedInventory;
import net.Indyuce.mmocore.gui.api.item.InventoryItem;
import net.Indyuce.mmocore.gui.api.item.Placeholders;
import net.Indyuce.mmocore.gui.api.item.SimplePlaceholderItem;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SkillList extends EditableInventory {
	public SkillList() {
		super("skill-list");
	}

	@Override
	public InventoryItem load(String function, ConfigurationSection config) {

		if (function.equals("skill"))
			return new SkillItem(config);

		if (function.equals("switch"))
			return new SwitchItem(config);

		if (function.equals("level"))
			return new LevelItem(config);

		if (function.equals("upgrade"))
			return new InventoryItem<SkillViewerInventory>(config) {

				@Override
				public Placeholders getPlaceholders(SkillViewerInventory inv, int n) {
					Skill selected = inv.selected.getSkill();
					Placeholders holders = new Placeholders();

					holders.register("skill_caps", selected.getName().toUpperCase());
					holders.register("skill", selected.getName());
					holders.register("skill_points", "" + inv.getPlayerData().getSkillPoints());

					return holders;
				}

				@Override
				public boolean canDisplay(SkillViewerInventory inv) {
					return !inv.binding;
				}
			};

		if (function.equals("slot"))
			return new InventoryItem<SkillViewerInventory>(config) {
				private final String none = MythicLib.plugin.parseColors(config.getString("no-skill"));
				private final Material emptyMaterial = Material
						.valueOf(config.getString("empty-item").toUpperCase().replace("-", "_").replace(" ", "_"));
				private final int emptyCMD = config.getInt("empty-custom-model-data", getModelData());

				@Override
				public Placeholders getPlaceholders(SkillViewerInventory inv, int n) {
					Skill selected = inv.selected.getSkill();
					Skill skill = inv.getPlayerData().hasSkillBound(n) ? inv.getPlayerData().getBoundSkill(n).getSkill() : null;

					Placeholders holders = new Placeholders();

					holders.register("skill", skill == null ? none : skill.getName());
					holders.register("index", "" + (n + 1));
					holders.register("slot", MMOCoreUtils.intToRoman(n + 1));
					holders.register("selected", selected.getName());

					return holders;
				}

				@Override
				public ItemStack display(SkillViewerInventory inv, int n) {
					ItemStack item = super.display(inv, n);
					if (!inv.getPlayerData().hasSkillBound(n)) {
						item.setType(emptyMaterial);

						if (MythicLib.plugin.getVersion().isStrictlyHigher(1, 13)) {
							ItemMeta meta = item.getItemMeta();
							meta.setCustomModelData(emptyCMD);
							item.setItemMeta(meta);
						}
					} else {
						if (MythicLib.plugin.getVersion().isStrictlyHigher(1, 13)) {
							Skill skill = inv.getPlayerData().getBoundSkill(n).getSkill();
							item.setType(skill.getIcon().getType());
							ItemMeta meta = item.getItemMeta();
							meta.setCustomModelData(skill.getIcon().getItemMeta().getCustomModelData());

							item.setItemMeta(meta);
						}
					}
					return item;
				}

				@Override
				public boolean canDisplay(SkillViewerInventory inv) {
					return inv.binding;
				}

				@Override
				public boolean hasDifferentDisplay() {
					return true;
				}
			};

		return new SimplePlaceholderItem(config);
	}

	public GeneratedInventory newInventory(PlayerData data) {
		return new SkillViewerInventory(data, this);
	}

	public class SwitchItem extends SimplePlaceholderItem<SkillViewerInventory> {
		private final SimplePlaceholderItem binding, upgrading;

		public SwitchItem(ConfigurationSection config) {
			super(config);

			Validate.isTrue(config.contains("binding"), "Config must have 'binding'");
			Validate.isTrue(config.contains("upgrading"), "Config must have 'upgrading'");

			binding = new SimplePlaceholderItem(config.getConfigurationSection("binding"));
			upgrading = new SimplePlaceholderItem(config.getConfigurationSection("upgrading"));
		}

		@Override
		public ItemStack display(SkillViewerInventory inv, int n) {
			return inv.binding ? upgrading.display(inv) : binding.display(inv);
		}

		@Override
		public boolean canDisplay(SkillViewerInventory inv) {
			return true;
		}
	}

	public class LevelItem extends InventoryItem<SkillViewerInventory> {
		private final int offset;

		public LevelItem(ConfigurationSection config) {
			super(config);

			offset = config.getInt("offset");
		}

		@Override
		public boolean hasDifferentDisplay() {
			return true;
		}

		@Override
		public ItemStack display(SkillViewerInventory inv, int n) {

			SkillInfo skill = inv.selected;
			int skillLevel = inv.getPlayerData().getSkillLevel(skill.getSkill()) + n - offset;
			if (skillLevel < 1)
				return new ItemStack(Material.AIR);

			List<String> lore = new ArrayList<>(getLore());
			int index = lore.indexOf("{lore}");
			lore.remove(index);
			List<String> skillLore = skill.calculateLore(inv.getPlayerData(), skillLevel);
			for (int j = 0; j < skillLore.size(); j++)
				lore.add(index + j, skillLore.get(j));

			for (int j = 0; j < lore.size(); j++)
				lore.set(j, ChatColor.GRAY + MythicLib.plugin.parseColors(lore.get(j)));

			ItemStack skillIcon = skill.getSkill().getIcon();
			ItemStack item = new ItemStack(skillIcon.getType());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(MythicLib.plugin.parseColors(getName().replace("{skill}", skill.getSkill().getName())
					.replace("{roman}", MMOCoreUtils.intToRoman(skillLevel)).replace("{level}", "" + skillLevel)));
			meta.addItemFlags(ItemFlag.values());
			meta.setLore(lore);
			if (MythicLib.plugin.getVersion().isStrictlyHigher(1, 13))
				meta.setCustomModelData(skillIcon.getItemMeta().getCustomModelData());
			item.setItemMeta(meta);

			return NBTItem.get(item).addTag(new ItemTag("skillId", skill.getSkill().getId())).toItem();
		}

		@Override
		public Placeholders getPlaceholders(SkillViewerInventory inv, int n) {
			return new Placeholders();
		}

		@Override
		public boolean canDisplay(SkillViewerInventory inv) {
			return !inv.binding;
		}
	}

	public class SkillItem extends InventoryItem<SkillViewerInventory> {
		private final int selectedSkillSlot;

		public SkillItem(ConfigurationSection config) {
			super(Material.BARRIER, config);

			selectedSkillSlot = config.getInt("selected-slot");
		}

		@Override
		public boolean hasDifferentDisplay() {
			return true;
		}

		@Override
		public ItemStack display(SkillViewerInventory inv, int n) {

			/*
			 * calculate placeholders
			 */
			SkillInfo skill = inv.skills.get(mod(n + inv.getPlayerData().skillGuiDisplayOffset, inv.skills.size()));
			Placeholders holders = getPlaceholders(inv.getPlayerData(), skill);

			List<String> lore = new ArrayList<>(getLore());

			int index = lore.indexOf("{lore}");
			lore.remove(index);
			List<String> skillLore = skill.calculateLore(inv.getPlayerData());
			for (int j = 0; j < skillLore.size(); j++)
				lore.add(index + j, skillLore.get(j));

			boolean unlocked = skill.getUnlockLevel() <= inv.getPlayerData().getLevel();

			lore.removeIf(next -> (next.startsWith("{unlocked}") && !unlocked) || (next.startsWith("{locked}") && unlocked) || (next.startsWith("{max_level}") && (!skill.hasMaxLevel() || skill.getMaxLevel() > inv.getPlayerData().getSkillLevel(skill.getSkill()))));

			for (int j = 0; j < lore.size(); j++)
				lore.set(j, ChatColor.GRAY + holders.apply(inv.getPlayer(), lore.get(j)));

			/*
			 * generate item
			 */
			ItemStack item = skill.getSkill().getIcon();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(holders.apply(inv.getPlayer(), getName()));
			meta.addItemFlags(ItemFlag.values());
			meta.setLore(lore);
			item.setItemMeta(meta);

			return NBTItem.get(item).addTag(new ItemTag("skillId", skill.getSkill().getId())).toItem();
		}

		public Placeholders getPlaceholders(PlayerData player, SkillInfo skill) {
			Placeholders holders = new Placeholders();
			holders.register("skill", skill.getSkill().getName());
			holders.register("unlock", "" + skill.getUnlockLevel());
			holders.register("level", "" + player.getSkillLevel(skill.getSkill()));
			return holders;
		}

		@Override
		public Placeholders getPlaceholders(SkillViewerInventory inv, int n) {
			return new Placeholders();
		}
	}

	public class SkillViewerInventory extends GeneratedInventory {

		/*
		 * cached information
		 */
		private final List<SkillInfo> skills;
		private final List<Integer> skillSlots;
		private final List<Integer> slotSlots;

		private boolean binding;
		private SkillInfo selected;

		public SkillViewerInventory(PlayerData playerData, EditableInventory editable) {
			super(playerData, editable);

			skills = new ArrayList<>(playerData.getProfess().getSkills());
			skillSlots = getEditable().getByFunction("skill").getSlots();
			slotSlots = getEditable().getByFunction("slot").getSlots();
			binding = true;
		}

		@Override
		public String calculateName() {
			return getName();
		}

		@Override
		public void open() {
			int selectedSkillSlot = ((SkillItem) getEditable().getByFunction("skill")).selectedSkillSlot;
			selected = skills.get(mod(selectedSkillSlot + playerData.skillGuiDisplayOffset, skills.size()));

			super.open();
		}

		@Override
		public void whenClicked(InventoryClickEvent event, InventoryItem item) {
			if (skillSlots.contains(event.getRawSlot())
					&& event.getRawSlot() != ((SkillItem) getEditable().getByFunction("skill")).selectedSkillSlot + 2) {
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
				playerData.skillGuiDisplayOffset = (playerData.skillGuiDisplayOffset + (event.getRawSlot() - 4)) % skills.size();
				open();
				return;
			}

			if (item.getFunction().equals("previous")) {
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
				playerData.skillGuiDisplayOffset = (playerData.skillGuiDisplayOffset - 1) % skills.size();
				open();
				return;
			}

			if (item.getFunction().equals("next")) {
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
				playerData.skillGuiDisplayOffset = (playerData.skillGuiDisplayOffset + 1) % skills.size();
				open();
				return;
			}

			if (item.getFunction().equals("switch")) {
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
				binding = !binding;
				open();
				return;
			}

			/*
			 * binding or unbinding skills.
			 */
			if (binding) {
				for (int index = 0; index < slotSlots.size(); index++) {
					int slot = slotSlots.get(index);
					if (event.getRawSlot() == slot) {

						// unbind if there is a current spell.
						if (event.getAction() == InventoryAction.PICKUP_HALF) {
							if (!playerData.hasSkillBound(index)) {
								MMOCore.plugin.configManager.getSimpleMessage("no-skill-bound").send(player);
								player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
								return;
							}

							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
							playerData.unbindSkill(index);
							open();
							return;
						}

						if (selected == null)
							return;

						if (selected.getSkill().isPassive()) {
							MMOCore.plugin.configManager.getSimpleMessage("not-active-skill").send(player);
							player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							return;
						}

						if (!playerData.hasSkillUnlocked(selected)) {
							MMOCore.plugin.configManager.getSimpleMessage("not-unlocked-skill").send(player);
							player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							return;
						}

						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
						playerData.setBoundSkill(index, selected);
						open();
						return;
					}
				}

				/*
				 * upgrading a player skill
				 */
			} else if (item.getFunction().equals("upgrade")) {
				if (!playerData.hasSkillUnlocked(selected)) {
					MMOCore.plugin.configManager.getSimpleMessage("not-unlocked-skill").send(player);
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					return;
				}

				if (playerData.getSkillPoints() < 1) {
					MMOCore.plugin.configManager.getSimpleMessage("not-enough-skill-points").send(player);
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					return;
				}

				if (selected.hasMaxLevel() && playerData.getSkillLevel(selected.getSkill()) >= selected.getMaxLevel()) {
					MMOCore.plugin.configManager.getSimpleMessage("skill-max-level-hit").send(player);
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					return;
				}

				playerData.giveSkillPoints(-1);
				playerData.setSkillLevel(selected.getSkill(), playerData.getSkillLevel(selected.getSkill()) + 1);
				MMOCore.plugin.configManager.getSimpleMessage("upgrade-skill", "skill", selected.getSkill().getName(), "level",
						"" + playerData.getSkillLevel(selected.getSkill())).send(player);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
				open();
			}
		}
	}

	private int mod(int x, int n) {
		return x < 0 ? (x + n) : (x % n);
	}
}