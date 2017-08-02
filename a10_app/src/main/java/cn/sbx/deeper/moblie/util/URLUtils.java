package cn.sbx.deeper.moblie.util;

public class URLUtils {

    public static String mobileOfficeMattersActivityURL = "ForIpad/ToDoList/ToDoList2.aspx";// 代办已办列表

    public static String mobileOfficeMattersDetailActivityURL = "ForIpad/ToDoList/ToListDetail.aspx";// 代办已办详细

    public static String mobileOfficeMattersDetailDownloadURL = "ForIpad/ToDoList/ToDoPdf.aspx";// 代办下载URL

    public static String mobileOfficeMattersDetailActivitySendURL = "ForIpad/ToDoList/PostList.aspx";// 送审URL

    public static String mobileOfficeMeetingActivityURL = "ForIpad/Mettings.aspx";// 会议资料

    public static String mobileOfficeNoticeActivityURL = "ForIpad/Notices.aspx";// 通知公告

    public static String mobileOfficeScheduleActivityURL = "ForIpad/Schedule.aspx";// 日程安排

    public static String loginAccountActivityURL = "ForIpad/UserAccounts.aspx";// 应用帐号

    public static String modifyAccountActivityURL = "ForIpad/SetUserInfo/UpdateUser.aspx";// 修改帐号

    public static String delAccountActivityURL = "ForIpad/SetUserInfo/DelUser.aspx";// 删除帐号

    public static String getAppDetail = "/Integrated/GetAppDetail.aspx"; // 关于我们
    // 检查登陆人是否是开发者
    public static String devCheckUrl = "/Integrated/CheckDev.aspx";
    // 升级服务器URL
    // public static String updateServer = "/checkupdate.aspx";
    public static String updateServer = "/MDM/CheckUpdate.aspx";

    public static String networkAvailable = "/testnet.aspx";
    /*
	 * ======================================================金陵石化URL==============
	 * ================================================================
	 */

    /**
     * 合同列表
     */
    public static String mobileOfficehtlist = "/Integrated/GetData.aspx?pageId=26";

    /**
     * 合同详细
     */
    public static String mobileOfficehtDetail = "/Integrated/GetData.aspx?pageId=27&prm1=";

    /**
     * 合同附件下载
     */
    public static String mobileOfficehtDownload = "/ForIpad/ToDoList/getfile.aspx";

    /**
     * 合同送审
     */
    public static String mobileOfficeSend = "/Integrated/GetData.aspx?pageId=28";

    /**
     * OA列表
     */
    public static String mobileOfficeOAlist = "/Integrated/GetData.aspx?pageId=1";

    /**
     * OA详细
     */
    public static String mobileOfficeOADetail = "/Integrated/GetData.aspx?pageId=5&prm1=";

    /**
     * OA送审 /Integrated/GetData.aspx?pageId=28 public static String
     * mobileOfficeOASend = "/ForIpad/ToDoList/PostList.aspx";
     */
    public static String mobileOfficeOASend = "/Integrated/GetData.aspx?pageId=8";

    /**
     * OA下载附件
     */
    public static String mobileOfficeOADownload = "/Integrated/GetFile.aspx";

    /**
     * 期刊一级列表
     */
    public static String mobileOfficeNewslistMain = "/Integrated/GetData.aspx?pageId=30";

    /**
     * 新闻分类二级列表
     */
    public static String mobileOfficeNewsList = "/Integrated/GetData.aspx?pageId=31&prm1=";

    /**
     * 手机报详细内容
     */
    public static String mobileOfficeNewsDetail = "/Integrated/GetData.aspx?pageId=32&prm1=";

    /**
     * 综合新闻列表
     */
    public static String mobileOfficeIntranetNewsList = "/Integrated/GetData.aspx?pageId=35";

    /**
     * 综合新闻详细
     */
    public static String mobileOfficeIntranetNewsDetail = "/Integrated/GetData.aspx?pageId=36&prm1=";

    /**
     * 会议通知列表
     */
    public static String mobileOfficeMettingList = "/Integrated/GetData.aspx?pageId=37";

    /**
     * 会议通知详细
     */
    public static String mobileOfficeMettingDetail = "/Integrated/GetData.aspx?pageId=38&prm1=";

    /**
     * 生产信息列表
     */
    public static String mobileOfficeProductList = "/Integrated/GetData.aspx?pageId=39";

    /**
     * 生产信息详细
     */
    public static String mobileOfficeProductDetail = "/Integrated/GetData.aspx?pageId=40&prm1=";

    /**
     * 安全信息列表
     */
    public static String mobileOfficesafeInfosList = "/Integrated/GetData.aspx?pageId=41";

    /**
     * 安全信息详细
     */
    public static String mobileOfficesafeInfosDetail = "/Integrated/GetData.aspx?pageId=42&prm1=";

    /**
     * 环保信息列表
     */
    public static String mobileOfficeenviroInfosList = "/Integrated/GetData.aspx?pageId=43";

    /**
     * 环保信息详细
     */
    public static String mobileOfficeenviroInfosDetail = "/Integrated/GetData.aspx?pageId=44&prm1=";

