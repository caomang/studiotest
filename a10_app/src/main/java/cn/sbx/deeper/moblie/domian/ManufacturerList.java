package cn.sbx.deeper.moblie.domian;

import java.util.ArrayList;

public class ManufacturerList {

	public String manufacturer;
	public String manufacturerDescr;
	public ArrayList<ModelInfo> modelInfo;
	
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getManufacturerDescr() {
		return manufacturerDescr;
	}
	public void setManufacturerDescr(String manufacturerDescr) {
		this.manufacturerDescr = manufacturerDescr;
	}
	public ArrayList<ModelInfo> getModelInfo() {
		return modelInfo;
	}
	public void setModelInfo(ArrayList<ModelInfo> modelInfo) {
		this.modelInfo = modelInfo;
	}
	
}
