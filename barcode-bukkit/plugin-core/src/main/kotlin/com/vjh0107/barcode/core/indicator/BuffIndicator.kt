package com.vjh0107.barcode.core.indicator

import com.vjh0107.barcode.core.BarcodeCorePlugin
import com.vjh0107.barcode.core.database.getNullableCorePlayerData
import org.bukkit.OfflinePlayer
import org.bukkit.scheduler.BukkitTask

class BuffIndicator(
    val player: OfflinePlayer,
    val key: String,
    var icon: String,
    tick: Int,
    val isDebuff: Boolean,
    var onEnd: () -> Unit
) {
    constructor(player: OfflinePlayer, key: String, icon: String, tick: Int, isDebuff: Boolean) : this(
        player,
        key,
        icon,
        tick,
        isDebuff,
        {}
    )

    private val delay: Int = tick % 20
    var second: Int = (tick - (tick % 20)) / 20

    private val scheduler: BukkitTask = BarcodeCorePlugin.runAsyncTaskTimer(delay.toLong(), 20) {
        this.second -= 1
        if (second <= 0) {
            this.close()
        }
    }

    fun close() {
        if (!this.scheduler.isCancelled) {
            this.scheduler.cancel()
            this.onEnd()
            // 플레이어가 오프라인이라면 remove key 해줄 필요가 없다.
            player.uniqueId.getNullableCorePlayerData()?.buffIndicator?.indicators?.remove(key)
        }
    }
}
