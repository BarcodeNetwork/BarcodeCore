package com.vjh0107.barcode.framework.utils.formatters

import org.bukkit.ChatColor

fun String.colorize(): String {
    return this.replace("&", "§")
}

fun String.decolorize(): String {
    return ChatColor.stripColor(this.colorize()) ?: this
}

fun String.translateAlternateColorCodes(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}