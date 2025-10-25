package com.rakey.piemotd.manager;

import com.rakey.piemotd.PieMotd;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class IconManager {
    private final PieMotd plugin;
    private List<File> iconFiles;
    private int currentIndex = 0;
    private long lastRotationTime = 0;
    private long rotationInterval;

    public IconManager(PieMotd plugin) {
        this.plugin = plugin;
    }

    public void loadIcons() {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        this.iconFiles = new ArrayList<>();
        // 改为秒，直接乘以1000（原来是分钟乘以60*1000）
        this.rotationInterval = (long) (config.getDouble("server-icon.rotation-interval", 30.0) * 1000);

        // 创建icons目录
        File iconsDir = new File(plugin.getDataFolder(), "icons");
        if (!iconsDir.exists()) {
            iconsDir.mkdirs();
            plugin.getLogger().info("§a已创建icons目录: " + iconsDir.getPath());
        }

        // 加载头像文件
        List<String> iconPaths = config.getStringList("server-icon.icons");
        for (String iconPath : iconPaths) {
            File iconFile = new File(plugin.getDataFolder(), iconPath);

            // 详细日志
            plugin.getLogger().info("检查头像文件: " + iconFile.getAbsolutePath());
            plugin.getLogger().info("文件是否存在: " + iconFile.exists());

            if (iconFile.exists()) {
                if (isValidIcon(iconFile)) {
                    iconFiles.add(iconFile);
                    plugin.getLogger().info("✓ 加载成功: " + iconPath);
                } else {
                    plugin.getLogger().warning("✗ 图片尺寸或格式无效: " + iconPath);
                    // 输出图片信息帮助调试
                    try {
                        BufferedImage image = ImageIO.read(iconFile);
                        if (image != null) {
                            plugin.getLogger().warning("图片尺寸: " + image.getWidth() + "x" + image.getHeight());
                        }
                    } catch (IOException e) {
                        plugin.getLogger().warning("无法读取图片信息: " + e.getMessage());
                    }
                }
            } else {
                plugin.getLogger().warning("✗ 文件不存在: " + iconPath);
            }
        }

        plugin.getLogger().info("§a已加载 " + iconFiles.size() + " 个头像文件");
    }

    private boolean isValidIcon(File iconFile) {
        try {
            BufferedImage image = ImageIO.read(iconFile);
            if (image == null) {
                plugin.getLogger().warning("无法读取图片文件: " + iconFile.getName());
                return false;
            }

            int width = image.getWidth();
            int height = image.getHeight();

            // 支持 64x64 和 128x128
            boolean validSize = (width == 64 && height == 64) || (width == 128 && height == 128);

            if (!validSize) {
                plugin.getLogger().warning("图片尺寸不支持: " + width + "x" + height + " (需要 64x64 或 128x128)");
            }

            return validSize;
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "读取图片文件时出错: " + iconFile.getName(), e);
            return false;
        }
    }

    public File getCurrentIcon() {
        if (iconFiles == null || iconFiles.isEmpty()) {
            return getDefaultIcon();
        }

        String rotationMode = plugin.getConfigManager().getConfig().getString("server-icon.rotation-mode", "RANDOM");
        long currentTime = System.currentTimeMillis();

        // 检查是否需要轮换
        if (currentTime - lastRotationTime < rotationInterval) {
            if (currentIndex < iconFiles.size()) {
                return iconFiles.get(currentIndex);
            }
        }

        lastRotationTime = currentTime;

        switch (rotationMode.toUpperCase()) {
            case "RANDOM":
                currentIndex = ThreadLocalRandom.current().nextInt(iconFiles.size());
                break;
            case "SEQUENTIAL":
                currentIndex = (currentIndex + 1) % iconFiles.size();
                break;
            case "TIME":
                // 基于时间的轮换（每小时一个头像）
                int hours = java.time.LocalTime.now().getHour();
                currentIndex = hours % iconFiles.size();
                break;
            default:
                currentIndex = 0;
        }

        return iconFiles.get(currentIndex);
    }

    private File getDefaultIcon() {
        File defaultIcon = new File(plugin.getDataFolder(),
                plugin.getConfigManager().getConfig().getString("server-icon.default", "icons/default.png"));
        return defaultIcon.exists() ? defaultIcon : null;
    }

    public CachedServerIcon loadServerIcon(File iconFile) {
        try {
            return plugin.getServer().loadServerIcon(iconFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "§c无法加载服务器头像: " + iconFile.getName(), e);
            return null;
        }
    }
}