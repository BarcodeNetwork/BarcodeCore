package net.Indyuce.mmocore.api.player;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.cooldown.CooldownInfo;
import io.lumine.mythic.lib.player.cooldown.CooldownMap;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.ConfigMessage;
import net.Indyuce.mmocore.api.event.*;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttribute;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttributes;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.Indyuce.mmocore.api.player.profess.SavedClassInformation;
import net.Indyuce.mmocore.api.player.profess.Subclass;
import net.Indyuce.mmocore.api.player.profess.resource.PlayerResource;
import net.Indyuce.mmocore.api.player.stats.PlayerStats;
import net.Indyuce.mmocore.api.player.stats.StatType;
import net.Indyuce.mmocore.api.util.Closable;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.api.util.math.particle.SmallParticleEffect;
import net.Indyuce.mmocore.experience.EXPSource;
import net.Indyuce.mmocore.experience.PlayerProfessions;
import net.Indyuce.mmocore.listener.SpellCast.SkillCasting;
import net.Indyuce.mmocore.manager.SoundManager;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill;
import net.Indyuce.mmocore.skill.Skill.SkillInfo;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import net.Indyuce.mmocore.skill.metadata.SkillMetadata.CancelReason;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Level;

public class PlayerData extends OfflinePlayerData implements Closable {

    /**
     * Corresponds to the MythicLib player data. It is used to keep
     * track of the Player instance corresponding to that player data,
     * as well as other things like the last time the player logged in/out
     */
    private final MMOPlayerData mmoData;

    /**
     * Can be null, the {@link #getProfess()} method will return the
     * player class, or the default one if this field is null.
     */
    @Nullable
    private PlayerClass profess;
    private int level, experience, skillPoints, attributePoints, attributeReallocationPoints;// skillReallocationPoints,
    private double mana, stamina;

    private final PlayerStats playerStats;
    private final Map<String, Integer> skills = new HashMap<>();
    private final List<SkillInfo> boundSkills = new ArrayList<>();
    private final PlayerProfessions collectSkills = new PlayerProfessions(this);
    private final PlayerAttributes attributes = new PlayerAttributes(this);
    private final Map<String, SavedClassInformation> classSlots = new HashMap<>();

    private long lastWaypoint, lastFriendRequest, actionBarTimeOut, lastLootChest;

    // NON-FINAL player data stuff made public to facilitate field change
    public int skillGuiDisplayOffset;
    public SkillCasting skillCasting;
    public boolean noCooldown;
    public CombatRunnable combat;

    /**
     * Player data is stored in the data map before it's actually fully loaded
     * so that external plugins don't necessarily have to listen to the PlayerDataLoadEvent.
     */
    private boolean fullyLoaded = false;

    public PlayerData(MMOPlayerData mmoData) {
        super(mmoData.getUniqueId());

        this.mmoData = mmoData;
        this.playerStats = new PlayerStats(this);
    }

    /**
     * Update all references after /mmocore reload so there can be garbage
     * collection with old plugin objects like class or skill instances.
     */
    public void update() {

        try {
            profess = profess == null ? null : MMOCore.plugin.classManager.get(profess.getId());
            getStats().updateStats();
        } catch (NullPointerException exception) {
            MMOCore.log(Level.SEVERE, "[Userdata] Could not find class " + getProfess().getId() + " while refreshing player data.");
        }

        int j = 0;
        while (j < boundSkills.size())
            try {
                boundSkills.set(j, getProfess().getSkill(boundSkills.get(j).getSkill()));
                j++;
            } catch (NullPointerException npe1) {
                boundSkills.remove(j);
                try {
                    MMOCore.log(Level.SEVERE, "[Userdata] Could not find skill " + boundSkills.get(j).getSkill().getId() + " in class "
                            + getProfess().getId() + " while refreshing player data.");
                } catch (NullPointerException npe2) {
                    MMOCore.log(Level.SEVERE,
                            "[Userdata] Could not find unidentified skill in class " + getProfess().getId() + " while refreshing player data.");
                }
            }
    }

    @Override
    public void close() {

    }

    public MMOPlayerData getMMOPlayerData() {
        return mmoData;
    }

    public PlayerProfessions getCollectionSkills() {
        return collectSkills;
    }

    public Player getPlayer() {
        return mmoData.getPlayer();
    }

    @Override
    public long getLastLogin() {
        return mmoData.getLastLogin();
    }

