package com.vjh0107.barcode.clientblock.database.entity

import com.vjh0107.barcode.framework.database.exposed.extensions.json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ClientBlockDataTable : IntIdTable("barcodecore_client_blocks") {
    val blockName: Column<String> = text("block_name").index()
    val locationPos1: Column<String> = json("location_1")
    val locationPos2: Column<String> = json("location_2")
    val blockData: Column<String> = json("block_data")
}