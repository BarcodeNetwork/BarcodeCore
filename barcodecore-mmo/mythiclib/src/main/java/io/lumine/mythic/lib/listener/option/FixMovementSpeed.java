package io.lumine.mythic.lib.listener.option;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FixMovementSpeed implements Listener {

    @EventHandler
    public void a(PlayerJoinEvent event) {
        event.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(.1);
    }
}
