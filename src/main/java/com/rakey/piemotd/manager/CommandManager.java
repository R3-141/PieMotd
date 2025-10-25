package com.rakey.piemotd.manager;

import com.rakey.piemotd.PieMotd;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final PieMotd plugin;

    public CommandManager(PieMotd plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("piemotd.reload")) {
                    sender.sendMessage(Component.text("§c你没有权限使用此命令."));
                    return true;
                }
                reloadConfig(sender);
                break;

            case "preview":
                if (!sender.hasPermission("piemotd.admin")) {
                    sender.sendMessage(Component.text("§c你没有权限使用此命令."));
                    return true;
                }
                previewMotd(sender);
                break;

            case "help":
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("§6=== PieMotd 帮助 ==="));
        sender.sendMessage(Component.text("§e/piemotd reload §7- 重载插件配置"));
        sender.sendMessage(Component.text("§e/piemotd preview §7- 预览当前MOTD"));
        sender.sendMessage(Component.text("§e/piemotd help §7- 显示此帮助"));
    }

    private void reloadConfig(CommandSender sender) {
        try {
            plugin.getConfigManager().loadConfigs();
            plugin.getMotdManager().loadMotd();
            plugin.getIconManager().loadIcons();
            plugin.getWelcomeManager().loadWelcomeConfig();
            sender.sendMessage(Component.text("§aPieMotd 配置已重载!"));
        } catch (Exception e) {
            sender.sendMessage(Component.text("§c重载配置时出错: " + e.getMessage()));
            plugin.getLogger().severe("重载配置时出错: " + e.getMessage());
        }
    }

    private void previewMotd(CommandSender sender) {
        Component currentMotd = plugin.getMotdManager().getCurrentMotd();
        sender.sendMessage(Component.text("§6当前 MOTD 预览:"));
        sender.sendMessage(currentMotd);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("preview");
            completions.add("help");
        }

        return completions;
    }
}