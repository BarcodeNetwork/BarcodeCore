package com.vjh0107.barcode.framework.utils.transformer

import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalContracts::class)
fun Entity.isPlayer(): Boolean {
    contract { returns(true) implies (this@isPlayer is Player) }

    return type == EntityType.PLAYER
}

fun Entity.toPlayer(): Player {
    if (this.isPlayer()) {
        return this
    }

    throw TypeCastException("엔티티가 플레이어가 아닙니다.")
}

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalContracts::class)
fun CommandSender.isPlayer(): Boolean {
    contract { returns(true) implies (this@isPlayer is Player) }

    return this as? Player != null
}

fun CommandSender.toPlayer(): Player {
    if (this.isPlayer()) {
        return this
    }

    throw TypeCastException("엔티티가 플레이어가 아닙니다.")
}