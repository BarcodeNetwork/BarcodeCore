package io.lumine.mythic.lib.commands.mmolib;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.modifier.ModifierType;
import io.lumine.mythic.lib.api.stat.modifier.TemporaryStatModifier;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MMOTempStatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Not enough args.");
            sender.sendMessage(ChatColor.YELLOW + "Usage: /mmotempstat <player> <stat name> <value> <tick duration>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) sender.sendMessage(ChatColor.RED + "Player not found.");

        MMOPlayerData playerData = MMOPlayerData.get(target);
        StatInstance statInstance = playerData.getStatMap().getInstance(args[1]);

        ModifierType type = args[2].toCharArray()[args[2].length() - 1] == '%' ? ModifierType.RELATIVE : ModifierType.FLAT;
        double value = Double.parseDouble(type == ModifierType.RELATIVE ? args[2].substring(0, args[2].length() - 1) : args[2]);
        Long duration = Long.parseLong(args[3]);

        String tempStatKey = UUID.randomUUID().toString();
        TemporaryStatModifier tempModifier = new TemporaryStatModifier(value, duration, type, tempStatKey, statInstance);
        statInstance.addModifier(tempStatKey, tempModifier);

        return true;
    }
}
