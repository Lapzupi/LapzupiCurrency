package com.lapzupi.dev.currency.api

import java.util.*

/**
 * @author sarhatabaot
 */
interface CurrencyAPI {
    fun setBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?)
    fun giveBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?)
    fun takeBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?)
    fun getBalance(uuid: UUID?): Double?
}