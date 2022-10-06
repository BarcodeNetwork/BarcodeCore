package com.vjh0107.barcode.framework.utils.messagesender

import com.vjh0107.barcode.framework.utils.formatters.colorize
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

private const val PREFIX = "§8&l[BN]"

fun CommandSender.sendBNMessage(message: String, usePrefix: Boolean = true) {
    val prefix = if (usePrefix) "$PREFIX " else ""
    this.sendMessage("$prefix§f$message".colorize())
}

fun CommandSender.sendBNWarnMessage(message: String, usePrefix: Boolean = true) {
    val prefix = if (usePrefix) "$PREFIX " else ""
    if (this is Player) {
        this.playSound(this.location, Sound.ENTITY_VILLAGER_NO, 1f, 2f)
    }
    this.sendMessage("$prefix§c$message".colorize())
}

fun CommandSender.info(msg: String) {
    if (msg.isEmpty()) return
    sendMessage("$PREFIX §f$msg")
}

fun CommandSender.error(msg: String) {
    sendMessage("$PREFIX §c$msg")
}
