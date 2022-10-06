package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.UtilityMethods;

public enum TriggerType {

    // Combat
    KILL_ENTITY,
    ATTACK,
    DAMAGED,
    DAMAGED_BY_ENTITY,
    DEATH,

    // Bow
    SHOOT_BOW,
    ARROW_TICK,
    ARROW_HIT,
    ARROW_LAND,

    // Trident
    SHOOT_TRIDENT,
    TRIDENT_TICK,
    TRIDENT_HIT,
    TRIDENT_LAND,

    // Clicks
    RIGHT_CLICK(false),
    LEFT_CLICK(false),
    SHIFT_RIGHT_CLICK(false),
    SHIFT_LEFT_CLICK(false),

    // Misc
    LOGIN,
    SNEAK(false);

    /**
     * When set to false, any skill with this trigger type should
     * send a message to the player if this skill cannot be used.
     */
    private final boolean silent;

    TriggerType() {
        this(true);
    }

    TriggerType(boolean silent) {
        this.silent = silent;
    }

    public boolean isSilent() {
        return silent;
    }

    public String getName() {
        return UtilityMethods.caseOnWords(name().toLowerCase().replace("_", " "));
    }

    public String getLowerCaseId() {
        return name().toLowerCase().replace("_", "-");
    }

    public static TriggerType safeValueOf(String format) {
        for (TriggerType type : values())
            if (type.name().equals(format))
                return type;
        return null;
    }
}
