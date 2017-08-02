package cn.sbx.deeper.moblie.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.sunboxsoft.monitor.utils.PerfUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.com.petrochina.oos.BSMCPBaseUtils;
import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.SoftInfo;
import cn.sbx.deeper.moblie.logic.MainLoadingListener;
import cn.sbx.deeper.moblie.logic.MainProcess;
import cn.sbx.deeper.moblie.util.APNUtils;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.FileCache;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.LogUtil;
import cn.sbx.deeper.moblie.util.MD5Utils;
import cn.sbx.deeper.moblie.util.SharedPreferenceUtil;
import cn.sbx.deeper.moblie.util.StreamUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.UserInfo;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * Splash界面
 *
 * @author terry.C
 */
public class SplashActivity extends BaseActivity implements MainLoadingListener {
    protected static final String TAG = null;
    Context mContext;
    SharedPreferences sp, sps, databasepwd;
    private AsyncTask<Void, Void, Void> checkNetworkTask;
    private MainProcess mainProcess;
    public static SoftInfo info;
    private SharedPreferences loginNameForUser;
    private SharedPreferences setTextForUserName;
    Editor databseEdit;

    public SplashActivity() {
        mContext = SplashActivity.this;
    }

    String warningPushUsername;
    String warningPushPassword;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 121:
                    PerfUtils.putBoolean(mContext, "loginState", true);
                    PerfUtils.putBoolean(mContext, "isOnline", true);

