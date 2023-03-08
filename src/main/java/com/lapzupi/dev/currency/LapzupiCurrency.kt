package com.lapzupi.dev.currency

import co.aikar.commands.PaperCommandManager
import com.lapzupi.dev.currency.command.CurrencyCommand
import com.lapzupi.dev.currency.config.MainConfig
import com.lapzupi.dev.currency.database.Database
import com.lapzupi.dev.currency.listeners.JoinLeaveListener
import com.lapzupi.dev.currency.placeholder.CurrencyPlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LapzupiCurrency : JavaPlugin() {
    private lateinit var balanceManager: BalanceManager
    private lateinit var database: Database
    private lateinit var mainConfig: MainConfig
    
    override fun onEnable() {
        mainConfig = MainConfig(this)
        database = Database(mainConfig)
        balanceManager = BalanceManager(database)
        
        getLogger().info(mainConfig.toString())
        
        CurrencyPlaceholderExpansion(this).register()
        
        Bukkit.getPluginManager().registerEvents(JoinLeaveListener(this), this)
        
        val commandManager = PaperCommandManager(this)
        commandManager.enableUnstableAPI("brigadier")
        commandManager.registerCommand(CurrencyCommand(this))
    }
    
    override fun onDisable() {
        database.shutdown()
    }
    
    fun onReload() {
        mainConfig.reloadConfig()
        database.onReload()
    }
    
    fun getBalanceManager(): BalanceManager {
        return balanceManager
    }
    
    fun getDatabase(): Database {
        return database
    }
    
    fun getMainConfig(): MainConfig {
        return mainConfig
    }
}