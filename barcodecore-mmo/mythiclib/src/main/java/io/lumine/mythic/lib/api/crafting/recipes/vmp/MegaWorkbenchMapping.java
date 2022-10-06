package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeStation;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.utils.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The 6x6 grid of a mega crafting table, and its result slot.
 * <p></p>
 * Slot definitions:
 * <p></p>
 * Inventory 1 (Main)
 * <p> #1  TOP LEFT
 * </p>#2  TOP MID-LEFT
 * <p> #3  TOP CENTER
 * </p>#4  TOP MID-RIGHT
 * <p> #5  TOP RIGHT
 * </p>#6  EXTRA RIGHT
 *
 * <p> #7  TMD LEFT
 * </p>#8  TMD MID-LEFT
 * <p> #9  TMD CENTER
 * </p>#10 TMD MID-RIGHT
 * <p> #11 TMD RIGHT
 * </p>#12 TMD EXTRA RIGHT
 *
 * <p> #13 MID LEFT
 * </p>#14 MID MID-LEFT
 * <p> #15 MID CENTER
 * </p>#16 MID MID-RIGHT
 * <p> #17 MID RIGHT
 * </p>#18 MID EXTRA RIGHT
 *
 * <p> #19 BID LEFT
 * </p>#20 BID MID-LEFT
 * <p> #21 BID CENTER
 * </p>#22 BID MID-RIGHT
 * <p> #23 BID RIGHT
 * </p>#24 BID EXTRA RIGHT
 *
 * <p> #25 BOT LEFT
 * </p>#26 BOT MID-LEFT
 * <p> #27 BOT CENTER
 * </p>#28 BOT MID-RIGHT
 * <p> #29 BOT RIGHT
 * </p>#30 BOT EXTRA RIGHT
 *
 * <p> #31 BEX LEFT
 * </p>#32 BEX MID-LEFT
 * <p> #33 BEX CENTER
 * </p>#34 BEX MID-RIGHT
 * <p> #35 BEX RIGHT
 * </p>#36 BEX EXTRA RIGHT
 *
 * </p>
 * <p></p>
 * <p>Inventory 2 (Result)
 * </p> #0 RESULT
 *
 * @author Gunging
 */
public class MegaWorkbenchMapping  extends VanillaInventoryMapping implements CustomInventoryCheck, CommandExecutor, Listener {

