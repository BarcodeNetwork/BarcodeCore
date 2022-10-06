package com.vjh0107.barcode.framework.database.exposed.table

import com.vjh0107.barcode.framework.database.exposed.column.impl.PlayerIDColumnType
import com.vjh0107.barcode.framework.database.exposed.column.impl.ProfileIDColumnType
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

/**
 * PlayerID 와 profileID 를 index 로 가짐.
 *
 * @param name 테이블 이름임. 만약에 빈 공간일 경우 클래스 이름에서 Table 을 뺀 값으로 들어가게 됨.
 */
open class BarcodeIDTable(name: String = "") : IntIdTable(name) {
    val profileID = profileID("profileID").uniqueIndex("profileID")
    val playerID = playerID("playerID").index("playerID", false)

    fun playerID(name: String): Column<MinecraftPlayerID> {
        return registerColumn(name, PlayerIDColumnType())
    }

    fun profileID(name: String): Column<ProfileID> {
        return registerColumn(name, ProfileIDColumnType())
    }
}