    @Override
    public int getLevel() {
        return Math.max(1, level);
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    /**
     * @return Experience needed in order to reach next level
     */
    public int getLevelUpExperience() {
        return getProfess().getExpCurve().getExperience(getLevel() + 1);
    }

    // public int getSkillReallocationPoints() {
    // return skillReallocationPoints;
    // }

    public int getAttributePoints() {
        return attributePoints;
    }

    public int getAttributeReallocationPoints() {
        return attributeReallocationPoints;
    }

    public boolean isOnline() {
        return mmoData.isOnline();
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
        getStats().updateStats();
    }

    public void takeLevels(int value) {
        this.level = Math.max(1, level - value);
        getStats().updateStats();
    }

    public void giveLevels(int value, EXPSource source) {
        int total = 0;
        while (value-- > 0)
            total += getProfess().getExpCurve().getExperience(getLevel() + value + 1);
        giveExperience(total, source);
    }

    public void setExperience(int value) {
        experience = Math.max(0, value);
        refreshVanillaExp();
    }

    /**
     * Class experience can be displayed on the player's exp bar.
     * This updates the exp bar to display the player class level and exp.
     */
    public void refreshVanillaExp() {
        if (!isOnline() || !MMOCore.plugin.configManager.overrideVanillaExp)
            return;

        getPlayer().setLevel(getLevel());
        getPlayer().setExp(Math.max(0, Math.min(1, (float) experience / (float) getLevelUpExperience())));
    }

    // public void setSkillReallocationPoints(int value) {
    // skillReallocationPoints = Math.max(0, value);
    // }

    public void setAttributePoints(int value) {
        attributePoints = Math.max(0, value);
    }

    public void setAttributeReallocationPoints(int value) {
        attributeReallocationPoints = Math.max(0, value);
    }

    public void setSkillPoints(int value) {
        skillPoints = Math.max(0, value);
    }

    public boolean hasSavedClass(PlayerClass profess) {
        return classSlots.containsKey(profess.getId());
    }

    public Set<String> getSavedClasses() {
        return classSlots.keySet();
    }

    public SavedClassInformation getClassInfo(PlayerClass profess) {
        return getClassInfo(profess.getId());
    }

    public SavedClassInformation getClassInfo(String profess) {
        return classSlots.get(profess);
    }

    public void applyClassInfo(PlayerClass profess, SavedClassInformation info) {
        classSlots.put(profess.getId(), info);
    }

    public void unloadClassInfo(PlayerClass profess) {
        classSlots.remove(profess.getId());
    }

    public long getWaypointCooldown() {
        return Math.max(0, lastWaypoint + 5000 - System.currentTimeMillis());
    }

    /**
     * Handles the per-player loot chest cooldown system. That is to
     * reduce the rik of spawning multiple loot chests around the same
     * player in a row, which could be game breaking.
     *
     * @return If a random chest can spawn around that player
     */
    public boolean canSpawnLootChest() {
        return lastLootChest + MMOCore.plugin.configManager.lootChestPlayerCooldown < System.currentTimeMillis();
    }

    public void applyLootChestCooldown() {
        lastLootChest = System.currentTimeMillis();
    }

    /**
     * @deprecated Provide a heal reason with {@link #heal(double, PlayerResourceUpdateEvent.UpdateReason)}
     */
    @Deprecated
    public void heal(double heal) {
        this.heal(heal, PlayerResourceUpdateEvent.UpdateReason.OTHER);
    }

    public void heal(double heal, PlayerResourceUpdateEvent.UpdateReason reason) {
        if (!isOnline())
            return;

        // Avoid calling an useless event
        double max = getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double newest = Math.max(0, Math.min(getPlayer().getHealth() + heal, max));
        if (getPlayer().getHealth() == newest)
            return;

        PlayerResourceUpdateEvent event = new PlayerResourceUpdateEvent(this, PlayerResource.HEALTH, heal, reason);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        // Use updated amount from event
        getPlayer().setHealth(Math.max(0, Math.min(getPlayer().getHealth() + event.getAmount(), max)));
    }

    public void log(Level level, String message) {
        MMOCore.plugin.getLogger().log(level, "[Userdata:" + (isOnline() ? getPlayer().getName() : "Offline Player") + "] " + message);
    }

    public boolean hasReachedMaxLevel() {
        return getProfess().getMaxLevel() > 0 && getLevel() >= getProfess().getMaxLevel();
    }

    /**
     * Gives experience without displaying an EXP hologram around the player
     *
     * @param value  Experience to give the player
     * @param source How the player earned experience
     */
    public void giveExperience(int value, EXPSource source) {
        giveExperience(value, source, null);
    }

    /**
     * Called when giving experience to a player
     *
     * @param value            Experience to give the player
     * @param source           How the player earned experience
     * @param hologramLocation Location used to display the hologram. If it's null, no
     *                         hologram will be displayed
     */
    public void giveExperience(double value, EXPSource source, @Nullable Location hologramLocation) {
        if (hasReachedMaxLevel()) {
            setExperience(0);
            return;
        }

        // Experience hologram
        if (hologramLocation != null && isOnline())
            MMOCoreUtils.displayIndicator(hologramLocation.add(.5, 1.5, .5), MMOCore.plugin.configManager.getSimpleMessage("exp-hologram", "exp", "" + value).message());

        value = MMOCore.plugin.boosterManager.calculateExp(null, value);
        value *= 1 + getStats().getStat(StatType.ADDITIONAL_EXPERIENCE) / 100;

        PlayerExperienceGainEvent event = new PlayerExperienceGainEvent(this, (int) value, source);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        experience += event.getExperience();

        // Calculate the player's next level
        int oldLevel = level, needed;
        while (experience >= (needed = getLevelUpExperience())) {

            if (hasReachedMaxLevel()) {
                experience = 0;
                break;
            }

            experience -= needed;
            level = getLevel() + 1;
        }

        if (level > oldLevel) {
            Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(this, null, oldLevel, level));
            if (isOnline()) {
                new ConfigMessage("level-up").addPlaceholders("level", "" + level).send(getPlayer());
                MMOCore.plugin.soundManager.play(getPlayer(), SoundManager.SoundEvent.LEVEL_UP);
                new SmallParticleEffect(getPlayer(), Particle.SPELL_INSTANT);
            }
            getStats().updateStats();
        }

        refreshVanillaExp();
    }

