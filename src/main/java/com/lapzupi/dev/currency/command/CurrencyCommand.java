package com.lapzupi.dev.currency.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.lapzupi.dev.currency.LapzupiCurrency;
import com.lapzupi.dev.currency.config.MainConfig;
import com.lapzupi.dev.currency.transaction.Transaction;
import com.lapzupi.dev.currency.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        sender.sendMessage(Component.text("Reloaded %s v%s".formatted(this.plugin.getName(), this.plugin.getDescription().getVersion())));
    }
    
    @Subcommand("version")
    @CommandPermission("lapzupi.currency.version")
    public void onVersion(final @NotNull CommandSender sender) {
        sender.sendMessage(Component.text("%s v%s".formatted(plugin.getName(), plugin.getDescription().getVersion())));
    }
    
    @Subcommand("reasons")
    @CommandPermission("lapzupi.currency.reasons")
    @Description("List all reason ids and their content.")
    public void onReason(final CommandSender sender) {
        Component reason = Component.text("");
        for(final String key: plugin.getReasonsConfig().getReasonsKeys()) {
            reason = reason.append(Component.text(key))
                .append(Component.text(" - "))
                .append(Component.text(plugin.getReasonsConfig().getReason(key))
                    .appendNewline());
        }
        sender.sendMessage(reason);
    }
    
    @Subcommand("history")
    @CommandPermission("lapzupi.currency.history")
    @Description("View the transaction history.")
    public void onHistory(final CommandSender sender, @Optional @Default(value = "1") Integer page, @Optional OfflinePlayer target) {
        if(sender instanceof ConsoleCommandSender) {
            if(target == null) {
                sender.sendMessage(Component.text("You must specify a player when sending from console."));
                return;
            }
            sender.sendMessage(Component.text("Displaying transaction history for %s page %d".formatted(target.getName(), page)));
            sender.sendMessage(getTransactionComponentList(sender, Objects.requireNonNull(plugin.getDatabase().getTransactions(target.getUniqueId(), page))));
        }
        
        if(target != null) {
            if(!sender.hasPermission("lapzupi.currency.history.others")) {
                sender.sendMessage(Component.text("You do not have permission to view the transaction history of other players."));
                return;
            }
            
            sender.sendMessage(Component.text("Displaying transaction history for %s page %d".formatted(target.getName(), page)));
            sender.sendMessage(getTransactionComponentList(sender,Objects.requireNonNull(plugin.getDatabase().getTransactions(target.getUniqueId(), page))));
            return;
        }
        
        final Player senderPlayer = (Player) sender;
        sender.sendMessage(Component.text("Displaying transaction history for %s page %d".formatted(senderPlayer.getName(), page)));
        sender.sendMessage(getTransactionComponentList(sender,Objects.requireNonNull(plugin.getDatabase().getTransactions(senderPlayer.getUniqueId(), page))));
    }
    
    
    @Subcommand("balance|bal")
    @CommandCompletion("@players")
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
        sender.sendMessage(Component.text("%s ".formatted(target.getName()))
            .append(Component.text("has %s "
                .formatted(plugin.getMainConfig().getNumber())
                .formatted(balance)))
            .append(Component.text("%s".formatted(plugin.getMainConfig().getCurrencyName())))
        );
    }
    
    @Subcommand("take")
    @CommandCompletion("@players @nothing @reasons")
    @CommandPermission("lapzupi.currency.take")
    public void onTake(final CommandSender sender, final @NotNull OfflinePlayer player, final double amount, @Optional @Default("Take command.") String reason, @Optional boolean hidden) {
        if(userNotExists(player.getUniqueId())) {
            sender.sendMessage(Component.text(NO_PROFILE.formatted(player.getName())));
            return;
        }
        
        var newBalance = plugin.getDatabase().takeBalance(player.getUniqueId(),amount,LapzupiCurrency.class.getName(),reason,hidden);
        plugin.getBalanceManager().updateCachedUser(player.getUniqueId());
        sender.sendMessage(Component.text("Took %.2f from %s. %s now has %.2f".formatted(amount, player.getName(), player.getName(), newBalance)));
    }
    
    @Subcommand("give")
    @CommandCompletion("@players @nothing @reasons")
    @CommandPermission("lapzupi.currency.give")
    @Description("Give a player currency. Optionally, include a reason.")
    public void onGive(final CommandSender sender, final @NotNull OfflinePlayer player, final double amount, @Optional @Default("Give command.") String reason, @Optional boolean hidden) {
        if(userNotExists(player.getUniqueId())) {
            sender.sendMessage(Component.text(NO_PROFILE.formatted(player.getName())));
            return;
        }
        
        var newBalance = plugin.getDatabase().giveBalance(player.getUniqueId(),amount,LapzupiCurrency.class.getName(),reason,hidden);
        plugin.getBalanceManager().updateCachedUser(player.getUniqueId());
        sender.sendMessage(Component.text("Gave %.2f to %s. %s now has %.2f".formatted(amount, player.getName(), player.getName(), newBalance)));
    }
    
    @Subcommand("set")
    @CommandCompletion("@players @nothing @reasons")
    @CommandPermission("lapzupi.currency.set")
    @Description("Set the amount of currency a player has. Optionally, include a reason.")
    public void onSet(final CommandSender sender, final @NotNull OfflinePlayer player, final double amount, @Optional @Default("Set command.") String reason, @Optional boolean hidden) {
        if(userNotExists(player.getUniqueId())) {
            sender.sendMessage(Component.text(NO_PROFILE.formatted(player.getName())));
            return;
        }
    
        plugin.getDatabase().setBalance(player.getUniqueId(),amount,LapzupiCurrency.class.getName(),reason,hidden);
        plugin.getBalanceManager().updateCachedUser(player.getUniqueId());
        sender.sendMessage(Component.text("Set %.2f for %s".formatted(amount, player.getName())));
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
    
    private String getHiddenReason(final CommandSender sender, final Transaction transaction) {
        if(sender.hasPermission("lapzupi.currency.history.hidden")) {
            return transaction.getReason();
        }
        return transaction.getHidden() ? "***": transaction.getReason();
    }
    
    private @NotNull Component getComponentFromTransaction(final CommandSender sender,final @NotNull Transaction transaction) {
        final MainConfig.TransactionTypeConfig typeConfig = plugin.getMainConfig().getTransactionTypeConfig(transaction.getTransactionType());
        final String reason = getHiddenReason(sender,transaction);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss z");
        final String formattedTime = dateFormat.format(transaction.getTimestamp());
        return Component
            .text(typeConfig.symbol())
            .color(TextColor.fromCSSHexString(typeConfig.color()))
            .appendSpace()
            .append(Component.text(transaction.getAmount()))
            .hoverEvent(
                Component.text(formattedTime)
                    .appendNewline()
                    .append(Component.text(reason))
            );
    }
    
    
    
    
    private Component getTransactionComponentList(final CommandSender sender, final List<Transaction> transactions) {
        Component component = Component.text("");
        for(Transaction transaction: transactions) {
            component = component.append(getComponentFromTransaction(sender,transaction)).appendNewline();
        }
        return component;
    }
}
