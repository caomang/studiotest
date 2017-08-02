package cn.sbx.deeper.moblie.domian;

import java.util.List;


public class TaskView {
	public String itemId;
	public String subTitle;
	public String title;
	public String priority;
	public String confidential;
	public List<Row> rows;
	public List<Router> routers;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getConfidential() {
		return confidential;
	}
	public void setConfidential(String confidential) {
		this.confidential = confidential;
	}
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	public List<Router> getRouters() {
		return routers;
	}
	public void setRouters(List<Router> routers) {
		this.routers = routers;
	}
	@Override
	public String toString() {
		return "TaskView [itemId=" + itemId + ", subTitle=" + subTitle
				+ ", title=" + title + ", priority=" + priority
				+ ", confidential=" + confidential + ", rows=" + rows
				+ ", routers=" + routers + "]";
	}
	
	
	
}
