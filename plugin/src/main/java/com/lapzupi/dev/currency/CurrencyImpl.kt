package com.lapzupi.dev.currency

import com.lapzupi.dev.currency.api.CurrencyAPI
import com.lapzupi.dev.currency.database.Database
import java.util.*

/**
 *
 * @author sarhatabaot
 */
class CurrencyImpl(private val database: Database, private val balanceManager: BalanceManager): CurrencyAPI {

    override fun setBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean) {
        database.setBalance(uuid,amount, plugin, reason, hidden)
        balanceManager.updateCachedUser(uuid, amount)
    }
    
    override fun giveBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean): Double {
        val newBalance = database.giveBalance(uuid, amount, plugin, reason, hidden)
        balanceManager.updateCachedUser(uuid, newBalance)
        return newBalance
    }
    
    override fun takeBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean): Double {
        val newBalance = database.takeBalance(uuid, amount, plugin, reason, hidden)
        balanceManager.updateCachedUser(uuid, newBalance)
        return newBalance
    }
    
    override fun getBalance(uuid: UUID?): Double? {
        if(uuid != null && balanceManager.hasCachedUser(uuid)) {
            return balanceManager.getCachedUser(uuid)?.getBalance()
        }
        return database.getBalance(uuid)
    }
    
}