    @Override
    public int getMainWidth(int slot) throws IllegalArgumentException {
        switch (slot) {
            case 0:
            case 9:
            case 18:
            case 27:
            case 36:
            case 45:
                return 0;
            case 1:
            case 10:
            case 19:
            case 28:
            case 37:
            case 46:
                return 1;
            case 2:
            case 11:
            case 20:
            case 29:
            case 38:
            case 47:
                return 2;
            case 3:
            case 12:
            case 21:
            case 30:
            case 39:
            case 48:
                return 3;
            case 4:
            case 13:
            case 22:
            case 31:
            case 40:
            case 49:
                return 4;
            case 5:
            case 14:
            case 23:
            case 32:
            case 41:
            case 50:
                return 5;
            default: break; }

        // Skip trash
        if (slot < 50) { return -1; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getMainHeight(int slot) throws IllegalArgumentException {
        switch (slot) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return 0;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return -1;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                return -2;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
                return -3;
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
                return -4;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
                return -5;
            default: break; }

        // Skip trash
        if (slot < 50) { return 1; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getMainSlot(int width, int height) throws IllegalArgumentException {
        /*
         * Don't be thrown off by seeing negative height in these slot
         * numbers, it is kind of inverted, this is how it looks:
         *
         * [0] [0] [0] [0] [0]  This is height 0
         * [1] [1] [1] [1] [1]  This is height -1
         * [2] [2] [2] [2] [2]  This is height -2
         * [3] [3] [3] [3] [3]  This is height -3
         * [4] [4] [4] [4] [4]  This is height -4
         *
         * The horizontal number is the normal, intuitive though.
         * [0] [1] [2] [3] [4] This is height 0
         * [0] [1] [2] [3] [4] This is height -1
         * [0] [1] [2] [3] [4] This is height -2
         * [0] [1] [2] [3] [4] This is height -3
         * [0] [1] [2] [3] [4] This is height -4
         */
        switch (height) {
            case 0:
                switch (width) {
                    case 0: return 0;
                    case 1: return 1;
                    case 2: return 2;
                    case 3: return 3;
                    case 4: return 4;
                    case 5: return 5;
                    default: break; }
            case -1:
                switch (width) {
                    case 0: return 9;
                    case 1: return 10;
                    case 2: return 11;
                    case 3: return 12;
                    case 4: return 13;
                    case 5: return 14;
                    default: break; }
            case -2:
                switch (width) {
                    case 0: return 18;
                    case 1: return 19;
                    case 2: return 20;
                    case 3: return 21;
                    case 4: return 22;
                    case 5: return 23;
                    default: break; }
            case -3:
                switch (width) {
                    case 0: return 27;
                    case 1: return 28;
                    case 2: return 29;
                    case 3: return 30;
                    case 4: return 31;
                    case 5: return 32;
                    default: break; }
            case -4:
                switch (width) {
                    case 0: return 36;
                    case 1: return 37;
                    case 2: return 38;
                    case 3: return 39;
                    case 4: return 40;
                    case 5: return 41;
                    default: break; }
            case -5:
                switch (width) {
                    case 0: return 45;
                    case 1: return 46;
                    case 2: return 47;
                    case 3: return 48;
                    case 4: return 49;
                    case 5: return 50;
                    default: break; }
            default: break; }

        throwOutOfBounds(width, height); return 0;
    }
    @Override
    public int getMainInventoryStart() { return 0; }
    @Override
    public int getMainInventorySize() { return 36; }
    @Override
    public int getMainInventoryWidth() { return 6; }
    @Override
    public int getMainInventoryHeight() { return 6; }

    @Override
    public int getResultWidth(int slot) throws IllegalArgumentException {
        if (slot == 25) { return 0; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getResultHeight(int slot) throws IllegalArgumentException {
        if (slot == 25) { return 0; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getResultSlot(int width, int height) throws IllegalArgumentException {
        if (width == 0 && height == 0) { return 25; }

        throwOutOfBounds(width, height); return 0;
    }
    @Override
    public int getResultInventoryStart() { return 25; }
    @Override
    public int getResultInventorySize() { return 1; }
    @Override
    public int getResultInventoryWidth() { return 1; }
    @Override
    public int getResultInventoryHeight() { return 1; }
    @Override public boolean isResultSlot(int slot) { return slot == 25; }

    @Override
    public int getSideWidth(@NotNull String side, int slot) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideHeight(@NotNull String side, int slot) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideSlot(@NotNull String side, int width, int height) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventoryStart(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventorySize(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventoryWidth(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventoryHeight(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }

    @Override
    public boolean applyToSideInventory(@NotNull Inventory inventory, @NotNull MythicRecipeInventory finalSide, @NotNull String sideKeyName, boolean amountOnly) { return false; }

    @Override public boolean mainIsResult() { return false; }
    @Nullable
    @Override public MythicRecipeStation getIntendedStation() { return MythicRecipeStation.WORKBENCH; }
    @NotNull @Override public InventoryType getIntendedInventory() { return InventoryType.CHEST; }
    @NotNull @Override public ArrayList<String> getSideInventoryNames() { return sNames; }
    @NotNull final static ArrayList<String> sNames = new ArrayList<>();

    /**
     * The mythical identifier that is appended at the beginning of
     * the name of the inventory view to identify it as the SWorkbench!
     */
    public static final String MEGA_WORKBENCH = "\u00a7g\u00a7w\u00a7°\u00a7¡\u00a7v";

    @Override
    public boolean IsTargetInventory(@NotNull InventoryView view) {

        // Check size alv
        if (view.getTopInventory().getSize() != 54) { return false; }

        return view.getTitle().startsWith(MEGA_WORKBENCH);
    }

    @NotNull
    @Override
    public String getCustomStationKey() { return "mwb"; }

    public static MegaWorkbenchMapping MWB = new MegaWorkbenchMapping();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // All right, lets identify our args
        Player target = null;
        boolean failure = false;
        FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
        ffp.activatePrefix(true, "Mega Workbench");

        // No args specified, well oops
        if (args.length == 0) {

            // Command Sender applicable?
            if (sender instanceof Player) {

                // That shall be the target
                target = (Player) sender;

                // Failure
            } else {

                // RIP
                failure = true;

                // Add fail message
                ffp.log(FriendlyFeedbackCategory.ERROR, "$fYou must specify a player when calling from the console. $bBy the way, you can download the free texture pack assets to make the glass borders look smooth at https://sites.google.com/view/gootilities/core-plugin-goop/containers/container-templates/edge-formations?authuser=0");
            }

            // Find that player
        } else {

            // Identify name
            String name = args[0];

            // Can you get a UUID from it?
            UUID possibleUUID = SilentNumbers.UUIDParse(name);
            if (possibleUUID != null) { target = Bukkit.getPlayer(possibleUUID); }

            // Still null?
            if (target == null) {

                // First attempt as get player exact o/
                target = Bukkit.getPlayerExact(name);

                // Still null?
                if (target == null) {

                    // RIP
                    failure = true;

                    // Add fail message
                    ffp.log(FriendlyFeedbackCategory.ERROR, "Player $i{0}$b not found.", name);
                }
            }
        }

        // All right, ready?
        if (!failure) {

            // Open that station to them
            Inventory swb = getSuperWorkbench(target);

            // Open it
            target.openInventory(swb);

            // Log errors
        } else {

            // Log messages
            if (sender instanceof Player) { ffp.sendAllTo((Player) sender); } else { ffp.sendAllTo(sender); }
        }

        // :gruno:
        return false;
    }

    /**
     * The edge item used to limit the Super Workbench Area
     */
    public static final ItemStack MWB_EDGE = ItemFactory.of(Material.GRAY_STAINED_GLASS_PANE).name("\u00a7\u02a4").flag(ItemFlag.HIDE_POTION_EFFECTS).model(3000).build();

    /**
     * Creates a new instance of the super workbench, ready
     * to be opened for a player
     *
     * @param player Player who will own it
     *
     * @return A super workbench ready to use
     */
    public static Inventory getSuperWorkbench(@NotNull Player player) {

        // Create fresh
        Inventory swb = Bukkit.createInventory(player, 54, MEGA_WORKBENCH + InventoryType.WORKBENCH.getDefaultTitle());

        // Use chad glass panes
        for (Integer i : getEdgeSlots()) { swb.setItem(i, MWB_EDGE);}

        // That's it, yeah
        return swb;
    }

    /**
     * The slots that are considered 'Edge' and are completely uninteractable
     */
    static ArrayList<Integer> edgeSlots = generateEdgeSlots();
    /**
     * @return Build the slots that are considered 'Edge' and are completely uninteractable
     */
    static ArrayList<Integer> generateEdgeSlots() {
        ArrayList<Integer> ret = new ArrayList<>();

        // Use chad glass panes
        ret.add(6);
        ret.add(15);
        ret.add(24);
        ret.add(33);
        ret.add(42);
        ret.add(51);
        //
        ret.add(7);
        ret.add(16);

        ret.add(34);
        ret.add(43);
        ret.add(52);
        //
        ret.add(8);
        ret.add(17);
        ret.add(26);
        ret.add(35);
        ret.add(44);
        ret.add(53);

        // Yes
        return ret;
    }
    /**
     * @return The slots that are considered 'Edge' and are completely uninteractable
     */
    public static ArrayList<Integer> getEdgeSlots() { return edgeSlots; }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnClick(InventoryClickEvent event) {

        // Easiest way to quit this method - is the size 5x9 = 45 slots?
        int size = event.getView().getTopInventory().getSize();
        if (size != 54) { return; }

        // Bruh what
        if (!(event.getView().getPlayer() instanceof Player)) { return; }

        // All right lets continue
        InventoryAction act = event.getAction();

        // Is this inventory the super workbench
        if (!IsTargetInventory(event.getView())) { return; }

        // Not a click on the top inventory, probably not our business
        if (event.getRawSlot() >= event.getView().getTopInventory().getSize()) {

            // Weird move to other inventory glitch
            if (act == InventoryAction.MOVE_TO_OTHER_INVENTORY && event.getCurrentItem() != null) {

                // Will have to be manual
                event.setCancelled(true);

                // Find available slots
                ItemStack item = event.getCurrentItem();
                ArrayList<Integer> vibes = new ArrayList<>();
                int lastAir = -1;

                // Iterate through main inventory slots, find the first 'viable' slot
                for (int h = 0; h < getMainInventoryHeight(); h++) {
                    for (int w = 0; w < getMainInventoryWidth(); w++) {

                        // Main Slot
                        int sl = getMainSlot(w, -h);

                        // Fill row
                        ItemStack observed = get(event.getView().getTopInventory(), sl);
                        boolean aero = SilentNumbers.isAir(observed);

                        // Is it air? or stackable?
                        if (aero || (observed.isSimilar(item) && observed.getAmount() < observed.getType().getMaxStackSize())) {

                            // Include as viable
                            if (!aero) { vibes.add(sl); }

                            // Break on air
                            if (aero && lastAir < 0) { lastAir = sl; }
                        }
                    }
                }

                // Include ok ~ last check such that its the last considered by the code below
                if (lastAir >= 0) { vibes.add(lastAir); }

                // Found viable?
                boolean completion = false;
                ItemStack viableStacker = event.getCurrentItem().clone();
                for (Integer v : vibes) {

                    // Add all amount
                    ItemStack observed = event.getView().getTopInventory().getItem(v);

                    // Game
                    if (SilentNumbers.isAir(observed)) {

                        // Put remaining item there
                        event.getCurrentItem().setAmount(0);
                        event.getView().getTopInventory().setItem(v, viableStacker);
                        completion = true;
                        break;

                    // Split amount
                    } else {

                        // How many can it take
                        int remainder = observed.getType().getMaxStackSize() - observed.getAmount();
                        int vsAmount = viableStacker.getAmount();

                        // Can fit the entire thing
                        if (remainder >= viableStacker.getAmount()) {

                            // Erase viable stacker
                            viableStacker.setAmount(0);
                            event.getCurrentItem().setAmount(0);
                            observed.setAmount(observed.getAmount() + vsAmount);
                            event.getView().getTopInventory().setItem(v, observed);
                            completion = true;
                            break;

                            // Can fit part
                        } else {

                            // Transfer amounts yoi
                            viableStacker.setAmount(vsAmount - remainder);
                            observed.setAmount(observed.getAmount() + remainder);
                            event.getView().getTopInventory().setItem(v, observed);
                        }
                    }
                }

                // Did not reach to completion
                if (!completion) {

                    // Update amount
                    event.setCurrentItem(viableStacker);
                }

            } else { return; }
        }

        // Ok just block the edge slots
        if (getEdgeSlots().contains(event.getSlot())) {

            // Block that shit
            event.setCancelled(true);
        }

        // Is it the damn result slot? Trying to put anything in there? no hell no
        if (event.getSlot() == 25 &&
                act != InventoryAction.PICKUP_ALL &&
                act != InventoryAction.MOVE_TO_OTHER_INVENTORY) { event.setCancelled(true); }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnClick(InventoryDragEvent event) {

        // Easiest way to quit this method - is the size 5x9 = 45 slots?
        int size = event.getView().getTopInventory().getSize();
        if (size != 54) { return; }

        // Bruh what
        if (!(event.getView().getPlayer() instanceof Player)) { return; }

        // Is this inventory the super workbench
        if (!IsTargetInventory(event.getView())) { return; }

        // Cancel if result slot is interacted-with
        for (Integer i : event.getRawSlots()) { if (i == 25) { event.setCancelled(true); return; } }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnInvenClose(InventoryCloseEvent event) {

        // Easiest way to quit this method - is the size 5x9 = 45 slots?
        int size = event.getView().getTopInventory().getSize();
        if (size != 54) { return; }

        // Bruh what
        if (!(event.getView().getPlayer() instanceof Player)) { return; }

        // Is this inventory the super workbench
        if (!IsTargetInventory(event.getView())) { return; }

        // Ok we must gather all items and drop them to the ground
        int z = 0;

        // Drops
        ArrayList<ItemStack> drops = new ArrayList<>();

        // For every main slot
        for (int s = getMainInventoryStart(); s < (getMainInventorySize() + getMainInventoryStart() + z); s++) {

            // Read
            int w = getMainWidth(s);
            int h = getMainHeight(s);

            // Any of them extraneous?
            if (w < 0 || h > 0) { z++; continue; }

            // Get that item
            ItemStack item = event.getView().getTopInventory().getItem(s);

            // Valid
            if (SilentNumbers.isAir(item)) { continue; }

            // Drops
            drops.add(item);
        }

        // Give the gems back
        for (ItemStack drop : event.getPlayer().getInventory().addItem(drops.toArray(new ItemStack[0])).values()) {

            // Not air right
            if (SilentNumbers.isAir(drop)) { continue; }

            // Drop to the world
            event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), drop); }
    }
}
