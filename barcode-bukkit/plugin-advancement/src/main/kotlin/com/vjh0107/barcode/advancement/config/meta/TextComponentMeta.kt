package com.vjh0107.barcode.advancement.config.meta

import com.vjh0107.barcode.advancement.config.placeholder.Placeholder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

interface TextComponentMeta {
    fun getResult(player: Player) : TextComponent

    /**
     * Placeholder 로부터 키를 받아옵니다.
     *
     * @param placeholder Placeholder
     * @return key
     */
    fun String.parsePlaceholderKey(placeholder: Placeholder) : String? {
        return if (this.startsWith(placeholder.prefix) && this.endsWith(placeholder.suffix)) {
            this
                .replace(placeholder.prefix, "")
                .replace(placeholder.suffix, "")
        } else {
            null
        }
    }
}