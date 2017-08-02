package cn.sbx.deeper.moblie.domian;

//安全隐患类
public class SecurityCheck {
	public String userNo;
	public String path;
	public String checkDate;
	public String cmMrCommCd;
	
	public String getCmMrCommCd() {
		return cmMrCommCd;
	}
	public void setCmMrCommCd(String cmMrCommCd) {
		this.cmMrCommCd = cmMrCommCd;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
}
