package net.Indyuce.mmoitems.stat.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.edition.StatEdition;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.StringListData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.util.AltChar;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StringListStat extends ItemStat {
    public StringListStat(String id, Material mat, String name, String[] lore, String[] types, Material... materials) {
        super(id, mat, name, lore, types, materials);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StringListData whenInitialized(Object object) {
        Validate.isTrue(object instanceof List<?>, "Must specify a string list");
        return new StringListData((List<String>) object);
    }

    @Override
    public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {

        // Empty stuff
        if (!(data instanceof StringListData)) { return; }
        if (((StringListData) data).getList().size() == 0) { return; }

        // Chop
        String joined = String.join(", ", ((StringListData) data).getList());
        String format = MMOItems.plugin.getLanguage().getStatFormat(getPath());
        String finalStr = format.replace("#", joined);

        // Identify colour
        StringBuilder col = new StringBuilder(""); int pnd = format.indexOf('#');
        if (pnd > 0) {

            // Everything before thay pound
            String input = format.substring(0, pnd);
            int length = input.length();

            for(int index = length - 1; index > -1; --index) {

                // Observe char
                char section = input.charAt(index);

                boolean isSection = (section == '\u00a7' || section == '&');
                boolean isAngle = (section == '<');

                // Is there at least one char, as for it to be a color code
                if (isSection && index < (length - 1)) {

                    // Observe next character
                    char c = input.charAt(index + 1);
                    ChatColor color = ChatColor.getByChar(c);

                    // Was it a color code?
                    if (color != null) {

                        // That's our color
                        col.insert(0, color.toString());

                        // If its a reset or a color character, that's the end
                        if (color.isColor() || color == ChatColor.RESET) {
                            break;
                        }
                    }

                // If there is at least 10 chars, as for it to complete HEX######>
                } else if(isAngle && index < (length - 10)) {

                    // Observe tenth characters
                    char aC = input.charAt(index + 10);

                    // Closing bracket
                    if (aC == '>') {

                        // Observe hex
                        char lH = input.charAt(index + 1);
                        char lE = input.charAt(index + 2);
                        char lX = input.charAt(index + 3);
                        if (lH == 'H' && lE == 'E' && lX == 'X') {

                            // Get hex
                            char c1 = input.charAt(index + 4);
                            char c2 = input.charAt(index + 5);
                            char c3 = input.charAt(index + 6);
                            char c4 = input.charAt(index + 7);
                            char c5 = input.charAt(index + 8);
                            char c6 = input.charAt(index + 9);

                            col.insert(0, '>')
                                .insert(0, c6)
                                .insert(0, c5)
                                .insert(0, c4)
                                .insert(0, c3)
                                .insert(0, c2)
                                .insert(0, c1)
                                .insert(0, "<HEX");

                            // Um yes that qualifies as a color code
                            break;
                        }
                    }
                }
            }
        }

        // Display in lore
        item.getLore().insert(getPath(), SilentNumbers.chop(finalStr, 50, col.toString()));

        // Apply yes
        item.addItemTag(getAppliedNBT(data));
    }

    @NotNull
    @Override
    public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {

        // Start out with a new JSON Array
        JsonArray array = new JsonArray();

        // For every list entry
        for (String str : ((StringListData) data).getList()) {

            // Add to the array as-is
            array.add(str);
        }

        // Make the result list
        ArrayList<ItemTag> ret = new ArrayList<>();

        // Add the Json Array
        ret.add(new ItemTag(getNBTPath(), array.toString()));

        // Ready.
        return ret;
    }

    @Override
    public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.PICKUP_ALL)
            new StatEdition(inv, this).enable("Write in the chat the line you want to add.");

        if (event.getAction() == InventoryAction.PICKUP_HALF && inv.getEditedSection().contains(getPath())) {
            List<String> list = inv.getEditedSection().getStringList(getPath());
            if (list.isEmpty())
                return;

            String last = list.get(list.size() - 1);
            list.remove(last);
            inv.getEditedSection().set(getPath(), list.isEmpty() ? null : list);
            inv.registerTemplateEdition();
            inv.getPlayer()
                    .sendMessage(MMOItems.plugin.getPrefix() + "Successfully removed '" + MythicLib.plugin.parseColors(last) + ChatColor.GRAY + "'.");
        }
    }

    @Override
    public void whenInput(@NotNull EditionInventory inv, @NotNull String message, Object... info) {
        List<String> list = inv.getEditedSection().contains(getPath()) ? inv.getEditedSection().getStringList(getPath()) : new ArrayList<>();
        list.add(message);
        inv.getEditedSection().set(getPath(), list);
        inv.registerTemplateEdition();
        inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + getName() + " Stat successfully added.");
    }

    @Override
    public void whenLoaded(@NotNull ReadMMOItem mmoitem) {

        // Find the relevant tags
        ArrayList<ItemTag> relevantTags = new ArrayList<>();
        if (mmoitem.getNBT().hasTag(getNBTPath()))
            relevantTags.add(ItemTag.getTagAtPath(getNBTPath(), mmoitem.getNBT(), SupportedNBTTagValues.STRING));

        // Generate data
        StatData data = getLoadedNBT(relevantTags);

        // Valid?
        if (data != null) { mmoitem.setData(this, data); }
    }

    @Nullable
    @Override
    public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {

        // Get it
        ItemTag listTag = ItemTag.getTagAtPath(getNBTPath(), storedTags);

        // Found?
        if (listTag != null) {
            try {

                // Value must be a Json Array
                JsonArray array = new JsonParser().parse((String) listTag.getValue()).getAsJsonArray();

                // Create String List Data
                return new StringListData(array);

            } catch (JsonSyntaxException |IllegalStateException exception) {
                /*
                 * OLD ITEM WHICH MUST BE UPDATED.
                 */
            }
        }

        // No correct tags
        return null;
    }

    @Override
    public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {
        if (statData.isPresent()) {
            lore.add(ChatColor.GRAY + "Current Value:");
            StringListData data = (StringListData) statData.get();
            data.getList().forEach(element -> lore.add(ChatColor.GRAY + MythicLib.plugin.parseColors(element)));

        } else
            lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.RED + "None");

        lore.add("");
        lore.add(ChatColor.YELLOW + AltChar.listDash + " Click to add a permission.");
        lore.add(ChatColor.YELLOW + AltChar.listDash + " Right click to remove the last permission.");
    }

    @NotNull
    @Override
    public StatData getClearStatData() {
        return new StringListData();
    }
}
