package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;
import java.util.List;

/**
 * 邮箱详细信息实体
 * @author terry.C
 * 
 */
public class MailDetail implements Serializable{
	
	public String subject;//邮件主题
	
	public String sender;//接收者
	
	public String receivers;//发送者
	
	public String cc;//抄送
	
	public String bc;//密送
	
	public String deliveredDate;//投递时间
	
	public String content;//邮件主题
	
	public List<MailAttachment> mailAttachments;//邮件附件

	@Override
	public String toString() {
		return "MailDetail [subject=" + subject + ", sender=" + sender
				+ ", receivers=" + receivers + ", cc=" + cc + ", bc=" + bc
				+ ", deliveredDate=" + deliveredDate + ", content=" + content
				+ ", mailAttachments=" + mailAttachments + "]";
	}
	
}
