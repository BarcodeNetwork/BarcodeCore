package com.vjh0107.barcode.framework.database.player

import com.vjh0107.barcode.framework.serialization.SerializableData
import java.util.*

interface Identifier : Comparable<Identifier>, SerializableData {
    val id: UUID

    override fun compareTo(other: Identifier): Int {
        return this.id.compareTo(other.id)
    }
}