    public int getExperience() {
        return experience;
    }

    @Override
    @NotNull
    public PlayerClass getProfess() {
        return profess == null ? MMOCore.plugin.classManager.getDefaultClass() : profess;
    }

    /**
     * @deprecated Provide reason with {@link #giveMana(double, PlayerResourceUpdateEvent.UpdateReason)}
     */
    @Deprecated
    public void giveMana(double amount) {
        giveMana(amount, PlayerResourceUpdateEvent.UpdateReason.OTHER);
    }

    public void giveMana(double amount, PlayerResourceUpdateEvent.UpdateReason reason) {

        // Avoid calling useless event
        double max = getStats().getStat(StatType.MAX_MANA);
        double newest = Math.max(0, Math.min(mana + amount, max));
        if (mana == newest)
            return;

        PlayerResourceUpdateEvent event = new PlayerResourceUpdateEvent(this, PlayerResource.MANA, amount, reason);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        // Use updated amount from Bukkit event
        mana = Math.max(0, Math.min(mana + event.getAmount(), max));
    }

    /**
     * @deprecated Provide reason with {@link #giveStamina(double, PlayerResourceUpdateEvent.UpdateReason)}
     */
    @Deprecated
    public void giveStamina(double amount) {
        giveStamina(amount, PlayerResourceUpdateEvent.UpdateReason.OTHER);
    }

    public void giveStamina(double amount, PlayerResourceUpdateEvent.UpdateReason reason) {

        // Avoid calling useless event
        double max = getStats().getStat(StatType.MAX_STAMINA);
        double newest = Math.max(0, Math.min(stamina + amount, max));
        if (stamina == newest)
            return;

        PlayerResourceUpdateEvent event = new PlayerResourceUpdateEvent(this, PlayerResource.STAMINA, amount, reason);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        // Use updated amount from Bukkit event
        setStamina(stamina + event.getAmount());
        stamina = Math.max(0, Math.min(stamina + event.getAmount(), max));
    }

    public double getMana() {
        return mana;
    }

    public double getStamina() {
        return stamina;
    }

    public PlayerStats getStats() {
        return playerStats;
    }

    public PlayerAttributes getAttributes() {
        return attributes;
    }

    public void setMana(double amount) {
        mana = Math.max(0, Math.min(amount, getStats().getStat(StatType.MAX_MANA)));
    }

