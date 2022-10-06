package io.lumine.mythic.lib.listener.event;


import io.lumine.mythic.lib.api.event.ArmorEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipEventListener implements Listener {

    // private final List<String> blockedMaterials;

    // public ArmorEquipEventListener(List<String> blockedMaterials){
    // this.blockedMaterials = blockedMaterials;
    // }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled() || event.getAction() == InventoryAction.NOTHING)
            return;

        final boolean shift = event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT), numberkey = event.getClick().equals(ClickType.NUMBER_KEY);

        if (event.getSlotType() != InventoryType.SlotType.ARMOR && event.getSlotType() != InventoryType.SlotType.QUICKBAR && event.getSlotType() != InventoryType.SlotType.CONTAINER)
            return;
        if (event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            return;
        if (!event.getInventory().getType().equals(InventoryType.CRAFTING) && !event.getInventory().getType().equals(InventoryType.PLAYER))
            return;
        if (!(event.getWhoClicked() instanceof Player))
            return;
        ArmorEquipEvent.ArmorType newArmorType = ArmorEquipEvent.ArmorType.matchType(shift ? event.getCurrentItem() : event.getCursor());
        if (!shift && newArmorType != null && event.getRawSlot() != newArmorType.getSlot()) {
            // Used for drag and drop checking to make sure you aren't trying to
            // place a helmet in the boots slot.
            return;
        }

        if (shift) {
            newArmorType = ArmorEquipEvent.ArmorType.matchType(event.getCurrentItem());
            if (newArmorType != null) {
                final boolean equipping = event.getRawSlot() != newArmorType.getSlot();
                Player player = (Player) event.getWhoClicked();

                if (equipping == isAirOrNull(newArmorType.getItem(player)))
                    Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent((Player) event.getWhoClicked(), newArmorType, equipping ? null : event.getCurrentItem(), equipping ? event.getCurrentItem() : null));
                // if(armorEquipEvent.isCancelled()){
                // event.setCancelled(true);
                // }
            }

        } else {
            ItemStack newArmorPiece = event.getCursor();
            ItemStack oldArmorPiece = event.getCurrentItem();
            if (numberkey) {

                // prevents shit in the 2v2 crafting
                if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                    // event.getClickedInventory() == The players inventory
                    // event.getHotBarButton() == key people are pressing to
                    // equip
                    // or unequip the item to or from.
                    // event.getRawSlot() == The slot the item is going to.
                    // event.getSlot() == Armor slot, can't use
                    // event.getRawSlot() as
                    // that gives a hotbar slot ;-;
                    ItemStack hotbarItem = event.getClickedInventory().getItem(event.getHotbarButton());
                    if (!isAirOrNull(hotbarItem)) {// Equipping
                        newArmorType = ArmorEquipEvent.ArmorType.matchType(hotbarItem);
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = event.getClickedInventory().getItem(event.getSlot());
                    } else // Unequipping
                        newArmorType = ArmorEquipEvent.ArmorType.matchType(!isAirOrNull(event.getCurrentItem()) ? event.getCurrentItem() : event.getCursor());
                }

                // equip with no new item going into the slot
            } else if (isAirOrNull(event.getCursor()) && !isAirOrNull(event.getCurrentItem()))
                newArmorType = ArmorEquipEvent.ArmorType.matchType(event.getCurrentItem());

            // event.getCurrentItem() == Unequip
            // event.getCursor() == Equip
            // newArmorType =
            // ArmorType.matchType(!isAirOrNull(event.getCurrentItem()) ?
            // event.getCurrentItem() : event.getCursor());
            if (newArmorType != null && event.getRawSlot() == newArmorType.getSlot())
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent((Player) event.getWhoClicked(), newArmorType, oldArmorPiece, newArmorPiece));
            // if(armorEquipEvent.isCancelled()){
            // event.setCancelled(true);
            // }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK))
            return;

        ArmorEquipEvent.ArmorType type = ArmorEquipEvent.ArmorType.matchType(event.getItem());
        if (type != null && isAirOrNull(type.getItem(event.getPlayer())))
            Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(event.getPlayer(), ArmorEquipEvent.ArmorType.matchType(event.getItem()), null, event.getItem()));
        // if(armorEquipEvent.isCancelled()){
        // event.setCancelled(true);
        // player.updateInventory();
        // }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorType slot
        // Can't replace armor using this method making getCursor() useless.
        ArmorEquipEvent.ArmorType type = ArmorEquipEvent.ArmorType.matchType(event.getOldCursor());
        if (event.getRawSlots().isEmpty())
            return;// Idk if this will ever happen
        if (type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0))
            Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent((Player) event.getWhoClicked(), type, null, event.getOldCursor()));
        // if(armorEquipEvent.isCancelled()){
        // event.setResult(Result.DENY);
        // event.setCancelled(true);
        // }
    }

    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent event) {
        ArmorEquipEvent.ArmorType type = ArmorEquipEvent.ArmorType.matchType(event.getBrokenItem());
        if (type != null)
            Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(event.getPlayer(), type, event.getBrokenItem(), null));
        // if(armorEquipEvent.isCancelled()){
        // ItemStack i = event.getBrokenItem().clone();
        // i.setAmount(1);
        // i.setDurability((short) (i.getDurability() - 1));
        // if(type.equals(ArmorType.HELMET)){
        // p.getInventory().setHelmet(i);
        // }else if(type.equals(ArmorType.CHESTPLATE)){
        // p.getInventory().setChestplate(i);
        // }else if(type.equals(ArmorType.LEGGINGS)){
        // p.getInventory().setLeggings(i);
        // }else if(type.equals(ArmorType.BOOTS)){
        // p.getInventory().setBoots(i);
        // }
        // }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (ItemStack item : player.getInventory().getArmorContents())
            if (!isAirOrNull(item))
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(player, ArmorEquipEvent.ArmorType.matchType(item), item, null));
    }

    @EventHandler
    public void onDispense(BlockDispenseArmorEvent event) {
        ArmorEquipEvent.ArmorType type = ArmorEquipEvent.ArmorType.matchType(event.getItem());
        if (type != null && event.getTargetEntity() instanceof Player)
            Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent((Player) event.getTargetEntity(), type, null, event.getItem()));
        // if(armorEquipEvent.isCancelled()){
        // event.setCancelled(true);
        // }
    }

    /**
     * A utility method to support versions that use null or air ItemStacks.
     */
    private boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType().equals(Material.AIR);
    }
}
