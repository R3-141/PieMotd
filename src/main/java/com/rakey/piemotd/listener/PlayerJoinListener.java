package com.rakey.piemotd.listener;

import com.rakey.piemotd.PieMotd;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final PieMotd plugin;

    public PlayerJoinListener(PieMotd plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 延迟执行，确保玩家完全进入游戏
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getWelcomeManager().welcomePlayer(player);
        }, 20L); // 延迟1秒（20 ticks）
    }
}