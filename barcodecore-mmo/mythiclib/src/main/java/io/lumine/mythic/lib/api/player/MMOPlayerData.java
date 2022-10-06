package io.lumine.mythic.lib.api.player;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.comp.flags.CustomFlag;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.listener.PlayerListener;
import io.lumine.mythic.lib.player.cooldown.CooldownMap;
import io.lumine.mythic.lib.player.cooldown.CooldownType;
import io.lumine.mythic.lib.skill.metadata.TriggerMetadata;
import io.lumine.mythic.lib.skill.trigger.PassiveSkill;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MMOPlayerData {
    private final UUID uuid;

    private Player player;

    /**
     * Last time the player either logged in or logged out.
     */
    private long lastLogActivity;

    // Data saved till next server restart
    private final CooldownMap basicCooldowns = new CooldownMap();
    private final StatMap stats = new StatMap(this);
    private final Set<PassiveSkill> passiveSkills = new HashSet<>();

    private static final Map<UUID, MMOPlayerData> data = new HashMap<>();

    private MMOPlayerData(Player player) {
        this.uuid = player.getUniqueId();

        this.player = player;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * @return The player's StatMap which can be used by any other plugins to
     *         apply stat modifiers to ANY MMOItems/MMOCore/external stats,
     *         calculate stat values, etc.
     */
    public StatMap getStatMap() {
        return stats;
    }

    /**
     * @return All active skill triggers
     */
    public Set<PassiveSkill> getPassiveSkills() {
        return passiveSkills;
    }

    /**
     * Unregisters active skill triggers with a specific key
     *
     * @param key
     */
    public void unregisterSkillTriggers(String key) {
        Iterator<PassiveSkill> iter = passiveSkills.iterator();
        while (iter.hasNext()) {
            PassiveSkill trigger = iter.next();
            if (trigger.getKey().equals(key))
                iter.remove();
        }
    }

    /**
     * Registers as active a skill trigger. It can be unregistered
     * later if necessary using {@link #unregisterSkillTriggers(String)}.
     * From the time where that method is called, performing an action will
     * cause the saved TriggeredSkill to be executed.
     *
     * @param trigger Trigger to register
     */
    public void registerSkillTrigger(PassiveSkill trigger) {
        passiveSkills.add(trigger);
    }

    /**
     * Used to trigger skills with no attack metadata. This caches the player
     * statistics and create an attack metadata.
     *
     * @param triggerType Action performed to trigger the skills
     * @param target      The potential target to cast the skill onto
     */
    public void triggerSkills(TriggerType triggerType, Entity target) {
        triggerSkills(triggerType, new AttackMetadata(new DamageMetadata(), stats.cache(EquipmentSlot.MAIN_HAND)), target);
    }

    /**
     * Trigger skills with an attack metadata.
     *
     * @param triggerType    Action performed to trigger the skills
     * @param target         The potential target to cast the skill onto
     * @param attackMetadata The attack being performed
     */
    public void triggerSkills(TriggerType triggerType, AttackMetadata attackMetadata, Entity target) {
        triggerSkills(triggerType, attackMetadata, target, attackMetadata.getStats().getData().getPassiveSkills());
    }

    /**
     * Trigger a specific set of skills, with an attack metadata.
     *
     * @param triggerType    Action performed to trigger the skills
     * @param target         The potential target to cast the skill onto
     * @param attackMetadata The attack being performed
     * @param skills         The list of skills currently active for the player
     */
    public void triggerSkills(TriggerType triggerType, AttackMetadata attackMetadata, Entity target, Collection<PassiveSkill> skills) {
        if (!MythicLib.plugin.getFlags().isFlagAllowed(attackMetadata.getPlayer(), CustomFlag.MMO_ABILITIES))
            return;

        TriggerMetadata triggerMeta = new TriggerMetadata(attackMetadata, target);

        for (PassiveSkill trigger : skills)
            if (trigger.getType() == triggerType)
                trigger.getTriggeredSkill().execute(triggerMeta);
    }

    /**
     * @return The last time, in millis, the player logged in or out
     * @deprecated Use {@link #getLastLogActivity()} instead
     */
    @Deprecated
    public long getLastLogin() {
        return getLastLogActivity();
    }

    /**
     * @return The last time, in millis, the player logged in or out
     */
    public long getLastLogActivity() {
        return lastLogActivity;
    }

    /**
     * This method simply checks if the cached Player instance is null
     * because MythicLib uncaches it when the player leaves for memory purposes.
     *
     * @return If the player is currently online.
     */
    public boolean isOnline() {
        return player != null;
    }

    /**
     * Throws an IAE if the player is currently not online
     * OR if the Player instance was not cached in yet.
     * <p>
     * MythicLib updates the Player instance on event priority LOW
     * using {@link PlayerJoinEvent} here: {@link PlayerListener}
     *
     * @return Returns the corresponding Player instance.
     */
    public Player getPlayer() {
        Validate.notNull(player, "Player is offline");
        return player;
    }

    /**
     * Caches a new Player instance and refreshes the last log activity
     *
     * @param player Player instance to cache
     */
    public void updatePlayer(Player player) {
        this.player = player;
        this.lastLogActivity = System.currentTimeMillis();
    }

    /**
     * Used when damage mitigation or a crit occurs to apply cooldown
     *
     * @param cd    The type of mitigation
     * @param value Mitigation cooldown in seconds
     */
    public void applyCooldown(CooldownType cd, double value) {
        basicCooldowns.applyCooldown(cd.name(), value);
    }

    /**
     * @param cd Cooldown type
     * @return If the mecanic is currently on cooldown for the player
     */
    public boolean isOnCooldown(CooldownType cd) {
        return basicCooldowns.isOnCooldown(cd.name());
    }

    /**
     * Cooldown maps centralize cooldowns in MythicLib for easier use.
     * Can be used for item cooldows, item abilities, MMOCore player
     * skills or any other external plugin
     *
     * @return The main player's cooldown map
     */
    public CooldownMap getCooldownMap() {
        return basicCooldowns;
    }

    /**
     * Called everytime a player enters the server. If the
     * resource data is not initialized yet, initializes it.
     * <p>
     * This is called async using {@link AsyncPlayerPreLoginEvent} which does
     * not provide a Player instance, meaning the cached Player instance is NOT
     * loaded yet. It is only loaded when the player logs in using {@link PlayerJoinEvent}
     *
     * @param player Player whose data should be initialized
     */
    public static MMOPlayerData setup(Player player) {
        MMOPlayerData found = data.get(player.getUniqueId());

        // Not loaded yet
        if (found == null) {
            MMOPlayerData playerData = new MMOPlayerData(player);
            data.put(player.getUniqueId(), playerData);
            return playerData;
        }

        found.updatePlayer(player);
        return found;
    }

    /**
     * This essentially checks if a player logged in since the last time the
     * server started/was reloaded.
     *
     * @param uuid The player UUID to check
     * @return If the MMOPlayerData instance is loaded for a specific
     *         player
     */
    public static boolean isLoaded(UUID uuid) {
        return data.containsKey(uuid);
    }

    public static MMOPlayerData get(@NotNull OfflinePlayer player) {
        return data.get(player.getUniqueId());
    }

    public static MMOPlayerData get(UUID uuid) {
        return Objects.requireNonNull(data.get(uuid), "Player data not loaded");
    }

    /**
     * This is being used to easily check if an online player corresponds to
     * a real player or a Citizens NPC. Citizens NPCs do not have any player
     * data associated to them
     *
     * @return Checks if plater data is loaded for a specific player UID
     */
    public static boolean has(Player player) {
        return has(player.getUniqueId());
    }

    /**
     * This is being used to easily check if an online player corresponds to
     * a real player or a Citizens NPC. Citizens NPCs do not have any player
     * data associated to them
     *
     * @return Checks if plater data is loaded for a specific player UID
     */
    public static boolean has(UUID uuid) {
        return data.containsKey(uuid);
    }

    /**
     * @return Currently loaded MMOPlayerData instances. This can be used to
     *         apply things like resource regeneration or other runnable based
     *         tasks instead of looping through online players and having to
     *         resort to a map-lookup-based get(Player) call
     */
    public static Collection<MMOPlayerData> getLoaded() {
        return data.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MMOPlayerData)) return false;

        MMOPlayerData that = (MMOPlayerData) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}

