package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

/**
 * 更新实体类
 * @author terry.C
 *
 */
public class SoftInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String progID;
	
	private String version;
	
	private String updateLog;
	
	private String downloadUrl;
	
	private String forced;
	
	private String lastVersion;

	public String getProgID() {
		return progID;
	}

	public void setProgID(String progID) {
		this.progID = progID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getForced() {
		return forced;
	}

	public void setForced(String forced) {
		this.forced = forced;
	}
	
	

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	@Override
	public String toString() {
		return "SoftInfo [progID=" + progID + ", version=" + version
				+ ", updateLog=" + updateLog + ", downloadUrl=" + downloadUrl
				+ ", forced=" + forced + "]";
	}
}
