package com.vjh0107.barcode

import com.vjh0107.barcode.core.meister.MeisterSkills
import org.junit.jupiter.api.Test

class MeisterTest {
    @Test
    fun getSkills() {
        MeisterSkills.map.forEach {
            println(it)
        }
    }
}