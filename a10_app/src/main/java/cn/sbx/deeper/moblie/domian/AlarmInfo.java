package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.Date;

public class AlarmInfo implements Serializable {

	public String id;
	public String productName;
	public String alarmTime;
	public String alarmDesc;
	public String alarmSource;
	public String alarmState;
	public String disposeOpinion;
	public String checkOpinion;
	
	//预警组件
	public int ctrlPoint;
	public int alarmTotal;
	public int solved;
	public int handling;
	public int undisposed;
	public Date lastCommandTime;
	public String leader;
	public int newCommandCount;
	public String alarmTimespan;
	public String graph;
	//added 
	public boolean positionFlag;
}
