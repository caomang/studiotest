package cn.sbx.deeper.moblie.domian;

public class MobileNewLieb {
	public boolean level;
	public String type;//type=1是普通列表。type=2是附件。type=3是URL视频。type=4是socket视频
	public String id;
	public String title;
	public String date;
	public String author;//姓名
	public String img;
	public String summary;//描述
	public String imgPath;
	public String fileUrl;
	public String videoUrl;
	public String socketVideo;
	public String socketIp;
	public String socketPort;
	public boolean flag;
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getImgText() {
		return imgText;
	}
	public void setImgText(String imgText) {
		this.imgText = imgText;
	}
	public String imgText;
}
