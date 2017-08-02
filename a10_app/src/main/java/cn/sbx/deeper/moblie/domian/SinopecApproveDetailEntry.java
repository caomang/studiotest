package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean for approve detail element
 * 
 * @author terry.C
 * 
 */
public class SinopecApproveDetailEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SinopecForm form;

	public SinopecOperater operater;
	public Other other;
	public String message;
	public boolean submitFlag;

	public class Other implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String currNode;
		public String displayDevice;
		public String deviceResult;
		
		
	}

	public class SinopecForm implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public List<SinopecTable> sinopecTables = new ArrayList<SinopecTable>();
	}

	public class SinopecOperater implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public List<SinopecTable> sinopecTables = new ArrayList<SinopecTable>();

	}

	public class SinopecTable implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String name;
		public String expand;
		public String EditField;
		public String columns;
		public String Type;
		public String title;
		public List<SinopecTR> trs = new ArrayList<SinopecTR>();

	}

	public class SinopecTR implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public List<SinopecTD> tds = new ArrayList<SinopecTD>();
		public String parent;
	}

	public class SinopecTD implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String content = "";
		public String submitflag;// submit用
		public List<Object> sinopecViews = new ArrayList<Object>();
	}

	public class UIInput implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String type;//
		public String name;//
		public String value;//
		public String title;//
		public String required;//
		public String checked;//
		public String maxlength;//
		public String minlength;//
		public String regex;//
		public String message;//
		public String alt;//
		public String submitflag;// submit用
		public String parent;
		public int index; // 表示所在第几个table里面
		public ApproveViewTag tag; // 详细界面用到
		public String closevalue;// 驳回
		public String regexmessage;
	}

	public class UICommonphrase implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String title;
		public String name;
		public String value;
		public String required;
		public String id;
		public String alt;
		public String type;
		public String maxlength;
		public String minlength;
		public String regex;
		public String message;
		public String submitflag;
		public String mDefault;
		public String custom;
		public String parent;
		public int index;
		public ApproveViewTag tag;





		public String closevalue;// 驳回
	}

	public class UISubtitle implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String title;
		public String titlecolor;
		public String backgroudcolor;
		public String fontsize;
		// public String name;
		// public String value;
		// public String required;
		// public String id;
		// public String alt;
		// public String type;
		// public String maxlength;
		// public String minlength;
		// public String regex;
		// public String message;
		// public String submitflag;
		// public String mDefault;
		// public String custom;
		public String parent;
		public int index;
		public ApproveViewTag tag;
		public String closevalue;// 驳回
	}

	public class UIUser implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String name;//
		public String value;
		public String required;
		public String alt;
		public String type;
		public String mDefault;
		public String filter;
		public String multi;
		public String title;//
		public String message;
		public String isreplace; // 是否移除
		public String userchoice; // true 表示只取人员id，false 只取部门id
		public String submitflag;// submit用
		public String parent;
		public List<UIOption> options = new ArrayList<UIOption>();
		public int index; // 表示所在第几个table里面
		public ApproveViewTag tag;
		public String closevalue;// 驳回

		public String link;
	}

	public class UISelect implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;//
		public String value;
		public String required;
		public String alt;
		public String title;//
		public List<UIOption> options = new ArrayList<UIOption>();
		public ApproveViewTag tag;
		public String submitflag;
		public String Message;
		public String type;
	}

	public class UIOption implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;
		public String value;
		public String checked;
		public String readonly;
		public String mobile;
		public String linkmessage;
		public String batchselect;
		public List<UIOption> options = new ArrayList<UIOption>();

	}

	public class UIDate implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String name;
		public String value;
		public String type;
		public String Today;
		public String compareTo;
		public String Than;
		public String Message;
		public String regex;
		public String timespan;
		public String submitflag;
		public String current;
		public String required;
		public ApproveViewTag tag; // 详细界面用到

	}

	// 添加treeuser控件

	public class UITreeuser implements Serializable {

		/**
		 * <treeuser name="zhuban" title="主办" value="" required="false" alt=""
		 * id="opers" default="false" multi="true" type="user" filter="1"
		 * message="请选择人员" submitflag="主办" link="showopinion">
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String name;//
		public String value;
		public String required;
		public String alt;
		public String type;
		public String mDefault;
		public String filter;
		public String multi;
		public String title;//
		public String message;
		public String isreplace; // 是否移除
		public String userchoice; // true 表示只取人员id，false 只取部门id
		public String submitflag;// submit用
		public String parent;
		public String link;
		// public List<UIOption> options = new
		// ArrayList<SinopecApproveDetailEntry.UIOption>();
		public int index; // 表示所在第几个table里面
		public ApproveViewTag tag;
		public String closevalue;// 驳回
		public List<UIOption> optionList = new ArrayList<UIOption>();
		// public List<UIOptions> options = new
		// ArrayList<SinopecApproveDetailEntry.UIOptions>();

	}

	// public class UIOptions implements Serializable{
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	// public String name;
	// public String value;
	// public String checked;
	// public String readonly;
	// public String batchselect;
	// public List<UIOption> optionList = new
	// ArrayList<SinopecApproveDetailEntry.UIOption>();
	// public List<UIOptions> optionsList = new
	// ArrayList<SinopecApproveDetailEntry.UIOptions>();
	// }
	public class UIOpinion implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 11L;
		public String title;
		public String name;
		public String submitflag;
		public String message;
		public String alt;
		public String required;
		public String value;
		public String mDefault;
		public String rules;
		public String parent;

		public int index;
		public ApproveViewTag tag;
		public String type;
	}
}
