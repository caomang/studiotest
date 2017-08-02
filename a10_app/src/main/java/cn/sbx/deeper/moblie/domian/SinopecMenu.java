package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.util.MenuAuthType;

public class SinopecMenu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String layout;

	public String notification;

	public String level;
	public String version;
	
	public String columns;
	
	public String itemTemplate;
	
	public MenuAuthType menuAuthType;
	
//	public List<MenuGroup> menuGroup = new ArrayList<MenuGroup>();
//	public List<MenuModule> menuModules = new ArrayList<MenuModule>();
	
	public List<Object> menuObject = new ArrayList<Object>();
	
}
