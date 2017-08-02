package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

/**
 * OA邮件附件实体
 * @author terry.C
 * 
 */

public class MailAttachment implements Serializable{
	
	public String name;//附件名称
	
	public String url;//附件下载地址
	
	public String contentType;//邮件类型
	
}
