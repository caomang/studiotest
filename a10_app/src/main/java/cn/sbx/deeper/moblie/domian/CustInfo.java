package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.List;

public class CustInfo implements Serializable {

	/**
	 * 用户信息
	 */
	private static final long serialVersionUID = 1L;
	private String schedInfoID;
	private String meterReadCycleRouteSequence;// 抄表顺序号
	private String accountId;// 客户编号
	private String entityName;// 客户名称
	private String customerClass;// 客户类型编码
	private String cmCustClDescr;// 客户类型描述
	private String cmMrAddress;// 完整地址
	private String cmMrDistrict;// 县（区）
	private String cmMrStreet;// 街道
	private String cmMrCommunity;// 小区
	private String cmMrBuilding;// 楼栋
	private String cmMrUnit;// 单元
	private String cmMrRoomNum;// 房间号
	private String spMeterHistoryId;// 服务点/计量表历史记录标识
	private String meterConfigurationId;// 计量表配置编号
	private String cmMrMtrBarCode;// 计量表条码值
	private String fullScale;// 计量表最大值
	private String cmMrAvgVol;// 月均气量
	private String rateSchedule;// 气价编码
	private String cmRsDescr;// 气价描述
	private String cmMrLastBal;// 欠费金额（正为欠费，负为余额）
	private String cmMrOverdueAmt;// 滞纳金
	private String cmMrDebtStatDt;// 欠费统计日期
	private String cmMrLastMrDttm;// 上期抄表日期
	private String readType;// 上期读数类型
	private String cmMrLastMr;// 上期读数
	private String cmMrLastVol;// 上期气量
	private String cmMrLastDebt;// 上期气费
	private String cmMrLastSecchkDt;// 上次安检日期
	private String cmMrRemark;// 备注
	private String cmMrState;// 标识
	private String cmMrDate;// 保存时间、上传时间
	private List<PerPhone> perPhones;// 电话

	public String getCmMrState() {
		return cmMrState;
	}

	public void setCmMrState(String cmMrState) {
		this.cmMrState = cmMrState;
	}

	public String getCmMrDate() {
		return cmMrDate;
	}

	public void setCmMrDate(String cmMrDate) {
		this.cmMrDate = cmMrDate;
	}


	public String getSchedInfoID() {
		return schedInfoID;
	}

	public void setSchedInfoID(String schedInfoID) {
		this.schedInfoID = schedInfoID;
	}

	public String getMeterReadCycleRouteSequence() {
		return meterReadCycleRouteSequence;
	}

