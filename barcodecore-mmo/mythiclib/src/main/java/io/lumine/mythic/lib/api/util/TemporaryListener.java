package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class TemporaryListener implements Listener {

    /**
     * Handler lists which must be called when the temporary listener is closed
     * so that the listener is entirely unregistered
     */
    private final HandlerList[] lists;

    /**
     * Sometimes the close method is called twice because of a safe delayed task
     * not being cancelled when the listener is closed. It's set to true after
     * being closed at least once
     */
    private boolean closed;

    public TemporaryListener(HandlerList... events) {
        this(MythicLib.plugin, events);
    }

    /**
     * Used to register listeners which should be unregistered after a specific
     * period of time
     *
     * @param plugin Plugin registering the listener
     * @param events Handler lists which will be used to unregistered the
     *               listener
     */
    public TemporaryListener(JavaPlugin plugin, HandlerList... events) {
        lists = events;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Unregisters the temporary listener after some delay
     *
     * @param duration Delay before unregistration
     */
    public void close(long duration) {
        Bukkit.getScheduler().runTaskLater(MythicLib.plugin, (Runnable) this::close, duration);
    }

    /**
     * Immediately unregisters the listener
     *
     * @return If it's the first time this method is called for this instance
     */
    public boolean close() {
        if (closed)
            return false;

        closed = true;
        whenClosed();
        for (HandlerList list : lists)
            list.unregister(this);
        return true;
    }

    /**
     * Called when the listener is closed for the first time.
     * If {@link #close()} is called a second time after the listener
     * was already closed, this method will NOT get called a second time
     */
    public abstract void whenClosed();
}
