package com.vjh0107.barcode.framework.dependencies.economy

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

@BarcodeComponent
class EconomyProvider(private val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "EconomyProvider"

    @Registrar(depend = "Vault")
    fun initialize() {
        plugin.logger.info("Vault 를 발견하였습니다.")
        ECONOMY = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)?.provider
            ?: throw NullPointerException("Vault 를 가져올 수 없습니다.")
    }

    companion object {
        lateinit var ECONOMY: Economy

        val OfflinePlayer.money
            get() = ECONOMY.getBalance(this)

        fun OfflinePlayer.giveMoney(value: Double): EconomyResponse {
            return ECONOMY.depositPlayer(this, value)
        }

        fun OfflinePlayer.takeMoney(value: Double): EconomyResponse {
            return ECONOMY.withdrawPlayer(this, value)
        }
    }
}
