package com.vjh0107.barcode.framework.dependencies.hologram.builder

import com.gmail.filoghost.holographicdisplays.api.Hologram
import org.bukkit.inventory.ItemStack

interface HolographicDisplaysBuilderScope {
    /**
     * 문장을 홀로그램 줄에 추가합니다.
     */
    fun addLine(content: String): HolographicDisplaysBuilderScope

    /**
     * 아이템을 홀로그램 줄에 추가합니다.
     */
    fun addLine(content: ItemStack): HolographicDisplaysBuilderScope

    /**
     * 홀로그램에 플레이스홀더를 허용하는지 정합니다.
     */
    fun setAllowPlaceholder(value: Boolean): HolographicDisplaysBuilderScope

    /**
     * 홀로그램을 구합니다.
     */
    fun getHologram(): Hologram
}