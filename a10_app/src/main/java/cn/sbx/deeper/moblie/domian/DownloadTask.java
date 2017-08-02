package cn.sbx.deeper.moblie.domian;

import android.app.Notification;

public class DownloadTask
{
	private String url;
	private int notifyID;
	private Notification notification;

	public DownloadTask()
	{
		// TODO Auto-generated constructor stub
	}

	public Notification getNotification()
    {
	    return notification;
    }

	public void setNotification(Notification notification)
    {
	    this.notification = notification;
    }

	public int getNotifyID()
    {
	    return notifyID;
    }

	public void setNotifyID(int notifyID)
    {
	    this.notifyID = notifyID;
    }

	public String getUrl()
    {
	    return url;
    }

	public void setUrl(String url)
    {
	    this.url = url;
    }

}
