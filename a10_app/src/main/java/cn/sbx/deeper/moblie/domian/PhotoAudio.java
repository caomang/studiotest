package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

//图片
public class PhotoAudio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String timeLength;
	private String cmScFileName;
	private String cmScFileTitle;
	private String cmScFileForm;
	private String cmScBusiType;
	private String cmScFileRoute;
	private String cmScFileSize;
	private String cmScFileDttm;
	private String cmScFileVar1;
	private String cmScFileVar2;
	private String cmScdate;

	public String getCmScdate() {
		return cmScdate;
	}

	public void setCmScdate(String cmScdate) {
		this.cmScdate = cmScdate;
	}

	public String getCmScFileName() {
		return cmScFileName;
	}

	public void setCmScFileName(String cmScFileName) {
		this.cmScFileName = cmScFileName;
	}

	public String getCmScFileTitle() {
		return cmScFileTitle;
	}

	public void setCmScFileTitle(String cmScFileTitle) {
		this.cmScFileTitle = cmScFileTitle;
	}

	public String getCmScFileForm() {
		return cmScFileForm;
	}

	public void setCmScFileForm(String cmScFileForm) {
		this.cmScFileForm = cmScFileForm;
	}

	public String getCmScBusiType() {
		return cmScBusiType;
	}

	public void setCmScBusiType(String cmScBusiType) {
		this.cmScBusiType = cmScBusiType;
	}

	public String getCmScFileRoute() {
		return cmScFileRoute;
	}

	public void setCmScFileRoute(String cmScFileRoute) {
		this.cmScFileRoute = cmScFileRoute;
	}

	public String getCmScFileSize() {
		return cmScFileSize;
	}

	public void setCmScFileSize(String cmScFileSize) {
		this.cmScFileSize = cmScFileSize;
	}

	public String getCmScFileDttm() {
		return cmScFileDttm;
	}

	public void setCmScFileDttm(String cmScFileDttm) {
		this.cmScFileDttm = cmScFileDttm;
	}

	public String getCmScFileVar1() {
		return cmScFileVar1;
	}

	public void setCmScFileVar1(String cmScFileVar1) {
		this.cmScFileVar1 = cmScFileVar1;
	}

	public String getCmScFileVar2() {
		return cmScFileVar2;
	}

	public void setCmScFileVar2(String cmScFileVar2) {
		this.cmScFileVar2 = cmScFileVar2;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(String timeLength) {
		this.timeLength = timeLength;
	}

}
