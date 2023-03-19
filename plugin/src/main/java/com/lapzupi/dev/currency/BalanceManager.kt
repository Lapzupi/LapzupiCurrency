package com.lapzupi.dev.currency

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.lapzupi.dev.currency.database.Database
import com.lapzupi.dev.currency.user.User
import java.time.Duration
import java.util.*

/**
 * @author sarhatabaot
 */
class BalanceManager(private val database: Database) {
    private val balanceCache: LoadingCache<UUID, User> = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(5))
        .build { uuid: UUID -> getUserFromDatabase(uuid) }
    
    private fun getUserFromDatabase(uuid: UUID): User {
        return database.getUser(uuid)
    }
    
    fun addUserToCache(user: User) {
        balanceCache.put(user.getUuid(), user)
    }
    
    fun getCachedUser(uuid: UUID): User? {
        return balanceCache.get(uuid)
    }
    
    fun hasCachedUser(uuid: UUID): Boolean {
        return balanceCache.get(uuid) != null
    }
    
    fun updateCachedUser(uuid: UUID) {
        balanceCache.refresh(uuid)
    }
    
    fun removeCachedUser(uuid: UUID) {
        balanceCache.invalidate(uuid)
    }
}