package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.List;
/**
 * Notice
 *
 */
public class NoticeDetail implements Serializable{
	public Integer id;
	public String key1;
	public String publishDate;
	public String title;
	public String content;
	public String publisher;
	public String author;
	public String newContents;
	//new added
	public String viewType;
	public String url;
	public String videoUrl;
	public String fileUrl;
//	public String dateTitle;
//	public String dateName;
//	public String dateValue;
//	public String dateType;
	public String layout = "";
	
	public SearchCondition searchCondition = new SearchCondition();//新增搜索条件
	
	
	public String getNewContent() {
		return newContents;
	}
	public void setNewContent(String newContents) {
		this.newContents = newContents;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<MeetingPdf> getImgUrls() {
		return imgUrls;
	}
	public void setImgUrls(List<MeetingPdf> imgUrls) {
		this.imgUrls = imgUrls;
	}
	
	public List<MeetingPdf> imgUrls;
	
	
	public List<Attachment> attachment_list;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey1() {
		return key1;
	}
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public List<Attachment> getAttachment_list() {
		return attachment_list;
	}
	public void setAttachment_list(List<Attachment> attachment_list) {
		this.attachment_list = attachment_list;
	}
	
	
	


}
