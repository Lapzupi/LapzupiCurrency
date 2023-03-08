package com.lapzupi.dev.currency.database

import com.devskiller.friendly_id.FriendlyId
import com.lapzupi.dev.currency.LapzupiCurrency
import com.lapzupi.dev.currency.api.CurrencyAPI
import com.lapzupi.dev.currency.config.MainConfig
import com.lapzupi.dev.currency.database.connection.ConnectionFactory
import com.lapzupi.dev.currency.database.connection.MySqlConnectionFactory
import com.lapzupi.dev.currency.transaction.TransactionType
import com.lapzupi.dev.currency.user.User
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.SQLException
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

/**
 * @author sarhatabaot
 */
class Database(mainConfig: MainConfig) : CurrencyAPI {
    private val connectionFactory: ConnectionFactory
    private val mainConfig: MainConfig
    private val logger = LoggerFactory.getLogger(Database::class.java)
    
    init {
        connectionFactory = MySqlConnectionFactory()
        this.mainConfig = mainConfig
        onReload()
    }
    
    fun onReload() {
        connectionFactory.init(mainConfig.address, mainConfig.port, mainConfig.databaseName, mainConfig.username, mainConfig.password)
    }
    
    private fun setBalance(uuid: UUID?, amount: Double) {
        val updateUserBalanceSql = "UPDATE `currency_users` SET balance = ? WHERE uuid = ?;"
        executeStatement { connection: Connection ->
            try {
                connection.prepareStatement(updateUserBalanceSql).use { statement ->
                    statement.setDouble(1, amount)
                    statement.setString(2, uuid.toString())
                    statement.executeUpdate()
                }
            } catch (e: SQLException) {
                logStatementError(e)
            }
        }
    }
    
    private fun takeBalance(uuid: UUID?, amount: Double) {
        val balance = nullOrZero(getBalance(uuid))
        val newBalance = getAmountToRemove(balance, amount)
        setBalance(uuid, newBalance)
    }
    
    private fun giveBalance(uuid: UUID?, amount: Double) {
        val amountToGive = sanitizeBalance(amount)
        val balance = nullOrZero(getBalance(uuid))
        setBalance(uuid, balance + amountToGive)
    }
    
    private fun getAmountToRemove(oldBalance: Double, amount: Double): Double {
        val amountToRemove = sanitizeBalance(amount)
        return if (oldBalance < amountToRemove) {
            0.0
        } else oldBalance - amountToRemove
    }
    
    private fun nullOrZero(amount: Double?): Double {
        return amount ?: 0.0
    }
    
    private fun sanitizeBalance(amount: Double): Double {
        return if (amount < 0) amount * -1 else amount
    }
    
    fun hasUser(uuid: UUID): Boolean {
        return java.lang.Boolean.TRUE == getStatement { connection: Connection ->
            try {
                connection.prepareStatement("SELECT * FROM `currency_users` WHERE uuid = ?").use { statement ->
                    statement.setString(1, uuid.toString())
                    return@getStatement statement.executeQuery().next()
                }
            } catch (e: SQLException) {
                logger.error("Could not execute query, returning false.", e)
                return@getStatement false
            }
        }
    }
    
    override fun getBalance(uuid: UUID?): Double? {
        val getUserBalance = "SELECT balance FROM `currency_users` WHERE uuid = ?;"
        return getStatement { connection: Connection ->
            try {
                connection.prepareStatement(getUserBalance).use { statement ->
                    statement.setString(1, uuid.toString())
                    val set = statement.executeQuery()
                    if (set.next()) {
                        return@getStatement set.getDouble("balance")
                    }
                    return@getStatement null
                }
            } catch (e: SQLException) {
                logStatementError(e)
                return@getStatement null
            }
        }
    }
    
    fun getUser(uuid: UUID): User {
        val getUserSql = "SELECT * FROM `currency_users` WHERE uuid = ?;"
        return getStatement { connection: Connection ->
            try {
                connection.prepareStatement(getUserSql).use { statement ->
                    statement.setString(1, uuid.toString())
                    val set = statement.executeQuery()
                    if (set.next()) {
                        return@getStatement User(uuid, set.getString("username"), set.getDouble("balance"))
                    }
                    return@getStatement null
                }
            } catch (e: SQLException) {
                logStatementError(e)
                return@getStatement null
            }
        }!!
    }
    
    private fun <R> getStatement(func: Function<Connection, R>): R? {
        try {
            connectionFactory.connection.use { connection -> return func.apply(connection) }
        } catch (e: SQLException) {
            logStatementError(e)
            return null
        }
    }
    
    /**
     * This should be used when executing a statement.
     * The connection is closed automatically.
     *
     * @param consumer Function to execute.
     */
    private fun executeStatement(consumer: Consumer<Connection>) {
        try {
            connectionFactory.connection.use { connection -> consumer.accept(connection) }
        } catch (e: SQLException) {
            logStatementError(e)
        }
    }
    
    private fun logTransaction(uuid: UUID?, amount: Double, type: TransactionType, plugin: String?, reason: String?) {
        val transactionId = FriendlyId.createFriendlyId()
        val timestamp = Timestamp.from(Instant.now())
        executeStatement { connection: Connection ->
            try {
                connection.prepareStatement("INSERT INTO `currency_transactions` " +
                    "(friendly_id, user_uuid, `timestamp`, amount, plugin, `type`, reason) " +
                    "VALUES (?,?,?,?,?,?,?);").use { statement ->
                    statement.setString(1, transactionId)
                    statement.setString(2, uuid.toString())
                    statement.setTimestamp(3, timestamp)
                    statement.setDouble(4, amount)
                    statement.setString(5, plugin)
                    statement.setString(6, type.name)
                    statement.setString(7, reason)
                    statement.executeUpdate()
                }
            } catch (e: SQLException) {
                logStatementError(e)
            }
        }
    }
    
    fun shutdown() {
        connectionFactory.shutdown()
    }
    
    override fun setBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?) {
        setBalance(uuid, amount)
        logTransaction(uuid, amount, TransactionType.SET, plugin, reason)
    }
    
    override fun giveBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?) {
        giveBalance(uuid, amount)
        logTransaction(uuid, amount, TransactionType.GIVE, plugin, reason)
    }
    
    override fun takeBalance(uuid: UUID?, amount: Double, plugin: String?, reason: String?) {
        takeBalance(uuid, amount)
        logTransaction(uuid, amount, TransactionType.TAKE, plugin, reason)
    }
    
    private fun logStatementError(e: SQLException) {
        logger.error("Could not execute statement", e)
    }
    
    fun createNewUser(uuid: UUID?, username: String?): User {
        logTransaction(uuid, 0.0, TransactionType.SET, LapzupiCurrency::class.java.name, "New user created.")
        return User(uuid!!, username!!, 0.0)
    }
}