package com.vjh0107.barcode

import com.vjh0107.barcode.core.playerclass.models.statmap.AbstractClassStatMap
import com.vjh0107.barcode.framework.utils.print
import org.junit.jupiter.api.Test

class StatTest {
    @Test
    fun calculateTest(classStatMap: AbstractClassStatMap) {
        classStatMap.calculateStats(200).print()
    }
}
