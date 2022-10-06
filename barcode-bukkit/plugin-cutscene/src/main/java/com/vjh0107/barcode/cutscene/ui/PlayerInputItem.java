package com.vjh0107.barcode.cutscene.ui;

import com.vjh0107.barcode.cutscene.BarcodeCutscenePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInputItem implements Listener {

	public static Map<UUID, PlayerInputItem> playerLink = new HashMap<>();
	
	private Player player;
	private Citem4 call;
	
	public PlayerInputItem(Player p, Citem4 call) {
		if(playerLink.containsKey(p.getUniqueId())) {	
			PlayerInputItem input = playerLink.get(p.getUniqueId());
			HandlerList.unregisterAll(input);
		}
		
		playerLink.put(p.getUniqueId(), this);
		Bukkit.getPluginManager().registerEvents(this, BarcodeCutscenePlugin.instance);
		this.player = p;
		this.call = call;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		
		if(!p.equals(player)) {
			return;
		}
		
		ItemStack is = p.getInventory().getItemInMainHand();
		if(is == null) {
			return;
		}
		
		event.setCancelled(true);

		playerLink.remove(p.getUniqueId());
		HandlerList.unregisterAll(this);
		
	    call.call(is);
	}
}
	