    /**
     * 检查信息列表
     */
    public static String mobileOfficecheckInfosList = "/Integrated/GetData.aspx?pageId=47";

    /**
     * 检查信息详细
     */
    public static String mobileOfficecheckInfosDetail = "/Integrated/GetData.aspx?pageId=48&prm1=";

    /**
     * 内网信息下载附件 public static String mobileOfficeinyranetDownload =
     * "/ForIpad/ToDoList/GetFile.aspx?typeid=006";
     */
    public static String mobileOfficeinyranetDownload = "/Integrated/GetFile.aspx";

    /**
     * 图片新闻列表
     */
    public static String mobileOfficeImageNewsList = "/Integrated/GetData.aspx?pageId=45";

    /**
     * 图片新闻详细
     */
    public static String mobileOfficecheckImageNewsDetail = "/Integrated/GetData.aspx?pageId=46&prm1=";

    /**
     * 工作纪要列表
     */
    public static String mobileOfficeWorkformList = "/Integrated/GetData.aspx?pageId=49";

    /**
     * 工作纪要列表详细
     */
    public static String mobileOfficeWorkformDetail = "/Integrated/GetData.aspx?pageId=50&prm1=";
    /**
     * 门户文件列表
     */
    public static String mobileOfficeFilesList = "/Integrated/GetData.aspx?pageId=51";

    /**
     * 门户文件列表详细
     */
    public static String mobileOfficeFilesDetail = "/Integrated/GetData.aspx?pageId=52&prm1=";
    /**
     * 门户摘要列表
     */
    public static String mobileOfficeSummaryList = "/Integrated/GetData.aspx?pageId=53";

    /**
     * 门户摘要列表详细
     */
    public static String mobileOfficeSummaryDetail = "/Integrated/GetData.aspx?pageId=54&prm1=";

    /**
     * 选择路由联系人查找部门
     */
    // public static String mobileOfficechooseDepart =
    // "/Integrated/GetData.aspx?pageId=2";

    /**
     * 选择路由联系人查找联系人
     */
    // public static String mobileOfficechoosePeople =
    // "/Integrated/GetData.aspx?pageId=3";

    /**
     * 广石化内网动态菜单修改
     */
    /**
     * 菜单类别列表
     */
    public static String mobileOfficeIntranetAsynMenuList = "/Integrated/GetDataGroup.aspx?GroupId=1";

    /**
     * 列表详细
     */
    public static String mobileOfficeIntranetDetail = "/Integrated/GetData.aspx?pageId=";

    /**
     * 详细
     */
    public static String mobileOfficeIntranetMainDetail = "/Integrated/GetData.aspx?pageId=";

	/*----------------------------------------------中冶URL-----------------------------------------------------------------------*/

    /**
     * pdf书架列表
     */
    public static String mobileOfficeAnalyReport = "/foripad/Category.aspx";

    /**
     * pdf树形结构
     */
    public static String mobileOfficeAnalyReportTree = "/foripad/CategoryDoc.aspx?CategoryId=";

    /**
     * 修改登录密码
     */
    public static String mobileOfficeModifyLoginPwd = "/updatepwd.aspx";

	/*----------------------------------------------------2.0Url---------------------------------------------------------------------------*/

    /**
     * 主菜单URL
     */
    public static String mainMenuUrl = "/Integrated/GetUserApps.aspx?userid=";
    public final static String baseContentUrl2 = "/Integrated/GetMobileData.aspx?pageid=";

    /**
     * 加载菜单图片URL
     */
    public static String loadImageUrl = "/Integrated/GetPicture.aspx?catalog=android@800_1280@yellow&filename=";

    /**
     * 无线会议--查询会议状态接口URL
     */
    public static String wireless_getWirelessStatus = "/WirelessMeetings/GetWlMeetingStatus.aspx";

    /**
     * 无线会议--参加会议接口URL
     */
    public static String wireless_join_meet = "/WirelessMeetings/JoinWlMeeting.aspx";

    /**
     * 无线会议--散会接口URL
     */
    public static String wireless_disconnect_meet = "/WirelessMeetings/FinishWlMeeting.aspx";

    /**
     * 无线会议--申请主持接口URL
     */
    public static String wireless_apply_preside_meet = "/WirelessMeetings/ApplyPresidedWlMeeting.aspx";

    /**
     * 无线会议--取消主持接口URL
     */
    public static String wireless_cancel_preside_meet = "/WirelessMeetings/CancelPresidedWlMeeting.aspx";

    /**
     * 无线会议--发送会议状态接口URL
     */
    public static String wireless_send_statu_meet = "/WirelessMeetings/SendWlMeetingStatus.aspx";

    /**
     * 无线会议--发送会议批注信息接口URL
     */
    public static final String wireless_send_annotate = "/WirelessMeetings/SendAnnotationInfo.aspx";

    /**
     * 无线会议--接收会议批注信息接口URL
     */
    public static final String wireless_receive_annotate = "WirelessMeetings/GetAnnotationInfo.aspx";

