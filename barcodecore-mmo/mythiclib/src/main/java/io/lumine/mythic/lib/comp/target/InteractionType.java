package io.lumine.mythic.lib.comp.target;

public enum InteractionType {

    /**
     * Any skill that damages or harms another entity
     */
    OFFENSE_SKILL,

    /**
     * Any skill that applies a buffs or regenerates
     * some resource to an entity
     */
    SUPPORT_SKILL,

    /**
     * Any other offense based action like melee attacks.
     */
    OFFENSE_ACTION,

    /**
     * Any other support/friendly action, not being used in MMOCore
     * nor in MMOItems
     */
    SUPPORT_ACTION;

    public boolean isSkill() {
        return this == OFFENSE_SKILL || this == SUPPORT_SKILL;
    }

    public boolean isOffense() {
        return this == OFFENSE_ACTION || this == OFFENSE_SKILL;
    }
}
