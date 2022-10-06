package com.vjh0107.barcode.framework.utils

import java.util.logging.Logger

inline fun <R> Logger.logTimeSpent(message: String, block: () -> R): R {
    val start = System.currentTimeMillis()
    val answer = block()
    this.info(message + " 완료에 걸린시간: " + (System.currentTimeMillis() - start) + "ms")
    return answer
}
