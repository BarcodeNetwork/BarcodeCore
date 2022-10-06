package com.vjh0107.barcode.framework.dependencies.hologram.adapter.impl

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.dependencies.hologram.adapter.HolographicDisplaysAdapter
import com.vjh0107.barcode.framework.dependencies.hologram.builder.HolographicDisplaysBuilderScope
import com.vjh0107.barcode.framework.dependencies.hologram.builder.impl.HolographicDisplaysBuilderScopeImpl
import org.bukkit.Location

class HolographicDisplaysAdapterImpl<T : AbstractBarcodePlugin>(val plugin: T) : HolographicDisplaysAdapter {
    private val holograms: MutableMap<String, Hologram> = mutableMapOf()

    override fun createHologram(id: String, location: Location): Hologram {
        val hologram = HologramsAPI.createHologram(plugin, location)
        holograms[id] = hologram
        return hologram
    }

    override fun createHologram(id: String, location: Location, scope: HolographicDisplaysBuilderScope.() -> HolographicDisplaysBuilderScope): Hologram {
        val hologram = HologramsAPI.createHologram(plugin, location)
        holograms[id] = hologram
        return scope(HolographicDisplaysBuilderScopeImpl(hologram)).getHologram()
    }

    override fun getHologram(id: String): Hologram {
        return holograms[id] ?: throw NullPointerException("홀로그램을 찾을 수 없습니다.")
    }

    override fun getHologramNullable(id: String): Hologram? {
        return holograms[id]
    }

    override fun removeHologram(id: String) {
        getHologramNullable(id)?.delete()
        holograms.remove(id)
    }

    override fun getPluginHolograms(): List<Hologram> {
        return HologramsAPI.getHolograms(plugin).toList()
    }
}