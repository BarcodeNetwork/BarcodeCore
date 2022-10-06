package io.lumine.mythic.lib.commands;

import org.bukkit.command.CommandSender;

import io.lumine.utils.chat.ColorString;

public class CommandHelper {
    
	public static void sendHeader(CommandSender sender)	{
		sender.sendMessage(ColorString.get("&e&m----------&6&m=====&6 &lMythicLib&6 &n=====&e&n----------"));
	}
	
	public static void sendSuccess(CommandSender sender, String message)	{
		sender.sendMessage(ColorString.get("&d[MythicLib] &a" + message));
	}
	
	public static void sendError(CommandSender sender, String message)	{
		sender.sendMessage(ColorString.get("&d[MythicLib] &c" + message));
	}
	
    public static void sendCommandMessage(CommandSender player, String[] args) {
        player.sendMessage(ColorString.get("&e&m----------&6&m=====&6 &lMythicLib&6 &m=====&e&m----------"));
        player.sendMessage(" ");
        player.sendMessage(args);
        player.sendMessage(" ");
        player.sendMessage(ColorString.get("&e&m-------------&b www.mythiccraft.io &e&m-------------"));
    }
    
    public static void sendCommandArgumentMessage(CommandSender player, String[] args) {
        player.sendMessage(ColorString.get("&e&n----------&6&n=====&6 &lMythicLib&6 &n=====&e&n----------"));
        player.sendMessage(" ");
        player.sendMessage(args);
        player.sendMessage(" ");
        player.sendMessage(ColorString.get("&e&n-------------&b www.mythiccraft.io &e&n-------------"));
    }
}
