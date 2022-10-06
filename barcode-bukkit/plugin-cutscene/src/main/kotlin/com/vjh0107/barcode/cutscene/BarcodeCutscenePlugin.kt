package com.vjh0107.barcode.cutscene

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider

class BarcodeCutscenePlugin : AbstractBarcodePlugin() {
    @InjectInstance
    companion object : InstanceProvider<BarcodeCutscenePlugin> {
        @JvmStatic
        override lateinit var instance: BarcodeCutscenePlugin
    }

    override fun onPostEnable() {
        printAsciiArt()
    }

    private fun printAsciiArt() {
        this.logger.info(
            "\n" +
                    "   ___                         _        ___      _                           \n" +
                    "  / __\\ __ _ _ __ ___ ___   __| | ___  / __\\   _| |_ ___  ___ ___ _ __   ___ \n" +
                    " /__\\/// _` | '__/ __/ _ \\ / _` |/ _ \\/ / | | | | __/ __|/ __/ _ \\ '_ \\ / _ \\\n" +
                    "/ \\/  \\ (_| | | | (_| (_) | (_| |  __/ /__| |_| | |_\\__ \\ (_|  __/ | | |  __/\n" +
                    "\\_____/\\__,_|_|  \\___\\___/ \\__,_|\\___\\____/\\__,_|\\__|___/\\___\\___|_| |_|\\___|\n" +
                    "                                                                             "
        )
    }
}