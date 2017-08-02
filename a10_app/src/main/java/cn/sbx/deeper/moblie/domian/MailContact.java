package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class MailContact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9169512799511517298L;

	private String userId = "";
	private String fullname = "";
	private String altname = "";
	private String departmentId = "";
	private String departmentName = "";
	private boolean bool = false;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAltname() {
		return altname;
	}

	public void setAltname(String altname) {
		this.altname = altname;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

}
