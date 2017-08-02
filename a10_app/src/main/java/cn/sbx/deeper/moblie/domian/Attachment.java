package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;


/**
 *pdf的URI 
 *
 */
public class Attachment implements Serializable{
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	public Integer id;
	public String key2;
	public String pdf;
	public String url;
	public String title;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey2() {
		return key2;
	}
	public void setKey2(String key2) {
		this.key2 = key2;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	@Override
	public String toString() {
		return "Attachment [id=" + id + ", key2=" + key2 + ", pdf=" + pdf + "]";
	}
	

}
