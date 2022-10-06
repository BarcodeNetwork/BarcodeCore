package com.vjh0107.barcode.framework.serialization

import com.vjh0107.barcode.framework.serialization.serializers.LocalDateTimeSerializer
import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class SerializationModule {
    @Single
    fun json() = Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            contextual(UUIDSerializer)
            contextual(LocalDateTimeSerializer)
        }
    }
}