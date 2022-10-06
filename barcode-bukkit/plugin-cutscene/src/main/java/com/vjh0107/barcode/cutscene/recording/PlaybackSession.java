package com.vjh0107.barcode.cutscene.recording;

import com.vjh0107.barcode.cutscene.BarcodeCutscenePlugin;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.npc.npcs.CutsceneNPC;
import com.vjh0107.barcode.cutscene.npc.data.Equipment;
import com.vjh0107.barcode.cutscene.npcs.Position;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.framework.serialization.serializers.LocationSerializer;
import kotlinx.serialization.json.Json;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class PlaybackSession {

	private Player player;
	
	public void play(CutsceneNPC npc, DataPath dataPath, int ID, Player player) {
		tick(0, loadRecording(dataPath, ID), npc);
		this.player = player;
	}
	
	public boolean isPaused = false;
	
	private void tick(int n, List<Node> nodes, CutsceneNPC npc) {
		if(n >= nodes.size() || stop) {
			return;
		}
		
		if(isPaused) {
			BarcodeCutscenePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(BarcodeCutscenePlugin.instance, new Runnable() {
				public void run() {
					tick(n, nodes, npc);
				}
			}, 1);
			
			return;
		}
		
		applyNode(nodes.get(n), npc);
		
		BarcodeCutscenePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(BarcodeCutscenePlugin.instance, new Runnable() {
			public void run() {
				tick(n+1, nodes, npc);
			}
		}, 1);
	}
	
	public boolean stop = false;
	
	public void stopPlaying() {
		for(Location b : changedBlocks) {
			player.sendBlockChange(b, revertBlock.get(b));
		}
		
		changedBlocks.clear();
		revertBlock.clear();
		
		for(Block b : turnOn) {
			if(b.getBlockData() instanceof Openable) {
				Openable door = (Openable) b.getBlockData();
				door.setOpen(true);
				b.setBlockData(door);
				//player.sendBlockChange(b.getLocation(), door);
			}
			
			if(!(b.getBlockData() instanceof Powerable)) {
				continue;
			}
			
			Powerable pow = (Powerable)b.getBlockData();
			pow.setPowered(true);
			//b.setBlockData(pow);
		}
		
		for(Block b : turnOff) {
			if(b.getBlockData() instanceof Openable) {
				Openable door = (Openable) b.getBlockData();
				door.setOpen(false);
				b.setBlockData(door);
			}
				
			if(!(b.getBlockData() instanceof Powerable)) {
				continue;
			}
			
			Powerable pow = (Powerable)b.getBlockData();
			pow.setPowered(false);
			//b.setBlockData(pow);
			
			player.sendBlockChange(b.getLocation(), pow);
		}
	}
	
	public static List<Node> loadRecording(DataPath dataPath, int ID) {
		FileConfiguration fc = DataHandler.getFile(dataPath);
		
		if(!fc.contains("Nodes."+ID)) {
			return new ArrayList<Node>();
		}
		
		List<Node> nodes = new ArrayList<>();
		
		for(String s : fc.getStringList("Nodes."+ID)) {
			String[] parts = s.split("==");
			
			String rawLocation = parts[0];
			Location location = Json.Default.decodeFromString(LocationSerializer.INSTANCE, rawLocation);
			
			String[] equipmentParts = parts[1].split(",");
			Equipment equipment = new Equipment(convert(equipmentParts[0]),convert(equipmentParts[1]),convert(equipmentParts[2]),convert(equipmentParts[3]),convert(equipmentParts[4]),convert(equipmentParts[5]));
			Position playerStatus = Position.valueOf(parts[2]);
			String action = parts[3];
			int animation = Integer.parseInt(parts[4]);
			
			nodes.add(new Node(location, equipment, playerStatus, action, animation, parts[5].equalsIgnoreCase("true"), parts[6].equalsIgnoreCase("true")));
		}
		
		return nodes;
	}

	private static ItemStack convert(String part) {
		ItemStack is = new ItemStack(Material.AIR);
		
		String[] parts = part.split(";");
		
		if(!parts[0].equalsIgnoreCase("0")) {
			is.setType(Material.valueOf(parts[0]));
		}
		
		if(parts[1].equalsIgnoreCase("1")) {
			is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		
		if(parts.length > 2) {
			ItemMeta im = is.getItemMeta();
			
			try {
				im.setCustomModelData(Integer.parseInt(parts[2]));
			}catch(Exception e) {
				
			}
			
			is.setItemMeta(im);
		}
		
		return is;
	}
	
	private List<Location> changedBlocks = new ArrayList<>();
	private Map<Location, BlockData> revertBlock = new HashMap<>();
	
	private boolean onFire = false;
	private Location prevLocation = null;
	
	public void applyNode(Node node, CutsceneNPC npc) {
		if(node == null) {
			return;
		}
		
		try {
			Random r = new Random();
			int ran = r.nextInt(5);
			if(ran == 1) {
				if(prevLocation != node.getLocation()) {
					try {
						if(prevLocation.distance(node.getLocation()) > 0.1) {
							Block b = npc.getLocation(player.getWorld()).clone().add(0, -1, 0).getBlock();
							String m = b.getType().toString().replaceAll("_BLOCK", "");
							
							for(Sound sound : Sound.values()) {
								if(sound.name().contains("BLOCK") && sound.name().contains("STEP")) {
									if(sound.name().contains(m)) {
										player.playSound(b.getLocation(), sound, 1, Toolbox.getRandom(0.7, 1.3));
										break;
									}
								}
							}
						}
					}catch(Exception e) {
						
					}
				}
			}
			
			
			Location tp = node.getLocation();
			if(npc.getEntityType() == EntityType.ENDER_DRAGON) {
				tp.setYaw(tp.getYaw()-180);
			}

				npc.teleport(tp);
			
				prevLocation = node.getLocation();
				npc.setPosition(node.getPlayerStatus());
				
				if(node.getAnimation() != -1) {
					npc.playAnimation(node.getAnimation());
					
					if(node.getAnimation() == 0) {
						player.playSound(npc.getLocation(player.getWorld()), Sound.ENTITY_SPLASH_POTION_THROW, 0.5f, Toolbox.getRandom(0.3, 0.5));
					}
				}
				
				npc.setEquipment(node.getEquipment());
			
			if(node.isOnFire()) {
				onFire = true;
				npc.changeEntityMetadata(0, 1);
			}else {
				if(onFire) {
					onFire = false;
					npc.changeEntityMetadata(0, 0);
				}
			}
			
			if(node.isActivating()) {
				npc.changeEntityMetadata(7, 1);
			} else {
				npc.changeEntityMetadata(7, 0);
			}
			
			String action = node.getAction();
			
			if(!action.equalsIgnoreCase("0") && action != null) {
				String[] parts = action.split(",,");
				
				if(parts[0].equalsIgnoreCase("PROJECTILE_EVENT")) {
					if(parts[1].equalsIgnoreCase("FISHING_HOOK")) {
						
					}else {
						Projectile projectile = (Projectile) node.getLocation().getWorld().spawnEntity(node.getLocation().clone().add(0,1,0), EntityType.valueOf(parts[1]));
						projectile.setVelocity(new Vector(Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4])));
					}
				}else if(parts[0].equalsIgnoreCase("CHEST_CLOSE")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					int z = Integer.parseInt(parts[3]);
					
					Location loc = new Location(node.getLocation().getWorld(), x, y, z);
					npc.setChestOpen(loc, 0);
					Location loc2 = new Location(node.getLocation().getWorld(), x+1, y, z);
					npc.setChestOpen(loc2, 0);
					Location loc3 = new Location(node.getLocation().getWorld(), x-1, y, z);
					npc.setChestOpen(loc3, 0);
					Location loc4 = new Location(node.getLocation().getWorld(), x, y, z+1);
					npc.setChestOpen(loc4, 0);
					Location loc5 = new Location(node.getLocation().getWorld(), x, y, z-1);
					npc.setChestOpen(loc5, 0);
					
					player.playSound(loc, Sound.BLOCK_CHEST_CLOSE, 1, Toolbox.getRandom(0.7, 1.3));
				}else if(parts[0].equalsIgnoreCase("CHEST")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					int z = Integer.parseInt(parts[3]);
					
					Location loc = new Location(node.getLocation().getWorld(), x, y, z);
					npc.setChestOpen(loc, 1);
					
					player.playSound(loc, Sound.BLOCK_CHEST_OPEN, 1, Toolbox.getRandom(0.7, 1.3));
				}else if(parts[0].equalsIgnoreCase("POWERABLE")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					int z = Integer.parseInt(parts[3]);
					boolean on = parts[4].equalsIgnoreCase("true");
					
					Location loc = new Location(node.getLocation().getWorld(), x, y, z);
					
					if(loc.getBlock().getBlockData() instanceof Openable) {
						Openable door = (Openable) loc.getBlock().getBlockData();
						door.setOpen(on);
						loc.getBlock().setBlockData(door);
						//player.sendBlockChange(loc, door);
						
						try {
							if(on) {
								turnOff.add(loc.getBlock());
								player.playSound(loc, Sound.valueOf("BLOCK_WOODEN_DOOR_OPEN"), 1, Toolbox.getRandom(0.7, 1.3));
							}else {
								turnOn.add(loc.getBlock());
								player.playSound(loc, Sound.valueOf("BLOCK_WOODEN_DOOR_CLOSE"), 1, Toolbox.getRandom(0.7, 1.3));
							}
						}catch(Exception e) {
							
						}
					}
					
					BlockData d = loc.getBlock().getBlockData();
		            if (d instanceof Powerable){
		                ((Powerable)d).setPowered(on);
		                //loc.getBlock().setBlockData(d);
		                player.sendBlockChange(loc, d);
		                
		                player.playSound(loc, Sound.BLOCK_LEVER_CLICK, 1, Toolbox.getRandom(0.7, 1.3));
		                
		                if(on) {
		                	if(!turnOff.contains(loc.getBlock()) && !turnOn.contains(loc.getBlock())) {
		                		turnOff.add(loc.getBlock());
		                	}
		                }else {
		                	if(!turnOff.contains(loc.getBlock()) && !turnOn.contains(loc.getBlock())) {
		                		turnOn.add(loc.getBlock());
		                	}
		                }
		            }
				}else if(parts[0].equalsIgnoreCase("BLOCK_PLACE")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					int z = Integer.parseInt(parts[3]);
					Material m = Material.valueOf(parts[4]);
					
					BlockData blockData = Bukkit.getServer().createBlockData(parts[5]);
					
					Location loc = new Location(node.getLocation().getWorld(), x, y, z);
					
					boolean success = false;
					for(Sound sound : Sound.values()) {
						if(sound.name().contains("BLOCK") && sound.name().contains("PLACE")) {
							if(sound.name().contains(m.name())) {
								player.playSound(loc, sound, 1, Toolbox.getRandom(0.7, 1.3));
								success = true;
								break;
							}
						}
					}
					
					if(!success) {
						if(m.toString().contains("GLASS")){
							player.playSound(loc, Sound.BLOCK_GLASS_PLACE, 1, Toolbox.getRandom(0.7, 1.3));
						}else if(m.toString().contains("STONE")){
							player.playSound(loc, Sound.BLOCK_STONE_PLACE, 1, Toolbox.getRandom(0.7, 1.3));
						}else if(m.toString().contains("GRASS") || m.toString().contains("DIRT")){
							player.playSound(loc, Sound.BLOCK_GRASS_PLACE, 1, Toolbox.getRandom(0.7, 1.3));
						}else {
							player.playSound(loc, Sound.BLOCK_GLASS_PLACE, 1, Toolbox.getRandom(0.7, 1.3));
						}
					}
					
					if(!revertBlock.containsKey(loc)) {
						revertBlock.put(loc, loc.getBlock().getBlockData());
						//loc.getBlock().setType(m);
						
						player.sendBlockChange(loc, blockData);
						
						changedBlocks.add(loc);
						//loc.getBlock().setBlockData(blockData);
					}else {
						player.sendBlockChange(loc, m.createBlockData());
						//loc.getBlock().setType(m);
					}
					
					String name = m.name();
					name = name.replaceAll("DIRT", "GRASS");
					name = name.replaceAll("COBBLESTONE", "STONE");
					
					Sound sound = null;
					for(Sound s: Sound.values()) {
						if(!s.name().contains("PLACE")){continue;}
						if(!s.name().contains(name.replaceAll("_BLOCK", ""))) {continue;}
						sound = s;
					}
					try {
						if(sound == null) {sound = Sound.valueOf(String.format("BLOCK_%s_PLACE", name.replaceAll("_BLOCK", "")));;}
						loc.getWorld().playSound(loc, sound, 1, Toolbox.getRandom(0.7, 1.3));
					}catch(Exception e) {
						
					}
				}else if(parts[0].equalsIgnoreCase("BLOCK_DESTROY")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					int z = Integer.parseInt(parts[3]);
					
					Location loc = new Location(node.getLocation().getWorld(), x, y, z);
					Material m = loc.getBlock().getType();
					if(!revertBlock.containsKey(loc)) {
						revertBlock.put(loc, loc.getBlock().getBlockData());
						player.sendBlockChange(loc, Material.AIR.createBlockData());
						//loc.getBlock().setType(Material.AIR);
						changedBlocks.add(loc);
					}else {
						player.sendBlockChange(loc, Material.AIR.createBlockData());
						//loc.getBlock().setType(Material.AIR);
					}
					
					String name = m.name();
					name = name.replaceAll("DIRT", "GRASS");
					name = name.replaceAll("COBBLESTONE", "STONE");
					
					boolean success = false;
					for(Sound sound : Sound.values()) {
						if(sound.name().contains("BLOCK") && sound.name().contains("BREAK")) {
							if(sound.name().contains(m.name())) {
								player.playSound(loc, sound, 1, Toolbox.getRandom(0.7, 1.3));
								success = true;
								break;
							}
						}
					}
					
					if(!success) {
						if(m.toString().contains("GLASS")){
							player.playSound(loc, Sound.BLOCK_GLASS_BREAK, 1, Toolbox.getRandom(0.7, 1.3));
						}else if(m.toString().contains("STONE")){
							player.playSound(loc, Sound.BLOCK_STONE_BREAK, 1, Toolbox.getRandom(0.7, 1.3));
						}else if(m.toString().contains("GRASS") || m.toString().contains("DIRT")){
							player.playSound(loc, Sound.BLOCK_GRASS_BREAK, 1, Toolbox.getRandom(0.7, 1.3));
						}else {
							player.playSound(loc, Sound.BLOCK_GLASS_BREAK, 1, Toolbox.getRandom(0.7, 1.3));
						}
					}
				}
			}
		}catch(Exception e) {
			
		}
	}
	
	List<Block> turnOff = new ArrayList<>();
	List<Block> turnOn = new ArrayList<>();
}
