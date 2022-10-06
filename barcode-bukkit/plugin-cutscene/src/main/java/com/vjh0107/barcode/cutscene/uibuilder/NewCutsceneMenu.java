package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.Cutscene;
import com.vjh0107.barcode.cutscene.data.PlayerDataExtensionsKt;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.ui.*;
import com.vjh0107.barcode.cutscene.utils.Texture;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.utils.Standards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class NewCutsceneMenu {

	public static Map<UUID, Integer> prevPage = new HashMap<>();
	public static Map<UUID, String> prevPageFile = new HashMap<>();
	
	public static MasterUI getMenu(Player p, String file, int page) {
		return getMenu(p, file, page, false);
	}
	
	public static MasterUI getMenu(Player p, String file, int page, boolean delete) {
		prevPageFile.put(p.getUniqueId(), file);
		prevPage.put(p.getUniqueId(), page);
		MasterUI ui = new MasterUI(ChatColor.AQUA+""+ChatColor.BOLD+"["+ChatColor.YELLOW+""+ChatColor.BOLD+"Cutscene '"+ChatColor.GOLD+file+ChatColor.YELLOW+"' Page " + ChatColor.AQUA + (page+1) + ChatColor.BOLD + "]", 54, p);
		
		ui.addItem(0, Toolbox.createCustomSkull(1, ChatColor.GOLD+""+ChatColor.BOLD+"Timeline", new ArrayList<>(), "b86b9d58bcd1a555f93e7d8659159cfd25b8dd6e9bce1e973822824291862"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				
			}
		});
		
		ui.addItem(9, Toolbox.createCustomSkull(1, ChatColor.AQUA+""+ChatColor.BOLD+"+"+ChatColor.WHITE+""+ChatColor.BOLD+"Camera", new ArrayList<>(), "14422a82c899a9c1454384d32cc54c4ae7a1c4d72430e6e446d53b8b385e330"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				
			}
		});
		
		ui.addItem(18, Toolbox.createCustomSkull(1, ChatColor.AQUA+""+ChatColor.BOLD+"+"+ChatColor.WHITE+""+ChatColor.BOLD+"NPCs", new ArrayList<>(), "3e39c6450854393ecdbf546b10aa21d57853b87b8ac22cc37291b2f6371"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				
			}
		});
		
		ui.addItem(27, Toolbox.createCustomSkull(1, ChatColor.AQUA+""+ChatColor.BOLD+"+"+ChatColor.WHITE+""+ChatColor.BOLD+"Effects", new ArrayList<>(), "9f84735fc9c760e95eaf10cec4f10edb5f3822a5ff9551eeb5095135d1ffa302"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				
			}
		});
		
		ui.addItem(36, Toolbox.createCustomSkull(1, ChatColor.AQUA+""+ChatColor.BOLD+"+"+ChatColor.WHITE+""+ChatColor.BOLD+"Sounds", new ArrayList<>(), "4ceeb77d4d25724a9caf2c7cdf2d88399b1417c6b9ff5213659b653be4376e3"), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				
			}
		});
		
		DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
		FileConfiguration fc = DataHandler.getFile(dataPath);
		
		/*
		 * 
		 * TIMELINE FOR TIME
		 * 
		 */
		List<Integer> totalTimeline = new ArrayList<>();
		if(fc.contains("Timeline")) {
			List<String> timeline = fc.getStringList("Timeline");
			
			for(int i = 0; i < 8*(page+1) && i < timeline.size(); i++) {
				String time = timeline.get(i);
				if(!Toolbox.isNumeric(time)) {
					totalTimeline.add(0);
				}
				
				totalTimeline.add(Integer.parseInt(time));
			}
		}
		
		boolean add = false;
		while(totalTimeline.size() < (page+1)*8) {
			totalTimeline.add(0);
			add = true;
		}
		
		if(add) {
			fc.set("Timeline", totalTimeline);
		}
		
		for(int i = 1; i < 9; i++) {
			
			int get = (page*8)+(i-1);
			
			double elapsedTicks = 0;
			for(int f = 0; f < get; f++) {
				int tick = totalTimeline.get(f);
				elapsedTicks += tick;
			}
			
			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Click to edit");
			lore.add(ChatColor.YELLOW+""+ChatColor.BOLD+"Middle Click to add node");
			lore.add(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Drop (Q) to play from here");
			lore.add("");
			lore.add(ChatColor.YELLOW+""+ChatColor.BOLD+"INFO:");
			lore.add(ChatColor.WHITE+"Elapsed Time: " + ChatColor.GRAY + elapsedTicks + " ticks " + ChatColor.RED + Toolbox.round(elapsedTicks/20, 4) + " seconds");
			
			int val = totalTimeline.get(get);
			Material mat = Material.ORANGE_STAINED_GLASS_PANE;
			if(val == 0) {
				mat = Material.BLACK_STAINED_GLASS_PANE;
			}
			
			ui.addItem(i, mat, ChatColor.WHITE+""+ChatColor.BOLD+val+" ticks", lore, new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					Player p = masterUI.getPlayer();
					if(clickType == ClickType.DROP) {
						if(PlayerDataExtensionsKt.getCutscenePlayerData(p).isInCutscene()) {
							p.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Sorry, you are already in a cutscene!");
							return;
						}
						
						Cutscene cutscene = new Cutscene(dataPath, p);
						cutscene.startCutscene(get);
						
						return;
					}
					
					if(clickType == ClickType.MIDDLE) {
						List<String> timeline = fc.getStringList("Timeline");
						List<String> npctimeline = fc.getStringList("npcTimeline");
						List<String> effectstimeline = fc.getStringList("effectsTimeline");
						List<String> cameratimeline = fc.getStringList("cameraTimeline");
						List<String> soundstimeline = fc.getStringList("soundsTimeline");
						timeline.add(get, "0");
						npctimeline.add(get, "Empty");
						effectstimeline.add(get, "Empty");
						cameratimeline.add(get, "Empty");
						soundstimeline.add(get, "Empty");
						fc.set("Timeline", timeline);
						
						int i = -1;
						for(String s : npctimeline) {
							i++;
							if(s.equalsIgnoreCase("Empty")) {
								continue;
							}
							
							String[] parts = s.split(",");
							if(parts[0].equalsIgnoreCase("Spawn")) {
								continue;
							}
							
							int spot = Integer.parseInt(parts[1]);
							if(spot >= get) {
								String place = parts[0]+","+(spot+1)+","+parts[2];

								if(parts.length > 2) {
									for(int q = 3; q < parts.length; q++) {
										place += "," + parts[q];
									}
								}
								
								npctimeline.set(i, place);
							}
						}
						
						fc.set("npcTimeline", npctimeline);
						fc.set("effectsTimeline", effectstimeline);
						fc.set("cameraTimeline", cameratimeline);
						fc.set("soundsTimeline", soundstimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
						return;
					}
					
					p.closeInventory();
					p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Please insert your new value in ticks. Seconds to Ticks means dividing by 20. This value will indicate the delay between this node and the next one.");
					askForInput(p, file, "Timeline", get, page, dataPath);
				}
			});
		}
		
		
		/*
		 * 
		 * TIMELINE FOR CAMERAS
		 * 
		 */
		
		List<String> cameraTimeline = new ArrayList<>();
		if(fc.contains("cameraTimeline")) {
			List<String> cameratimeline = fc.getStringList("cameraTimeline");
			
			for(int i = 0; i < 8*(page+1) && i < cameratimeline.size(); i++) {
				String name = cameratimeline.get(i);
				cameraTimeline.add(name);
			}
		}
		
		add = false;
		while(cameraTimeline.size() < (page+1)*8) {
			cameraTimeline.add("Empty");
			add = true;
		}
		
		if(add) {
			fc.set("cameraTimeline", cameraTimeline);
		}
		
		for(int i = 1; i < 9; i++) {
			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Left Click to edit");
			lore.add(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Middle Click to teleport");
			lore.add(ChatColor.RED+""+ChatColor.BOLD+"Right Click to delete");
			lore.add("");
			
			int get = (page*8)+(i-1);
			String name = cameraTimeline.get(get);
			
			Material mat = Material.WHITE_STAINED_GLASS_PANE;
			if(!name.equalsIgnoreCase("Empty")) {
				mat = Material.GREEN_STAINED_GLASS_PANE;
				
				if(name.contains(",")) {
					String[] parts = name.split(",");
					lore.add(ChatColor.YELLOW+""+ChatColor.BOLD+"Position:");
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"World: " + ChatColor.GRAY + parts[0]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"X: " + ChatColor.GRAY + parts[1]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Y: " + ChatColor.GRAY + parts[2]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Z: " + ChatColor.GRAY + parts[3]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Pitch: " + ChatColor.GRAY + parts[5]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Yaw: " + ChatColor.GRAY + parts[4]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Camera Freeze: " + ChatColor.GRAY + parts[6]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Camera Teleport: " + ChatColor.GRAY + parts[7]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Camera Speed: " + ChatColor.GRAY + parts[9]);
					
					String clockwise = "Counter-Clockwise";
					if(parts[8].equalsIgnoreCase("true")) {
						clockwise = "Clockwise";
					}
					
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Rotation: " + ChatColor.GRAY + clockwise);
				}
				
				name = ChatColor.WHITE+""+ChatColor.BOLD+"Camera Position";
			}
			
			ui.addItem(i+9, mat, ChatColor.WHITE+""+name, lore, new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					if(clickType == ClickType.RIGHT) {
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> list = fc.getStringList("cameraTimeline");
						list.set(get, "Empty");
						fc.set("cameraTimeline", list);
						DataHandler.saveFile(fc, dataPath);
						
						NewCutsceneMenu.getMenu(p, file, page);
						return;
					}
					
					if(clickType == ClickType.MIDDLE) {
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> list = fc.getStringList("cameraTimeline");
						String set = list.get(get);
						String[] parts = set.split(",");
						Location loc = new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), (float)Double.parseDouble(parts[5]), (float)Double.parseDouble(parts[4]));
						p.teleport(loc);
						p.closeInventory();
						return;
					}
					
					Player p = masterUI.getPlayer();
					p.closeInventory();
					p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Move to the location you want the camera to move to and click. The camera will smoothly move towards this position.");
					new PlayerInputLocation(p, new Citem3() {
						public void call(Location answer) {
							p.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Great! We recorded the position. Question 1/3: " + ChatColor.WHITE + "Should the camera be fixed? If no, the player will be allowed to look around. (Answer with 'yes' or 'no')");
							final Location a = answer;
							new PlayerInput(p, new Citem2() {
								public void call(String answer) {
									String frozen = "false";
									if(answer.equalsIgnoreCase("yes")) {
										frozen = "true";
									}
									
									final String f = frozen;
									
									p.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Okay freeze is set to: " + f + ". Question 2/3: " + ChatColor.WHITE + "Should the camera teleport to this position? If no the camera will glide smoothly to this position from any previous position. (Answer with 'yes' or 'no')");
									
									new PlayerInput(p, new Citem2() {
										public void call(String answer) {
											String teleport = "false";
											if(answer.equalsIgnoreCase("yes")) {
												teleport = "true";
											}
											
											final String t = teleport;
											
											if(t.equalsIgnoreCase("true")) {
												p.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Great! The camera position has been added.");
												
												FileConfiguration fc = DataHandler.getFile(dataPath);
												List<String> list = fc.getStringList("cameraTimeline");
												list.set(get, 
														a.getWorld().getName()+","+
														Toolbox.round(a.getX(), 4)+","+
														Toolbox.round(a.getY(), 4)+","+
														Toolbox.round(a.getZ(), 4)+","+
														Toolbox.round(a.getPitch(), 4)+","+
														Toolbox.round(a.getYaw(), 4)+","+
														f + "," + t + "," + "true" + ",0");
												fc.set("cameraTimeline", list);
												DataHandler.saveFile(fc, dataPath);
												
												NewCutsceneMenu.getMenu(p, file, page);
												return;
											}
											
											p.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Okay teleport is set to: " + teleport + ". Question 3/3: " + ChatColor.WHITE + "How quickly should the camera glide to this point?");
											
											new PlayerInput(p, new Citem2() {
												public void call(String answer) {
													if(!Toolbox.isNumeric(answer) || Integer.parseInt(answer) <= 0) {
														p.sendMessage(ChatColor.RED+"Error! Invalid input...");
														NewCutsceneMenu.getMenu(p, file, page);
														return;
													}
													
													p.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Great! The camera position has been added.");
													
													FileConfiguration fc = DataHandler.getFile(dataPath);
													List<String> list = fc.getStringList("cameraTimeline");
													list.set(get, 
															a.getWorld().getName()+","+
															Toolbox.round(a.getX(), 4)+","+
															Toolbox.round(a.getY(), 4)+","+
															Toolbox.round(a.getZ(), 4)+","+
															Toolbox.round(a.getPitch(), 4)+","+
															Toolbox.round(a.getYaw(), 4)+","+
															f + "," + t + "," + "true" + "," + answer);
													fc.set("cameraTimeline", list);
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
				}
			});
		}
		
		/*
		 * 
		 * TIMELINE FOR NPCS
		 * 
		 */
		
		List<String> npcTimeline = new ArrayList<>();
		if(fc.contains("npcTimeline")) {
			List<String> npctimeline = fc.getStringList("npcTimeline");
			
			for(int i = 0; i < 8*(page+1) && i < npctimeline.size(); i++) {
				String name = npctimeline.get(i);
				npcTimeline.add(name);
			}
		}
		
		add = false;
		while(npcTimeline.size() < (page+1)*8) {
			npcTimeline.add("Empty");
			add = true;
		}
		
		if(add) {
			fc.set("npcTimeline", npcTimeline);
		}
		
		for(int i = 1; i < 9; i++) {
			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Click to create new");
			lore.add(ChatColor.YELLOW+""+ChatColor.BOLD+"Middle Click to edit");
			lore.add(ChatColor.RED+""+ChatColor.BOLD+"Right click to delete");
			
			int get = (page*8)+(i-1);
			String name = npcTimeline.get(get);
			
			String texture = "b86b9d58bcd1a555f93e7d8659159cfd25b8dd6e9bce1e973822824291862";
			boolean useHead = false;
			
			if(!name.equalsIgnoreCase("Empty")) {
				String[] parts = name.split(",");
				if(parts[0].equalsIgnoreCase("spawn")) {
					name = ChatColor.WHITE+""+ChatColor.BOLD+"Spawn NPC";
					
					String showName = "excited_kitten";
					if(!parts[13].equalsIgnoreCase("null")) {
						showName = parts[13];
					}
					
					lore.add("");
					lore.add(ChatColor.YELLOW+""+ChatColor.BOLD+"Spawn NPC:");
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Name: "+ChatColor.GRAY+parts[1]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Helmet: "+ChatColor.GRAY+parts[2]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Chestplate: "+ChatColor.GRAY+parts[3]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Leggings: "+ChatColor.GRAY+parts[4]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Boots: "+ChatColor.GRAY+parts[5]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Item in Hand: "+ChatColor.GRAY+parts[6]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Item in Offhand: "+ChatColor.GRAY+parts[16]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Skin Owner: "+ChatColor.GRAY+showName);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Entity Type: "+ChatColor.GRAY+parts[17]);
					lore.add("");
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Location: "+ChatColor.GRAY+"World="+parts[7]+" X="+parts[8]+" Y="+parts[9]+" Z="+parts[10]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+""+ChatColor.GRAY+"Yaw="+parts[11]+" Pitch="+parts[12]);
					
					useHead = true;
					texture = Texture.getTexture(parts[17]);
				}else if(parts[0].equalsIgnoreCase("action")) {
					try {
						int slot = Integer.parseInt(parts[1]);
						String npc = npcTimeline.get(slot);
						String[] npcparts = npc.split(",");
						
						lore.add("");
						lore.add(ChatColor.YELLOW+""+ChatColor.BOLD+"NPC Action:");
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Type: "+ChatColor.GRAY+parts[2]);
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"NPC Name: "+ChatColor.GRAY+npcparts[1]);
					}catch(Exception e) {
						
					}
					
					name = ChatColor.WHITE+""+ChatColor.BOLD+"NPC Action; " + parts[2];
					
					useHead = true;
					
					if(parts[2].equalsIgnoreCase("SwingArm")) {
						texture = "50dfc8a3563bf996f5c1b74b0b015b2cceb2d04f94bbcdafb2299d8a5979fac1";
					}else if(parts[2].equalsIgnoreCase("Move")) {
						texture = "d980d16ed2f1dbf9a1d54fabde91661e462f8e9faceb69a75813fab87cedfcff";
						//mat = Material.IRON_BOOTS;
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Speed: "+ChatColor.GRAY+parts[3]);
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"X: "+ChatColor.GRAY+parts[4]);
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Y: "+ChatColor.GRAY+parts[5]);
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Z: "+ChatColor.GRAY+parts[6]);
					}else if(parts[2].equalsIgnoreCase("ToggleSneaking")) {
						//mat = Material.LEATHER_BOOTS;
						texture = "d980d16ed2f1dbf9a1d54fabde91661e462f8e9faceb69a75813fab87cedfcff";
					}else if(parts[2].equalsIgnoreCase("ToggleSwimming")) {
						texture = "d980d16ed2f1dbf9a1d54fabde91661e462f8e9faceb69a75813fab87cedfcff";
						//mat = Material.TROPICAL_FISH;
					}else if(parts[2].equalsIgnoreCase("ToggleFlying")) {
						texture = "d980d16ed2f1dbf9a1d54fabde91661e462f8e9faceb69a75813fab87cedfcff";
						//mat = Material.ELYTRA;
					}
				}else if(parts[0].equalsIgnoreCase("Recording")) {
					useHead = true;
					name = ChatColor.WHITE+""+ChatColor.BOLD+"Recorded Movement";
					try {
						int slot = Integer.parseInt(parts[1]);
						String npc = npcTimeline.get(slot);
						String[] npcparts = npc.split(",");
						
						lore.add("");
						
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"NPC Name: "+ChatColor.GRAY+npcparts[1]);
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"NPC Type: "+ChatColor.GRAY+npcparts[17]);
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"NPC Spawn Slot: "+ChatColor.GRAY+""+slot);
						
						DataPath recordingPath = new DataPath(file + "_Recording_"+parts[2], Standards.PATH_TO_RECORDINGS, file);
						FileConfiguration fc2 = DataHandler.getFile(recordingPath);
						List<String> nodes = fc2.getStringList("Nodes."+parts[2]);
						
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Duration: "+ChatColor.GRAY+nodes.size() + " Ticks");
					}catch(Exception e) {
						
					}
					texture = "eb28f4eeff891b78d51f75d8722c628484ba49df9c9f2371898c26967386";
					//mat = Material.STRUCTURE_BLOCK;
				}
			}
			
			ItemStack is = Toolbox.createCustomSkull(1, ChatColor.WHITE+""+name, lore, texture);
			if(!useHead) {
				is = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
				ItemMeta im = is.getItemMeta();
				im.setLore(lore);
				is.setItemMeta(im);
			}
			
			
			final String n = name;
			ui.addItem(i+18, is, new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					if(clickType == ClickType.RIGHT) {
						List<String> list = fc.getStringList("npcTimeline");
						list.set(get, "Empty");
						fc.set("npcTimeline", list);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
						return;
					}else if(clickType == ClickType.MIDDLE) {
						if(ChatColor.stripColor(n).equalsIgnoreCase("Spawn NPC")) {
							String[] parts = npcTimeline.get(get).split(",");
							
							int data = 0;
							if(parts.length > 18) {
								data = Integer.parseInt(parts[18]);
							}
							
							NPCMenu_New.getMenu(p, file, get, page, parts[1], 
									Material.getMaterial(parts[2]), Material.getMaterial(parts[3]), Material.getMaterial(parts[4]), Material.getMaterial(parts[5]), Material.getMaterial(parts[6]),
									parts[13], Material.getMaterial(parts[16]), EntityType.valueOf(parts[17]), data);
							return;
						}
					}
					
					NPCMenu.getMenu(p, file, get, page);
				}
			});
		}
		
		ui.addItem(45, Material.ARROW, ChatColor.WHITE+"Return", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				CutscenePickerMenu.getMenu(masterUI.getPlayer(), 0);
			}
		});

		if(!delete) {
			ui.addItem(53, Material.TNT, ChatColor.RED+""+ChatColor.BOLD+"Delete", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					getMenu(p, file, page, true);
				}
			});
		}else {
			ui.addItem(53, Material.REDSTONE_BLOCK, ChatColor.RED+""+ChatColor.BOLD+"Delete Permanently!", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
					DataHandler.deleteFile(dataPath);
					CutscenePickerMenu.getMenu(p, 0);
					p.sendMessage(ChatColor.RED+"Deleted that cutscene!");
				}
			});
			
			ui.addItem(52, Material.DIAMOND_BLOCK, ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Cancel Delete (Keep)", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					getMenu(p, file, page, false);
				}
			});
		}
		
		/*
		 * 
		 * TIMELINE FOR EFFECTS
		 * 
		 */
		
		List<String> effectsTimeline = new ArrayList<>();
		if(fc.contains("effectsTimeline")) {
			List<String> effectstimeline = fc.getStringList("effectsTimeline");
			
			for(int i = 0; i < 8*(page+1) && i < effectstimeline.size(); i++) {
				String name = effectstimeline.get(i);
				effectsTimeline.add(name);
			}
		}
		
		add = false;
		while(effectsTimeline.size() < (page+1)*8) {
			effectsTimeline.add("Empty");
			add = true;
		}
		
		if(add) {
			fc.set("effectsTimeline", effectsTimeline);
		}
		
		for(int i = 1; i < 9; i++) {
			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Click to edit");
			lore.add(ChatColor.RED+""+ChatColor.BOLD+"Right Click to delete");
			
			int get = (page*8)+(i-1);
			String name = effectsTimeline.get(get);
			
			Material mat = Material.WHITE_STAINED_GLASS_PANE;
			if(!name.equalsIgnoreCase("Empty")) {
				mat = Material.PURPLE_STAINED_GLASS_PANE;
			}
			
			if(!name.equalsIgnoreCase("Empty")) {
				if(!name.contains(",")) {
					String[] parts = name.split(";");
					if(parts[0].equalsIgnoreCase("Message")) {
						mat = Material.WRITTEN_BOOK;
						name = "Chat Message";
						lore.add("");
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Message: " + ChatColor.GRAY + parts[1]);
					}else if(parts[0].equalsIgnoreCase("Title")) {
						mat = Material.ENCHANTED_BOOK;
						name = "Title Message";
						lore.add("");
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Duration: " + ChatColor.GRAY + parts[1] + " Ticks");
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Title: " + ChatColor.GRAY + parts[2]);
						lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Subtitle: " + ChatColor.GRAY + parts[3]);
					}else if(parts[0].equalsIgnoreCase("Command")) {
						mat = Material.COMMAND_BLOCK;
						name = "Command";
						lore.add("");
						lore.add(ChatColor.GREEN+""+ChatColor.BOLD+"Command: " + ChatColor.RESET + "" + ChatColor.GRAY + "/" + parts[1]);
					}
				}else {
					String[] parts = name.split(",");
					if(parts[0].equalsIgnoreCase("Effect")) {
						if(parts[1].equalsIgnoreCase("LightningStrike")) {
							mat = Material.BLAZE_ROD;
							name = "Lightning Strike Effect";
						}else if(parts[1].equalsIgnoreCase("GreenSmoke")) {
							mat = Material.EMERALD;
							name = "Green Smoke Effect";
						}else if(parts[1].equalsIgnoreCase("Smoke")) {
							mat = Material.INK_SAC;
							name = "Smoke Effect";
						}else if(parts[1].equalsIgnoreCase("Fire")) {
							mat = Material.BLAZE_POWDER;
							name = "Fire Effect";
						}else if(parts[1].equalsIgnoreCase("Hearts")) {
							mat = Material.GLISTERING_MELON_SLICE;
							name = "Hearts Effect";
						}else if(parts[1].equalsIgnoreCase("Damage")) {
							mat = Material.IRON_SWORD;
							name = "Damage Effect";
						}else if(parts[1].equalsIgnoreCase("Notes")) {
							mat = Material.NOTE_BLOCK;
							name = "Notes Effect";
						}
					}else if(parts[0].equalsIgnoreCase("Time")) {
						mat = Material.CLOCK;
						name = "Time: " + parts[1];
					}
				}
			}
			
			ui.addItem(i+27, mat, ChatColor.WHITE+""+ChatColor.BOLD+name, lore, new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					if(clickType == ClickType.RIGHT) {
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> list = fc.getStringList("effectsTimeline");
						list.set(get, "Empty");
						fc.set("effectsTimeline", list);
						DataHandler.saveFile(fc, dataPath);
						
						NewCutsceneMenu.getMenu(p, file, page);
						return;
					}
					
					Player p = masterUI.getPlayer();
					Effects_Select.getMenu(p, file, get, page);
				}
			});
		}
		
		/*
		 * 
		 * TIMELINE FOR SOUNDS
		 * 
		 */
		
		List<String> soundsTimeline = new ArrayList<>();
		if(fc.contains("soundsTimeline")) {
			List<String> soundstimeline = fc.getStringList("soundsTimeline");
			
			for(int i = 0; i < 8*(page+1) && i < soundstimeline.size(); i++) {
				String name = soundstimeline.get(i);
				soundsTimeline.add(name);
			}
		}
		
		add = false;
		while(soundsTimeline.size() < (page+1)*8) {
			soundsTimeline.add("Empty");
			add = true;
		}
		
		if(add) {
			fc.set("soundsTimeline", soundsTimeline);
		}
		
		DataHandler.saveFile(fc, dataPath);
		
		for(int i = 1; i < 9; i++) {
			List<String> lore = new ArrayList<>();
			lore.add("");
			lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Click to edit");
			lore.add(ChatColor.RED+""+ChatColor.BOLD+"Right Click to delete");
			
			int get = (page*8)+(i-1);
			String name = soundsTimeline.get(get);
			
			Material mat = Material.PURPLE_STAINED_GLASS_PANE;
			if(name.equalsIgnoreCase("Empty")) {
				mat = Material.WHITE_STAINED_GLASS_PANE;
			}else {
				String[] parts = name.split(",");
				if(parts[0].equalsIgnoreCase("Sound")) {
					name = "Play Sound";
					lore.add("");
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Sound: " + ChatColor.GRAY + parts[1]);
					lore.add(ChatColor.YELLOW+"-"+ChatColor.WHITE+""+ChatColor.BOLD+"Pitch: " + ChatColor.GRAY + parts[2]);
				}
			}
			
			ui.addItem(i+36, mat, ChatColor.WHITE+""+ChatColor.BOLD+name, lore, new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					if(clickType == ClickType.RIGHT) {
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> list = fc.getStringList("soundsTimeline");
						list.set(get, "Empty");
						fc.set("soundsTimeline", list);
						DataHandler.saveFile(fc, dataPath);
						
						NewCutsceneMenu.getMenu(p, file, page);
						return;
					}
					
					Player p = masterUI.getPlayer();
					Sound_Select.getMenu(p, file, get, page, 0, null);
				}
			});
		}
		
		if(page > 0) {
			ui.addItem(48, Material.FEATHER, ChatColor.WHITE+""+ChatColor.BOLD+"Previous Page", new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					NewCutsceneMenu.getMenu(p, file, page-1);
				}
			});
		}
		
		ui.addItem(49, Material.FEATHER, ChatColor.WHITE+""+ChatColor.BOLD+"Next Page", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				NewCutsceneMenu.getMenu(p, file, page+1);
			}
		});
		
		ui.addItem(50, Material.EMERALD_BLOCK, ChatColor.GREEN+""+ChatColor.BOLD+"Start", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(PlayerDataExtensionsKt.getCutscenePlayerData(p).isInCutscene()) {
					p.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Sorry, you are already in a cutscene!");
					return;
				}
				
				Cutscene cutscene = new Cutscene(dataPath, p);
				cutscene.startCutscene(0);
			}
		});
		
		ui.addItem(45, Material.ARROW, ChatColor.WHITE+""+ChatColor.BOLD+"Return", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				CutscenePickerMenu.getMenu(masterUI.getPlayer(), 0);
			}
		});
		
		return ui;
	}
	
	public static void askForInput(Player p, String file, String set, int edit, int page, DataPath dataPath) {
		new PlayerInput(p, new Citem2() {
			public void call(String answer) {
				if(!Toolbox.isNumeric(answer) || Integer.parseInt(answer) < 0) {
					p.sendMessage(ChatColor.RED+"Please give a numeric value higher than 0");
					askForInput(p, file, set, edit, page, dataPath);
					return;
				}
				
				FileConfiguration fc = DataHandler.getFile(dataPath);
				List<String> timeline = fc.getStringList(set);
				timeline.set(edit, Integer.parseInt(answer)+"");
				fc.set(set, timeline);
				DataHandler.saveFile(fc, dataPath);
				
				p.sendMessage(ChatColor.GREEN+"Succesfully changed the delay on this timeline node to " + Integer.parseInt(answer) + " ticks. Divide by 20 to get the value in seconds");
				NewCutsceneMenu.getMenu(p, file, page);
			}	
		});
	}
}
