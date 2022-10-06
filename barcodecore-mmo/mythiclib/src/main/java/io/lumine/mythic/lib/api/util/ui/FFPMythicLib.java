package io.lumine.mythic.lib.api.util.ui;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * 'Whats the difference between a villain and a super villain?'
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class FFPMythicLib extends FriendlyFeedbackPalette {

    @NotNull @Override public String getBodyFormat() { return "§x§8§7§8§5§7§2"; }
    @NotNull @Override public String consoleBodyFormat() { return ChatColor.GRAY.toString(); }

    @NotNull @Override public String getExampleFormat() { return "§x§f§f§d§8§3§b"; }
    @NotNull @Override public String consoleExampleFormat() { return ChatColor.GOLD.toString(); }

    @NotNull @Override public String getInputFormat() { return "§x§f§c§f§f§7§8"; }
    @NotNull @Override public String consoleInputFormat() { return ChatColor.YELLOW.toString(); }

    @NotNull @Override public String getResultFormat() { return "§x§d§9§f§f§6§9"; }
    @NotNull @Override public String consoleResultFormat() { return ChatColor.AQUA.toString(); }

    @NotNull @Override public String getSuccessFormat() { return "§x§0§0§f§f§0§0"; }
    @NotNull @Override public String consoleSuccessFormat() { return ChatColor.GREEN.toString(); }

    @NotNull @Override public String getFailureFormat() { return "§x§f§f§4§7§4§7"; }
    @NotNull @Override public String consoleFailureFormat() { return ChatColor.RED.toString(); }

    @NotNull @Override public String getRawPrefix() { return "§x§5§7§3§9§2§5[§x§f§f§a§e§0§0MythicLib#s§x§5§7§3§9§2§5] "; }
    @NotNull @Override public String getRawPrefixConsole() { return "§8[§6MythicLib#s§8] "; }

    @NotNull @Override public String getSubdivisionFormat() { return "§x§e§b§8§2§3§1§o"; }
    @NotNull @Override public String consoleSubdivisionFormat() { return "§c§o"; }

    private final static FFPMythicLib ffp = new FFPMythicLib();
    public static FFPMythicLib get() { return ffp;}
}
