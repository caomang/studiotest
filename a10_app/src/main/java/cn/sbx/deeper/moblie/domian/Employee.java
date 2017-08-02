package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Employee implements Serializable {
	private static final long serialVersionUID = 0x88321L;
	public String workPhone = "";
	
	public String id="";
	public String name = "";
	public String userName = "";
	public String company = "";
	public String department = "";
	public ContactDepartment dept;
	public String position = "";
	public String mobileNo = "";
	public String officeNo = "";
	public String faxNo = "";
	public String email = "";
	public String onLine = "";
	public String phone = "";
	public boolean selected = false;
	public boolean viewing;
	public boolean defaultChecked = false;
	public String deptName="";
	public boolean exists = false;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((dept == null) ? 0 : dept.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((faxNo == null) ? 0 : faxNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mobileNo == null) ? 0 : mobileNo.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((officeNo == null) ? 0 : officeNo.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (dept == null) {
			if (other.dept != null)
				return false;
		} else if (!dept.equals(other.dept))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (faxNo == null) {
			if (other.faxNo != null)
				return false;
		} else if (!faxNo.equals(other.faxNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mobileNo == null) {
			if (other.mobileNo != null)
				return false;
		} else if (!mobileNo.equals(other.mobileNo))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (officeNo == null) {
			if (other.officeNo != null)
				return false;
		} else if (!officeNo.equals(other.officeNo))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	// 新的员工信息参数

	private List<Row> listRows = new ArrayList<Row>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Row> getListRows() {
		return listRows;
	}

	public void setListRows(List<Row> listRows) {
		this.listRows = listRows;
	}

	public class Row implements Serializable {

		private static final long serialVersionUID = 1L;
		private String label = "";
		private String text = "";
		private String category = "";

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

	}

	// ContactPeopleAll
	public String workMobile;
	public String idString;
	public String fullName;
	public String altName;
	public String mobile;
	public String online;
	public String officer;
	public String fax;

	// contactPeople
	public String groupId;
	public String homePhone;
	public String telephone;
	public String otherPhone1;
	public String otherPhone2;
	
}
