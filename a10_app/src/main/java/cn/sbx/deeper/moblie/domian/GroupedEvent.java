package cn.sbx.deeper.moblie.domian;

import java.util.ArrayList;

public class GroupedEvent {

	public String title;
	public String dateTime;
	public String location;
	//added 
	public String Field1;
	public String Field2;
	public String Field3;
	public ArrayList<SubGroupedEvent> sges = new ArrayList<SubGroupedEvent>();

	public class SubGroupedEvent {

		public String label;
		public String text;

	}
}
