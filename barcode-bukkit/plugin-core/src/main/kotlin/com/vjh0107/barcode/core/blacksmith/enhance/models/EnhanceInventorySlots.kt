package com.vjh0107.barcode.core.blacksmith.enhance.models

import com.vjh0107.barcode.framework.utils.item.createGuiItem
import com.vjh0107.barcode.framework.utils.item.models.BarcodeGuiItems
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.ArrayList

typealias EnhanceItems = EnhanceInventorySlots.Items

enum class EnhanceInventorySlots(val item: ItemStack?, val slotNums: ArrayList<Int>) {
    ITEM_SLOT(null, arrayListOf(1)),
    PREVIEW_SLOT(Items.EMPTY_PREVIEW.item, arrayListOf(19)),
    ENHANCE_BUTTON_SLOT(Items.DEFAULT_ENHANCE_BUTTON.item, arrayListOf(16)),
    EMPTY_SLOT(Items.EMPTY.item, arrayListOf(0, 2, 6, 7, 8, 9, 10, 11, 15, 17, 18, 20, 24, 25, 26)),
    ENHANCE_MATERIAL_SLOT(null, arrayListOf(3, 4, 5, 12, 13, 14, 21, 22, 23));

    enum class Items(val item: ItemStack) {
        EMPTY(BarcodeGuiItems.EMPTY.item),
        DEFAULT_ENHANCE_BUTTON(
            createGuiItem(
                Material.DAMAGED_ANVIL,
                1,
                "§c강화하기",
                listOf("§7장비를 강화하기 위해서는 강화할 아이템", "§7그리고 강화석이 필요합니다.", "", "§e좌클릭 시 강화 도움말을 확인할 수 있습니다."),
                null)
        ),
        ITEM_THAT_MAX_UPGRADE_IS_ZERO(
            createGuiItem(
                Material.BARRIER,
                1,
                "§c해당 장비는 강화가 불가능한 장비입니다!",
                listOf("§7장비를 강화하기 위해서는 강화할 아이템", "§7그리고 강화석이 필요합니다.", "", "§e좌클릭 시 강화 도움말을 확인할 수 있습니다."),
                null)
        ),
        ENHANCE_MATERIAL_IS_CORRUPTED(
            createGuiItem(
                Material.BARRIER,
                1,
                "§c강화석이 아닌 아이템을 제거해주세요!",
                listOf("§7장비를 강화하기 위해 강화석이 아닌 아이템은 사용 불가능합니다!", "", "§e좌클릭 시 강화 도움말을 확인할 수 있습니다."),
                null)
        ),
        MAX_UPGRADE_REACHED(
            createGuiItem(
                Material.BARRIER,
                1,
                "§c더이상 강화할 수 없습니다!",
                listOf("§7최고 강화 단계이기 때문에,", "§7강화가 불가능합니다!", "", "§e좌클릭 시 강화 도움말을 확인할 수 있습니다."),
                null)
        ),
        MAX_VOLUME_REACHED(
            createGuiItem(
                Material.BARRIER,
                1,
                "§c강화석이 포화 상태 입니다!",
                listOf("§7장비에 총 강화석 부피가 초과될 경우,", "§7장비가 파괴될 위험이 있기 때문에, 강화가 불가능합니다!", "", "§e좌클릭 시 강화 도움말을 확인할 수 있습니다."),
                null)
        ),
        ENHANCE_MATERIAL_NOT_AVAILABLE(
            createGuiItem(
                Material.BARRIER,
                1,
                "§c강화 재료를 올려주세요!",
                listOf("§7강화석을 올려주셔야 강화가 가능합니다!", "", "§e좌클릭 시 강화 도움말을 확인할 수 있습니다."),
                null)
        ),
        CANT_ENHANCE_ITEM_BUTTON(
            createGuiItem(
                Material.BARRIER,
                1,
                "§c해당 아이템은 강화가 불가능 합니다!",
                listOf("§7해당 아이템은 강화할 수 없는 아이템 입니다.","§7강화할 수 있는 장비를 올려주세요.", "", "§e좌클릭 시 강화 도움말을 확인할 수 있습니다."),
                null)
        ),
        EMPTY_PREVIEW(
            createGuiItem(
                Material.WHITE_STAINED_GLASS_PANE,
                1,
                "§f미리보기",
                listOf("§7강화 성공 시 해당 아이템으로 강화됩니다."),
                null)
        )
    }
}
