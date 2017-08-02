package cn.sbx.deeper.moblie;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;
  
/**
 * @author chaixingbo
 * 单例模式来记录打开的Activity
 * 以便在退出时所有的数据被销毁
 * 使用说明：
 * 一、初始化：ExitApplication.getInstance().addActivity(this);
 * 二、退     出：ExitApplication.getInstance().exit();
 *
 */
public class ExitApplication extends Application {   
  
    private List<Activity> activityList = new ArrayList<Activity>();   
    private static ExitApplication instance;   
  
    private ExitApplication()   
    {   
    	
    }   
    //单例模式中获取唯一的ExitApplication实例   
    public static ExitApplication getInstance()   
    {     	
	    if(null == instance)   
	    {   
	    	instance = new ExitApplication();   
	    }   
	    return instance;      
    }
    
    //添加Activity到容器中   
    public void addActivity(Activity activity)   
    {   
    	activityList.add(activity);   
    }
    
    //遍历所有Activity并finish   
    public void exit()   
    {   
	    for(Activity activity:activityList)   
	    {   
	    	activity.finish();   
	    }   
	  
	    System.exit(0);     
    } 
}  
