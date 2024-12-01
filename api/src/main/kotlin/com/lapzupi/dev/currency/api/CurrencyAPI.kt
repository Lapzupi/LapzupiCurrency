package com.lapzupi.dev.currency.api

import java.util.*

/**
 * Interface for managing player currencies.
 *
 * @author sarhatabaot
 */
interface CurrencyAPI {
    /**
     * Sets the balance of the specified player.
     *
     * @param uuid The unique identifier of the player.
     * @param amount The new balance amount.
     * @param plugin The name of the plugin performing the operation.
     * @param reason The reason for the operation.
     * @param hidden Whether the operation should be hidden from the player.
     */
    fun setBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean)

    /**
     * Gives the specified amount to the player's balance and returns the new balance.
     *
     * @param uuid The unique identifier of the player.
     * @param amount The amount to be given.
     * @param plugin The name of the plugin performing the operation.
     * @param reason The reason for the operation.
     * @param hidden Whether the operation should be hidden from the player.
     * @return The new balance after giving the specified amount.
     */
    fun giveBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean): Double

    /**
     * Takes the specified amount from the player's balance and returns the new balance.
     *
     * @param uuid The unique identifier of the player.
     * @param amount The amount to be taken.
     * @param plugin The name of the plugin performing the operation.
     * @param reason The reason for the operation.
     * @param hidden Whether the operation should be hidden from the player.
     * @return The new balance after taking the specified amount.
     */
    fun takeBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?, hidden: Boolean): Double

    /**
     * Retrieves the current balance of the specified player.
     *
     * @param uuid The unique identifier of the player.
     * @return The current balance of the player, or null if the player does not exist.
     */
    fun getBalance(uuid: UUID?): Double?
}