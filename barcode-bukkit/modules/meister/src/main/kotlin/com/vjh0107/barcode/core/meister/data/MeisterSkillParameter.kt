package com.vjh0107.barcode.core.meister.data

import com.vjh0107.barcode.framework.utils.formatters.toBarcodeFormat

data class MeisterSkillParameter private constructor (val base: Double, val increase: Double) {
    companion object {
        fun of(base: Double, increase: Double) : MeisterSkillParameter {
            return MeisterSkillParameter(base, increase)
        }
    }

    fun get(level: Int) : Double {
        return this.base + (this.increase * level)
    }

    fun getDisplay(level: Int) : String {
        return get(level).toBarcodeFormat()
    }
}
