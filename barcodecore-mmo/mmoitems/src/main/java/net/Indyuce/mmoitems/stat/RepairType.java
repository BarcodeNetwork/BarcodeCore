package net.Indyuce.mmoitems.stat;

import net.Indyuce.mmoitems.stat.type.StringStat;
import org.bukkit.Material;

public class RepairType extends StringStat {
	public RepairType() {
		super("REPAIR_TYPE", Material.ANVIL, "Repair Type", new String[]{"If items have a repair type they can", "only be repaired by consumables", "with the same repair type.", "(And vice-versa)"}, new String[]{"all"});
	}
}
