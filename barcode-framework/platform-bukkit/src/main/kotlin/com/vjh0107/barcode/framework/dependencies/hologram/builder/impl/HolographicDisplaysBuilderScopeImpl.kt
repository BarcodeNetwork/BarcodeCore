package com.vjh0107.barcode.framework.dependencies.hologram.builder.impl

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.vjh0107.barcode.framework.dependencies.hologram.builder.HolographicDisplaysBuilderScope
import org.bukkit.inventory.ItemStack

class HolographicDisplaysBuilderScopeImpl(private val hologram: Hologram) : HolographicDisplaysBuilderScope {
    override fun addLine(content: String): HolographicDisplaysBuilderScope {
        hologram.appendTextLine(content)
        return this
    }

    override fun addLine(content: ItemStack): HolographicDisplaysBuilderScope {
        hologram.appendItemLine(content)
        return this
    }

    override fun setAllowPlaceholder(value: Boolean): HolographicDisplaysBuilderScope {
        hologram.isAllowPlaceholders = value
        return this
    }

    override fun getHologram(): Hologram {
        return hologram
    }
}