package net.Indyuce.mmoitems.stat.type;

import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Choose Stats present a list of options from which the user may choose one.
 * <p></p>
 * You could consider them a more advanced DisableStat, while DisableStat only
 * allows to choose <b>true</b> or <b>false</b>, alternating when clicked, Choose
 * Stats cycle through a list instead.
 *
 * @author Gunging
 */
public abstract class ChooseStat extends StringStat {

    /**
     * Contains the list of different options the player may choose from.
     * <b>Make sure its is always initialized and with at least 1 element</b>
     */
    private final ArrayList<String> choices = new ArrayList<>();

    /**
     * Definitions of what each choosing type does, for display in lore.
     */
    private final HashMap<String, String> hints = new HashMap<>();

    public ChooseStat(String id, Material mat, String name, String[] lore, String[] types, Material... materials) {
        super(id, mat, name, lore, types, materials);
    }

    public void addChoices(String... choices) {
        this.choices.addAll(Arrays.asList(choices));
    }

    public void setHint(String choice, String desc) {
        hints.put(choice, desc);
    }

    @Override
    public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
        Validate.isTrue(choices.size() > 0, "\u00a77Invalid Chooseable Item Stat " + ChatColor.GOLD + getId() + "\u00a77' - \u00a7cNo options to choose from.");

        // If removing, reset to default
        if (event.getAction() == InventoryAction.PICKUP_HALF) {

            // Edits into persistent files
            inv.getEditedSection().set(getPath(), null);
            inv.registerTemplateEdition();

            // Mention that it was removed
            inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Successfully removed " + getName() + ".");

        } else {

            // Get current
            String current = inv.getEditedSection().getString(getPath());

            // Included?
            int currentIndex = 0;
            if (current != null && choices.contains(current)) {
                currentIndex = choices.indexOf(current);
            }

            // Increase and Cap
            currentIndex++;
            if (currentIndex >= choices.size()) {
                currentIndex = 0;
            }

            // Get
            current = choices.get(currentIndex);

            // Edits into persistent files
            inv.getEditedSection().set(getPath(), current);
            inv.registerTemplateEdition();

            // Sends a message
            inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + getName() + " successfully changed to " + current + ChatColor.GRAY + ".");
        }
    }

    @Override
    public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {
        Validate.isTrue(choices.size() > 0, "\u00a77Invalid Chooseable Item Stat " + ChatColor.GOLD + getId() + "\u00a77' - \u00a7cNo options to choose from.");

        // To display current choosing, gets the very first element
        String def = choices.get(0);

        // Does this item have any specified value for this?
        if (statData.isPresent()) {

            // Put in current
            def = statData.get().toString();

            // Display the value of the current stat data
            lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.GREEN + def);

        } else {

            // Mention that it currently has no value
            lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.RED + def);
        }

        // Get Definition
        if (hints.containsKey(def))
            for (String definition : SilentNumbers.chop(hints.get(def), 50, "")) {
                lore.add(ChatColor.GRAY + "   " + definition);
            }

        lore.add("");
        lore.add(ChatColor.YELLOW + AltChar.listDash + " Right click to return to default value.");
        lore.add(ChatColor.YELLOW + AltChar.listDash + " Left click to cycle through the available options:");
        for (String str : choices) {

            // Is it the one?
            String pick = ChatColor.GOLD.toString();
            if (str.equals(def)) {
                pick = ChatColor.RED.toString() + ChatColor.BOLD.toString();
            }

            lore.add(pick + "  " + AltChar.smallListDash + " \u00a77" + str);
        }
    }
}
