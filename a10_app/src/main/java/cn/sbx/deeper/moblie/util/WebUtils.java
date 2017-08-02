package cn.sbx.deeper.moblie.util;

import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.domian.AlarmInfo;
import cn.sbx.deeper.moblie.domian.MessageType;

public class WebUtils {

//    public static String intranetUrl = "http://10.29.32.78/DMCWebTest/";//neiwang
//    public static String internetUrl = "http://10.29.32.78/DMCWebTest/";
    //测试环境
//    public static String intranetUrl = "http://127.0.0.1:10092/DMCWeb/";
//    public static String internetUrl = "http://127.0.0.1:10092/DMCWeb/";//jiamiji
    //生产环境
    public static String intranetUrl = "http://127.0.0.1:10122/DMCWeb/";
    public static String internetUrl = "http://127.0.0.1:10122/DMCWeb/";//jiamiji


//正式环境对应的端口号
//    public static String intranetUrl = "http://127.0.0.1:10122/DMCWeb/";
//    public static String internetUrl = "http://127.0.0.1:10122/DMCWeb/";//jiamiji

//    public static String internetUrl = "http://11.11.179.153/dmcweba10/";
//    public static String intranetUrl = "http://11.11.179.153/dmcweba10/";
//    public static String internetUrl = "http://127.0.0.1:10092/dmcweba10/";
//    public static String intranetUrl = "http://127.0.0.1:10092/dmcweba10/";

    //    public static String internetUrl = "http://10.120.131.248/DMCWebTest/";
//    public static String intranetUrl = "http://10.120.131.248/DMCWebTest/";
//
    //图片上传 webservices url
//    public static String webservicesUrl = "http://127.0.0.1:10092/DMCWeb/Integrated/SaveImgService.asmx";


    //测试环境图片上传
//    public static String webservicesUrl = "http://127.0.0.1:10092/DMCWeb/Integrated/SaveImgService.asmx";
//    public static String webservicesUrl1 = "http://127.0.0.1:10092/DMCWeb/Integrated/SavePwdService.asmx";


    //正式环境图片上传
    public static String webservicesUrl = "http://127.0.0.1:10122/DMCWeb/Integrated/SaveImgService.asmx";
    public static String webservicesUrl1 = "http://127.0.0.1:10122/DMCWeb/Integrated/SavePwdService.asmx";


//正式环境对应的端口号
//    public static String webservicesUrl = "http://127.0.0.1:10122/DMCWeb/Integrated/SaveImgService.asmx";
//    public static String webservicesUrl1 = "http://127.0.0.1:10122/DMCWeb/Integrated/SavePwdService.asmx";
//    public static String webservicesUrl = "http://10.120.133.17/DMCWeb/Integrated/SaveImgService.asmx";
//    public static String webservicesUrl = "http://10.29.32.78/DCWeb`Test/ATenTest/SaveImgService.asmx";
//    public static String webservicesUrl = "http://10.120.133.17/ATenTest/SaveImgService.asmx";


    // 获取紧急联系人
    public static String contactUrl = "";
    // 上传
    public static String getsubmitUrl = "";
    public static String submitUrl = "";
    public static String uploadchaobiaoUrl = "";
    public static String chaoBiaoMoneyUrl = ""; //气价计算
    public static String uploadUrl_anjian = "";//安检上传
    // 上次gps坐标
    public static String uploadgpsUrl = "";
    public static String downloadurl = "";
    //	安检下载
    public static String downloadurl_anjian = "";
    //	字典下载
    public static String downloadDataDicUrl = "";


    //	图片上传 ftp
    public static String upPhotoUrl = "10.120.131.248";
//    public static String upPhotoUrl = "127.0.0.1";

    public static int upPhotoHost = 21;
//    public static int upPhotoHost = 10093;

    public static String upPhotoName = "htest";
    public static String upPhotoPass = "@htest";


    // 集群通
    public static String jqtUrl = "218.249.39.214";
    public static String jqtport = "5080";
    public static String jqtusername = "";
    public static boolean loginStatus = false;

    public static String apnUrl = "";
    public static String rootUrl = "";

    public static String networkType = "0";// 内网 0 外网 1 apn 2

    public static String cookie;

    public static String deviceId;

    public static String phoneNumber;

    public static String packageName;

    public static List<String> launchPakageNames;

    public static String versionName;

    public static double sd_Avail;

    // 燕山增加
    public static String esPackageName = "com.estrongs.android.pop";// es 文件管理器
    // 燕山打开zip
    public static List<MessageType> msgType = new ArrayList<MessageType>();
    // 预警增加
    public static int netType = 0;
    public static List<AlarmInfo> alertItems = new ArrayList<AlarmInfo>();
    public static boolean reading = false;
    public static int sdkVersion;
    // 静态变量 添加常用语
    public static String dExpress = "";
    // 新合署办公
    public static String username = "";
    public static String password = "";
    //
    public static String role = "0";// 0 不是开发者 1 是开发者
}
