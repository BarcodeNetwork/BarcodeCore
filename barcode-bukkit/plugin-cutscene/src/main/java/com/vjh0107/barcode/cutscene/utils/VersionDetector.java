package com.vjh0107.barcode.cutscene.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class VersionDetector {
	public static Material getMaterial(String... strings) {
		for(String s : strings) {
			try {
				Material mat = Material.getMaterial(s);
				return mat;
			}catch(Exception e) {
				
			}
		}
		
		return null;
	}
}
