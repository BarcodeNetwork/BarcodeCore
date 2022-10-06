package com.vjh0107.barcode.framework.utils.asker.ask

import com.vjh0107.barcode.framework.utils.asker.data.AskPlayer
import kotlinx.coroutines.Job
import org.bukkit.entity.Player
import java.util.*

interface Ask {
    /**
     * Ask 주체입니다.
     */
    val sender: Player

    /**
     * Primary Key 입니다.
     */
    val key: UUID

    /**
     * Ask 의 Display name 입니다.
     */
    val title: String

    /**
     * Ask 중인 타겟 플레이어 Set 입니다.
     */
    val targetAskPlayers: Set<AskPlayer>

    /**
     * 타겟 플레이어들을 Set<Player> 로 반환합니다.
     */
    fun getTargetPlayers() : Set<Player> {
        return targetAskPlayers.map { it.player }.toSet()
    }

    /**
     * Ask 가 지니고 있는 Job 입니다.
     * 이 Job 은 Ask 의 만료시간을 관리합니다.
     *
     */
    val expirationJob: Job

    /**
     * expireScheduler task 를 캔슬하며, Ask 인스턴스를 삭제합니다.
     */
    fun cancel()

    /**
     * Ask 하였을 때 실행됩니다.
     */
    fun whenAsk()

    /**
     * 전체 target player 가 Ask 를 수락하였을 때 실행됩니다.
     */
    fun whenAccept()

    /**
     * Ask 를 거절하였을 때 실행됩니다.
     */
    fun whenDecline()

    /**
     * 보냅니다.
     */
    fun send()
}