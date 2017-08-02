package cn.sbx.deeper.moblie.contrants;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;

@SuppressLint("UseSparseArrays")
public class Constants {

    //程序压力测试的次数
    public static int testForUpDataNum=0;

    // 密钥
    public final static String secretKey = "/??p?Fo?F??eU^D???5Ry???";
    // 向量
    public final static String iv = "???|j??*";

    public static final boolean DEBUG = true;
    public static byte[] lock = new byte[1024];
    public static String Receiver_Group = "intent.actioin.group.receiver";
    public static String Receiver_Group2 = "intent.actioin.group.receiver2";
    // smp20131212135922586 left menu ma.s20131224183919389 bottom menu
    // ma.s20131224180939223 蒙少:20131225175754242 lbl:20131205093307883
    // zb.s20140113104914836 有问题的接口 gqsh.s20140110133908882
    // 润滑油 zb.s20140123103106690

    // 总部 zb.s20140213092215159 zb.s20140213092215159 上海高桥
    // gqsh.s20140110133908882 济南zb.s20140311101247034

    // 总部 zb.s20140213092215159 zb.s20140213092215159
    // zb.s20140306140204018 erp
    // 开发者模式 1 开启 0 关闭
    public static String developerMode = "0";
    public static String home_cache = "sunboxsoft";// sd卡缓存目录名称
    public static String sd_cache = "sc";// sd卡缓存目录名称
    //	public static String db_path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";// 数据库类型
    public static String db_path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/a10_db.db";// 数据库类型
    //	public static String path_db = "jdbc:dmedb://data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/A10_database.db";// 加密数据库类型
    public static String path_db = "jdbc:sqldroid://data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/A10_database.db";// 加密数据库类型
//	public static String path_db = "jdbc:sqldroid://data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/A10_database.db";// 加密数据库类型


    public static View backView;
    // 外事 zb.s20140228091244543
    // 燕山测试：zb.s20140228091244543
    // zb.s20140416150555212 燕山61演示版本
    // zb.s20140509131133624 外网版本
    // zb.s20140617143149541 燕山DMZ最新正式版本
    // zb.s20140827105925356 迪谱 4.0 演示
    // zb.s20140806113152243 河北移动4.0
    // zb.s20140922100541716 沙河A3
    // zb.s20141229140209292 山东销售
    // xj.s20150205163756896 巡检
    public static final String testPackage = "zb.s20150924085920475";// "zb.s20140916153921546";//
    // "zb.s20140806113152243";//润滑油
    // zb.s20140311101202549
    // //原有
    // zb.s20140311142029358

    public static String LEFT_MENU_DIRECT = "Intent.action.leftmenu.direct";
    public static String MODIFY_FRAGMENT_TYPE_OA_RECEIVER = "Intent.action.modifytype.oa";
    public static String MODIFY_APP_MAIN_SKIN = "Intent.action.modifyskin.main";
    public static String MODIFY_APP_MENU_NUM = "Intent.action.updatemenunum.main";
    public static String GET_APP_MENU_NUM = "Intent.action.getNumber.main";
    public static String GET_APP_MENU_TAB = "Intent.action.menu.tab";
    public static String GET_APP_MENU_REFRESH = "Intent.action.menu.refresh";
    public static String GET_KEHU_QIAN_MING = "Intent.action.name.kehu";
    public static String receive_upload_result_anjian = "Intent.action.name.upload_anjian";
    public static boolean useVPN = false;

    public static Map<String, String> getBroadcastNamePairs() {
        Map<String, String> map = null;
        map = new HashMap<String, String>();
        map.put("approval", MODIFY_FRAGMENT_TYPE_OA_RECEIVER);
        return map;
    }

    private static Map<Integer, String> broadcastNamePairs = new HashMap<Integer, String>();
    public static Map<String, String> broadcastNameNewPairs = new HashMap<String, String>();

    public static String getBroadcastNamePairsString(int index) {
        return broadcastNamePairs.get(index);
    }

    public static void setBroadcastNamePairs(int index) {
        String action = UUID.randomUUID().toString();
        broadcastNamePairs.put(index, action);
    }

