package io.lumine.mythic.lib;

import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.MegaWorkbenchMapping;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.SuperWorkbenchMapping;
import io.lumine.mythic.lib.api.placeholders.MythicPlaceholders;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.commands.BaseCommand;
import io.lumine.mythic.lib.commands.HealthScaleCommand;
import io.lumine.mythic.lib.commands.mmolib.ExploreAttributesCommand;
import io.lumine.mythic.lib.commands.mmolib.MMODebugCommand;
import io.lumine.mythic.lib.commands.mmolib.MMOLibCommand;
import io.lumine.mythic.lib.commands.mmolib.MMOTempStatCommand;
import io.lumine.mythic.lib.comp.PlaceholderAPIHook;
import io.lumine.mythic.lib.comp.flags.DefaultFlagHandler;
import io.lumine.mythic.lib.comp.flags.FlagPlugin;
import io.lumine.mythic.lib.comp.flags.WorldGuardFlags;
import io.lumine.mythic.lib.comp.hologram.CustomHologramFactoryList;
import io.lumine.mythic.lib.comp.mythicmobs.MythicMobsHook;
import io.lumine.mythic.lib.comp.target.CitizensTargetRestriction;
import io.lumine.mythic.lib.gui.PluginInventory;
import io.lumine.mythic.lib.listener.*;
import io.lumine.mythic.lib.listener.event.PlayerAttackEventListener;
import io.lumine.mythic.lib.listener.option.FixMovementSpeed;
import io.lumine.mythic.lib.manager.*;
import io.lumine.utils.events.extra.ArmorEquipEventListener;
import io.lumine.utils.holograms.BukkitHologramFactory;
import io.lumine.utils.holograms.HologramFactory;
import io.lumine.utils.plugin.LuminePlugin;
import io.lumine.utils.scoreboard.PacketScoreboardProvider;
import io.lumine.utils.scoreboard.ScoreboardProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Level;

public class MythicLib extends LuminePlugin {
    public static MythicLib plugin;

    private final DamageManager damageManager = new DamageManager();
    private final EntityManager entityManager = new EntityManager();
    private final StatManager statManager = new StatManager();
    private final JsonManager jsonManager = new JsonManager();
    private final ConfigManager configManager = new ConfigManager();
    private AttackEffects attackEffects;
    private FlagPlugin flagPlugin = new DefaultFlagHandler();
    @Getter
    private ScoreboardProvider scoreboardProvider;

    @Override
    public void load() {
        plugin = this;

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            flagPlugin = new WorldGuardFlags();
            getLogger().log(Level.INFO, "Hooked onto WorldGuard");
        }
    }

    @Override
    public void enable() {
        registerCommand("mythiclib", new BaseCommand(this));

        saveDefaultConfig();

        final int configVersion = getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
        final int defConfigVersion = getConfig().getDefaults().getInt("config-version");
        if (configVersion != defConfigVersion) {
            getLogger().warning("You may be using an outdated config.yml!");
            getLogger().warning("(Your config version: '" + configVersion + "' | Expected config version: '" + defConfigVersion + "')");
        }

        this.scoreboardProvider = new PacketScoreboardProvider(this);
        this.provideService(ScoreboardProvider.class, this.scoreboardProvider);

        // Hologram provider
        this.provideService(HologramFactory.class, new BukkitHologramFactory(), ServicePriority.Low);

        // Register listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(damageManager, this);
        Bukkit.getPluginManager().registerEvents(new DamageReduction(), this);
        Bukkit.getPluginManager().registerEvents(attackEffects = new AttackEffects(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerAttackEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new MythicCraftingManager(), this);
        Bukkit.getPluginManager().registerEvents(new SkillTriggers(), this);

        if (getConfig().getBoolean("health-scale.enabled"))
            Bukkit.getPluginManager().registerEvents(new HealthScale(getConfig().getDouble("health-scale.scale"), getConfig().getInt("health-scale.delay", 0)), this);

        if (getConfig().getBoolean("fix-movement-speed"))
            Bukkit.getPluginManager().registerEvents(new FixMovementSpeed(), this);

        // Custom hologram providers
        for (CustomHologramFactoryList custom : CustomHologramFactoryList.values())
            if (custom.isInstalled(getServer().getPluginManager()))
                try {
                    provideService(HologramFactory.class, custom.generateFactory(), custom.getServicePriority());
                    getLogger().log(Level.INFO, "Hooked onto " + custom.getPluginName());
                } catch (Exception exception) {
                    getLogger().log(Level.WARNING, "Could not hook onto " + custom.getPluginName() + ": " + exception.getMessage());
                }

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            Bukkit.getPluginManager().registerEvents(new MythicMobsHook(), this);
            getLogger().log(Level.INFO, "Hooked onto MythicMobs");
        }

        if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
            entityManager.registerRestriction(new CitizensTargetRestriction());
            getLogger().log(Level.INFO, "Hooked onto Citizens");
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            MythicPlaceholders.registerPlaceholder(new PlaceholderAPIHook());
            getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
        }

        getCommand("exploreattributes").setExecutor(new ExploreAttributesCommand());
        getCommand("mythiclib").setExecutor(new MMOLibCommand());
        getCommand("mmodebug").setExecutor(new MMODebugCommand());
        getCommand("mmotempstat").setExecutor(new MMOTempStatCommand());
        getCommand("healthscale").setExecutor(new HealthScaleCommand());

        // Super workbench
        getCommand("superworkbench").setExecutor(SuperWorkbenchMapping.SWB);
        Bukkit.getPluginManager().registerEvents(SuperWorkbenchMapping.SWB, this);
        getCommand("megaworkbench").setExecutor(MegaWorkbenchMapping.MWB);
        Bukkit.getPluginManager().registerEvents(MegaWorkbenchMapping.MWB, this);

        // Load player data of online players
        Bukkit.getOnlinePlayers().forEach(player -> MMOPlayerData.setup(player));

        configManager.reload();
    }

    public void reload() {
        reloadConfig();
        configManager.reload();
        attackEffects.reload();
    }

    @Override
    public void disable() {
        //this.configuration.unload();
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().getHolder() != null && player.getOpenInventory().getTopInventory().getHolder() instanceof PluginInventory)
                player.closeInventory();
    }

    public static MythicLib inst() {
        return plugin;
    }


    public JsonManager getJson() {
        return jsonManager;
    }

    public DamageManager getDamage() {
        return damageManager;
    }

    public EntityManager getEntities() {
        return entityManager;
    }

    public StatManager getStats() {
        return statManager;
    }

    public ConfigManager getMMOConfig() {
        return configManager;
    }

    public FlagPlugin getFlags() {
        return flagPlugin;
    }

    public void handleFlags(FlagPlugin flagPlugin) {
        this.flagPlugin = flagPlugin;
    }
}
