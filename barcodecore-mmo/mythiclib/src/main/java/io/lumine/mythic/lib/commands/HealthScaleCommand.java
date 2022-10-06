package io.lumine.mythic.lib.commands;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealthScaleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && sender.hasPermission("mythiclib.commands.healthscale")) {
            Player player = (Player) sender;
            player.setHealthScale(Double.parseDouble(args[0]));
            player.setHealthScaled(true);
            return true;
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player != null) {
                player.setHealthScale(Double.parseDouble(args[1]));
                player.setHealthScaled(true);
                MythicLib.plugin.getLogger().info(args[0] + " Health has been scaled!");
                return true;
            } else {
                MythicLib.plugin.getLogger().info("That player does NOT exist!");
                return false;
            }
        }
    }
}
