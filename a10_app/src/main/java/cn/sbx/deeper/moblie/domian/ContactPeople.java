package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class ContactPeople implements Serializable {

	/**
	 * 拨打电话联系人
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String phone;
	private String member;
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