    public void setStamina(double amount) {
        stamina = Math.max(0, Math.min(amount, getStats().getStat(StatType.MAX_STAMINA)));
    }

    public boolean isFullyLoaded() {
        return fullyLoaded;
    }

    public void setFullyLoaded() {
        this.fullyLoaded = true;
    }

    public boolean isCasting() {
        return skillCasting != null;
    }

    /**
     * @return If the action bar is not being used to display anything else
     *         i.e if the "general info" action bar can be displayed
     */
    public boolean canSeeActionBar() {
        return actionBarTimeOut < System.currentTimeMillis();
    }

    /**
     * @param timeOut Delay during which the general info action bar
     *                will not be displayed to the player
     */
    public void setActionBarTimeOut(long timeOut) {
        actionBarTimeOut = System.currentTimeMillis() + (timeOut * 50);
    }

    @Deprecated
    public void setAttribute(PlayerAttribute attribute, int value) {
        setAttribute(attribute.getId(), value);
    }

    @Deprecated
    public void setAttribute(String id, int value) {
        attributes.setBaseAttribute(id, value);
    }

    @Deprecated
    public Map<String, Integer> mapAttributePoints() {
        return getAttributes().mapPoints();
    }

    public int getSkillLevel(Skill skill) {
        return skills.getOrDefault(skill.getId(), 1);
    }

    public void setSkillLevel(Skill skill, int level) {
        setSkillLevel(skill.getId(), level);
    }

    public void setSkillLevel(String skill, int level) {
        skills.put(skill, level);
    }

    public void resetSkillLevel(String skill) {
        skills.remove(skill);
    }

    /*
     * better use getProfess().findSkill(skill).isPresent()
     */
    @Deprecated
    public boolean hasSkillUnlocked(Skill skill) {
        return getProfess().hasSkill(skill.getId()) && hasSkillUnlocked(getProfess().getSkill(skill.getId()));
    }

    /**
     * Checks for the player's level and compares it to the skill unlock level.
     * <p>
     * Any skill, when the player has the right level is instantly
     * unlocked, therefore one must NOT check if the player has unlocked the
     * skill by checking if the skills map contains the skill id as key. This
     * only checks if the player has spent any skill point.
     *
     * @return If the player unlocked the skill
     */
    public boolean hasSkillUnlocked(SkillInfo info) {
        return getLevel() >= info.getUnlockLevel();
    }

    public Map<String, Integer> mapSkillLevels() {
        return new HashMap<>(skills);
    }

    public void giveSkillPoints(int value) {
        setSkillPoints(skillPoints + value);
    }

    public void giveAttributePoints(int value) {
        setAttributePoints(attributePoints + value);
    }

    // public void giveSkillReallocationPoints(int value) {
    // setSkillReallocationPoints(skillReallocationPoints + value);
    // }

    public void giveAttributeReallocationPoints(int value) {
        setAttributeReallocationPoints(attributeReallocationPoints + value);
    }

    public CooldownMap getCooldownMap() {
        return mmoData.getCooldownMap();
    }

    public void setClass(PlayerClass profess) {
        this.profess = profess;

        // Clear old skills
        for (Iterator<SkillInfo> iterator = boundSkills.iterator(); iterator.hasNext(); )
            if (!getProfess().hasSkill(iterator.next().getSkill()))
                iterator.remove();

        // Update stats
        getStats().updateStats();
    }

    public boolean hasSkillBound(int slot) {
        return slot < boundSkills.size();
    }

    public SkillInfo getBoundSkill(int slot) {
        return slot >= boundSkills.size() ? null : boundSkills.get(slot);
    }

    public void setBoundSkill(int slot, SkillInfo skill) {
        if (boundSkills.size() < 7)
            boundSkills.add(skill);
        else
            boundSkills.set(slot, skill);
    }

    public void unbindSkill(int slot) {
        boundSkills.remove(slot);
    }

    public List<SkillInfo> getBoundSkills() {
        return boundSkills;
    }

    public boolean isInCombat() {
        return combat != null;
    }

    /**
     * Loops through all the subclasses available to the player and
     * checks if they could potentially upgrade to one of these
     *
     * @return If the player can change its current class to
     *         a subclass
     */
    public boolean canChooseSubclass() {
        for (Subclass subclass : getProfess().getSubclasses())
            if (getLevel() >= subclass.getLevel())
                return true;
        return false;
    }

