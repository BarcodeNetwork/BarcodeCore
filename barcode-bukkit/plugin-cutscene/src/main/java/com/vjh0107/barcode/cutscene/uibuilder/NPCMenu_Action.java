package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.recording.RecordSession;
import com.vjh0107.barcode.cutscene.ui.*;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.cutscene.utils.Standards;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class NPCMenu_Action {

	public static MasterUI getMenu(Player p, String file, int slot, int position, int page, String name) {
		MasterUI ui = new MasterUI(ChatColor.GREEN+"Action NPC: '" + name + "'", 9, p);
		
		DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
		FileConfiguration fc = DataHandler.getFile(dataPath);
		
		List<String> npcTimeline = fc.getStringList("npcTimeline");
		
		ui.addItem(0, Material.IRON_BOOTS, ChatColor.WHITE+"이동", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				p.closeInventory();
				p.sendMessage(ChatColor.GOLD+"npc를 소환할 곳으로 이동하여 좌클릭해주세요.");
				new PlayerInputLocation(p, new Citem3() {
					public void call(Location answer) {
						final Location target = answer;
						p.sendMessage(ChatColor.GREEN + "NPC 속도를 정해주세요. (0.2가 기본)");
						new PlayerInput(p, new Citem2() {
							public void call(String answer) {
								if(!Toolbox.isNumeric(answer) || Double.parseDouble(answer) <= 0.0) {
									p.sendMessage(ChatColor.RED+"양의 정수만 써주세요.");
									return;
								}
								
								npcTimeline.set(slot, "Action,"+position+",Move,"+Double.parseDouble(answer)+","+Toolbox.round(target.getX(),4)+","+Toolbox.round(target.getY(),4)+","+Toolbox.round(target.getZ(),4));
								fc.set("npcTimeline", npcTimeline);
								DataHandler.saveFile(fc, dataPath);
								NewCutsceneMenu.getMenu(p, file, page);
							}
						});
					}
				});
			}
		});
		
		ui.addItem(1, Material.LEATHER_BOOTS, ChatColor.WHITE+"웅크리기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				npcTimeline.set(slot, "Action,"+position+",ToggleSneaking");
				fc.set("npcTimeline", npcTimeline);
				DataHandler.saveFile(fc, dataPath);
				NewCutsceneMenu.getMenu(p, file, page);
			}
		});
		
		ui.addItem(2, Material.ELYTRA, ChatColor.WHITE+"날기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				npcTimeline.set(slot, "Action,"+position+",ToggleFlying");
				fc.set("npcTimeline", npcTimeline);
				DataHandler.saveFile(fc, dataPath);
				NewCutsceneMenu.getMenu(p, file, page);
			}
		});
		
		ui.addItem(3, Material.TROPICAL_FISH, ChatColor.WHITE+"수영하기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				npcTimeline.set(slot, "Action,"+position+",ToggleSwimming");
				fc.set("npcTimeline", npcTimeline);
				DataHandler.saveFile(fc, dataPath);
				NewCutsceneMenu.getMenu(p, file, page);
			}
		});
		
		ui.addItem(5, Toolbox.createCustomSkull(1, ChatColor.AQUA+"+"+ChatColor.WHITE+"Record Movement", new ArrayList<>(), "14422a82c899a9c1454384d32cc54c4ae7a1c4d72430e6e446d53b8b385e330"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"좌클릭으로 녹화를 시작할 위치로 이동 후 1번 키를 눌러 녹화를 중지합니다.");
				
				new PlayerInputLocation(p, new Citem3() {
					public void call(Location answer) {
						p.sendMessage(ChatColor.GREEN+"녹화 시작");
						
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						
						if(RecordSession.recordSessions.containsKey(p.getUniqueId())) {
							p.sendMessage(ChatColor.RED+"이미 녹화중입니다.");
							return;
						}
						
						if(p.getInventory().getHeldItemSlot() == 0) {
							p.getInventory().setHeldItemSlot(8);
						}
						
						FileConfiguration fc = DataHandler.getFile(dataPath);
						
						int ID = 0;
						if(fc.contains("ID")) {
							ID = fc.getInt("ID");
						}
						
						fc.set("ID", ID+1);
						DataHandler.saveFile(fc, dataPath);
						
						DataPath recordingPath = new DataPath(file + "_Recording_"+ID, Standards.PATH_TO_RECORDINGS, file);
						
						EntityType type = EntityType.PLAYER;
						String data = npcTimeline.get(position);
						String[] parts = data.split(",");
						type = EntityType.valueOf(parts[17]);
						RecordSession session = new RecordSession(p, p, dataPath, recordingPath, file, slot, page, position, type, ID);
						session.startRecording();
					}
				});
			}
		});
		
		ui.addItem(8, Material.GOLDEN_SWORD, ChatColor.WHITE+"팔 휘두르기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				npcTimeline.set(slot, "Action,"+position+",SwingArm");
				fc.set("npcTimeline", npcTimeline);
				DataHandler.saveFile(fc, dataPath);
				NewCutsceneMenu.getMenu(p, file, page);
			}
		});
		
		return ui;
	}
}









