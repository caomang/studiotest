package cn.sbx.deeper.moblie.domian;

import java.util.ArrayList;

public class SchedInfoResidents {
	public String cmScTypeCd; // 安检计划类型编码
	public String cmSchedId; // 安检计划ID
	public String description;// 安检计划描述
	public String scheduleDateTimeStart;// 计划安检时间
	public String spType;// 服务点类型编码
	public String cmMrDate;// 上传时间
	private int account; // 记录当前任务下用户数量

	// 上传任务列表选中状态
	public boolean isCheck;

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public ArrayList<CustInfo_AnJian> custInfo;

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getCmMrDate() {
		return cmMrDate;
	}

	public void setCmMrDate(String cmMrDate) {
		this.cmMrDate = cmMrDate;
	}

	public String getCmScTypeCd() {
		return cmScTypeCd;
	}

	public void setCmScTypeCd(String cmScTypeCd) {
		this.cmScTypeCd = cmScTypeCd;
	}

	public String getCmSchedId() {
		return cmSchedId;
	}

	public void setCmSchedId(String cmSchedId) {
		this.cmSchedId = cmSchedId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScheduleDateTimeStart() {
		return scheduleDateTimeStart;
	}

	public void setScheduleDateTimeStart(String scheduleDateTimeStart) {
		this.scheduleDateTimeStart = scheduleDateTimeStart;
	}

	public String getSpType() {
		return spType;
	}

	public void setSpType(String spType) {
		this.spType = spType;
	}

	public ArrayList<CustInfo_AnJian> getCustInfo() {
		return custInfo;
	}

	public void setCustInfo(ArrayList<CustInfo_AnJian> custInfo) {
		this.custInfo = custInfo;
	}

}
