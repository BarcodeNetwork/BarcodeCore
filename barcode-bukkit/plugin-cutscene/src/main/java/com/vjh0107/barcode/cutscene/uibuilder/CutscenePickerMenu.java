package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.ui.Citem;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.utils.Standards;
import com.vjh0107.barcode.cutscene.ui.Citem2;
import com.vjh0107.barcode.cutscene.ui.MasterUI;
import com.vjh0107.barcode.cutscene.ui.PlayerInput;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CutscenePickerMenu {

	public static MasterUI getMenu(Player p, int page) {
		MasterUI ui = new MasterUI(ChatColor.GOLD + "바코드 컷씬 메뉴", 54, p);
		
		int slot = page*27;
		
		List<String> files = new ArrayList<>();
		for(File file : DataHandler.getAllFilesInDirectory(Standards.PATH_TO_CUTSCENES)) {
			if(file == null) {
				continue;
			}
			String name = file.getName().replaceAll(".yml", "");
			files.add(name);
		}
		
		int placementSlot = -1;
		for(int i = slot; i < files.size() && i < slot+27; i++) {
			placementSlot++;
			
			String name = files.get(i);
			
			DataPath dataPath = new DataPath(name, Standards.PATH_TO_CUTSCENES);
			FileConfiguration fc = DataHandler.getFile(dataPath);
			
			double length = 0;
			int npcs = 0;
			int cameraNodes = 0;
			
			if(fc.contains("Timeline")) {for(String s : fc.getStringList("Timeline")) {if(!Toolbox.isNumeric(s)) {continue;}length += Integer.parseInt(s);}}
			if(fc.contains("npcTimeline")) {for(String s : fc.getStringList("npcTimeline")) {if(s.startsWith("Spawn")) {npcs++;}}}
			
			if(fc.contains("cameraTimeline")) {
				for(String s : fc.getStringList("cameraTimeline")) {
					if(!s.startsWith("Empty")) {
						cameraNodes++;
					}
				}
			}
			
			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GRAY + "길이: " + ChatColor.YELLOW + Toolbox.round((length/20),2) + " 초");
			lore.add(ChatColor.GRAY + "엔피시 수: " + ChatColor.YELLOW + npcs + " 개");
			lore.add(ChatColor.GRAY + "카메라 노드 수: " + ChatColor.YELLOW + cameraNodes + " 노드들");
			lore.add("");
			lore.add(ChatColor.GREEN+"클릭하여 수정합니다.");

			ui.addItem(placementSlot, Material.PAPER, ChatColor.GOLD+""+ChatColor.BOLD+"Name: "+ChatColor.WHITE+name, lore, new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					int page = 0;
					if(NewCutsceneMenu.prevPage.containsKey(p.getUniqueId())) {
						if(NewCutsceneMenu.prevPageFile.get(p.getUniqueId()).equalsIgnoreCase(name)) {
							page = NewCutsceneMenu.prevPage.get(p.getUniqueId());
						}
					}
					
					NewCutsceneMenu.getMenu(masterUI.getPlayer(), name, page);
				}
			});
		}
		
		if(page > 0) {
			ui.addItem(36, Material.FEATHER, ChatColor.WHITE+""+ChatColor.BOLD+"이전 페이지", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					CutscenePickerMenu.getMenu(masterUI.getPlayer(), page-1);
				}
			});
		}
		
		if(slot+27 < files.size()) {
			ui.addItem(44, Material.FEATHER, ChatColor.WHITE+""+ChatColor.BOLD+"다음 페이지", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					CutscenePickerMenu.getMenu(masterUI.getPlayer(), page+1);
				}
			});
		}
		
		List<String> l = new ArrayList<>();
		l.add("");
		l.add(ChatColor.GREEN + "클릭하여 생성합니다.");
		
		ui.addItem(49, Material.EMERALD, ChatColor.GREEN+""+ChatColor.BOLD+"+"+ChatColor.WHITE + "컷씬 생성하기", l, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				masterUI.getPlayer().closeInventory();
				askForInput(masterUI);
			}
		});
		
		return ui;
	}
	
	public static void askForInput(MasterUI masterUI) {
		masterUI.getPlayer().sendMessage(ChatColor.GREEN + "생성할 컷씬 이름을 적어주세요.");
		new PlayerInput(masterUI.getPlayer(), new Citem2() {
			public void call(String answer) {
				DataPath dataPath = new DataPath(answer.replaceAll(" ", "_"), Standards.PATH_TO_CUTSCENES);
				
				if(DataHandler.checkIfFileExists(dataPath)) {
					masterUI.getPlayer().sendMessage(ChatColor.RED+"이미 있는 이름입니다.");
					askForInput(masterUI);
					return;
				}
				
				DataHandler.getFile(dataPath);
				NewCutsceneMenu.getMenu(masterUI.getPlayer(), answer.replaceAll(" ", "_"), 0);
			}
		});
	}
}
