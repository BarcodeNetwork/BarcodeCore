package com.vjh0107.barcode.clientblock.block

import org.bukkit.entity.Player
import javax.xml.stream.Location

interface ClientBlocks {
    val name: String

    val locationPos1: Location

    val locationPos2: Location

    fun loadBlocks()

    fun saveBlocks()

    fun sendClientBlocks(player: Player, delay: Long = 0L)

    fun sendRealBlocks(player: Player, delay: Long = 0L)
}