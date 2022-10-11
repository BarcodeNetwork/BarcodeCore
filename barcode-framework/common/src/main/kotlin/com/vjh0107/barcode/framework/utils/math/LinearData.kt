package com.vjh0107.barcode.framework.utils.math

import kotlin.math.max
import kotlin.math.min

/**
 * 인자에 따라 선형으로 데이터를 저장합니다.
 */
data class LinearData private constructor (
    val base: Double,
    val perParameter: Double,
    val min: Double? = null,
    val max: Double? = null
) {
    fun hasMax(): Boolean {
        return max != null
    }
    fun hasMin(): Boolean {
        return min != null
    }

    fun calculate(parameter: Int): Double {
        var value = base + perParameter * (parameter - 1)
        if (min != null) value = max(min, value)
        if (max != null) value = min(max, value)
        return value
    }

    companion object {
        val ZERO = LinearData(0.0, 0.0, 0.0, 0.0)

        fun of(base: Double, perParameter: Double, min: Double? = null, max: Double? = null): LinearData {
            return LinearData(base, perParameter, min, max)
        }
    }
}
