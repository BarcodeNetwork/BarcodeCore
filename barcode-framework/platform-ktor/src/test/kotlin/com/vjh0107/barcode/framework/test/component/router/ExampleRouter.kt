package com.vjh0107.barcode.framework.test.component.router

import com.vjh0107.barcode.framework.AbstractBarcodeApplication
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRouter
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.name

@BarcodeComponent
class ExampleRouter(
    private val dataSource: BarcodeDataSource,
    private val barcodeApplication: AbstractBarcodeApplication
) : BarcodeRouter {
    override fun Routing.route() {
        get("/") {
            call.respondText("example1, database: ${dataSource.database.name}, application: ${barcodeApplication.getDatabaseHost()}")
        }
    }
}