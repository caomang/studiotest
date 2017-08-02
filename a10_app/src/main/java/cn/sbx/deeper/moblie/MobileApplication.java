package cn.sbx.deeper.moblie;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.util.VersionUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import petrochina.ghzy.a10fieldwork.R;


//import android.database.sqlite.SQLiteDatabase;

public class MobileApplication extends Application {
    public String cookie;
    public String deviceId;
    public String phoneNumber;
    public String packageName;
    public List<String> names;
    SharedPreferences sp;
    public static Context appContext;
    public static String userName;
    public static String pwd;
    public static int netType;
    public Stack<Activity> activityStack = new Stack<Activity>(); // activity栈集合
    private static MobileApplication application = null;

    public String routecode;

    private static ArrayList<String> StrCitys;
    public SQLiteDatabase db;// 操作数据库的工具类
    // 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("mark", "网络状态已经改变");
                int netType = 0;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();
                if (networkInfo == null) {
                    netType = 0;
                } else {
                    int nType = networkInfo.getType();
                    if (nType == ConnectivityManager.TYPE_MOBILE) {
                        String extraInfo = networkInfo.getExtraInfo();
                        if (!android.text.TextUtils.isEmpty(extraInfo)) {
                            if (extraInfo.toLowerCase().equals("cmnet")) {
                                netType = 3;
                            } else {
                                netType = 2;
                            }
                        }
                    } else if (nType == ConnectivityManager.TYPE_WIFI) {
                        netType = 1;
                    }
                }
                MobileApplication.netType = 1;
                System.out.println("net type: " + netType);
            }
        }
    };

    @Override
    public void onCreate() {
        appContext = getApplicationContext();
        application = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        // SDKInitializer.initialize(this); (可能是地图)
        super.onCreate();
        // CrashHandler crashHandler = CrashHandler.getInstance();
        // crashHandler.init(appContext);
        sp = getSharedPreferences("sys_config", Context.MODE_PRIVATE);
        // PDF.setApplicationContext(this); // PDF class needs application
        // context to load assets
        initDeviceId();
        Constants.autoScanNet = sp.getBoolean(Constants.PREF_AUTOSCANNET,
                Constants.autoScanNet);
        String developerMode = sp.getString("developerMode",
                Constants.developerMode);
        Constants.developerMode = developerMode;
        // WebUtils.launchPakageNames = getHomes();
        // initDefaultThemeAndSkin();
        initBlueThemeAndSkin();

        // if (VPNManager.getInstance() == null) {
        // VPNManager.initialize(getApplicationContext());
        // }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
      /*  Intent intentBro = new Intent();
        intentBro.setAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.sendBroadcast(intentBro);*/
        // 计算剩余空间
        querySize();
        initImageLoader(getApplicationContext());
        // 导入数据库
        importInitDatabaseA10();
        importInitDB();
//        importInitDatabase();
        // 创建数据库
        createDB();
    }

    private void createDB() {
        // 判断数据库中是否存在表
        Connection con = null;
        Statement state = null;
        try {
            con = SQLiteData.openOrCreateDatabase(getApplicationContext());
            state = con.createStatement();
//            String sql = "select * from dmedb_master where name ="; //加密数据库
            String sql = "select * from sqlite_master where name =";

            ResultSet set = state.executeQuery(sql + " 'custInfo'");
            // 客户信息表
            if (!set.next()) {
                state.executeUpdate("create table custInfo(schedInfoID varchar(128),"
                        + " meterReadCycleRouteSequence varchar(128),"
                        + "accountId varchar(128),"
                        + "entityName varchar(128),"
                        + "customerClass varchar(128),"
                        + "cmCustClDescr varchar(128),"
                        + "cmMrAddress varchar(128),"
                        + "cmMrDistrict varchar(128),"
                        + "cmMrStreet varchar(128),"
                        + "cmMrCommunity varchar(128),"
                        + "cmMrBuilding varchar(128),"
                        + "cmMrUnit varchar(128),"
                        + "cmMrRoomNum varchar(128),"
                        + "spMeterHistoryId varchar(128),"
                        + "meterConfigurationId varchar(128),"
                        + "cmMrMtrBarCode varchar(128),"
                        + "fullScale varchar(128),"
                        + "cmMrAvgVol varchar(128),"
                        + "rateSchedule varchar(128),"
                        + "cmRsDescr varchar(128),"
                        + "cmMrLastBal varchar(128),"
                        + "cmMrOverdueAmt varchar(128),"
                        + "cmMrDebtStatDt varchar(128),"
                        + "cmMrLastMrDttm varchar(128),"
                        + "readType varchar(128),"
                        + "cmMrLastMr varchar(128),"
                        + "cmMrLastVol varchar(128),"
                        + "cmMrLastDebt varchar(128),"
                        + "cmMrLastSecchkDt varchar(128),"
                        + "cmMrRemark varchar(128),"
                        + "cmMrState varchar(128)," + "cmMrDate varchar(128))");
            }
            set.close();
            ResultSet set1 = state
                    .executeQuery(sql + " 'attachment'");
            if (!set1.next()) {
                state.executeUpdate("create table attachment(bzId varchar(128),"

                        + "bzType varchar(128),"
                        + "userID varchar(128),"
                        + "attachmentType varchar(128),"
                        + "attachmenturl varchar(128),"
                        + "status varchar(128))");
            }
            set1.close();

            ResultSet set2 = state.executeQuery(sql + " 'dictionaries'");
            if (!set2.next()) {

                state.executeUpdate("create table dictionaries(dictionaryDescr varchar(128),"

                        + "dictionaryCode varchar(128),"
                        + "parentID varchar(128),"
                        + "dictsequence varchar(128) default '0')");
            }
            set2.close();

            ResultSet set3 = state
                    .executeQuery(sql + "'login_user'");
            if (!set3.next()) {

                state.executeUpdate("create table login_user(userID varchar(128),"

                        + "username varchar(128),"
                        + "password varchar(128),"
                        + "deviceId varchar(128))");
            }
            set3.close();

            ResultSet set4 = state.executeQuery(sql + "'perPhone'");
            if (!set4.next()) {
                state.executeUpdate("create table perPhone(accountId varchar(128),"

                        + "sequence varchar(128),"
                        + "phoneType varchar(128),"
                        + "phone varchar(128),"
                        + "extension varchar(128),"
                        + "cmPhoneOprtType varchar(128))");
            }
            set4.close();

            ResultSet set5 = state.executeQuery(sql + "'schedInfo'");
            if (!set5.next()) {
                state.executeUpdate("create table schedInfo(userID varchar(128),"

                        + "meterReadRoute varchar(128),"
                        + "cmMrRteDescr varchar(128),"
                        + "meterReadCycle varchar(128),"
                        + "cmMrCycDescr varchar(128),"
                        + "scheduledSelectionDate varchar(128),"
                        + "scheduledReadDate varchar(128),"
                        + "cmMrDate varchar(128) default '0')");
            }
            set5.close();

            ResultSet set6 = state.executeQuery(sql + "'uploadcustInfo'");
            if (!set6.next()) {
                state.executeUpdate("create table uploadcustInfo(spMeterHistoryId varchar(128),"

                        + "meterConfigurationId varchar(128),"
                        + "cmMr varchar(128),"
                        + "readType varchar(128),"
                        + "cmMrDttm varchar(128),"
                        + "cmMrRefVol varchar(128),"
                        + "cmMrRefDebt varchar(128),"
                        + "cmMrNotiPrtd varchar(128),"
                        + "cmMrCommCd varchar(128),"
                        + "cmMrRemark varchar(128),"
                        + "cmMrState varchar(128)," + "cmMrDate varchar(128))");
            }
            set6.close();
            ResultSet set7 = state.executeQuery(sql + "'UserLadder'");
            if (!set7.next()) {
                state.executeUpdate("create table UserLadder(firstGasPrice varchar(128),"

                        + "firstTolerance varchar(128),"
                        + "firstGasFee varchar(128),"
                        + "secondGasPrice varchar(128),"
                        + "secondTolerance varchar(128),"
                        + "secondGasFee varchar(128),"
                        + "thirdGasPrice varchar(128),"
                        + "thirdTolerance varchar(128),"
                        + "thirdGasFee varchar(128)," + "UserNo varchar(128))");
            }
            set7.close();

            // 安检数据表

            // 任务表(schedInfo_aj)
            ResultSet set8 = state.executeQuery(sql + "'schedInfo_aj'");
            if (!set8.next()) {
                state.executeUpdate("create table schedInfo_aj (userID varchar(128),"

                        + "cmSchedId varchar(128),"
                        + "description varchar(128),"
                        + "cmScTypeCd varchar(128),"
                        + "spType   varchar(128),"
                        + "scheduleDateTimeStart varchar(128),"
                        + "cmMrDate varchar(128) default '0' ,"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set8.close();

            // 下载客户信息表(custInfo_ju_aj)
            ResultSet set9 = state.executeQuery(sql + "'custInfo_ju_aj'");
            if (!set9.next()) {
                state.executeUpdate("create table custInfo_ju_aj ("
                        + "cmSchedId varchar(128),"
                        + "fieldActivityId varchar(128),"
                        + "servicePointId varchar(128),"
                        + "spType varchar(128)," + "badgeNumber varchar(128),"
                        + "meterConfigurationId varchar(128),"
                        + "accountId varchar(128),"
                        + "entityName varchar(128),"
                        + "customerClass varchar(128),"
                        + "cmCustClDescr varchar(128),"
                        + "cmMrAddress varchar(128),"
                        + "cmMrDistrict varchar(128),"
                        + "cmMrStreet varchar(128),"
                        + "cmMrCommunity varchar(128),"
                        + "cmMrBuilding varchar(128),"
                        + "cmMrUnit varchar(128),"
                        + "cmMrRoomNum varchar(128),"
                        + "cmScOpenDttm varchar(128),"
                        + "cmScResType varchar(128),"
                        + "cmScUserType varchar(128),"
                        + "meterType varchar(128),"
                        + "manufacturer varchar(128)," + "model varchar(128),"
                        + "serialNumber varchar(128),"
                        + "cmMrMtrBarCode varchar(128),"
                        + "cmMlr varchar(128)," + "cmScLgfmGj varchar(128),"
                        + "cmScLgfmWz varchar(128),"
                        + "cmScLgfmCz varchar(128)," + "cmScZjPp varchar(128),"
                        + "cmScZjYs varchar(128)," + "cmScZjXhbh varchar(128),"
                        + "cmScZjSyrq varchar(128),"
                        + "cmScLjgCz varchar(128)," + "cmScCnlPp varchar(128),"
                        + "cmScCnlPffs varchar(128),"
                        + "cmScCnlSyrq varchar(128),"
                        + "cmScRsqPp varchar(128),"
                        + "cmScRsqPffs varchar(128),"
                        + "cmScRsqSyrq varchar(128),"
                        + "cmScBjqPp varchar(128),"
                        + "cmScBjqSyrq varchar(128),"
                        + "cmMrLastSecchkDt varchar(128),"
                        + "cmScIntvl varchar(128)," + "cmScAqyh varchar(128),"
                        + "cmScYhzg varchar(128)," + "cmScRemark varchar(128),"
                        + "cmMrState varchar(128)," + "cmMrDate varchar(128),"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set9.close();
            // 上传表
            ResultSet set10 = state.executeQuery(sql + "'uploadcustInfo_aj'");
            if (!set10.next()) {
                state.executeUpdate("create table uploadcustInfo_aj ("
                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
                        + "cmScResType varchar(128),"
                        + "cmScUserType varchar(128),"
                        + "cmScDttm varchar(128)," + "cmScAjrh varchar(128),"
                        + "cmScYhzg varchar(128)," + "cmScZgtzd varchar(128),"
                        + "cmScZtjs varchar(128)," + "cmMr varchar(128),"
                        + "readType varchar(128)," + "cmScSyql varchar(128),"
                        + "meterType varchar(128),"
                        + "manufacturer varchar(128)," + "model varchar(128),"
                        + "serialNumber varchar(128),"
                        + "cmMrMtrBarCode varchar(128),"
                        + "cmMlr varchar(128)," + "cmScLgfmGj varchar(128),"
                        + "cmScLgfmWz varchar(128),"
                        + "cmScLgfmCz varchar(128)," + "cmScZjPp varchar(128),"
                        + "cmScZjYs varchar(128)," + "cmScZjXhbh varchar(128),"
                        + "cmScZjSyrq varchar(128),"
                        + "cmScLjgCz varchar(128)," + "cmScCnlPp varchar(128),"
                        + "cmScCnlPffs varchar(128),"
                        + "cmScCnlSyrq varchar(128),"
                        + "cmScRsqPp varchar(128),"
                        + "cmScRsqPffs varchar(128),"
                        + "cmScRsqSyrq varchar(128),"
                        + "cmScBjqPp varchar(128),"
                        + "cmScBjqSyrq varchar(128),"
                        + "cmScNotiPrtd varchar(128),"
                        + "cmMrCommCd varchar(128),"
                        + "cmScRemark varchar(128),"
                        + "cmMrState varchar(128)," + "cmMrDate varchar(128),"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set10.close();

            // 安全讲解(perSp_aj)
            ResultSet set11 = state.executeQuery(sql + "'perSp_aj'");
            if (!set11.next()) {
                state.executeUpdate("create table perSp_aj ("
                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
                        + "cmScSpItem varchar(128),"
                        + "cmScSpCheck varchar(128),"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set11.close();

            // 图片文件(perFile_aj)
            ResultSet set12 = state.executeQuery(sql + "'perFile_aj'");
            if (!set12.next()) {
                state.executeUpdate("create table perFile_aj ("
                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
                        + "cmScFileName varchar(128),"
                        + "cmScFileTitle varchar(128),"
                        + "cmScFileForm varchar(128),"
                        + "cmScBusiType varchar(128),"
                        + "cmScFileRoute varchar(128),"
                        + "cmScFileSize varchar(128),"
                        + "cmScFileDttm varchar(128),"
                        + "cmScFileVar1 varchar(128),"
                        + "cmScFileVar2 varchar(128),"
                        + "cmScdate varchar(128) ,"
                        + "cmMrState varchar(128) default 'N' ,"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set12.close();

            // 隐患信息表(perSh_aj)
            ResultSet set13 = state.executeQuery(sql + "'perSh_aj'");
            if (!set13.next()) {
                state.executeUpdate("create table perSh_aj ("
                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
                        + "cmScShType varchar(128),"
                        + "cmScShTypeDescr varchar(128),"
                        + "cmScShItem varchar(128),"
                        + "cmScShItemDescr varchar(128),"
                        + "cmScShCheck varchar(128),"
                        + "cmScShIsOld varchar(128),"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set13.close();

            // 数据字典_表具-型号(dic_modelInfo_aj)
            ResultSet set14 = state.executeQuery(sql + "'dic_modelInfo_aj'");
            if (!set14.next()) {
                state.executeUpdate("create table dic_modelInfo_aj ("
                        + "manufacturer varchar(128),"
                        + "manufacturerDescr varchar(128),"
                        + "model varchar(128)," + "modelDescr varchar(128),"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set14.close();

            // 数据字典_隐患选项(dic_cmScShItem_aj)
            ResultSet set15 = state.executeQuery(sql + "'dic_cmScShItem_aj'");
            if (!set15.next()) {
                state.executeUpdate("create table dic_cmScShItem_aj ("
                        + "cmScShType varchar(128),"
                        + "cmScShTypeDescr varchar(128),"
                        + "cmScShNO varchar(128)," + "cmScShItem varchar(128),"
                        + "cmScShItemDescr varchar(128),"
                        + "Reserve_space1 varchar(128),"
                        + "Reserve_space2 varchar(128),"
                        + "Reserve_space3 varchar(128),"
                        + "Reserve_space4 varchar(128),"
                        + "Reserve_space5 varchar(128))");
            }
            set15.close();

        } catch (Exception e) {
            e.printStackTrace();
            // 抛异常处理,
            Toast.makeText(getApplicationContext(), "数据库创建失败,", Toast.LENGTH_SHORT).show();
            // System.exit(0);

        } finally {
            // 关闭连接
            try {
                if (state != null)
                    state.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    private void initDeviceId() {
        TelephonyManager telephonemanage = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            deviceId = genarelAndroidId();
            Log.i(MobileApplication.class.getSimpleName(),
                    "deviceId:=====================" + deviceId);
            if (deviceId == null) {
                deviceId = wm.getConnectionInfo().getMacAddress();
                if (deviceId != null)
                    deviceId = deviceId.replaceAll(":", "");
            }
            phoneNumber = telephonemanage.getLine1Number();
            PackageInfo info = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0);
            packageName = info.packageName;
            WebUtils.deviceId = deviceId;
            if (phoneNumber != null && !"".equals(phoneNumber)) {
                WebUtils.phoneNumber = phoneNumber.substring(3,
                        phoneNumber.length());
            } else {
                WebUtils.phoneNumber = "00000000";
            }
            WebUtils.packageName = packageName;
            WebUtils.versionName = VersionUtils.getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        // 属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
            System.out.println(ri.activityInfo.packageName);
        }
        return names;
    }

    private String genarelAndroidId() {
        return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
    }

    private void initDefaultThemeAndSkin() {
        Constants.changeSkin = sp.getBoolean(Constants.CHANGE_SKIN, false);
        Constants.changeBg = sp.getBoolean(Constants.CHANGE_MENU_BG, false);
        Constants.bg_name = sp.getString(Constants.BG_FILENAME, "bg_10.jpg");
        Constants.bg_folder = sp.getString(Constants.BG_FOLDER, "menu-bg");
        Constants.skin_folder = sp.getString(Constants.SKIN_FOLDER, "");
        if ("".equals(Constants.skin_folder))
            Constants.skin_folder = "skin";
        // DataCache.themeMap.put("skin", "yellow");
        // DataCache.themeMap.put("skin-red", "red");
        // DataCache.themeMap.put("skin-blue", "blue");

    }

    private void initBlueThemeAndSkin() {
        Constants.changeSkin = sp.getBoolean(Constants.CHANGE_SKIN, true);
        Constants.changeBg = sp.getBoolean(Constants.CHANGE_MENU_BG, false);
        Constants.bg_name = sp.getString(Constants.BG_FILENAME, "bg_10.jpg");
        Constants.bg_folder = sp.getString(Constants.BG_FOLDER, "menu-bg");
        Constants.skin_folder = sp
                .getString(Constants.SKIN_FOLDER, "skin-blue");
        if ("".equals(Constants.skin_folder))
            Constants.skin_folder = "skin";
        // DataCache.themeMap.put("skin", "yellow");
        // DataCache.themeMap.put("skin-red", "red");
        // DataCache.themeMap.put("skin-blue", "blue");
    }

    public void querySize() {
        new AsyncTask<Void, Void, Double>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Double doInBackground(Void... params) {
                DecimalFormat df = new DecimalFormat("#.00");
                // String sdTotal = (df.format(getSDTotalSize() * 1.0
                // / (1024 * 1024 * 1024)))
                // + "G";
                String sdAvail = (df.format(getSDAvailableSize() * 1.0
                        / (1024 * 1024)))
                        + "MB";
                double avail = getSDAvailableSize() * 1.0 / (1024 * 1024);
                return avail;
            }

            @Override
            protected void onPostExecute(Double result) {
                super.onPostExecute(result);
                if (result != null) {
                    WebUtils.sd_Avail = result;
                } else {

                }
            }
        }.execute();
    }

    /**
     * 1、判断SD卡是否存在
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 计算SD卡的剩余空间
     *
     * @return 剩余空间
     */
    public static long getSDAvailableSize() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return getAvailableSize(Environment.getExternalStorageDirectory()
                    .toString());
        }

        return 0;
    }

    /**
     * 计算剩余空间
     *
     * @param path
     * @return
     */
    private static long getAvailableSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return (long) fileStats.getAvailableBlocks() * fileStats.getBlockSize(); // 注意与fileStats.getFreeBlocks()的区别
    }

    /**
     * 获取SD卡的总空间
     *
     * @return
     */
    public static long getSDTotalSize() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return getTotalSize(Environment.getExternalStorageDirectory()
                    .toString());
        }

        return 0;
    }

    /**
     * 计算总空间
     *
     * @param path
     * @return
     */
    private static long getTotalSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return (long) fileStats.getBlockCount() * fileStats.getBlockSize();
    }

    public void clearActivity() {
        for (Activity activity : activityStack) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    public static MobileApplication getInstance() {
        return application;
    }

    /**
     * 将raw文件中的sqlite数据库导入到项目data/data/下
     */
    public void importInitDatabase() {
        // 数据库的目录
        String dirPath = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
        String dirPath_anjian = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
        File dir = new File(dirPath);
        File dir2 = new File(dirPath_anjian);

        if (!dir2.exists()) {
            dir2.mkdir();
        }

        if (!dir.exists()) {
            dir.mkdir();
        }
        // 数据库文件
        File dbfile = new File(dir, "a10_db.db");
        try {
            if (!dbfile.exists()) {
                dbfile.createNewFile();
                // 加载欲导入的数据库 chinacity.db
                InputStream is = this.getApplicationContext().getResources()
                        .openRawResource(R.raw.a10_db);
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffere = new byte[is.available()];
                is.read(buffere);
                fos.write(buffere);
                is.close();
                fos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // openOrCreateDB();
        // findprovince();

        // 数据库文件
        File dbfile2 = new File(dir2, "cb_db.db");
        try {
            if (!dbfile2.exists()) {
                dbfile2.createNewFile();
                // 加载欲导入的数据库 chinacity.db
                InputStream is = this.getApplicationContext().getResources()
                        .openRawResource(R.raw.cb_db);
                FileOutputStream fos = new FileOutputStream(dbfile2);
                byte[] buffere = new byte[is.available()];
                is.read(buffere);
                fos.write(buffere);
                is.close();
                fos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // openOrCreateDB();
        // findprovince();
    }

    /**
     * 打开数据库
     */
    public void openOrCreateDB() {
//		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
        String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db = SQLiteDatabase.openOrCreateDatabase(path, null);

    }

    /**
     * 数据库查询方法，根据city查询city_Code
     */
    public void findprovince() throws SQLiteException {
        StrCitys = new ArrayList<String>();
        // TODO 对应查询城市区号
        String sql = "select distinct user_name from chaobiao_data where rowid <= 10";
        // 查询数据返回游标对象
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            // 通过游标对象获取值
            StrCitys.add(c.getString(0));// 获取当前行的列为weather_id的值
        }
        c.close();
        for (int i = 0; i < StrCitys.size(); i++) {
            System.out.println(StrCitys.get(i) + "====");

        }
    }

    /**
     * 数据库查询方法，根据省份查询城市名称
     */
    public void findBy_province(String province) throws SQLiteException {
        StrCitys = new ArrayList<String>();
        // TODO 对应查询城市区号
        String sql = "select * from city  where province='" + province + "'";
        // 查询数据返回游标对象
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            // 通过游标对象获取值
            StrCitys.add(c.getString(2));
        }
        c.close();
    }


    public void importInitDatabaseA10() {
        // 数据库的目录
        String dirPath = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
//		String dirPath = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/";

        String dirPath_anjian = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
//		String dirPath_anjian = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/";

        File dir2 = new File(dirPath_anjian);
        if (!dir2.exists()) {
            dir2.mkdir();
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 数据库文件
        File dbfile = new File(dir, "a10_db.db");
        try {
            if (!dbfile.exists()) {
                dbfile.createNewFile();
                // 加载欲导入的数据库 chinacity.db
                InputStream is = getApplicationContext().getResources()
                        .openRawResource(R.raw.a10_db);
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffere = new byte[is.available()];
                is.read(buffere);
                fos.write(buffere);
                is.close();
                fos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // openOrCreateDB();
        // findprovince();
        // 数据库文件

        File dbfile2 = new File(dir2, "cb_db.db");
        try {
            if (!dbfile2.exists()) {
                dbfile2.createNewFile();
                // 加载欲导入的数据库 chinacity.db
                InputStream is = this.getApplicationContext().getResources()
                        .openRawResource(R.raw.cb_db);
                FileOutputStream fos = new FileOutputStream(dbfile2);
                byte[] buffere = new byte[is.available()];
                is.read(buffere);
                fos.write(buffere);
                is.close();
                fos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // openOrCreateDB();
        // findprovince();
    }

    /**
     * 将raw文件中的sqlite数据库导入到项目data/data/下
     * A10_database.db
     */
    public void importInitDB() {
        // 数据库的目录
        String dirPath = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 数据库文件
        File dbfile = new File(dir, "A10_database.db");
        try {
            if (!dbfile.exists()) {
                dbfile.createNewFile();
                // 加载欲导入的数据库 chinacity.db
                InputStream is = getApplicationContext().getResources()
                        .openRawResource(R.raw.init_db);
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffere = new byte[is.available()];
                is.read(buffere);
                fos.write(buffere);
                is.close();
                fos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // openOrCreateDB();
        // findprovince();
    }
}
