package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.skill.Skill.SkillInfo;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerPreCastSkillEvent extends PlayerDataEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final SkillInfo skill;

    private boolean cancelled;

    /**
     * Called right before a player casts a skill. This occurs before
     * checking for mana, stamina costs and ability cooldown.
     *
     * @param playerData Player casting the skill
     * @param skill      Skill being cast
     */
    public PlayerPreCastSkillEvent(PlayerData playerData, SkillInfo skill) {
        super(playerData);

        this.skill = skill;
    }

    public SkillInfo getCast() {
        return skill;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
