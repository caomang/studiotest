package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReceiveAnnotateInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String lastTPoint = "0";
	private String docId;
	private List<HostInfo> hostInfos = new ArrayList<HostInfo>();

	private static ReceiveAnnotateInfo info = null;

	// 单例模式
	public static ReceiveAnnotateInfo getInfo() {
		if (info == null)
			info = new ReceiveAnnotateInfo();
		return info;
	}

	public static String getLastTPoint() {
		return lastTPoint;
	}

	public static void setLastTPoint(String lastTPoint) {
		ReceiveAnnotateInfo.lastTPoint = lastTPoint;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public List<HostInfo> getHostInfos() {
		return hostInfos;
	}

	public void setHostInfos(List<HostInfo> hostInfos) {
		this.hostInfos = hostInfos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public class HostInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String id;
		private String docAttr;
		private String docLength;
		private String annotate;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDocAttr() {
			return docAttr;
		}

		public void setDocAttr(String docAttr) {
			this.docAttr = docAttr;
		}

		public String getDocLength() {
			return docLength;
		}

		public void setDocLength(String docLength) {
			this.docLength = docLength;
		}

		public String getAnnotate() {
			return annotate;
		}

		public void setAnnotate(String annotate) {
			this.annotate = annotate;
		}
	}

}
