package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.ui.Citem;
import com.vjh0107.barcode.cutscene.ui.Citem2;
import com.vjh0107.barcode.cutscene.ui.MasterUI;
import com.vjh0107.barcode.cutscene.ui.PlayerInput;
import com.vjh0107.barcode.cutscene.utils.EditDistanceRecursive;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.utils.Standards;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sound_Select {

	public static MasterUI getMenu(Player p, String file, int slot, int page, int soundPage, String sort) {
		MasterUI ui = new MasterUI(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Add Sound Page " + (soundPage+1), 54, p);
		
		List<Sound> unsortedList = new ArrayList<>();
		
		Map<Sound, Double> distance = new HashMap<>();
		
		List<Sound> sounds = new ArrayList<>();
		
		for(Sound sound : Sound.values()) {
			if(sort == null) {
				sounds.add(sound);
				continue;
			}
			
			double d = EditDistanceRecursive.calculate(sound.name().toLowerCase(), sort.toLowerCase());
			distance.put(sound, d);
			unsortedList.add(sound);
		}
		
		if(sort != null) {
			while(!unsortedList.isEmpty()) {
				Sound least = null;
				double d2 = -1;
				for(Sound sound : unsortedList) {
					double d = distance.get(sound);
					if(least == null || d > d2) {
						least = sound;
						d2 = d;
					}
				}
				
				if(least == null) {
					break;
				}
				
				unsortedList.remove(least);
				sounds.add(least);
			}
		}
		
		int c = -1;
		for(int i = 45*soundPage; i < 45*(soundPage+1) && i < sounds.size(); i++) {
			c++;
			Sound sound = sounds.get(i);
			
			if(sound == null) {
				break;
			}
			
			Material m = Material.STONE;
			
			if(sound.name().toUpperCase().contains("AMBIENT")) {
				m = Material.COAL_BLOCK;
			}else if(sound.name().toUpperCase().contains("BLOCK")) {
				m = Material.GRASS_BLOCK;
			}else if(sound.name().toUpperCase().contains("ENTITY")) {
				m = Material.ZOMBIE_HEAD;
			}else if(sound.name().toUpperCase().contains("ITEM")) {
				m = Material.GLASS_BOTTLE;
			}else if(sound.name().toUpperCase().contains("MUSIC")) {
				m = Material.MUSIC_DISC_CAT;
			}else if(sound.name().toUpperCase().contains("UI_")) {
				m = Material.PAPER;
			}else if(sound.name().toUpperCase().contains("WEATHER")) {
				m = Material.WATER_BUCKET;
			}
			
			for(Material mat : Material.values()) {
				if(sound.name().toUpperCase().contains(mat.name().toUpperCase())) {
					if(mat.isItem()) {
						m = mat;
						break;
					}
				}
			}
			
			if(!m.isItem() || m == Material.AIR) {
				m = Material.STONE;
			}
			
			addEffect(ui, file, page, c, m, Toolbox.capitalizeWords(ChatColor.WHITE+""+ChatColor.BOLD+sound.name().replace("_", " ").toLowerCase()), sound.name(), slot);
		}
		
		if(soundPage > 0) {
			ui.addItem(48, Material.ARROW, ChatColor.WHITE+""+ChatColor.BOLD+"Previous Page", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					Sound_Select.getMenu(p, file, slot, page, soundPage-1, sort);
				}
			});
		}
		
		if(sounds.size() > (soundPage+1)*45) {
			ui.addItem(50, Material.ARROW, ChatColor.WHITE+""+ChatColor.BOLD+"Next Page", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					Sound_Select.getMenu(p, file, slot, page, soundPage+1, sort);
				}
			});
		}
		
		ui.addItem(52, Material.ANVIL, ChatColor.WHITE+""+ChatColor.BOLD+"Stop All Sound", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
				FileConfiguration fc = DataHandler.getFile(dataPath);
				List<String> effectTimeline = fc.getStringList("soundsTimeline");
				effectTimeline.set(slot, "StopSound");
				fc.set("soundsTimeline", effectTimeline);
				DataHandler.saveFile(fc, dataPath);
				NewCutsceneMenu.getMenu(p, file, page);
				
				NewCutsceneMenu.getMenu(p, file, page);
			}
		});
		
		ui.addItem(49, Material.LEAD, ChatColor.WHITE+""+ChatColor.BOLD+"Search...", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Write the keyword you want to sort by or write 'Empty'");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						if(answer.equalsIgnoreCase("Empty")) {
							Sound_Select.getMenu(p, file, slot, page, soundPage, null);
							return;
						}
						
						Sound_Select.getMenu(p, file, slot, page, soundPage, answer);
					}
				});
			}
		});
		
		ui.addItem(45, Material.FEATHER, ChatColor.WHITE+""+ChatColor.BOLD+"Return", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				NewCutsceneMenu.getMenu(p, file, page);
			}
		});
		
		return ui;
	}
	
	public static void addEffect(MasterUI ui, String file, int page, int slot, Material mat, String display, String set, int put) {
		ui.addItem(slot, mat, display, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"What pitch should the sound play at? Please answer between 0.0 and 2.0");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						if(!Toolbox.isNumeric(answer) || Double.parseDouble(answer) < 0.0 || Double.parseDouble(answer) > 2.0) {
							p.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Invalid input.");
							return;
						}
						
						p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Success!");
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> effectTimeline = fc.getStringList("soundsTimeline");
						effectTimeline.set(put, "Sound,"+set+","+answer);
						fc.set("soundsTimeline", effectTimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
					}
				});
			}
		});
	}
}
