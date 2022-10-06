package com.vjh0107.barcode.core

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.core.dependencies.mmo.MMOComponent
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider
import net.kyori.adventure.text.Component

class BarcodeCorePlugin : AbstractBarcodePlugin() {
    @InjectInstance
    companion object : InstanceProvider<BarcodeCorePlugin> {
        override lateinit var instance: BarcodeCorePlugin
    }

    override fun onPostLoad() {
        MMOComponent.onLoad(this)
    }

    override fun onPreEnable() {
        saveDefaultConfig()
    }

    override fun onPreDisable() {
        server.onlinePlayers.forEach {
            it.kick(Component.text("§6바코드 네트워크가 재시작 중입니다..."))
        }
    }

    override fun onPostEnable() {
        printAsciiArt()
    }

    private fun printAsciiArt() {
        this.logger.info(
            "\n__________                                .___     _________                       \n" +
                    "\\______   \\_____ _______   ____  ____   __| _/____ \\_   ___ \\  ___________   ____  \n" +
                    " |    |  _/\\__  \\\\_  __ \\_/ ___\\/  _ \\ / __ |/ __ \\/    \\  \\/ /  _ \\_  __ \\_/ __ \\ \n" +
                    " |    |   \\ / __ \\|  | \\/\\  \\__(  <_> ) /_/ \\  ___/\\     \\___(  <_> )  | \\/\\  ___/ \n" +
                    " |______  /(____  /__|    \\___  >____/\\____ |\\___  >\\______  /\\____/|__|    \\___  >\n" +
                    "        \\/      \\/            \\/           \\/    \\/        \\/                   \\/ \n"
        )
    }
}
