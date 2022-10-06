package com.vjh0107.barcode.advancement.services

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files

@BarcodeComponent
class ConfigManager(val plugin: BarcodeAdvancementPlugin) : BarcodePluginManager {
    init {
        plugin.configManager = this
    }

    var debugging = false

    override fun load() {
        loadDefaultFiles()
        plugin.reloadConfig()
        debugging = plugin.config.getBoolean("debug")
        if (debugging) {
            plugin.logger.info("Debug mode enabled")
        }
    }

    fun loadDefaultFiles() {
        loadDefaultFile("advancements", "example.yml")
    }

    fun loadDefaultFile(name: String) {
        loadDefaultFile("", name)
    }
    fun loadDefaultFile(path: String, name: String) {
        val newPath = if (path.isEmpty()) "" else "/$path"
        val folder: File = File(plugin.dataFolder.toString() + newPath)
        if (!folder.exists()) folder.mkdir()
        val file: File = File(plugin.dataFolder.toString() + newPath, name)

        if (!file.exists()) try {
            val pathTransformed = if (path.isEmpty()) "" else "$path/"
            val resource = plugin.getResource("default/$pathTransformed$name")
                ?: throw FileNotFoundException("default/" + (if (path.isEmpty()) "" else "$path/") + name + " 에서 기본 파일을 찾을 수 없음.")

            Files.copy(resource, file.absoluteFile.toPath())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}