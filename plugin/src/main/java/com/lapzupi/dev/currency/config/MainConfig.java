package com.lapzupi.dev.currency.config;

import com.lapzupi.dev.config.Transformation;
import com.lapzupi.dev.config.YamlConfigurateFile;
import com.lapzupi.dev.currency.LapzupiCurrency;
import com.lapzupi.dev.currency.transaction.TransactionType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.lang.reflect.Type;

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
    
    private TransactionTypeConfig give;
    private TransactionTypeConfig take;
    private TransactionTypeConfig set;
    
    
    public MainConfig(@NotNull LapzupiCurrency plugin) throws ConfigurateException {
        super(plugin, "", "config.yml", "");
    }
    
    @Override
    protected void builderOptions(TypeSerializerCollection.@NotNull Builder builder) {
        builder.register(TransactionTypeConfig.class, new TypeSerializer<>() {
            @Override
            public TransactionTypeConfig deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
                final String symbol = node.node("symbol").getString();
                final String color = node.node("color").getString();
                return new TransactionTypeConfig(symbol, color);
            }
    
            @Override
            public void serialize(@NotNull Type type, @Nullable TransactionTypeConfig obj, @NotNull ConfigurationNode node) throws SerializationException {
                //nothing for now
            }
        });
    }
    
    public TransactionTypeConfig getTransactionTypeConfig(final @NotNull TransactionType type) {
        switch (type) {
            case SET -> {
                return set;
            }
            case GIVE -> {
                return give;
            }
            default -> {
                return take;
            }
        }
    }
    

    
    @Override
    protected Transformation getTransformation() {
        return null;
    }
    
    public void initValues() throws ConfigurateException {
        this.rootNode = loader.load();
    
        final CommentedConfigurationNode currencyNode = rootNode.node("currency");
        this.currencyName = currencyNode.node("name").getString("zupis");
        this.currencyNumber = currencyNode.node("number").getString("%.2f");
        this.currencyFormatting =  currencyNode.node("formatting").getString("%currency_number% %currency_name%");
    
        final CommentedConfigurationNode databaseNode = rootNode.node("database");
        this.databaseName = databaseNode.node("name").getString("minecraft");
        this.databaseAddress = databaseNode.node("address").getString("127.0.0.1");
        this.databasePort = databaseNode.node("port").getInt(3306);
        this.databaseUsername = databaseNode.node("username").getString("");
        this.databasePassword = databaseNode.node("password").getString("");
    
        final CommentedConfigurationNode historyNode =rootNode.node("history");
        this.give = historyNode.node("give").get(TransactionTypeConfig.class);
        this.set = historyNode.node("set").get(TransactionTypeConfig.class);
        this.take = historyNode.node("take").get(TransactionTypeConfig.class);
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
    
    public TransactionTypeConfig getGive() {
        return give;
    }
    
    public TransactionTypeConfig getTake() {
        return take;
    }
    
    public TransactionTypeConfig getSet() {
        return set;
    }
    
    public int getConfigVersion() {
        return rootNode.node("version").getInt();
    }
    
    public record TransactionTypeConfig(String symbol, String color){}
    
}
