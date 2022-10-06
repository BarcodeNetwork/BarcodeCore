package io.lumine.mythic.lib.commands.mmolib;

import io.lumine.mythic.lib.api.explorer.AttributeData;
import io.lumine.mythic.lib.gui.AttributeExplorer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ExploreAttributesCommand implements CommandExecutor {

    /*
     * using strings to store item makes compatibility with older and newer
     * stats much easier.
     */
    public static final Map<String, AttributeData> data = new HashMap<>();
    public static final DecimalFormat format = new DecimalFormat("0.#####");

    public ExploreAttributesCommand() {
        data.put("ARMOR", new AttributeData(Material.IRON_CHESTPLATE, "Armor bonus of an Entity."));
        data.put("ARMOR_TOUGHNESS", new AttributeData(Material.GOLDEN_CHESTPLATE, "Armor durability bonus of an Entity."));
        data.put("ATTACK_DAMAGE", new AttributeData(Material.IRON_SWORD, "Attack damage of an Entity."));
        data.put("ATTACK_SPEED", new AttributeData(Material.LIGHT_GRAY_DYE, "Attack speed of an Entity."));
        data.put("KNOCKBACK_RESISTANCE", new AttributeData(Material.TNT_MINECART, "Resistance of an Entity to knockback."));
        data.put("LUCK", new AttributeData(Material.GRASS, "Luck bonus of an Entity."));
        data.put("MAX_HEALTH", new AttributeData(Material.GOLDEN_APPLE, "Maximum health of an Entity."));
        data.put("MOVEMENT_SPEED", new AttributeData(Material.LEATHER_BOOTS, "Movement speed of an Entity."));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "> Only players may use this command.");
            return true;
        }

        Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "> Could not find player called " + args[0] + ".");
            return true;
        }

        new AttributeExplorer((Player) sender, target).open();
        return true;
    }

    public static boolean isMetaItem(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
    }
}
