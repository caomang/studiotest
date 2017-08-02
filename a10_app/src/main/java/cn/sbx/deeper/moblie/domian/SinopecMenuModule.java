package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SinopecMenuModule implements Serializable{

	/**
	 *  菜单项的module
	 */
	private static final long serialVersionUID = 1L;

	public String id;
	
	public String mClass;
	
	public String caption;
	
	public String barImg1;
	
	public String barImg2;
	
	public String itemImg1;
	
	public String itemImg2;
	
	public String bgImg;
	
	public String originalDoc;
	
	public String notification;
	
	public boolean isGroupModule = false;
	
	public String bottomShow;
	
	public List<SinopecMenuPage> menuPages = new ArrayList<SinopecMenuPage>();

	@Override
	public String toString() {
		return "MenuModule [id=" + id + ", mClass=" + mClass + ", caption="
				+ caption + ", barImg1=" + barImg1 + ", barImg2=" + barImg2
				+ ", itemImg1=" + itemImg1 + ", itemImg2=" + itemImg2
				+ ", bgImg=" + bgImg + ", originalDoc=" + originalDoc
				+ ", notification=" + notification + "]";
	}
	public String parentId;
	public boolean isChoose;
	public String code;
	
	public String numUrl;
	
	
	
}
