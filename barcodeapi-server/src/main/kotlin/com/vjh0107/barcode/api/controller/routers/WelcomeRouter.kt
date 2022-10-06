package com.vjh0107.barcode.api.controller.routers

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRouter
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@BarcodeComponent
class WelcomeRouter : BarcodeRouter {
    override fun Routing.route() {
        route("/") {
            get {
                call.respondText("바코드 네트워크 API 서버입니다." +
                        "\n" +
                        "\n/profiles/<마인크래프트 uuid>, " +
                        "\n/rpgPlayerData/<프로파일 uuid>," +
                        "\n/rpgPlayerData/<마인크래프트 uuid>/<index>")
            }
        }
    }
}