    /**
     * Everytime a player does a combat action, like taking
     * or dealing damage to an entity, this method is called.
     */
    public void updateCombat() {
        if (isInCombat())
            combat.update();
        else
            combat = new CombatRunnable(this);
    }

    public SkillMetadata cast(Skill skill) {
        return cast(getProfess().getSkill(skill));
    }

    public SkillMetadata cast(SkillInfo skill) {

        PlayerPreCastSkillEvent preEvent = new PlayerPreCastSkillEvent(this, skill);
        Bukkit.getPluginManager().callEvent(preEvent);
        if (preEvent.isCancelled())
            return new SkillMetadata(this, skill, CancelReason.OTHER);

        // Check for mana/stamina/cooldown and cast skill
        CasterMetadata casterMeta = new CasterMetadata(this);
        SkillMetadata cast = skill.getSkill().whenCast(casterMeta, skill);

        // Send failure messages
        if (!cast.isSuccessful()) {
            if (!skill.getSkill().isPassive() && isOnline()) {
                if (cast.getCancelReason() == CancelReason.LOCKED)
                    MMOCore.plugin.configManager.getSimpleMessage("not-unlocked-skill").send(getPlayer());

                if (cast.getCancelReason() == CancelReason.MANA)
                    MMOCore.plugin.configManager.getSimpleMessage("casting.no-mana", "mana", getProfess().getManaDisplay().getName()).send(getPlayer());

                if (cast.getCancelReason() == CancelReason.STAMINA)
                    MMOCore.plugin.configManager.getSimpleMessage("casting.no-stamina").send(getPlayer());

                if (cast.getCancelReason() == CancelReason.COOLDOWN)
                    MMOCore.plugin.configManager.getSimpleMessage("casting.on-cooldown").send(getPlayer());
            }

            PlayerPostCastSkillEvent postEvent = new PlayerPostCastSkillEvent(this, skill, cast);
            Bukkit.getPluginManager().callEvent(postEvent);
            return cast;
        }

        // Apply cooldown, mana and stamina costs
        if (!noCooldown) {

            // Cooldown
            double flatCooldownReduction = Math.max(0, Math.min(1, getStats().getStat(StatType.COOLDOWN_REDUCTION) / 100));
            CooldownInfo cooldownHandler = getCooldownMap().applyCooldown(cast.getSkill(), cast.getCooldown());
            cooldownHandler.reduceInitialCooldown(flatCooldownReduction);

            giveMana(-cast.getManaCost(), PlayerResourceUpdateEvent.UpdateReason.SKILL_COST);
            giveStamina(-cast.getStaminaCost(), PlayerResourceUpdateEvent.UpdateReason.SKILL_COST);
        }

        PlayerPostCastSkillEvent postEvent = new PlayerPostCastSkillEvent(this, skill, cast);
        Bukkit.getPluginManager().callEvent(postEvent);
        return cast;
    }

    @Override
    public int hashCode() {
        return mmoData.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerData && ((PlayerData) obj).getUniqueId().equals(getUniqueId());
    }

    public static PlayerData get(OfflinePlayer player) {
        return get(player.getUniqueId());
    }

    public static PlayerData get(UUID uuid) {
        /**
         * TODO 캐릭터 슬롯 만들면 여기 바꿔야함.
         */
        return MMOCore.plugin.dataProvider.getDataManager().get(uuid);
    }

    /**
     * This is used to check if the player data is loaded for a
     * specific player. This might seem redundant because the given
     * Player instance is linked to an online player, and data
     * is always loaded for an online player.
     * <p>
     * In fact a Player instance can be attached to a Citizens NPC
     * which has no player data loaded hence this method
     *
     * @param player Either a real player or an NPC
     * @return If player data for that player is loaded
     */
    public static boolean has(Player player) {
       return has(player.getUniqueId());
    }

    /**
     * This is used to check if the player data is loaded for a
     * specific player. This might seem redundant because the given
     * Player instance is linked to an online player, and data
     * is always loaded for an online player.
     * <p>
     * In fact a Player instance can be attached to a Citizens NPC
     * which has no player data loaded hence this method
     *
     * @param uuid A (real or fictive) player UUID
     * @return If player data for that player is loaded
     */
    public static boolean has(UUID uuid) {
        return MMOCore.plugin.dataProvider.getDataManager().isLoaded(uuid);
    }

    public static Collection<PlayerData> getAll() {
        return MMOCore.plugin.dataProvider.getDataManager().getLoaded();
    }
}
