package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class ResultInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String result;
	public String key;	
	public String message;
	public String currNode;
	public String displayDevice;
	
//	<result><message>成功</message><key>1</key><currNode>2</currNode></result></Root>
}
