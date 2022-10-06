package com.vjh0107.barcode.framework.database.exposed.entity

import com.vjh0107.barcode.framework.database.exposed.table.BarcodeIDTable
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.dao.IntEntityClass

abstract class BarcodeEntityClass<out E : BarcodePlayerEntity>(
    private val barcodeTable: BarcodeIDTable,
    entityType: Class<E>? = null
) : IntEntityClass<E>(barcodeTable, entityType) {

    /**
     * ProfileID 로 칼럼을 찾습니다.
     *
     * @param targetProfileID ProfileID
     * @return <out E : BarcodeEntity> 단일 반환
     */
    fun findByProfileID(targetProfileID: ProfileID): E? {
        return this.find { barcodeTable.profileID eq targetProfileID }.firstOrNull()
    }

    /**
     * MinecraftPlayerID 로 칼럼들을 찾습니다.
     *
     * @param targetMinecraftPlayerID MinecraftPlayerID
     * @return <out E : BarcodeEntity> 를 리스트로 반환
     */
    fun findByMinecraftPlayerID(targetMinecraftPlayerID: MinecraftPlayerID): List<E> {
        return this.find { barcodeTable.playerID eq targetMinecraftPlayerID }.toList()
    }

    /**
     * PlayerIDWrapper 를 인자로 새로운 엔티티를 생성합니다.
     *
     * @param playerIDWrapper PlayerIDWrapper
     * @param init init block
     */
    fun new(playerIDWrapper: PlayerIDWrapper, init: E.() -> Unit) {
        new(playerIDWrapper.profileID, playerIDWrapper.minecraftPlayerID, init)
    }

    /**
     * ProfileID 와 MinecraftPlayerID 를 인자로 새로운 엔티티를 생성합니다.
     *
     * @param profileID ProfileID
     * @param playerUUID MinecraftPlayerID
     * @param init init block
     */
    fun new(profileID: ProfileID, playerUUID: MinecraftPlayerID, init: E.() -> Unit) {
        val playerDataInit: E.() -> Unit = {
            this.profileID = profileID
            this.playerID = playerUUID
            this.init()
        }
        this.new(null, playerDataInit)
    }
}
