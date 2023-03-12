package com.lapzupi.dev.currency.transaction

import java.sql.Timestamp
import java.util.*

/**
 * @author sarhatabaot
 */
class Transaction(
    val friendlyId: String,
    val user: UUID,
    val amount: Double,
    val transactionType: TransactionType,
    val pluginName: String,
    val reason: String,
    val timestamp: Timestamp,
    val hidden: Boolean) {
    override fun toString(): String {
        return "Transaction(friendlyId='$friendlyId', user=$user, amount=$amount, transactionType=$transactionType, pluginName='$pluginName', reason='$reason', timestamp=$timestamp, hidden=$hidden)"
    }
}
    
