package com.vjh0107.barcode.framework.utils.formatters

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Double.toBarcodeFormat() : String {
    return String.format("%.1f",this).replace(".0", "")
}

fun Double.toPreciseBarcodeFormat() : String {
    return String.format("%.2f",this).replace(".00", "")
}

fun <T : Collection<String>> T.toBarcodeFormat() : String {
    var strBuilder: String? = null
    this.forEach forEach@{ string ->
        if (strBuilder == null) {
            strBuilder = string
            return@forEach
        }
        strBuilder = "$strBuilder, $string"
    }
    return strBuilder ?: "[BarcodeFormatters] err1: $this"
}

/**
 * collection 을 줄여서 표기해줌.
 *
 * <remain> 혹은 <all> 을 넣어서 줄인 후의 메시지를 정할 수 있다.
 */
fun <T : Collection<String>> T.toBarcodeFormat(size: Int, message: String): String {
    var strBuilder: String? = null
    repeat(size) { index ->
        val string = this@toBarcodeFormat.toList()[index]
        if (strBuilder == null) {
            strBuilder = string
            return@repeat
        }
        strBuilder = "$strBuilder, $string"
    }
    val remain = this.size - size

    return if (remain > 0) {
        if (message.contains("<remain>")) {
            "$strBuilder ${message.replace("<remain>", remain.toString())}"
        } else if (message.contains("<all>")) {
            "$strBuilder ${message.replace("<all>", this.size.toString())}"
        } else {
            throw IllegalArgumentException("<remain> 혹은 <all> 플레이스홀더를 넣어주세요.")
        }
    } else {
        strBuilder ?: "[BarcodeFormatters] err2: $this"
    }
}

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")

fun LocalDateTime.toBarcodeFormat() : String {
    return this.format(dateTimeFormatter);
}