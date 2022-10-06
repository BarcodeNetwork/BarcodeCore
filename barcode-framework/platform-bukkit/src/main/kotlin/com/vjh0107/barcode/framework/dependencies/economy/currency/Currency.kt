package com.vjh0107.barcode.framework.dependencies.economy.currency

import com.vjh0107.barcode.framework.database.player.Identifier

interface Currency {
    val name: String

    fun getBalance(id: Identifier): Double

    fun setBalance(id: Identifier, amount: Double)

    fun addBalance(id: Identifier, amount: Double) {
        setBalance(id, getBalance(id) + amount)
    }

    fun subtractBalance(id: Identifier, amount: Double) {
        setBalance(id, getBalance(id) - amount)
    }
}