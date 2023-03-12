package com.lapzupi.dev.currency.api

import java.util.*

/**
 * @author sarhatabaot
 */
interface CurrencyAPI {
    fun setBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean)
    fun giveBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean): Double
    fun takeBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean): Double
    fun getBalance(uuid: UUID?): Double?
}