package com.vjh0107.barcode.api.test

import com.vjh0107.barcode.framework.utils.toUUID

object Junhyung {
    val playerUUID = "bc770b0e60d64fe5b5e9c7b83c0daeb8".toUUID()
    val profiles = listOf(RPGPlayerData1, RPGPlayerData2, RPGPlayerData3)

    interface RPGPlayerDataTemplate {
        val classID: String
        val level: Int
        val experience: Long

        val skills: Map<String, Int>
        val skillPoints: Int
        val boundSkills: List<String>

        val attributes: Map<String, Int>
        val attributePoints: Int
        val attributeResetPoint: Int
    }

    object RPGPlayerData1 : RPGPlayerDataTemplate {
        override val classID = "BLADE"
        override val level = 120
        override val experience = 123232L

        override val skills = mapOf(Pair("어설트", 12), Pair("피어스커터", 20), Pair("스핀엣지", 11))
        override val skillPoints = 14
        override val boundSkills = listOf("어설트", "피어스커터")

        override val attributes = mapOf(Pair("생명", 1), Pair("민첩", 26))
        override val attributePoints = 43
        override val attributeResetPoint = 1
    }

    object RPGPlayerData2 : RPGPlayerDataTemplate {
        override val classID = "PALADIN"
        override val level = 111
        override val experience = 12232L

        override val skills = mapOf(Pair("차지스매시", 12), Pair("플레어컷", 20), Pair("브레이크", 11))
        override val skillPoints = 11
        override val boundSkills = listOf("플레어컷", "브레이크")

        override val attributes = mapOf(Pair("생명", 11), Pair("민첩", 20))
        override val attributePoints = 23
        override val attributeResetPoint = 2
    }

    object RPGPlayerData3 : RPGPlayerDataTemplate {
        override val classID = "HUNTER"
        override val level = 93
        override val experience = 21232L

        override val skills = mapOf(Pair("exampleskill1", 12), Pair("exampleskill2", 20), Pair("exampleskill3", 1))
        override val skillPoints = 21
        override val boundSkills = listOf("exampleskill1", "exampleskill3")

        override val attributes = mapOf(Pair("생명", 2), Pair("민첩", 30))
        override val attributePoints = 21
        override val attributeResetPoint = 4
    }
}