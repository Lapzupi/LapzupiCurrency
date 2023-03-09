package com.lapzupi.dev.currency.config;

import com.lapzupi.dev.config.Transformation;
import com.lapzupi.dev.config.YamlConfigurateFile;
import com.lapzupi.dev.currency.LapzupiCurrency;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;

/**
 * @author sarhatabaot
 */
public class MainConfig extends YamlConfigurateFile<LapzupiCurrency> {
    private String currencyName;
    private String currencyNumber;
    private String currencyFormatting;
    
    private String databaseAddress;
    private String databaseName;
    private int databasePort;
    private String databaseUsername;
    private String databasePassword;
    
    
    public MainConfig(@NotNull LapzupiCurrency plugin) throws ConfigurateException {
        super(plugin, "", "config.yml", "");
    }
    
    @Override
    protected void builderOptions() {
        // nothing yet
    }
    
    @Override
    protected Transformation getTransformation() {
        return null;
    }
    
    public void initValues() throws ConfigurateException {
        this.rootNode = loader.load();
    
        CommentedConfigurationNode currencyNode = rootNode.node("currency");
        this.currencyName = currencyNode.node("name").getString("zupis");
        this.currencyNumber = currencyNode.node("number").getString("%.2f");
        this.currencyFormatting =  currencyNode.node("formatting").getString("%currency_number% %currency_name%");
        
        CommentedConfigurationNode databaseNode = rootNode.node("database");
        this.databaseName = databaseNode.node("name").getString("minecraft");
        this.databaseAddress = databaseNode.node("address").getString("127.0.0.1");
        this.databasePort = databaseNode.node("port").getInt(3306);
        this.databaseUsername = databaseNode.node("username").getString("");
        this.databasePassword = databaseNode.node("password").getString("");
    }
    
    public String getCurrencyName() {
        return currencyName;
    }
    
    public String getNumber() {
        return currencyNumber;
    }
    
    public String getFormatting() {
        return currencyFormatting;
    }
    
    public String getAddress() {
        return databaseAddress;
    }
    
    public String getPassword() {
        return databasePassword;
    }
    
    public String getUsername() {
        return databaseUsername;
    }
    
    public int getPort() {
        return databasePort;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    public int getConfigVersion() {
        return rootNode.node("version").getInt();
    }
}