                    // System.out.println("serverCookie:"+serverCookie);
                    WebUtils.deviceId = application.deviceId;
                    Intent intent = new Intent(SplashActivity.this,
                            SwitchMenuActivity.class).putExtra(
                            "warningPushUsername", warningPushUsername)
                            .putExtra("warningPushPassword",
                                    warningPushPassword);
                    startActivity(intent);
                    SplashActivity.this.finish();
                    overridePendingTransition(
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_left_exit);
                    break;
                default:
                    break;

            }


        }
    };


    private class ScanNetWorkTask implements Runnable {
        String url;

        public ScanNetWorkTask(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
                DefaultHttpClient hc = HttpUtils.initHttpClient(httpParams);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = hc.execute(httpGet);
                if (HttpStatus.SC_OK == httpResponse.getStatusLine()
                        .getStatusCode()) {
                    Message msg = new Message();
                    msg.what = URL_SUCCESS;
                    msg.obj = url;
                    netHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = URL_ERROR;
                netHandler.sendMessage(msg);
            }
        }
    }

    private final int SHOW_PROGRESS = 0;
    private final int DISMISS_PROGRESS = 1;
    private final int URL_SUCCESS = 2;
    private final int URL_ERROR = 3;
    private int i = 0;
    private String checkedUrl;
    private boolean netAvailable = false;
    private ProgressHUD overlayProgress;
    private Handler netHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_PROGRESS:
                    // overlayProgress = AlertUtils.showDialog(
                    // NetSettingActivity.this,
                    // getString(R.string.msg_net_scan_nets), null, false);
                    break;
                case DISMISS_PROGRESS:
                    if (overlayProgress != null)
                        overlayProgress.dismiss();
                    break;
                case URL_SUCCESS:
                    synchronized (this) {
                        if (!netAvailable) {
                            netAvailable = true;
                            checkedUrl = (String) msg.obj;
                            // commitServerUrl();
                            WebUtils.rootUrl = checkedUrl;
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.msg_net_choose_success),
                                    Toast.LENGTH_SHORT).show();
                            if (overlayProgress != null)
                                overlayProgress.dismiss();
                            // finish();
                        }
                    }
                    break;
                case URL_ERROR:
                    synchronized (this) {
                        i++;
                        if (i == 3) {
                            if (overlayProgress != null)
                                overlayProgress.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.msg_net_choose_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    String uKeyName = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_splash_main);
        //打印手持机的名字
        loginNameForUser = getSharedPreferences("LoginNameForUser", MODE_PRIVATE);
        //存储用于显示登陆的姓名
        setTextForUserName = getSharedPreferences("setTextForUserName", MODE_PRIVATE);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();

        if (networkInfo == null) {
            MobileApplication.netType = 0;
        } else {
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                String extraInfo = networkInfo.getExtraInfo();
                if (!android.text.TextUtils.isEmpty(extraInfo)) {
                    if (extraInfo.toLowerCase().equals("cmnet")) {
                        MobileApplication.netType = 3;
                    } else {
                        MobileApplication.netType = 2;
                    }
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                MobileApplication. netType = 1;
            }
        }
        MobileApplication.netType = 1;

        try {
            String ukeyUrl = BSMCPBaseUtils.handleOpenURL(this);
            Log.e("0-0-0-0-0-0-0->", ukeyUrl);

            String loginID = BSMCPBaseUtils.getParams("LoginID", ukeyUrl);
            String loginName = BSMCPBaseUtils.getParams("LoginName", ukeyUrl);
            String s = loginName.substring(loginName.indexOf("=") + 1) + "";
            Editor edit = loginNameForUser.edit();
            edit.putString("LoginNameForSign", s);
            edit.commit();

            Log.e("0-0-0-0-0-0-0->", s);
            Editor edit1 = setTextForUserName.edit();
            edit1.putString("LoginNameForSign_for_show", loginID.substring(loginID.indexOf("/") + 1));
            edit1.commit();
            uKeyName = loginID.substring(loginID.indexOf("/") + 1) + "";//"t1400001"


//            uKeyName = "t1400001";
            Constants.loginName = uKeyName;//
            System.out.println(" Ukey: ukeyUrl=" + ukeyUrl + "  loginID=" + loginID + "  uKeyName= " + uKeyName);
        } catch (Exception e) {
            e.printStackTrace();
//            uKeyName = "";
            String LoginNameForSign_for_show = setTextForUserName.getString("LoginNameForSign_for_show", "");//"t1400001"
            if (LoginNameForSign_for_show.equals("")) {
                Toast.makeText(SplashActivity.this, "第一次请从移动大厅进行登录", Toast.LENGTH_SHORT).show();
            }
            uKeyName = LoginNameForSign_for_show;
            Constants.loginName = uKeyName;
            System.out.println(" Ukey 获取用户异常");
        }
//        setLoginUserText();
        sp = getSharedPreferences("sys_config", Context.MODE_PRIVATE);

        sps = PreferenceManager.getDefaultSharedPreferences(mContext);

        databasepwd = getSharedPreferences("databasepwd", Context.MODE_PRIVATE);

        databseEdit = databasepwd.edit();

        int checked_position = sp.getInt("checked_position", 2);
        if (checked_position == 4) {
            sp.edit().putInt("checked_position", 4).commit();
        }
        switch (checked_position) {
            case 1:
                WebUtils.rootUrl = sp.getString("PREF_MOA_INTRA_ADDR",
                        WebUtils.intranetUrl);
                break;
            case 2:
                WebUtils.rootUrl = sp.getString("PREF_MOA_INTERNET_ADDR",
                        WebUtils.internetUrl);
                break;
            case 3:
                break;
            // case 4://自动检测
            // WebUtils.rootUrl = sp.getString(Constants.serverURL, "");
            // break;
            case 4:
                netHandler.sendEmptyMessage(SHOW_PROGRESS);
                new Thread(new ScanNetWorkTask(sp.getString("PREF_MOA_INTRA_ADDR",
                        WebUtils.intranetUrl).trim())) {
                }.start();
                new Thread(new ScanNetWorkTask(sp.getString(
                        "PREF_MOA_INTERNET_ADDR", WebUtils.internetUrl).trim())) {
                }.start();
                break;
        }
        Intent intent = getIntent();
        if (intent != null) {
            warningPushUsername = intent.getStringExtra("warningPushUsername");
            warningPushPassword = intent.getStringExtra("warningPushPassword");
        }
        FileCache fileCache = new FileCache(mContext);

//
        new UpdateTask().execute();// 20140922

        // mainProcess = new MainProcess();
        // mainProcess.initState(this, this);
        initMobileOffice();
        /*new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    //
                    initDictionary();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Constants.autoScanNet) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            checkNetwork();
                        }
                    });
                } else {*/
                   /* runOnUiThread(new Runnable() {
                        public void run() {*/
//                            startActivity(new Intent(mContext,
//                                    ChooseLoginMode.class).putExtra(
//                                    "warningPushUsername", warningPushUsername)
//                                    .putExtra("warningPushPassword",
//                                            warningPushPassword));
//                            finish();
//                            overridePendingTransition(
//                                    R.anim.fragment_slide_right_enter,
//                                    R.anim.fragment_slide_left_exit);

							/*
                             * startActivity(new Intent(mContext,
							 * LoginActivity.class).putExtra(
							 * "warningPushUsername", warningPushUsername)
							 * .putExtra("warningPushPassword",
							 * warningPushPassword)); finish();
							 * overridePendingTransition(
							 * R.anim.fragment_slide_right_enter,
							 * R.anim.fragment_slide_left_exit);
							 */

                            //
//                            // 沙河Ukey
                            if (uKeyName == null || uKeyName.equals("")) {
                                jumpToLoginActivity();
                            } else {
                                if (!databasepwd.getBoolean("FirstSetPwd", false)) {

                                    System.out.println("Ukey 登录   ukeyName= " + uKeyName);
                                    login(uKeyName, MD5Utils.getValues(uKeyName));//("liuyinliang","ibelin");//


                                } else {
                                    //TODO 这个地方是第二次登录的时候直接获取数据库的密码

                                    System.out.println("Ukey 登录   ukeyName= " + uKeyName);
                                    login(uKeyName, MD5Utils.getValues(uKeyName));//("liuyinliang","ibelin");//



                                }


                            }
                            overridePendingTransition(R.anim.fragment_slide_right_enter,
                                    R.anim.fragment_slide_left_exit);
                       /* }
                    });*/
              /*  }*/

           /* }
        }.start();*/
    }

    private void getDatabasePwd(final String uKeyNamePwd) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String s = WebServicesUtil.connectWebServiceTest2(uKeyNamePwd);
