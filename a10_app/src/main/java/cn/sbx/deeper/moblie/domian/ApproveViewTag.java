package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.Comparator;

public class ApproveViewTag implements Serializable, Comparator<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;
	public String title;

	public String parent;
	public String submitflag_tag;// submit用
	public String required_tag;//
	public String type;
	public String name;
	public String value;
	public String message;
	public String checked;//
	public String node;

	public ApproveViewTag() {

	}

	public ApproveViewTag(String parent0,String submitflag0, String required0, String type0, String name0, String value0, String message0,String checked0,String node0) {
		parent = parent0;
		submitflag_tag = submitflag0;
		required_tag = required0;
		type = type0;
		name = name0;
		value = value0;
		message = message0;
		checked = checked0;
		node = node0;
	}

	@Override
	public int compare(String object1, String object2) {
		// TODO Auto-generated method stub
		return object1.compareTo(object2);
	}

}
