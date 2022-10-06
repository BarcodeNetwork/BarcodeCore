package com.vjh0107.barcode.cutscene.uibuilder;

import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.npc.data.Skin;
import com.vjh0107.barcode.cutscene.ui.*;
import com.vjh0107.barcode.cutscene.utils.Texture;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.utils.Standards;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NPCMenu_New {

	public static MasterUI getMenu(Player p, String file, int slot, int page, String name, Material helmet, Material chestplate, Material leggings, Material boots, Material hand, String owner, Material offhand, EntityType type, int data) {
		MasterUI ui = new MasterUI(ChatColor.GREEN+"엔피시 설정", 18, p);
		
		String showName = "이름없음";
		if(name != null) {
			showName = name;
		}
		
		ui.addItem(0, Material.NAME_TAG, ChatColor.WHITE+"이름: " + ChatColor.RESET + showName, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"입력해주세요");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, answer, helmet, chestplate, leggings, boots, hand, owner, offhand, type, data);
					}
				});
			}
		});
		
		Material showMaterialHelmet = Material.BARRIER;
		if(helmet != Material.AIR) {
			showMaterialHelmet = helmet;
		}
		
		List<String> helmetLore = new ArrayList<>();
		helmetLore.add("");
		helmetLore.add(ChatColor.RED+"우클릭으로 삭제하기");
		
		String showOwner = "excited_kitten";
		if(owner != null) {
			showOwner = owner;
		}
		
		ui.addItem(1, Material.PAINTING, ChatColor.WHITE+"스킨 주인: " + showOwner, helmetLore, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(clickType == ClickType.RIGHT) {
					NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, hand, null, offhand, type, data);
					return;
				}
				
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"이름 적어주세요. 누구 스킨 가져올까요");
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, hand, answer, offhand, type, data);
					}
				});
			}
		});
		
		
		ui.addItem(2, showMaterialHelmet, ChatColor.WHITE+"헬멧", helmetLore, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(clickType == ClickType.RIGHT) {
					NPCMenu_New.getMenu(p, file, slot, page, name, Material.AIR, chestplate, leggings, boots, hand, owner, offhand, type, data);
					return;
				}
				
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"헬멧을 우클릭해주세요");
				new PlayerInputItem(p, new Citem4() {
					public void call(ItemStack answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, answer.getType(), chestplate, leggings, boots, hand, owner, offhand, type, data);
					}
				});
			}
		});
		
		Material showMaterialChestplate = Material.BARRIER;
		if(chestplate != Material.AIR) {
			showMaterialChestplate = chestplate;
		}
		
		List<String> chestplateLore = new ArrayList<>();
		chestplateLore.add("");
		chestplateLore.add(ChatColor.RED+"우클릭으로 삭제합니다");
		
		ui.addItem(3, showMaterialChestplate, ChatColor.WHITE+"갑옷", chestplateLore, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(clickType == ClickType.RIGHT) {
					NPCMenu_New.getMenu(p, file, slot, page, name, helmet, Material.AIR, leggings, boots, hand, owner, offhand, type, data);
					return;
				}
				
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"갑옷을 우클릭해주세요");
				new PlayerInputItem(p, new Citem4() {
					public void call(ItemStack answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, helmet, answer.getType(), leggings, boots, hand, owner, offhand, type, data);
					}
				});
			}
		});
		
		Material showMaterialLeggings = Material.BARRIER;
		if(leggings != Material.AIR) {
			showMaterialLeggings = leggings;
		}
		
		List<String> leggingsLore = new ArrayList<>();
		leggingsLore.add("");
		leggingsLore.add(ChatColor.RED+"우클릭으로 삭제합니다");
		
		ui.addItem(4, showMaterialLeggings, ChatColor.WHITE+"레깅스", leggingsLore, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(clickType == ClickType.RIGHT) {
					NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, Material.AIR, boots, hand, owner, offhand, type, data);
					return;
				}
				
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"레깅스를 우클릭해주세요");
				new PlayerInputItem(p, new Citem4() {
					public void call(ItemStack answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, answer.getType(), boots, hand, owner, offhand, type, data);
					}
				});
			}
		});
		
		Material showMaterialBoots = Material.BARRIER;
		if(boots != Material.AIR) {
			showMaterialBoots = boots;
		}
		
		List<String> bootsLore = new ArrayList<>();
		bootsLore.add("");
		bootsLore.add(ChatColor.RED+"우클릭으로 삭제합니다");
		
		ui.addItem(5, showMaterialBoots, ChatColor.WHITE+"부츠", bootsLore, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(clickType == ClickType.RIGHT) {
					NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, Material.AIR, hand, owner, offhand, type, data);
					return;
				}
				
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"부츠를 클릭해주세요");
				new PlayerInputItem(p, new Citem4() {
					public void call(ItemStack answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, answer.getType(), hand, owner, offhand, type, data);
					}
				});
			}
		});
		
		Material showMaterialHand = Material.BARRIER;
		if(hand != Material.AIR) {
			showMaterialHand = hand;
		}
		
		List<String> handLore = new ArrayList<>();
		handLore.add("");
		handLore.add(ChatColor.RED+"우클릭으로 삭제합니다");
		
		ui.addItem(6, showMaterialHand, ChatColor.WHITE+"메인핸드", handLore, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(clickType == ClickType.RIGHT) {
					NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, Material.AIR, owner, offhand, type, data);
					return;
				}
				
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"아이템을 우클릭해주세요");
				new PlayerInputItem(p, new Citem4() {
					public void call(ItemStack answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, answer.getType(), owner, offhand, type, data);
					}
				});
			}
		});
		
		Material showMaterialOffHand = Material.BARRIER;
		if(offhand != Material.AIR) {
			showMaterialOffHand = offhand;
		}
		
		List<String> offhandLore = new ArrayList<>();
		offhandLore.add("");
		offhandLore.add(ChatColor.RED+"우클릭으로 삭제합니다");
		
		ui.addItem(7, showMaterialOffHand, ChatColor.WHITE+"오프핸드", offhandLore, new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				if(clickType == ClickType.RIGHT) {
					NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, hand, owner, Material.AIR, type, data);
					return;
				}
				
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"아이템을 우클릭해주세요");
				new PlayerInputItem(p, new Citem4() {
					public void call(ItemStack answer) {
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, hand, owner, answer.getType(), type, data);
					}
				});
			}
		});
		
		String texture = Texture.getTexture(type.name());
		
		ui.addItem(13, Toolbox.createCustomSkull(1, ChatColor.WHITE+"Entity Type; " + type.name(), new ArrayList<String>(), texture), new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				p.closeInventory();
				
				List<String> types = new ArrayList<>();
				for(EntityType type : EntityType.values()) {
					if(type.isAlive()) {
						types.add(type.name());
					}
				}
				
				p.sendMessage(ChatColor.GREEN+"All available types:");
				p.sendMessage(Toolbox.getEnumeration(types));
				
				p.sendMessage(ChatColor.GREEN+"Write the name of the entitytype you want in the chat.");
				
				new PlayerInput(p, new Citem2() {
					public void call(String answer) {
						EntityType pick = EntityType.PLAYER;
						for(EntityType type : EntityType.values()) {
							if(type.name().equalsIgnoreCase(answer)) {
								pick = type;
								break;
							}
						}
						
						p.sendMessage(ChatColor.GREEN+"완료");
						NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, hand, owner, offhand, pick, data);
					}
				});
			}
		});
		
		int min = 0;
		int max = 0;
		
		String n = "";
		boolean show = false;
		List<String> possibilities = new ArrayList<>();
		Material mat = Material.PLAYER_HEAD; 
		
		if(type == EntityType.SHEEP) {
			show = true;
			max = 15;
			
			n = ChatColor.WHITE+"White";mat = Material.WHITE_WOOL;
			if(data == 1) {n = ChatColor.WHITE+"Orange";mat = Material.ORANGE_WOOL;
			}else if(data == 2) {n = ChatColor.WHITE+"Magenta";mat = Material.MAGENTA_WOOL;
			}else if(data == 3) {n = ChatColor.WHITE+"Light Blue";mat = Material.LIGHT_BLUE_WOOL;
			}else if(data == 4) {n = ChatColor.WHITE+"Yellow";mat = Material.YELLOW_WOOL;
			}else if(data == 5) {n = ChatColor.WHITE+"Lime";mat = Material.LIME_WOOL;
			}else if(data == 6) {n = ChatColor.WHITE+"Pink";mat = Material.PINK_WOOL;
			}else if(data == 7) {n = ChatColor.WHITE+"Gray";mat = Material.GRAY_WOOL;
			}else if(data == 8) {n = ChatColor.WHITE+"Light Gray";mat = Material.LIGHT_GRAY_WOOL;
			}else if(data == 9) {n = ChatColor.WHITE+"Cyan";mat = Material.CYAN_WOOL;
			}else if(data == 10) {n = ChatColor.WHITE+"Purple";mat = Material.PURPLE_WOOL;
			}else if(data == 11) {n = ChatColor.WHITE+"Blue";mat = Material.BLUE_WOOL;
			}else if(data == 12) {n = ChatColor.WHITE+"Brown";mat = Material.BROWN_WOOL;
			}else if(data == 13) {n = ChatColor.WHITE+"Green";mat = Material.GREEN_WOOL;
			}else if(data == 14) {n = ChatColor.WHITE+"Red";mat = Material.RED_WOOL;
			}else if(data == 15) {n = ChatColor.WHITE+"Black";mat = Material.BLACK_WOOL;}
			
			possibilities.add("White");
			possibilities.add("Orange");
			possibilities.add("Magenta");
			possibilities.add("Light Blue");
			possibilities.add("Yellow");
			possibilities.add("Lime");
			possibilities.add("Pink");
			possibilities.add("Gray");
			possibilities.add("Light Gray");
			possibilities.add("Cyan");
			possibilities.add("Purple");
			possibilities.add("Blue");
			possibilities.add("Brown");
			possibilities.add("Green");
			possibilities.add("Red");
			possibilities.add("Black");
		}
		
		final int m2 = min;
		final int m = max;
		
		if(show) {
			ui.addItem(12, mat, ChatColor.WHITE+"Entity Variant: " + n, new Citem() {
				public void call(MasterUI masterUI, ClickType clickType) {
					p.closeInventory();
					p.sendMessage(ChatColor.GREEN+"Okay! Please write a number between " + m2 + " and " + m + " in the chat. These correspond to the following variants;");
					
					int ID = 0;
					for(String s : possibilities) {
						p.sendMessage(ChatColor.YELLOW+"#"+ChatColor.RESET+""+ChatColor.WHITE+ID+": " + ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.BOLD+s);
						ID++;
					}
					
					new PlayerInput(p, new Citem2() {
						public void call(String answer) {
							if(!Toolbox.isNumeric(answer)) {
								p.sendMessage(ChatColor.RED+"Invalid input!");
								return;
							}
							
							int val = Integer.parseInt(answer);
							if(val < m2 || val > m) {
								p.sendMessage(ChatColor.RED+"Invalid input!");
								return;
							}
							
							p.sendMessage(ChatColor.GREEN+"완료");
							NPCMenu_New.getMenu(p, file, slot, page, name, helmet, chestplate, leggings, boots, hand, owner, offhand, type, val);
						}
					});
				}
			});
		}
		
		ui.addItem(8, Material.EMERALD, ChatColor.GREEN+"Finish", new Citem() {
			public void call(MasterUI masterUI, ClickType clickType) {
				p.closeInventory();
				p.sendMessage(ChatColor.GREEN+"Move to the location you want the NPC to spawn at and perform a left click.");
				new PlayerInputLocation(p, new Citem3() {
					public void call(Location answer) {
						DataPath dataPath = new DataPath(file, Standards.PATH_TO_CUTSCENES);
						FileConfiguration fc = DataHandler.getFile(dataPath);
						List<String> npcTimeline = fc.getStringList("npcTimeline");
						
						String value = "";
						String signature = "";
						
						try {
							if(owner.equalsIgnoreCase("%player%")) {
								EntityPlayer playerNMS = ((CraftPlayer) p).getHandle();
								GameProfile profile = playerNMS.fq();
								Property property = profile.getProperties().get("textures").iterator().next();
								value = property.getValue();
								signature = property.getSignature();
							} else if(owner != null) {
								@SuppressWarnings("deprecation")
								OfflinePlayer off = Bukkit.getOfflinePlayer(owner);
								if(off != null) {
									// TODO: coroutine
									//Skin skin = Skin.getSkin(off.getUniqueId());
//									if(skin != null) {
//										value = skin.getValue();
//										signature = skin.getSignature();
//									}
								}
							}
						}catch(Exception e) {
							
						}
						
						npcTimeline.set(slot, "Spawn,"+name+","+helmet.toString()+","+chestplate.toString()+","+leggings.toString()+","+boots.toString()+","+hand.toString()+","+
						answer.getWorld().getName()+","+Toolbox.round(answer.getX(),4)+","+Toolbox.round(answer.getY(),4)+","+Toolbox.round(answer.getZ(),4)+","+Toolbox.round(answer.getYaw(), 4)+","+Toolbox.round(answer.getPitch(), 4)+","+owner+","+value+","+signature+","+offhand.toString()+","+type.name()+","+data);
						fc.set("npcTimeline", npcTimeline);
						DataHandler.saveFile(fc, dataPath);
						NewCutsceneMenu.getMenu(p, file, page);
					}
				});
			}
		});
		
		return ui;
	}
}
