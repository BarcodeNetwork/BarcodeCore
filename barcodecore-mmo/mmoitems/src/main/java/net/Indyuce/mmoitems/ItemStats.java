package net.Indyuce.mmoitems;

import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.stat.*;
import net.Indyuce.mmoitems.stat.block.*;
import net.Indyuce.mmoitems.stat.type.*;
import org.bukkit.Material;

/**
 * A central file for the plugin since new stats are added ALL the time.
 * It is also much safer to initialize class objects OUTSIDE of the field type class.
 */
@SuppressWarnings("unused")
public class ItemStats {

    // Main Item Stats
    public static final ItemStat
            REVISION_ID = new RevisionID(),
            MATERIAL = new MaterialStat(),
            ITEM_DAMAGE = new ItemDamage(),
            CUSTOM_MODEL_DATA = new CustomModelData(),
            MAX_DURABILITY = new MaximumDurability(),
            WILL_BREAK = new LostWhenBroken(),
            NAME = new DisplayName(),
            LORE = new Lore(),
            NBT_TAGS = new NBTTags(),
            LORE_FORMAT = new LoreFormat(),

    // Block Specific Stats
    BLOCK_ID = new BlockID(),
            REQUIRED_POWER = new RequiredPower(),
            REQUIRE_POWER_TO_BREAK = new RequirePowerToBreak(),
            MIN_XP = new MinXP(),
            MAX_XP = new MaxXP(),
            GEN_TEMPLATE = new GenTemplate(),

    // Misc Stats
    DISPLAYED_TYPE = new StringStat("DISPLAYED_TYPE", VersionMaterial.OAK_SIGN.toMaterial(), "Displayed Type", new String[]{"This option will only affect the", "type displayed on the item lore."}, new String[]{"all"}),
            ENCHANTS = new Enchants(),
            HIDE_ENCHANTS = new HideEnchants(),
            PERMISSION = new Permission(),
            ITEM_PARTICLES = new ItemParticles(),
            ARROW_PARTICLES = new ArrowParticles(),
            PROJECTILE_PARTICLES = new ProjectileParticles(),

    // Disable Interaction Stats
    DISABLE_INTERACTION = new DisableStat("INTERACTION", VersionMaterial.GRASS_BLOCK.toMaterial(), "Disable Interaction", new String[]{"!block", "all"}, "Disable any unwanted interaction:", "block placement, item use..."),
            DISABLE_CRAFTING = new DisableStat("CRAFTING", VersionMaterial.CRAFTING_TABLE.toMaterial(), "Disable Crafting", "Players can't use this item while crafting."), DISABLE_SMELTING = new DisableStat("SMELTING", Material.FURNACE, "Disable Smelting", "Players can't use this item in furnaces."),
            DISABLE_SMITHING = new DisableStat("SMITHING", Material.DAMAGED_ANVIL, "Disable Smithing", "Players can't smith this item in smithing tables."),
            DISABLE_ENCHANTING = new DisableStat("ENCHANTING", VersionMaterial.ENCHANTING_TABLE.toMaterial(), "Disable Enchanting", new String[]{"!block", "all"}, "Players can't enchant this item."),
            DISABLE_REPAIRING = new DisableStat("REPAIRING", Material.ANVIL, "Disable Repairing", new String[]{"!block", "all"}, "Players can't use this item in anvils."),
            DISABLE_ARROW_SHOOTING = new DisableStat("ARROW_SHOOTING", Material.ARROW, "Disable Arrow Shooting", new Material[]{Material.ARROW}, "Players can't shoot this", "item using a bow."),
            DISABLE_ATTACK_PASSIVE = new DisableStat("ATTACK_PASSIVE", Material.BARRIER, "Disable Attack Passive", new String[]{"piercing", "slashing", "blunt"}, "Disables the blunt/slashing/piercing", "passive effects on attacks."),

