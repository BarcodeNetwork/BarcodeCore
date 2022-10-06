package com.vjh0107.barcode.api.controller.routers

import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.serialization.serialize
import com.vjh0107.barcode.api.controller.resources.ProfileListResource
import com.vjh0107.barcode.api.services.ProfileListService
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRouter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

@BarcodeComponent
class ProfileListRouter(private val service: ProfileListService, private val json: Json) : BarcodeRouter {
    override fun Routing.route() {
        get<ProfileListResource.MinecraftID> { params ->
            val respond = service.getProfilesIndexed(MinecraftPlayerID.of(params.id))
            if (respond.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "접속한 적 없는 플레이어입니다.")
                return@get
            }

            call.respondText(respond.serialize(json))
        }
    }

}