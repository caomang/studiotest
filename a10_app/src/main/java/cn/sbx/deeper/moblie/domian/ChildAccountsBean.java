package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class ChildAccountsBean implements Serializable {
	public String name;
	public String passWord;
	
	public String resault;
	public String  message;

	public String getResault() {
		return resault;
	}
	public void setResault(String resault) {
		this.resault = resault;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetaname() {
		return detaname;
	}
	public void setDetaname(String detaname) {
		this.detaname = detaname;
	}
	public String getBsId() {
		return bsId;
	}
	public void setBsId(String bsId) {
		this.bsId = bsId;
	}
	public String getBsName() {
		return bsName;
	}
	public void setBsName(String bsName) {
		this.bsName = bsName;
	}
	public String getDetapassWord() {
		return detapassWord;
	}
	public void setDetapassWord(String detapassWord) {
		this.detapassWord = detapassWord;
	}
	public String detaname;
	public String bsId;
	public String bsName;
	public String detapassWord;

}
