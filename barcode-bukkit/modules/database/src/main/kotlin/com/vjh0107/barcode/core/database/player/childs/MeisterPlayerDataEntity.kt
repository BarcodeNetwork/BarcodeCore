package com.vjh0107.barcode.core.database.player.childs

import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefEntity
import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefEntityClass
import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefTable
import com.vjh0107.barcode.framework.database.exposed.extensions.json
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.dao.id.EntityID

class MeisterPlayerDataEntity(id: EntityID<ProfileID>) : RootPlayerDataRefEntity(id) {
    companion object : RootPlayerDataRefEntityClass<MeisterPlayerDataEntity>(MeisterPlayerDataTable)

    var levels by MeisterPlayerDataTable.levels

    var skills by MeisterPlayerDataTable.skills
    var skillPoints by MeisterPlayerDataTable.skillPoints
    var resetPoints by MeisterPlayerDataTable.resetPoints
}

object MeisterPlayerDataTable : RootPlayerDataRefTable("barcodecore_player_meister") {
    val levels = json("levels")

    val skills = json("skills")
    val skillPoints = integer("skill_points")
    val resetPoints = integer("reset_points")
}
