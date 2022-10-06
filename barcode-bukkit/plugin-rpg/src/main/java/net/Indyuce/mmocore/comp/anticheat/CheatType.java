package net.Indyuce.mmocore.comp.anticheat;

public enum CheatType {
	GENERAL_EXPLOITS("Exploits"),
	NO_SWING("NoSwing"),
	MOVEMENT("IrregularMovements"),
	CLIPPING("Clip"),
	IMPOSSIBLE_ACTION("ImpossibleActions"),
	INVENTORY_CLEAR("ItemDrops"),
	INVENTORY_CLICKS("InventoryClicks"),
	AUTO_SPRINT("Sprint"),
	JESUS("Jesus"),
	NO_SLOWDOWN("NoSlowdown"),
	CRITICAL_HITS("Criticals"),
	NUKER("Nuker"),
	GHOST_HAND("GhostHand"),
	LIQUIDS("Liquids"),
	BLOCK_REACH("BlockReach"),
	ELYTRA("ElytraMove"),
	BOAT("BoatMove"),
	FAST_BOW("FastBow"),
	FAST_CLICK("FastClicks"),
	FAST_HEAL("FastHeal"),
	FLYING("Fly"),
	HIT_REACH("HitReach"),
	FAST_BREAK("FastBreak"),
	FAST_PLACE("FastPlace"),
	SPEED("Speed"),
	NO_FALL("NoFall"),
	ILLEGAL_POS("IllegalPosition"),
	FAST_EAT("FastEat"),
	VELOCITY("Velocity"),
	KILLAURA("KillAura");

	private final String spartan;

	CheatType(String spartan) {
		this.spartan = spartan;
	}

	public String toSpartan() {
		return spartan;
	}
}
