package com.vjh0107.barcode.framework.exceptions

import java.util.*

class PlayerNotFoundException : RuntimeException {
    constructor() : super("플레이어를 찾을 수 없습니다.")
    constructor(playerName: String) : super("이름 $playerName 으로 플레이어를 찾을 수 없습니다.")
    constructor(playerUUID: UUID) : super("uuid $playerUUID 로 플레이어를 찾을 수 없습니다.")
}