    public static String getBroadcastNameNewPairsString(String name) {
        return broadcastNameNewPairs.get(name);
    }

    public static void setBroadcastNameNewPairs(String name) {
        String action = UUID.randomUUID().toString();
        broadcastNameNewPairs.put(name, action);
    }

    public static boolean change_current_skin_statu = false;
    public static final String CHANGE_MENU_BG = "change_menu_bg";
    public static final String CHANGE_SKIN = "change_skin";
    public static final String SKIN_FOLDER = "skin_folder";
    public static final String BG_FOLDER = "bg_folder";
    public static final String BG_FILENAME = "bg_name";

    public static boolean changeBg = false;
    public static boolean changeSkin = false;
    public static String bg_folder = "";
    public static String bg_name = "";
    public static String skin_folder = "";

    /**
     * 应用帐号
     */
    public static boolean changeAppcount = false;
    /**
     * zyp
     */
    public static boolean check = false;

    /**
     * Metting Seprator
     */
    public static final String CONTENT_SEPRATOR = "@@";
    public static final String ITEM_SEPRATOR = "##";
    public static final String SECRET_SEPRATOR = "@#";

    /**
     * net setting
     */
    // public static String ROOT_URL_DEFAULT =
    // "http://deepermobile.xicp.cn/DPXML2.0/";
    public static String ROOT_URL_DEFAULT = "http://d.deepermobile.com/DPXML2.0/";
    public static final String WS_URL_DEFAULT = "ws://10.238.63.69:3000/";// 推送地址
    public static final String networkAvailable = "/DataMutualCenter/testNet.aspx";
    public static String INTRANET_URL_KEY = "intranet_url_key";
    public static String INTRANET_URL = "http://10.29.68.54/DPXML_inner2.0/";
    public static String INTERNET_URL_KEY = "internet_url_key";
    public static String INTERNET_URL = "http://d.deepermobile.com/DPXML2.0/";
    public static String APN_URL_KEY = "anp_url_key";
    public static String APN_URL = "http://10.10.10.1";
    public static final String PREF_AUTOSCANNET = "PREF_AUTOSCANNET";
    public static boolean autoScanNet = false; // 是否自动检测网络
    public static boolean intranetAvailable = false; // 是否自动检测网络
    public static boolean internetAvailable = false; // 是否自动检测网络
    public static final String serverURL = "serverURL";

    /**
     * main fragment type
     */
    public static final String FRAGMENT_TYPE_APPROVAL = "approval";
    public static final String FRAGMENT_TYPE_APPROVAL2 = "approval2"; // 审批2.0
    public static final String FRAGMENT_TYPE_SITE = "site";
    public static final String FRAGMENT_TYPE_CHART = "chart";
    public static final String FRAGMENT_TYPE_DOCUMENT = "document";
    public static final String FRAGMENT_TYPE_SETTING = "setting";
    public static final String FRAGMENT_TYPE_SCHEDULE = "schedule";
    public static final String FRAGMENT_TYPE_NEWS = "news";
    public static final String FRAGMENT_TYPE_DATA = "data";
    public static final String FRAGMENT_TYPE_MEETING = "meeting";
    public static final String FRAGMENT_TYPE_CONTACTS = "contacts";
    public static final String FRAGMENT_TYPE_MONITOR = "monitor";
    public static final String FRAGMENT_TYPE_WPAGESHOW = "wpageshow";
    public static final String FRAGMENT_TYPE_VIDEO = "guangshihua";
    public static final String FRAGMENT_TYPE_PAGESHOW = "pageshow";
    public static final String FRAGMENT_TYPE_EMAIL = "dpemail";
    public static final String FRAGMENT_TYPE_LINKAPP = "linkapp";

    public static Map<String, Fragment> getFragmentPairs() {
        Map<String, Fragment> map = null;
        map = new HashMap<String, Fragment>();
        // map.put("nevagation", new MainMenuFragment());
        // map.put("approval", new MobileOAFragment());
        // map.put("site", new MobileIntranetFragment());
        // map.put("chart", new BiFragment());
        // map.put("document", new AnalysisReportFragment());
        // map.put("setting", new MoreFragment());
        // map.put("schedule", new ScheduleFragment());
        // map.put("news", new MobileNewsPaperShelfFragment());
        // map.put("data", new MettingFragment());
        // map.put("meeting", new MettingFragment());
        return map;
    }

