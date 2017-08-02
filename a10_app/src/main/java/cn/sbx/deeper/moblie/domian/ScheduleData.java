package cn.sbx.deeper.moblie.domian;

/**
 * 日程类
 * 
 * @author 王克
 * 
 */
public class ScheduleData {

	private String itemID;// 日程id
	private String Title;// 日程标题
	private String StartTime;// 日程开始时间
	private String Endtime;// 日程结束时间
	private String localtion;// 日程地点
	private String creator;// 日程创建者
	private String organizer;// 日程主办处室
	private String scope;// 日程参加范围

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndtime() {
		return Endtime;
	}

	public void setEndtime(String endtime) {
		Endtime = endtime;
	}

	public String getLocaltion() {
		return localtion;
	}

	public void setLocaltion(String localtion) {
		this.localtion = localtion;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

}
