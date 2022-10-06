package net.Indyuce.mmocore.manager.social;

import net.Indyuce.mmocore.experience.Booster;
import net.Indyuce.mmocore.experience.Profession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BoosterManager {
	private final List<Booster> map = new ArrayList<>();

	/**
	 * If MMOCore can find a booster with the same profession and value, the two
	 * boosters will stack to reduce the amount of boosters displayed at the
	 * same time. Otherwise, booster is registered
	 * 
	 * @param booster
	 *            Booster to register
	 */
	public void register(Booster booster) {

		// flushes booster list to reduce future calculations
		flush();

		for (Booster active : map)
			if (active.canStackWith(booster)) {
				active.addLength(booster.getLength());
				return;
			}

		map.add(booster);
	}

	public Booster get(int index) {
		flush();
		return map.get(index);
	}

	/**
	 * Cleans timed out boosters from the MMOCore registry
	 */
	private void flush() {
		map.removeIf(Booster::isTimedOut);
	}

	/**
	 * @return Sums all current experience boosters values
	 */
	public double getMultiplier(Profession profession) {
		double d = 1;

		for (Booster booster : map)
			if (Objects.equals(profession, booster.getProfession()) && !booster.isTimedOut())
				d += booster.getExtra();

		return d;
	}

	public int calculateExp(Profession profession, double exp) {
		return (int) (exp * getMultiplier(profession));
	}

	/**
	 * @return Collection of currently registered boosters. Some of them can be
	 *         expired but are not unregistered yet!
	 */
	public List<Booster> getActive() {
		return map.stream().filter((b) -> !b.isTimedOut()).collect(Collectors.toList());
	}
}
