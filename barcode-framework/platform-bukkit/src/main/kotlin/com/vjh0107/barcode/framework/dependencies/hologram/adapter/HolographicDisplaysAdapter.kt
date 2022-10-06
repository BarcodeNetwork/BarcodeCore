package com.vjh0107.barcode.framework.dependencies.hologram.adapter

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.dependencies.hologram.adapter.impl.HolographicDisplaysAdapterImpl
import com.vjh0107.barcode.framework.dependencies.hologram.builder.HolographicDisplaysBuilderScope
import org.bukkit.Location

interface HolographicDisplaysAdapter {
    /**
     * 홀로그램을 생성합니다.
     */
    fun createHologram(id: String, location: Location): Hologram

    /**
     * 홀로그램을 빌더를 통해 생성합니다.
     */
    fun createHologram(id: String, location: Location, scope: HolographicDisplaysBuilderScope.() -> HolographicDisplaysBuilderScope): Hologram

    /**
     * 홀로그램을 구해옵니다.
     *
     * @throws NullPointerException
     */
    fun getHologram(id: String): Hologram

    /**
     * 홀로그램을 nullable 하게 구해옵니다.
     */
    fun getHologramNullable(id: String): Hologram?

    /**
     * 홀로그램을 삭제합니다.
     */
    fun removeHologram(id: String)

    /**
     * 어댑터의 플러그인을 통해 생성한 모든 홀로그램을 구해옵니다.
     */
    fun getPluginHolograms(): List<Hologram>

    companion object {
        fun <T : AbstractBarcodePlugin> of(plugin: T): HolographicDisplaysAdapter {
            return HolographicDisplaysAdapterImpl(plugin)
        }
    }
}