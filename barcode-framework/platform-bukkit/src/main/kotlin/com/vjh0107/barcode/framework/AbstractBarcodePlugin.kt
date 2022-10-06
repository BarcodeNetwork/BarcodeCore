package com.vjh0107.barcode.framework

import com.vjh0107.barcode.framework.component.handler.ComponentHandlers
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import com.vjh0107.barcode.framework.database.config.DatabaseHostProvider
import com.vjh0107.barcode.framework.events.BarcodePluginDisableEvent
import com.vjh0107.barcode.framework.exceptions.KeyNotFoundException
import com.vjh0107.barcode.framework.injection.instance.injector.InjectorFactory
import com.vjh0107.barcode.framework.koin.injector.inject
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.parameter.parametersOf
import java.io.File

abstract class AbstractBarcodePlugin : JavaPlugin(), BarcodeApplication, DatabaseHostProvider {
    /**
     * 컴포넌트 handler 들
     */
    private val componentHandlers: ComponentHandlers by inject { parametersOf(this) }

    /**
     * 플러그인이 비활성화되면 자동으로 unregister 됩니다.
     */
    fun <LISTENER : Listener> registerListener(listener: LISTENER) {
        this.server.pluginManager.registerEvents(listener, this)
    }

    /**
     * @see com.vjh0107.barcode.framework.injection.instance.InjectInstance
     */
    final override fun onLoad() {
        onPreLoad()
        InjectorFactory.getPluginInstanceInjector().inject(this)
        onPostLoad()
    }

    final override fun onEnable() {
        onPreEnable()
        componentHandlers.get().forEach {
            it.onEnable()
        }
        this.logger.info("ComponentHandler 가 정상적으로 등록되었습니다.")
        onPostEnable()
    }

    final override fun onDisable() {
        onPreDisable()
        componentHandlers.get().forEach {
            it.onDisable()
        }
        BarcodePluginDisableEvent(this).callEvent()
        onPostDisable()
    }

    /**
     * 컴포넌트 핸들러들 전부 리로드
     */
    fun reloadPlugin() {
        componentHandlers.get().forEach {
            it.onReload()
        }
    }

    // publicize
    public override fun getFile(): File {
        return super.getFile()
    }

    override fun getDatabaseHost(): DatabaseHost {
        return try {
            val databaseSection = config.getConfigurationSection("database") ?: throw KeyNotFoundException()
            val address = databaseSection.getString("address") ?: throw KeyNotFoundException()
            val port = databaseSection.getString("port") ?: throw KeyNotFoundException()
            val databaseName = databaseSection.getString("databaseName") ?: throw KeyNotFoundException()
            val user = databaseSection.getString("user") ?: throw KeyNotFoundException()
            val password = databaseSection.getString("password") ?: throw KeyNotFoundException()

            DatabaseHost(address, port, user, password, databaseName)
        } catch (exception: KeyNotFoundException) {
            logger.severe("config.yml 를 설정해주세요. 'database.(host, port, databaseName, user, password)'")
            pluginLoader.disablePlugin(this)
            throw exception
        }
    }
}