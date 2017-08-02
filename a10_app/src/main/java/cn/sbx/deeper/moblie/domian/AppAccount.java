package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class AppAccount implements Serializable{

	private String typeNo_key;
	private String typeNo_value;
	private String typeName_key;
	private String typeName_value;
	private String name_name;
	private String name_value;
	private String password_name;
	private String password_value;
	private int useMain_name;
	private int visible = 0;
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public int getUseMain_name() {
		return useMain_name;
	}
	public void setUseMain_name(int useMain_name) {
		this.useMain_name = useMain_name;
	}
	public int getUseMain_value() {
		return useMain_value;
	}
	public void setUseMain_value(int useMain_value) {
		this.useMain_value = useMain_value;
	}
	private int useMain_value;
	public String getTypeNo_key() {
		return typeNo_key;
	}
	public void setTypeNo_key(String typeNo_key) {
		this.typeNo_key = typeNo_key;
	}
	public String getTypeNo_value() {
		return typeNo_value;
	}
	public void setTypeNo_value(String typeNo_value) {
		this.typeNo_value = typeNo_value;
	}
	public String getTypeName_key() {
		return typeName_key;
	}
	public void setTypeName_key(String typeName_key) {
		this.typeName_key = typeName_key;
	}
	public String getTypeName_value() {
		return typeName_value;
	}
	public void setTypeName_value(String typeName_value) {
		this.typeName_value = typeName_value;
	}
	public String getName_name() {
		return name_name;
	}
	public void setName_name(String name_name) {
		this.name_name = name_name;
	}
	public String getName_value() {
		return name_value;
	}
	public void setName_value(String name_value) {
		this.name_value = name_value;
	}
	public String getPassword_name() {
		return password_name;
	}
	public void setPassword_name(String password_name) {
		this.password_name = password_name;
	}
	public String getPassword_value() {
		return password_value;
	}
	public void setPassword_value(String password_value) {
		this.password_value = password_value;
	}
	@Override
	public String toString() {
		return "AppAccount [typeNo_key=" + typeNo_key + ", typeNo_value="
				+ typeNo_value + ", typeName_key=" + typeName_key
				+ ", typeName_value=" + typeName_value + ", name_name="
				+ name_name + ", name_value=" + name_value + ", password_name="
				+ password_name + ", password_value=" + password_value + "]";
	}
	
	
}
