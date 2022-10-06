package com.vjh0107.barcode.cutscene.npc.data

import com.mojang.authlib.properties.Property
import com.vjh0107.barcode.cutscene.npc.dto.GameProfileWrapper
import com.vjh0107.barcode.framework.BarcodeFrameworkModule
import com.vjh0107.barcode.framework.serialization.SerializableData
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Skin private constructor (
    val value: String,
    val signature: String
) : SerializableData {
    companion object {
        /**
         * 스킨을 uuid 를 통해 가져옵니다.
         *
         * @param uuid target player uuid
         * @throws Missing
         * @throws RequestTimeOutException
         * @return 스킨
         */
        suspend fun fetchSkin(uuid: UUID): Skin {
            val gameProfile = withContext(Dispatchers.IO) {
                val url = "https://sessionserver.mojang.com/session/minecraft/profile/$uuid?unsigned=false"
                val response = BarcodeFrameworkModule.httpClient.get(url)
                response.body<GameProfileWrapper>()
            }

            return with(gameProfile.properties.first()) {
                Skin(this.value, this.signature)
            }
        }

        @JvmStatic
        fun of(value: String, signature: String): Skin {
            return Skin(value, signature)
        }
    }

    /**
     * Property 로 만듭니다.
     */
    fun createProperty(): Property {
        return Property(this.value, this.signature)
    }
}