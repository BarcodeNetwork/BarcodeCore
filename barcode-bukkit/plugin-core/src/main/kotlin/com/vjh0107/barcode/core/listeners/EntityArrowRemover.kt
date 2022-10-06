package com.vjh0107.barcode.core.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.version.adapter.NMSAdapter
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileHitEvent

/**
 * 엔티티에게 박힌 화살을 제거합니다.
 */
@BarcodeComponent
class EntityArrowRemover : BarcodeListener {
    @EventHandler
    fun onShotByArrow(event: ProjectileHitEvent) {
        val victim = event.hitEntity ?: return
        NMSAdapter.removeArrows(victim)
    }
}