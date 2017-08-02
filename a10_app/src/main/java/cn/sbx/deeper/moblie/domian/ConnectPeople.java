package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

/**
 * OA邮件联系人实体
 * 
 * @author terry.C
 * 
 */
public class ConnectPeople implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;

	public String fullname;

	public String altname;

	public String mobile;

	public String online;

	public String company;

	public String department;

	public String officer;
	public String phone;
	public String email;
	public String fax;

	public String userId;// 联系人id

	public String fullName;// 联系人email

	public String altName;// 联系人中文名

	public String departmentId;// 联系人所在部门

	@Override
	public String toString() {
		return "ConnectPeople [userId=" + userId + ", fullName=" + fullName
				+ ", altName=" + altName + ", departmentId=" + departmentId
				+ "]";
	}

}
