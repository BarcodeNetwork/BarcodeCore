package com.vjh0107.barcode.cutscene.ui;

import com.vjh0107.barcode.cutscene.BarcodeCutscenePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MasterUI implements Listener {
	
	public static Map<UUID, MasterUI> openInventory = new HashMap<>();
	
	public static MasterUI getOpenInventory(Player p) {
		if(!openInventory.containsKey(p.getUniqueId())) {
			return null;
		}
		
		return openInventory.get(p.getUniqueId());
	}
	
	private Map<Integer, Citem> action = new HashMap<>();
	
	private Inventory inventory;
	private Player player;
	
	public MasterUI(String title, int size, Player p) {
		inventory = Bukkit.createInventory(p, size, title);
		openInventory.put(p.getUniqueId(), this);
		p.openInventory(inventory);
		Bukkit.getPluginManager().registerEvents(this, BarcodeCutscenePlugin.instance);
		this.player = p;
	}
	
	public void addItem(int slot, ItemStack is, Citem item) {
		inventory.setItem(slot,  is);
		action.put(slot, item);
	}

	public void addItem(int slot, Material mat, String displayName, Citem item) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		if(displayName != null) {
			im.setDisplayName(displayName);
		}
		
		is.setItemMeta(im);
		
		inventory.setItem(slot, is);
		action.put(slot, item);
	}
	
	
	public void addItem(int slot, Material mat, String displayName, List<String> lore, Citem item) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		if(displayName != null) {
			im.setDisplayName(displayName);
		}
		
		im.setLore(lore);
		
		is.setItemMeta(im);
		
		inventory.setItem(slot,  is);
		action.put(slot, item);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getClickedInventory();
		if(inv == null) {
			return;
		}
		
		if(!inv.equals(inventory)) {
			return;
		}
		
		event.setCancelled(true);
		
		int slot = event.getSlot();
		
		if(!action.containsKey(slot)) {
			return;
		}
		
		Citem item = action.get(slot);
		item.call(this, event.getClick());
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(!event.getInventory().equals(inventory)) {
			return;
		}
		
		if(event.getPlayer().equals(player)) {
			return;
		}
		
		openInventory.remove(event.getPlayer().getUniqueId());
		HandlerList.unregisterAll(this);
	}
}
