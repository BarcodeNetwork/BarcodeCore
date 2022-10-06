package io.lumine.mythic.lib.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.commands.Command;
import io.lumine.utils.text.Text;

public class BaseCommand extends Command<MythicLib> {

    public BaseCommand(MythicLib plugin) {
        super(plugin);
        
        addSubCommands(
                new ReloadCommand(this)
            );
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        String[] messages = {
                Text.colorizeLegacy("<aqua>/mythiclib <gray>- <white>Prints this message!"),
              };
        CommandHelper.sendCommandMessage(sender, messages);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) { return SilentNumbers.toArrayList("reload"); }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
