package com.vjh0107.barcode.framework.exceptions.multiprofile

import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID

class IllegalPlayerIDException : IllegalArgumentException {
    constructor(id: String) : super("$id 는 올바른 플레이어 ID 값이 아닙니다.")
    constructor(playerID: MinecraftPlayerID) : super("${playerID.id} 는 올바른 플레이어 ID 값이 아닙니다.")
}