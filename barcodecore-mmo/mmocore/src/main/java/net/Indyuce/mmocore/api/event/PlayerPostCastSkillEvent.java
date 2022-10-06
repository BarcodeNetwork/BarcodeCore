package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.skill.Skill.SkillInfo;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.event.HandlerList;

public class PlayerPostCastSkillEvent extends PlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final SkillInfo skill;
    private final SkillMetadata result;

    /**
     * Called right after a player casts a skill.
     *
     * @param playerData Player casting the skill
     * @param skill      Skill being cast
     * @param result     SKill casting result
     */
    public PlayerPostCastSkillEvent(PlayerData playerData, SkillInfo skill, SkillMetadata result) {
        super(playerData);

        this.skill = skill;
        this.result = result;
    }

    public SkillInfo getCast() {
        return skill;
    }

    public SkillMetadata getResult() {
        return result;
    }

    public boolean wasSuccessful() {
        return result.isSuccessful();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
