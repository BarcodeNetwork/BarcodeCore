package com.vjh0107.barcode.core.meister.repository.impl

import com.vjh0107.barcode.core.database.player.childs.MeisterPlayerDataEntity
import com.vjh0107.barcode.core.database.player.childs.MeisterPlayerDataTable
import com.vjh0107.barcode.core.meister.data.MeisterSkillPlayerData
import com.vjh0107.barcode.core.meister.repository.MeisterSkillRepository
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.getPlayerID
import com.vjh0107.barcode.framework.database.player.repository.AbstractSavablePlayerDataRepository
import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import com.vjh0107.barcode.framework.koin.annotation.BarcodeSingleton
import com.vjh0107.barcode.framework.serialization.serialize
import org.bukkit.entity.Player

@BarcodeSingleton(binds = [MeisterSkillRepository::class])
@BarcodeComponent
class MeisterSkillRepositoryImpl(
    plugin: AbstractBarcodePlugin
) : AbstractSavablePlayerDataRepository<MeisterSkillPlayerData>(plugin), MeisterSkillRepository {
    override fun getTablesToLoad() = listOf(MeisterPlayerDataTable)

    override suspend fun loadData(id: PlayerIDWrapper): MeisterSkillPlayerData {
        return dataSource.query {
            val result = MeisterPlayerDataEntity.findByIdOrNew(id.profileID)
            MeisterSkillPlayerData.of(result)
        }
    }

    override suspend fun saveData(id: PlayerIDWrapper, playerData: MeisterSkillPlayerData) {
        dataSource.query {
            with(MeisterPlayerDataEntity[id.profileID]) {
                skills = playerData.skills.serialize()
                skillPoints = playerData.skillPoints
                resetPoints = playerData.resetPoints
            }
        }
    }

    override fun getPlayerData(player: Player): MeisterSkillPlayerData {
        return getPlayerData(player.getPlayerID()) ?: throw PlayerDataNotFoundException(player)
    }
}