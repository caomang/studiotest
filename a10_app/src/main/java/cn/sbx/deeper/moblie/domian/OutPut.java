package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  
 * 
 *
 */
public class OutPut implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String respCode;
	public String respDescr;
//	抄表计划任务
	public List<SchedInfo> schedInfos;
	//	数据字典标记事件
	public String dataDicVersion;
	// 数据字典
	public ArrayList<CmMrCommInfo_Dic> cmMrCommInfo;
	public ArrayList<CmScAjrhInfo_Dic> cmScAjrhInfo;
	public ArrayList<CmScBusiTypeInfo_Dic> cmScBusiTypeInfo;
	public ArrayList<CmScCnlPffsInfo> cmScCnlPffsInfo;
	public ArrayList<CmScLgfmCzInfo> cmScLgfmCzInfo;
	public ArrayList<CmScLgfmWzInfo> cmScLgfmWzInfo;
	public ArrayList<CmScLjgCzInfo> cmScLjgCzInfo;
	public ArrayList<CmScResTypeInfo> cmScResTypeInfo;
	public ArrayList<CmScRsqPffsInfo> cmScRsqPffsInfo;
	
//	public ArrayList<CmScShTypeInfo> cmScShTypeInfo; 
//	public ArrayList<ManufacturerInfo> manufacturerInfo; //单独解析
	
	
	
	public ArrayList<CmScSpItemInfo> cmScSpItemInfo;
	public ArrayList<CmScTypeInfo> cmScTypeInfo;
	public ArrayList<CmScUserTypeInfo> cmScUserTypeInfo;
	public ArrayList<CmScYhzgInfo> cmScYhzgInfo;
	public ArrayList<CmScZjXhbhInfo> cmScZjXhbhInfo;
	public ArrayList<CmScZjYsInfo> cmScZjYsInfo;

	
	public ArrayList<MeterTypeInfo> meterTypeInfo;
	public ArrayList<PhoneTypeInfo> phoneTypeInfo;
	public ArrayList<ReadTypeInfo> readTypeInfo;
