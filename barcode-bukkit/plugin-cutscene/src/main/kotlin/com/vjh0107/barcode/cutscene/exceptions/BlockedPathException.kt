package com.vjh0107.barcode.cutscene.exceptions

class BlockedPathException(
    startLocationBlocked: Boolean,
    endLocationBlocked: Boolean
) : RuntimeException("시작지점과 도착자점의 위치가 막혀있습니다. ${checkBlocked(startLocationBlocked, endLocationBlocked)}") {
    companion object {
        fun checkBlocked(startLocationBlocked: Boolean, endLocationBlocked: Boolean): String {
            return "startLocationBlocked: $startLocationBlocked, endLocationBlocked: $endLocationBlocked"
        }
    }
}