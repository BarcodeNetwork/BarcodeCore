package net.Indyuce.mmoitems.stat.block;

import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import io.lumine.mythic.lib.api.item.ItemTag;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BlockID extends DoubleStat {

    public BlockID() {
        super("BLOCK_ID", Material.STONE, "Block ID", new String[] { "This value determines which", "custom block will get placed." }, new String[] { "block" });
    }

    @Override
    public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {
        super.whenApplied(item, data);
        item.addItemTag(new ItemTag("CustomModelData", (int) ((DoubleData) data).getValue() +1000));
    }

    @Override
    public void whenPreviewed(@NotNull ItemStackBuilder item, @NotNull StatData currentData, @NotNull RandomStatData templateData) throws IllegalArgumentException { whenApplied(item, currentData); }
}
