package cn.sbx.deeper.moblie.domian;


public class PerPhone  {
	/**
	 * 用户电话
	 */
	private static final long serialVersionUID = 1L;
	private String sequence;
	private String phoneType;
	private String phone;
	private String extension;
	private String cmPhoneOprtType;  
	private String cmStageDwnStatus; 
	private String cmSchedId;

	
	
	
	
	
	public String getCmPhoneOprtType() {
		return cmPhoneOprtType;
	}

	public void setCmPhoneOprtType(String cmPhoneOprtType) {
		this.cmPhoneOprtType = cmPhoneOprtType;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
