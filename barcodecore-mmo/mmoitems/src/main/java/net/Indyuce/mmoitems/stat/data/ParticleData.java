package net.Indyuce.mmoitems.stat.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

import com.google.gson.JsonObject;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.particle.api.ParticleRunnable;
import net.Indyuce.mmoitems.particle.api.ParticleType;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;

public class ParticleData implements StatData, RandomStatData {
	private final ParticleType type;
	private final Particle particle;
	private final Map<String, Double> modifiers = new HashMap<>();
	private final Color color;

	public ParticleData(JsonObject object) {
		particle = Particle.valueOf(object.get("Particle").getAsString());
		type = ParticleType.valueOf(object.get("Type").getAsString());

		if (object.has("Color")) {
			JsonObject color = object.getAsJsonObject("Color");
			this.color = Color.fromRGB(color.get("Red").getAsInt(), color.get("Green").getAsInt(), color.get("Blue").getAsInt());
		} else
			color = Color.fromRGB(255, 0, 0);

		object.getAsJsonObject("Modifiers").entrySet().forEach(entry -> setModifier(entry.getKey(), entry.getValue().getAsDouble()));
	}

	public ParticleData(ConfigurationSection config) {
		Validate.isTrue(config.contains("type") && config.contains("particle"), "Particle is missing type or selected particle.");

		String format = config.getString("type").toUpperCase().replace("-", "_").replace(" ", "_");
		type = ParticleType.valueOf(format);

		format = config.getString("particle").toUpperCase().replace("-", "_").replace(" ", "_");
		particle = Particle.valueOf(format);

		color = config.contains("color") ? Color.fromRGB(config.getInt("color.red"), config.getInt("color.green"), config.getInt("color.blue"))
				: Color.fromRGB(255, 0, 0);

		for (String key : config.getKeys(false))
			if (!key.equalsIgnoreCase("particle") && !key.equalsIgnoreCase("type") && !key.equalsIgnoreCase("color"))
				setModifier(key, config.getDouble(key));
	}

	public ParticleData(ParticleType type, Particle particle) {
		this.type = type;
		this.particle = particle;
		this.color = Color.fromRGB(255, 0, 0);
	}

	public ParticleType getType() {
		return type;
	}

	public Particle getParticle() {
		return particle;
	}

	public Color getColor() {
		return color;
	}

	public double getModifier(String path) {
		return modifiers.containsKey(path) ? modifiers.get(path) : type.getModifier(path);
	}

	public Set<String> getModifiers() {
		return modifiers.keySet();
	}

	public void setModifier(String path, double value) {
		modifiers.put(path, value);
	}

	// Depending on if the particle is colorable or not, display with colors or not.
	public void display(Location location, float speed) {
		display(location, 1, 0, 0, 0, speed);
	}

	public void display(Location location, int amount, float offsetX, float offsetY, float offsetZ, float speed) {
		if (particle == Particle.REDSTONE) {
			location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, new Particle.DustOptions(color, 1));
		}
        else if (particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT) {
            // 0 for amount to allow colors (Thats why there is a for loop). Then the offsets are RGB values from 0.0 - 1.0, last 1 is the brightness.
        	for (int i = 0; i < amount; i++) {
        		location.getWorld().spawnParticle(particle, location, 0, (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, 1);
        	}
        }
        // else if (particle == Particle.NOTE) { Do Fancy Color Stuff Good Luck } 
        // The above code semi-worked for note particles but I think there is a limited amount of colors so its harder and prob have to get the nearest one.
		else {
			location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed);
		}
	}

	public ParticleRunnable start(PlayerData player) {
		ParticleRunnable runnable = type.newRunnable(this, player);
		runnable.runTaskTimer(MMOItems.plugin, 0, type.getTime());
		return runnable;
	}

	public JsonObject toJson() {
		JsonObject object = new JsonObject();
		object.addProperty("Particle", getParticle().name());
		object.addProperty("Type", getType().name());

		if (isColorable(particle)) {
			JsonObject color = new JsonObject();
			color.addProperty("Red", getColor().getRed());
			color.addProperty("Green", getColor().getGreen());
			color.addProperty("Blue", getColor().getBlue());
			object.add("Color", color);
		}

		JsonObject modifiers = new JsonObject();
		getModifiers().forEach(name -> modifiers.addProperty(name, getModifier(name)));
		object.add("Modifiers", modifiers);
		return object;
	}

	// TODO Allow Note to be colored and allow BLOCK_DUST/ITEM_DUST to pick a block/item.
    public static boolean isColorable(Particle particle) {
        // || particle == Particle.NOTE
        return particle == Particle.REDSTONE || particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT;
    }

	@Override
	public StatData randomize(MMOItemBuilder builder) {
		return this;
	}
}
