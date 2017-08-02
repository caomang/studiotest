package cn.sbx.deeper.moblie.domian;

import java.util.ArrayList;
import java.util.List;

public class UploadcustInfo {
	private String meterConfigurationId;
	private String cmMr;
	private String readType;
	private String cmMrDttm;
	private String cmMrRefVol;
	private String cmMrRefDebt;
	private String cmMrNotiPrtd;
	private String cmMrCommCd;
	private String cmMrRemark;
	private String cmMrState;
	private String cmMrDate;

	List<PerPhone> PerPhone_list = new ArrayList<PerPhone>();
	
	public List<PerPhone> getPerPhone_list() {
		return PerPhone_list;
	}

	private String spMeterHistoryId;

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

	public String getCmMr() {
		return cmMr;
	}

	public void setCmMr(String cmMr) {
		this.cmMr = cmMr;
	}

	public String getReadType() {
		return readType;
	}

	public void setReadType(String readType) {
		this.readType = readType;
	}

	public String getCmMrDttm() {
		return cmMrDttm;
	}

	public void setCmMrDttm(String cmMrDttm) {
		this.cmMrDttm = cmMrDttm;
	}

	public String getCmMrRefVol() {
		return cmMrRefVol;
	}

	public void setCmMrRefVol(String cmMrRefVol) {
		this.cmMrRefVol = cmMrRefVol;
	}

	public String getCmMrRefDebt() {
		return cmMrRefDebt;
	}

	public void setCmMrRefDebt(String cmMrRefDebt) {
		this.cmMrRefDebt = cmMrRefDebt;
	}

	public String getCmMrNotiPrtd() {
		return cmMrNotiPrtd;
	}

	public void setCmMrNotiPrtd(String cmMrNotiPrtd) {
		this.cmMrNotiPrtd = cmMrNotiPrtd;
	}

	public String getCmMrCommCd() {
		return cmMrCommCd;
	}

	public void setCmMrCommCd(String cmMrCommCd) {
		this.cmMrCommCd = cmMrCommCd;
	}

	public String getCmMrRemark() {
		return cmMrRemark;
	}

	public void setCmMrRemark(String cmMrRemark) {
		this.cmMrRemark = cmMrRemark;
	}

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

}
