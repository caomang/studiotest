package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean for search condition
 * it contain attributes exp:dropDownList,text input
 * @author terry.C
 *
 */
public class SearchCondition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<UIDropSelect> selects = new ArrayList<UIDropSelect>();
	public List<UIDropOrderBy> orderBys = new ArrayList<UIDropOrderBy>();
	public List<UITextInput> inputs = new ArrayList<UITextInput>();
	public List<UIDate> dates = new ArrayList<UIDate>();
	
	public class UIDate implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 0x232L;
		public String dateName;
		public String required;
		public String dateTitle;
		public String dateType;
		public String dateValue;
	}
	public class UIDropSelect implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;
		public String required;
		public String title;
		//added
		public String value;
		public String alt;
		public String refresh;
		public List<UIDropOption> options = new ArrayList<UIDropOption>();
		
	}
	
	public class UIDropOption implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;
		public String value;
		public String checked;
	}
	
	public class UIDropOrderBy implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;
		public String required;
		public String title;
		public List<UIDropOption> options = new ArrayList<UIDropOption>();
	}
	
	public class UITextInput implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String title;
		public String type;
		public String checked;
		public String value;
		public String name;
		public String required;
	}
}
