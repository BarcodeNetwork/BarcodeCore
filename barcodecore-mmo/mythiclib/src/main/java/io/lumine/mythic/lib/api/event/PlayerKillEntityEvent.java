package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.AttackMetadata;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public class PlayerKillEntityEvent extends MMOPlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity target;
    private final AttackMetadata attack;

    public PlayerKillEntityEvent(MMOPlayerData data, AttackMetadata attack, LivingEntity target) {
        super(data);

        this.attack = attack;
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public AttackMetadata getAttack() {
        return attack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
