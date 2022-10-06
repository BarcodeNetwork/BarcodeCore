//package com.vjh0107.barcode.core.dependencies.skript.arena
//
//import ch.njol.skript.lang.Expression
//import ch.njol.skript.lang.SkriptParser
//import ch.njol.skript.lang.util.SimpleExpression
//import ch.njol.util.Kleenean
//import me.snykkk.dungeonsystem.arena.Arena
//import me.snykkk.dungeonsystem.arena.ArenaManager
//import org.bukkit.entity.Player
//import org.bukkit.event.Event
//
//
//class ExprArenaOfPlayer : SimpleExpression<Arena>() {
//    lateinit var player: Expression<Player>
//    override fun toString(e: Event?, debug: Boolean): String {
//        return "arena of %player%"
//    }
//
//    override fun init(exprs: Array<out Expression<*>>?, matchedPattern: Int, isDelayed: Kleenean?, parseResult: SkriptParser.ParseResult?): Boolean {
//        player = exprs!![0] as Expression<Player>
//        return true
//    }
//
//    override fun isSingle(): Boolean {
//        return true
//    }
//
//    override fun getReturnType(): Class<out Arena> {
//        return Arena::class.java
//    }
//
//    override fun get(e: Event?): Array<Arena?> {
//        val arena: Arena? = ArenaManager.getArenaManager().getArena(player.getSingle(e))
//        return arrayOf(arena)
//    }
//}