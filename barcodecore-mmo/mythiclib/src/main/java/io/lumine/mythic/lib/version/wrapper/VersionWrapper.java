package io.lumine.mythic.lib.version.wrapper;

import io.lumine.mythic.lib.api.MMORayTraceResult;
import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

public interface VersionWrapper {
    default MMORayTraceResult rayTrace(Player player, double range, Predicate<Entity> predicate) {
        return rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), range, predicate);
    }

    default MMORayTraceResult rayTrace(Player player, Vector direction, double range, Predicate<Entity> predicate) {
        return rayTrace(player.getEyeLocation(), direction, range, predicate);
    }

    MMORayTraceResult rayTrace(Location loc, Vector direction, double range, Predicate<Entity> predicate);

    NBTItem copyTexture(NBTItem item);

    ItemStack textureItem(Material material, int model);

    NBTItem getNBTItem(ItemStack item);

    void sendActionBar(Player player, String message);

    void sendJson(Player player, String message);

    void playArmAnimation(Player player);

    boolean isInBoundingBox(Entity entity, Location loc);
}
