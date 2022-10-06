package com.vjh0107.barcode.framework.serialization

import com.vjh0107.barcode.framework.serialization.serializers.LocalDateTimeSerializer
import com.vjh0107.barcode.framework.utils.print
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class SerializeTest {
    @Test
    fun test() {
        SerializableObject("Sdf").serialize().run { println(this) }
        SerializableObject("Sdf").serialize().deserialize<SerializableObject>().run { println(this.a) }
    }

    @Serializable
    data class SerializableObject(val a: String = "asdf") : SerializableData

    @Serializable
    data class SerializableObjects(
        val map: MutableMap<String, SerializableObject> = mutableMapOf(Pair("key", SerializableObject("aa")))
    ) : SerializableData

    @Test
    fun serializeMap() {
        SerializableObjects(mutableMapOf(Pair("a", SerializableObject("aaa")))).serialize().print()
    }

    @Test
    fun serializeBytes() {
        val objectA = SerializableObjects(
            mutableMapOf(
                Pair(
                    "a",
                    SerializableObject("abaaqetrehyr4eh4terkqryklyrjrtqaaaetrehyr4eh4terkqryklyrjrtqㅂㄷ3ㅛ3ㅅㅂ234aaaaaaaaaaabaㅁㄴㅇaaaaaaaabaaaaaaccaaaaaaaabaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                )
            )
        )
        val serialize = objectA.serialize().toByteArray().print()
        val deserialize = String(serialize).print()
    }

    @Test
    fun charArrayTest() {
        val charArray = charArrayOf('a', 'b')
        charArray.toString().print()
        String(charArray).print()
    }

    @Serializable
    data class TimeContainer(@Serializable(with = LocalDateTimeSerializer::class) val time: LocalDateTime) :
        SerializableData

    @Test
    fun timeStampTest() {
        val time = TimeContainer(LocalDateTime.now()).serialize().print()
        val deserialize = time.deserialize<TimeContainer>().print()
    }



    @Test
    fun serializeObjectMap() {
        val data: MutableMap<String, String> = HashMap()
        data["world"] = "worldName"
        data["x"] = "1"
        data["y"] = "2"
        data["z"] = "3"
        data["yaw"] = "4"
        data["pitch"] = "5"

    }
}