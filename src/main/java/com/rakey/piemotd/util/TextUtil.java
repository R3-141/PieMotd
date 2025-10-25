package com.rakey.piemotd.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

public class TextUtil {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component parseMiniMessage(String text) {
        // 替换变量
        String processedText = replaceVariables(text);
        return miniMessage.deserialize(processedText);
    }

    private static String replaceVariables(String text) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;

        return text
                .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("%time%", java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")))
                .replace("%date%", java.time.LocalDate.now().toString())
                .replace("%server_version%", Bukkit.getVersion())
                .replace("%memory_used%", String.valueOf(usedMemory))
                .replace("%memory_max%", String.valueOf(maxMemory))
                .replace("%tps%", String.format("%.2f", Bukkit.getServer().getTPS()[0]));
    }
}