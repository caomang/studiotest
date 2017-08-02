package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频设备分组信息
 * @author terry.C
 *
 */
public class MonitorDeviceGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String caption;
	
	public String id;
	
	public List<MonitorDevice> monitorDevices = new ArrayList<MonitorDevice>();
	
	public List<MonitorDeviceGroup> deviceGroups = new ArrayList<MonitorDeviceGroup>();
}
