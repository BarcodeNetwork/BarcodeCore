package com.vjh0107.barcode.framework.inventory

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

interface BarcodeInventory : InventoryHolder {
    /**
     * SlotItem 정보를 담습니다. object 키워드로 선언 후 상속하여 사용합니다.
     */
    interface SlotItem {
        val slots: List<Int>
        fun get(): ItemStack
    }

    /**
     * SlotItem 을 통해서 아이템을 설정합니다.
     */
    fun Inventory.setItems(item: SlotItem) : Inventory

    /**
     * 인벤토리 주인입니다.
     */
    val player: Player

    /**
     * getInventory() 로 얻은 인벤토리를 플레이어에게 엽니다.
     */
    fun open()

    /**
     * 인벤토리 인스턴스를 생성합니다. Bukkit 의 InventoryHolder 를 사용합니다.
     */
    override fun getInventory(): Inventory

    /**
     * gui 인벤토리 클릭 이벤트
     */
    fun whenClickedTop(event: InventoryClickEvent)

    /**
     * 플레이어 인벤토리 클릭 이벤트
     */
    fun whenClickedBottom(event: InventoryClickEvent)

    /**
     * 인벤토리 닫을때 발생되는 이벤트를 구현합니다.
     */
    fun whenClosed(event: InventoryCloseEvent)
}