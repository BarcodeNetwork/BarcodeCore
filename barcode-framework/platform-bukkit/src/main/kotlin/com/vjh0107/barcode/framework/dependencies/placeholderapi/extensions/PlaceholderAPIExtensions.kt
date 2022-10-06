package com.vjh0107.barcode.framework.dependencies.placeholderapi.extensions

import com.vjh0107.barcode.framework.dependencies.placeholderapi.PlaceholderAPIComponent
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player

fun String.parseWithPAPI(player: Player): String {
    if (!PlaceholderAPIComponent.isEnabled) {
        throw NullPointerException("PlaceholderAPI 를 찾을 수 없습니다.")
    }
    return PlaceholderAPI.setPlaceholders(player, this)
}
