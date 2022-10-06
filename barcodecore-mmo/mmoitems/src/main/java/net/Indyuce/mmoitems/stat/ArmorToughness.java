package net.Indyuce.mmoitems.stat;

import net.Indyuce.mmoitems.stat.type.AttributeStat;
import io.lumine.mythic.lib.api.item.ItemTag;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

public class ArmorToughness extends AttributeStat {
	public ArmorToughness() {
		super("ARMOR_TOUGHNESS", Material.DIAMOND_CHESTPLATE, "Armor Toughness",
				new String[] { "Armor toughness reduces damage taken." }, Attribute.GENERIC_ARMOR_TOUGHNESS);
	}
}