    // RPG Stats
    REQUIRED_LEVEL = new RequiredLevel(),
            REQUIRED_CLASS = new RequiredClass(),
            ATTACK_DAMAGE = new AttackDamage(),
            ATTACK_SPEED = new AttackSpeed(),
            CRITICAL_STRIKE_CHANCE = new DoubleStat("CRITICAL_STRIKE_CHANCE", Material.NETHER_STAR, "Critical Strike Chance", new String[]{"Critical Strikes deal more damage.", "In % chance."}, new String[]{"!miscellaneous", "!block", "all"}),
            CRITICAL_STRIKE_POWER = new DoubleStat("CRITICAL_STRIKE_POWER", Material.NETHER_STAR, "Critical Strike Power", new String[]{"The extra damage weapon crits deals.", "(Stacks with default value)", "In %."}, new String[]{"!miscellaneous", "!block", "all"}),
            COOLDOWN_REDUCTION = new DoubleStat("COOLDOWN_REDUCTION", Material.BOOK, "Cooldown Reduction", new String[]{"Reduces cooldowns of item and player skills (%)."}),
            RANGE = new DoubleStat("RANGE", Material.STICK, "Range", new String[]{"The range of your item attacks."}, new String[]{"staff", "whip", "wand", "musket"}),
            MANA_COST = new DoubleStat("MANA_COST", VersionMaterial.LAPIS_LAZULI.toMaterial(), "Mana Cost", new String[]{"Mana spent by your weapon to be used."}, new String[]{"piercing", "slashing", "blunt", "range"}),
            STAMINA_COST = new DoubleStat("STAMINA_COST", VersionMaterial.LIGHT_GRAY_DYE.toMaterial(), "Stamina Cost", new String[]{"Stamina spent by your weapon to be used."}, new String[]{"piercing", "slashing", "blunt", "range"}),
            ARROW_VELOCITY = new DoubleStat("ARROW_VELOCITY", Material.ARROW, "Arrow Velocity", new String[]{"Determines how far your", "weapon can shoot.", "Default: 1.0"}, new String[]{"bow", "crossbow"}),
            ARROW_POTION_EFFECTS = new ArrowPotionEffects(),
            PVE_DAMAGE = new DoubleStat("PVE_DAMAGE", VersionMaterial.PORKCHOP.toMaterial(), "PvE Damage", new String[]{"Additional damage against", "non human entities in %."}, new String[]{"piercing", "slashing", "blunt", "offhand", "range", "tool", "armor", "gem_stone", "accessory"}),
            PVP_DAMAGE = new DoubleStat("PVP_DAMAGE", VersionMaterial.SKELETON_SKULL.toMaterial(), "PvP Damage", new String[]{"Additional damage", "against players in %."}, new String[]{"piercing", "slashing", "blunt", "offhand", "range", "tool", "armor", "gem_stone", "accessory"}),
            BLUNT_POWER = new DoubleStat("BLUNT_POWER", Material.IRON_AXE, "Blunt Power", new String[]{"The radius of the AoE attack.", "If set to 2.0, enemies within 2 blocks", "around your target will take damage."}, new String[]{"blunt", "gem_stone"}),
            BLUNT_RATING = new DoubleStat("BLUNT_RATING", Material.BRICK, "Blunt Rating", new String[]{"The force of the blunt attack.", "If set to 50%, enemies hit by the attack", "will take 50% of the initial damage."}, new String[]{"blunt", "gem_stone"}),
            WEAPON_DAMAGE = new DoubleStat("WEAPON_DAMAGE", Material.IRON_SWORD, "Weapon Damage", new String[]{"Additional on-hit weapon damage in %."}),
            SKILL_DAMAGE = new DoubleStat("SKILL_DAMAGE", Material.BOOK, "Skill Damage", new String[]{"Additional ability damage in %."}),
            PROJECTILE_DAMAGE = new DoubleStat("PROJECTILE_DAMAGE", Material.ARROW, "Projectile Damage", new String[]{"Additional skill/weapon projectile damage."}),
            MAGIC_DAMAGE = new DoubleStat("MAGIC_DAMAGE", Material.MAGMA_CREAM, "Magic Damage", new String[]{"Additional magic skill damage in %."}),
            PHYSICAL_DAMAGE = new DoubleStat("PHYSICAL_DAMAGE", Material.IRON_AXE, "Physical Damage", new String[]{"Additional skill/weapon physical damage."}),
            DEFENSE = new DoubleStat("DEFENSE", Material.SHIELD, "Defense", new String[]{"Reduces damage from any source.", "Formula can be set in MMOLib Config."}),
            DAMAGE_REDUCTION = new DoubleStat("DAMAGE_REDUCTION", Material.IRON_CHESTPLATE, "Damage Reduction", new String[]{"Reduces damage from any source.", "In %."}),
            FALL_DAMAGE_REDUCTION = new DoubleStat("FALL_DAMAGE_REDUCTION", Material.FEATHER, "Fall Damage Reduction", new String[]{"Reduces fall damage.", "In %."}),
            PROJECTILE_DAMAGE_REDUCTION = new DoubleStat("PROJECTILE_DAMAGE_REDUCTION", VersionMaterial.SNOWBALL.toMaterial(), "Projectile Damage Reduction", new String[]{"Reduces projectile damage.", "In %."}),
            PHYSICAL_DAMAGE_REDUCTION = new DoubleStat("PHYSICAL_DAMAGE_REDUCTION", Material.LEATHER_CHESTPLATE, "Physical Damage Reduction", new String[]{"Reduces physical damage.", "In %."}),
            FIRE_DAMAGE_REDUCTION = new DoubleStat("FIRE_DAMAGE_REDUCTION", Material.BLAZE_POWDER, "Fire Damage Reduction", new String[]{"Reduces fire damage.", "In %."}),
            MAGIC_DAMAGE_REDUCTION = new DoubleStat("MAGIC_DAMAGE_REDUCTION", Material.POTION, "Magic Damage Reduction", new String[]{"Reduce magic damage dealt by potions.", "In %."}),
            PVE_DAMAGE_REDUCTION = new DoubleStat("PVE_DAMAGE_REDUCTION", VersionMaterial.PORKCHOP.toMaterial(), "PvE Damage Reduction", new String[]{"Reduces damage dealt by mobs.", "In %."}),
            PVP_DAMAGE_REDUCTION = new DoubleStat("PVP_DAMAGE_REDUCTION", VersionMaterial.SKELETON_SKULL.toMaterial(), "PvP Damage Reduction", new String[]{"Reduces damage dealt by players", "In %."}),

