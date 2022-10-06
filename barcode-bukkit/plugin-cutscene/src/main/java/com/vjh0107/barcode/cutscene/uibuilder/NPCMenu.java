package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.ui.Citem;
import com.vjh0107.barcode.cutscene.ui.MasterUI;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;

public class NPCMenu {

	public static MasterUI getMenu(Player p, String file, int slot, int page) {
		MasterUI ui = new MasterUI(ChatColor.GREEN+"NPC", 9, p);
		
		ui.addItem(3, Toolbox.createCustomSkull(1, ChatColor.GOLD+"+"+ChatColor.WHITE+"기존에 있던 NPC 쓰기", new ArrayList<>(), "4f01ec6331a3bc30a8204ec56398d08ca38788556bca9b81d776f6238d567367"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				NPCMenu_Select.getMenu(p, file, slot, page);
			}
		});
		
		ui.addItem(5, Toolbox.createCustomSkull(1, ChatColor.GOLD+"+"+ChatColor.WHITE+"NPC 생성하기", new ArrayList<>(), "9d06cfa6ae76526cfd77edf3f160329c23a0deafc8e681e154031037b619ad33"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				NPCMenu_New.getMenu(p, file, slot, page, null, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, null, Material.AIR, EntityType.PLAYER, 0);
			}
		});
		
		return ui;
	}
}
