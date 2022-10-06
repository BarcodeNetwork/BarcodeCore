package com.vjh0107.barcode.cutscene.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Toolbox {

	public static boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static double diff(double one, double two) {
		double three = one-two;
		if(three < 0) {
			three *= -1;
		}
		return three;
	}
	
	public static Float getRandom(double rangeMin, double rangeMax) {
		Random r = new Random();
		return (float)(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
	}
	
	public static ItemStack createCustomSkull(int amount, String displayName, List<String> lore, String texture) {
        texture = "http://textures.minecraft.net/texture/" + texture;
       
        Material head = VersionDetector.getMaterial("PLAYER_HEAD", "SKULL_ITEM");
        
        ItemStack skull = new ItemStack(head, amount);
        if (texture.isEmpty()) {
            return skull;
        }
       
        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        skullMeta.setDisplayName(displayName);
        skullMeta.setLore(lore);
       
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        }
        catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        assert profileField != null;
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        }
        catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
	
	public static String getEnumeration(List<String> list) {
		try {
			String header = "";
			
			if(!list.isEmpty()) {
				for(int i = 0; i < list.size(); i++) {
					if(list.size() == (i+1)) {header += ChatColor.translateAlternateColorCodes('&', " &7and ");}
					if(list.size() != (i+1)) {header += ChatColor.translateAlternateColorCodes('&', "&7, ");}
					
					header += ChatColor.translateAlternateColorCodes('&', "&f")+capitalizeWords(list.get(i).toLowerCase());
				}
				
				if(list.size() > 1) {
					header = header.replaceFirst(ChatColor.translateAlternateColorCodes('&', "&7, "), "");
				}else {
					header = header.replaceFirst(ChatColor.translateAlternateColorCodes('&', " &7and "), "");
				}
				
				return header;
			}
			
			return null;
		}catch(Exception e) {
			ErrorHandler.handleError(e);
			return null;
		}
	}
	
	public static String capitalizeWords(String text) {
		try {
			text = text.toLowerCase();
		    StringBuilder sb = new StringBuilder();
		    if(text.length()>0){
		        sb.append(Character.toUpperCase(text.charAt(0)));
		    }
		    for (int i=1; i<text.length(); i++){
		        String chPrev = String.valueOf(text.charAt(i-1));
		        String ch = String.valueOf(text.charAt(i));
	
		        if(Objects.equals(chPrev, " ")){
		            sb.append(ch.toUpperCase());
		        }else {
		            sb.append(ch);
		        }
	
		    }
	
		    return sb.toString();
		}catch(Exception e) {
			ErrorHandler.handleError(e);
			return text;
		}
	}
	

}