//                if (s != null) {
//
//                    if(s.equals("Exist")){
//                        Toast.makeText(SplashActivity.this, "没有查询倒相应的密码", Toast.LENGTH_SHORT).show();
//
//                        return;
//                    }else{
//                        DatabaseHelper.PASSPHRASE=s;
//                        Message msg = Message.obtain();
//                        msg.what=121;
//                        mHandler.sendMessage(msg);
//
//                    }
//
//                } else {
//                    Toast.makeText(SplashActivity.this, "获取数据库密码有误", Toast.LENGTH_SHORT).show();
//                    return;
//
//                }
//            }
//        }).start();
//
//






        new AsyncTask<Void, Void, String>() {
            private volatile boolean running = true;
            private ProgressHUD overlayProgress;
            private String errorMessage = getString(R.string.msg_login_net_or_server_error);
            private String serverCookie;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (overlayProgress != null)
                    overlayProgress.dismiss();
                if (result != null) {

                    if(result.equals("Exist")){
                        Toast.makeText(SplashActivity.this, "没有查询倒相应的密码", Toast.LENGTH_SHORT).show();

                        return;
                    }else{
                        DatabaseHelper.PASSPHRASE=result;
                        Message msg = Message.obtain();
                        msg.what=121;
                        mHandler.sendMessage(msg);

                    }

                } else {
                    Toast.makeText(SplashActivity.this, "获取数据库密码有误", Toast.LENGTH_SHORT).show();
                    return;

                }
            }

            protected void onCancelled() {
                running = false;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                overlayProgress = AlertUtils.showDialog(SplashActivity.this,
                        getString(R.string.ms_login_notice), this, running);
                overlayProgress.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        running = false;
                    }
                });
            }


            @Override
            protected String doInBackground(Void... params) {
                WebUtils.role = "0";
                DataCollectionUtils.checkRole(uKeyNamePwd); // 检测开发者角色
                InputStream in = null;
                try {
                    MobileApplication application = (MobileApplication) getApplication();


                    System.out.println("Login:" + WebUtils.rootUrl
                            + URLUtils.getDatabaseURL);

                    HttpParams httpParameters = new BasicHttpParams();
                    DefaultHttpClient hc = HttpUtils
                            .initHttpClient(httpParameters);
                    HttpPost post = new HttpPost(WebUtils.rootUrl
                            + URLUtils.getDatabaseURL);
                    post.setHeader("Cookie", WebUtils.cookie);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("EquipType",
                            "Android"));
                    nameValuePairs.add(new BasicNameValuePair("EquipSN",
                            WebUtils.deviceId));

                    nameValuePairs.add(new BasicNameValuePair("RequestType",
                            "pull"));

                    nameValuePairs.add(new BasicNameValuePair("userId",
                            uKeyNamePwd));
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                            "UTF-8"));

                    HttpResponse response = hc.execute(post);
                    if (200 == response.getStatusLine().getStatusCode()) {
                        /*
                         * 【0】=》登录成功 【1】=》用户禁用 【2】=》设备未激活 【3】=》设备禁用
						 * 【4】=》用户名或密码错误 【5】=》验证模式为一对一验证，但已存在该设备或用户信息 【6】=》登录异常
						 */
                        // in = conn.getInputStream();
                        HttpEntity entity = response.getEntity();
                        in = entity.getContent();
                        String s = StreamUtils.retrieveContent(in);
                        System.out.println("stream =" + s);

                        in = new ByteArrayInputStream(s.getBytes());


                        if (s != null) {
                            return s;
                        }
                    }
                    return "";

                } catch (Exception e) {
                    e.printStackTrace();
                    //加密机未连接
                    return "";
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }.execute();


    }

    private void setLoginUserText() {
        String select_user = "select * from login_user where username = "
                + "'" + Constants.loginName + "'" + "";
        String sql_insert = "insert into login_user (userID,password,username) values ("
                + "'"
                + Constants.loginName
                + "'"
                + ","
                + "'"
                + "111111"
                + "'"
                + "," + "'" + Constants.loginName + "'" + ")";
        String sql_update = "update login_user set userID = " + "'"
                + Constants.loginName + "'" + ",password = " + "'" + "111111"
                + "'" + "";
        Connection db1 = null;
        Statement state = null;
        ResultSet set = null;
        try {
            db1 = SQLiteData.openOrCreateDatabase(SplashActivity.this);
            state = db1.createStatement();
            set = state.executeQuery(select_user);
            if (set.next()) {
                state.executeUpdate(sql_update);
            } else {
                state.executeUpdate(sql_insert);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db1 != null) {
                try {
                    db1.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (set != null) {
                try {
                    set.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private void initMobileOffice() {
        // TODO Auto-generated method stub
        Editor e = sp.edit();
        e.putString("PREF_APN_DISPLAY_NAME", "燕山石化(APN)");
        e.putString("PREF_APN_NAME", "YSSH-DDN.BJ");
        e.putString("PREF_MOA_INTRA_ADDR_FOR_APN",
                "http://172.19.204.1/DPXML_APN/");
        // // e.putString("serverURL", "http://10.101.6.70/xml/");
        // e.putString("serverURL", "http://172.19.204.1/DPXML_APN/");
        // e.putBoolean("PREF_DEFAULT_AS_APN", true);
        // e.putBoolean("PREF_DEFAULT_AS_NET_DIRECT", false);
        // e.commit();
        try {

            // System.out.println("Model=> "+android.os.Build.MODEL);
            // System.out.println("Brand=> "+android.os.Build.BRAND);
            WebUtils.sdkVersion = getAndroidSDKVersion();
            if (WebUtils.sdkVersion > 16) {// android4.1以上 做特殊处理
                // apn设置 非燕山项目注释掉
                // Intent intent = new
                // Intent(android.content.Intent.ACTION_MAIN);
                // intent.setClassName("com.android.settings",
                // "com.android.settings.ApnSettings");

                // startActivity(intent);
                // Toast.makeText(mContext, "请手动添加燕山石化APN并选中！",
                // Toast.LENGTH_LONG)
                // .show();
                return;
            }

            // 检测
            APNUtils.setContext(mContext);

            int currApnId = APNUtils.fetchCurrentApnId();

            int apnId = APNUtils.fetchAPNId("YSSH-DDN.BJ");
            if (currApnId == apnId)
                return;
            // -? 没有，则插入，获得ID
            // -: 有，则获得ID
            if (apnId != -1)
                APNUtils.deleteAPN(apnId);
            apnId = APNUtils.insertAPN("燕山石化(APN)", "YSSH-DDN.BJ");
            // 根据ID来启用
            if (apnId != -1 && APNUtils.SetDefaultAPN(apnId))
                ;
            else {
                Toast.makeText(mContext, "默认APN设置失败，请手动设置！", Toast.LENGTH_SHORT)
                        .show();
            }
            if (apnId != currApnId)
                e.putInt("PREF_CURR_NORMAL_APN_ID", currApnId);
        } catch (Exception ex) {
            ex.printStackTrace();
            APNUtils.setContext(mContext);
            int ysAPN = APNUtils.fetchAPNId("YSSH-DDN.BJ");
            if (ysAPN != -1) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText("YSSH-DDN.BJ");
                // 非燕山项目注释掉
                // Intent intent = new
                // Intent(android.content.Intent.ACTION_MAIN);
                // intent.setClassName("com.android.settings",
                // "com.android.settings.ApnSettings");
                // startActivity(intent);
                // Toast.makeText(mContext, "请选中燕山石化APN，以使用移动办公系统！",
                // Toast.LENGTH_LONG).show();
            } else {
                try {
                    // 非燕山项目注释掉
                    // Intent intent = new Intent(
                    // android.content.Intent.ACTION_INSERT);
                    // intent.setClassName("com.android.settings",
                    // "com.android.settings.ApnEditor");
                    // intent.setData(Uri.parse("content://telephony/carriers"));
                    //
                    // startActivity(intent);
                    // Toast.makeText(mContext, "正确设置APN信息后，请按菜单键进行保存！",
                    // Toast.LENGTH_LONG).show();
                } catch (Exception ee) {
                    ee.printStackTrace();
                    // 非燕山项目注释掉
                    // Intent intent = new Intent(
                    // android.content.Intent.ACTION_MAIN);
                    // intent.setClassName("com.android.settings",
                    // "com.android.settings.ApnSettings");
                    // startActivity(intent);
                    // Toast.makeText(mContext, "请按菜单键，新建燕山石化介入点！",
                    // Toast.LENGTH_LONG).show();
                }
            }
        }
        e.commit();
    }

    private void checkNetwork() {
        checkNetworkTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Toast.makeText(getApplicationContext(), "正在选择网络环境...",
                // Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (!Constants.useVPN) {
                    Toast.makeText(getApplicationContext(), "使用内网登录",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "使用vpn登录",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(mContext, LoginActivity.class));
                    Constants.useVPN = false;
                    finish();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
                    DefaultHttpClient hc = HttpUtils.initHttpClient(httpParams);
                    HttpGet httpGet = new HttpGet(WebUtils.rootUrl
                            + Constants.networkAvailable);
                    Log.i(TAG, "test INTRANET_URL url:" + WebUtils.rootUrl
                            + Constants.networkAvailable);
                    HttpResponse httpResponse = hc.execute(httpGet);
                    if (HttpStatus.SC_OK == httpResponse.getStatusLine()
                            .getStatusCode()) {
                        InputStream is = httpResponse.getEntity().getContent();
                        String result = StreamUtils.retrieveContent(is);
                        if (result != null
                                && "".equalsIgnoreCase(result.trim())) {
                            Constants.useVPN = false;
                            return null;
                        }
                    } else {
                        Constants.useVPN = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.useVPN = true;
                }

                return null;
            }
        }.execute();
    }

    protected void initDictionary() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(),
                    Constants.home_cache + "/oa");
            System.out.println("剩余空间：" + WebUtils.sd_Avail);
            if (!file.exists()) {
                if (WebUtils.sd_Avail < 5) {
                    Toast.makeText(SplashActivity.this, "sd卡剩余空间太小,请清理空间",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                file.mkdirs();
            }
            if (WebUtils.sd_Avail < 10) {
                Toast.makeText(SplashActivity.this, "sd卡剩余空间太小,可能导致附件不可查看",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SplashActivity.this, "未安装SD卡,可能导致附件不可查看",
                    Toast.LENGTH_SHORT).show();
        }
    }

    class UpdateTask extends AsyncTask<Void, Void, SoftInfo> {
        @Override
        protected void onPostExecute(SoftInfo result) {
            super.onPostExecute(result);
            if (result != null) {
                info = result;
                if (info.getUpdateLog() == null
                        || info.getUpdateLog().equals("")) {
                    info.setUpdateLog("已有新版本，是否更新");
                }
                sps.edit().putString("updateLog", info.getUpdateLog()).commit();
            }
        }

        @Override
        protected SoftInfo doInBackground(Void... params) {

            return DataCollectionUtils.receiverUpdateData();
        }

    }

    @Override
    public void checkUpdate(SoftInfo softInfo) {
        info = softInfo;
    }

    @Override
    public void initMainView() {
        // TODO Auto-generated method stub

    }

    /**
     * 判断手机系统版本
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        // Build bd = new Build();
        // String aa = bd.MODEL;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
            // WebUtils.brand = android.os.Build.BRAND;//samsung
            // WebUtils.systemVersion = android.os.Build.VERSION.RELEASE;//4.0.4

            // System.out.println("android.os.Build.VERSION.RELEASE="+android.os.Build.VERSION.RELEASE);//VERSION.RELEASE=4.0.3
            // System.out.println("android.os.Build.VERSION.SDK="+android.os.Build.VERSION.SDK);//VERSION.SDK=15
            // System.out.println("android.os.Build.VERSION.CODENAME="+android.os.Build.VERSION.CODENAME);
            // System.out.println("android.os.Build.VERSION.SDK_INT="+android.os.Build.VERSION.SDK_INT);
            // System.out.println("android.os.Build.BOARD="+android.os.Build.BOARD);
            // System.out.println("android.os.Build.BRAND="+android.os.Build.BRAND);
            // System.out.println("android.os.Build.MODEL="+android.os.Build.MODEL);//GT-I9228
            // System.out.println("android.os.Build.PRODUCT="+android.os.Build.PRODUCT);
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }
        return version;
    }

    private int error_code = 0;

    private void login(final String username, final String password) {

        new AsyncTask<Void, Void, Boolean>() {
            private volatile boolean running = true;
            private ProgressHUD overlayProgress;
            private String errorMessage = getString(R.string.msg_login_net_or_server_error);
            private String serverCookie;

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (overlayProgress != null)
                    overlayProgress.dismiss();
                if (result) {     // //登录成功 用户名和密码保存到sp，记录相关状态 保存Cookie 进入主界面
                    databseEdit.putBoolean("FirstSetPwd", true);
                    databseEdit.commit();
                    MobileApplication application = (MobileApplication) getApplication();
                    application.cookie = serverCookie;
                    UserInfo.getInstance().setUsername(username);
                    UserInfo.getInstance().setPassword(password);
                    PerfUtils.putString(mContext, "userName", username);
                    // Editor editor = sp.edit();
                    // editor.putString("serverURL", serverURL);
                    // editor.commit();
                    // WebUtils.rootUrl = sp.getString("serverURL", "");
                    WebUtils.cookie = serverCookie;
                    if (WebUtils.role.equals("0")) {
                        Constants.developerMode = "0";
                    }

                    if ((boolean) (SharedPreferenceUtil.getData(SplashActivity.this, "Boolean", false))) {
                        getDatabasePwd(uKeyName);

//                        PerfUtils.putBoolean(mContext, "loginState", true);
//                        PerfUtils.putBoolean(mContext, "isOnline", true);
//
//                        // System.out.println("serverCookie:"+serverCookie);
//                        WebUtils.deviceId = application.deviceId;
//                        Intent intent = new Intent(SplashActivity.this,
//                                SwitchMenuActivity.class).putExtra(
//                                "warningPushUsername", warningPushUsername)
//                                .putExtra("warningPushPassword",
//                                        warningPushPassword);
//                        startActivity(intent);
//                        SplashActivity.this.finish();
//                        overridePendingTransition(
//                                R.anim.fragment_slide_right_enter,
//                                R.anim.fragment_slide_left_exit);

                    } else {
                        PerfUtils.putBoolean(mContext, "isOnline", true);
                        PerfUtils.putBoolean(mContext, "loginState", true);
                        Intent intent = new Intent(SplashActivity.this,
                                DatabaseSetPWDActivity.class).putExtra(
                                "warningPushUsername", warningPushUsername)
                                .putExtra("warningPushPassword",
                                        warningPushPassword);
                        startActivity(intent);
                        SplashActivity.this.finish();
                        overridePendingTransition(
                                R.anim.fragment_slide_right_enter,
                                R.anim.fragment_slide_left_exit);

                    }
                } else {
                    switch (error_code) {
                        case 1:
                            Toast.makeText(
                                    mContext,
                                    getString(R.string.msg_login_result_user_disable),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(
                                    mContext,
                                    getString(R.string.msg_login_result_device_un_activitied),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(
                                    mContext,
                                    getString(R.string.msg_login_result_device_disable),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(
                                    mContext,
                                    getString(R.string.msg_login_result_username_password_error),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            Toast.makeText(
                                    mContext,
                                    getString(R.string.msg_login_result_user_existed),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 6:
                            Toast.makeText(
                                    mContext,
                                    getString(R.string.msg_login_result_device_exception),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 7:  //进入到里到离线登录界面
                            Toast.makeText(
                                    mContext,
                                    getString(R.string.msg_login_net_error),
                                    Toast.LENGTH_SHORT).show();
                            jumpToLoginActivity();
                            break;
                        default:
                            Toast.makeText(mContext, errorMessage,
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            protected void onCancelled() {
                running = false;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                overlayProgress = AlertUtils.showDialog(SplashActivity.this,
                        getString(R.string.ms_login_notice), this, running);
                overlayProgress.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        running = false;
                    }
                });
            }


            @Override
            protected Boolean doInBackground(Void... params) {
                WebUtils.role = "0";
                LogUtil.e("123456789034567893456723456","走了");
                DataCollectionUtils.checkRole(username); // 检测开发者角色
                InputStream in = null;
                try {
                    MobileApplication application = (MobileApplication) getApplication();

                    String identification = "";
                    String param = "&EquipSN=" + application.deviceId
                            + "&EquipType=Android" + "&Soft="
                            + Constants.testPackage;
                    byte[] data = (identification + "Username=" + username
                            + "&Password=" + password + param)
                            .getBytes("UTF-8");

//                    WebUtils.rootUrl = "http://127.0.0.1:10092/DMCWebTest/";

                    System.out.println("Login:" + WebUtils.rootUrl
                            + URLUtils.splashActivityURL);

                    HttpParams httpParameters = new BasicHttpParams();
                    DefaultHttpClient hc = HttpUtils
                            .initHttpClient(httpParameters);
                    HttpPost post = new HttpPost(WebUtils.rootUrl
                            + URLUtils.splashActivityURL);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("Username",
                            username));
                    nameValuePairs.add(new BasicNameValuePair("Password",
                            password));
                    nameValuePairs.add(new BasicNameValuePair("LoginType",
                            "ukey"));
                    nameValuePairs.add(new BasicNameValuePair("EquipSN",
                            application.deviceId));
                    nameValuePairs.add(new BasicNameValuePair("EquipType",
                            "Android"));
                    nameValuePairs.add(new BasicNameValuePair("Soft",
                            Constants.testPackage));
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                            "UTF-8"));
                    HttpResponse response = hc.execute(post);
                    if (200 == response.getStatusLine().getStatusCode()) {
                        /*
                         * 【0】=》登录成功 【1】=》用户禁用 【2】=》设备未激活 【3】=》设备禁用
						 * 【4】=》用户名或密码错误 【5】=》验证模式为一对一验证，但已存在该设备或用户信息 【6】=》登录异常
						 */
                        // in = conn.getInputStream();
                        HttpEntity entity = response.getEntity();
                        in = entity.getContent();
                        String s = StreamUtils.retrieveContent(in);
                        System.out.println("stream =" + s);

                        in = new ByteArrayInputStream(s.getBytes());

                        String responseData = "6";
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setInput(in, "UTF-8");
                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_TAG:
                                    if ("message"
                                            .equalsIgnoreCase(parser.getName())) {
                                        responseData = parser.nextText();
                                        break;
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                        if (responseData != null) {
                            if ("1".equals(responseData)) {
                                error_code = 1;
                                return false;
                            } else if ("2".equals(responseData)) {
                                error_code = 2;
                                return false;
                            } else if ("3".equals(responseData)) {
                                error_code = 3;
                                return false;
                            } else if ("4".equals(responseData)) {
                                error_code = 4;
                                return false;
                            } else if ("5".equals(responseData)) {
                                error_code = 5;
                                return false;
                            } else if ("6".equals(responseData)) {
                                error_code = 6;
                                return false;
                            }
                        }
                        // 登录成功
                        List<Cookie> cookies = hc.getCookieStore().getCookies();
                        for (Cookie cookie : cookies) {
                            if ("UserInfo".equalsIgnoreCase(cookie.getName())) {
                                // sessionOnly:TRUE domain:"moatest.hecmcc.com"
                                // path:"/
                                String cookieString = cookie.getName() + "="
                                        + cookie.getValue() + "; domain="
                                        + cookie.getDomain();
                                System.out.println("cookieString:"
                                        + cookie.toString());
                                if (cookieString == null
                                        || "".equals(cookieString)) {
                                    errorMessage = getString(R.string.msg_login_result_username_password_error);
                                    return false;
                                }
                                serverCookie = cookieString;
                            }
                        }
                        Editor editor = sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        MobileApplication.userName = username;
                        editor.commit();
                        return running;
                    } else {
                        //加密机未连接
                        error_code = 7;
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //加密机未连接
                    error_code = 7;
                    return false;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }.execute();
    }

    private void jumpToLoginActivity() {
        PerfUtils.putBoolean(mContext, "loginState", false);
        PerfUtils.putBoolean(mContext, "isOnline", false);
        startActivity(new Intent(mContext,
                LoginActivity.class).putExtra("warningPushUsername",
                warningPushUsername)
                .putExtra("warningPushPassword",
                        warningPushPassword));//.putExtra("isOnLine",true)
        finish();
    }
}
