package com.vjh0107.barcode.core.database.player.childs

import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefTable
import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefEntity
import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefEntityClass
import com.vjh0107.barcode.framework.database.exposed.extensions.json
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.dao.id.EntityID

class RPGPlayerDataEntity(id: EntityID<ProfileID>) : RootPlayerDataRefEntity(id) {
    companion object : RootPlayerDataRefEntityClass<RPGPlayerDataEntity>(RPGPlayerDataTable)

    var classID by RPGPlayerDataTable.classID
    var level by RPGPlayerDataTable.level
    var experience by RPGPlayerDataTable.experience

    var skills by RPGPlayerDataTable.skills
    var skillPoints by RPGPlayerDataTable.skillPoints
    var boundSkills by RPGPlayerDataTable.boundSkills

    var attributes by RPGPlayerDataTable.attributes
    var attributePoints by RPGPlayerDataTable.attributePoints
    var attributeResetPoints by RPGPlayerDataTable.attributeResetPoints
}

object RPGPlayerDataTable : RootPlayerDataRefTable("barcodecore_player_rpg") {
    val classID = varchar("class", 20)
    val level = integer("level")
    val experience = long("experience").default(0)

    val skills = json("skills")
    val skillPoints = integer("skill_points")
    val boundSkills = json("bound_skills")

    val attributes = json("attributes")
    val attributePoints = integer("attribute_points")
    val attributeResetPoints = integer("attribute_reset_points")
}
