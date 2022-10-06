package io.lumine.mythic.lib.gui;

import io.lumine.mythic.lib.api.explorer.AttributeData;
import io.lumine.mythic.lib.api.explorer.ChatInput;
import io.lumine.mythic.lib.api.explorer.ItemBuilder;
import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.commands.mmolib.ExploreAttributesCommand;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AttributeExplorer extends PluginInventory {
    private final Player target;

    /*
     * explored attribute
     */
    private Attribute explored;
    private List<AttributeModifier> modifiers;
    private int page;

    private static final int[] slots = { 8, 17, 26, 35, 44, 53, 7, 16, 25, 34, 43, 52 };
    private static final int[] modifierSlots = { 19, 20, 21, 22, 23, 28, 29, 30, 31, 32, 37, 38, 39, 40, 41 };

    public AttributeExplorer(Player player, Player target) {
        super(player);
        Validate.notNull(target, "Target cannot be null");
        this.target = target;
    }

    public Attribute getExplored() {
        return explored;
    }

    public Player getTarget() {
        return target;
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 54, explored == null ? "Attributes from " + target.getName() : "Exploring: " + getName(explored) + " (" + (page + 1) + ")");

        inv.setItem(3, new ItemBuilder(Material.WHITE_BED, "&6Refresh &8(Click)"));

        int j = 0;
        for (Attribute attribute : Attribute.values()) {
            AttributeInstance ins = target.getAttribute(attribute);
            if (ins == null)
                continue;

            String key = attribute.name().substring("GENERIC_".length());
            if (!ExploreAttributesCommand.data.containsKey(key))
                continue;

            AttributeData data = ExploreAttributesCommand.data.get(key);

            ItemStack item = data.getIcon();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + ">> " + getName(attribute));
            meta.addItemFlags(ItemFlag.values());

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + data.getDescription());
            lore.add("");
            lore.add(ChatColor.GRAY + "Base Value: " + ChatColor.GOLD + ExploreAttributesCommand.format.format(ins.getBaseValue()));
            lore.add(ChatColor.GRAY + "Default Value: " + ChatColor.GOLD + ExploreAttributesCommand.format.format(ins.getDefaultValue()));
            lore.add(ChatColor.GRAY + "Total Value: " + ChatColor.GOLD + ChatColor.BOLD + ExploreAttributesCommand.format.format(ins.getValue()));
            lore.add("");
            lore.add(ChatColor.GRAY + "Attribute Modifiers: " + ChatColor.GOLD + ins.getModifiers().size());
            lore.add(ChatColor.GRAY + "Due to Modifiers: " + ChatColor.GOLD + ExploreAttributesCommand.format.format(ins.getValue() - ins.getBaseValue()));
            lore.add("");
            lore.add(ChatColor.YELLOW + AltChar.smallListDash + " Left click to explore.");
            lore.add(ChatColor.YELLOW + AltChar.smallListDash + " Right click to set the base value.");
            lore.add(ChatColor.YELLOW + AltChar.smallListDash + " Shift click to reset base value.");

            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(slots[j++], item);
        }

        ItemStack fillAttribute = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "&cNo Attribute");
        while (j < slots.length)
            inv.setItem(slots[j++], fillAttribute);

        if (explored != null) {

            inv.setItem(1, new ItemBuilder(Material.WRITABLE_BOOK, "&6New Attribute.."));
            inv.setItem(5, new ItemBuilder(Material.BARRIER, "&6" + AltChar.rightArrow + " Back"));

            final int min = page * modifierSlots.length, max = (page + 1) * modifierSlots.length;
            int k = min;

            while (k < Math.min(modifiers.size(), max)) {
                AttributeModifier modifier = modifiers.get(k);

                ItemStack item = new ItemStack(Material.GRAY_DYE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GOLD + "Modifier n" + (k + 1));

                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatColor.GRAY + "Name: " + ChatColor.GOLD + modifier.getName());
                lore.add(ChatColor.GRAY + "Amount: " + ChatColor.GOLD + modifier.getAmount());
                lore.add(ChatColor.GRAY + "Operation: " + ChatColor.GOLD + modifier.getOperation());
                lore.add("");
                lore.add(ChatColor.GRAY + "Slot: " + ChatColor.GOLD + (modifier.getSlot() == null ? "None" : modifier.getSlot().name()));
                lore.add(ChatColor.GRAY + "ID: " + ChatColor.GOLD + modifier.getUniqueId());
                lore.add("");
                lore.add(ChatColor.YELLOW + AltChar.smallListDash + " Right click to remove.");

                meta.setLore(lore);
                item.setItemMeta(meta);
                inv.setItem(modifierSlots[k++ - min], item);
            }

            if (modifiers.size() > max)
                inv.setItem(33, new ItemBuilder(Material.ARROW, "&6Next Page"));

            if (page > 0)
                inv.setItem(27, new ItemBuilder(Material.ARROW, "&6Previous Page"));

            ItemStack fillModifier = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "&cNo Modifier");
            while (k < max)
                inv.setItem(modifierSlots[k++ - min], fillModifier);
        }

        return inv;
    }

    public void setExplored(Attribute attribute) {
        explored = attribute;
        modifiers = explored != null ? new ArrayList<>(target.getAttribute(explored).getModifiers()) : null;
    }

    private String getName(Attribute attribute) {
        return caseOnWords(attribute.name().substring("GENERIC_".length()).toLowerCase().replace("_", " "));
    }

    @Override
    public void whenClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!event.getInventory().equals(event.getClickedInventory()))
            return;

        ItemStack item = event.getCurrentItem();
        if (!ExploreAttributesCommand.isMetaItem(item))
            return;

        if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Refresh " + ChatColor.DARK_GRAY + "(Click)")) {
            setExplored(explored);
            open();
            return;
        }

        if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + AltChar.rightArrow + " Back")) {
            setExplored(null);
            open();
            return;
        }

        if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Next Page")) {
            page++;
            open();
            return;
        }

        if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Previous Page")) {
            page--;
            open();
            return;
        }

        if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "New Attribute..")) {
            new AttributeCreator(this).open();
            return;
        }

        if (item.getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Modifier n") && event.getAction() == InventoryAction.PICKUP_HALF) {
            int index = Integer.parseInt(item.getItemMeta().getDisplayName().substring((ChatColor.GOLD + "Modifier n").length())) - 1;
            target.getAttribute(explored).removeModifier(modifiers.get(index));
            getPlayer().sendMessage(ChatColor.YELLOW + "> Modifier n" + (index + 1) + " successfully deleted.");
            setExplored(explored);
            open();
            return;
        }

        if (item.getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + ">> ")) {
            Attribute attribute = Attribute.valueOf("GENERIC_" + item.getItemMeta().getDisplayName().substring((ChatColor.GOLD + ">> ").length()).toUpperCase().replace(" ", "_"));

            if (event.getAction() == InventoryAction.PICKUP_ALL) {
                setExplored(attribute);
                open();
            }

            else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                target.getAttribute(attribute).setBaseValue(target.getAttribute(attribute).getDefaultValue());
                getPlayer().sendMessage(ChatColor.YELLOW + "> Base value of " + ChatColor.GOLD + attribute.name() + ChatColor.YELLOW + " successfully reset.");
                open();

            } else if (event.getAction() == InventoryAction.PICKUP_HALF) {

                getPlayer().closeInventory();
                getPlayer().sendMessage(ChatColor.YELLOW + "> Write in the chat the value you want.");
                new ChatInput(getPlayer(), (output) -> {

                    if (output == null) {
                        open();
                        return true;
                    }

                    double d;
                    try {
                        d = Double.parseDouble(output);
                    } catch (NumberFormatException exception) {
                        getPlayer().sendMessage(ChatColor.RED + "> " + output + " is not a valid number. Type 'cancel' to cancel.");
                        return false;
                    }

                    getPlayer().sendMessage(ChatColor.YELLOW + "> Base value set to " + ChatColor.GOLD + ExploreAttributesCommand.format.format(d) + ChatColor.YELLOW + ".");
                    target.getAttribute(attribute).setBaseValue(d);
                    open();
                    return true;
                });
            }
        }
    }

    private String caseOnWords(String s) {
        StringBuilder builder = new StringBuilder(s);
        boolean isLastSpace = true;
        for (int item = 0; item < builder.length(); item++) {
            char ch = builder.charAt(item);
            if (isLastSpace && ch >= 'a' && ch <= 'z') {
                builder.setCharAt(item, (char) (ch + ('A' - 'a')));
                isLastSpace = false;
            } else isLastSpace = ch == ' ';
        }
        return builder.toString();
    }
}
