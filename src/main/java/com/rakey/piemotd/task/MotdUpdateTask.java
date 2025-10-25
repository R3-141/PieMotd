package com.rakey.piemotd.task;

import com.rakey.piemotd.PieMotd;
import org.bukkit.scheduler.BukkitRunnable;

public class MotdUpdateTask extends BukkitRunnable {
    private final PieMotd plugin;

    public MotdUpdateTask(PieMotd plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // 这里处理MOTD动态更新
        // 暂时留空，后面实现具体功能
    }
}