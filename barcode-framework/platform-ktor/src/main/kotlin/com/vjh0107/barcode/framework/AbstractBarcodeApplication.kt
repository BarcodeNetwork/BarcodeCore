package com.vjh0107.barcode.framework

import com.vjh0107.barcode.framework.component.BarcodeRouter
import com.vjh0107.barcode.framework.component.handler.ComponentHandlers
import com.vjh0107.barcode.framework.component.handler.KtorComponentHandlerModule
import com.vjh0107.barcode.framework.database.KtorDatabaseModule
import com.vjh0107.barcode.framework.database.config.DatabaseHostProvider
import com.vjh0107.barcode.framework.koin.KoinContextualApplication
import com.vjh0107.barcode.framework.koin.initKoin
import com.vjh0107.barcode.framework.koin.injector.inject
import com.vjh0107.barcode.framework.serialization.SerializationModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import io.ktor.server.cio.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.slf4j.Logger

/**
 * Ktor 어플리케이션
 *
 * onLoad: ApplicationEngine 이 빌드될 때
 * onEnable: Application 이 시작될 때
 * onDisable: Application 이 꺼질 때
 */
abstract class AbstractBarcodeApplication : KoinContextualApplication, DatabaseHostProvider {
    /**
     * 컴포넌트 handler 들 (lazy)
     */
    private val componentHandlers: ComponentHandlers by inject { parametersOf(this) }

    /**
     * Router 들
     */
    private val routers: MutableSet<BarcodeRouter> = mutableSetOf()

    val koinApplication = initKoin()

    /**
     * 구현된 어플리케이션의 Root 모듈 입니다.
     */
    abstract fun getApplicationModule(): Module

    final override fun getModules(): List<Module> {
        val applicationProvider = module {
            single { this@AbstractBarcodeApplication } bind (AbstractBarcodeApplication::class)
        }
        return listOf(
            applicationProvider,
            getApplicationModule(),
            KtorComponentHandlerModule().module,
            KtorDatabaseModule().module,
            SerializationModule().module
        )
    }

    /**
     * Ktor 는 jul 이 아닌, slf4j 를 사용한다.
     */
    abstract fun getSLF4JLogger(): Logger

    final override fun getLogger(): java.util.logging.Logger {
        return java.util.logging.Logger.getLogger(getSLF4JLogger().name)
    }

    /**
     * Router 를 등록합니다.
     */
    fun registerRouter(router: BarcodeRouter) {
        routers.add(router)
    }

    abstract fun getConfig(): ApplicationConfig

    open fun Application.applicationDeclaration() {}

    fun build(): ApplicationEngine {
        runBlocking {
            onPreLoad()
        }
        val environment = applicationEngineEnvironment engine@{
            log = getSLF4JLogger()
            config = getConfig()
            connector { port = config.port; host = config.host }

            module {
                applicationDeclaration()
                install(Routing) routing@{ routers.forEach { it.registerRouter(this@routing) } }
                install(applicationMonitor)
                install(ContentNegotiation) { json() }
            }
        }
        val server: ApplicationEngine = embeddedServer(CIO, environment)
        onLoad()
        val serverModule = module { single { server } }
        koinApplication.modules(serverModule)
        onPostLoad()
        return server
    }

    private val applicationMonitor = createApplicationPlugin(name = "ApplicationMonitor") {
        on(MonitoringEvent(ApplicationStarting)) {
            onPreEnable()
            onEnable()
        }
        on(MonitoringEvent(ApplicationStarted)) { onPostEnable() }

        on(MonitoringEvent(ApplicationStopPreparing)) { onPreDisable() }
        on(MonitoringEvent(ApplicationStopping)) { onDisable() }
        on(MonitoringEvent(ApplicationStopped)) { onPostDisable() }
    }

    /**
     * Ktor Router 가 intialize 되기 전에 실행
     */
    final override fun onPreLoad() {
        componentHandlers.get().forEach { it.onEnable() }
        this.getLogger().info("ComponentHandler 가 정상적으로 등록되었습니다.")
    }

    final override fun onDisable() {
        componentHandlers.get().forEach { it.onDisable() }
    }

    final override fun onPostDisable() {
        val server: ApplicationEngine by inject()
        with(server.application.environment.monitor) {
            unsubscribe(ApplicationStarting) {}
            unsubscribe(ApplicationStarted) {}
            unsubscribe(ApplicationStopPreparing) {}
            unsubscribe(ApplicationStopping) {}
            unsubscribe(ApplicationStopped) {}
        }
        stopKoin()
    }
}