package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.List;

public class ApprovalItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8617207045717514842L;
	private String id;
	private String code;
	private String caption;
	private String barImg1;
	private String barImg2;
	private String itemImg1;
	private String itemImg2;
	private String bgImg;
	private String pageType;
	private List<ApprovalDetail> detail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
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

	public List<ApprovalDetail> getDetail() {
		return detail;
	}

	public void setDetail(List<ApprovalDetail> detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "ApprovalItem toString : id= " + getId() + " code=" + getCode() + " caption=" + getCaption() + " barImg1=" + getBarImg1()
				+ " barImg2=" + getBarImg2() + " list=" + getDetail();

	}

}