	public void setMeterReadCycleRouteSequence(
			String meterReadCycleRouteSequence) {
		this.meterReadCycleRouteSequence = meterReadCycleRouteSequence;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getCustomerClass() {
		return customerClass;
	}

	public void setCustomerClass(String customerClass) {
		this.customerClass = customerClass;
	}

	public String getCmCustClDescr() {
		return cmCustClDescr;
	}

	public void setCmCustClDescr(String cmCustClDescr) {
		this.cmCustClDescr = cmCustClDescr;
	}

	public String getCmMrAddress() {
		return cmMrAddress;
	}

	public void setCmMrAddress(String cmMrAddress) {
		this.cmMrAddress = cmMrAddress;
	}

	public String getCmMrDistrict() {
		return cmMrDistrict;
	}

	public void setCmMrDistrict(String cmMrDistrict) {
		this.cmMrDistrict = cmMrDistrict;
	}

	public String getCmMrStreet() {
		return cmMrStreet;
	}

	public void setCmMrStreet(String cmMrStreet) {
		this.cmMrStreet = cmMrStreet;
	}

	public String getCmMrCommunity() {
		return cmMrCommunity;
	}

	public void setCmMrCommunity(String cmMrCommunity) {
		this.cmMrCommunity = cmMrCommunity;
	}

	public String getCmMrBuilding() {
		return cmMrBuilding;
	}

	public void setCmMrBuilding(String cmMrBuilding) {
		this.cmMrBuilding = cmMrBuilding;
	}

	public String getCmMrUnit() {
		return cmMrUnit;
	}

	public void setCmMrUnit(String cmMrUnit) {
		this.cmMrUnit = cmMrUnit;
	}

	public String getCmMrRoomNum() {
		return cmMrRoomNum;
	}

	public void setCmMrRoomNum(String cmMrRoomNum) {
		this.cmMrRoomNum = cmMrRoomNum;
	}

	public String getSpMeterHistoryId() {
		return spMeterHistoryId;
	}

	public void setSpMeterHistoryId(String spMeterHistoryId) {
		this.spMeterHistoryId = spMeterHistoryId;
	}

	public String getMeterConfigurationId() {
		return meterConfigurationId;
	}

	public void setMeterConfigurationId(String meterConfigurationId) {
		this.meterConfigurationId = meterConfigurationId;
	}

	public String getCmMrMtrBarCode() {
		return cmMrMtrBarCode;
	}

	public void setCmMrMtrBarCode(String cmMrMtrBarCode) {
		this.cmMrMtrBarCode = cmMrMtrBarCode;
	}

	public String getFullScale() {
		return fullScale;
	}

	public void setFullScale(String fullScale) {
		this.fullScale = fullScale;
	}

	public String getCmMrAvgVol() {
		return cmMrAvgVol;
	}

	public void setCmMrAvgVol(String cmMrAvgVol) {
		this.cmMrAvgVol = cmMrAvgVol;
	}

	public String getRateSchedule() {
		return rateSchedule;
	}

	public void setRateSchedule(String rateSchedule) {
		this.rateSchedule = rateSchedule;
	}

	public String getCmRsDescr() {
		return cmRsDescr;
	}

	public void setCmRsDescr(String cmRsDescr) {
		this.cmRsDescr = cmRsDescr;
	}

	public String getCmMrLastBal() {
		return cmMrLastBal;
	}

	public void setCmMrLastBal(String cmMrLastBal) {
		this.cmMrLastBal = cmMrLastBal;
	}

	public String getCmMrOverdueAmt() {
		return cmMrOverdueAmt;
	}

	public void setCmMrOverdueAmt(String cmMrOverdueAmt) {
		this.cmMrOverdueAmt = cmMrOverdueAmt;
	}

	public String getCmMrDebtStatDt() {
		return cmMrDebtStatDt;
	}

	public void setCmMrDebtStatDt(String cmMrDebtStatDt) {
		this.cmMrDebtStatDt = cmMrDebtStatDt;
	}

	public String getCmMrLastMrDttm() {
		return cmMrLastMrDttm;
	}

	public void setCmMrLastMrDttm(String cmMrLastMrDttm) {
		this.cmMrLastMrDttm = cmMrLastMrDttm;
	}

	public String getReadType() {
		return readType;
	}

	public void setReadType(String readType) {
		this.readType = readType;
	}

	public String getCmMrLastMr() {
		return cmMrLastMr;
	}

	public void setCmMrLastMr(String cmMrLastMr) {
		this.cmMrLastMr = cmMrLastMr;
	}

	public String getCmMrLastVol() {
		return cmMrLastVol;
	}

	public void setCmMrLastVol(String cmMrLastVol) {
		this.cmMrLastVol = cmMrLastVol;
	}

	public String getCmMrLastDebt() {
		return cmMrLastDebt;
	}

	public void setCmMrLastDebt(String cmMrLastDebt) {
		this.cmMrLastDebt = cmMrLastDebt;
	}

	public String getCmMrLastSecchkDt() {
		return cmMrLastSecchkDt;
	}

	public void setCmMrLastSecchkDt(String cmMrLastSecchkDt) {
		this.cmMrLastSecchkDt = cmMrLastSecchkDt;
	}

	public String getCmMrRemark() {
		return cmMrRemark;
	}

	public void setCmMrRemark(String cmMrRemark) {
		this.cmMrRemark = cmMrRemark;
	}

	public List<PerPhone> getPerPhones() {
		return perPhones;
	}

	public void setPerPhones(List<PerPhone> perPhones) {
		this.perPhones = perPhones;
	}

}
