package com.lapzupi.dev.currency.user

import java.util.*

/**
 * @author sarhatabaot
 */
class User {
    
    private val uuid: UUID
    private val username: String
    
    private var balance: Double
    
    constructor(uuid: UUID, username: String) {
        this.uuid = uuid
        this.username = username
        balance = 0.0
    }
    
    constructor(uuid: UUID, username: String, balance: Double) {
        this.uuid = uuid
        this.username = username
        this.balance = balance
    }
    
    fun addBalance(balance: Double) {
        this.balance += balance
    }
    
    fun removeBalance(balance: Double) {
        this.balance -= balance
    }
    
    fun setBalance(balance: Double) {
        this.balance = balance
    }
    
    fun hasBalance(balance: Double): Boolean {
        return this.balance >= balance
    }
    
    fun getBalance(): Double {
        return this.balance
    }
    
    fun getUuid(): UUID {
        return this.uuid
    }
}