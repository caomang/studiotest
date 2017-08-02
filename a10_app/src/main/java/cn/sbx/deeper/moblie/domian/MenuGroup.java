package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String caption;
	
	public String barImg1;
	
	public String barImg2;
	
	public String itemImg1;
	
	public String itemImg2;
	
	public String bgImg;
	
	public List<MenuModule> menuModules = new ArrayList<MenuModule>();
}
