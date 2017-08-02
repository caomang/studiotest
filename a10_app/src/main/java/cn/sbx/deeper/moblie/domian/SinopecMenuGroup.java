package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SinopecMenuGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String caption;
	
	public String barImg1;
	
	public String barImg2;
	
	public String id;
	
	public boolean containsAll = false;
	
	public String itemTemplate;
	
	public String subModuleId;
	
	public String bottomShow;
	
	@Override
	public String toString() {
		return "MenuGroup [caption=" + caption + ", barImg1=" + barImg1
				+ ", barImg2=" + barImg2 + ", itemImg1=" + itemImg1
				+ ", layout=" + layout + ", notification=" + notification
				+ ", level=" + level + ", rows=" + rows + ", columns="
				+ columns + ", itemImg2=" + itemImg2 + ", bgImg=" + bgImg;
				
	}

	public String itemImg1;
	public String layout;
	public String notification;
	public String level;
	public String rows;
	public String columns;
	
	public String itemImg2;
	
	public String bgImg;
	
//	public List<SinopecMenuModule> menuModules = new ArrayList<SinopecMenuModule>();
//	public List<SinopecMenuGroup> menuMenuGroup = new ArrayList<SinopecMenuGroup>();
	
	public List<Object> menuobjObjects = new ArrayList<Object>();
	public String parentId;
	public boolean isChoose;
	public String code;
}
