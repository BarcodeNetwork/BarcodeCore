//package com.vjh0107.barcode.core.dependencies.skript
//
//import ch.njol.skript.lang.Expression
//import ch.njol.skript.lang.SkriptParser
//import ch.njol.skript.lang.util.SimpleExpression
//import ch.njol.util.Kleenean
//import me.snykkk.dungeonsystem.arena.Arena
//import org.bukkit.event.Event
//
//class ExprArenaTime : SimpleExpression<Number>() {
//    lateinit var arena: Expression<Arena>
//    override fun toString(e: Event?, debug: Boolean): String {
//        return "time of %arena%"
//    }
//
//    @SuppressWarnings("unchecked")
//    override fun init(exprs: Array<out Expression<*>>?, matchedPattern: Int, isDelayed: Kleenean?, parseResult: SkriptParser.ParseResult?): Boolean {
//        arena = exprs!![0] as Expression<Arena>
//        return true
//    }
//
//    override fun isSingle(): Boolean {
//        return true
//    }
//
//    override fun getReturnType(): Class<out Number> {
//        return Number::class.java
//    }
//
//    override fun get(e: Event?): Array<Number> {
//        val arena = arena.getSingle(e) ?: throw NullPointerException("invalid arena")
//        return arrayOf(arena.getTime("time_expire"))
//    }
//
//}