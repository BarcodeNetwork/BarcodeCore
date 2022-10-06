package net.Indyuce.mmocore.experience;

public class ExperienceInfo {
	private final Profession profess;
	private final int value;

	public ExperienceInfo(int value, Profession profess) {
		this.value = value;
		this.profess = profess;
	}

	public Profession getProfession() {
		return profess;
	}

	public int getValue() {
		return value;
	}
}
