package com.vjh0107.barcode.core.blacksmith.enhance

//무조건 enhanceLevel은 새로운 강화차수 기준이다.

object EnhanceMeta {
    fun getEnhanceMaxVolume(itemLevel: Int) : Double {
        return when(itemLevel) {
            in 1..49 -> 50.0
            in 50..99 -> 100.0
            in 100..149 -> 150.0
            in 150..199 -> 200.0
            in 200..250 -> 250.0
            else -> 50.0
        }
    }

    fun getEnhanceCost(itemLevel: Int, enhanceLevel: Int, tier: String): Double {
        return getEnhanceCost(itemLevel, enhanceLevel, getTierAsString(tier))
    }

    fun getEnhanceCost(itemLevel: Int, enhanceLevel: Int, tier: Int): Double {
        val goldModifier = when(tier) {
            1 -> 1.0
            2 -> 1.25
            3 -> 1.5
            4 -> 1.75
            5 -> 2.0
            6 -> 2.5
            else -> 999999.0
        }
        return getEnhanceCostFormula(itemLevel, enhanceLevel, goldModifier)
    }

    fun getTierAsString(key: String): Int {
        return key.replace("T", "").toIntOrNull()
            ?: throw ClassCastException("can't get tier by string value to int value")
    }

    fun getEnhanceCostFormula(itemLevel: Int, enhanceLevel: Int, goldModifier: Double): Double {
        return 33 * itemLevel * goldModifier + (25 * itemLevel * enhanceLevel)
    }

    fun getUpgradeChancePenalty(itemLevel: Int, enhanceNewLevel: Int) : Double {
        val enhancePenalty = 1 - when(enhanceNewLevel){
            in 1..3 -> 0.0
            4 -> 3.0
            5 -> 5.0
            6 -> 10.0
            7 -> 15.0
            8 -> 17.0
            9 -> 19.0
            10 -> 21.0
            11 -> 23.0
            12 -> 25.0
            13 -> 50.0
            14 -> 60.0
            15 -> 65.0
            16 -> 70.0
            17 -> 75.0
            18 -> 85.0
            19 -> 90.0
            20 -> 95.0
            else -> 100.0
        } / 100.0

        val enhanceReadjust = when(itemLevel) {
            in 1..49 -> 100.0
            in 50..99 -> 50.0
            in 100..149 -> 33.0
            in 150..199 -> 25.0
            in 200..250 -> 20.0
            else -> 0.0
        } / 100.0

        return enhancePenalty * enhanceReadjust
    }

    fun calculateEnhanceChance(enhanceChance: Double, itemLevel: Int, enhanceNewLevel: Int) : Double {
        return enhanceChance * getUpgradeChancePenalty(itemLevel, enhanceNewLevel)
    }

//    fun getWeaponUpgradeModifier(enhanceLevel: Int) : Double {
//        return when(enhanceLevel) {
//            1 -> 2
//            2 -> 3
//            3 -> 5
//            4 -> 6
//            5 -> 7
//            6 -> 10
//            7 -> 7
//            8 -> 7
//            9 -> 9
//            10 -> 12
//            11 -> 9
//            12 -> 11
//            13 -> 12
//            14 -> 13
//            15 -> 17
//            16 -> 15
//            17 -> 17
//            18 -> 19
//            19 -> 22
//            20 -> 30
//            else -> 0
//        } / 100.0
//    }
}