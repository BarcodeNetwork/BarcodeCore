package io.lumine.mythic.lib.comp.flags;

public enum CustomFlag {

    // MMOItems flags -
    MI_WEAPONS,
    MI_COMMANDS,
    MI_CONSUMABLES,
    MI_TOOLS,

    // Common flags
    MMO_ABILITIES,
    ABILITY_PVP;

    public String getPath() {
        return name().toLowerCase().replace("_", "-");
    }
}