    // Extra Stats
    UNBREAKABLE = new Unbreakable(),
            TIER = new ItemTierStat(),
            SET = new ItemSetStat(),
            ARMOR = new Armor(),
            ARMOR_TOUGHNESS = new ArmorToughness(),
            MAX_HEALTH = new MaxHealth(),
            UNSTACKABLE = new Unstackable(),
            MAX_MANA = new DoubleStat("MAX_MANA", VersionMaterial.LAPIS_LAZULI.toMaterial(), "Max Mana", new String[]{"Adds mana to your max mana bar."}),
            KNOCKBACK_RESISTANCE = new KnockbackResistance(),
            MOVEMENT_SPEED = new MovementSpeed(),
            TWO_HANDED = new BooleanStat("TWO_HANDED", Material.IRON_INGOT, "Two Handed", new String[]{"If set to true, a player will be", "significantly slower if holding two", "items, one being Two Handed."}, new String[]{"piercing", "slashing", "blunt", "offhand", "range", "tool"}),
            EQUIP_PRIORITY = new DoubleStat("EQUIP_PRIORITY", VersionMaterial.DIAMOND_HORSE_ARMOR.toMaterial(), "Equip Priority", new String[]{"Sets the level of priority this item has for the", "right click to swap equipped armor feature."}),
            DROP_ON_DEATH = new DisableDeathDrop(),
            DURABILITY_BAR = new DurabilityBar(),

    // Permanent Effects
    PERM_EFFECTS = new PermanentEffects(),
            GRANTED_PERMISSIONS = new GrantedPermissions(),

    // Consumable Stats
    RESTORE_HEALTH = new RestoreHealth(),
            RESTORE_FOOD = new RestoreFood(),
            RESTORE_SATURATION = new RestoreSaturation(),
            RESTORE_MANA = new RestoreMana(),
            RESTORE_STAMINA = new RestoreStamina(),
            CAN_IDENTIFY = new CanIdentify(),
            CAN_DECONSTRUCT = new CanDeconstruct(),
            CAN_DESKIN = new CanDeskin(),
            EFFECTS = new Effects(),
            SOULBINDING_CHANCE = new SoulbindingChance(),
            SOULBOUND_BREAK_CHANCE = new SoulbindingBreakChance(),
            SOULBOUND_LEVEL = new SoulboundLevel(),
          //  AUTO_SOULBIND = new BooleanStat("AUTO_SOULBIND", VersionMaterial.ENDER_EYE.toMaterial(), "Auto-Soulbind", new String[]{"Automatically soulbinds this item to", "a player when he acquires it."}, new String[]{"!consumable", "all"}),
            ITEM_COOLDOWN = new DoubleStat("ITEM_COOLDOWN", Material.COOKED_CHICKEN, "Item Cooldown", new String[]{"This cooldown applies for consumables", "as well as for item commands."}, new String[]{"!armor", "!gem_stone", "!block", "all"}),
            VANILLA_EATING_ANIMATION = new VanillaEatingAnimation(),
            GEM_COLOR = new GemColor(),
            GEM_UPGRADE_SCALING = new GemUpgradeScaling(),
            ITEM_TYPE_RESTRICTION = new ItemTypeRestriction(),
            MAX_CONSUME = new MaxConsume(),
            SUCCESS_RATE = new SuccessRate(),

    // Crafting Stats
    CRAFTING = new Crafting(),
            CRAFT_PERMISSION = new StringStat("CRAFT_PERMISSION", VersionMaterial.OAK_SIGN.toMaterial(), "Crafting Recipe Permission", new String[]{"The permission needed to craft this item.", "Changing this value requires &o/mi reload recipes&7."}, new String[]{"all"}),
    //CRAFT_AMOUNT = new DoubleStat("CRAFTED_AMOUNT", Material.WOODEN_AXE, "Crafted Amount", new String[]{"The stack count for", "this item when crafted."}, new String[]{"all"}),

