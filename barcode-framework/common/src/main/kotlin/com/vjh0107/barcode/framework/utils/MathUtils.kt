package com.vjh0107.barcode.framework.utils

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.roundToInt

fun List<Float>.getAverageOfFloats() : Float {
    return this.apply {
        listOf(this.maxOrNull() ?: 0f, this.minOrNull() ?: 0f)
    }.average().toFloat()
}

fun atan2toAngle (x: Double, y: Double): Double {
    return (atan2(y, x) *180)/ PI
}

/**
 * 이거 진짜 편합니다. 진짜로요.
 * null 을 0 으로 바꿔줍니다.
 */
fun Double?.nullToZero() : Double {
    return this ?: 0.0
}

/**
 * 주어진 자릿수만큼 '남기고' 반올림합니다.
 * @sample decimals 가 3 일때, 1.2355 -> 1.236
 */
fun round(number: Double, decimals: Int): Double {
    val rounded = (number * 10.0.pow(decimals.toDouble())).roundToInt()
    return rounded / 10.0.pow(decimals.toDouble())
}

/**
 * 불필요한 0을 전부 지워줍니다.
 * @sample 1.0000 -> 1, 1.000100 -> 1.0001
 */
fun removeDecimalZeros(source: String): String {
    if (source.contains(".")) {
        val decimals = source.substring(source.lastIndexOf("."))
        // 마지막 0 이 아닌 char 을 찾습니다.
        var lastChar = -1
        for (index in 1 until decimals.length) {
            val char = decimals[index]
            if (char != '0') {
                lastChar = index
            }
        }
        return source.substring(0, source.lastIndexOf(".") + lastChar + 1)
    }
    return source
}

/**
 * 불필요한 0을 전부 지워주고, 소수점 다섯번째 자리에서 반올림 합니다.
 * @sample decimals 가 3이고, 값이 5.1627000 일때, 5.163
 */
fun cleanRound(target: Double, decimals: Int): String {
    return removeDecimalZeros(round(target, decimals).toString())
}