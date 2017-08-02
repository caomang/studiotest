package cn.sbx.deeper.moblie.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.domian.ApprovalGroup;
import cn.sbx.deeper.moblie.domian.ContactDepartment;
import cn.sbx.deeper.moblie.domian.NewsPaper;
import cn.sbx.deeper.moblie.domian.NewsPaperMain;
import cn.sbx.deeper.moblie.domian.SinopecMenu;


/**
 * 
 * @author terry.C
 *
 */
public class DataCache {
	
	public static List<Object> allMenus = new ArrayList<Object>();
	
	public static SinopecMenu sinopecMenu = new SinopecMenu();
	
	public static LinkedHashMap<String, ApprovalGroup> groups = new LinkedHashMap<String, ApprovalGroup>();
	
	
	
//	public static List<ScheduleItem> scheduleItems = new ArrayList<ScheduleItem>();
	
//	public static List<AnalyReportItem> reports = new ArrayList<AnalyReportItem>();
	
//	public static List<Object> analyReports = new ArrayList<Object>();
	
//	public static List<Object> analyReportPdfs = new ArrayList<Object>();
	
//	public static List<Meeting> mettings = new ArrayList<Meeting>();
	
//	public static  List<AppAccount> appAccounts = new ArrayList<AppAccount>();
	
	/**
	 * OA审批
	 */
//	public static Map<String, List<Task>> tasks = new HashMap<String, List<Task>>();
	/**
	 * 手机报,这个变量只做了一个非空判断，
	 */
	public static LinkedHashMap<String, List<NewsPaperMain>> newsPaperShelf = new LinkedHashMap<String, List<NewsPaperMain>>();
	
	public static LinkedHashMap<String, List<NewsPaper>> newsPaperList = new LinkedHashMap<String, List<NewsPaper>>(); 
	
	/**
	 * 内网信息
	 */
//	public static List<TypeItem> list_title = new ArrayList<TypeItem>();  //内网信息标题
//	public static Map<String, List<NewsPaper>> intranets = new LinkedHashMap<String, List<NewsPaper>>();  //内容信息内容
	/**
	 * 企业通讯录
	 */
	public static ContactDepartment topDept = new ContactDepartment();
	public static HashMap<String, Integer> taskCount = new HashMap<String, Integer>();
	
	/**
	 * 主题表
	 */
	public static Map<String, String> themeMap = new HashMap<String, String>();
	/**
	 * 下载队列表
	 */
	public static List<String> imageQueue = new ArrayList<String>();
	
	//layout Type
	public static String layoutType = "";
	//news paper cache
	public static LinkedHashMap<String, LinkedHashMap<String, List<NewsPaperMain>>> papers = new LinkedHashMap<String, LinkedHashMap<String, List<NewsPaperMain>>>();
	public static int numsOfMoudleHasNotification = 0;
}