    // Unique Stats
    AUTOSMELT = new BooleanStat("AUTOSMELT", Material.COAL, "Autosmelt", new String[]{"If set to true, your tool will", "automaticaly smelt mined ores."}, new String[]{"tool"}),
            BOUNCING_CRACK = new BooleanStat("BOUNCING_CRACK", VersionMaterial.COBBLESTONE_WALL.toMaterial(), "Bouncing Crack", new String[]{"If set to true, your tool will", "also break nearby blocks."}, new String[]{"tool"}),
            PICKAXE_POWER = new PickaxePower(),
            CUSTOM_SOUNDS = new CustomSounds(),
            ELEMENTS = new Elements(),
            COMMANDS = new Commands(),
            STAFF_SPIRIT = new StaffSpiritStat(),
            LUTE_ATTACK_SOUND = new LuteAttackSoundStat(),
            LUTE_ATTACK_EFFECT = new LuteAttackEffectStat(),
            NOTE_WEIGHT = new DoubleStat("NOTE_WEIGHT", VersionMaterial.MUSIC_DISC_MALL.toMaterial(), "Note Weight", new String[]{"Defines how the projectile cast", "by your lute tilts downwards."}, new String[]{"lute"}),
            REMOVE_ON_CRAFT = new BooleanStat("REMOVE_ON_CRAFT", Material.GLASS_BOTTLE, "Remove on Craft", new String[]{"If the item should be completely", "removed when used in a recipe,", "or if it should become an", "empty bottle or bucket."}, new String[]{"all"}, Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION, Material.MILK_BUCKET, Material.LAVA_BUCKET, Material.WATER_BUCKET),
            COMPATIBLE_TYPES = new CompatibleTypes(),
            COMPATIBLE_IDS = new CompatibleIds(),
            GEM_SOCKETS = new GemSockets(),
            RANDOM_UNSOCKET = new RandomUnsocket(),
    //todo CAN_UNSOCKET = new CanUnsocket(),
    REPAIR = new RepairPower(),
            REPAIR_PERCENT = new RepairPowerPercent(),
            REPAIR_TYPE = new RepairType(),
            INEDIBLE = new BooleanStat("INEDIBLE", Material.POISONOUS_POTATO, "Inedible", new String[]{"Players won't be able to right-click this consumable.", "", "No effects of it will take place."}, new String[]{"consumable"}),
            DISABLE_RIGHT_CLICK_CONSUME = new DisableStat("RIGHT_CLICK_CONSUME", Material.BAKED_POTATO, "Infinite Consume", new String[]{"consumable"}, "Players will be able to right-click this consumable", "and benefit from its effects, but it won't be consumed."),
            KNOCKBACK = new DoubleStat("KNOCKBACK", VersionMaterial.IRON_HORSE_ARMOR.toMaterial(), "Knockback", new String[]{"Using this musket will knock", "the user back if positive."}, new String[]{"musket"}),
            RECOIL = new DoubleStat("RECOIL", VersionMaterial.IRON_HORSE_ARMOR.toMaterial(), "Recoil", new String[]{"Corresponds to the shooting innacuracy."}, new String[]{"musket"}),
            HANDWORN = new BooleanStat("HANDWORN", Material.STRING, "Handworn", new String[]{"This item ignores two-handedness.", "", "Basically for a ring or a glove that you", " can wear and still have your hand free", " to carry a two-handed weapon."}, new String[]{"offhand"}),

    // Abilities & Upgrading
    ABILITIES = new Abilities(),
            UPGRADE = new UpgradeStat(),

    // Unique Item Stats
    SKULL_TEXTURE = new SkullTextureStat(),
            DYE_COLOR = new DyeColor(),
            HIDE_DYE = new HideDye(),
            POTION_EFFECTS = new PotionEffects(),
            POTION_COLOR = new PotionColor(),
            SHIELD_PATTERN = new ShieldPatternStat(),
            HIDE_POTION_EFFECTS = new HidePotionEffects(),

    // Internal Stats
    SOULBOUND = new Soulbound(),
            CUSTOM_DURABILITY = new CustomDurability(),
            STORED_TAGS = new StoredTags(),
            ITEM_LEVEL = new ItemLevel(),
            INTERNAL_REVISION_ID = new InternalRevisionID();

    /**
     * @deprecated Item damage is now {@link ItemDamage} and
     *         custom durability is now {@link CustomDurability}
     */
    @Deprecated
    public static final ItemStat DURABILITY = ITEM_DAMAGE;

}