    // added by wangst
    public static String Setting = "Intent.action.Setting";
    public static String MODIFY_APP_MENU = "Intent.action.updatemenu.main";
    public static SinopecMenuModule menuModule;
    public static SinopecMenuModule menuModulechaobiao;
    public static boolean isFirst = true;
    public static boolean istab = false;
    public static List<String> attachmentTypesList = java.util.Arrays
            .asList(new String[]{"pdf", "docx", "doc", "xls", "xlsx", "bmp",
                    "png", "jpeg", "jpg", "gif", "ppt", "pptx", "txt", "rar",
                    "zip"});
    // public static String[] attachmentTypes
    // ={"pdf","docx","doc","xls","xlsx","bmp","png","jpeg","jpg","gif","ppt","pptx","txt","rar","zip"};

    // public static String currentGroupModule = "";

    public static final String REGEX_TASK_DETAIL_ATT = "\\[(\\S*?):\"(.*?)\"\\](.*?)\\[/(\\S*?)\\]";
    public static final String REGEX_MOBILE_NO = "^1[3|5|8]\\d{9}$";
    public static final String MESSAGE_TYPE_TEXT = "TEXT";
    public static final String MESSAGE_TYPE_MULTIMEDIA = "MULTIMEDIA";
    public static String BASE_URL_ALL = "http://moa.hecmcc.com";
    public static final String URL_SEND_TEXT_MSG = "/1_Portal/4ShortMessage/4_1ShortMessage.aspx";
    public static final String URL_SEND_MULTIMEDIA_MSG = "/1_Portal/4ShortMessage/4_2MultiMidiaMessage.aspx";
    public static final String GUID = "guid";
    public static final long timeOfQuit = 1000 * 60 * 10;

    public static class Variables {
        // public static String BASE_URL =
        // "http://211.138.9.86";//http://moa.hecmcc.com

        // public static String BASE_URL = "http://moa.hecmcc.com";
        // public static String BASE_URL_INNER = "http://10.129.254.155";
        // public static String BASE_URL_INTERNET = "http://211.138.9.86";
        public static boolean DEFAULT_PAGE = true;
        public static String GUID = "";
        public static final String DEPARTMENT_ID_TOP = "1";
        public static String BASE_URL_ALL = "http://moa.hecmcc.com";
    }

    public static class Params {
        public static final String GUID = "guid";
        public static final String RECIPIENTS = "DestionMobile";
        public static final String MSG_CONTENT = "Msg";

    }

    // 菜单缓存
    public static final String TABLE_MENUINFO = "menu";
    public static final String C_ID = "id";
    public static final String C_NAME = "name";
    public static final String C_TYPE = "type";
    public static final String C_PARENTID = "parentid";
    public static final String C_ISCHOOSE = "ischoose";
    public static final String C_USERNAME = "username";
    public static final String C_MODULEID = "code";

    public static final String Refresh_Time = "Refresh_Time";
    public static final String Speed = "Speed";
    public static final String Update_Num = "UpdtateNum";

    public static String Change_Num = "0";


    //	sp 导航数据名称
    public static String navigate = "daohangData";
    //	登录
    public static String login = "loginData";
    //	登录名
    public static String loginName = "";
    //	安检是否有异常
    public static String isYiChang = "";
    //	拍照临时存放器
    public static ArrayList<PhotoAudio> imgList = new ArrayList<PhotoAudio>();
    //标记图片是否删除
    public static boolean isDeletePicture = false;
    //	标记抄表有上传任务
    public static boolean chaoBiaoSched_Yes = false;
    public static boolean anJianSched_NO = false;

    //	存放上传失败的图片 在 点击图片补传时 使用
    public static ArrayList<PhotoAudio> list_BC_Picture = new ArrayList<PhotoAudio>();
    ;


}
