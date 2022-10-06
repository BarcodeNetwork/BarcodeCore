package com.vjh0107.barcode.core.database.player.root

import com.vjh0107.barcode.framework.database.exposed.entity.BarcodePlayerEntity
import com.vjh0107.barcode.framework.database.exposed.entity.BarcodeEntityClass
import com.vjh0107.barcode.framework.database.exposed.extensions.json
import com.vjh0107.barcode.framework.database.exposed.table.BarcodeIDTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

class RootPlayerDataEntity(id: EntityID<Int>) : BarcodePlayerEntity(id) {
    companion object : BarcodeEntityClass<RootPlayerDataEntity>(RootPlayerDataTable)

    override var profileID by RootPlayerDataTable.profileID
    override var playerID by RootPlayerDataTable.playerID

    /**
     * 계정 생성일
     */
    var createdAt by RootPlayerDataTable.createdAt

    /**
     * 언제 데이터가 업데이트 됐는지
     */
    var updatedAt by RootPlayerDataTable.updatedAt

    var nicknames by RootPlayerDataTable.nicknames
}

object RootPlayerDataTable : BarcodeIDTable("barcodecore_player_root") {
    val createdAt = datetime("created_at").index().default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val nicknames = json("nicknames")
}
