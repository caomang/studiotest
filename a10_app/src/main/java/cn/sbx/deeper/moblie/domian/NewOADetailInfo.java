package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewOADetailInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String message;
	private ApprovalDetailTableInfo tableInfo;
	private ApprovalProcessInfo processInfo;
	private SubmitInfo submitInfo;

	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage(){
		return message;
	}
	public ApprovalDetailTableInfo getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(ApprovalDetailTableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	public ApprovalProcessInfo getProcessInfo() {
		return processInfo;
	}

	public void setProcessInfo(ApprovalProcessInfo processInfo) {
		this.processInfo = processInfo;
	}

	public SubmitInfo getSubmitInfo() {
		return submitInfo;
	}

	public void setSubmitInfo(SubmitInfo submitInfo) {
		this.submitInfo = submitInfo;
	}

	// 表格
	public class ApprovalDetailTableInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<String> lists = new ArrayList<String>();
		private List<List<String>> listRows = new ArrayList<List<String>>();

		public List<String> getLists() {
			return lists;
		}

		public void setLists(List<String> lists) {
			this.lists = lists;
		}

		public List<List<String>> getListRows() {
			return listRows;
		}

		public void setListRows(List<List<String>> listRows) {
			this.listRows = listRows;
		}

	}

	// 合同流程
	public class ApprovalProcessInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<ProcessSection> sections = new ArrayList<ProcessSection>();

		public List<ProcessSection> getSections() {
			return sections;
		}

		public void setSections(List<ProcessSection> sections) {
			this.sections = sections;
		}

		// 环节节点
		public class ProcessSection implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String state = "";
			private String caption = "";
			private List<ProcessGroupList> groupLists = new ArrayList<ProcessGroupList>();

			public String getCaption() {
				return caption;
			}

			public void setCaption(String caption) {
				this.caption = caption;
			}

			public String getState() {
				return state;
			}

			public void setState(String state) {
				this.state = state;
			}

			public List<ProcessGroupList> getGroupLists() {
				return groupLists;
			}

			public void setGroupLists(List<ProcessGroupList> groupLists) {
				this.groupLists = groupLists;
			}

			// 组集合
			public class ProcessGroupList implements Serializable {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				List<ProcessGroup> groups = new ArrayList<ProcessGroup>();

				public List<ProcessGroup> getGroups() {
					return groups;
				}

				public void setGroups(List<ProcessGroup> groups) {
					this.groups = groups;
				}

				// 组
				public class ProcessGroup implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					private String name = ""; // 组的名称
					private String expand = ""; // 是否展开
					private List<ProcessGroupItem> groupItems;

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}

					public String getExpand() {
						return expand;
					}

					public void setExpand(String expand) {
						this.expand = expand;
					}

					public List<ProcessGroupItem> getGroupItems() {
						return groupItems;
					}

					public void setGroupItems(List<ProcessGroupItem> groupItems) {
						this.groupItems = groupItems;
					}

					/**
					 * 组的列表项，这些项里面数据格式是<Row><Label>标的</Label><Text></Text></Row>
					 * 不过有的数据格式不是这种标准，比如有时候相对人里面的数据格式是以表格的形式传过来的， 比如
					 * <Table>
					 * <Header><Column
					 * ShowInList="1">相对人名称</Column></Header><Content><Row>
					 * ，这一点需要注意，我的处理方式就是，ProcessGroupItem对象里面再创建一个内部对象
					 * ，放着table里面的数据
					 * 
					 * @author Administrator
					 * 
					 */
					public class ProcessGroupItem implements Serializable {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
						private String label = "";
						private String text = "";
						private String type = "";
						private String pCode = "";
						private boolean ifTableData = false; // 表示是否是表格里面的数据,如果是true，表示是表格里面的数据
						private ProcessGroupItemTableInfo tableInfo = new ProcessGroupItemTableInfo();

						public ProcessGroupItemTableInfo getTableInfo() {
							return tableInfo;
						}

						public void setTableInfo(ProcessGroupItemTableInfo tableInfo) {
							this.tableInfo = tableInfo;
						}

						public boolean isIfTableData() {
							return ifTableData;
						}

						public void setIfTableData(boolean ifTableData) {
							this.ifTableData = ifTableData;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}

						public String getpCode() {
							return pCode;
						}

						public void setpCode(String pCode) {
							this.pCode = pCode;
						}

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

						/**
						 * 这个对象的出现，是因为实在我想不出什么更好的办法，
						 * 
						 * @author Administrator
						 * 
						 */
						public class ProcessGroupItemTableInfo implements Serializable {

							private static final long serialVersionUID = 1L;
							/**
							 * 下面这些字段，也是想不到有什么好的办法，只能用这些list来给他们一一对应上，
							 */
							private List<String> showInlList = new ArrayList<String>(); // 集合里面如果有1，表示这一列需要显示，
							private List<String> headerList = new ArrayList<String>();
							private List<List<String[]>> contentList = new ArrayList<List<String[]>>();

							public List<List<String[]>> getContentList() {
								return contentList;
							}

							public void setContentList(List<List<String[]>> contentList) {
								this.contentList = contentList;
							}

							public List<String> getShowInlList() {
								return showInlList;
							}

							public void setShowInlList(List<String> showInlList) {
								this.showInlList = showInlList;
							}

							public List<String> getHeaderList() {
								return headerList;
							}

							public void setHeaderList(List<String> headerList) {
								this.headerList = headerList;
							}

						}
					}

				}

			}
		}
	}

	// 提交
	public class SubmitInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private SubRouters subRouters = null;
		private SubTextBoard subTextBoard = null;

		public SubRouters getSubRouters() {
			return subRouters;
		}

		public void setSubRouters(SubRouters subRouters) {
			this.subRouters = subRouters;
		}

		public SubTextBoard getSubTextBoard() {
			return subTextBoard;
		}

		public void setSubTextBoard(SubTextBoard subTextBoard) {
			this.subTextBoard = subTextBoard;
		}

		// 操作方式
		public class SubRouters implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String paraName;
			private String Title;
			private List<SubRouter> routerList = new ArrayList<SubRouter>();

			public String getParaName() {
				return paraName;
			}

			public void setParaName(String paraName) {
				this.paraName = paraName;
			}

			public String getTitle() {
				return Title;
			}

			public void setTitle(String title) {
				Title = title;
			}

			public List<SubRouter> getRouterList() {
				return routerList;
			}

			public void setRouterList(List<SubRouter> routerList) {
				this.routerList = routerList;
			}

			public class SubRouter implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				private String key = "";
				private String caption = "";
				private List<SubOption> optionList = new ArrayList<SubOption>();
				private SubTextView subTextView;

				public SubTextView getSubTextView() {
					return subTextView;
				}

				public void setSubTextView(SubTextView subTextView) {
					this.subTextView = subTextView;
				}

				public List<SubOption> getOptionList() {
					return optionList;
				}

				public void setOptionList(List<SubOption> optionList) {
					this.optionList = optionList;
				}

				public String getKey() {
					return key;
				}

				public void setKey(String key) {
					this.key = key;
				}

				public String getCaption() {
					return caption;
				}

				public void setCaption(String caption) {
					this.caption = caption;
				}

				public class SubOption implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					private String multiple = ""; // 是否多选
					private String paraName = ""; // :URL参数名
					private String caption = ""; // 列表的标题
					private String type = ""; // Contacts 选人
					private String required = ""; // 选人是否必须
					private String available = ""; // 为按钮是否可用
					private String initOUID = ""; // 初始化选择人员组织机构ID控制
					private String source = ""; // 表示请求联系人的URL地址
					private String display = ""; // 1 显示 0不显示
					private String IsChoose = ""; // 1表示复选框显示，0表示复选框狂不显示
					private List<SubItem> items = new ArrayList<SubItem>();
					private List<SubItem> filterItems = new ArrayList<SubItem>();

					public String getRequired() {
						return required;
					}

					public void setRequired(String required) {
						this.required = required;
					}

					public String getIsChoose() {
						return IsChoose;
					}

					public void setIsChoose(String isChoose) {
						IsChoose = isChoose;
					}

					public List<SubItem> getFilterItems() {
						return filterItems;
					}

					public void setFilterItems(List<SubItem> filterItems) {
						this.filterItems = filterItems;
					}

					public List<SubItem> getItems() {
						return items;
					}

					public void setItems(List<SubItem> items) {
						this.items = items;
					}

					public String getMultiple() {
						return multiple;
					}

					public void setMultiple(String multiple) {
						this.multiple = multiple;
					}

					public String getParaName() {
						return paraName;
					}

					public void setParaName(String paraName) {
						this.paraName = paraName;
					}

					public String getCaption() {
						return caption;
					}

					public void setCaption(String caption) {
						this.caption = caption;
					}

					public String getType() {
						return type;
					}

					public void setType(String type) {
						this.type = type;
					}

					public String getAvailable() {
						return available;
					}

					public void setAvailable(String available) {
						this.available = available;
					}

					public String getInitOUID() {
						return initOUID;
					}

					public void setInitOUID(String initOUID) {
						this.initOUID = initOUID;
					}

					public String getSource() {
						return source;
					}

					public void setSource(String source) {
						this.source = source;
					}

					public String getDisplay() {
						return display;
					}

					public void setDisplay(String display) {
						this.display = display;
					}

					public class SubItem implements Serializable {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
						private String key = "";
						private String selected = ""; // 默认选中，如果为1，就让他选中
						private String name = "";
						private boolean bool = false; // 用来表示checkbox是否选中
						private int init = 0; // 后面会有数据出现，用这个值来表示是否是新来的值，0表示初始化的值，1表示后来的值

						public int getInit() {
							return init;
						}

						public void setInit(int init) {
							this.init = init;
						}

						public boolean isBool() {
							return bool;
						}

						public void setBool(boolean bool) {
							this.bool = bool;
						}

						public String getKey() {
							return key;
						}

						public void setKey(String key) {
							this.key = key;
						}

						public String getSelected() {
							return selected;
						}

						public void setSelected(String selected) {
							this.selected = selected;
						}

						public String getName() {
							return name;
						}

						public void setName(String name) {
							this.name = name;
						}

					}

				}

				// 审批意见
				public class SubTextView implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					private String paraName = "";
					private String Title = "";
					private String defautValue = "";
					private String display = "";
					private String available = "";
					private String required = "";
					private String resultValue = ""; // 用于从另一个界面返回的值
														// ,用来表示当前界面需要显示的值

					public String getRequired() {
						return required;
					}

					public void setRequired(String required) {
						this.required = required;
					}

					public String getResultValue() {
						return resultValue;
					}

					public void setResultValue(String resultValue) {
						this.resultValue = resultValue;
					}

					public String getParaName() {
						return paraName;
					}

					public void setParaName(String paraName) {
						this.paraName = paraName;
					}

					public String getTitle() {
						return Title;
					}

					public void setTitle(String title) {
						Title = title;
					}

					public String getDefautValue() {
						return defautValue;
					}

					public void setDefautValue(String defautValue) {
						this.defautValue = defautValue;
					}

					public String getDisplay() {
						return display;
					}

					public void setDisplay(String display) {
						this.display = display;
					}

					public String getAvailable() {
						return available;
					}

					public void setAvailable(String available) {
						this.available = available;
					}

				}

			}

		}

		// 环节办理人
		public class SubTextBoard implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String title;
			private List<TextBoardItem> itemList = new ArrayList<TextBoardItem>();

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public List<TextBoardItem> getItemList() {
				return itemList;
			}

			public void setItemList(List<TextBoardItem> itemList) {
				this.itemList = itemList;
			}

			public class TextBoardItem implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				private String title;
				private String subtitle;

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public String getSubtitle() {
					return subtitle;
				}

				public void setSubtitle(String subtitle) {
					this.subtitle = subtitle;
				}
			}

		}
	}
}
