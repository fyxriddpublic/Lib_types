package lib.types;

import com.fyxridd.lib.core.api.ConfigApi;
import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.event.ReloadConfigEvent;
import lib.types.api.TypesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypesMain implements Listener{
    private static String savePath;

	//插件名 类型名 类型信息
	private static HashMap<String, HashMap<String, TypeElement>>
		entityHash = new HashMap<String, HashMap<String,TypeElement>>(),
		itemHash = new HashMap<String, HashMap<String,TypeElement>>(),
        blockHash = new HashMap<String, HashMap<String, TypeElement>>();
	
	public TypesMain() {
        savePath = TypesPlugin.dataPath+File.separator+"types.yml";
        //初始化配置
        initConfig();
		//读取配置文件
		loadConfig();
		//注册事件
		Bukkit.getPluginManager().registerEvents(this, TypesPlugin.instance);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onReloadConfig(ReloadConfigEvent e) {
		if (e.getPlugin().equals(TypesPlugin.pn)) loadConfig();
	}
	
	/**
     * @see lib.types.api.TypesApi#reloadTypes(String, org.bukkit.configuration.file.YamlConfiguration)
	 */
	public static void reloadTypes(String pn, YamlConfiguration config) {
		if (pn == null || config == null) return;
		//清除
		entityHash.put(pn, new HashMap<String, TypeElement>());
		itemHash.put(pn, new HashMap<String, TypeElement>());
        blockHash.put(pn, new HashMap<String, TypeElement>());
		//读取实体类型
		if (config.contains("entity")) {
			MemorySection ms = (MemorySection) config.get("entity");
            if (ms != null) {
                Map<String, Object> map = ms.getValues(false);
                for (String key : map.keySet()) loadEntityType(pn, ms, key);
            }
		}
		//读取物品类型
		if (config.contains("item")) {
			MemorySection ms = (MemorySection) config.get("item");
            if (ms != null) {
                Map<String, Object> map = ms.getValues(false);
                for (String key : map.keySet()) loadItemType(pn, ms, key);
            }
		}
        //读取方块类型
        if (config.contains("block")) {
            MemorySection ms = (MemorySection) config.get("block");
            if (ms != null) {
                Map<String, Object> map = ms.getValues(false);
                for (String key : map.keySet()) loadBlockType(pn, ms, key);
            }
        }
	}

	/**
     * @see lib.types.api.TypesApi#checkEntity(String, String, org.bukkit.entity.EntityType)
	 */
	public static boolean checkEntity(String pn, String type, EntityType entityType){
        if (pn == null || type == null || entityType == null) return false;

		try {
			EntityElement entityElement = (EntityElement) entityHash.get(pn).get(type);
			return entityElement.check(entityType);
		} catch (Exception e) {
            e.printStackTrace();
            return false;
		}
	}

	/**
     * @see lib.types.api.TypesApi#checkItem(String, String, org.bukkit.inventory.ItemStack)
	 */
	public static boolean checkItem(String pn, String type, ItemStack is) {
        if (pn == null || type == null || is == null) return false;

        try {
            if (pn == null) pn = TypesPlugin.pn;
			ItemElement itemElement = (ItemElement) itemHash.get(pn).get(type);
			return itemElement.check(is);
		} catch (Exception e) {
            e.printStackTrace();
            return false;
		}
	}

    /**
     * @see lib.types.api.TypesApi#checkBlock(String, String, org.bukkit.Material)
     */
    public static boolean checkBlock(String pn, String type, Material material) {
        if (pn == null || type == null || material == null) return false;

        try {
            BlockElement blockElement = (BlockElement) blockHash.get(pn).get(type);
            return blockElement.check(material);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSafeBlock(Material material) {
        if (material == null) return false;

        return checkBlock(TypesPlugin.pn, "safeBlocks", material);
    }

	/**
	 * 从配置中读取指定的实体类型
	 * @param pn 插件名,不为null
	 * @param ms 类型从哪个配置中读取,不为null
	 * @param type 类型,不为null
	 */
	private static void loadEntityType(String pn, MemorySection ms, String type) {
		//已经读取过此类型
		if (entityHash.get(pn).containsKey(type)) return;
		//读取
		List<String> list = ms.getStringList(type);
		List<EntityType> entityList = new ArrayList<EntityType>();
		for (String s:list) {
			EntityType entityType = CoreApi.getEntityType(s);
			if (entityType != null) entityList.add(entityType);
		}
		entityHash.get(pn).put(type, new EntityElement(entityList));
	}

	/**
	 * 从配置中读取指定的物体类型
	 * @param pn 插件名,不为null
	 * @param ms 类型从哪个配置中读取,不为null
	 * @param type 类型,不为null
	 */
	private static void loadItemType(String pn, MemorySection ms, String type) {
		//已经读取过此类型
		if (itemHash.get(pn).containsKey(type)) return;
		//读取
		MemorySection section = (MemorySection) ms.get(type);
		if (section != null) {
			ItemElement itemElement = new ItemElement(section);
			itemHash.get(pn).put(type, itemElement);
		}
	}

    /**
     * 从配置中读取指定的方块类型
     * @param pn 插件名,不为null
     * @param ms 类型从哪个配置中读取,不为null
     * @param type 类型,不为null
     */
    private static void loadBlockType(String pn, MemorySection ms, String type) {
        //已经读取过此类型
        if (blockHash.get(pn).containsKey(type)) return;
        //读取
        List<String> list = ms.getStringList(type);
        blockHash.get(pn).put(type, new BlockElement(list));
    }

    private void initConfig() {
        List<String> filter = ConfigApi.getDefaultFilter();
        filter.add("types.yml");
        ConfigApi.register(TypesPlugin.file, TypesPlugin.dataPath, filter, TypesPlugin.pn, null);
        ConfigApi.loadConfig(TypesPlugin.pn);
    }

	private static void loadConfig() {
		YamlConfiguration typesConfig = CoreApi.loadConfigByUTF8(new File(savePath));
        if (typesConfig == null) {
            ConfigApi.log(TypesPlugin.pn, "typesConfig load error");
            return;
        }
		reloadTypes(TypesPlugin.pn, typesConfig);
	}
}
