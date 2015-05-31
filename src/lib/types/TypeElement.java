package lib.types;

public interface TypeElement {
	/**
	 * 检测是否满足类型
	 * @param obj 检测的对象
	 * @return
	 */
	public boolean check(Object obj);
}
