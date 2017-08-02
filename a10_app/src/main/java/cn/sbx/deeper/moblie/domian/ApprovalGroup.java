package cn.sbx.deeper.moblie.domian;

import java.util.List;

public class ApprovalGroup {
	private String id;
	private String code;
	private String caption;
	private String barImg1;
	private String barImg2;
	private String itemImg1;
	private String itemImg2;
	private String bgImg;
	private String submenu;
	private List<ApprovalItem> item;
	private String category;
	private String captionGroup;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCaptionGroup() {
		return captionGroup;
	}

	public void setCaptionGroup(String captionGroup) {
		this.captionGroup = captionGroup;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getBarImg1() {
		return barImg1;
	}

	public void setBarImg1(String barImg1) {
		this.barImg1 = barImg1;
	}

	public String getBarImg2() {
		return barImg2;
	}

	public void setBarImg2(String barImg2) {
		this.barImg2 = barImg2;
	}

	public String getItemImg1() {
		return itemImg1;
	}

	public void setItemImg1(String itemImg1) {
		this.itemImg1 = itemImg1;
	}

	public String getItemImg2() {
		return itemImg2;
	}

	public void setItemImg2(String itemImg2) {
		this.itemImg2 = itemImg2;
	}

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}

	public String getSubmenu() {
		return submenu;
	}

	public void setSubmenu(String submenu) {
		this.submenu = submenu;
	}

	public List<ApprovalItem> getItem() {
		return item;
	}

	public void setItem(List<ApprovalItem> item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "ApprovalGroup toString : category= "+getCategory()+" caption=" + getCaption() + " barImg1=" + getBarImg1() + " barImg2=" + getBarImg2() + " list=" + getItem();
	}

}
