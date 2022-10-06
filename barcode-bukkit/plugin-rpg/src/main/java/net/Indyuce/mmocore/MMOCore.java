package net.Indyuce.mmocore;

import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmocore.api.ConfigFile;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.resource.PlayerResource;
import net.Indyuce.mmocore.api.player.stats.StatType;
import net.Indyuce.mmocore.command.*;
import net.Indyuce.mmocore.comp.MMOCoreTargetRestriction;
import net.Indyuce.mmocore.comp.anticheat.AntiCheatSupport;
import net.Indyuce.mmocore.comp.anticheat.SpartanPlugin;
import net.Indyuce.mmocore.comp.placeholder.DefaultParser;
import net.Indyuce.mmocore.comp.placeholder.PlaceholderAPIParser;
import net.Indyuce.mmocore.comp.placeholder.PlaceholderParser;
import net.Indyuce.mmocore.listener.PlayerListener;
import net.Indyuce.mmocore.listener.SpellCast;
import net.Indyuce.mmocore.manager.*;
import net.Indyuce.mmocore.manager.data.DataProvider;
import net.Indyuce.mmocore.manager.data.mysql.MySQLDataProvider;
import net.Indyuce.mmocore.manager.profession.ProfessionManager;
import net.Indyuce.mmocore.manager.social.BoosterManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Level;

public class MMOCore extends JavaPlugin {
	public static MMOCore plugin;

	public ConfigManager configManager;
	public SoundManager soundManager;
	public AntiCheatSupport antiCheatSupport;
	public PlaceholderParser placeholderParser = new DefaultParser();
	public DataProvider dataProvider;
	public final SkillManager skillManager = new SkillManager();
	public final ClassManager classManager = new ClassManager();
	public final BoosterManager boosterManager = new BoosterManager();
	public final AttributeManager attributeManager = new AttributeManager();
	public final ProfessionManager professionManager = new ProfessionManager();
	public final net.Indyuce.mmocore.manager.ExperienceManager experience = new net.Indyuce.mmocore.manager.ExperienceManager();

	public boolean shouldDebugSQL = false;

	public MMOCore() {
		plugin = this;
	}

	public void load() {
		// Register target restrictions due to MMOCore in MythicLib
		MythicLib.plugin.getEntities().registerRestriction(new MMOCoreTargetRestriction());
	}

	public void enable() {
		saveDefaultConfig();

		final int configVersion = getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
		final int defConfigVersion = getConfig().getDefaults().getInt("config-version");
		if (configVersion != defConfigVersion) {
			getLogger().warning("You may be using an outdated config.yml!");
			getLogger().warning("(Your config version: '" + configVersion + "' | Expected config version: '" + defConfigVersion + "')");
		}

		dataProvider = new MySQLDataProvider(getConfig());
		shouldDebugSQL = getConfig().getBoolean("mysql.debug");

		if (getConfig().isConfigurationSection("default-playerdata"))
			dataProvider.getDataManager().loadDefaultData(getConfig().getConfigurationSection("default-playerdata"));

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			placeholderParser = new PlaceholderAPIParser();
			getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
		}

		if (Bukkit.getPluginManager().getPlugin("Spartan") != null) {
			antiCheatSupport = new SpartanPlugin();
			getLogger().log(Level.INFO, "Hooked onto Spartan");
		}

		/*
		 * Resource regeneration. Must check if entity is dead otherwise regen will make
		 * the 'respawn' button glitched plus HURT entity effect bug
		 */
		new BukkitRunnable() {
			public void run() {
				for (PlayerData player : PlayerData.getAll())
					if (player.isOnline() && !player.getPlayer().isDead())
						for (PlayerResource resource : PlayerResource.values()) {
							double regenAmount = player.getProfess().getHandler(resource).getRegen(player);
							if (regenAmount != 0)
								resource.regen(player, regenAmount);
						}
			}
		}.runTaskTimer(MMOCore.plugin, 100, 20);

		reloadPlugin();

		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new SpellCast(), this);
		/*
		 * Initialize player data from all online players. This is very important to do
		 * that after registering all the professses otherwise the player datas can't
		 * recognize what profess the player has and professes will be lost
		 */
		Bukkit.getOnlinePlayers().forEach(player -> dataProvider.getDataManager().setup(player.getUniqueId()));

		// Command
		try {
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

			FileConfiguration config = new ConfigFile("commands").getConfig();

			if (config.contains("player"))
				commandMap.register("mmocore", new PlayerStatsCommand(config.getConfigurationSection("player")));
			if (config.contains("attributes"))
				commandMap.register("mmocore", new AttributesCommand(config.getConfigurationSection("attributes")));
			if (config.contains("skills"))
				commandMap.register("mmocore", new SkillsCommand(config.getConfigurationSection("skills")));

		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}

		MMOCoreCommandTreeRoot mmoCoreCommand = new MMOCoreCommandTreeRoot();
		getCommand("mmocore").setExecutor(mmoCoreCommand);
		getCommand("mmocore").setTabCompleter(mmoCoreCommand);

		if (getConfig().getBoolean("auto-save.enabled")) {
			int autosave = getConfig().getInt("auto-save.interval") * 20;
			new BukkitRunnable() {
				public void run() {

					// Save player data
					for (PlayerData data : PlayerData.getAll())
						if (data.isFullyLoaded())
							dataProvider.getDataManager().saveData(data);
				}
			}.runTaskTimerAsynchronously(MMOCore.plugin, autosave, autosave);
		}
	}

	public void disable() {

		// Save player data
		for (PlayerData data : PlayerData.getAll())
			if (data.isFullyLoaded()) {
				data.close();
				dataProvider.getDataManager().saveData(data);
			}

		// Close MySQL data provider (memory leaks)
		if (dataProvider instanceof MySQLDataProvider)
			((MySQLDataProvider) dataProvider).close();
	}

	public void reloadPlugin() {
		reloadConfig();

		configManager = new ConfigManager();

		skillManager.reload();

		attributeManager.clear();
		attributeManager.reload();

		// experience must be loaded before professions and classes
		experience.reload();

		professionManager.clear();
		professionManager.reload();

		classManager.clear();
		classManager.reload();

		InventoryManager.load();
		soundManager = new SoundManager(new ConfigFile("sounds").getConfig());

		StatType.load();
	}

	public static void log(String message) {
		log(Level.INFO, message);
	}

	public static void log(Level level, String message) {
		plugin.getLogger().log(level, message);
	}

	public File getJarFile() {
		return getFile();
	}

	public boolean hasAntiCheat() {
		return antiCheatSupport != null;
	}

	public static void sqlDebug(String s) {
		if(!MMOCore.plugin.shouldDebugSQL) return;
		MMOCore.plugin.getLogger().warning("- [SQL Debug] " + s);
	}
}
