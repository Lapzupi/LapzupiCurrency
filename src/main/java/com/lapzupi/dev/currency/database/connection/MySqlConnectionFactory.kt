package com.lapzupi.dev.currency.database.connection

import com.zaxxer.hikari.HikariConfig

/**
 * @author sarhatabaot
 */
class MySqlConnectionFactory : ConnectionFactory() {
    override fun configureDatabase(config: HikariConfig, address: String, port: Int, databaseName: String, username: String, password: String) {
        config.jdbcUrl = "jdbc:mysql://$address:$port/$databaseName"
        config.username = username
        config.password = password
    }
    
    override fun overrideProperties(properties: MutableMap<String, String>) {
        properties.putIfAbsent("cachePrepStmts", "true")
        properties.putIfAbsent("prepStmtCacheSize", "250")
        properties.putIfAbsent("prepStmtCacheSqlLimit", "2048")
        properties.putIfAbsent("useServerPrepStmts", "true")
        properties.putIfAbsent("useLocalSessionState", "true")
        properties.putIfAbsent("rewriteBatchedStatements", "true")
        properties.putIfAbsent("cacheResultSetMetadata", "true")
        properties.putIfAbsent("cacheServerConfiguration", "true")
        properties.putIfAbsent("elideSetAutoCommits", "true")
        properties.putIfAbsent("maintainTimeStats", "false")
        properties.putIfAbsent("alwaysSendSetIsolation", "false")
        properties.putIfAbsent("cacheCallableStmts", "true")
        
        // https://stackoverflow.com/a/54256150
        properties.putIfAbsent("serverTimezone", "UTC")
        super.overrideProperties(properties)
    }
    
    override val type: String
        get() = "MYSQL"
}