package com.lapzupi.dev.currency.placeholder

import com.lapzupi.dev.currency.LapzupiCurrency
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import java.util.*

/**
 * @author sarhatabaot
 */
class CurrencyPlaceholderExpansion(private val plugin: LapzupiCurrency) : PlaceholderExpansion() {
    override fun onRequest(player: OfflinePlayer, params: String): String? {
        if (params.startsWith("balance_")) {
            val uuid = params.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            return plugin.getBalanceManager().getCachedUser(UUID.fromString(uuid))?.getBalance().toString()
        }
        if (params.equals("balance", ignoreCase = true)) {
            return plugin.getBalanceManager().getCachedUser(player.uniqueId)?.getBalance().toString()
        }
        
        if (params.equals("number", ignoreCase = true)) {
            return plugin.getMainConfig().number
        }
        
        if(params.equals("name", ignoreCase = true)) {
            return plugin.getMainConfig().currencyName
        }
        
        if(params.equals("formatting", ignoreCase = true)) {
            return PlaceholderAPI.setPlaceholders(player, plugin.getMainConfig().formatting)
        }
        
        return null
    }
    
    override fun getIdentifier(): String {
        return "currency"
    }
    
    override fun getAuthor(): String {
        return "Lapzupi Development Team"
    }
    
    override fun getVersion(): String {
        return "1.0.0"
    }
}