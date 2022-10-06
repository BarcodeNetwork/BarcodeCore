package com.vjh0107.barcode.advancement

import com.vjh0107.barcode.advancement.services.ConditionManager
import com.vjh0107.barcode.advancement.services.*
import com.vjh0107.barcode.framework.AbstractBarcodePlugin

class BarcodeAdvancementPlugin : AbstractBarcodePlugin() {

    lateinit var advancementManager: AdvancementManageService
    lateinit var configManager: ConfigManager
    lateinit var playerRenderersManager: PlayerRenderersManager
    lateinit var conditionManager: ConditionManager

    override fun onEnable() {
        saveDefaultConfig()
    }
}