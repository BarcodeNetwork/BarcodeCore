package com.vjh0107.barcode.cutscene.recording.data

import com.vjh0107.barcode.cutscene.npc.data.Equipment
import com.vjh0107.barcode.cutscene.npcs.Position
import org.bukkit.Location

data class Node(
    val location: Location,
    val equipment: Equipment,
    val position: Position,
    val action: String,
    val animation: Int,
    val activating: Boolean,
    val onFire: Boolean
) {
}