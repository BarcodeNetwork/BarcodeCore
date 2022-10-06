package net.Indyuce.mmocore.experience;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class Profession {
    private final String id, name;
    private final ExpCurve expCurve;
    private final int maxLevel;
    private final Map<ProfessionOption, Boolean> options = new HashMap<>();

    /*
     * experience given to the main player level whenever he levels up this
     * profession
     */
    private final LinearValue experience;

    public Profession(String id, FileConfiguration config) {
        this.id = id.toLowerCase().replace("_", "-").replace(" ", "-");
        this.name = config.getString("name");
        Validate.notNull(name, "Could not load name");

        expCurve = config.contains("exp-curve")
                ? MMOCore.plugin.experience.getOrThrow(config.get("exp-curve").toString().toLowerCase().replace("_", "-").replace(" ", "-"))
                : ExpCurve.DEFAULT;
        experience = new LinearValue(config.getConfigurationSection("experience"));

        if (config.contains("options"))
            for (String key : config.getConfigurationSection("options").getKeys(false))
                try {
                    ProfessionOption option = ProfessionOption.valueOf(key.toUpperCase().replace("-", "_").replace(" ", "_"));
                    options.put(option, config.getBoolean("options." + key));
                } catch (IllegalArgumentException exception) {
                    MMOCore.plugin.getLogger().log(Level.WARNING,
                            "Could not load option '" + key + "' from profession '" + id + "': " + exception.getMessage());
                }

        maxLevel = config.getInt("max-level");
    }

    public boolean getOption(ProfessionOption option) {
        return options.getOrDefault(option, option.getDefault());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ExpCurve getExpCurve() {
        return expCurve;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean hasMaxLevel() {
        return maxLevel > 0;
    }

    public int calculateExperience(int x) {
        return (int) experience.calculate(x);
    }

    public LinearValue getExperience() {
        return experience;
    }

    public static enum ProfessionOption {

        /**
         * When disabled, removes exp holograms when mined
         */
        EXP_HOLOGRAMS(true);

        private final boolean def;

        private ProfessionOption(boolean def) {
            this.def = def;
        }

        public boolean getDefault() {
            return def;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profession that = (Profession) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
