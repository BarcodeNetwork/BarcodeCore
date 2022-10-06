package com.vjh0107.barcode.framework.exceptions.playerdata

import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import org.bukkit.entity.Player

class PlayerDataNotFoundException : NullPointerException {
    constructor() : super("플레이어 데이터를 구해올 수 없습니다.")

    constructor(debug: String) : super("플레이어 데이터를 구해올 수 없습니다. debug value: $debug")

    constructor(id: PlayerIDWrapper) : super(
        "$id 를 가진 플레이어 데이터를 얻을 수 없는 상황입니다. 플레이어가 접속중일 때 구해질 수 있도록 올바르게 구현해주세요."
    )

    constructor(player: Player) : super(
        "${player}의 플레이어 데이터를 온라인 플레이어 객체를 통해 얻을 수 없는 상황입니다. 플레이어가 접속중일 때 구해질 수 있도록 올바르게 구현해주세요."
    )

    constructor(player: Player, debug: String) : super(
        "${player}의 플레이어 데이터를 온라인 플레이어 객체를 통해 얻을 수 없는 상황입니다. 플레이어가 접속중일 때 구해질 수 있도록 올바르게 구현해주세요."
                + " debug value: $debug"
    )
}