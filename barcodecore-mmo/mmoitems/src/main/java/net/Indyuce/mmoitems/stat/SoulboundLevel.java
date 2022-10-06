package net.Indyuce.mmoitems.stat;

import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.version.VersionMaterial;
import org.jetbrains.annotations.NotNull;

public class SoulboundLevel extends DoubleStat {
	public SoulboundLevel() {
		super("SOULBOUND_LEVEL", VersionMaterial.ENDER_EYE.toMaterial(), "Soulbinding Level", new String[] { "The soulbound level defines how much", "damage players will take when trying", "to use a soulbound item. It also determines", "how hard it is to break the binding." }, new String[] { "consumable" });
	}

	// writes soulbound level with roman writing in lore
	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {
		int value = (int) ((DoubleData) data).getValue();
		item.addItemTag(new ItemTag("MMOITEMS_SOULBOUND_LEVEL", value));
		item.getLore().insert("soulbound-level", formatNumericStat(value, "#", MMOUtils.intToRoman(value)));
	}
	@Override
	public void whenPreviewed(@NotNull ItemStackBuilder item, @NotNull StatData currentData, @NotNull RandomStatData templateData) throws IllegalArgumentException { whenApplied(item, currentData);}
}
