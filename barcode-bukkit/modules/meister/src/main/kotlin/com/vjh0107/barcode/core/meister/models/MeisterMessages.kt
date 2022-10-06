package com.vjh0107.barcode.core.meister.models

import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import org.bukkit.Sound
import org.bukkit.entity.Player

object MeisterMessages {
    enum class Fail(val message: String) {
        NOT_YET_LEARNED("&c아직 배울 수 없는 스킬입니다."),
        MAX_LEVEL_REACHED("&c최고 레벨의 스킬입니다."),

        NO_SKILL_POINTS_SPENT("&c초기화할 전문기술 스킬이 없습니다."),


        SKILL_POINT_REQUIRED("&c스킬 포인트가 부족합니다."),
        SKILL_RESET_POINT_REQUIRE("&c전문기술 스킬 초기화 포인트가 부족합니다.");

        fun send(player: Player) {
            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 2f)
            player.sendBNMessage(this.message, usePrefix = false)
        }
    }
    enum class Success(val message: String) {
        SKILLS_REALLOCATED("&e성공적으로 전문기술 스킬을 초기화하였습니다."),
        SKILL_LEVEL_UP("&6{skill}&e의 레벨이 &6{level}&e로 올랐습니다!");

        fun send(player: Player, skill: MeisterSkills? = null, level: Int = 0) {
            val message = skill?.let {
                message
                    .replace("{skill}", skill.displayName)
                    .replace("{level}", level.toString())
            } ?: message
            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f)
            player.sendBNMessage(message, usePrefix = false)
        }
    }
}