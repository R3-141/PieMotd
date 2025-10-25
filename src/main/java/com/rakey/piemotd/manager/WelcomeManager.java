package com.rakey.piemotd.manager;

import com.rakey.piemotd.PieMotd;
import com.rakey.piemotd.util.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WelcomeManager {
    private final PieMotd plugin;
    private String welcomeTitleText;
    private String welcomeSubtitleText;
    private int fadeIn;
    private int stay;
    private int fadeOut;
    private boolean soundEnabled;
    private String soundName;
    private float soundVolume;
    private float soundPitch;

    public WelcomeManager(PieMotd plugin) {
        this.plugin = plugin;
    }

    public void loadWelcomeConfig() {
        FileConfiguration config = plugin.getConfigManager().getConfig();

        // 加载标题设置（保存原始文本，不立即解析）
        this.welcomeTitleText = config.getString("welcome.title.main", "<gradient:#FF512F:#DD2476>欢迎 %player%!</gradient>");
        this.welcomeSubtitleText = config.getString("welcome.title.subtitle", "<gray>祝你游戏愉快！你是第 %player_count% 位玩家</gray>");

        // 加载时间设置
        this.fadeIn = config.getInt("welcome.title.fade-in", 20);
        this.stay = config.getInt("welcome.title.stay", 100);
        this.fadeOut = config.getInt("welcome.title.fade-out", 20);

        // 加载音效设置
        this.soundEnabled = config.getBoolean("welcome.sound.enabled", true);
        this.soundName = config.getString("welcome.sound.sound", "entity.player.levelup");
        this.soundVolume = (float) config.getDouble("welcome.sound.volume", 1.0);
        this.soundPitch = (float) config.getDouble("welcome.sound.pitch", 1.0);

        plugin.getLogger().info("§a欢迎配置加载完成");
    }

    public void welcomePlayer(Player player) {
        if (!plugin.getConfigManager().getConfig().getBoolean("welcome.enabled", true)) {
            return;
        }

        // 替换变量并解析 MiniMessage
        String processedTitle = replacePlayerVariables(welcomeTitleText, player);
        String processedSubtitle = replacePlayerVariables(welcomeSubtitleText, player);

        Component titleComponent = TextUtil.parseMiniMessage(processedTitle);
        Component subtitleComponent = TextUtil.parseMiniMessage(processedSubtitle);

        // 发送欢迎标题（使用 Kyori Adventure API）
        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn * 50L),  // ticks 转毫秒
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
        );

        Title title = Title.title(titleComponent, subtitleComponent, times);

        // 使用 Adventure API 显示标题
        plugin.getServer().getConsoleSender().sendMessage(titleComponent); // 调试：在控制台显示标题内容
        player.showTitle(title);

        // 播放欢迎音效
        if (soundEnabled) {
            try {
                Sound sound = Sound.valueOf(soundName.toUpperCase().replace(".", "_"));
                player.playSound(player.getLocation(), sound, soundVolume, soundPitch);
            } catch (IllegalArgumentException e) {
                // 如果音效名称无效，使用默认音效
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, soundVolume, soundPitch);
                plugin.getLogger().warning("§c未知音效: " + soundName + "，使用默认音效");
            }
        }

        plugin.getLogger().info("§a为玩家 " + player.getName() + " 显示欢迎标题");
    }

    private String replacePlayerVariables(String text, Player player) {
        return text
                .replace("%player%", player.getName())
                .replace("%player_count%", String.valueOf(Bukkit.getOnlinePlayers().size()));
    }
}