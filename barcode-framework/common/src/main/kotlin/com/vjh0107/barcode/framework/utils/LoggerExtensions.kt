package com.vjh0107.barcode.framework.utils

import java.util.logging.Level
import java.util.logging.Logger

inline fun <R> Logger.logTimeSpent(message: String, block: () -> R): R {
    val start = System.currentTimeMillis()
    val answer = block()
    this.info(message + " 완료에 걸린시간: " + (System.currentTimeMillis() - start) + "ms")
    return answer
}

/**
 * Thread.getStackTrace -> LoggerExtensionKt.traceableLog -> 호출된 곳
 */
fun Logger.traceableLog(logLevel: Level, message: String) {
    this.log(logLevel, "[${Thread.currentThread().stackTrace[2].className.split(".").last()}] $message")
}

fun Logger.traceableInfo(message: String) {
    this.traceableLog(Level.INFO, message)
}