package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustInfo_AnJian implements Serializable {

	public String accountId; 
	public String badgeNumber; 
	public String cmCustClDescr; 
	public String cmMlr; 
	public String cmMrAddress; 
	public String cmMrBuilding; 
	public String cmMrCommunity; 
	public String cmMrDistrict; 
	public String cmMrLastSecchkDt; 
	public String cmMrRoomNum; 
	public String cmMrStreet; 
	public String cmMrUnit; 
	public String cmScAqyh; 
	public String cmScBjqPp; 
	public String cmScBjqSyrq; 
	public String cmScCnlPffs; 
	public String cmScCnlPp; 
	public String cmScCnlSyrq; 
	public String cmScLgfmCz; 
	public String cmScLgfmGj; 
	public String cmScLgfmWz; 
	public String cmScLjgCz; 
	public String cmScOpenDttm; 
	public String cmScResType; 
	public String cmScRsqPffs; 
	public String cmScRsqPp; 
	public String cmScRsqSyrq; 
	public String cmScUserType; 
	public String cmScYhzg; 
	public String cmScZjPp; 
	public String cmScZjSyrq; 
	public String cmScZjXhbh; 
	public String cmScZjYs; 
	public String cmSchedId; 
	public String cmStageDwnStatus; 
	public String customerClass; 
	public String entityName; 
	public String fieldActivityId; 
	public String manufacturer; 
	public String meterConfigurationId; 
	public String meterType; 
	public String model; 
	public String serialNumber; 
	public String servicePointId; 
	public String spType; 
	public String version; 
	public String cmMrMtrBarCode; 
	public String cmScIntvl; 
	public String cmMrState; 
	public ArrayList<PerPhone_Anjian> perPhones;
	public ArrayList<PerSh> perSh;
	
	
	

	public String getCmMrState() {
		return cmMrState;
	}
	public void setCmMrState(String cmMrState) {
		this.cmMrState = cmMrState;
	}
	
	
	public ArrayList<PerPhone_Anjian> getPerPhones() {
		return perPhones;
	}
	public void setPerPhones(List<PerPhone_Anjian> listPerPhone) {
		this.perPhones = (ArrayList<PerPhone_Anjian>) listPerPhone;
	}
	public String getCmScIntvl() {
		return cmScIntvl;
	}
	public void setCmScIntvl(String cmScIntvl) {
		this.cmScIntvl = cmScIntvl;
	}
	public String getCmMrMtrBarCode() {
		return cmMrMtrBarCode;
	}
	public void setCmMrMtrBarCode(String cmMrMtrBarCode) {
		this.cmMrMtrBarCode = cmMrMtrBarCode;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getBadgeNumber() {
		return badgeNumber;
	}
	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}
	public String getCmCustClDescr() {
		return cmCustClDescr;
	}
	public void setCmCustClDescr(String cmCustClDescr) {
		this.cmCustClDescr = cmCustClDescr;
	}
	public String getCmMlr() {
		return cmMlr;
	}
	public void setCmMlr(String cmMlr) {
		this.cmMlr = cmMlr;
	}
	public String getCmMrAddress() {
		return cmMrAddress;
	}
	public void setCmMrAddress(String cmMrAddress) {
		this.cmMrAddress = cmMrAddress;
	}
	public String getCmMrBuilding() {
		return cmMrBuilding;
	}
	public void setCmMrBuilding(String cmMrBuilding) {
		this.cmMrBuilding = cmMrBuilding;
	}
	public String getCmMrCommunity() {
		return cmMrCommunity;
	}
	public void setCmMrCommunity(String cmMrCommunity) {
		this.cmMrCommunity = cmMrCommunity;
	}
	public String getCmMrDistrict() {
		return cmMrDistrict;
	}
	public void setCmMrDistrict(String cmMrDistrict) {
		this.cmMrDistrict = cmMrDistrict;
	}
	public String getCmMrLastSecchkDt() {
		return cmMrLastSecchkDt;
	}
	public void setCmMrLastSecchkDt(String cmMrLastSecchkDt) {
		this.cmMrLastSecchkDt = cmMrLastSecchkDt;
	}
	public String getCmMrRoomNum() {
		return cmMrRoomNum;
	}
	public void setCmMrRoomNum(String cmMrRoomNum) {
		this.cmMrRoomNum = cmMrRoomNum;
	}
	public String getCmMrStreet() {
		return cmMrStreet;
	}
	public void setCmMrStreet(String cmMrStreet) {
		this.cmMrStreet = cmMrStreet;
	}
	public String getCmMrUnit() {
		return cmMrUnit;
	}
	public void setCmMrUnit(String cmMrUnit) {
		this.cmMrUnit = cmMrUnit;
	}
	public String getCmScAqyh() {
		return cmScAqyh;
	}
	public void setCmScAqyh(String cmScAqyh) {
		this.cmScAqyh = cmScAqyh;
	}
	public String getCmScBjqPp() {
		return cmScBjqPp;
	}
	public void setCmScBjqPp(String cmScBjqPp) {
		this.cmScBjqPp = cmScBjqPp;
	}
	public String getCmScBjqSyrq() {
		return cmScBjqSyrq;
	}
	public void setCmScBjqSyrq(String cmScBjqSyrq) {
		this.cmScBjqSyrq = cmScBjqSyrq;
	}
	public String getCmScCnlPffs() {
		return cmScCnlPffs;
	}
	public void setCmScCnlPffs(String cmScCnlPffs) {
		this.cmScCnlPffs = cmScCnlPffs;
	}
	public String getCmScCnlPp() {
		return cmScCnlPp;
	}
	public void setCmScCnlPp(String cmScCnlPp) {
		this.cmScCnlPp = cmScCnlPp;
	}
	public String getCmScCnlSyrq() {
		return cmScCnlSyrq;
	}
	public void setCmScCnlSyrq(String cmScCnlSyrq) {
		this.cmScCnlSyrq = cmScCnlSyrq;
	}
	public String getCmScLgfmCz() {
		return cmScLgfmCz;
	}
	public void setCmScLgfmCz(String cmScLgfmCz) {
		this.cmScLgfmCz = cmScLgfmCz;
	}
	public String getCmScLgfmGj() {
		return cmScLgfmGj;
	}
	public void setCmScLgfmGj(String cmScLgfmGj) {
		this.cmScLgfmGj = cmScLgfmGj;
	}
	public String getCmScLgfmWz() {
		return cmScLgfmWz;
	}
	public void setCmScLgfmWz(String cmScLgfmWz) {
		this.cmScLgfmWz = cmScLgfmWz;
	}
	public String getCmScLjgCz() {
		return cmScLjgCz;
	}
	public void setCmScLjgCz(String cmScLjgCz) {
		this.cmScLjgCz = cmScLjgCz;
	}
	public String getCmScOpenDttm() {
		return cmScOpenDttm;
	}
	public void setCmScOpenDttm(String cmScOpenDttm) {
		this.cmScOpenDttm = cmScOpenDttm;
	}
	public String getCmScResType() {
		return cmScResType;
	}
	public void setCmScResType(String cmScResType) {
		this.cmScResType = cmScResType;
	}
	public String getCmScRsqPffs() {
		return cmScRsqPffs;
	}
	public void setCmScRsqPffs(String cmScRsqPffs) {
		this.cmScRsqPffs = cmScRsqPffs;
	}
	public String getCmScRsqPp() {
		return cmScRsqPp;
	}
	public void setCmScRsqPp(String cmScRsqPp) {
		this.cmScRsqPp = cmScRsqPp;
	}
	public String getCmScRsqSyrq() {
		return cmScRsqSyrq;
	}
	public void setCmScRsqSyrq(String cmScRsqSyrq) {
		this.cmScRsqSyrq = cmScRsqSyrq;
	}
	public String getCmScUserType() {
		return cmScUserType;
	}
	public void setCmScUserType(String cmScUserType) {
		this.cmScUserType = cmScUserType;
	}
	public String getCmScYhzg() {
		return cmScYhzg;
	}
	public void setCmScYhzg(String cmScYhzg) {
		this.cmScYhzg = cmScYhzg;
	}
	public String getCmScZjPp() {
		return cmScZjPp;
	}
	public void setCmScZjPp(String cmScZjPp) {
		this.cmScZjPp = cmScZjPp;
	}
	public String getCmScZjSyrq() {
		return cmScZjSyrq;
	}
	public void setCmScZjSyrq(String cmScZjSyrq) {
		this.cmScZjSyrq = cmScZjSyrq;
	}
	public String getCmScZjXhbh() {
		return cmScZjXhbh;
	}
	public void setCmScZjXhbh(String cmScZjXhbh) {
		this.cmScZjXhbh = cmScZjXhbh;
	}
	public String getCmScZjYs() {
		return cmScZjYs;
	}
	public void setCmScZjYs(String cmScZjYs) {
		this.cmScZjYs = cmScZjYs;
	}
	public String getCmSchedId() {
		return cmSchedId;
	}
	public void setCmSchedId(String cmSchedId) {
		this.cmSchedId = cmSchedId;
	}
	public String getCmStageDwnStatus() {
		return cmStageDwnStatus;
	}
	public void setCmStageDwnStatus(String cmStageDwnStatus) {
		this.cmStageDwnStatus = cmStageDwnStatus;
	}
	public String getCustomerClass() {
		return customerClass;
	}
	public void setCustomerClass(String customerClass) {
		this.customerClass = customerClass;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getFieldActivityId() {
		return fieldActivityId;
	}
	public void setFieldActivityId(String fieldActivityId) {
		this.fieldActivityId = fieldActivityId;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getMeterConfigurationId() {
		return meterConfigurationId;
	}
	public void setMeterConfigurationId(String meterConfigurationId) {
		this.meterConfigurationId = meterConfigurationId;
	}
	public String getMeterType() {
		return meterType;
	}
	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getServicePointId() {
		return servicePointId;
	}
	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}
	public String getSpType() {
		return spType;
	}
	public void setSpType(String spType) {
		this.spType = spType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public ArrayList<PerSh> getPerSh() {
		return perSh;
	}
	public void setPerSh(ArrayList<PerSh> perSh) {
		this.perSh = perSh;
	}
	
	
}
