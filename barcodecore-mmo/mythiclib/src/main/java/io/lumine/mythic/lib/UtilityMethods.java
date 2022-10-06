package io.lumine.mythic.lib;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UtilityMethods {
    private static final Random RANDOM = new Random();

    /**
     * Super useful to display enum names like DIAMOND_SWORD in chat
     *
     * @param input String with lower cases and spaces only
     * @return Same string with capital letters at the beginning of each word.
     */
    public static String caseOnWords(String input) {
        StringBuilder builder = new StringBuilder(input);
        boolean isLastSpace = true;
        for (int i = 0; i < builder.length(); i++) {
            char ch = builder.charAt(i);
            if (isLastSpace && ch >= 'a' && ch <= 'z') {
                builder.setCharAt(i, (char) (ch + ('A' - 'a')));
                isLastSpace = false;
            } else isLastSpace = ch == ' ';
        }
        return builder.toString();
    }

    public static void dropItemNaturally(Location loc, ItemStack stack) {
        double dx = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        double dy = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        double dz = ((RANDOM.nextFloat() * 0.5F) + 0.25D) / 10;
        loc.getWorld().dropItem(loc.add(0.5, 0.5, 0.5), stack).setVelocity(new Vector(dx, dy, dz));
    }

    /**
     * Used to find players in chunks around some location. This is
     * used when displaying individual holograms to a list of players.
     *
     * @param loc Target location
     * @return Players in chunks around the location
     */
    public static List<Player> getNearbyPlayers(Location loc) {
        List<Player> players = new ArrayList<>();

        int cx = loc.getChunk().getX();
        int cz = loc.getChunk().getZ();

        for (int x = -1; x < 2; x++)
            for (int z = -1; z < 2; z++)
                for (Entity target : loc.getWorld().getChunkAt(cx + x, cz + z).getEntities())
                    if (target instanceof Player)
                        players.add((Player) target);

        return players;
    }
}
