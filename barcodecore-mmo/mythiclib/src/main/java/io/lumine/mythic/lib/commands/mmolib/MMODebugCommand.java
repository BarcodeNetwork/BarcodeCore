package io.lumine.mythic.lib.commands.mmolib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTCompound;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.api.util.NBTTypeHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

public class MMODebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) sender.sendMessage(ChatColor.RED + "Not enough args.");
        else {
            switch(args[0]) {
                case "nbt":
                    if (!(sender instanceof Player))
                        sender.sendMessage(ChatColor.RED + "You can only run this as a player");
                    else debugNBT(sender);
                    break;
                case "stats":
                    if (!(sender instanceof Player))
                        sender.sendMessage(ChatColor.RED + "You can only run this as a player");
                    else debugStats(sender);
                    break;
                case "log":
                    debugLog(sender);
                    break;
                case "versions":
                    debugVersions(sender);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown command.");
                    break;
            }
        }
        return true;
    }

    private void debugNBT(CommandSender sender) {
        Player player = (Player) sender;
        JsonObject inventory = new JsonObject();
        if(isValid(player.getEquipment().getItemInMainHand()))
            inventory.add("mainhand", fromNBT(NBTItem.get(player.getEquipment().getItemInMainHand())));
        if(isValid(player.getEquipment().getItemInOffHand()))
            inventory.add("offhand", fromNBT(NBTItem.get(player.getEquipment().getItemInOffHand())));
        if(isValid(player.getEquipment().getHelmet()))
            inventory.add("helmet", fromNBT(NBTItem.get(player.getEquipment().getHelmet())));
        if(isValid(player.getEquipment().getChestplate()))
            inventory.add("chest", fromNBT(NBTItem.get(player.getEquipment().getChestplate())));
        if(isValid(player.getEquipment().getLeggings()))
            inventory.add("legs", fromNBT(NBTItem.get(player.getEquipment().getLeggings())));
        if(isValid(player.getEquipment().getBoots()))
            inventory.add("boots", fromNBT(NBTItem.get(player.getEquipment().getBoots())));

        if(inventory.size() == 0) sender.sendMessage(ChatColor.RED + "No NBT items found");
        //else sender.spigot().sendMessage(upload(MythicLib.plugin.getJson().toString(inventory)));
        //TODO make the above line work
        sender.sendMessage("Command is currently under maintenance.");
    }

    private JsonObject fromNBT(NBTItem nbt) {
        JsonObject data = new JsonObject();
        for(String tag : nbt.getTags()) {
            final int typeId = nbt.getTypeId(tag);
            if(NBTTypeHelper.COMPOUND.is(typeId))
                data.add(tag, fromCompound(nbt.getNBTCompound(tag)));
            else {
                JsonObject nbtData = new JsonObject();
                switch(typeId) {
                    case 0:
                        data.addProperty(tag, "END");
                        break;
                    case 1:
                        nbtData.addProperty("byte", nbt.get(tag).toString());
                        nbtData.addProperty("boolean", nbt.getBoolean(tag));
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        nbtData.addProperty("value", nbt.get(tag).toString());
                        break;
                    case 7:
                    case 11:
                    case 12:
                        nbtData.addProperty("array", nbt.get(tag).toString());
                        break;
                    case 8:
                        try {
                            JsonElement json = MythicLib.plugin.getJson().parse(nbt.get(tag).toString(), JsonElement.class);
                            if(json.isJsonNull()) nbtData.add("null_string", json.getAsJsonNull());
                            else if(json.isJsonPrimitive()) nbtData.add("string", json.getAsJsonPrimitive());
                            else if(json.isJsonArray()) nbtData.add("string_list", json.getAsJsonArray());
                            else if(json.isJsonObject()) nbtData.add("string_object", json.getAsJsonObject());
                        } catch(Exception e) {
                            nbtData.addProperty("string", nbt.getString(tag));
                        }
                        break;
                    case 9:
                        try {
                            JsonElement json = MythicLib.plugin.getJson().parse(nbt.get(tag).toString(), JsonElement.class);
                            if(json.isJsonNull()) nbtData.add("null_list", json.getAsJsonNull());
                            else if(json.isJsonPrimitive()) nbtData.add("primitive_list", json.getAsJsonPrimitive());
                            else if(json.isJsonArray()) nbtData.add("array_list", json.getAsJsonArray());
                            else if(json.isJsonObject()) nbtData.add("object_list", json.getAsJsonObject());
                        } catch(Exception e) {
                            nbtData.addProperty("unparsable_list", nbt.get(tag).toString());
                        }
                        break;
                    default:
                        nbtData.addProperty("unknown", nbt.get(tag).toString());
                }
                if(nbtData.size() != 0) {
                    nbtData.addProperty("typeid", typeId);
                    data.add(tag, nbtData);
                }
            }
        }
        return data;
    }

    private JsonObject fromCompound(NBTCompound compound) {
        JsonObject data = new JsonObject();
        for(String tag : compound.getTags()) {
            final int typeId = compound.getTypeId(tag);
            if(NBTTypeHelper.COMPOUND.is(typeId))
                data.add(tag, fromCompound(compound.getNBTCompound(tag)));
            else {
                JsonObject nbtData = new JsonObject();
                switch(typeId) {
                    case 0:
                        data.addProperty(tag, "END");
                        break;
                    case 1:
                        nbtData.addProperty("byte", (byte) compound.get(tag));
                        nbtData.addProperty("boolean", ((byte) compound.get(tag)) == 1);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        nbtData.addProperty("value", (Number) compound.get(tag));
                        break;
                    case 7:
                    case 11:
                    case 12:
                        nbtData.addProperty("array", compound.get(tag).toString());
                        break;
                    case 8:
                        nbtData.addProperty("string", compound.getString(tag));
                        break;
                    case 9:
                        try {
                            JsonElement json = MythicLib.plugin.getJson().parse(compound.get(tag).toString(), JsonElement.class);
                            if(json.isJsonNull()) nbtData.add("null_list", json.getAsJsonNull());
                            else if(json.isJsonPrimitive()) nbtData.add("primitive_list", json.getAsJsonPrimitive());
                            else if(json.isJsonArray()) nbtData.add("array_list", json.getAsJsonArray());
                            else if(json.isJsonObject()) nbtData.add("object_list", json.getAsJsonObject());
                        } catch(Exception e) {
                            nbtData.addProperty("unparsable_list", compound.get(tag).toString());
                        }
                        break;
                    default:
                        nbtData.addProperty("unknown", compound.get(tag).toString());
                }
                if(nbtData.size() != 0) {
                    nbtData.addProperty("typeid", typeId);
                    data.add(tag, nbtData);
                }
            }
        }
        return data;
    }

    private boolean isValid(ItemStack stack) {
        return stack != null && stack.getType() != Material.AIR;
    }

    private static void debugStats(CommandSender sender) {
        Player player = (Player) sender;
        MMOPlayerData mmo = MMOPlayerData.get(player);
        JsonObject stats = new JsonObject();
        for (StatInstance stat : mmo.getStatMap().getInstances()) {
            JsonObject instance = new JsonObject();
            instance.addProperty("base", stat.getBase());
            instance.addProperty("total", stat.getTotal());
            JsonObject modifiers = new JsonObject();
            for (String key : stat.getKeys()) {
                JsonObject mod = new JsonObject();
                StatModifier modifier = stat.getAttribute(key);
                mod.addProperty("value", modifier.getValue());
                mod.addProperty("type", modifier.getType().name());
                modifiers.add(key, mod);
            }
            instance.add("modifiers", modifiers);
            stats.add(stat.getStat(), instance);
        }
        //TODO fix this debug
        //sender.spigot().sendMessage(upload(MythicLib.plugin.getJson().toString(stats)));
        sender.sendMessage("Command is currently under maintenance.");
    }

    private static void debugLog(CommandSender sender) {
        try {
            File log = new File(MythicLib.plugin.getDataFolder(), "..\\..\\logs\\latest.log");
            Scanner scanner = new Scanner(log);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine())
                builder.append(scanner.nextLine()).append("\n");
            scanner.close();
            //TODO fix this debug
            //sender.spigot().sendMessage(upload(builder.toString()));
            sender.sendMessage("Command is currently under maintenance.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Couldn't load latest.log file...");
        }
    }

    private static void debugVersions(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            builder.append("> ").append(plugin.getName()).append(": ")
                    .append(plugin.getDescription().getVersion());
            if(!Bukkit.getPluginManager().isPluginEnabled(plugin))
                builder.append(" (Disabled)");
            builder.append("\n");
        }
        //TODO fix this debug
        //sender.spigot().sendMessage(upload(builder.toString()));
        sender.sendMessage("Command is currently under maintenance.");
    }

    private static BaseComponent[] upload(String toUpload) {
        String key = "INVALID-KEY";
        String error = "no error?";
        try {
            /* Start of Fix */
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }

            } };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            /* End of the fix*/
            // Thanks StackOverflow

            HttpURLConnection con = (HttpURLConnection) new URL("https://149.202.88.21:8085/documents").openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(toUpload);
                wr.flush();
            }
            String content = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            JsonObject json = MythicLib.plugin.getJson().parse(content, JsonObject.class);
            if (json.has("key")) key = json.get("key").getAsString();

            con.disconnect();
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            error = e.getLocalizedMessage();
        }

        if (key.equals("INVALID-KEY"))
            return new ComponentBuilder("Something went wrong! ").color(ChatColor.RED).append("(").append(error).append(")").color(ChatColor.GRAY).create();
        BaseComponent[] hover = new ComponentBuilder("Copy to clipboard...").color(ChatColor.WHITE).create();
        return new ComponentBuilder("Success! Please provide the support team with this code:\n").color(ChatColor.GREEN)
                .append(">> ").color(ChatColor.DARK_GRAY).append(key).color(ChatColor.YELLOW).event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(hover))).event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.asangarin.eu:8085/" + key)).create();
    }
}
