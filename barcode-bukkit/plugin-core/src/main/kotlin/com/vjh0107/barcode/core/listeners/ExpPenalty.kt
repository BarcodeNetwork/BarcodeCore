package com.vjh0107.barcode.core.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import net.Indyuce.mmocore.MMOCore
import net.Indyuce.mmocore.api.ConfigMessage
import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent

@BarcodeComponent
class ExpPenalty : BarcodeListener {
    //TODO: 던전에서는 패널티를 받으면 안된다.
    private val loss = MMOCore.plugin.config.getDouble("death-exp-loss.percent") / 100

    @EventHandler(priority = EventPriority.HIGH)
    fun a(event: PlayerDeathEvent) {
        val data = PlayerData.get(event.entity)
        val loss = (data.experience * loss).toInt()
        data.experience = data.experience - loss
        if (data.isOnline) ConfigMessage("death-exp-loss").addPlaceholders("loss", "" + loss).send(data.player)
    }
}