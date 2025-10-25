package com.rakey.piemotd;

import com.rakey.piemotd.manager.CommandManager;
import com.rakey.piemotd.manager.ConfigManager;
import com.rakey.piemotd.manager.MotdManager;
import com.rakey.piemotd.manager.IconManager;
import com.rakey.piemotd.manager.WelcomeManager;
import com.rakey.piemotd.listener.ServerPingListener;
import com.rakey.piemotd.listener.PlayerJoinListener;
import com.rakey.piemotd.task.MotdUpdateTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class PieMotd extends JavaPlugin {

    private static PieMotd instance;
    private ConfigManager configManager;
    private MotdManager motdManager;
    private IconManager iconManager;
    private WelcomeManager welcomeManager;

    @Override
    public void onEnable() {
        instance = this;

        // 初始化管理器
        this.configManager = new ConfigManager(this);
        this.motdManager = new MotdManager(this);
        this.iconManager = new IconManager(this);
        this.welcomeManager = new WelcomeManager(this);

        // 加载配置
        configManager.loadConfigs();
        motdManager.loadMotd();
        iconManager.loadIcons();
        welcomeManager.loadWelcomeConfig();

        // 注册监听器
        getServer().getPluginManager().registerEvents(new ServerPingListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // 注册命令
        CommandManager commandManager = new CommandManager(this);
        getCommand("piemotd").setExecutor(commandManager);
        getCommand("piemotd").setTabCompleter(commandManager);

        // 启动定时任务
        if (configManager.getConfig().getBoolean("motd.update-enabled", true)) {
            int interval = configManager.getConfig().getInt("motd.update-interval", 10);
            new MotdUpdateTask(this).runTaskTimer(this, 0, interval * 20L);
        }

        getLogger().info("§aPieMotd 插件已启用! §e版本: 1.0.0");
        getLogger().info("§a- 动态RGB MOTD");
        getLogger().info("§a- 智能服务器头像");
        getLogger().info("§a- 登录欢迎标题");
        getLogger().info("§a- 管理命令系统");
    }

    @Override
    public void onDisable() {
        getLogger().info("§cPieMotd 插件已禁用");
    }

    public static PieMotd getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MotdManager getMotdManager() {
        return motdManager;
    }

    public IconManager getIconManager() {
        return iconManager;
    }

    public WelcomeManager getWelcomeManager() {
        return welcomeManager;
    }
}