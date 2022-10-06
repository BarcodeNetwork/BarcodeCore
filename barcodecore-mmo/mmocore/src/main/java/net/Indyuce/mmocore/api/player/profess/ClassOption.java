package net.Indyuce.mmocore.api.player.profess;

public enum ClassOption {

	/*
	 * is class by default
	 */
	DEFAULT,

	/*
	 * displays in the /class GUI
	 */
	DISPLAY(true),

	/*
	 * only regen resource when out of combat
	 */
	OFF_COMBAT_HEALTH_REGEN,
	OFF_COMBAT_MANA_REGEN,
	OFF_COMBAT_STAMINA_REGEN,
	OFF_COMBAT_STELLIUM_REGEN;

	private final boolean def;

	ClassOption() {
		this(false);
	}

	ClassOption(boolean def) {
		this.def = def;
	}

	public boolean getDefault() {
		return def;
	}

	public String getPath() {
		return name().toLowerCase().replace("_", "-");
	}
}