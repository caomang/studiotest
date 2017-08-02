package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class LocucationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String DictionaryCode;
	private String TypeCode;
	private String Name;
	private String Description;
	private String AttachField1;

	public String getDictionaryCode() {
		return DictionaryCode;
	}

	public void setDictionaryCode(String dictionaryCode) {
		DictionaryCode = dictionaryCode;
	}

	public String getTypeCode() {
		return TypeCode;
	}

	public void setTypeCode(String typeCode) {
		TypeCode = typeCode;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getAttachField1() {
		return AttachField1;
	}

	public void setAttachField1(String attachField1) {
		AttachField1 = attachField1;
	}
}
