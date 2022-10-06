package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.ui.*;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.cutscene.utils.Standards;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class Effects_Select {

	public static MasterUI getMenu(Player p, String file, int slot, int page) {
		MasterUI ui = new MasterUI(ChatColor.LIGHT_PURPLE+"이펙트 추가하기", 54, p);
		
		addEffect(ui, file, page, 1, ChatColor.GRAY+"번개", "LightningStrike", "fbb1256eb9f667c05fb21e027aa1d53558bda74e240e4fa9e137d851c416fe98", slot);
		addEffect(ui, file, page, 2, ChatColor.GREEN+"초록색 연기", "GreenSmoke", "9f84735fc9c760e95eaf10cec4f10edb5f3822a5ff9551eeb5095135d1ffa302", slot);
		addEffect(ui, file, page, 3, ChatColor.DARK_GRAY+"연기", "Smoke", "4abf071dd72854de234b463a7101fb1d0a2f8329f865ae5f5439caf4e0da92b8", slot);
		addEffect(ui, file, page, 4, ChatColor.RED+"불", "Fire", "4461d9d06c0bf4a7af4b16fd12831e2be0cf42e6e55e9c0d311a2a8965a23b34", slot);
		addEffect(ui, file, page, 5, ChatColor.LIGHT_PURPLE+"하트", "Hearts", "f1266b748242115b303708d59ce9d5523b7d79c13f6db4ebc91dd47209eb759c", slot);
		addEffect(ui, file, page, 6, ChatColor.RED+"데미지", "Damage", "5ee118eddaee0dfb2cbc2c3d59c13a41a7d68cce945e42167aa1dcb8d0670517", slot);
		addEffect(ui, file, page, 7, ChatColor.LIGHT_PURPLE+"음표", "Notes", "9b1e20410bb6c7e6968afcd3ec855520c37a40d54a54e8dafc2e6b6f2f9a1915", slot);
		
		ui.addItem(30, Material.CLOCK, ChatColor.WHITE+"시간 설정하기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"시간 틱을 적어주세요 0~24000");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						if(!Toolbox.isNumeric(answer) || Integer.parseInt(answer) < 0 || Integer.parseInt(answer) > 24000) {
							p.sendMessage(ChatColor.RED+"0~24000 만 적어주세요.");
							return;
						}
						
						p.sendMessage(ChatColor.GREEN+"완료!");
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> effectTimeline = fc.getStringList("effectsTimeline");
						
						effectTimeline.set(slot, "Time,"+answer);
						fc.set("effectsTimeline", effectTimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
					}
				});
			}
		});
		
		ui.addItem(31, Material.BUCKET, ChatColor.GREEN+"날씨 설정", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				
				p.sendMessage(ChatColor.GOLD+"비가 오게 합니까? true/false");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						if(!answer.equalsIgnoreCase("true") && !answer.equalsIgnoreCase("false")) {
							p.sendMessage(ChatColor.RED+"true/false 만 적어주세요.");
							return;
						}
						
						WeatherType type = WeatherType.CLEAR;
						if(answer.equalsIgnoreCase("true")) {
							type = WeatherType.DOWNFALL;
						}
						
						p.sendMessage(ChatColor.GREEN+"완료!");
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> effectTimeline = fc.getStringList("effectsTimeline");
						
						effectTimeline.set(slot, "Weather,"+type.toString());
						fc.set("effectsTimeline", effectTimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
					}
				});
			}
		});
		
		ui.addItem(21, Material.WRITTEN_BOOK, ChatColor.GREEN+"메시지 보내기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"보낼 메시지를 적어주세요.");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> effectTimeline = fc.getStringList("effectsTimeline");
						
						answer = answer.replaceAll(";", "").replaceAll(",", "");
						
						effectTimeline.set(slot, "Message;"+answer);
						fc.set("effectsTimeline", effectTimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
					}
				});
			}
		});
		
		ui.addItem(23, Material.ENCHANTED_BOOK, ChatColor.GREEN+"타이틀 보내기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"타이틀을 적어주세요. null일 경우 넘어갑니다.");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						p.sendMessage(ChatColor.GREEN+"타이틀 지속 틱을 적어주세요.");
						
						final String message = answer;
						new PlayerInput(p, new Citem2() {
							public void call(String answer) {
								if(!Toolbox.isNumeric(answer) || Integer.parseInt(answer) < 0) {
									p.sendMessage(ChatColor.RED+"숫자만 적어야합니다.");
									return;
								}
								
								p.sendMessage(ChatColor.GREEN+"서브 타이틀을 적어주세요. null 을 적을 경우, 넘어갑니다.");
								
								final String time = answer;
								
								new PlayerInput(p, new Citem2() {
									public void call(String answer) {
								
										DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
										FileConfiguration fc = DataHandler.getFile(dataPath);
										List<String> effectTimeline = fc.getStringList("effectsTimeline");
										
										String finalMessage = message.replaceAll(";", "").replaceAll(",", "");
										String finalMessage2 = answer.replaceAll(";", "").replaceAll(",", "");
										
										if(finalMessage.equalsIgnoreCase("null")) {
											finalMessage = " ";
										}
										
										if(finalMessage2.equalsIgnoreCase("null")) {
											finalMessage2 = " ";
										}
										
										effectTimeline.set(slot, "Title;"+time+";"+finalMessage+";"+finalMessage2);
										fc.set("effectsTimeline", effectTimeline);
										DataHandler.saveFile(fc, dataPath);
										NewCutsceneMenu.getMenu(p, file, page);
									}
								});
							}
						});
					}
				});
			}
		});
		
		ui.addItem(24, Toolbox.createCustomSkull(1, ChatColor.GREEN+"명령어 실행하기", new ArrayList<>(), "198c1bc68d13b8656434439fd62962878f9a64bffc8ae4092afdb50d4e78937"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"슬래시를 제외한 명령어를 적어주세요. %player% 는 플레이어이름이며 #을 앞에 적을 경우 플레이어 권한으로 실행합니다. 아닐 경우, 콘솔 권한으로 실행합니다.");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						p.sendMessage(ChatColor.GREEN+"완료!");
						
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> effectTimeline = fc.getStringList("effectsTimeline");
						effectTimeline.set(slot, "Command;"+answer);
						fc.set("effectsTimeline", effectTimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
					}
				});
			}
		});
		
		ui.addItem(45, Material.FEATHER, ChatColor.WHITE+"돌아가기", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				NewCutsceneMenu.getMenu(p, file, page);
			}
		});
		
		return ui;
	}
	
	public static void addEffect(MasterUI ui, String file, int page, int slot, String display, String set, String texture, int put) {
		ui.addItem(slot, Toolbox.createCustomSkull(1, display, new ArrayList<>(), texture), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				Player p = masterUI.getPlayer();
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"이펙트를 플레이할 위치로 이동후 좌클릭해주세요.");
				new PlayerInputLocation(p, new Citem3() {
					public void call(Location answer) {
						p.sendMessage(ChatColor.GREEN+"완료!");
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> effectTimeline = fc.getStringList("effectsTimeline");
						effectTimeline.set(put, "Effect,"+set+","+answer.getWorld().getName()+","+Toolbox.round(answer.getX(),4)+","+Toolbox.round(answer.getY(),4)+","+Toolbox.round(answer.getZ(),4));
						fc.set("effectsTimeline", effectTimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
					}
				});
			}
		});
	}
}
