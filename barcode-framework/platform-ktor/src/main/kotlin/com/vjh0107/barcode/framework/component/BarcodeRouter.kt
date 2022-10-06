package com.vjh0107.barcode.framework.component

import io.ktor.server.routing.*

interface BarcodeRouter : IBarcodeComponent {
    fun Routing.route()

    /**
     * 라우터를 environment 에 등록합니다.
     */
    fun registerRouter(routing: Routing) = routing.route()
}