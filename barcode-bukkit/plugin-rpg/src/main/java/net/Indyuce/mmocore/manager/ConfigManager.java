package net.Indyuce.mmocore.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.util.EnumUtils;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.ConfigFile;
import net.Indyuce.mmocore.api.util.input.ChatInput;
import net.Indyuce.mmocore.api.util.input.PlayerInput;
import net.Indyuce.mmocore.api.util.input.PlayerInput.InputType;
import net.Indyuce.mmocore.command.CommandVerbose;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager {
    public final CommandVerbose commandVerbose = new CommandVerbose();

    public boolean overrideVanillaExp, canCreativeCast, cobbleGeneratorXP, saveDefaultClassInfo;
    public String partyChatPrefix, noSkillBoundPlaceholder;
    public ChatColor staminaFull, staminaHalf, staminaEmpty;
    public long combatLogTimer, lootChestExpireTime, lootChestPlayerCooldown;
    public SwapAction normalSwapAction, sneakingSwapAction;

    private final FileConfiguration messages;
    private final boolean chatInput;

    /*
     * the instance must be created after the other managers since all it does
     * is to update them based on the config except for the classes which are
     * already loaded based on the config
     */
    public ConfigManager() {
        // loadDefaultFile("recipes", "brewing.yml");
        // loadDefaultFile("recipes", "furnace.yml");

        if (!new File(MMOCore.plugin.getDataFolder() + "/drop-tables").exists())
            loadDefaultFile("drop-tables", "example-drop-tables.yml");

        if (!new File(MMOCore.plugin.getDataFolder() + "/professions").exists()) {
            loadDefaultFile("professions", "alchemy.yml");
            loadDefaultFile("professions", "farming.yml");
            loadDefaultFile("professions", "fishing.yml");
            loadDefaultFile("professions", "mining.yml");
            loadDefaultFile("professions", "smelting.yml");
            loadDefaultFile("professions", "smithing.yml");
            loadDefaultFile("professions", "woodcutting.yml");
            loadDefaultFile("professions", "enchanting.yml");
        }

        if (!new File(MMOCore.plugin.getDataFolder() + "/quests").exists()) {
            loadDefaultFile("quests", "adv-begins.yml");
            loadDefaultFile("quests", "tutorial.yml");
            loadDefaultFile("quests", "fetch-mango.yml");
        }

        if (!new File(MMOCore.plugin.getDataFolder() + "/classes").exists()) {
            loadDefaultFile("classes", "arcane-mage.yml");
            loadDefaultFile("classes", "human.yml");
            loadDefaultFile("classes", "mage.yml");
            loadDefaultFile("classes", "marksman.yml");
            loadDefaultFile("classes", "paladin.yml");
            loadDefaultFile("classes", "rogue.yml");
            loadDefaultFile("classes", "warrior.yml");
        }

        if (!new File(MMOCore.plugin.getDataFolder() + "/expcurves").exists()) {
            loadDefaultFile("expcurves", "levels.txt");
            loadDefaultFile("expcurves", "mining.txt");
        }

        loadDefaultFile("attributes.yml");
        loadDefaultFile("messages.yml");
        loadDefaultFile("stats.yml");
        loadDefaultFile("restrictions.yml");
        loadDefaultFile("sounds.yml");
        loadDefaultFile("commands.yml");

        commandVerbose.reload(MMOCore.plugin.getConfig().getConfigurationSection("command-verbose"));

        messages = new ConfigFile("messages").getConfig();
        chatInput = MMOCore.plugin.getConfig().getBoolean("use-chat-input");
        partyChatPrefix = MMOCore.plugin.getConfig().getString("party.chat-prefix");
        combatLogTimer = MMOCore.plugin.getConfig().getInt("combat-log.timer") * 1000L;
        lootChestExpireTime = Math.max(MMOCore.plugin.getConfig().getInt("loot-chests.chest-expire-time"), 1) * 1000L;
        lootChestPlayerCooldown = (long) MMOCore.plugin.getConfig().getDouble("player-cooldown") * 1000L;
        noSkillBoundPlaceholder = getSimpleMessage("no-skill-placeholder").message();

        staminaFull = getColorOrDefault("stamina-whole", ChatColor.GREEN);
        staminaHalf = getColorOrDefault("stamina-half", ChatColor.DARK_GREEN);
        staminaEmpty = getColorOrDefault("stamina-empty", ChatColor.WHITE);

        normalSwapAction = EnumUtils.getIfPresent(SwapAction.class, MMOCore.plugin.getConfig().getString("swap-keybind.normal").toUpperCase()).orElse(SwapAction.VANILLA);
        sneakingSwapAction = EnumUtils.getIfPresent(SwapAction.class, MMOCore.plugin.getConfig().getString("swap-keybind.sneaking").toUpperCase()).orElse(SwapAction.VANILLA);
        canCreativeCast = MMOCore.plugin.getConfig().getBoolean("can-creative-cast");
        cobbleGeneratorXP = MMOCore.plugin.getConfig().getBoolean("should-cobblestone-generators-give-exp");
        saveDefaultClassInfo = MMOCore.plugin.getConfig().getBoolean("save-default-class-info");
    }

    private ChatColor getColorOrDefault(String key, ChatColor defaultColor) {
        try {
            return ChatColor.valueOf(MMOCore.plugin.getConfig().getString("resource-bar-colors." + key).toUpperCase());
        } catch (IllegalArgumentException exception) {
            MMOCore.log(Level.WARNING, "Could not read resource bar color from '" + key + "': using default.");
            return defaultColor;
        }
    }

    public PlayerInput newPlayerInput(Player player, InputType type, Consumer<String> output) {
        return new ChatInput(player, type, output);
    }

    public void loadDefaultFile(String name) {
        loadDefaultFile("", name);
    }

    public void loadDefaultFile(String path, String name) {
        String newPath = path.isEmpty() ? "" : "/" + path;
        File folder = new File(MMOCore.plugin.getDataFolder() + (newPath));
        if (!folder.exists()) folder.mkdir();

        File file = new File(MMOCore.plugin.getDataFolder() + (newPath), name);
        if (!file.exists()) try {
            Files.copy(MMOCore.plugin.getResource("default/" + (path.isEmpty() ? "" : path + "/") + name), file.getAbsoluteFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMessage(String key) {
        return messages.getStringList(key);
    }

    public SimpleMessage getSimpleMessage(String key, String... placeholders) {
        String format = messages.getString(key, "");
        for (int j = 0; j < placeholders.length - 1; j += 2)
            format = format.replace("{" + placeholders[j] + "}", placeholders[j + 1]);
        return new SimpleMessage(MythicLib.plugin.parseColors(format));
    }

    public static class SimpleMessage {
        private final String message;
        private final boolean hasPlaceholders;

        public SimpleMessage(String message) {
            this.message = message;
            this.hasPlaceholders = this.message.contains("%");
        }

        public String message() {
            return message;
        }

        public boolean send(Player player) {
            String msg = hasPlaceholders ? MMOCore.plugin.placeholderParser.parse(player, message) : message;

            if (!msg.isEmpty()) {
                player.sendMessage(msg);
            }
            return !msg.isEmpty();
        }
    }

    public enum SwapAction {
        VANILLA,
        SPELL_CAST,
        HOTBAR_SWAP
    }
}
