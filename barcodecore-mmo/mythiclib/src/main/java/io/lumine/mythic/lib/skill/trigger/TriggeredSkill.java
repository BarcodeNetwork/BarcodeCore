package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.skill.metadata.TriggerMetadata;
import org.jetbrains.annotations.Nullable;

/**
 * This is purely an interface between MythicLib triggers
 * and MMOCore skills/MMOItems abilities.
 *
 * @author indyuce
 */
@FunctionalInterface
public interface TriggeredSkill {

    /**
     * Called when the skill is triggered.
     *
     * @param casterMeta Information about the player casting the skill
     */
    void execute(@Nullable TriggerMetadata casterMeta);
}
