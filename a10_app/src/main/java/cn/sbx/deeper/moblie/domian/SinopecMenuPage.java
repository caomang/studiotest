package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class SinopecMenuPage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;
	
	public String code;
	
	public String caption;
	
	public String pageType;

	public String urlpath;
	@Override
	public String toString() {
		return "MenuPage [id=" + id + ", code=" + code + ", caption=" + caption
				+ ", pageType=" + pageType + "]";
	}
}
