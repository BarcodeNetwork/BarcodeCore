package com.vjh0107.barcode.framework

import java.util.logging.Logger

/**
 * 로거를 제공해야하는 클래스에 필요합니다.
 *
 * TODO: https://github.com/Kotlin/KEEP/blob/binary-signature/proposals/multiplatform/binary-signature.md
 * 반영되면 val logger: Logger 로 변경
 */
interface LoggerProvider {
    // val logger: Logger

    fun getLogger(): Logger
}