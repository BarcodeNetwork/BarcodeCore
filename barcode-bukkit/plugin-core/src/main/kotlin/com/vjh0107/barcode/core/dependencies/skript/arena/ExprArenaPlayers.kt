//package com.vjh0107.barcode.core.dependencies.skript.arena
//
//import ch.njol.skript.lang.Expression
//import ch.njol.skript.lang.SkriptParser
//import ch.njol.skript.lang.util.SimpleExpression
//import ch.njol.util.Kleenean
//import me.snykkk.dungeonsystem.arena.Arena
//import org.bukkit.entity.Player
//import org.bukkit.event.Event
//
//
//class ExprArenaPlayers : SimpleExpression<Player>() {
//    lateinit var arena: Expression<Arena>
//    override fun toString(e: Event?, debug: Boolean): String {
//        return "players of %arena%"
//    }
//
//    override fun init(exprs: Array<out Expression<*>>?, matchedPattern: Int, isDelayed: Kleenean?, parseResult: SkriptParser.ParseResult?): Boolean {
//        arena = exprs!![0] as Expression<Arena>
//        return true
//    }
//
//    override fun isSingle(): Boolean {
//        return false
//    }
//
//    override fun getReturnType(): Class<out Player> {
//        return Player::class.java
//    }
//
//    override fun get(e: Event?): Array<Player?> {
//        val arena = arena.getSingle(e) ?: throw NullPointerException("invalid arena")
//        return arena.players.toTypedArray()
//    }
//}