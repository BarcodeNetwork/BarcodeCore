package com.vjh0107.barcode.clientblock.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClientBlockDataEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClientBlockDataEntity>(ClientBlockDataTable)

    /**
     * client block 의 이름입니다.
     */
    var blockName by ClientBlockDataTable.blockName

    /**
     * client block 의 위치입니다.
     */
    var locationPos1 by ClientBlockDataTable.locationPos1
    var locationPos2 by ClientBlockDataTable.locationPos2

    /**
     * client block 의 데이터입니다.
     */
    var blockData by ClientBlockDataTable.blockData
}