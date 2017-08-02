package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactDepartment implements Serializable{
	private static final long serialVersionUID = 0x3389L;
	public String id = "";//id属性，用于申请下一级部门的列表
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String name = "";
	public String subsector = "1";//subsector为下一级部门识别，1为存在，0为不存在；1
	public String haspersons = "";//haspersons为部门人员，1为存在，0为不存在
	public String personsid = "";//申请人员根据personsid申请
	public String parentid = "";
	
	public ContactDepartment parentDepartment;
	public ArrayList<Employee> employees = new ArrayList<Employee>();
	public ArrayList<ContactDepartment> subDepts = new ArrayList<ContactDepartment>();
}
