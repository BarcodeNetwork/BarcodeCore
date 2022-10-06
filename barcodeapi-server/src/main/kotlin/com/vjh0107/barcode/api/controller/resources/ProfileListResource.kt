package com.vjh0107.barcode.api.controller.resources

import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Resource("/profiles")
class ProfileListResource {

    @Serializable
    @Resource("{id}")
    class MinecraftID(
        val parent: ProfileListResource = ProfileListResource(),
        @Serializable(with = UUIDSerializer::class) val id: UUID
    )
}