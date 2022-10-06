package io.lumine.mythic.lib.commands.mmolib;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MMOLibCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MythicLib.plugin.reload();
        sender.sendMessage(ChatColor.GREEN + "> MythicLib reloaded!");
        return true;
    }
}