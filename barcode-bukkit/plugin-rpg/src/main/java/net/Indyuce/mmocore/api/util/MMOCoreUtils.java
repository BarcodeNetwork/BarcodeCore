package net.Indyuce.mmocore.api.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.version.VersionMaterial;
import io.lumine.mythic.utils.holograms.Hologram;
import io.lumine.mythic.utils.serialize.Position;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MMOCoreUtils {
    public static boolean pluginItem(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
    }

    public static String displayName(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName()
                : caseOnWords(item.getType().name().replace("_", " "));
    }

    public static String caseOnWords(String s) {
        StringBuilder builder = new StringBuilder(s);
        boolean isLastSpace = true;
        for (int item = 0; item < builder.length(); item++) {
            char ch = builder.charAt(item);
            if (isLastSpace && ch >= 'a' && ch <= 'z') {
                builder.setCharAt(item, (char) (ch + ('A' - 'a')));
                isLastSpace = false;
            } else
                isLastSpace = ch == ' ';
        }
        return builder.toString();
    }

    /**
     * Displays an in game indicator using a hologram. This uses
     * LumineUtils hologramFactory to summon holograms
     * <p>
     * The hologram despawns after 1sec
     *
     * @param loc     Target location
     * @param message Message to display
     */
    public static void displayIndicator(Location loc, String message) {
        Hologram holo = Hologram.create(Position.of(loc), Arrays.asList(message));
        Bukkit.getScheduler().runTaskLater(MMOCore.plugin, () -> holo.despawn(), 20);
    }

    public static boolean isPlayerHead(Material material) {
        return material == VersionMaterial.PLAYER_HEAD.toMaterial() || material == VersionMaterial.PLAYER_WALL_HEAD.toMaterial();
    }

    public static ItemStack readIcon(String string) throws IllegalArgumentException {
        String[] split = string.split(":");
        Material material = Material.valueOf(split[0].toUpperCase().replace("-", "_").replace(" ", "_"));
        return split.length > 1 ? MythicLib.plugin.getVersion().getWrapper().textureItem(material, Integer.parseInt(split[1])) : new ItemStack(material);
    }

    public static int getWorth(ItemStack[] items) {
        int t = 0;
        for (ItemStack item : items)
            if (item != null && item.getType() != Material.AIR)
                t += MythicLib.plugin.getVersion().getWrapper().getNBTItem(item).getInteger("RpgWorth") * item.getAmount();
        return t;
    }

    public static String toBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items)
                dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (Exception e) {
            return null;
        }
    }

    private static final String[] romanChars = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
    private static final int[] romanQuantities = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};

    public static String intToRoman(int input) {
        if (input < 1)
            return "<1";
        if (input > 3999)
            return ">3999";

        StringBuilder result = new StringBuilder();

        for (int j = 0; j < romanChars.length; j++) {
            int i = romanChars.length - j - 1;
            int q = romanQuantities[i];
            String c = romanChars[i];

            while (input >= q) {
                result.append(c);
                input -= q;
            }
        }

        return result.toString();
    }

    /**
     * Method to get all entities surrounding a location. This method does not
     * take every entity in the world but rather takes all the entities from the
     * 9 chunks around the entity, so even if the location is at the border of a
     * chunk (worst case border of 4 chunks), the entity will still be included
     */
    public static List<Entity> getNearbyChunkEntities(Location loc) {

        /*
         * Another method to save performance is: if an entity bounding box
         * calculation is made twice in the same tick, then the method does not
         * need to be called twice, it can utilize the same entity list since
         * the entities have not moved (e.g fireball which does 2+ calculations
         * per tick)
         *
         * Of course we're assuming that the projectile does move at a speed
         * lower than 1 chunk per second which is most likely true, otherwise
         * just use ray casting.
         */
        List<Entity> entities = new ArrayList<>();

        int cx = loc.getChunk().getX();
        int cz = loc.getChunk().getZ();

        for (int x = -1; x < 2; x++)
            for (int z = -1; z < 2; z++)
                entities.addAll(Arrays.asList(loc.getWorld().getChunkAt(cx + x, cz + z).getEntities()));

        return entities;
    }

    /**
     * @param player Player casting a spell/basic attack
     * @param target The target entity
     * @return If the player can attack the entity
     */
    public static boolean canTarget(PlayerData player, Entity target) {
        return canTarget(player, target, InteractionType.OFFENSE_SKILL);
    }

    /**
     * @param player      Player casting a spell/basic attack
     * @param target      The target entity
     * @param interaction Whether the action is an attack or a buff, this does
     *                    impact the outcome of the friendly fire check
     * @return If the player can target the entity given the attack type (buff or attack)
     */
    public static boolean canTarget(PlayerData player, Entity target, InteractionType interaction) {
        return target instanceof LivingEntity && MythicLib.plugin.getEntities().canTarget(player.getPlayer(), target, interaction);
    }

    public static void heal(LivingEntity target, double value) {
        double max = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double gain = Math.min(max, target.getHealth() + value) - target.getHealth();

        EntityRegainHealthEvent event = new EntityRegainHealthEvent(target, gain, RegainReason.CUSTOM);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled())
            target.setHealth(target.getHealth() + gain);
    }

    /**
     * Method used when mining a custom block or fishing, as the corresponding
     * interaction event is cancelled durability is not handled. This method is
     * needed and actually calls a damage event so that MMOItems can listen to
     * it
     *
     * @param player Player holding the item with durability
     * @param slot   The slot of the item with durability
     * @param damage Damage that needs to be applied
     */
    public static void decreaseDurability(Player player, EquipmentSlot slot, int damage) {
        ItemStack item = player.getInventory().getItem(slot);
        NBTItem nbt = NBTItem.get(item);

        if (!nbt.hasTag("MMOITEMS_MAX_DURABILITY")) {
            return;
        }

        PlayerItemDamageEvent event = new PlayerItemDamageEvent(player, item, damage);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;


        if (!nbt.getBoolean("Unbreakable") && item.getItemMeta() instanceof Damageable) {
            ItemMeta meta = item.getItemMeta();
            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + damage);
            item.setItemMeta(meta);
            if (((Damageable) meta).getDamage() >= item.getType().getMaxDurability()) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
                player.getInventory().setItem(slot, null);
            }
        }
    }
}
