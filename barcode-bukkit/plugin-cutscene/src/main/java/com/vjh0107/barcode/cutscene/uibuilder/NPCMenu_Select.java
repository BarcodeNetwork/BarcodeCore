package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.ui.Citem;
import com.vjh0107.barcode.cutscene.utils.Texture;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.utils.Standards;
import com.vjh0107.barcode.cutscene.ui.MasterUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class NPCMenu_Select {

	public static MasterUI getMenu(Player p, String file, int slot, int page) {
		MasterUI ui = new MasterUI(ChatColor.GREEN+""+ChatColor.BOLD+"Select Existing NPC", 54, p);
		
		DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
		FileConfiguration fc = DataHandler.getFile(dataPath);
		
		List<String> npcTimeline = fc.getStringList("npcTimeline");
		
		boolean empty = true;
		
		int n = -1;
		int c = -1;
		for(String s : npcTimeline) {
			c++;
			if(s.equalsIgnoreCase("Empty")) {
				continue;
			}
			
			if(c >= slot) {
				continue;
			}
			
			String[] parts = s.split(",");
			if(parts[0].equalsIgnoreCase("Spawn")) {
				n++;
				
				if(n > 53) {
					break;
				}
				
				final int c2 = c;
				
				empty = false;
				
				List<String> lore = new ArrayList<>();
				lore.add("");
				lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Name: " + ChatColor.GRAY + parts[1]);
				lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Type: " + ChatColor.GRAY + parts[17]);
				lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Spawn Slot: " + ChatColor.GRAY + c);
				
				String texture = Texture.getTexture(parts[17]);
				
				ui.addItem(n, Toolbox.createCustomSkull(1, ChatColor.WHITE+""+ChatColor.BOLD+"NPC", lore, texture), new Citem() {
					public void call(MasterUI masterUI, ClickType clickType) {
						NPCMenu_Action.getMenu(p, file, slot, c2, page, parts[1]);
					}
				});
			}
		}
		
		if(empty) {
			ui.addItem(45, Material.FEATHER, ChatColor.WHITE+""+ChatColor.BOLD+"Return", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					NPCMenu.getMenu(p, file, slot, page);
				}
			});
		}
		
		return ui;
	}
}
