package com.vjh0107.barcode.cutscene;

import com.vjh0107.barcode.cutscene.data.PlayerDataExtensionsKt;
import com.vjh0107.barcode.cutscene.npc.npcs.CutsceneNPC;
import com.vjh0107.barcode.cutscene.npc.data.Equipment;
import com.vjh0107.barcode.cutscene.npc.data.Skin;
import com.vjh0107.barcode.cutscene.npcs.Position;
import com.vjh0107.barcode.cutscene.utils.Toolbox;
import com.vjh0107.barcode.framework.version.adapter.NMSAdapter;
import com.vjh0107.barcode.cutscene.utils.Standards;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.vjh0107.barcode.cutscene.astar.AStar;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.recording.Node;
import com.vjh0107.barcode.cutscene.recording.PlaybackSession;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class Cutscene {

	private DataPath file;
	private Player player;
	
	public Cutscene(DataPath file, Player player) {
		this.file = file;
		this.player = player;
	}
	
	public String getName() {
		return file.getFileName();
	}

	private int count = 0;
	
	private int delay = -1;
	private boolean isPaused = false;
	
	public void setIsPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
	
	public int getTimepassed() {
		return count;
	}
	
	public boolean isPaused() {
		return isPaused;
	}

	public void startCutscene(int n) {
		startCutscene(n, false);
	}

	public void startCutscene(int n, boolean isMovable) {
		PlayerDataExtensionsKt.getCutscenePlayerData(player).setCutscene(this);
		CutsceneMode.setPlayerCutsceneMode(player, true, isMovable);

		FileConfiguration fc = DataHandler.getFile(file);
		
		List<String> timeline = fc.getStringList("Timeline");
		List<String> cameraTimeline = fc.getStringList("cameraTimeline");
		List<String> npcTimeline = fc.getStringList("npcTimeline");
		List<String> effectsTimeline = fc.getStringList("effectsTimeline");
		List<String> soundsTimeline = fc.getStringList("soundsTimeline");
		
		if(n > 0) {
			for(int i = 0; i < n; i++) {
				startNode(i, timeline, cameraTimeline, npcTimeline, effectsTimeline, soundsTimeline, true);
			}
		}
		
		startNode(n, timeline, cameraTimeline, npcTimeline, effectsTimeline, soundsTimeline, false);
	}

	public void stopCutscene(boolean reload) {
		isPaused = false;
		stopped = true;
		if(!reload) {
			PlayerDataExtensionsKt.getCutscenePlayerData(player).whenCutsceneStop();
		}
		
		CutsceneMode.setPlayerCutsceneMode(player, false);
		
		for(CutsceneNPC npc : allNPCs) {
			try {
				if(npc != null) {
					npc.despawn();
				}
				
				if(playback.containsKey(npc)) {
					PlaybackSession session = playback.get(npc);
					session.stop = true;
				}
			}catch(Exception e) {
				
			}
		}
		
		for(PlaybackSession session : allPlaybackSessions) {
			session.stopPlaying();
		}
	}
	
	private boolean stopped = false;
	
	private int currentCameraNode = -1;
	private int currentNPCNode = -1;
	private int currentEffectsNode = -1;
	private int currentSoundNode = -1;
	public boolean isFrozen = false;
	private double dx = 0;
	private double dy = 0;
	private double dz = 0;
	private double dyaw = 0;
	private double dpitch = 0;
	private int ticks_left = 0;
	
	private List<CutsceneNPC> allNPCs = new ArrayList<>();
	private Map<Integer, CutsceneNPC> variable = new HashMap<>();
	private Map<CutsceneNPC, Location> targetLocation = new HashMap<>();
	private Map<CutsceneNPC, Double> speed = new HashMap<>();
	private Map<CutsceneNPC, PlaybackSession> playback = new HashMap<>();
	private Map<CutsceneNPC, Position> position = new HashMap<>();
	private List<PlaybackSession> allPlaybackSessions = new ArrayList<>();
	
	private void startNode(int n, List<String> timeline, List<String> cameraTimeline, List<String> npcTimeline, List<String> effectsTimeline, List<String> soundsTimeline, boolean instant) {
		if(n >= timeline.size()) {
			stopCutscene(false);
			return;
		}
		
		if(stopped) {
			return;
		}
		
		count++;
		
		if(ticks_left > 0) {
			ticks_left -= 1;
			Location ploc = player.getLocation().clone();
			ploc.add(dx, dy, dz);
			ploc.setPitch((float) (ploc.getPitch()+dpitch));
			ploc.setYaw((float) (ploc.getYaw()+dyaw));
			player.teleport(ploc);
			player.setAllowFlight(true);
			player.setFlying(true);
		}
		
		String cameraNode = cameraTimeline.get(n);
		if(!cameraNode.equalsIgnoreCase("Empty")) {
			if(currentCameraNode != n) {
				currentCameraNode = n;
				
				String[] parts = cameraNode.split(",");
				
				Location loc = new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), (float)Double.parseDouble(parts[5]), (float)Double.parseDouble(parts[4]));
				
				boolean freeze = parts[6].equalsIgnoreCase("true");
				boolean teleport = parts[7].equalsIgnoreCase("true");
				int speed = Integer.parseInt(parts[9]);
				
				ticks_left = speed;
				
				isFrozen = freeze;
				
				if(teleport) {
					player.teleport(loc);
				}else {
					player.setAllowFlight(true);
					Location ploc = player.getLocation();
					
					dx = (loc.getX()-ploc.getX())/speed;
					dy = (loc.getY()-ploc.getY())/speed;
					dz = (loc.getZ()-ploc.getZ())/speed;
					
					int startYaw = (int) ploc.getYaw();
					//if(startYaw < 0) {startYaw*=-1;}
					
					int endYaw = (int) loc.getYaw();
					//if(endYaw < 0) {endYaw*=-1;}
					
					if(endYaw > startYaw) {
						int oneWay = endYaw-startYaw;//+
						int otherWay = startYaw+(360-endYaw);//-
						
						if(oneWay > otherWay) {
							dyaw = -((double)otherWay/speed);
						}else {
							dyaw = (double)oneWay/speed;
						}
					}else {
						int oneWay = (360-startYaw)+endYaw;//+
						int otherWay = startYaw-endYaw;//-
						
						if(oneWay > otherWay) {
							dyaw = -((double)otherWay/speed);
						}else {
							dyaw = (double)oneWay/speed;
						}
					}
					
					dpitch = (loc.getPitch()-ploc.getPitch())/speed;
				}
			}
		}
		
		if(!instant) {
			String soundNode = soundsTimeline.get(n);
			if(!soundNode.equalsIgnoreCase("Empty")) {
				if(currentSoundNode != n) {
					currentSoundNode = n;
					if(soundNode.equalsIgnoreCase("StopSound")) {
						for(Sound sound : Sound.values()) {
							player.stopSound(sound);
						}
					}
						
					String[] parts = soundNode.split(",");
					if(parts[0].equalsIgnoreCase("Sound")) {
						Sound sound = Sound.valueOf(parts[1].toUpperCase());
						double pitch = Double.parseDouble(parts[2]);
						player.playSound(player.getLocation(), sound, 10000f, (float)pitch);
					}
				}
			}
		}
		
		if(!instant) {
			String effectsNode = effectsTimeline.get(n);
			if(!effectsNode.equalsIgnoreCase("Empty")) {
				if(currentEffectsNode != n) {
					currentEffectsNode = n;
					if(!effectsNode.contains(",")) {
						String[] parts = effectsNode.split(";");
						if(parts[0].equalsIgnoreCase("Message")) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', parts[1]));
						}else if(parts[0].equalsIgnoreCase("Title")) {
							int time = Integer.parseInt(parts[1]);
							String title = ChatColor.translateAlternateColorCodes('&', parts[2]);
							String subtitle = ChatColor.translateAlternateColorCodes('&', parts[3]);
							player.sendTitle(title, subtitle, 10, time, 10);
						}else if(parts[0].equalsIgnoreCase("Command")) {
							String cmd = parts[1];
							cmd = cmd.replaceAll("%player%", player.getName());
							
							if(cmd.startsWith("#")) {
								cmd = cmd.replaceFirst("#", "");
								player.chat("/"+cmd);
							}else {
								BarcodeCutscenePlugin.instance.getServer().dispatchCommand(BarcodeCutscenePlugin.instance.getServer().getConsoleSender(), cmd);
							}
						}
					}else {
						String[] parts = effectsNode.split(",");
						if(parts[0].equalsIgnoreCase("Effect")) {
							Location loc = new Location(Bukkit.getWorld(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]));

							if(parts[1].equalsIgnoreCase("LightningStrike")) {
								NMSAdapter.INSTANCE.strikeLightning(player, loc);
							}else if(parts[1].equalsIgnoreCase("GreenSmoke")) {
								player.spawnParticle(Particle.VILLAGER_HAPPY, loc, 100, 0.5, 0.5, 0.5, 1);
							}else if(parts[1].equalsIgnoreCase("Smoke")) {
								player.spawnParticle(Particle.SMOKE_NORMAL, loc, 100, 0.5, 0.5, 0.5, 1);
							}else if(parts[1].equalsIgnoreCase("Fire")) {
								player.spawnParticle(Particle.FLAME, loc, 100, 0.5, 0.5, 0.5, 1);
							}else if(parts[1].equalsIgnoreCase("Hearts")) {
								player.spawnParticle(Particle.HEART, loc, 100, 0.5, 0.5, 0.5, 1);
							}else if(parts[1].equalsIgnoreCase("Damage")) {
								player.spawnParticle(Particle.DAMAGE_INDICATOR, loc, 100, 0.5, 0.5, 0.5, 1);
							}else if(parts[1].equalsIgnoreCase("Notes")) {
								player.spawnParticle(Particle.NOTE, loc, 100, 0.5, 0.5, 0.5, 1);
							}
						}else if(parts[0].equalsIgnoreCase("Time")) {
							player.setPlayerTime(Integer.parseInt(parts[1]), false);
						}else if(parts[0].equalsIgnoreCase("Weather")) {
							player.setPlayerWeather(WeatherType.valueOf(parts[1]));
						}
					}
				}
			}
		}
		
		String npcNode = npcTimeline.get(n);
		if(!npcNode.equalsIgnoreCase("Empty")) {
			if(currentNPCNode != n) {
				currentNPCNode = n;
				String[] parts = npcNode.split(",");
				
				if(parts[0].equalsIgnoreCase("Spawn")) {
					Location loc = new Location(Bukkit.getWorld(parts[7]), Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), Double.parseDouble(parts[10]), (float)Double.parseDouble(parts[11]), (float)Double.parseDouble(parts[12]));
					
					Equipment equipment = new Equipment(
							new ItemStack(Material.valueOf(parts[2])),
							new ItemStack(Material.valueOf(parts[3])),
							new ItemStack(Material.valueOf(parts[4])),
							new ItemStack(Material.valueOf(parts[5])),
							new ItemStack(Material.valueOf(parts[6])),
							new ItemStack(Material.valueOf(parts[16])));
					
					String type = parts[17];
					
					String name = parts[1];
					if(parts[1].equalsIgnoreCase("null")) {
						name = "";
					}
					
					int data = 0;
					if(parts.length > 18) {
						data = Integer.parseInt(parts[18]);
					}

					String value = parts[14];
					String signature = parts[15];

					if (value.equalsIgnoreCase("%player%") && signature.equalsIgnoreCase("%player%")) {
						GameProfile gameProfile = ((CraftPlayer) player).getHandle().fq();
						Property property = gameProfile.getProperties().get("textures").iterator().next();
						value = property.getValue();
						signature = property.getSignature();
					}

					CutsceneNPC npc = spawnNPC(loc, value, signature, name, equipment, EntityType.valueOf(type), data);
					allNPCs.add(npc);
					variable.put(n, npc);
				}else if(parts[2].equalsIgnoreCase("SwingArm")) {
					int pos = Integer.parseInt(parts[1]);
					CutsceneNPC npc = variable.get(pos);
					npc.playAnimation(0);
				}else if(parts[2].equalsIgnoreCase("Move")) {
					int pos = Integer.parseInt(parts[1]);
					CutsceneNPC npc = variable.get(pos);
					
					double speed = Double.parseDouble(parts[3]);
					Location loc = new Location(player.getWorld(), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Double.parseDouble(parts[6]));
					
					if(instant) {
						npc.teleport(loc);
					}else {
						targetLocation.put(npc, loc);
						this.speed.put(npc, speed);
						
						makeNPCMove(npc);
					}
				}else if(parts[2].equalsIgnoreCase("ToggleSneaking")) {
					int pos = Integer.parseInt(parts[1]);
					CutsceneNPC npc = variable.get(pos);
					if(position.containsKey(npc)) {
						if(position.get(npc).equals(Position.CROUCHING)) {
							position.remove(npc);
						}
					}else {
						position.put(npc, Position.CROUCHING);
					}
				}else if(parts[2].equalsIgnoreCase("ToggleSwimming")) {
					int pos = Integer.parseInt(parts[1]);
					CutsceneNPC npc = variable.get(pos);
					if(position.containsKey(npc)) {
						if(position.get(npc).equals(Position.SWIMMING)) {
							position.remove(npc);
						}
					}else {
						position.put(npc, Position.SWIMMING);
					}
				}else if(parts[2].equalsIgnoreCase("ToggleFlying")) {
					int pos = Integer.parseInt(parts[1]);
					CutsceneNPC npc = variable.get(pos);
					if(position.containsKey(npc)) {
						if(position.get(npc).equals(Position.GLIDING)) {
							position.remove(npc);
						}
					}else {
						position.put(npc, Position.GLIDING);
					}
				}else if(parts[0].equalsIgnoreCase("Recording")) {
					int pos = Integer.parseInt(parts[1]);
					CutsceneNPC npc = variable.get(pos);
					
					if(playback.containsKey(npc)) {
						PlaybackSession session = playback.get(npc);
						session.stop = true;
					}
					
					DataPath recordingPath = new DataPath(file.getFileName() + "_Recording_" + parts[2], Standards.PATH_TO_RECORDINGS, file.getFileName());
					
					if(instant) {
						List<Node> nodes = PlaybackSession.loadRecording(recordingPath, Integer.parseInt(parts[2]));
						if(!nodes.isEmpty()) {
							Node latest = nodes.get(nodes.size()-1);
							PlaybackSession session = new PlaybackSession();
							session.applyNode(latest, npc);
						}
					}else {
						PlaybackSession session = new PlaybackSession();
						session.play(npc, recordingPath, Integer.parseInt(parts[2]), player);
						
						allPlaybackSessions.add(session);
						
						playback.put(npc, session);
					}
				}
			}
		}
		
		for(CutsceneNPC npc : allNPCs) {
			if(playback.containsKey(npc)) {
				continue;
			}
			
			if(position.containsKey(npc)) {
				npc.setPosition(position.get(npc));
			}
		}
		
		
		int increase = 0;
		
		if(delay == -1) {
			delay = Integer.parseInt(timeline.get(n))+1;
		}else if(delay == 0) {
			delay = -1;
			increase = 1;
		}else {
			delay -= 1;
		}
		
		final int in = increase;
		
		if(!instant) {
			if(isPaused) {
				waitForUnpause(n, timeline, cameraTimeline, npcTimeline, effectsTimeline, soundsTimeline);
				return;
			}
		}
		
		if(!instant) {
			BarcodeCutscenePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(BarcodeCutscenePlugin.instance, new Runnable() {
				public void run() {
					startNode(n+in, timeline, cameraTimeline, npcTimeline, effectsTimeline, soundsTimeline, false);
				}
			}, 1);
		}
	}
	
	private void waitForUnpause(int n, List<String> timeline, List<String> cameraTimeline, List<String> npcTimeline, List<String> effectsTimeline, List<String> soundsTimeline) {
		if(!isPaused) {
			startNode(n, timeline, cameraTimeline, npcTimeline, effectsTimeline, soundsTimeline, false);
			for(PlaybackSession session : allPlaybackSessions) {
				session.isPaused = false;
			}
			return;
		}
		
		for(PlaybackSession session : allPlaybackSessions) {
			session.isPaused = true;
		}
		
		BarcodeCutscenePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(BarcodeCutscenePlugin.instance, new Runnable() {
			public void run() {
				waitForUnpause(n, timeline, cameraTimeline, npcTimeline, effectsTimeline, soundsTimeline);
			}
		}, 1);
	}
	
	private CutsceneNPC spawnNPC(Location loc, String value, String signature, String displayName, Equipment equipment, EntityType type, int data) {
		CutsceneNPC npc = CutsceneNPC.Companion.of();
		if (displayName.equalsIgnoreCase("%none%")) {
			npc.spawn(loc, Skin.of(value, signature), player.getName(), type, data);
		} else if (displayName.equalsIgnoreCase("%player%")) {
			npc.spawn(loc, Skin.of(value, signature), "§r" + player.getName(), type, data);
		} else {
			npc.spawn(loc, Skin.of(value, signature), displayName, type, data);
		}
		npc.setExclusivePlayer(player);
		npc.show(player);
		npc.setEquipment(equipment);
		return npc;
	}
	
	private void makeNPCMove(CutsceneNPC npc) {
		if(!targetLocation.containsKey(npc)) {
			return;
		}
		
		Location target = targetLocation.get(npc);
		double speed = this.speed.get(npc);
		
		recursiveWalking(npc, AStar.handle(npc.getLocation(player.getWorld()), target, 1000), speed, 0, target);
	}
	
	private void recursiveWalking(CutsceneNPC npc, List<Location> targets, double speed, int n, Location target) {
		if(!targetLocation.containsKey(npc)) {
			return;
		}
		
		if(n >= targets.size()) {
			targetLocation.remove(npc);
			return;
		}
		
		Location tar = targetLocation.get(npc);
		if(!tar.equals(target)) {
			return;
		}
		
		Location to = targets.get(n);
		double xdif = 0;
		if(Toolbox.diff(npc.getLocation(player.getWorld()).getX(), to.getX())>speed) {
			xdif = speed;
			if(npc.getLocation(player.getWorld()).getX()>to.getX()) {
				xdif = -speed;
			}
		}
		
		double ydif = 0;
		if(Toolbox.diff(npc.getLocation(player.getWorld()).getY()-1, to.getY())>speed) {
			ydif = speed;
			if(npc.getLocation(player.getWorld()).getY()-1>to.getY()) {
				ydif = -speed;
			}
		}
	
		double zdif = 0;
		if(Toolbox.diff(npc.getLocation(player.getWorld()).getZ(), to.getZ())>speed) {
			zdif = speed;
			if(npc.getLocation(player.getWorld()).getZ()>to.getZ()) {
				zdif = -speed;
			}
		}
		Location tp = npc.getLocation(player.getWorld()).clone().add(xdif,ydif,zdif);
		
		double dX = npc.getLocation(player.getWorld()).getX() - tp.getX();
		double dY =  npc.getLocation(player.getWorld()).getY() - tp.getY();
		double dZ = npc.getLocation(player.getWorld()).getZ() - tp.getZ();
		
		double yaw = Math.atan2(dZ, dX)+90;
		double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
		
		tp.setYaw((float) Math.toDegrees(yaw));
		tp.setPitch((float) pitch);
		
		npc.teleport(tp);
		
		BarcodeCutscenePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(BarcodeCutscenePlugin.instance, new Runnable() {
			public void run() {
				if(Toolbox.diff(npc.getLocation(player.getWorld()).getX(), to.getX()) <= speed && Toolbox.diff(npc.getLocation(player.getWorld()).getY()-1, to.getY()) <= speed && Toolbox.diff(npc.getLocation(player.getWorld()).getZ(), to.getZ()) <= speed) {
					recursiveWalking(npc, targets, speed, n+1, target);
				}else {
					recursiveWalking(npc, targets, speed, n, target);
				}
			}
		}, 1);
	}
}













