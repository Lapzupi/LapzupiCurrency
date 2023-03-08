package com.lapzupi.dev.currency.listeners

import com.lapzupi.dev.currency.BalanceManager
import com.lapzupi.dev.currency.LapzupiCurrency
import com.lapzupi.dev.currency.database.Database
import com.lapzupi.dev.currency.user.User
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

/**
 * @author sarhatabaot
 */
class JoinLeaveListener(plugin: LapzupiCurrency) : Listener {
    private val balanceManager: BalanceManager
    private val database: Database
    
    init {
        balanceManager = plugin.getBalanceManager()
        database = plugin.getDatabase()
    }
    
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val user = getUserFromDatabaseIfNotExistsCreate(event.player.uniqueId, event.player.name)
        balanceManager.addUserToCache(user)
        //Check if the user exists in the database.
        //If not, create it, and add that user to the cache.
    }
    
    private fun getUserFromDatabaseIfNotExistsCreate(uuid: UUID, name: String): User {
        return if (!database.hasUser(uuid)) {
            database.createNewUser(uuid, name)
        } else database.getUser(uuid)
    }
    
    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        balanceManager.removeCachedUser(event.player.uniqueId)
    }
}