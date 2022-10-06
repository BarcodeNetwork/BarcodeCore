package com.vjh0107.barcode.framework.component.handler.impl

import com.vjh0107.barcode.framework.AbstractBarcodeApplication
import com.vjh0107.barcode.framework.component.BarcodeRouter
import com.vjh0107.barcode.framework.component.handler.AbstractKtorComponentHandler
import com.vjh0107.barcode.framework.component.handler.BarcodeComponentHandler
import com.vjh0107.barcode.framework.koin.injector.inject
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import kotlin.reflect.KClass

@BarcodeComponentHandler
@Factory(binds = [AbstractKtorComponentHandler::class])
@Named("BarcodeRouterHandler")
class BarcodeRouterHandler<A : AbstractBarcodeApplication>(
    application: A
) : AbstractKtorComponentHandler<A, BarcodeRouter>(application) {

    override fun processAnnotation(clazz: KClass<BarcodeRouter>) {
        if (clazz.java.genericInterfaces.contains(BarcodeRouter::class.java)) {
            registerComponent(clazz)
        }
    }

    override fun onPostEnable() {
        this.getComponents().forEach { router ->
            application.registerRouter(router)
            application.getLogger().info("${router::class.simpleName} 라우터를 register 하였습니다.")
        }
    }

    override fun onDisable() {
        val server: ApplicationEngine by inject()
        val routes = server.application.plugin(Routing).children as MutableList<Route>
        routes.clear()
        application.getLogger().info("Router 를 전부 제거하였습니다.")
    }
}