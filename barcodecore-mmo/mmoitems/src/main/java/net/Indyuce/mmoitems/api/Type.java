package net.Indyuce.mmoitems.api;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.util.identify.UnidentifiedItem;
import net.Indyuce.mmoitems.manager.TypeManager;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Type {

    // slashing
    public static final Type SWORD = new Type(TypeSet.SLASHING, "SWORD", true, EquipmentSlot.MAIN_HAND);

    // piercing
    public static final Type DAGGER = new Type(TypeSet.PIERCING, "DAGGER", true, EquipmentSlot.MAIN_HAND);
    public static final Type SPEAR = new Type(TypeSet.PIERCING, "SPEAR", true, EquipmentSlot.MAIN_HAND);

    // blunt
    public static final Type HAMMER = new Type(TypeSet.BLUNT, "HAMMER", true, EquipmentSlot.MAIN_HAND);
    public static final Type GAUNTLET = new Type(TypeSet.BLUNT, "GAUNTLET", true, EquipmentSlot.MAIN_HAND);

    // range
    public static final Type WHIP = new Type(TypeSet.RANGE, "WHIP", true, EquipmentSlot.MAIN_HAND);
    public static final Type STAFF = new Type(TypeSet.RANGE, "STAFF", true, EquipmentSlot.MAIN_HAND);
    public static final Type BOW = new Type(TypeSet.RANGE, "BOW", true, EquipmentSlot.BOTH_HANDS);
    public static final Type CROSSBOW = new Type(TypeSet.RANGE, "CROSSBOW", false, EquipmentSlot.BOTH_HANDS);
    public static final Type MUSKET = new Type(TypeSet.RANGE, "MUSKET", true, EquipmentSlot.MAIN_HAND);
    public static final Type LUTE = new Type(TypeSet.RANGE, "LUTE", true, EquipmentSlot.MAIN_HAND);

    // offhand
    public static final Type CATALYST = new Type(TypeSet.OFFHAND, "CATALYST", false, EquipmentSlot.BOTH_HANDS);
    public static final Type OFF_CATALYST = new Type(TypeSet.OFFHAND, "OFF_CATALYST", false, EquipmentSlot.OFF_HAND);

    // any
    public static final Type ORNAMENT = new Type(TypeSet.EXTRA, "ORNAMENT", false, EquipmentSlot.ANY);

    // extra
    public static final Type ARMOR = new Type(TypeSet.EXTRA, "ARMOR", false, EquipmentSlot.ARMOR, true);
    public static final Type TOOL = new Type(TypeSet.EXTRA, "TOOL", false, EquipmentSlot.MAIN_HAND);
    public static final Type CONSUMABLE = new Type(TypeSet.EXTRA, "CONSUMABLE", false, EquipmentSlot.MAIN_HAND);
    public static final Type MISCELLANEOUS = new Type(TypeSet.EXTRA, "MISCELLANEOUS", false, EquipmentSlot.MAIN_HAND);
    public static final Type GEM_STONE = new Type(TypeSet.EXTRA, "GEM_STONE", false, EquipmentSlot.OTHER);
    public static final Type SKIN = new Type(TypeSet.EXTRA, "SKIN", false, EquipmentSlot.OTHER);
    public static final Type ACCESSORY = new Type(TypeSet.EXTRA, "ACCESSORY", false, EquipmentSlot.ACCESSORY);
    public static final Type BLOCK = new Type(TypeSet.EXTRA, "BLOCK", false, EquipmentSlot.OTHER);

    private final String id;
    private String name;
    private final TypeSet set;

    public boolean isFourGUIMode() {
        return fourGUIMode;
    }

    private final boolean fourGUIMode;

    /**
     * Used for item type restrictions for gem stones to easily check if the
     * item is a weapon.
     */
    private final boolean weapon;

    private final EquipmentSlot equipType;

    /**
     * Used to display the item in the item explorer and in the item recipes
     * list in the advanced workbench. can also be edited using the config
     * files.
     */
    private ItemStack item;

    /**
     * Any type can have a subtype which basically dictates what the item type
     * does.
     */
    private Type parent;

    private UnidentifiedItem unidentifiedTemplate;

    /*
     * list of stats which can be applied onto an item which has this type. This
     * improves performance when generating an item by a significant amount.
     */
    private final List<ItemStat> available = new ArrayList<>();

    public Type(TypeSet set, String id, boolean weapon, EquipmentSlot equipType) {
        this(set, id, weapon, equipType, false);
    }
    public Type(TypeSet set, String id, boolean weapon, EquipmentSlot equipType, boolean fourGUI) {
        this.set = set;
        this.id = id.toUpperCase().replace("-", "_").replace(" ", "_");
        this.equipType = equipType;
        this.fourGUIMode = fourGUI;
        this.weapon = weapon;
    }

    public Type(TypeManager manager, ConfigurationSection config) {
        id = config.getName().toUpperCase().replace("-", "_").replace(" ", "_");

        parent = manager.get(config.getString("parent").toUpperCase().replace("-", "_").replace(" ", "_"));
        set = parent.set;
        weapon = parent.weapon;
        equipType = parent.equipType;
        this.fourGUIMode = config.getBoolean("AlternateGUIMode", parent.fourGUIMode);
    }

    public void load(ConfigurationSection config) {
        Validate.notNull(config, "Could not find config for " + getId());

        name = config.getString("name");
        Validate.notNull(name, "Could not read name");

        item = read(config.getString("display"));
        Validate.notNull(item, "Could not read item");

        (unidentifiedTemplate = new UnidentifiedItem(this)).update(config.getConfigurationSection("unident-item"));
    }

    /**
     * @deprecated Type is no longer an enum so that external plugins can register their own types. Use getId() instead
     */
    @Deprecated
    public String name() {
        return getId();
    }

    /**
     * Example <code>OFF_CATALYST</code>
     *
     * @return Internal name in uppercase and no spaces.
     */
    public String getId() {
        return id;
    }

    public TypeSet getItemSet() {
        return set;
    }

    public boolean isWeapon() {
        return weapon;
    }

    public String getName() {
        return name;
    }

    public EquipmentSlot getEquipmentType() {
        return equipType;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public boolean isSubtype() {
        return parent != null;
    }

    public Type getParent() {
        return parent;
    }

    /**
     * @return Either if the two types are the same,
     *         or if this type is a subtype of the given type.
     */
    public boolean corresponds(Type type) {
        return equals(type) || (isSubtype() && getParent().equals(type));
    }

    public boolean corresponds(TypeSet set) {
        return getItemSet() == set;
    }

    /**
     * @return The collection of all stats which can be applied onto this
     *         specific item type. This list is cached when types are being
     *         loaded and is a PRETTY GOOD performance improvement.
     */
    public List<ItemStat> getAvailableStats() {
        return available;
    }

    /**
     * @return Finds the /item config file corresponding to the item type and
     *         loads it
     */
    public ConfigFile getConfigFile() {
        return new ConfigFile("/item", getId().toLowerCase());
    }

    public UnidentifiedItem getUnidentifiedTemplate() {
        return unidentifiedTemplate;
    }

    /**
     * @param stat The stat to check
     * @return If the stat can be handled by this type of item
     * @deprecated Use ItemStat.isCompatible(Type) instead
     */
    @Deprecated
    public boolean canHave(ItemStat stat) {
        return stat.isCompatible(this);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Type && ((Type) object).id.equals(id);
    }

    private ItemStack read(String str) {
        Validate.notNull(str, "Input must not be null");

        String[] split = str.split(":");
        Material material = Material.valueOf(split[0]);
        return split.length > 1 ? MythicLib.plugin.getVersion().getWrapper().textureItem(material, Integer.parseInt(split[1])) : new ItemStack(material);
    }

    @Override
    public String toString() {
        return getId();
    }

    /**
     * Reads an ItemStack in hopes for finding its MMOItem Type.
     *
     * @param item The item to retrieve the type from
     * @return The type of the item, if it has a type.
     */
    @Nullable
    public static Type get(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        return get(NBTItem.get(item).getType());
    }

    /**
     * Used in command executors and completions for easier manipulation
     *
     * @param id The type id
     * @return The type or null if it couldn't be found
     */
    public static @Nullable
    Type get(@Nullable String id) {
        if (id == null) {
            return null;
        }
        String format = id.toUpperCase().replace("-", "_").replace(" ", "_");
        return MMOItems.plugin.getTypes().has(format) ? MMOItems.plugin.getTypes().get(format) : null;
    }

    /**
     * Used in command executors and completions for easier manipulation
     *
     * @param id The type id
     * @return If a registered type with this ID could be found
     */
    public static boolean isValid(@Nullable String id) {
        return id != null && MMOItems.plugin.getTypes().has(id.toUpperCase().replace("-", "_").replace(" ", "_"));
    }
}
