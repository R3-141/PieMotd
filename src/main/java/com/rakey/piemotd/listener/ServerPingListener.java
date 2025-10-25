package com.rakey.piemotd.listener;

import com.rakey.piemotd.PieMotd;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import java.io.File;

public class ServerPingListener implements Listener {
    private final PieMotd plugin;

    public ServerPingListener(PieMotd plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        // 设置MOTD
        if (plugin.getConfigManager().getConfig().getBoolean("motd.enabled", true)) {
            event.motd(plugin.getMotdManager().getCurrentMotd());
        }

        // 设置服务器头像
        if (plugin.getConfigManager().getConfig().getBoolean("server-icon.enabled", true)) {
            try {
                File iconFile = plugin.getIconManager().getCurrentIcon();
                if (iconFile != null && iconFile.exists()) {
                    CachedServerIcon serverIcon = plugin.getIconManager().loadServerIcon(iconFile);
                    if (serverIcon != null) {
                        event.setServerIcon(serverIcon);
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("§c设置服务器头像时出错: " + e.getMessage());
            }
        }
    }
}