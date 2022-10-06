package net.Indyuce.mmocore.skill;

import io.lumine.mythic.lib.player.cooldown.CooldownObject;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.api.util.math.formula.IntegerLinearValue;
import net.Indyuce.mmocore.api.util.math.formula.LinearValue;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class Skill implements CooldownObject {
    private final String id;

    private String name;
    private ItemStack icon = new ItemStack(Material.BOOK);
    private List<String> lore;
    private boolean passive;

    private final Map<String, LinearValue> modifiers = new HashMap<>();
    private final Skill skill = this;

    protected static final Random random = new Random();

    public Skill() {
        id = getClass().getSimpleName().toUpperCase();
        name = getClass().getSimpleName().replace("_", " ");

        Validate.notNull(id, "ID cannot be null");
        Validate.notNull(id, "Name cannot be null");

        // Default modifiers
        addModifierIfNone("mana", LinearValue.ZERO);
        addModifierIfNone("stamina", LinearValue.ZERO);
    }

    public Skill(String id) {
        this.id = id.toUpperCase().replace(" ", "_").replace("-", "_");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaterial(Material material) {
        setIcon(new ItemStack(material));
    }

    public void setIcon(ItemStack item) {
        this.icon = item;
    }

    public void setLore(String... lore) {
        setLore(Arrays.asList(lore));
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setPassive() {
        passive = true;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLowerCaseId() {
        return id.replace("_", "-").toLowerCase();
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack getIcon() {
        return icon.clone();
    }

    @Override
    public String getCooldownPath() {
        return "mmocore_skill_" + id.toLowerCase();
    }

    /**
     * Passive skills do not display any message when trying to cast them
     *
     * @return If the skill is passive
     */
    public boolean isPassive() {
        return passive;
    }

    public boolean hasModifier(String modifier) {
        return modifiers.containsKey(modifier);
    }

    public void addModifier(String modifier, LinearValue linear) {
        modifiers.put(modifier, linear);
    }

    public void addModifierIfNone(String mod, LinearValue defaultValue) {
        if (!hasModifier(mod))
            addModifier(mod, defaultValue);
    }

    public LinearValue getModifierInfo(String modifier) {
        return modifiers.get(modifier);
    }

    public double getModifier(String modifier, int level) {
        return modifiers.get(modifier).calculate(level);
    }

    public Set<String> getModifiers() {
        return modifiers.keySet();
    }

    public void update(FileConfiguration config) {
        name = config.getString("name", "INVALID NAME");
        lore = config.getStringList("lore");
        icon = MMOCoreUtils.readIcon(config.getString("material", "DIRT"));

        for (String modifier : modifiers.keySet())
            if (config.contains(modifier))
                modifiers.put(modifier, readLinearValue(modifiers.get(modifier), config.getConfigurationSection(modifier)));
    }

    private LinearValue readLinearValue(LinearValue current, ConfigurationSection config) {
        return current instanceof IntegerLinearValue ? new IntegerLinearValue(config) : new LinearValue(config);
    }

    public abstract SkillMetadata whenCast(CasterMetadata casterMeta, SkillInfo skill);

    public SkillInfo newSkillInfo(ConfigurationSection config) {
        return new SkillInfo(config);
    }

    public SkillInfo newSkillInfo(int level) {
        return new SkillInfo(level);
    }

    public class SkillInfo {
        private final int level, max;
        private final Map<String, LinearValue> modifiers = new HashMap<>(skill.modifiers);

        /*
         * class used to save information about skills IN A CLASS CONTEXT i.e at
         * which level the skill can be unlocked, etc.
         */
        public SkillInfo(int level) {
            this.level = level;
            this.max = 0;
        }

        public SkillInfo(ConfigurationSection config) {
            level = config.getInt("level");
            max = config.getInt("max-level");

            for (String key : config.getKeys(false))
                if (config.get(key) instanceof ConfigurationSection && modifiers.containsKey(key))
                    modifiers.put(key, readLinearValue(modifiers.get(key), config.getConfigurationSection(key)));
        }

        public Skill getSkill() {
            return skill;
        }

        public int getUnlockLevel() {
            return level;
        }

        public boolean hasMaxLevel() {
            return max > 0;
        }

        public int getMaxLevel() {
            return max;
        }

        /*
         * this method can only OVERRIDE default modifiers
         */
        public void addModifier(String modifier, LinearValue linear) {
            if (modifiers.containsKey(modifier))
                modifiers.put(modifier, linear);
        }

        public double getModifier(String modifier, int level) {
            return modifiers.get(modifier).calculate(level);
        }

        public List<String> calculateLore(PlayerData data) {
            return calculateLore(data, data.getSkillLevel(skill));
        }

        public List<String> calculateLore(PlayerData data, int x) {
            List<String> list = new ArrayList<>();

            Map<String, String> placeholders = calculateModifiers(x);
            placeholders.put("mana_name", data.getProfess().getManaDisplay().getName());
            lore.forEach(str -> list.add(applyPlaceholders(placeholders, str)));

            return list;
        }

        private String applyPlaceholders(Map<String, String> placeholders, String str) {
            while (str.contains("{") && str.substring(str.indexOf("{")).contains("}")) {
                String holder = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
                str = str.replace("{" + holder + "}", placeholders.getOrDefault(holder, "PHE"));
            }
            return str;
        }

        private Map<String, String> calculateModifiers(int x) {
            Map<String, String> map = new HashMap<>();
            modifiers.keySet().forEach(modifier -> map.put(modifier, modifiers.get(modifier).getDisplay(x)));
            return map;
        }
    }
}
