package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

/**
 * The bean for approve tab and search condition
 * @author terry.C
 *
 */
public class ApproveTab implements Serializable{

	private static final long serialVersionUID = 1L;

	public String title;
	
	public String id;
	
	public SearchCondition searchCondition = new SearchCondition();
	
	public boolean isBatch;
}
