package cn.sbx.deeper.moblie.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunboxsoft.monitor.utils.PerfUtils;

import net.arraynetworks.vpn.Common;
import net.arraynetworks.vpn.VPNManager;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.SoftInfo;
import cn.sbx.deeper.moblie.logic.MainProcess;
import cn.sbx.deeper.moblie.service.DownloadServiceUpdate;
import cn.sbx.deeper.moblie.util.APNUtils;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.StreamUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.UserInfo;
import cn.sbx.deeper.moblie.util.VersionUtils;
import cn.sbx.deeper.moblie.util.VpnUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.LoginContainerLayout;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 系统登录界面
 *
 * @author terry.C
 */
public class LoginActivity extends BaseActivity implements OnClickListener,
        OnCheckedChangeListener {

    protected static final String TAG = LoginActivity.class.getSimpleName();
    private TextView et_username_login;
    private EditText et_password_login;
    private CheckBox cb_remind_password;
    private TextView tv_val_code_send_info;
    private Button bt_login_confirm, bt_setting, btn_fetch_val_code;
    CheckBox _cbAutoLogon;
    private ImageButton bt_login_cancel;
    private SharedPreferences sp;
    private String username;
    private String password;
    private String sUsername;
    private String sPassword;
    String warningPushUsername;
    String warningPushPassword;
    Boolean sPcbAutoLogon;
    Context mContext;
    private Thread mThreadStartVpn = null;
    private ProgressHUD overlayProgress;
    SharedPreferences sps;
    boolean flag = true;
    AlertDialog softUpdateDialog;

    private boolean isOnLine;// 登录的
    private LinearLayout password_confirmaction_linearLayout;//第一次登录的时候确认密码的布局
    private EditText et_password_login_confirmation;//等一次登录的时候确认密码的输入框

    public LoginActivity() {
        mContext = LoginActivity.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DatabaseHelper.encrypt(LoginActivity.this);
        // setContentView(R.layout.layout_login_main);
        //测试md5加盐加密
        //		String password = PasswordUtil.generate("admin");
        //		Boolean aBoolean =PasswordUtil.verify("admin", password);
        //		if (aBoolean){
        //			Toast.makeText(LoginActivity.this,"",Toast.LENGTH_SHORT).show();
        //		}

        sps = PreferenceManager.getDefaultSharedPreferences(mContext);
        setContentView(R.layout.layout_login_content);// 标准版
        // setContentView(R.layout.layout_login_content_hb);//河北
        Intent intent = getIntent();
        if (intent != null) {
            warningPushUsername = intent.getStringExtra("warningPushUsername");
            warningPushPassword = intent.getStringExtra("warningPushPassword");
            isOnLineLogin = intent.getBooleanExtra("isOnLine", false);
        }

        sp = getSharedPreferences("sys_config", Context.MODE_PRIVATE);
        sPcbAutoLogon = sp.getBoolean("_cbAutoLogon", false);
        // String path =
        // "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";
        // db = SQLiteDatabase.openOrCreateDatabase(path, null);
        getNetWorkType(mContext);// 判断网络状态

        setupView();
        initState();
        // VPNManager.getInstance().setHandler(mVPNHandler);
        info = SplashActivity.info;
        String version = getVersionName();
        if (flag) {
            if (info != null) {
                if (isNewer(info.getVersion(), version)) {
                    if (!"".equals(sps.getString("localApk", ""))) {
                        // 如果本地已经下载完了包
                        dialogUpdate(mContext, "开始安装", "1",
                                sps.getString("localApk", ""), info);
                    } else {
                        if (!"1".equals(info.getForced())) {// 非强制更新
                            if (LoginActivity.mNetWorkType == 4) {
                                // wifi 情况下直接下载更新
                                Intent i = new Intent(mContext,
                                        DownloadServiceUpdate.class);
                                i.putExtra("url", info.getDownloadUrl());
                                i.putExtra("notifyId", 0);
                                mContext.startService(i);
                            } else {// 3g情况
                                dialogUpdate(mContext, "开始更新", "2",
                                        sps.getString("localApk", ""), info);
                            }
                        } else {
                            // 强制更新
                            dialogUpdate(mContext, "立即更新", "3",
                                    sps.getString("localApk", ""), info);
                        }
                        // showUpdateDialog(info);
                        // flag = false;
                        // return;
                    }
                    flag = false;
                    return;
                }
            }
        }
//        if (warningPushUsername != null && warningPushPassword != null) {
//            login(warningPushUsername, warningPushPassword);
//            return;
//        }
//        if (sPcbAutoLogon) {
//            login(sUsername, sPassword);
//
//            // return;
//        }
        // // 启动集群通
        // String packageName = "com.zed3.sipua";
        // ComponentName componentName = new ComponentName(packageName,
        // startActivity(packageName));
        // Intent intent1 = new Intent();
        // intent1.setComponent(componentName);
        // intent1.setAction(Intent.ACTION_VIEW);
        // intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent1);
    }

    private String startActivity(String packageName) {
        if (packageName.equalsIgnoreCase("com.zed3.sipua")) {
            return "com.zed3.sipua.ui.splash.SplashActivity";
        }
        return "";
    }

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

                            login(username, password);
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

        ;
    };

    /**
     * 当前包名
     *
     * @return 当前应用的当前包名
     */
    public String getPackageName() {
        String packageName = VersionUtils.getPackageName();
        return packageName;
    }

    private String genarelAndroidId() {
        return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
    }

    private void initState() {
        sUsername = sp.getString("username", "");//140001
        sPassword = sp.getString("password", "");//qwe123
//        et_username_login.setText(sUsername);
        et_username_login.setText(Constants.loginName);

//        String select_user = "select * from login_user where username = "
//                + "'" + Constants.loginName + "'" + "";
//        String select_user_password = "select * from login_user where password = "
//                + "'" + Constants.loginName + "'" + "";
//
//        Connection db1 = null;
//        Statement state = null;
//        ResultSet set = null;
//        try {
//            db1 = SQLiteData.openOrCreateDatabase(LoginActivity.this);
//            state = db1.createStatement();
//            set = state.executeQuery(select_user);
//            while (set.next()) {
//                String select_user_name= set.getString("username");
//                String select_user_pwd= set.getString("password");
//                if((Constants.loginName.equals(select_user_name))){
//                    et_password_login.setText(select_user_pwd);
//                    break;
//                }else{
//                    et_password_login.setText("");
//                }
//
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (db1 != null) {
//                try {
//                    db1.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if (set != null) {
//                try {
//                    set.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }

        if (sp.getBoolean("remindPassword", false)) {
            et_password_login.setText(sPassword);
        } else {
            et_password_login.setText("");//abcd1234!
        }
        cb_remind_password.setChecked(sp.getBoolean("remindPassword", true));
    }

    // @Override
    // protected void onResume() {
    // super.onResume();
    // serverURL = sp.getString(Constants.serverURL, WebUtils.rootUrl);
    // }

    private void setupView() {
        bt_setting = (Button) findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener(this);
        et_username_login = (TextView) findViewById(R.id.et_username_login);
        // et_username_login.setOnClickListener(this);
        et_password_login = (EditText) findViewById(R.id.et_password_login);
        // et_password_login.setOnClickListener(this);
        password_confirmaction_linearLayout = (LinearLayout) findViewById(R.id.password_confirmaction_linearLayout);
        et_password_login_confirmation = (EditText) findViewById(R.id.et_password_login_confirmation);
        cb_remind_password = (CheckBox) findViewById(R.id.cb_remind_password);
        cb_remind_password.setOnCheckedChangeListener(this);
        bt_login_confirm = (Button) findViewById(R.id.bt_login_confirm);
        btn_fetch_val_code = (Button) findViewById(R.id.btn_fetch_val_code);
        bt_login_confirm.setOnClickListener(this);
        btn_fetch_val_code.setOnClickListener(this);
        _cbAutoLogon = (CheckBox) findViewById(R.id.cb_auto_logon);
        _cbAutoLogon.setOnCheckedChangeListener(this);
        _cbAutoLogon.setChecked(sPcbAutoLogon);
        tv_val_code_send_info = (TextView) findViewById(R.id.tv_val_code_send_info);
    }

    private boolean select_for_intent_activity = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

		/*
         * case R.id.et_username_login:
		 * bt_login_cancel.setVisibility(View.GONE); break; case
		 * R.id.et_password_login: bt_login_cancel.setVisibility(View.GONE);
		 * break;
		 */
            // 单击登录按钮
            case R.id.bt_login_confirm:
                username = et_username_login.getText().toString();//"t1400001"
                password = et_password_login.getText().toString();


                //----------------------
                /*String select_user = "select * from login_user where username = "
                        + "'" + Constants.loginName + "'" + "";
                String select_user_password = "select * from login_user where password = "
                        + "'" + Constants.loginName + "'" + "";

                Connection db1 = null;
                Statement state = null;
                ResultSet set = null;
                try {
                    db1 = SQLiteData.openOrCreateDatabase(LoginActivity.this);
                    state = db1.createStatement();
                    set = state.executeQuery(select_user);
                    while (set.next()) {
                        String select_user_name = set.getString("username");
                        String select_user_pwd = set.getString("password");
                        if ((username.equals(select_user_name)) && (password.equals(select_user_pwd))) {
                            Editor editor = sp.edit();
                            if (cb_remind_password.isChecked()) {
                                editor.putBoolean("remindPassword", true);
                            } else {
                                editor.putBoolean("remindPassword", false);
                            }

                            editor.commit();

                            select_for_intent_activity = true;
                            break;
                        }
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


                if (select_for_intent_activity) {
                    if (cb_remind_password.isChecked()) {
                        Editor editor = sp.edit();
                        editor.putString("password", password);
                        editor.commit();
                    } else {
                        Editor editor = sp.edit();
                        editor.putString("password", "");
                        editor.commit();
                    }


                    Intent intent = new Intent(LoginActivity.this,
                            SwitchMenuActivity.class).putExtra(
                            "warningPushUsername", warningPushUsername)
                            .putExtra("warningPushPassword",
                                    warningPushPassword);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    overridePendingTransition(
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_left_exit);
                } else {
                    Toast.makeText(LoginActivity.this, "第一次登录需在线登录", Toast.LENGTH_SHORT).show();
                }*/
                //-------------------


                if ("".equals(username)) {
                    Toast.makeText(LoginActivity.this, "帐号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(password)) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_username_login.getWindowToken(),
                            0);
                    imm.hideSoftInputFromWindow(et_password_login.getWindowToken(),
                            0);
                    // if(Constants.useVPN) {
                    // if(Common.VpnStatus.CONNECTED !=
                    // VPNManager.getInstance().getStatus()) {
                    // startConnectVpn();
                    // }else {
                    // login(username, password);
                    // }
                    // }else {
                    // 选择在线登录方式
                    if (isOnLineLogin) {
                        if (MobileApplication.netType == 0) { // 如果没有网络
                            // 选择的登录方式是在线,则不可登录
                            // Toast.makeText(mContext,
                            // R.string.no_available_net+"进入离线",
                            // Toast.LENGTH_LONG).show();

                            Toast.makeText(mContext, "进入离线",
                                    Toast.LENGTH_LONG).show(); // 改变登陆地状态 并保存
                            isOnLine = false;
                            PerfUtils.putBoolean(mContext, "isOnline",
                                    isOnLine);
                            Intent intent1 = new
                                    Intent(LoginActivity.this,
                                    SwitchMenuActivity.class).putExtra(
                                    "warningPushUsername", warningPushUsername)
                                    .putExtra("warningPushPassword",
                                            warningPushPassword);
                            startActivity(intent1);
                            LoginActivity.this.finish();
                            overridePendingTransition(
                                    R.anim.fragment_slide_right_enter,
                                    R.anim.fragment_slide_left_exit);

                            Toast.makeText(mContext, "网络未连接,请检查网络设置", Toast.LENGTH_LONG).show();

                        } else if (WebUtils.rootUrl.equals("")) {
                            new Thread(new ScanNetWorkTask(sp.getString(
                                    "PREF_MOA_INTRA_ADDR", WebUtils.intranetUrl)
                                    .trim())) {
                            }.start();
                            new Thread(new ScanNetWorkTask(sp.getString(
                                    "PREF_MOA_INTERNET_ADDR", WebUtils.internetUrl)
                                    .trim())) {
                            }.start();
                        } else {
                            // 在线登录
                            login(username, password);
                        }
                        // }
                    } else {
                        // 选择离线登录方式,
                        String login_username = PerfUtils.getString(mContext, "userName", "");
                        // 获取第一次登陆的用户名和密码 ，如果不存在 则是第一次登陆 不能离线登录
                        if (TextUtils.isEmpty(login_username) || login_username.equals("null")) {
                            Toast.makeText(mContext, "首次登录请选择在线登录",
                                    Toast.LENGTH_SHORT).show();
                        } else {
//						 校验离线密码,通过连接数据库
                            if (login_username.equals(username)) {
                                Constants.loginName = username;// 记录登录名称,
                                boolean tagForExist=false;
                                try {
//                                    Connection db1 = SQLiteData
//                                            .openOrCreateDatabase(Constants.path_db, (String) (SharedPreferenceUtil.getData(LoginActivity.this, "String", "")));//"test123456"
//                                    DatabaseHelper db=new DatabaseHelper(mContext);
//                                   SQLiteDatabase writableDatabase = db.getWritableDatabase();
//
//                                    String select_user = "select * from login_user where username = ?";
//
//                                    Cursor cursor = writableDatabase.rawQuery(select_user,new String[]{Constants.loginName});

//                                    Cursor cursor = writableDatabase.query(DatabaseHelper.LOGIN_USER, new String[]{"ROWID AS _id",//
//                                            DatabaseHelper.USERNAME,
//                                            DatabaseHelper.PASSWORD}, null, null, null, null, null);

                                    //判断游标是否为空
//                                    if (cursor.moveToFirst()) {
//                                        //遍历游标
//                                        for (int i = 0; i < cursor.getCount(); i++) {
//                                            cursor.move(i);
//                                            String username1 = cursor.getString(1);
//                                            String pwd = cursor.getString(2);
//                                            if(pwd.equals(password)&&username1.equals(username)){
//                                                tagForExist=true;
//                                                break;
//                                            }
//
//                                        }
//                                    }

//                                    if(tagForExist){

//                                    }else{
//                                        Toast.makeText(mContext, "密码不正确，请您重新输入",
//                                                Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }
                                    DatabaseHelper.PASSPHRASE=password;

                                    Toast.makeText(mContext, "您已进入离线状态",
                                            Toast.LENGTH_SHORT).show();
                                    // 改变登陆地状态 并保存
                                    isOnLine = false;
                                    PerfUtils.putBoolean(mContext, "isOnline",
                                            isOnLine);
                                    Intent intent1 = new Intent(LoginActivity.this,
                                            SwitchMenuActivity.class).putExtra(
                                            "warningPushUsername",
                                            warningPushUsername).putExtra(
                                            "warningPushPassword",
                                            warningPushPassword);
                                    startActivity(intent1);
                                    LoginActivity.this.finish();
                                    overridePendingTransition(
                                            R.anim.fragment_slide_right_enter,
                                            R.anim.fragment_slide_left_exit);
                                } catch (SQLiteException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "密码错误", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mContext, "用户名错误或该用户为第一次登录,第一次登录请在线登录", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                }
                // Intent intent = new Intent(LoginActivity.this,
                // SwitchMenuActivity.class);
                // startActivity(intent);
                // finish();
                // overridePendingTransition(R.anim.fragment_slide_right_enter,
                // R.anim.fragment_slide_left_exit);
                break;
            case R.id.bt_setting:
                Intent intent = new Intent(LoginActivity.this, LoginSetting.class);
                startActivity(intent);
                break;
            case R.id.btn_fetch_val_code:
                if (et_username_login.getText().toString().equals("")
                        || et_username_login.getText().toString() == null) {
                    Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else {
                    if (MobileApplication.netType == 0) {
                        // Toast.makeText(mContext, R.string.no_available_net,
                        // Toast.LENGTH_LONG).show();

                        // 获取第一次登陆的用户名和密码 ，如果不存在 则是第一次登陆 不能离线登录
                        String login_username = PerfUtils.getString(mContext,
                                "userName", username);

                        if (TextUtils.isEmpty(login_username)) {
                            Toast.makeText(mContext, "首次登录请连接网络",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(mContext, "无法连接网络，进入离线状态",
                                    Toast.LENGTH_SHORT).show();
                            // 改变登陆地状态 并保存
                            isOnLine = false;
                            PerfUtils.putBoolean(mContext, "isOnline", isOnLine);
                            Intent intent1 = new Intent(LoginActivity.this,
                                    SwitchMenuActivity.class).putExtra(
                                    "warningPushUsername", warningPushUsername)
                                    .putExtra("warningPushPassword",
                                            warningPushPassword);
                            startActivity(intent1);
                            LoginActivity.this.finish();
                            overridePendingTransition(
                                    R.anim.fragment_slide_right_enter,
                                    R.anim.fragment_slide_left_exit);
                        }

                        return;
                    }
                    new Builder(mContext)
                            .setTitle(R.string.dialog_title_val_code)
                            .setMessage(R.string.dialog_message_val_code)
                            .setPositiveButton(R.string.dialog_button_confirm,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            new FetchValCodeTask()
                                                    .execute(et_username_login
                                                            .getText().toString());
                                        }
                                    })
                            .setNegativeButton(R.string.dialog_button_cancel, null)
                            .create().show();

                }

                break;
        }
    }

    private class FetchValCodeTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            tv_val_code_send_info
                    .setText(getString(R.string.val_code_fetching));
            btn_fetch_val_code.setEnabled(false);
            btn_fetch_val_code.postDelayed(new Runnable() {
                public void run() {
                    btn_fetch_val_code.setEnabled(true);
                }
            }, 60000);
        }

        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            InputStream is = null;
            try {
                MobileApplication application = (MobileApplication) getApplication();
                HttpParams httpParameters = new BasicHttpParams();
                DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(WebUtils.rootUrl
                        + URLUtils.getCkeckCodeURL);

                httpPost.setHeader("EquipType", "Android");
                httpPost.setHeader("EquipSN", WebUtils.deviceId);
                httpPost.setHeader("Soft", WebUtils.packageName);
                httpPost.setHeader("Tel", WebUtils.phoneNumber);
                httpPost.setHeader("network", WebUtils.networkType);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                // GUID
                nameValuePairs.add(new BasicNameValuePair("UserId", userName));
                nameValuePairs.add(new BasicNameValuePair("DeviceCode",
                        application.deviceId));
                nameValuePairs.add(new BasicNameValuePair("Ip",
                        getLocalIpAddress()));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                HttpResponse response = hc.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                // byte[] data1 = LoadUtils.load(is);
                // String d = new String(data1,"utf-8");
                if (200 == response.getStatusLine().getStatusCode()) {
                    XmlPullParser parser = Xml.newPullParser();

                    parser.setInput(is, "UTF-8");
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if ("Message".equalsIgnoreCase(parser.getName())) {
                                String msg = parser.nextText().trim();
                                if (!"1".equals(msg)) {
                                    return msg;
                                }
                            } else if ("MOBILE".equalsIgnoreCase(parser
                                    .getName())) {
                                String mobileNo = parser.nextText().trim();
                                if (!TextUtils.isEmpty(mobileNo)) {
                                    return mobileNo;
                                } else
                                    return "0";
                            }
                        }
                        eventType = parser.next();
                    }
                } else if (500 == response.getStatusLine().getStatusCode()) {
                    // AlertUtils.showNetworkUnavailable();
                    // if (true)
                    // Log.e(mTag, StreamUtils.retrieveContent(is));
                }

            } catch (Exception e) {
                // if (DEBUG)
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // if (DEBUG)
                        e.printStackTrace();
                    }
                }
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!"0".equals(result)) {
                tv_val_code_send_info.setText(String.format(
                        getString(R.string.val_code_send_info), result));
            } else {
                btn_fetch_val_code.setEnabled(true);
                tv_val_code_send_info
                        .setText(getString(R.string.val_code_fetch_fail));
            }
        }
    }

    private boolean isNewer(String serverVersion, String clientVersion) {
        // 2.2.3 2.2
        String s1[] = serverVersion.split("\\.");
        String s2[] = clientVersion.split("\\.");

        if (s1.length != s2.length) {
            if (s1.length < s2.length) {
                for (int n = 1; n <= s2.length - s1.length; n++) {
                    serverVersion += ".0";
                }
            } else {
                for (int n = 1; n <= s1.length - s2.length; n++) {
                    clientVersion += ".0";
                }
            }
        }
        s1 = serverVersion.split("\\.");
        s2 = clientVersion.split("\\.");

        for (int i = 0; i < s1.length; i++) {
            int t1 = Integer.parseInt(s1[i]);
            int t2 = Integer.parseInt(s2[i]);
            if (t1 > t2) {
                return true;
            } else if (t1 < t2)
                return false;
        }
        return false;
    }

    void startConnectVpn() {
        overlayProgress = AlertUtils.showDialog(LoginActivity.this,
                getString(R.string.ms_login_notice), null, false);
        startVpn();
    }

    /**
     * @param username
     * @param password
     */
    private int error_code = 0;
    // private String serverURL;
    private LoginContainerLayout rl_login;
    private MainProcess mainProcess;
    private SoftInfo info;

    private void login(final String username, final String password) {

        new AsyncTask<Void, Void, Boolean>() {
            private volatile boolean running = true;
            private ProgressHUD overlayProgress;
            private String errorMessage = getString(R.string.no_available_net);
            private String serverCookie;

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (overlayProgress != null)
                    overlayProgress.dismiss();
                if (result) {
                    Constants.loginName = username;// 记录登录名称,
//                    Editor editor = sp.edit();
//                    if (cb_remind_password.isChecked()) {
//                        editor.putBoolean("remindPassword", true);
//                    } else {
//                        editor.putBoolean("remindPassword", false);
//                    }
//                    if (_cbAutoLogon.isChecked()) {
//                        editor.putBoolean("remindPassword", true);
//                        editor.putBoolean("_cbAutoLogon", true);
//                    } else {
//                        editor.putBoolean("_cbAutoLogon", false);
//                    }
//                    editor.commit();


                    PerfUtils.putString(mContext, username, username);
                    PerfUtils.putString(mContext, "userName", username);//只记录本次登陆的用户
                    // 改变登陆状态 并保存
                    isOnLine = true;
                    PerfUtils.putBoolean(mContext, "isOnline", isOnLine);

                    // 登录成功 用户名和密码保存到sp，记录相关状态 保存Cookie 进入主界面

                    MobileApplication application = (MobileApplication) getApplication();
                    application.cookie = serverCookie;
                    UserInfo.getInstance().setUsername(username);
                    UserInfo.getInstance().setPassword(password);
                    String developerMode = sp.getString(UserInfo.getInstance()
                            .getUsername() + "developerMode", "");
                    Constants.developerMode = developerMode;
                    WebUtils.cookie = serverCookie;
                    // System.out.println("serverCookie:"+serverCookie);
                    WebUtils.deviceId = application.deviceId;


//                    if((boolean)(SharedPreferenceUtil.getData(LoginActivity.this,"Boolean",false))){
 /*                   // 登陆成功将用户名和密码插入到本地数据库中,首先清空上次
                    String select_user = "select * from login_user where username = "
                            + "'" + username + "'" + "";
                    String sql_insert = "insert into login_user (userID,password,username) values ("
                            + "'"
                            + username
                            + "'"
                            + ","
                            + "'"
                            + password
                            + "'"
                            + "," + "'" + username + "'" + ")";
                    String sql_update = "update login_user set userID = " + "'"
                            + username + "'" + ",password = " + "'" + password
                            + "'" + "";
                    Connection db1 = null;
                    Statement state = null;
                    ResultSet set = null;
                    try {
                        db1 = SQLiteData.openOrCreateDatabase(LoginActivity.this);
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
*/

                    //记录登陆的用户名,每一个登录的用户都做记录

                    Intent intent = new Intent(LoginActivity.this,
                            SwitchMenuActivity.class).putExtra(
                            "warningPushUsername", warningPushUsername)
                            .putExtra("warningPushPassword",
                                    warningPushPassword);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    overridePendingTransition(
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_left_exit);

//                    }
//                    else{
//                        Intent intent = new Intent(LoginActivity.this,
//                                DatabaseSetPWDActivity.class).putExtra(
//                                "warningPushUsername", warningPushUsername)
//                                .putExtra("warningPushPassword",
//                                        warningPushPassword);
//                        startActivity(intent);
//                        LoginActivity.this.finish();
//                        overridePendingTransition(
//                                R.anim.fragment_slide_right_enter,
//                                R.anim.fragment_slide_left_exit);
//
//                    }


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
                        default:
                            // Toast.makeText(mContext, errorMessage,
                            // Toast.LENGTH_SHORT).show();

						/*
                         * // 获取第一次登陆的用户名和密码 ，如果不存在 则是第一次登陆 不能离线登录 String
						 * login_username = PerfUtils.getString(mContext,
						 * "userName", username); String select_user =
						 * "select * from login_user where username = " + "'" +
						 * username + "'" + ""; db =
						 * SQLiteDatabase.openOrCreateDatabase(
						 * Constants.db_path, null); Cursor cursor =
						 * db.rawQuery(select_user, null);
						 *
						 * if (!cursor.moveToNext()) { // 该用户不存在
						 * Toast.makeText(mContext, "首次登录请连接网络",
						 * Toast.LENGTH_SHORT).show(); } else {
						 *
						 * Toast.makeText(mContext, "无法连接网络，进入离线状态",
						 * Toast.LENGTH_SHORT).show(); // 改变登陆地状态 并保存 isOnLine =
						 * false; PerfUtils .putBoolean(mContext, "isOnline",
						 * isOnLine);
						 *
						 * Intent intent = new Intent(LoginActivity.this,
						 * SwitchMenuActivity.class).putExtra(
						 * "warningPushUsername", warningPushUsername)
						 * .putExtra("warningPushPassword",
						 * warningPushPassword); startActivity(intent);
						 * LoginActivity.this.finish();
						 * overridePendingTransition(
						 * R.anim.fragment_slide_right_enter,
						 * R.anim.fragment_slide_left_exit); } cursor.close();
						 * db.close();
						 */

                            // 登录结果返回 false
                            Toast.makeText(mContext, "无法连接网络,请检查网络设置", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            protected void onCancelled() {
                running = false;
            }

            ;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                overlayProgress = AlertUtils.showDialog(LoginActivity.this,
                        getString(R.string.ms_login_notice), this, running);
                overlayProgress.setCanceledOnTouchOutside(false);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                WebUtils.role = "0";
                DataCollectionUtils.checkRole(username); // 检测开发者角色
                System.out.println(WebUtils.role + "=========检查开发者");
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
                    System.out.println("Login:" + WebUtils.rootUrl
                            + URLUtils.splashActivityURL);

                    HttpParams httpParameters = new BasicHttpParams();
                    DefaultHttpClient hc = HttpUtils
                            .initHttpClient(httpParameters);
                    HttpPost post = new HttpPost(WebUtils.rootUrl
                            + URLUtils.splashActivityURL);

                    HttpConnectionParams.setConnectionTimeout(httpParameters,
                            10000);
                    HttpConnectionParams.setSoTimeout(httpParameters, 10000);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("Username",
                            username));
                    nameValuePairs.add(new BasicNameValuePair("Password",
                            password));
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
                        String STR = StreamUtils.retrieveContent(in);
                        in = new ByteArrayInputStream(STR.getBytes());
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
                        // editor.putString("username", "wangjing04.yssh");
                        // editor.putString("password", "0401108@ys");
                        // editor.putString("username", "zhouzf.yssh");
                        // editor.putString("password", "AFU1988");
                        editor.putString("username", username);
                        editor.putString("password", password);
                        // editor.putString("username", "lanxj.yssh");
                        // editor.putString("password", "$1$t8z0sz");
                        MobileApplication.userName = username;

						/*if (cb_remind_password.isChecked()) {
                            editor.putBoolean("remindPassword", true);
						} else {
							editor.putBoolean("remindPassword", false);
						}
						if (_cbAutoLogon.isChecked()) {
							editor.putBoolean("remindPassword", true);
							editor.putBoolean("_cbAutoLogon", true);
						} else {
							editor.putBoolean("_cbAutoLogon", false);
						}*/
                        editor.commit();
                        if (!running) {
                            return false;
                        }
                        return true;
                    } else {

                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
                // return true;
            }
        }.execute();
    }

    public void showErrorMessage(Context context, String title, String message) {
        new Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void startVpn() {
        int status = VPNManager.getInstance().getStatus();
        Log.i(TAG, "vpn status: " + status);

        if (status == Common.VpnStatus.IDLE) {
            start(VpnUtils.vpnHost, VpnUtils.vpnPort, username, password, "",
                    "", VPNManager.VpnFlag.VPN_FLAG_HTTP_PROXY // start
                            // http
                            // proxy
                            | VPNManager.VpnFlag.VPN_FLAG_SOCK_PROXY // start
                            // sock
                            // proxy
                            | VPNManager.VpnFlag.VPN_FLAG_PROXY_SCOPE_ALL // all
                            // programs
                            // are
                            // allowed
                            // to
                            // access
                            // proxy
                            | VPNManager.VpnFlag.VPN_FLAG_SKIP_MOTIONPRO_RES // don't
                            // fetch
                            // resource
                            | VPNManager.VpnFlag.VPN_FLAG_DISABLE_SKIP_LOGIN); // disable
            // app
            // use
            // devid
            // to
            // skip
            // normal
            // login
        }
    }

    public int start(String host, int port, String username, String password,
                     String certpath, String certpass, int flag) {
        Log.i(TAG, "start l3vpn enter");

        if (mThreadStartVpn != null) {
            Log.w(TAG, "ThreadStartVpn is not null, will kill it!");
            mThreadStartVpn.interrupt();
            try {
                mThreadStartVpn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThreadStartVpn = null;
        }
        mThreadStartVpn = new StartVpnThread(host, port, username, password,
                certpath, certpass, flag);

        mThreadStartVpn.start();

        return 0;
    }

    private class StartVpnThread extends Thread {
        String mHost;
        int mPort;
        String mUserName;
        String mPassword;
        String mCertPath;
        String mCertPass;
        int mFlag;

        public StartVpnThread(String host, int port, String username,
                              String password, String certpath, String certpass, int flag) {
            mHost = host;
            mPort = port;
            mUserName = username;
            mPassword = password;
            mCertPath = certpath;
            mCertPass = certpass;
            mFlag = flag;
        }

        public void run() {
            VPNManager.getInstance().startVPN(mHost, mPort, mUserName,
                    mPassword, mCertPath, mCertPass, mFlag);
        }
    }

    private String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 更新提示
     *
     * @param info
     */
    private void showUpdateDialog(final SoftInfo info) {
        // sp.edit().putBoolean("updateLogRead", false).commit();
        final Builder builer = new Builder(mContext);
        builer.setTitle(("1".equals(info == null ? "0" : info.getForced()) ? " 已有新版本，是否更新"
                : " 已有新版本，是否更新"));
        if (!"".equals(info.getUpdateLog().trim())) {
            builer.setMessage(info.getUpdateLog());
        }
        builer.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });
        if ("1".equals(info == null ? "1" : info.getForced())) {
            builer.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.this.finish();
                        }
                    });
        }

        // AlertDialog dialog = builer.create();
        // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // if ("0".equals(info == null ? "0" : info.getForced())) {
        // dialog.setCancelable(false);
        if ("0".equals(info == null ? "0" : info.getForced())) {
            builer.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // android.os.Process.killProcess(android.os.Process.myPid());
                            builer.create().dismiss();
                        }
                    });
        }
        // dialog.show();
        builer.create().show();
    }

    /**
     * 从服务器下载APK
     */
    protected void downLoadApk() {
        new AsyncTask<Void, Integer, File>() {
            ProgressDialog pd1 = null;
            private volatile boolean running = true;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd1 = new ProgressDialog(mContext);
                pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd1.setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                        finish();
                    }
                });
                if ("1".equals(info == null ? "0" : info.getForced())) {
                    pd1.setCancelable(false);
                }
                pd1.setTitle("提示");
                pd1.setMessage("正在下载更新,请稍候..");
                pd1.show();
            }

            @Override
            protected void onPostExecute(File result) {
                super.onPostExecute(result);
                pd1.dismiss();
                if (result != null) {
                    // 安装APK
                    installApk(result);
                } else {
                    Toast.makeText(mContext, "下载失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            protected void onCancelled() {
                running = false;
            }

            ;

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                pd1.setProgress(values[0]);
            }

            @Override
            protected File doInBackground(Void... params) {
                try {
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        URL url = new URL((info.getDownloadUrl()));
                        HttpURLConnection conn = (HttpURLConnection) url
                                .openConnection();
                        conn.setConnectTimeout(10000);
                        InputStream is = conn.getInputStream();
                        System.out.println("conn.getContentLength():"
                                + conn.getContentLength());
                        pd1.setMax(conn == null ? 100 : conn.getContentLength());
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                Constants.home_cache + "/oa/MobieOffice.apk");
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);

                        byte[] buffer = new byte[1024];
                        int len;
                        int total = 0;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            total += len;
                            publishProgress(total);
                        }
                        fos.close();
                        bis.close();
                        is.close();
                        if (!running) {
                            return null;
                        }
                        return file;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) // 按下返回键提醒用户是否退出系统
        {
            System.out.println("==apn 还原==");
            // APN设置
            // 将APN接入点还原
            int apnAsDefault = sp.getInt("checked_position", 0);
            if (apnAsDefault == 3) {
                int currApnId = sp.getInt("PREF_CURR_NORMAL_APN_ID", -1);
                int yapnId = -1;
                try {
                    yapnId = APNUtils.fetchCurrentApnId();
                    APNUtils.setContext(mContext);
                    if (-1 != currApnId && currApnId != yapnId) {
                        APNUtils.deleteAPN(yapnId);
                        APNUtils.SetDefaultAPN(currApnId);
                    } else {
                        // APN出现错误，提示用户手动修改
                        // apn设置 非燕山项目注释掉
                        // if (-1 != yapnId) {
                        // Intent intent = new
                        // Intent(android.content.Intent.ACTION_MAIN);
                        // intent.setClassName("com.android.settings",
                        // "com.android.settings.ApnSettings");
                        // startActivity(intent);
                        // Toast.makeText(mContext, "请将接入点置回，以正常访问网络！",
                        // Toast.LENGTH_LONG).show();
                        // }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // apn设置 非燕山项目注释掉
                    // if (-1 != yapnId) {
                    // Intent intent = new
                    // Intent(android.content.Intent.ACTION_MAIN);
                    // intent.setClassName("com.android.settings",
                    // "com.android.settings.ApnSettings");
                    // startActivity(intent);
                    // Toast.makeText(mContext, "请将接入点置回，以正常访问网络！",
                    // Toast.LENGTH_LONG).show();
                    // }
                }
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        return super.onKeyDown(keyCode, event); // 交由系统处理
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        switch (buttonView.getId()) {
            case R.id.cb_auto_logon:
                if (_cbAutoLogon.isChecked()) {
                    cb_remind_password.setChecked(true);
                }

                break;
            case R.id.cb_remind_password:
                if (!cb_remind_password.isChecked()) {
                    _cbAutoLogon.setChecked(false);
                }
                break;

            default:
                break;
        }
    }

    // 获取当前ip地址
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IpAddress", ex.toString());
        }
        return "";
    }

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;
    public static int mNetWorkType;
    private SQLiteDatabase db;
    private boolean isOnLineLogin;//离线登录还是在线登录，离线登录时false，在线是ture

    // 判断网络type
    public static int getNetWorkType(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            System.out.println("network Type:" + networkInfo.getTypeName());
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();

                mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
                        : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }

        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    // 定义按钮的响应,覆写系统的默认处理
    class ButtonHandler extends Handler {

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(
                            mDialog.get(), msg.what);
                    break;
            }
        }
    }

    class MHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    softUpdateDialog.show();// 显示对话框
                    break;
            }
            super.handleMessage(msg);
        }

    }

    private void dialogUpdate(final Context mContext, String updateOrInstall,
                              final String isInstall, final String path, final SoftInfo info) {
        // 如果本地已经下载完了包
        LayoutInflater factoryHis = LayoutInflater.from(LoginActivity.this);// 提示框
        View viewDialog = null;
        viewDialog = factoryHis.inflate(R.layout.dialog_update_layout, null);
        softUpdateDialog = new Builder(mContext)
                .setView(viewDialog).setCancelable(false).create();
        Button btn_dialog_ok = (Button) viewDialog
                .findViewById(R.id.btn_dialog_ok);
        btn_dialog_ok.setText(updateOrInstall);
        TextView textView = (TextView) viewDialog
                .findViewById(R.id.updatecontent);
        textView.setText(info.getUpdateLog());
        btn_dialog_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // isInstall 1:代表安装 2:代表非强制更新 3:代表强制更新
                if (isInstall.equals("1")) {
                    installApk(new File(path));
                    sps.edit().putString("localApk", "").commit();
                } else if (isInstall.equals("2")) {
                    Intent i = new Intent(mContext, DownloadServiceUpdate.class);
                    i.putExtra("url", info.getDownloadUrl());
                    i.putExtra("notifyId", 0);
                    mContext.startService(i);
                } else {
                    downLoadApk();
                }
                softUpdateDialog.dismiss();
            }
        });
        Button btn_dialog_cancel = (Button) viewDialog
                .findViewById(R.id.btn_dialog_cancel);
        btn_dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstall.equals("3")) {
                    LoginActivity.this.finish();
                } else {
                    softUpdateDialog.dismiss();
                }

            }
        });
        ButtonHandler buttonHandler = new ButtonHandler(softUpdateDialog);
        // 设定对话框的处理Handler
        try {
            Field field = softUpdateDialog.getClass()
                    .getDeclaredField("mAlert");
            field.setAccessible(true);
            // 获得mAlert变量的值
            Object obj = field.get(softUpdateDialog);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);
            // 修改mHandler变量的值，使用新的ButtonHandler类
            field.set(obj, buttonHandler);
        } catch (Exception e) {
        }
        // 显示对话框
        Message msg = new Message();
        msg.what = 0;
        msg.setTarget(new MHandler());
        msg.sendToTarget();
    }
}
