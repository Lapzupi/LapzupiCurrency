package com.lapzupi.dev.currency

import co.aikar.commands.PaperCommandManager
import com.lapzupi.dev.currency.api.CurrencyAPI
import com.lapzupi.dev.currency.command.CurrencyCommand
import com.lapzupi.dev.currency.config.MainConfig
import com.lapzupi.dev.currency.config.ReasonsConfig
import com.lapzupi.dev.currency.database.Database
import com.lapzupi.dev.currency.listeners.JoinLeaveListener
import com.lapzupi.dev.currency.placeholder.CurrencyPlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class LapzupiCurrency : JavaPlugin() {
    private lateinit var balanceManager: BalanceManager
    private lateinit var database: Database
    private lateinit var currencyImpl: CurrencyImpl
    private lateinit var mainConfig: MainConfig
    private lateinit var reasonsConfig: ReasonsConfig
    
    override fun onEnable() {
        mainConfig = MainConfig(this)
        reasonsConfig = ReasonsConfig(this)
        
        database = Database(mainConfig)
        balanceManager = BalanceManager(database)
        currencyImpl = CurrencyImpl(database, balanceManager)
        
        server.servicesManager.register(CurrencyAPI::class.java, this.currencyImpl, this, ServicePriority.Normal)
        
        
        logger.info(mainConfig.toString())
        
        CurrencyPlaceholderExpansion(this).register()
        
        Bukkit.getPluginManager().registerEvents(JoinLeaveListener(this), this)
        
        val commandManager = PaperCommandManager(this)
        commandManager.enableUnstableAPI("brigadier")
        commandManager.commandCompletions.registerCompletion("reasons") { reasonsConfig.reasonsKeys }
        commandManager.registerCommand(CurrencyCommand(this))
    }
    
    override fun onDisable() {
        database.shutdown()
    }
    
    fun onReload() {
        mainConfig.reloadConfig()
        reasonsConfig.reloadConfig()
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
    
    fun getCurrencyAPI(): CurrencyAPI {
        return currencyImpl
    }
    
    fun getReasonsConfig(): ReasonsConfig {
        return reasonsConfig
    }
}