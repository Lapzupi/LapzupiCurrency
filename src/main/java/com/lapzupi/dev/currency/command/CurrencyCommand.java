package com.lapzupi.dev.currency.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.lapzupi.dev.currency.LapzupiCurrency;
import com.lapzupi.dev.currency.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author sarhatabaot
 */
@CommandAlias("currency")
public class CurrencyCommand extends BaseCommand {
    private static final String NO_PROFILE = "No profile found for user: %s";
    private final LapzupiCurrency plugin;
    
    public CurrencyCommand(LapzupiCurrency plugin) {
        this.plugin = plugin;
    }
    
    @Subcommand("reload")
    @CommandPermission("lapzupi.currency.reload")
    public void onReload(final CommandSender sender) {
        this.plugin.onReload();
    }
    
    @Subcommand("version")
    @CommandPermission("lapzupi.currency.version")
    public void onVersion(final @NotNull CommandSender sender) {
        sender.sendMessage(Component.text("%s v%s".formatted(plugin.getName(), plugin.getDescription().getVersion())));
    }
    
    @Subcommand("balance|bal")
    @CommandPermission("lapzupi.currency.balance")
    public void onBalance(final CommandSender sender, @Optional Player target) {
        if(sender instanceof Player player && target == null) {
            target = player;
        }
    
        final User user = getUserFromCacheOrDatabase(target.getUniqueId());
        if(user == null) {
            sender.sendMessage(Component.text(NO_PROFILE.formatted(target.getName())));
            return;
        }
    
        final double balance = user.getBalance();
        sender.sendMessage(Component.text("%s".formatted(target.getName()))
            .append(Component.text("has %s"
                .formatted(plugin.getMainConfig().getNumber())
                .formatted(balance)))
            .append(Component.text("%s".formatted(plugin.getMainConfig().getCurrencyName())))
        );
    }
    
    @Subcommand("take")
    @CommandPermission("lapzupi.currency.take")
    public void onTake(final CommandSender sender, final @NotNull OfflinePlayer player, final double amount, @Optional @Default("Take command.") String reason) {
        if(userNotExists(player.getUniqueId())) {
            sender.sendMessage(Component.text(NO_PROFILE.formatted(player.getName())));
            return;
        }

        plugin.getDatabase().takeBalance(player.getUniqueId(),amount,LapzupiCurrency.class.getName(),reason);
    }
    
    @Subcommand("give")
    @CommandPermission("lapzupi.currency.give")
    public void onGive(final CommandSender sender, final @NotNull OfflinePlayer player, final double amount, @Optional @Default("Give command.") String reason) {
        if(userNotExists(player.getUniqueId())) {
            sender.sendMessage(Component.text(NO_PROFILE.formatted(player.getName())));
            return;
        }
    
        plugin.getDatabase().takeBalance(player.getUniqueId(),amount,LapzupiCurrency.class.getName(),reason);
    }
    
    @Subcommand("set")
    @CommandPermission("lapzupi.currency.set")
    public void onSet(final CommandSender sender, final @NotNull OfflinePlayer player, final double amount, @Optional @Default("Set command.") String reason) {
        if(userNotExists(player.getUniqueId())) {
            sender.sendMessage(Component.text(NO_PROFILE.formatted(player.getName())));
            return;
        }
    
        plugin.getDatabase().setBalance(player.getUniqueId(),amount,LapzupiCurrency.class.getName(),reason);
    }
    
    // Checks if user doesn't exist in cache or database
    private boolean userNotExists(final UUID uuid) {
        return !plugin.getBalanceManager().hasCachedUser(uuid) && !plugin.getDatabase().hasUser(uuid);
    }
    
    private @Nullable User getUserFromCacheOrDatabase(final UUID uuid) {
        if (plugin.getBalanceManager().hasCachedUser(uuid))
            return plugin.getBalanceManager().getCachedUser(uuid);
        if (plugin.getDatabase().hasUser(uuid)) {
            return plugin.getDatabase().getUser(uuid);
        }
        return null;
    }
}
