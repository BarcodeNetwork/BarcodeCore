package io.lumine.mythic.lib.skill.trigger;

/**
 * There is one skill trigger instance per passive skill the player
 * has. A passive skill can be registered by MMOItems items or MMOCore
 * passive skills.
 *
 * @author indyuce
 */
public class PassiveSkill {

    /**
     * Triggered whenever the action is performed
     */
    private final TriggeredSkill triggered;

    /**
     * Identifier given to skills to differenciate every of them.
     * Every plugin like MMOItems has a key to be able to manipulate
     * the triggers that were registered on the player at any time
     */
    private final String key;

    private final TriggerType type;

    public PassiveSkill(String key, TriggerType type, TriggeredSkill triggered) {
        this.key = key;
        this.type = type;
        this.triggered = triggered;
    }

    public TriggeredSkill getTriggeredSkill() {
        return triggered;
    }

    public TriggerType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
