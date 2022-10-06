package io.lumine.mythic.lib.commands;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.commands.Command;
import io.lumine.utils.text.Text;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command<MythicLib> {

    public ReloadCommand(Command<MythicLib> parent) {
        super(parent);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        getPlugin().reloadConfiguration();

        sender.sendMessage(Text.colorizeLegacy("<gold>MythicLib has been reloaded!"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermissionNode() {
        return "mythiclib.commands.reload";
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }
}
