package com.vjh0107.barcode

import com.vjh0107.barcode.framework.utils.print
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class DatabaseTest {

    @Serializable
    data class Skills(val skills: MutableMap<String, Int> = mutableMapOf())

    @Test
    fun serializeTest() {
        val skills = Skills().apply {
            skills["Skill_1"] = 3
            skills["Skill_2"] = 4
            skills["Skill_4"] = 1
        }
        val serialized = Json.encodeToString(skills)
        serialized.print()
    }

}