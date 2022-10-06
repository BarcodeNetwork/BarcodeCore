package com.vjh0107.barcode.core.dependencies.skript

import ch.njol.skript.Skript
import ch.njol.skript.lang.util.SimpleEvent
import ch.njol.skript.registrations.EventValues
import ch.njol.skript.util.Getter
import com.nisovin.magicspells.events.SpellTargetEvent
//import com.vjh0107.barcode.core.dependencies.skript.arena.ExprArenaMaxStage
//import com.vjh0107.barcode.core.dependencies.skript.arena.ExprArenaOfPlayer
//import com.vjh0107.barcode.core.dependencies.skript.arena.ExprArenaPlayers
import org.bukkit.entity.Entity
import org.bukkit.entity.Player


object SkriptRegistrarFactory {
    @Deprecated("replace with barcode dungeon module")
    fun registerDungeonsIntegrations() {
//        Classes.registerClass(
//            ClassInfo(Arena::class.java, "arena")
//                .user("arena?s?")
//                .name("Arena")
//                .description("DungeonSystem integration")
//                .since("1.0.0")
//                .parser(object : Parser<Arena>() {
//                    override fun canParse(context: ParseContext?): Boolean {
//                        return false
//                    }
//
//                    override fun toString(o: Arena?, flags: Int): String {
//                        return o.toString()
//                    }
//
//                    override fun toVariableNameString(o: Arena?): String {
//                        return o.toString()
//                    }
//
//                    fun getVariableNamePattern(): String {
//                        return "\\S+"
//                    }
//                })
//        )
//        Skript.getInstance().run {
//            registerExpression(ExprArenaID::class.java, String::class.java, ExpressionType.PROPERTY, "id of %arena%")
//            registerExpression(
//                ExprArenaPlayers::class.java,
//                Player::class.java,
//                ExpressionType.PROPERTY,
//                "players of %arena%"
//            )
//            registerExpression(
//                ExprArenaStage::class.java,
//                Number::class.java,
//                ExpressionType.PROPERTY,
//                "stage of %arena%"
//            )
//            registerExpression(
//                ExprArenaMaxStage::class.java,
//                Number::class.java,
//                ExpressionType.PROPERTY,
//                "max stage of %arena%"
//            )
//            registerExpression(
//                ExprArenaObjectiveCount::class.java,
//                Number::class.java,
//                ExpressionType.PROPERTY,
//                "objective of %arena%"
//            )
//            registerExpression(
//                ExprArenaMaxObjectiveCount::class.java,
//                Number::class.java,
//                ExpressionType.PROPERTY,
//                "max objective of %arena%"
//            )
//            registerExpression(
//                ExprArenaState::class.java,
//                String::class.java,
//                ExpressionType.PROPERTY,
//                "state of %arena%"
//            )
//            registerExpression(
//                ExprArenaTime::class.java,
//                Number::class.java,
//                ExpressionType.PROPERTY,
//                "time of %arena%"
//            )
//            registerExpression(
//                ExprArenaOfPlayer::class.java,
//                Arena::class.java,
//                ExpressionType.PROPERTY,
//                "arena of %player%"
//            )
//        }
//        Skript.registerEvent(
//            "arena next stage",
//            SimpleEvent::class.java,
//            ArenaNextStageEvent::class.java,
//            "arena next stage"
//        )
//            .description("DungeonsSystem by snykkk integration")
//            .examples("on arena next stage: ");
//        EventValues.registerEventValue(
//            ArenaNextStageEvent::class.java,
//            Arena::class.java,
//            object : Getter<Arena, ArenaNextStageEvent>() {
//                override fun get(event: ArenaNextStageEvent): Arena {
//                    return event.arena
//                }
//            },
//            0
//        );
//
//        Skript.registerEvent("arena end", SimpleEvent::class.java, ArenaEndEvent::class.java, "arena end")
//            .description("DungeonsSystem by snykkk integration")
//            .examples("on arena end: ");
//        EventValues.registerEventValue(
//            ArenaEndEvent::class.java,
//            Arena::class.java,
//            object : Getter<Arena, ArenaEndEvent>() {
//                override fun get(event: ArenaEndEvent): Arena {
//                    return event.arena
//                }
//            },
//            0
//        );
//
//        Skript.registerEvent("arena close", SimpleEvent::class.java, ArenaCloseEvent::class.java, "arena close")
//            .description("DungeonsSystem by snykkk integration")
//            .examples("on arena close: ");
//        EventValues.registerEventValue(
//            ArenaCloseEvent::class.java,
//            Arena::class.java,
//            object : Getter<Arena, ArenaCloseEvent>() {
//                override fun get(event: ArenaCloseEvent): Arena {
//                    return event.arena
//                }
//            },
//            0
//        );
//
//        Skript.registerEvent("arena restart", SimpleEvent::class.java, ArenaRestartEvent::class.java, "arena restart")
//            .description("DungeonsSystem by snykkk integration")
//            .examples("on arena restart: ");
//        EventValues.registerEventValue(
//            ArenaRestartEvent::class.java,
//            Arena::class.java,
//            object : Getter<Arena, ArenaRestartEvent>() {
//                override fun get(event: ArenaRestartEvent): Arena {
//                    return event.arena
//                }
//            },
//            0
//        );
//
//        Skript.registerEvent("arena start", SimpleEvent::class.java, ArenaStartEvent::class.java, "arena start")
//            .description("DungeonsSystem by snykkk integration")
//            .examples("on start: ");
//        EventValues.registerEventValue(
//            ArenaStartEvent::class.java,
//            Arena::class.java,
//            object : Getter<Arena, ArenaStartEvent>() {
//                override fun get(event: ArenaStartEvent): Arena {
//                    return event.arena
//                }
//            },
//            0
//        );
//
//        Skript.registerEvent("arena win", SimpleEvent::class.java, ArenaWinEvent::class.java, "arena win")
//            .description("DungeonsSystem by snykkk integration")
//            .examples("on arena win: ");
//        EventValues.registerEventValue(
//            ArenaWinEvent::class.java,
//            Arena::class.java,
//            object : Getter<Arena, ArenaWinEvent>() {
//                override fun get(event: ArenaWinEvent): Arena {
//                    return event.arena
//                }
//            },
//            0
//        );
    }

    fun registerMagicSpellsEvents() {

        Skript.registerEvent(
            "spell target",
            SimpleEvent::class.java,
            SpellTargetEvent::class.java, "spell target"
        )
            .description("매직스펠이 타겟될 때 발동하는 이벤트입니다.")
            .examples("on spell target: ")
        EventValues.registerEventValue(
            SpellTargetEvent::class.java, Player::class.java, object : Getter<Player?, SpellTargetEvent>() {
                override fun get(event: SpellTargetEvent): Player? {
                    return event.caster as? Player ?: throw ClassCastException("caster is not player")
                }
            }, 0
        )
        EventValues.registerEventValue(
            SpellTargetEvent::class.java, String::class.java, object : Getter<String?, SpellTargetEvent>() {
                override fun get(event: SpellTargetEvent): String? {
                    return event.spell.name
                }
            }, 0
        )
        EventValues.registerEventValue(
            SpellTargetEvent::class.java, Entity::class.java, object : Getter<Entity?, SpellTargetEvent>() {
                override fun get(event: SpellTargetEvent): Entity? {
                    return event.target
                }
            }, 0
        )
    }
}