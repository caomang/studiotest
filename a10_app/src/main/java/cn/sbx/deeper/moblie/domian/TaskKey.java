package cn.sbx.deeper.moblie.domian;

public class TaskKey {
	public String TypeData;
	public String typeKey;
	public String caption;
	public String ispage="";
	public  String isClick;
	public int currentPage = 1;
	@Override
	public int hashCode() {
		return 33;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof TaskKey)) {
			return false;
		}
		TaskKey taskKey = (TaskKey)o;
		return (this.typeKey.equals(taskKey.typeKey) && (this.caption.equals(taskKey.caption)));
	}
}
