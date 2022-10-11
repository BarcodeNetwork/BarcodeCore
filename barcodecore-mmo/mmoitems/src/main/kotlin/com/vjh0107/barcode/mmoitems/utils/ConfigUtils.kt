package com.vjh0107.barcode.mmoitems.utils

/**
 * 문자열을 내부 아이디로 만듭니다.
 */
fun String.toInternalId(): String {
    return this.uppercase().replace("-", "_")
}