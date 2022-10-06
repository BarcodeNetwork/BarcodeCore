package com.vjh0107.barcode.core.dependencies.mythicmobs

import com.vjh0107.barcode.core.dependencies.mythicmobs.targeters.impl.BarcodeTargeter
import com.vjh0107.barcode.core.dependencies.mythicmobs.targeters.impl.OwnedTargeter
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent

class BarcodeTargeterFactory(event: MythicTargeterLoadEvent) {
    private val mlc = event.config

    fun createBarcodeTargeter() : BarcodeTargeter {
        return BarcodeTargeter(mlc)
    }

    fun createOwnedTargeter() : OwnedTargeter {
        return OwnedTargeter(mlc)
    }
}