package io.lumine.mythic.lib.api.explorer;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Function;

public class ChatInput implements Listener {
    private final Player player;

    /*
     * if output is null, then it means the player typed "cancel" and wants to
     * go back. if the function returns false, then the chat input is not
     * cancelled.
     */
    private final Function<String, Boolean> output;

    public ChatInput(Player player, Function<String, Boolean> output) {
        this.player = player;
        this.output = output;
        Bukkit.getPluginManager().registerEvents(this, MythicLib.plugin);
    }

    public void close() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void a(AsyncPlayerChatEvent event) {
        if (event.getPlayer().equals(player)) {
            event.setCancelled(true);
            if (output.apply(event.getMessage().equalsIgnoreCase("cancel") ? null : event.getMessage()))
                close();
        }
    }

    @EventHandler
    public void b(InventoryCloseEvent event) {
        if (event.getPlayer().equals(player))
            close();
    }

    @EventHandler
    public void c(InventoryOpenEvent event) {
        if (event.getPlayer().equals(player))
            close();
    }
}
