package com.fyxridd.lib.types.api;

import com.fyxridd.lib.types.TypesMain;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class TypesApi {
    /**
     * @param file yml文件,可为null(null时无效果)
     * @see #reloadTypes(String, org.bukkit.configuration.file.YamlConfiguration)
     */
    public static void reloadTypes(String plugin, File file) {
        TypesMain.reloadTypes(plugin, file);
    }

    /**
     * 重新读取类型配置
     * @param plugin 插件名,可为null(null时无效果)
     * @param config 配置,可为null(null时无效果)
     */
    public static void reloadTypes(String plugin, YamlConfiguration config) {
        TypesMain.reloadTypes(plugin, config);
    }

    /**
     * 检测实体类型
     * @param plugin 插件名,可为null(null时返回false)
     * @param type 类型名,可为null(null时返回false)
     * @param entityType 实体类型,可为null(null时返回false)
     * @return 是否符合类型
     */
    public static boolean checkEntity(String plugin, String type, EntityType entityType){
        return TypesMain.checkEntity(plugin, type, entityType);
    }

    /**
     * 检测物品类型
     * @param plugin 插件名,可为null(null时返回false)
     * @param type 类型名,可为null(null时返回false)
     * @param is 物品,可为null(null时返回false)
     * @return 是否符合类型
     */
    public static boolean checkItem(String plugin, String type, ItemStack is){
        return TypesMain.checkItem(plugin, type, is);
    }

    /**
     * 检测方块类型
     * @param plugin 插件名,可为null(null时返回false)
     * @param type 类型名,可为null(null时返回false)
     * @param material 材料,可为null(null时返回false)
     * @return 是否符合类型
     */
    public static boolean checkBlock(String plugin, String type, Material material){
        return TypesMain.checkBlock(plugin, type, material);
    }

    /**
     * 检测是否是安全的方块
     * @param material 材料,可为null(null时返回false)
     * @return 是否是安全的方块
     */
    public static boolean isSafeBlock(Material material) {
        return TypesMain.isSafeBlock(material);
    }
}
