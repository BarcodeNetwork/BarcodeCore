package com.vjh0107.barcode.core.itembox.service

import com.vjh0107.barcode.core.itembox.data.ItemBoxItem
import org.bukkit.entity.Player

interface ItemBoxService {
    /**
     * 플레이어의 우편함 아이템을 구합니다.
     */
    fun getItem(player: Player, predicate: (ItemBoxItem) -> Boolean): ItemBoxItem?

    /**
     * 플레이어의 우편함 아이템을 전부 구합니다.
     */
    fun getItems(player: Player): List<ItemBoxItem>

    /**
     * 플레이어의 우편함에 아이템을 추가합니다.
     */
    fun addItem(player: Player, item: ItemBoxItem)

    /**
     * 플레이어의 우편함에서 아이템을 삭제합니다.
     */
    fun removeItem(player: Player, predicate: (ItemBoxItem) -> Boolean)

    /**
     * 아이템을 수령합니다.
     *
     * @return 수령에 성공하였으면 true 를 반환합니다.
     */
    fun receiveItem(player: Player, predicate: (ItemBoxItem) -> Boolean): Boolean

    /**
     * 플레이어의 우편함에서 만료된 아이템들을 반환합니다.
     *
     * @return 만료된 아이템들
     */
    fun getExpiredItems(player: Player): List<ItemBoxItem>

    /**
     * 플레이어의 우편함에서 만료된 아이템들을 삭제합니다.
     *
     * @return 아이템이 삭제되었으면 true 를 반환합니다.
     */
    fun removeExpiredItems(player: Player): Boolean
}