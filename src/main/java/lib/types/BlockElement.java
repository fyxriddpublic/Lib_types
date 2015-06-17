package lib.types;

import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.hashList.HashList;
import com.fyxridd.lib.core.api.hashList.HashListImpl;
import org.bukkit.Material;

import java.util.List;

public class BlockElement implements TypeElement{
	private HashList<Material> materials;

	public BlockElement(List<String> list) {
        materials = new HashListImpl<Material>();
        for (String s:list) {
            Material material = CoreApi.getMaterial(s);
            materials.add(material);
        }
	}

	/**
	 * @param obj 是Material
	 */
	@Override
	public boolean check(Object obj) {
		Material material = (Material) obj;
		return materials.has(material);
	}
}
