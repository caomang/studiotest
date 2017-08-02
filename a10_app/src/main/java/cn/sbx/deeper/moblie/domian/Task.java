package cn.sbx.deeper.moblie.domian;

public class Task {
	private boolean flag;
	private String id;
	private String title;
	private String requestor;
	private String date;
	private Integer state;
	private String description;
	private String field1;
	private String field2;
	private String field3;
	private String canopen;// 是否可以点击
	private String currNode;// 当前处于的操作
	private String displayDevice;// 是否显示设备号
	private String deviceResult;// 是否显示设备状态

	public String getDeviceResult() {
		return deviceResult;
	}

	public void setDeviceResult(String deviceResult) {
		this.deviceResult = deviceResult;
	}

	public String getCurrNode() {
		return currNode;
	}

	public void setCurrNode(String currNode) {
		this.currNode = currNode;
	}

	public String getDisplayDevice() {
		return displayDevice;
	}

	public void setDisplayDevice(String displayDevice) {
		this.displayDevice = displayDevice;
	}

	/**
	 * state 0 代表代办 1代表已办
	 * 
	 * @return
	 */

	public Integer getState() {
		return state;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCanopen() {
		return canopen;
	}

	public void setCanopen(String canopen) {
		this.canopen = canopen;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", requestor="
				+ requestor + ", date=" + date + ", state=" + state
				+ ",canopen=" + canopen + ",currNode=" + currNode
				+ ",displayDevice=" + displayDevice + "]";
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
