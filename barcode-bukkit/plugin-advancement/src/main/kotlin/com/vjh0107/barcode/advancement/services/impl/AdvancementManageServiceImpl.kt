package com.vjh0107.barcode.advancement.services.impl

import com.vjh0107.barcode.advancement.config.AdvancementFile
import com.vjh0107.barcode.advancement.services.AdvancementManageService
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import org.bukkit.configuration.file.YamlConfiguration
import org.koin.dsl.module
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import java.util.logging.Level

@BarcodeComponent
class AdvancementManageServiceImpl(private val plugin: AbstractBarcodePlugin) : AdvancementManageService {
    private val advancementsMap: SortedMap<Int, AdvancementFile> = sortedMapOf()

    val module = module {
        single<AdvancementManageService> { this@AdvancementManageServiceImpl }
    }

    override fun registerAdvancementFile(advancement: AdvancementFile) {
        if (advancement.isEnabled) {
            advancementsMap[advancement.index] = advancement
        }
    }

    override fun getComponentsAll(): Map<Int, AdvancementFile> {
        return advancementsMap
    }

    override fun close() {
        advancementsMap.clear()
    }

    override fun load() {
        val advancementsFolderListFile = File(plugin.dataFolder.toString() + "/advancements").listFiles()
            ?: throw FileNotFoundException()
        advancementsFolderListFile.forEach { file ->
            try {
                registerAdvancementFile(AdvancementFile(YamlConfiguration.loadConfiguration(file)))
            } catch (exception: IllegalArgumentException) {
                plugin.logger.log(Level.WARNING, "Advancement를 로드할 수 없습니다. '" + file.name + "': " + exception.message)
            }
        }
    }
}