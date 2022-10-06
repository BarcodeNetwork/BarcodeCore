package com.vjh0107.barcode

import com.vjh0107.barcode.core.blacksmith.enhance.EnhanceMeta
import com.vjh0107.barcode.framework.utils.print
import org.junit.jupiter.api.Test


class EnhanceTest {
    @Test
    fun calculateEnhanceCostTest() {
        EnhanceMeta.getEnhanceCost(80, 1, 1).print()
    }

    @Test
    fun calculateEnhancePenaltyTest() {
        EnhanceMeta.getUpgradeChancePenalty(110, 2).print()
    }

    @Test
    fun calculateEnhanceChanceTest() {
        EnhanceMeta.calculateEnhanceChance(80.0, 10, 1).print()
    }

}