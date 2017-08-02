package cn.sbx.deeper.moblie.util;

/**
 * 导航样式匹配的帮助类
 * @author terry.C
 *
 */
public class MenuTypeUtil {

	public static final MenuType chooseMenuType(String type) {
		if(type == null || "".equalsIgnoreCase(type)) {
			return MenuType.TOP;
		}
		if("bottom".equalsIgnoreCase(type)) {
			return MenuType.BOTTOM;
		}else if("top".equalsIgnoreCase(type)) {
			return MenuType.TOP;
		}else if("sodoku".equalsIgnoreCase(type)) {
			return MenuType.SQUARED;
		}else if("left".equalsIgnoreCase(type)) {
			return MenuType.LEFT;
		}else if("list".equalsIgnoreCase(type)) {
			return MenuType.LIST;
		}else if("all".equalsIgnoreCase(type)) {//added
			return MenuType.ALL;
		}
		return MenuType.BOTTOM;
	}
	
	public static final MenuDisplayType chooseMenuDisplayType(String type) {
		if(type == null || "".equalsIgnoreCase(type)) {
			return MenuDisplayType.PICWORD;
		}
		if("word".equalsIgnoreCase(type)) {
			return MenuDisplayType.WORD;
		}else if("picword".equalsIgnoreCase(type)) {
			return MenuDisplayType.PICWORD;
		}else if("picture".equalsIgnoreCase(type)) {
			return MenuDisplayType.PICTURE;
		}
		return MenuDisplayType.PICWORD;
	}
}
