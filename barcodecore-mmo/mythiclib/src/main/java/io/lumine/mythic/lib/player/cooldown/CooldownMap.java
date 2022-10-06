package io.lumine.mythic.lib.player.cooldown;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CooldownMap {
    private final Map<String, CooldownInfo> map = new HashMap<>();

    /**
     * Applies a cooldown
     *
     * @param obj      The skill or action
     * @param cooldown Initial skill or action cooldown
     * @return The newly registered cooldown info
     */
    public CooldownInfo applyCooldown(CooldownObject obj, double cooldown) {
        return applyCooldown(obj.getCooldownPath(), cooldown);
    }

    /**
     * Applies a cooldown
     *
     * @param path     The skill or action path, must be completely unique
     * @param cooldown Initial skill or action cooldown
     * @return The newly registered cooldown info
     */
    public CooldownInfo applyCooldown(String path, double cooldown) {
        CooldownInfo newest = new CooldownInfo(cooldown);
        map.put(path, newest);
        return newest;
    }

    /**
     * @return Finds the cooldown info for a specific action or skill
     */
    @Nullable
    public CooldownInfo getInfo(CooldownObject obj) {
        return getInfo(obj.getCooldownPath());
    }

    /**
     * @return Finds the cooldown info for a specific action or skill
     */
    @Nullable
    public CooldownInfo getInfo(String path) {
        return map.get(path);
    }

    /**
     * @param obj The skill or action
     * @return If the mechanic can be used by the player
     */
    public boolean isOnCooldown(CooldownObject obj) {
        return isOnCooldown(obj.getCooldownPath());
    }

    /**
     * @param path The skill or action path, must be completely unique
     * @return If the mechanic can be used by the player
     */
    public boolean isOnCooldown(String path) {
        CooldownInfo found = map.get(path);
        return found != null && !found.hasEnded();
    }
}
