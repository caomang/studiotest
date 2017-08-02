package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

/**
 * 视频设备信息
 * @author terry.C
 *
 */
public class MonitorDevice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String displayName;
	public String deviceAddr;
	public String requestProtocol;
	public String videoEncFormat;
	public String videoWidth;
	public String videoHeight;
	public String videoRate;
	public String additionalParams;
	public String paramSequence;
	public String dataStorageMode;
	public String responseAssertion;
	public boolean isChecked = false;
}
