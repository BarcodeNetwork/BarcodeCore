package com.vjh0107.barcode.cutscene.recording;

import com.vjh0107.barcode.cutscene.npc.data.Equipment;
import com.vjh0107.barcode.cutscene.utils.ErrorHandler;
import com.vjh0107.barcode.cutscene.recording.listeners.GlideListener;
import com.vjh0107.barcode.cutscene.datahandler.DataHandler;
import com.vjh0107.barcode.cutscene.datahandler.DataPath;
import com.vjh0107.barcode.cutscene.BarcodeCutscenePlugin;
import com.vjh0107.barcode.cutscene.npcs.Position;
import com.vjh0107.barcode.cutscene.uibuilder.NewCutsceneMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class RecordSession {

	public static Map<UUID, RecordSession> recordSessions = new HashMap<>();
	public String queuedAction = null;
	public int animation = -1;
	public boolean activating = false;
	public String file;
	public int npcSlot;
	public int slot;
	public int page;
	public EntityType type;
	
	List<Node> nodes = new ArrayList<>();
	
	Scoreboard scoreboard;
	Objective objective;
	
	Player recordingPlayer;
	Player recordedPlayer;
	DataPath dataPath;
	DataPath cinematicPath;
	
	boolean isRecording = false;
	
	public int ID = 0;
	public List<Location> changedBlocks = new ArrayList<>();
	public Map<Location, BlockData> revertBlock = new HashMap<>();
	
	public RecordSession(Player recordingPlayer, Player recordedPlayer, DataPath cinematicPath, DataPath dataPath, String file, int slot, int page, int npcSlot, EntityType type, int ID) {
		this.cinematicPath = cinematicPath;
		this.ID = ID;
		this.recordingPlayer = recordingPlayer;
		this.recordedPlayer = recordedPlayer;
		this.dataPath = dataPath;
		this.file = file;
		this.slot = slot;
		this.page = page;
		this.npcSlot = npcSlot;
		this.type = type;
	}

	public void startRecording() {
		try {
			RecordSession.recordSessions.put(recordingPlayer.getUniqueId(), this);
			recordingPlayer.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Started recording!");
			isRecording = true;
			
			setupScoreboard();
			
			tick();
		}catch(Exception e) {
			ErrorHandler.handleError(e);
		}
	}
	
	public void stopRecording() {
		try {
			FileConfiguration fc = DataHandler.getFile(cinematicPath);
			List<String> information = new ArrayList<>();
			
			List<String> npcTimeline = fc.getStringList("npcTimeline");
			npcTimeline.set(slot, "Recording,"+npcSlot+","+ID);
		
			fc.set("npcTimeline", npcTimeline);
			
			for(Node node : nodes) {
				information.add(node.serialize());
			}
			
			DataHandler.saveFile(fc, cinematicPath);
			
			FileConfiguration fc2 = DataHandler.getFile(dataPath);
			fc2.set("Nodes."+ID, information);
			DataHandler.saveFile(fc2, dataPath);
			
			
			recordingPlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			
			RecordSession.recordSessions.remove(recordingPlayer.getUniqueId());
			
			for(Location b : changedBlocks) {
				b.getBlock().setBlockData(revertBlock.get(b));
			}
		}catch(Exception e) {
			ErrorHandler.handleError(e);
		}
		recordingPlayer.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Stopped recording!");
		isRecording = false;
		
		NewCutsceneMenu.getMenu(recordedPlayer, file, page);
	}
	
	public void setQueuedAction(String queuedAction) {
		this.queuedAction = queuedAction;
	}
	
	private void tick() {
		if(!recordingPlayer.isOnline()) {
			stopRecording();
			return;
		}
		
		if(!recordedPlayer.isOnline()) {
			stopRecording();
			return;
		}
		
		if(recordedPlayer.getInventory().getHeldItemSlot() == 0) {
			stopRecording();
			return;
		}
		
		if(!isRecording) {return;}
		
		Node node = getNode();
		nodes.add(node);
		
		updateScores();
		
		BarcodeCutscenePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(BarcodeCutscenePlugin.instance, new Runnable() {
			public void run() {
				if(!isRecording) {return;}
				
				tick();
			}
		}, 1);
	}
	
	private Node getNode() {
		Location location = recordedPlayer.getLocation();
		location.add(0, dy, 0);
		location.setYaw((float) (location.getYaw()+dyaw));
		dyaw = 0;
		PlayerInventory playerInventory = recordedPlayer.getInventory();
		Equipment equipment = new Equipment(
				playerInventory.getHelmet(),
				playerInventory.getChestplate(),
				playerInventory.getLeggings(), 
				playerInventory.getBoots(), 
				playerInventory.getItemInMainHand(), 
				playerInventory.getItemInOffHand());
		
		Position playerStatus = Position.REGULAR;
		
		boolean swimming = false;
		if(recordedPlayer.getEyeLocation().getBlock().isLiquid()) {
			swimming = true;
		}
		
		if(recordedPlayer.getRemainingAir() != recordedPlayer.getMaximumAir() && recordedPlayer.getGameMode() != GameMode.CREATIVE && recordedPlayer.getGameMode() != GameMode.SPECTATOR) {
			swimming = true;
		}
		
		if(swimming) {
			playerStatus = Position.SWIMMING;
		}
		
		if(GlideListener.getGliding().contains(recordedPlayer.getUniqueId())) {
			playerStatus = Position.GLIDING;
		}
		
		if(recordedPlayer.isSneaking()) {
			playerStatus = Position.CROUCHING;
		}
		
		if(overwritePosition != null) {
			playerStatus = overwritePosition;
			overwritePosition = null;
		}
		
		String action = queuedAction;
		queuedAction = null;
		
		int ani = animation;
		animation = -1;
		
		boolean onFire = recordedPlayer.getFireTicks() > -20;
		
		return new Node(location, equipment, playerStatus, action, ani, activating, onFire);
	}
	
	public boolean isRecording() {
		return isRecording;
	}
	
	private void setupScoreboard() {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		@SuppressWarnings("deprecation")
		Objective objective = scoreboard.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.BOLD+""+ChatColor.GOLD+"Recording");
		this.objective = objective;
		
		updateScores();
		
		recordingPlayer.setScoreboard(scoreboard);
	}
	
	Position overwritePosition = null;
	int dy = 0;
	double dyaw = 0;
	
	private void updateScores() {
		try {
			addLine(ChatColor.BOLD+""+ChatColor.GOLD+"Frame Count", nodes.size());
			addLine(ChatColor.BOLD+""+ChatColor.GOLD+"Recording Length", nodes.size()/20);
			addLine(ChatColor.DARK_PURPLE + "", 1);
			addLine(ChatColor.DARK_PURPLE + "", 1);
			addLine(ChatColor.DARK_PURPLE + "", 1);
			addLine(ChatColor.BOLD+""+ChatColor.RED+"Select first slot to stop recording", 1);
			
			if(type == EntityType.HORSE || type == EntityType.ZOMBIE_HORSE || type == EntityType.SKELETON_HORSE) {
				addLine(ChatColor.DARK_PURPLE + "", 0);
				addLine(ChatColor.RED + "" + ChatColor.BOLD+"Horse Functions", 0);
				addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 2: Horse Prance", 0);
				addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 3: Horse Eat", 0);
				addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 4-9: Normal", 0);
				
				if(recordingPlayer.getInventory().getHeldItemSlot() == 1) {
					overwritePosition = Position.PRANCING;
				}
				
				if(recordingPlayer.getInventory().getHeldItemSlot() == 2) {
					overwritePosition = Position.EATING;
				}
			}
			
			if(type == EntityType.BAT) {
				addLine(ChatColor.DARK_PURPLE + "", 0);
				addLine(ChatColor.RED + "" + ChatColor.BOLD+"Bat Functions", 0);
				addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 2: Bat Hanging", 0);
				addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 3-9: Normal", 0);
				
				dy = 1;
				
				if(recordingPlayer.getInventory().getHeldItemSlot() == 1) {
					overwritePosition = Position.HANGING;
				}
			}
			
			if(type == EntityType.PLAYER) {
				addLine(ChatColor.DARK_PURPLE + "", 0);
				addLine(ChatColor.RED + "" + ChatColor.BOLD+"Player Functions", 0);
				addLine(ChatColor.GRAY+"Select slots for positions", 0);
				addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 2: Sleeping", 0);
				addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 3-9: Normal", 0);
				
				if(recordingPlayer.getInventory().getHeldItemSlot() == 1) {
					overwritePosition = Position.SLEEPING;
					dyaw = 90+45;
				}
			}
			
			try {
				if(type == EntityType.valueOf("FOX")) {
					addLine(ChatColor.DARK_PURPLE + "", 0);
					addLine(ChatColor.RED + "" + ChatColor.BOLD+"Fox Functions", 0);
					addLine(ChatColor.GRAY+"Select slots for positions", 0);
					addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 2: Fox Sleeping", 0);
					addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 3: Fox Faceplanting", 0);
					addLine(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Slot 4-9: Normal", 0);
					
					if(recordingPlayer.getInventory().getHeldItemSlot() == 1) {
						overwritePosition = Position.SLEEPING;
					}else if(recordingPlayer.getInventory().getHeldItemSlot() == 2) {
						overwritePosition = Position.FACEPLANTING;
					}
				}
			}catch(Exception e) {
				
			}
		}catch(Exception e) {
			ErrorHandler.handleError(e);
		}
	}
	
	private void addLine(String name, int value) {
		Score score = objective.getScore(name);
		score.setScore(value);
	}
}
















