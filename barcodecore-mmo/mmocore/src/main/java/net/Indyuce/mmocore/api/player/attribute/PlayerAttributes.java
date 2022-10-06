package net.Indyuce.mmocore.api.player.attribute;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.lumine.mythic.lib.api.stat.modifier.Closable;
import io.lumine.mythic.lib.api.stat.modifier.ModifierType;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

public class PlayerAttributes {
	private final PlayerData data;
	private final Map<String, AttributeInstance> instances = new HashMap<>();

	public PlayerAttributes(PlayerData data) {
		this.data = data;
	}

	public void load(ConfigurationSection config) {
		for (String key : config.getKeys(false))
			try {
				String id = key.toLowerCase().replace("_", "-").replace(" ", "-");
				Validate.isTrue(MMOCore.plugin.attributeManager.has(id), "Could not find attribute '" + id + "'");

				PlayerAttribute attribute = MMOCore.plugin.attributeManager.get(id);
				AttributeInstance ins = new AttributeInstance(attribute);
				ins.setBase(config.getInt(key));
				instances.put(id, ins);
			} catch (IllegalArgumentException exception) {
				data.log(Level.WARNING, exception.getMessage());
			}
	}

	public void save(ConfigurationSection config) {
		instances.values().forEach(ins -> config.set(ins.id, ins.getBase()));
	}

	public String toJsonString() {
		JsonObject json = new JsonObject();
		for (AttributeInstance ins : instances.values())
			json.addProperty(ins.getId(), ins.getBase());
		return json.toString();
	}

	public void load(String json) {
		Gson parser = new Gson();
		JsonObject jo = parser.fromJson(json, JsonObject.class);
		for (Entry<String, JsonElement> entry : jo.entrySet()) {
			try {
				String id = entry.getKey().toLowerCase().replace("_", "-").replace(" ", "-");
				Validate.isTrue(MMOCore.plugin.attributeManager.has(id), "Could not find attribute '" + id + "'");

				PlayerAttribute attribute = MMOCore.plugin.attributeManager.get(id);
				AttributeInstance ins = new AttributeInstance(attribute);
				ins.setBase(entry.getValue().getAsInt());
				instances.put(id, ins);
			} catch (IllegalArgumentException exception) {
				data.log(Level.WARNING, exception.getMessage());
			}
		}
	}

	public PlayerData getData() {
		return data;
	}

	public int getAttribute(PlayerAttribute attribute) {
		return getInstance(attribute).getTotal();
	}

	public Collection<AttributeInstance> getInstances() {
		return instances.values();
	}

	public Map<String, Integer> mapPoints() {
		Map<String, Integer> map = new HashMap<>();
		instances.values().forEach(ins -> map.put(ins.id, ins.spent));
		return map;
	}

	public AttributeInstance getInstance(PlayerAttribute attribute) {
		if (instances.containsKey(attribute.getId()))
			return instances.get(attribute.getId());

		AttributeInstance ins = new AttributeInstance(attribute);
		instances.put(attribute.getId(), ins);
		return ins;
	}

	public int countSkillPoints() {
		int n = 0;
		for (AttributeInstance ins : instances.values())
			n += ins.getBase();
		return n;
	}

	public class AttributeInstance {
		private int spent;

		private final String id;
		private final Map<String, StatModifier> map = new HashMap<>();

		public AttributeInstance(PlayerAttribute attribute) {
			id = attribute.getId();
		}

		public int getBase() {
			return spent;
		}

		public void setBase(int value) {
			spent = Math.max(0, value);

			update();
		}

		public void addBase(int value) {
			setBase(spent + value);
		}

		/*
		 * 1) two types of attributes: flat attributes which add X to the value,
		 * and relative attributes which add X% and which must be applied
		 * afterwards 2) the 'd' parameter lets you choose if the relative
		 * attributes also apply on the base stat, or if they only apply on the
		 * instances stat value
		 */
		public int getTotal() {
			double d = spent;

			for (StatModifier attr : map.values())
				if (attr.getType() == ModifierType.FLAT)
					d += attr.getValue();

			for (StatModifier attr : map.values())
				if (attr.getType() == ModifierType.RELATIVE)
					d *= attr.getValue();

			// cast to int at the last moment
			return (int) d;
		}

		public StatModifier getModifier(String key) {
			return map.get(key);
		}

		public void addModifier(String key, double value) {
			addModifier(key, new StatModifier(value));
		}

		public void addModifier(String key, StatModifier modifier) {
			map.put(key, modifier);

			update();
		}

		public Set<String> getKeys() {
			return map.keySet();
		}

		public boolean contains(String key) {
			return map.containsKey(key);
		}

		public void remove(String key) {

			/*
			 * closing stat is really important with temporary stats because
			 * otherwise the runnable will try to remove the key from the map
			 * even though the attribute was cancelled before hand
			 */
			StatModifier mod;
			if (map.containsKey(key) && (mod = map.get(key)) instanceof Closable) {
				((Closable) mod).close();
				map.remove(key);
			}

			update();
		}

		public void update() {
			PlayerAttribute attribute = MMOCore.plugin.attributeManager.get(id);
			int total = getTotal();
			attribute.getBuffs()
					.forEach((key, buff) -> data.getStats().getInstance(key).addModifier("attribute." + attribute.getId(), buff.multiply(total)));
		}

		public String getId() {
			return id;
		}
	}

	public void setBaseAttribute(String id, int value) {
		getInstances().forEach(ins -> {
			if (ins.getId().equals(id))
				ins.setBase(value);
		});
	}
}
