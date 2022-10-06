package com.vjh0107.barcode.core.meister.models

import com.vjh0107.barcode.core.database.player.CorePlayerPlayerData
import com.vjh0107.barcode.core.meister.data.MeisterSkillParameter
import com.vjh0107.barcode.framework.utils.formatters.colorize
import com.vjh0107.barcode.framework.utils.item.createGuiItem
import com.vjh0107.barcode.framework.utils.formatters.toPreciseBarcodeFormat
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class MeisterSkills(
    val index: Int,
    val displayName: String,
    val requireLevel: Int,
    val maxLevel: Int,
    val parameter: MeisterSkillParameter,
    vararg val lore: String,
    val loreType: LoreTypes = LoreTypes.PERCENT
) {
    MANA_FOUNTAIN(0,
        "마나샘", 0, 50, MeisterSkillParameter.of(5.0, 0.9),
        "&7채집/채광 완료 시 &e{parameter}% &7확률로",
        "&b레테르 파편&7을 획득한다."
    ),
    CRUSHER(1,
        "분쇄기", 20, 50, MeisterSkillParameter.of(1.0, 0.08),
        "&7채집/채광 중 &e{parameter}% &7확률로 &e5초&7간",
        "&b공격속도&7가 &e2배 &7증가한다."
    ),
    DEMATERIALIZE(2,
        "해체 분석", 40, 50, MeisterSkillParameter.of(3.0, 0.14),
        "&7채집/채광 중 &e{parameter}% &7확률로",
        "&e2배&7의 피해를 입힌다."
    ),
    LUCK(3,
        "행운", 60, 50, MeisterSkillParameter.of(1.0, 0.12),
        "&7채집/채광 완료시 &e{parameter}% &7확률로",
        "&b아이템&7을 추가로 한번 더 지급한다."
    ),
    DESTROYER(4,
        "파괴자", 100, 50, MeisterSkillParameter.of(0.5, 0.03),
        "&7채집/채광 시 &e{parameter}% &7확률로",
        "&b즉시 &7채집/채광을 완료한다."
    ),
    HAND_OF_BLESSING(5,
        "축복의 손길", 150, 50, MeisterSkillParameter.of(10.0, 0.2),
        "&7강화 시도시 파괴 확률이",
        "&7영구적으로 &e{parameter}% &9감소&7한다."
    ),
    ASPIRE_OF_MEISTER(6,
        "장인의 열망", 250, 50, MeisterSkillParameter.of(10.0, 0.2),
        "&7강화 시도시 최대 용량이",
        "&7영구적으로 &e{parameter}% &c증가&7한다.",
        loreType = LoreTypes.LINEAR
    ),
    MASTER(7,
        "명장", 400, 400, MeisterSkillParameter.of(0.0, 0.05),
        "&7강화 성공시 &e{parameter}% &7확률로",
        "&7추가로 강화에 성공한다."
    );

    val id: String get() = this.name.lowercase()

    companion object {
        val map = mutableMapOf<Int, MeisterSkills>().apply apply@{
            MeisterSkills.values().forEach {
                this@apply.put(it.index, it)
            }
        }
    }

    enum class LoreTypes(val lore: String) {
        PERCENT("&8스킬 레벨 당 확률이 &e{increase}% &8씩 증가합니다."),
        LINEAR("&8스킬 레벨 당 증가값이 &e{increase} &8씩 증가합니다.");
    }

    /**
     * @return 사용 가능한 스킬이면 true, 불가능하면 false
     */
    fun validatePlayer(player: Player): Boolean {
        return MeisterProfessions.getMeisterLevel(player) >= this@MeisterSkills.requireLevel
    }

    fun getItem(data: CorePlayerPlayerData): ItemStack {
        val material: Material = if (validatePlayer(data.player)) {
            Material.ENCHANTED_BOOK
        } else {
            Material.BOOK
        }
        return createGuiItem(material, 1, getName(data), getLore(data), 0)
    }

    private fun getName(data: CorePlayerPlayerData): String {
        return parsePlaceholder("&f${this.displayName} &6[{level}/{max_level}]", data)
    }

    private fun getLore(player: Player): List<String> {

        val lore = arrayListOf<String>().apply {
            if (!validatePlayer(player)) {
                add("&c[✖] 요구 레벨 {require_level}")
            }
            add("&7패시브 스킬")
            add("")
            this@MeisterSkills.lore.forEach {
                add(it)
            }
            add("")
            add(this@MeisterSkills.loreType.lore)
            add("")
            add("&e클릭 시 해당 스킬에 포인트를 투자합니다.")
            add("&e◆ 보유중인 스킬 포인트 : {points}")
        }.map { parsePlaceholder(it, data) }
        return lore
    }

    private fun parsePlaceholder(lore: String, data: CorePlayerPlayerData): String {
        val skillLevel = data.savableDataObject.meisterSkill.getSkillLevel(this)
        val parameterResult = getParameterResult(data)

        return lore
            .replace("{increase}", this.parameter.increase.toPreciseBarcodeFormat())
            .replace("{parameter}", parameterResult.toPreciseBarcodeFormat())
            .replace("{points}", data.savableDataObject.meisterSkill.skillPoints.toString())
            .replace("{level}", skillLevel.toString())
            .replace("{max_level}", this.maxLevel.toString())
            .replace("{require_level}", this.requireLevel.toString())
            .colorize()
    }

    fun getParameterResult(data: CorePlayerPlayerData) : Double {
        return this.parameter.base + (this.parameter.increase * data.savableDataObject.meisterSkill.getSkillLevel(this))
    }
}