//	安检计划任务
	public ArrayList<SchedInfoResidents> schedInfoResidents;


	// 后加
	public String sysDttm; //时间






	public String getDataDicVersion() {
		return dataDicVersion;
	}

	public void setDataDicVersion(String dataDicVersion) {
		this.dataDicVersion = dataDicVersion;
	}
	
	public String getRespCode() {
		return respCode;
	}


	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}


	public String getRespDescr() {
		return respDescr;
	}


	public void setRespDescr(String respDescr) {
		this.respDescr = respDescr;
	}


	public List<SchedInfo> getSchedInfos() {
		return schedInfos;
	}


	public void setSchedInfos(List<SchedInfo> schedInfos) {
		this.schedInfos = schedInfos;
	}


	public ArrayList<CmMrCommInfo_Dic> getCmMrCommInfo() {
		return cmMrCommInfo;
	}


	public void setCmMrCommInfo(ArrayList<CmMrCommInfo_Dic> cmMrCommInfo) {
		this.cmMrCommInfo = cmMrCommInfo;
	}


	public ArrayList<CmScAjrhInfo_Dic> getCmScAjrhInfo() {
		return cmScAjrhInfo;
	}


	public void setCmScAjrhInfo(ArrayList<CmScAjrhInfo_Dic> cmScAjrhInfo) {
		this.cmScAjrhInfo = cmScAjrhInfo;
	}


	public ArrayList<CmScBusiTypeInfo_Dic> getCmScBusiTypeInfo() {
		return cmScBusiTypeInfo;
	}


	public void setCmScBusiTypeInfo(ArrayList<CmScBusiTypeInfo_Dic> cmScBusiTypeInfo) {
		this.cmScBusiTypeInfo = cmScBusiTypeInfo;
	}


	public ArrayList<CmScCnlPffsInfo> getCmScCnlPffsInfo() {
		return cmScCnlPffsInfo;
	}


	public void setCmScCnlPffsInfo(ArrayList<CmScCnlPffsInfo> cmScCnlPffsInfo) {
		this.cmScCnlPffsInfo = cmScCnlPffsInfo;
	}


	public ArrayList<CmScLgfmCzInfo> getCmScLgfmCzInfo() {
		return cmScLgfmCzInfo;
	}


	public void setCmScLgfmCzInfo(ArrayList<CmScLgfmCzInfo> cmScLgfmCzInfo) {
		this.cmScLgfmCzInfo = cmScLgfmCzInfo;
	}


	public ArrayList<CmScLgfmWzInfo> getCmScLgfmWzInfo() {
		return cmScLgfmWzInfo;
	}


	public void setCmScLgfmWzInfo(ArrayList<CmScLgfmWzInfo> cmScLgfmWzInfo) {
		this.cmScLgfmWzInfo = cmScLgfmWzInfo;
	}


	public ArrayList<CmScLjgCzInfo> getCmScLjgCzInfo() {
		return cmScLjgCzInfo;
	}


	public void setCmScLjgCzInfo(ArrayList<CmScLjgCzInfo> cmScLjgCzInfo) {
		this.cmScLjgCzInfo = cmScLjgCzInfo;
	}


	public ArrayList<CmScResTypeInfo> getCmScResTypeInfo() {
		return cmScResTypeInfo;
	}


	public void setCmScResTypeInfo(ArrayList<CmScResTypeInfo> cmScResTypeInfo) {
		this.cmScResTypeInfo = cmScResTypeInfo;
	}


	public ArrayList<CmScRsqPffsInfo> getCmScRsqPffsInfo() {
		return cmScRsqPffsInfo;
	}


	public void setCmScRsqPffsInfo(ArrayList<CmScRsqPffsInfo> cmScRsqPffsInfo) {
		this.cmScRsqPffsInfo = cmScRsqPffsInfo;
	}


	public ArrayList<CmScSpItemInfo> getCmScSpItemInfo() {
		return cmScSpItemInfo;
	}


	public void setCmScSpItemInfo(ArrayList<CmScSpItemInfo> cmScSpItemInfo) {
		this.cmScSpItemInfo = cmScSpItemInfo;
	}


	public ArrayList<CmScUserTypeInfo> getCmScUserTypeInfo() {
		return cmScUserTypeInfo;
	}


	public void setCmScUserTypeInfo(ArrayList<CmScUserTypeInfo> cmScUserTypeInfo) {
		this.cmScUserTypeInfo = cmScUserTypeInfo;
	}


	public ArrayList<CmScYhzgInfo> getCmScYhzgInfo() {
		return cmScYhzgInfo;
	}


	public void setCmScYhzgInfo(ArrayList<CmScYhzgInfo> cmScYhzgInfo) {
		this.cmScYhzgInfo = cmScYhzgInfo;
	}


	public ArrayList<CmScZjXhbhInfo> getCmScZjXhbhInfo() {
		return cmScZjXhbhInfo;
	}


	public void setCmScZjXhbhInfo(ArrayList<CmScZjXhbhInfo> cmScZjXhbhInfo) {
		this.cmScZjXhbhInfo = cmScZjXhbhInfo;
	}


	public ArrayList<CmScZjYsInfo> getCmScZjYsInfo() {
		return cmScZjYsInfo;
	}


	public void setCmScZjYsInfo(ArrayList<CmScZjYsInfo> cmScZjYsInfo) {
		this.cmScZjYsInfo = cmScZjYsInfo;
	}


	public ArrayList<MeterTypeInfo> getMeterTypeInfo() {
		return meterTypeInfo;
	}


	public void setMeterTypeInfo(ArrayList<MeterTypeInfo> meterTypeInfo) {
		this.meterTypeInfo = meterTypeInfo;
	}


	public ArrayList<PhoneTypeInfo> getPhoneTypeInfo() {
		return phoneTypeInfo;
	}


	public void setPhoneTypeInfo(ArrayList<PhoneTypeInfo> phoneTypeInfo) {
		this.phoneTypeInfo = phoneTypeInfo;
	}


	public ArrayList<ReadTypeInfo> getReadTypeInfo() {
		return readTypeInfo;
	}


	public void setReadTypeInfo(ArrayList<ReadTypeInfo> readTypeInfo) {
		this.readTypeInfo = readTypeInfo;
	}


	public String getSysDttm() {
		return sysDttm;
	}


	public void setSysDttm(String sysDttm) {
		this.sysDttm = sysDttm;
	}

	public ArrayList<SchedInfoResidents> getSchedInfoResidents() {
		return schedInfoResidents;
	}


	public void setSchedInfoResidents(
			ArrayList<SchedInfoResidents> schedInfoResidents) {
		this.schedInfoResidents = schedInfoResidents;
	}

}
