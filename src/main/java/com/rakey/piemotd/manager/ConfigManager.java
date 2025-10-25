package com.rakey.piemotd.manager;

import com.rakey.piemotd.PieMotd;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {
    private final PieMotd plugin;
    private FileConfiguration config;
    private FileConfiguration language;

    public ConfigManager(PieMotd plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        // 创建插件数据文件夹
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // 加载主配置
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try (InputStream in = plugin.getResource("config.yml")) {
                Files.copy(in, configFile.toPath());
                plugin.getLogger().info("§a已创建默认配置文件");
            } catch (Exception e) {
                plugin.getLogger().severe("无法创建配置文件: " + e.getMessage());
            }
        }

        // 加载语言文件
        File languageFile = new File(plugin.getDataFolder(), "language.yml");
        if (!languageFile.exists()) {
            try (InputStream in = plugin.getResource("language.yml")) {
                Files.copy(in, languageFile.toPath());
                plugin.getLogger().info("§a已创建默认语言文件");
            } catch (Exception e) {
                plugin.getLogger().severe("无法创建语言文件: " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        language = YamlConfiguration.loadConfiguration(languageFile);

        // 检查当前语言设置
        String currentLanguage = config.getString("settings.language", "zh_cn");
        plugin.getLogger().info("§a配置文件和语言文件加载完成 - 当前语言: " + currentLanguage);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getLanguage() {
        return language;
    }

    // 新增方法：获取当前语言的消息
    public String getMessage(String path) {
        String currentLanguage = config.getString("settings.language", "zh_cn");
        String fullPath = currentLanguage + "." + path;

        if (language.contains(fullPath)) {
            return language.getString(fullPath);
        }

        // 如果当前语言没有找到，尝试英文
        if (!currentLanguage.equals("en_us")) {
            String englishPath = "en_us." + path;
            if (language.contains(englishPath)) {
                return language.getString(englishPath);
            }
        }

        // 如果都没有找到，返回默认消息
        return "[" + path + "]";
    }

    // 新增方法：重载所有配置
    public void reloadAllConfigs() {
        loadConfigs();
    }
}