package com.lapzupi.dev.currency.transaction

import org.jetbrains.annotations.Contract
import java.sql.Timestamp
import java.util.*

/**
 * @author sarhatabaot
 */
class Transaction(
    private val friendlyId: String,
    private val user: UUID,
    private val amount: Double,
    private val transactionType: TransactionType,
    private val pluginName: String,
    private val reason: String,
    private val timestamp: Timestamp) {
    companion object {
        @Contract(value = "_, _, _, _, _, _ -> new", pure = true)
        fun createTransaction(friendlyId: String, user: UUID, amount: Double, transactionType: TransactionType, pluginName: String, reason: String, timestamp: Timestamp): Transaction {
            return Transaction(friendlyId, user, amount, transactionType, pluginName, reason, timestamp)
        }
    }
}