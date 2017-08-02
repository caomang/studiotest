package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewOAInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String autoSearch = "";
	private String detailStyle = "";
	private ApprovalSearchInfo searchInfo = null;
	private List<AppContentListInfo> contentListInfos = new ArrayList<AppContentListInfo>();

	public ApprovalSearchInfo getSearchInfo() {
		return searchInfo;
	}

	public void setSearchInfo(ApprovalSearchInfo searchInfo) {
		this.searchInfo = searchInfo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<AppContentListInfo> getContentListInfos() {
		return contentListInfos;
	}

	public void setContentListInfos(List<AppContentListInfo> contentListInfos) {
		this.contentListInfos = contentListInfos;
	}

	public String getDetailStyle() {
		return detailStyle;
	}

	public void setDetailStyle(String detailStyle) {
		this.detailStyle = detailStyle;
	}

	public String getAutoSearch() {
		return autoSearch;
	}

	public void setAutoSearch(String autoSearch) {
		this.autoSearch = autoSearch;
	}

	/**
	 * 搜索
	 */
	public class ApprovalSearchInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private List<ApproveTextBox> lisTextBoxs = new ArrayList<ApproveTextBox>();
		private List<ApprovaldropDownList> listDropDownLists = new ArrayList<ApprovaldropDownList>();
		private List<ApproveDatePicker> listDatePickers = new ArrayList<ApproveDatePicker>();
		private List<ApproveContacts> listContacts = new ArrayList<ApproveContacts>();

		public List<ApproveTextBox> getLisTextBoxs() {
			return lisTextBoxs;
		}

		public void setLisTextBoxs(List<ApproveTextBox> lisTextBoxs) {
			this.lisTextBoxs = lisTextBoxs;
		}

		public List<ApprovaldropDownList> getListDropDownLists() {
			return listDropDownLists;
		}

		public void setListDropDownLists(List<ApprovaldropDownList> listDropDownLists) {
			this.listDropDownLists = listDropDownLists;
		}

		public List<ApproveDatePicker> getListDatePickers() {
			return listDatePickers;
		}

		public void setListDatePickers(List<ApproveDatePicker> listDatePickers) {
			this.listDatePickers = listDatePickers;
		}

		public List<ApproveContacts> getListContacts() {
			return listContacts;
		}

		public void setListContacts(List<ApproveContacts> listContacts) {
			this.listContacts = listContacts;
		}

		/**
		 * 
		 * @author minjiewu
		 * @see 下拉列表
		 * 
		 */
		public class ApprovaldropDownList implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String paraName = "";
			private String title = "";
			private String defaultValue = "";
			private String required = "";
			private List<String[]> listItems = new ArrayList<String[]>();

			public List<String[]> getListItems() {
				return listItems;
			}

			public void setListItems(List<String[]> listItems) {
				this.listItems = listItems;
			}

			public String getParaName() {
				return paraName;
			}

			public void setParaName(String paraName) {
				this.paraName = paraName;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getDefaultValue() {
				return defaultValue;
			}

			public void setDefaultValue(String defaultValue) {
				this.defaultValue = defaultValue;
			}

			public String getRequired() {
				return required;
			}

			public void setRequired(String required) {
				this.required = required;
			}

		}

		/**
		 * 
		 * @author minjiewu
		 * @see 时间
		 * 
		 */
		public class ApproveDatePicker implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String type = "";
			private List<ApproveDateItem> dateItems = new ArrayList<ApproveDateItem>();

			public List<ApproveDateItem> getDateItems() {
				return dateItems;
			}

			public void setDateItems(List<ApproveDateItem> dateItems) {
				this.dateItems = dateItems;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public class ApproveDateItem implements Serializable {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				private String type = "";
				private String paraName = "";
				private String title = "";
				private String defaultValue = "";
				private String required = "";

				public String getType() {
					return type;
				}

				public void setType(String type) {
					this.type = type;
				}

				public String getParaName() {
					return paraName;
				}

				public void setParaName(String paraName) {
					this.paraName = paraName;
				}

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public String getDefaultValue() {
					return defaultValue;
				}

				public void setDefaultValue(String defaultValue) {
					this.defaultValue = defaultValue;
				}

				public String getRequired() {
					return required;
				}

				public void setRequired(String required) {
					this.required = required;
				}
			}

		}

		/**
		 * 
		 * @author minjiewu
		 * @see 文本搜索
		 * 
		 */
		public class ApproveTextBox implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String paraName = "";
			private String title = "";
			private String defaultValue = "";
			private String required = "";

			public String getParaName() {
				return paraName;
			}

			public void setParaName(String paraName) {
				this.paraName = paraName;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getDefaultValue() {
				return defaultValue;
			}

			public void setDefaultValue(String defaultValue) {
				this.defaultValue = defaultValue;
			}

			public String getRequired() {
				return required;
			}

			public void setRequired(String required) {
				this.required = required;
			}

		}

		/**
		 * 
		 * @author minjiewu
		 * @see 联系人
		 * 
		 */
		public class ApproveContacts implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String paraName = "";
			private String title = "";
			private String defaultValue = "";
			private String required = "";
			private String sourse = "";

			public String getSourse() {
				return sourse;
			}

			public void setSourse(String sourse) {
				this.sourse = sourse;
			}

			public String getParaName() {
				return paraName;
			}

			public void setParaName(String paraName) {
				this.paraName = paraName;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getDefaultValue() {
				return defaultValue;
			}

			public void setDefaultValue(String defaultValue) {
				this.defaultValue = defaultValue;
			}

			public String getRequired() {
				return required;
			}

			public void setRequired(String required) {
				this.required = required;
			}

		}

	}

	/**
	 * 
	 * @author minjiewu
	 * @see 审批信息列表
	 * 
	 */
	public class AppContentListInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String type = "";
		private String detailStyle = "";
		private String itemID = "";
		private String title = "";
		private String subTitle = "";
		private String addition = "";

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDetailStyle() {
			return detailStyle;
		}

		public void setDetailStyle(String detailStyle) {
			this.detailStyle = detailStyle;
		}

		public String getItemID() {
			return itemID;
		}

		public void setItemID(String itemID) {
			this.itemID = itemID;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSubTitle() {
			return subTitle;
		}

		public void setSubTitle(String subTitle) {
			this.subTitle = subTitle;
		}

		public String getAddition() {
			return addition;
		}

		public void setAddition(String addition) {
			this.addition = addition;
		}
	}
}
