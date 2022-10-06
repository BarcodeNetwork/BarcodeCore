package com.vjh0107.barcode.api.controller.routers

import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import com.vjh0107.barcode.framework.serialization.serialize
import com.vjh0107.barcode.api.controller.resources.RPGPlayerDataResource
import com.vjh0107.barcode.api.services.RPGPlayerDataService
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRouter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

@BarcodeComponent
class RPGPlayerDataInfoRouter(private val service: RPGPlayerDataService, private val json: Json) : BarcodeRouter {
    override fun Routing.route() {
        get<RPGPlayerDataResource.ByIndex> { params ->
            val id = params.id
            val index = params.index

            val respond = service.getByIndex(MinecraftPlayerID.of(id), index)
            if (respond == null) {
                call.respond(HttpStatusCode.NotFound, "프로파일이 존재하지 않습니다.")
                return@get
            }
            call.respondText(respond.serialize(json))
        }

        get<RPGPlayerDataResource.ByProfileID> { params ->
            val id = params.id

            val respond = service.getByProfileId(ProfileID.of(id))
            if (respond == null) {
                call.respond(HttpStatusCode.NotFound, "프로파일이 존재하지 않습니다.")
                return@get
            }
            call.respondText(respond.serialize(json))
        }
    }
}