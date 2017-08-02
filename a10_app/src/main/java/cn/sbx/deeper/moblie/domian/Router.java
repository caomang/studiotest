package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.List;

public class Router implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String captionText;
	private Integer multiple;
	private String action;
	private String peoplePicker;
	public String getPeoplePicker() {
		return peoplePicker;
	}

	public void setPeoplePicker(String peoplePicker) {
		this.peoplePicker = peoplePicker;
	}

	private List<Receiver> receivers;

	private String receiverRequired = "1"; // 接收人是否必须
	private String commentRequired = "1"; // 意见填写是否必须
	private String CommentVisible = "1"; // 意见框是否可见
private String initOuid; //申请组织机构的数据的参数传递
	
	
	public String getInitOuid() {
		return initOuid;
	}

	public void setInitOuid(String initOuid) {
		this.initOuid = initOuid;
	}

	public String getReceiverRequired() {
		return receiverRequired;
	}

	public void setReceiverRequired(String receiverRequired) {
		this.receiverRequired = receiverRequired;
	}

	public String getCommentRequired() {
		return commentRequired;
	}

	public void setCommentRequired(String commentRequired) {
		this.commentRequired = commentRequired;
	}

	public String getCommentVisible() {
		return CommentVisible;
	}

	public void setCommentVisible(String commentVisible) {
		CommentVisible = commentVisible;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaptionText() {
		return captionText;
	}

	public void setCaptionText(String captionText) {
		this.captionText = captionText;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public List<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Router [id=" + id + ", captionText=" + captionText + ", multiple=" + multiple + ", receivers=" + receivers + "]";
	}

}
