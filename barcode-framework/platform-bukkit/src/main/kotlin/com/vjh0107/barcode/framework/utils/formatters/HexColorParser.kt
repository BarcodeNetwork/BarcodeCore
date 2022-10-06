package com.vjh0107.barcode.framework.utils.formatters

import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

object HexColorParser {
    private val PATTERN = Pattern.compile("<(#|HEX)([a-fA-F0-9]{6})>")

    fun parseColorCodes(target: String): String {
        var format = target
        var match = PATTERN.matcher(format)
        while (match.find()) {
            val color = format.substring(match.start(), match.end())
            format = format.replace(color, "" + ChatColor.of('#'.toString() + match.group(2)))
            match = PATTERN.matcher(format)
        }
        return ChatColor.translateAlternateColorCodes('&', format)
    }
}

fun String.parseColorCode(): String {
    return HexColorParser.parseColorCodes(this)
}