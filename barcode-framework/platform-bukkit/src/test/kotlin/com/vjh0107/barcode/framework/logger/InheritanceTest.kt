package com.vjh0107.barcode.framework.logger

import io.kotest.assertions.print.print
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.engine.test.logging.TestLogger
import kotlinx.coroutines.test.runTest
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.PluginBase
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import java.io.File
import java.io.InputStream
import java.util.logging.Logger

class InheritanceTest : AnnotationSpec() {
    interface LoggerProvider {
        val logger: Logger
    }

    @Suppress("ABSTRACT_MEMBER_NOT_IMPLEMENTED", "ACCIDENTAL_OVERRIDE")
    class PluginMock : PluginBase(), LoggerProvider {
        override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? { TODO("Not yet implemented") }
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean { TODO("Not yet implemented") }
        override fun getDataFolder(): File { TODO("Not yet implemented") }
        override fun getDescription(): PluginDescriptionFile { TODO("Not yet implemented") }
        override fun getConfig(): FileConfiguration { TODO("Not yet implemented") }
        override fun getResource(filename: String): InputStream? { TODO("Not yet implemented") }
        override fun saveConfig() { TODO("Not yet implemented") }
        override fun saveDefaultConfig() { TODO("Not yet implemented") }
        override fun saveResource(resourcePath: String, replace: Boolean) { TODO("Not yet implemented") }
        override fun reloadConfig() { TODO("Not yet implemented") }
        override fun getPluginLoader(): PluginLoader { TODO("Not yet implemented") }
        override fun getServer(): Server { TODO("Not yet implemented") }
        override fun isEnabled(): Boolean { TODO("Not yet implemented") }
        override fun onDisable() { TODO("Not yet implemented") }
        override fun onLoad() { TODO("Not yet implemented") }
        override fun onEnable() { TODO("Not yet implemented") }
        override fun isNaggable(): Boolean { TODO("Not yet implemented") }
        override fun setNaggable(canNag: Boolean) { TODO("Not yet implemented") }
        override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? { TODO("Not yet implemented") }
        override fun getDefaultBiomeProvider(worldName: String, id: String?): BiomeProvider? { TODO("Not yet implemented") }
        override fun getLogger(): Logger {
            return Logger.getGlobal()
        }
    }

    @Test
    fun run() = runTest {
        val plugin = PluginMock()
        log(plugin)
    }

    fun log(logger: LoggerProvider) {
        logger.logger.info("1")
    }
}