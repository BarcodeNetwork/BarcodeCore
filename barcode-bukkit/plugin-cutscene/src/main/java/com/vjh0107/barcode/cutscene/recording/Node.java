package com.vjh0107.barcode.cutscene.recording;

import com.vjh0107.barcode.cutscene.npc.data.Equipment;
import com.vjh0107.barcode.cutscene.npcs.Position;
import com.vjh0107.barcode.framework.serialization.serializers.LocationSerializer;
import kotlinx.serialization.json.Json;
import org.bukkit.Location;

public class Node {

	Location location;
	Equipment equipment;
	Position playerStatus;
	String action;
	int animation;
	boolean activating;
	boolean onFire;
	
	public Node(Location location, Equipment equipment, Position playerStatus, String action, int animation, boolean activating, boolean onFire) {
		this.location = location;
		this.equipment = equipment;
		this.playerStatus = playerStatus;
		this.action = action;
		this.animation = animation;
		this.activating = activating;
		this.onFire = onFire;
	}

	public boolean isOnFire() {
		return onFire;
	}
	
	public boolean isActivating() {
		return activating;
	}
	
	public int getAnimation() {
		return animation;
	}
	
	public Location getLocation() {
		return location;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Position getPlayerStatus() {
		return playerStatus;
	}

	public String getAction() {
		return action;
	}
	
	public String serialize() {
		String locationString = Json.Default.encodeToString(LocationSerializer.INSTANCE, location);
		String equipmentString = Equipment.serialize(equipment);
		String playerStatusString = playerStatus.name();
		
		return locationString + "==" + equipmentString + "==" + playerStatusString + "==" + action + "==" + animation + "==" + activating + "==" + onFire;
	}
}
