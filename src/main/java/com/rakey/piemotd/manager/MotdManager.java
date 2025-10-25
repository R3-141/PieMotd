package com.rakey.piemotd.manager;

import com.rakey.piemotd.PieMotd;
import com.rakey.piemotd.util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MotdManager {
    private final PieMotd plugin;
    private List<String> motdMessages;
    private int currentIndex = 0;

    public MotdManager(PieMotd plugin) {
        this.plugin = plugin;
    }

    public void loadMotd() {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        this.motdMessages = config.getStringList("motd.messages");
        plugin.getLogger().info("§a已加载 " + motdMessages.size() + " 个MOTD消息");
    }

    public Component getCurrentMotd() {
        if (motdMessages == null || motdMessages.isEmpty()) {
            return Component.text("默认MOTD");
        }

        String motdText;
        if (plugin.getConfigManager().getConfig().getBoolean("motd.random", true)) {
            // 随机选择
            motdText = motdMessages.get(ThreadLocalRandom.current().nextInt(motdMessages.size()));
        } else {
            // 顺序选择
            motdText = motdMessages.get(currentIndex);
            currentIndex = (currentIndex + 1) % motdMessages.size();
        }

        return TextUtil.parseMiniMessage(motdText);
    }
}