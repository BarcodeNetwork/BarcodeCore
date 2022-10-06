package com.vjh0107.barcode.framework.exceptions.multiprofile

import org.bukkit.entity.Player

class PlayerProfileNotFoundException : NullPointerException {
    constructor(player: Player) : super("$player 의 profile 을 찾을 수 없습니다.")
}