package net.Indyuce.mmoitems.stat;

import com.google.gson.*;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.CommandListEdition;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.CommandData;
import net.Indyuce.mmoitems.stat.data.CommandListData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Commands extends ItemStat {
	private static final int max = 15;

	public Commands() {
		super("COMMANDS", VersionMaterial.COMMAND_BLOCK_MINECART.toMaterial(), "Commands",
				new String[] { "The commands your item", "performs when right clicked." }, new String[] { "!armor", "!block", "!gem_stone", "all" });
	}

	@Override
	public CommandListData whenInitialized(Object object) {
		Validate.isTrue(object instanceof ConfigurationSection, "Must specify a config section");
		ConfigurationSection config = (ConfigurationSection) object;

		CommandListData list = new CommandListData();

		for (String key : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(key);
			list.add(new CommandData(section.getString("format"), section.getDouble("delay"), section.getBoolean("console"),
					section.getBoolean("op")));
		}

		return list;
	}

	@Override
	public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
		new CommandListEdition(inv.getPlayer(), inv.getEdited()).open(inv.getPage());
	}

	@Override
	public void whenInput(@NotNull EditionInventory inv, @NotNull String message, Object... info) {
		if (inv.getEditedSection().contains("commands"))
			if (inv.getEditedSection().getConfigurationSection("commands").getKeys(false).size() >= max) {
				// max command number = 8
				inv.open();
				inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Your item has reached the " + max + " commands limit.");
				return;
			}

		double delay = 0;
		boolean console = false, op = false;

		String[] split = message.split(" ");
		for (int j = 0; j < split.length && split[j].startsWith("-"); j++) {
			String arg = split[j];
			if (arg.startsWith("-d:")) {
				delay = MMOUtils.parseDouble(arg.substring(3));
				message = message.replaceFirst(arg + " ", "");
			} else if (arg.equalsIgnoreCase("-c")) {
				console = true;
				message = message.replaceFirst(arg + " ", "");
			} else if (arg.equalsIgnoreCase("-op")) {
				op = true;
				message = message.replaceFirst(arg + " ", "");
			}
		}

		/*
		 * determine the command ID based on the command IDs which have been
		 * registered before.
		 */
		ConfigurationSection commands = inv.getEditedSection().getConfigurationSection("commands");
		String path = "cmd" + (max + 1);
		if (commands == null)
			path = "cmd0";
		else
			for (int j = 0; j < max; j++)
				if (!commands.contains("cmd" + j)) {
					path = "cmd" + j;
					break;
				}

		inv.getEditedSection().set("commands." + path + ".format", message);
		inv.getEditedSection().set("commands." + path + ".delay", delay);
		inv.getEditedSection().set("commands." + path + ".console", console ? console : null);
		inv.getEditedSection().set("commands." + path + ".op", op ? op : null);
		inv.registerTemplateEdition();
		inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Command successfully registered.");
	}

	@Override
	public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {
		lore.add(ChatColor.GRAY + "Current Commands: " + ChatColor.RED
				+ (statData.isPresent() ? ((CommandListData) statData.get()).getCommands().size() : "0"));
		lore.add("");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Click to edit item commands.");
	}

	@Override
	public @NotNull StatData getClearStatData() {
		return new CommandListData();
	}

	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {

		// Add persistent tags onto item
		item.addItemTag(getAppliedNBT(data));

		// Addlore
		List<String> lore = new ArrayList<>();
		String commandFormat = ItemStat.translate("command");
		((CommandListData) data).getCommands().forEach(command -> {

			lore.add(commandFormat.replace("#c", "/" + command.getCommand()).replace("#d", "" + command.getDelay()));
		});
		item.getLore().insert("commands", lore);
	}

	@NotNull
	@Override
	public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {

		// Will end up returning this
		ArrayList<ItemTag> ret = new ArrayList<>();

		// But it contains only 1 tag: THIS
		JsonArray array = new JsonArray();

		// Add each command
		for (CommandData cd : ((CommandListData) data).getCommands()) {

			JsonObject object = new JsonObject();
			object.addProperty("Command", cd.getCommand());
			object.addProperty("Delay", cd.getDelay());
			object.addProperty("Console", cd.isConsoleCommand());
			object.addProperty("Op", cd.hasOpPerms());

			// Include object
			array.add(object);
		}

		// Add that tag in there
		ret.add(new ItemTag(getNBTPath(), array.toString()));

		// Thats it
		return ret;
	}

	@Override
	public void whenLoaded(@NotNull ReadMMOItem mmoitem) {

		// Find the relevant tag
		ArrayList<ItemTag> relevantTag = new ArrayList<>();

		// Yes
		if (mmoitem.getNBT().hasTag(getNBTPath()))
			relevantTag.add(ItemTag.getTagAtPath(getNBTPath(), mmoitem.getNBT(), SupportedNBTTagValues.STRING));

		// Attempt to bake data
		CommandListData data = (CommandListData) getLoadedNBT(relevantTag);

		// Valid?
		if (data != null) {

			// yup
			mmoitem.setData(this, data);
		}
	}

	@Nullable
	@Override
	public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {

		// Find relevant tag
		ItemTag relevant = ItemTag.getTagAtPath(getNBTPath(), storedTags);

		// Found it?
		if (relevant != null) {

			// Attempt to parse it
			try {

				// New data <3
				CommandListData commands = new CommandListData();

				// Parse array from it
				JsonArray ar = new JsonParser().parse((String) relevant.getValue()).getAsJsonArray();

				// Examine every element
				for (JsonElement e : ar) {

					// Valid?
					if (e.isJsonObject()) {

						// Get Key
						JsonObject key = e.getAsJsonObject();

						// Interpret Command Data
						CommandData cd = new CommandData(
								key.get("Command").getAsString(),
								key.get("Delay").getAsDouble(),
								key.get("Console").getAsBoolean(),
								key.get("Op").getAsBoolean());

						// Register
						commands.add(cd);
					}
				}
				return commands;

			// Needs updating
			} catch (JsonSyntaxException|IllegalStateException exception) {
				/*
				 * OLD ITEM WHICH MUST BE UPDATED.
				 */
			}
		}

		return null;
	}
}
