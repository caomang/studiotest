package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.List;

public class SchedInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String meterReadRoute;// 抄表路径编码
	private String cmMrRteDescr;// 抄表路径描述
	private String meterReadCycle;// 抄表周期编码
	private String cmMrCycDescr;// 抄表周期描述
	private String scheduledSelectionDate;// 计划选择日期
	private String scheduledReadDate;// 计划抄表日期
	private String cmMrDate;// 上传任务日期
	private List<CustInfo> custInfos;// 用户信息
	
	private int account ; //记录当前任务下用户数量
	
	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public String getCmMrDate() {
		return cmMrDate;
	}

	public void setCmMrDate(String cmMrDate) {
		this.cmMrDate = cmMrDate;
	}



	
//	上传任务列表选中状态
	public boolean isCheck;
//	该任务所在数据库的rowid
	private  String rowId;
	
	
	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getMeterReadRoute() {
		return meterReadRoute;
	}

	public void setMeterReadRoute(String meterReadRoute) {
		this.meterReadRoute = meterReadRoute;
	}

	public String getCmMrRteDescr() {
		return cmMrRteDescr;
	}

	public void setCmMrRteDescr(String cmMrRteDescr) {
		this.cmMrRteDescr = cmMrRteDescr;
	}

	public String getMeterReadCycle() {
		return meterReadCycle;
	}

	public void setMeterReadCycle(String meterReadCycle) {
		this.meterReadCycle = meterReadCycle;
	}

	public String getCmMrCycDescr() {
		return cmMrCycDescr;
	}

	public void setCmMrCycDescr(String cmMrCycDescr) {
		this.cmMrCycDescr = cmMrCycDescr;
	}

	public String getScheduledSelectionDate() {
		return scheduledSelectionDate;
	}

	public void setScheduledSelectionDate(String scheduledSelectionDate) {
		this.scheduledSelectionDate = scheduledSelectionDate;
	}

	public String getScheduledReadDate() {
		return scheduledReadDate;
	}

	public void setScheduledReadDate(String scheduledReadDate) {
		this.scheduledReadDate = scheduledReadDate;
	}

	public List<CustInfo> getCustInfos() {
		return custInfos;
	}

	public void setCustInfos(List<CustInfo> custInfos) {
		this.custInfos = custInfos;
	}

	 

}
