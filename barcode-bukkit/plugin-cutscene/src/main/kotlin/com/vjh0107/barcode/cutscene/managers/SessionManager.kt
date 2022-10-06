package com.vjh0107.barcode.cutscene.managers

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.cutscene.Cutscene

@BarcodeComponent
class SessionManager : BarcodePluginManager {
    var sessions: List<Cutscene> = ArrayList()

    override fun load() {

    }

    override fun close() {
        sessions.forEach {
            it.stopCutscene(true)
        }
    }
}