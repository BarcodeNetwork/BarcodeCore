package com.vjh0107.barcode.cutscene

import org.bukkit.command.PluginCommand
import org.bukkit.command.PluginCommandUtils
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

@Suppress("unused")
class CutscenePluginMocked : JavaPlugin {

    constructor() : super()
    constructor(
        loader: JavaPluginLoader,
        description: PluginDescriptionFile,
        dataFolder: File,
        file: File?
    ) : super(loader, description, dataFolder, File(System.getProperty("user.dir", "api/src/test/kotlin/CustomMockPlugin.kt")))

    override fun getCommand(name: String): PluginCommand {
        return PluginCommandUtils.createPluginCommand(name, this)
    }
}