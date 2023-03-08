package com.lapzupi.dev.currency.database.connection

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.SQLException
import java.util.concurrent.TimeUnit

/**
 * @author sarhatabaot
 */
abstract class ConnectionFactory {
    private var dataSource: HikariDataSource? = null
    private val logger = LoggerFactory.getLogger(ConnectionFactory::class.java)
    
    /**
     * This may be different with every database type.
     *
     * @param config       hikari config
     * @param address      address
     * @param port         port
     * @param databaseName databaseName
     * @param username     username
     * @param password     password
     */
    protected abstract fun configureDatabase(config: HikariConfig, address: String, port: Int, databaseName: String, username: String, password: String)
    
    fun init(address: String, port: Int, databaseName: String, username: String, password: String) {
        val config = HikariConfig()
        config.poolName = "currency-hikari"
        configureDatabase(config, address, port, databaseName, username, password)
        config.initializationFailTimeout = -1
        val properties: MutableMap<String, String> = HashMap()
        overrideProperties(properties)
        setProperties(config, properties)
        dataSource = HikariDataSource(config)
        logger.info("Connected to database!")
    
        val flyway = Flyway.configure(javaClass.classLoader)
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .baselineVersion("0")
            .table("currency_flyway_schema_history")
            .load()

        try {
            flyway.migrate()
        } catch (e: FlywayException) {
            logger.error("There was a problem migrating to the latest database version. You may experience issues.", e)
        }
    }
    
    //LP
    protected open fun overrideProperties(properties: MutableMap<String, String>) {
        properties.putIfAbsent("socketTimeout", TimeUnit.SECONDS.toMillis(30).toString())
    }
    
    //LP
    protected fun setProperties(config: HikariConfig, properties: Map<String, String>) {
        for ((key, value) in properties) {
            config.addDataSourceProperty(key, value)
        }
    }
    
    fun shutdown() {
        if (dataSource != null) {
            dataSource!!.close()
        }
    }
    
    abstract val type: String?
    
    @get:Throws(SQLException::class)
    val connection: Connection
        get() {
            if (dataSource == null) {
                throw SQLException("Null data source")
            }
            return dataSource!!.connection ?: throw SQLException("Null connection")
        }
}