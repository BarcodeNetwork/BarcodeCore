package com.vjh0107.barcode.advancement.services

import com.vjh0107.barcode.advancement.config.AdvancementFile
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.component.Reloadable

interface AdvancementManageService : BarcodePluginManager, Reloadable {
    override fun load()

    override fun close()

    fun registerAdvancementFile(advancementFile: AdvancementFile)

    fun getComponentsAll(): Map<Int, AdvancementFile>
}