package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class ApprovalDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6232629270770985086L;
	private String id;
	private String code;
	private String caption;
	private String pageType;

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

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	@Override
	public String toString() {
		return "ApprovalDetail toString : id= " + getId() + " code="
				+ getCode() + " caption=" + getCaption() + " pageType="
				+ getPageType();

	}
}
