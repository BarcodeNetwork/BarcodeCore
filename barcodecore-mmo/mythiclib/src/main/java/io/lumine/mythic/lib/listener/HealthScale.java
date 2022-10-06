package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.Schedulers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HealthScale implements Listener {
    private final double scale;
    private final int delay;

    /**
     * @param scale Amount of total health displayed in the health bar
     *              no matter what the player's max health is. It's good
     *              when people give players 10.000 health
     * @param delay Some servers needs delay,
     *              https://git.lumine.io/mythiccraft/mythiclib/-/commit/b22d73022114d9d96ac97fb6c5605ea140a59ecb
     */
    public HealthScale(double scale, int delay) {
        this.scale = scale;
        this.delay = delay;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Schedulers.bukkit().runTaskLater(MythicLib.plugin, () -> {
            player.setHealthScaled(true);
            player.setHealthScale(scale);
        }, delay);
    }
}