	/*
	 * =======================================================Sinopec
	 * 3.0========
	 * ================================================================
	 * ==============
	 */

    /**
     * 所有链接的中间一段
     */
    public static String baseContentUrltest = "/Integrated/getReports.aspx?pageid=";
    // public static String baseContentUrl =
    // "/Integrated/getReports.aspx?pageid=";
    public static String baseContentUrl = "/Integrated/GetMobileData.aspx?pageid=";
    public static String baseContentUrlNew = "/Integrated/GetMobileData.aspx";

    // public static String baseContentUrl =
    // "/Integrated/GetMobileData.aspx?pageid=";

    /**
     * login url
     */
    public static String splashActivityURL = "/Login.aspx";


    /**
     * get Database passwd
     */
    public static String getDatabaseURL="/Integrated/GetMoblieOffLinePwd.aspx";

    /**
     * give secret to server
     */

    public static String giveSecretURL="Integrated/AddUserOffLine";

    /**
     * getIntegrated url
     */
    public static String getCkeckCodeURL = "/Integrated/CreateCheckCode.aspx";

    /**
     * main menu url
     */
    public static String sinopecMainMenu = "/Integrated/GetUserApps.aspx";

    /**
     * main auth url
     */
    public static String sinopecMainAuthMenu = "/Integrated/GetUserAuth.aspx";
    /**
     * 获取子帐号
     */
    public static final String US_GETCHILD = "/UserInfo/GetChildAccount.aspx";// 获取子帐号

    public static final String US_SAVEUSERACCOUNT = "/UserInfo/SaveUserAccount.aspx";// 保存子帐号
    public static final String US_DeleteAccount = "/UserInfo/DeleteAccount.aspx";// 保存子帐号
    /**
     * 新建日程
     */
    public static final String SCHEDULES_CREATE = "/WebComponent/ContentHtml/phone/meetingapply.html";// 新建日程
    /**
     * 删除日程
     */
    public static final String SCHEDULES_DELETE = "/WebComponent/Schedules/DeleteSchedulesHandler.ashx";// 删除日程

    public static String getContact = "/Integrated/GetMobileData.aspx";

    // /////////////新合署办公//////////////////
    public static String MessageType;
    public static String GetUserName;
    public static String GetUsernameBatch;
    public static String GetUsers;
    public static String GetOrgs;
    public static String newMessage;
    public static String forwardMessage;
    public static String showNewMessage;
    public static String showUnreadMessage;
    public static String markRead;
    public static String showReadMessage;
    public static String replyMessage;
    public static String finishMessage;
    public static String showFinishMessage;
    public static String fileMessage;
    public static String showFiledMessage;
    public static String showReply;
    public static String getMessageCount;
    public static String setReadReplyCount;
    public static String getMessageAttachment;
    public static String getReplyAttachment;
    public static String YUninOffice;
    public static String YTwoUninOffice;
    public static String DUZH;
    public static String getDepartmentMessageCount;
    public static String attachement;
    // ///////////////////新合署办公DSZCZ////////////////////////
    public static String getAssignedPeopleCount;
    // ///////////////////预警信息//////////////////////////
    public static String mobileOfficeSendUnitURL;
    public static String mobileOfficeUnitsListURL;
    public static String mobileOfficeAlarmstListURL;
    public static String mobileOfficeSendAlarmstatusURL;
    // /////////////////email///////////////////////////
    public static String mail_CGX;
    public static String mail_attachment;
    public static String mail_FJX;
    public static String mail_FSYJ;
    public static String mail_LXR;
    public static String mail_SJX;
    public static String mail_TotalCount;
    public static String mail_XX;
    public static String mail_departmentList;
    public static String mail_SCYJ;// delete is exist
    // public static String mail_userList;
    // ////////////////////文档系统/////////////////////////////
    public static String CheckUserGroup;
    public static String addlog;
    public static String addPlan;
    public static String WorkLogType;
    public static String List;
    public static String IsCheckSerach;
    public static String pageDetail;

    /**
     * 金陵巡检系统
     */
    // http://mobile.jlpec.com/GPS/Login.aspx
    // http://42.96.173.8:8888/web/

//	http://42.96.173.8:81/InspectionInterface/Inspection/SubmitWorkflow.aspx  


    public static final String loginUrl = "Login.aspx";
    public static final String gpsInterface = "InspectionInterface/Inspection/SubmitWorkflow.aspx?pageid=";
    public static final String contactInterface = "InspectionInterface/Linkman/GetLinkman.aspx?pageid=";

    // http://mobile.jlpec.com/GPS/EmergencyTab.ashx?userid=wf_test_02
    public static final String emergencyTab = "EmergencyTab.ashx";
    // http://mobile.jlpec.com/GPS/EmergencyList.ashx?userid=wf_test_01&id=%E9%80%89%E9%A1%B9%E5%8D%A1id
    public static final String emergencyList = "EmergencyList.ashx";
    // http://42.96.173.8:8888/LDAR_GPS/RouteList.ashx?userid=nfxj
    public static final String routeList = "RouteList.ashx";
}
