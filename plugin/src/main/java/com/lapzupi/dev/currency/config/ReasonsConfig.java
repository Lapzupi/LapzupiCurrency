package com.lapzupi.dev.currency.config;

import com.lapzupi.dev.config.Transformation;
import com.lapzupi.dev.config.YamlConfigurateFile;
import com.lapzupi.dev.currency.LapzupiCurrency;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author sarhatabaot
 */
public class ReasonsConfig extends YamlConfigurateFile<LapzupiCurrency> {
    private Map<String,String> reasons;
    
    public ReasonsConfig(@NotNull LapzupiCurrency plugin) throws ConfigurateException {
        super(plugin, "", "reasons.yml", "");
    }
    
    @Override
    protected void initValues() throws ConfigurateException {
        this.reasons = new HashMap<>();
        
        for(Map.Entry<Object, CommentedConfigurationNode> entry: rootNode.childrenMap().entrySet()) {
            final String key = entry.getKey().toString();
            final String value = entry.getValue().getString("");
            this.reasons.put(key,value);
        }
    }
    
    public Set<String> getReasonsKeys() {
        return this.reasons.keySet();
    }
    
    public String getReason(final String key) {
        return this.reasons.get(key);
    }
    
    @Override
    protected void builderOptions(TypeSerializerCollection.Builder builder) {
        // Nothing for now
    }
    
    @Override
    protected Transformation getTransformation() {
        return null;
    }
}
