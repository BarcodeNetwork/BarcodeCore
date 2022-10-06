package net.Indyuce.mmoitems.stat.data.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import net.Indyuce.mmoitems.api.Element;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.api.util.NumericStatFormula;
import net.Indyuce.mmoitems.stat.data.ElementListData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import org.jetbrains.annotations.NotNull;

public class RandomElementListData implements StatData, RandomStatData, UpdatableRandomStatData {
	private final Map<Element, NumericStatFormula> damage = new HashMap<>(), defense = new HashMap<>();

	public RandomElementListData(ConfigurationSection config) {
		Validate.notNull(config, "Config cannot be null");

		for (String key : config.getKeys(false)) {
			Element element = Element.valueOf(key.toUpperCase());
			if (config.contains(key + ".damage"))
				damage.put(element, new NumericStatFormula(config.get(key + ".damage")));
			if (config.contains(key + ".defense"))
				defense.put(element, new NumericStatFormula(config.get(key + ".defense")));
		}
	}

	public boolean hasDamage(Element element) {
		return damage.containsKey(element);
	}

	public boolean hasDefense(Element element) {
		return defense.containsKey(element);
	}

	@NotNull public NumericStatFormula getDefense(@NotNull Element element) { return defense.getOrDefault(element, NumericStatFormula.ZERO); }
	@NotNull public NumericStatFormula getDamage(@NotNull Element element) { return damage.getOrDefault(element, NumericStatFormula.ZERO); }

	public Set<Element> getDefenseElements() {
		return defense.keySet();
	}

	public Set<Element> getDamageElements() {
		return damage.keySet();
	}

	public void setDamage(Element element, NumericStatFormula formula) {
		damage.put(element, formula);
	}

	public void setDefense(Element element, NumericStatFormula formula) {
		defense.put(element, formula);
	}

	@Override
	public StatData randomize(MMOItemBuilder builder) {
		ElementListData elements = new ElementListData();
		damage.forEach((element, formula) -> elements.setDamage(element, formula.calculate(builder.getLevel())));
		defense.forEach((element, formula) -> elements.setDefense(element, formula.calculate(builder.getLevel())));
		return elements;
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public <T extends StatData> T reroll(@NotNull ItemStat stat, @NotNull T original, int determinedItemLevel) {

		// Start brand new
		ElementListData elements = new ElementListData();
		ElementListData originalElements = (ElementListData) original;

		// Evaluate each
		for (Element elm : Element.values()) {

			// Whats its
			NumericStatFormula damageGen = getDamage(elm);
			NumericStatFormula defenseGen = getDefense(elm);
			DoubleData damageVal = new DoubleData(originalElements.getDamage(elm));
			DoubleData defenseVal = new DoubleData(originalElements.getDefense(elm));
			
			// Evaluate
			DoubleData damageResult = damageGen.reroll(stat, damageVal, determinedItemLevel);
			DoubleData defenseResult = defenseGen.reroll(stat, defenseVal, determinedItemLevel);

			// Apply
			elements.setDamage(elm, damageResult.getValue());
			elements.setDefense(elm, defenseResult.getValue());
		}

		// THats it
		return (T) elements;
	}
}