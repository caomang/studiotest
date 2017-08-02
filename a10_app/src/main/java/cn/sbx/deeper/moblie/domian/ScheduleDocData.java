package cn.sbx.deeper.moblie.domian;

/**
 * 日程文档类
 * 
 * @author 王克
 * 
 */
public class ScheduleDocData {

	private String Id;// 日程文档id
	private String Title;// 日程文档标题
	private String FileUri;// 日程文档地址
	private String CategoryId;// 日程文档分类id
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getFileUri() {
		return FileUri;
	}
	public void setFileUri(String fileUri) {
		FileUri = fileUri;
	}
	public String getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

}
