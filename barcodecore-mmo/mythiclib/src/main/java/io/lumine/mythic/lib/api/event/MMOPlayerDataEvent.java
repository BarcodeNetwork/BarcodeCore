package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.player.PlayerEvent;

public abstract class MMOPlayerDataEvent extends PlayerEvent {
    private final MMOPlayerData playerData;

    public MMOPlayerDataEvent(MMOPlayerData playerData) {
        super(playerData.getPlayer());

        this.playerData = playerData;
    }

    public MMOPlayerData getData() {
        return playerData;
    }
}
