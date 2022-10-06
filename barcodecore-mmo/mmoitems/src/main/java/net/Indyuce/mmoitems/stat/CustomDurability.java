package net.Indyuce.mmoitems.stat;

import io.lumine.mythic.lib.api.item.ItemTag;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.InternalStat;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Internal stat used to save the item's current custom durability.
 * <p>
 * This fixes a bug where every time the player would apply a gem,
 * or basically rebuild the item using MMOItemBuilder, the custom
 * durability bar would always reset to 100%.
 * <p>
 * Items with no MMOITEMS_DURABILITY tag yet means that they still
 * have full durability
 *
 * @author indyuce
 */
public class CustomDurability extends DoubleStat implements InternalStat {
	public CustomDurability() {
		super("DURABILITY", Material.SHEARS, "Custom Durability", new String[0], new String[]{"!block", "all"});
	}

	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {
		int current = (int) ((DoubleData) data).getValue();
		if (current != 0)

			// Shortcut oui oui
			item.addItemTag(new ItemTag(getNBTPath(), current));
	}

/*	@Override
	public String getDynamicLoreId() {
		return getPath();
	}

	@Override
	public String calculatePlaceholder(NBTItem item) {

		int max = (int) item.getInteger("MMOITEMS_MAX_DURABILITY");
		int current = item.hasTag("MMOITEMS_DURABILITY") ? item.getInteger("MMOITEMS_DURABILITY") : max;

		return MMOItems.plugin.getLanguage().getStatFormat(ItemStats.CUSTOM_DURABILITY.getPath()).replace("#c", "" + current).replace("#m", "" + max);
	}*/
}
