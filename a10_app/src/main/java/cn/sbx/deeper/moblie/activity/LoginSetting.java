package cn.sbx.deeper.moblie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.util.APNUtils;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

public class LoginSetting extends BaseActivity implements OnClickListener {
    Context mContext = this;
    int checked_position;
    LinearLayout ll_apn;
    TextView tv_title;
    Button bt_left, btn_save, btn_next;
    EditText et_intranet, et_internet, et_apn, et_apn_name, et_apn_addr;
    CheckBox cb_intranet, cb_internet, cb_apn, cb_auto;
    SharedPreferences sp;
    private String checkedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_setting);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("登录设置");
        bt_left = (Button) findViewById(R.id.bt_left);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setVisibility(View.GONE);

        bt_left.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.VISIBLE);
        bt_left.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_save.setText("保存");
        ll_apn = (LinearLayout) findViewById(R.id.ll_apn);

        cb_intranet = (CheckBox) findViewById(R.id.cb_intranet);
        cb_internet = (CheckBox) findViewById(R.id.cb_internet);
        cb_apn = (CheckBox) findViewById(R.id.cb_apn);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);

        sp = getSharedPreferences("sys_config", Context.MODE_PRIVATE);


        cb_intranet.setOnClickListener(this);
        cb_internet.setOnClickListener(this);
        cb_apn.setOnClickListener(this);
        cb_auto.setOnClickListener(this);

        et_intranet = (EditText) findViewById(R.id.et_intranet);
        et_internet = (EditText) findViewById(R.id.et_internet);
        et_apn = (EditText) findViewById(R.id.et_apn);
        et_apn_name = (EditText) findViewById(R.id.apn_name);
        et_apn_addr = (EditText) findViewById(R.id.apn_addr);
        et_intranet.setText(sp.getString("PREF_MOA_INTRA_ADDR",
                WebUtils.intranetUrl));
        et_internet.setText(sp.getString("PREF_MOA_INTERNET_ADDR",
                WebUtils.internetUrl));
        et_apn.setText(sp.getString("PREF_APN_DISPLAY_NAME", "燕山石化(APN)"));
        et_apn_name.setText(sp.getString("PREF_APN_NAME", "YSSH-DDN.BJ"));
        et_apn_addr.setText(sp.getString("PREF_MOA_INTRA_ADDR_FOR_APN",
                WebUtils.apnUrl));

        checked_position = sp.getInt("checked_position", 2);
        if (checked_position == 3) {
            ll_apn.setVisibility(View.VISIBLE);
        } else
            ll_apn.setVisibility(View.GONE);
        switch (checked_position) {
            case 1:
                cb_intranet.setChecked(true);
                et_intranet.setEnabled(true);
                et_internet.setEnabled(false);
                et_apn.setEnabled(false);
                break;
            case 2:
                cb_internet.setChecked(true);
                et_intranet.setEnabled(false);
                et_internet.setEnabled(true);
                et_apn.setEnabled(false);
                break;
            case 3:
                cb_apn.setChecked(true);
                et_intranet.setEnabled(false);
                et_internet.setEnabled(false);
                et_apn.setEnabled(true);
                break;
            case 4:
                cb_auto.setChecked(true);
                et_intranet.setEnabled(false);
                et_internet.setEnabled(false);
                et_apn.setEnabled(false);
                break;
        }
    }


    private final int SHOW_PROGRESS = 0;
    private final int DISMISS_PROGRESS = 1;
    private final int URL_SUCCESS = 2;
    private final int URL_ERROR = 3;
    private int i = 0;

    private ProgressHUD overlayProgress;
    private boolean netAvailable = false;
    private Handler netHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_PROGRESS:
//				overlayProgress = AlertUtils.showDialog(
//						NetSettingActivity.this,
//						getString(R.string.msg_net_scan_nets), null, false);
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
                            commitServerUrl();
                            WebUtils.rootUrl = checkedUrl;
                            Toast.makeText(getApplicationContext(), getString(R.string.msg_net_choose_success), Toast.LENGTH_SHORT).show();
                            if (overlayProgress != null)
                                overlayProgress.dismiss();
                            finish();
                        }
                    }
                    break;
                case URL_ERROR:
                    synchronized (this) {
                        i++;
                        if (i == 3) {
                            if (overlayProgress != null)
                                overlayProgress.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.msg_net_choose_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void commitServerUrl() {
        Editor editor = sp.edit();
        editor.putString(Constants.serverURL, checkedUrl);
        editor.putBoolean(Constants.PREF_AUTOSCANNET, Constants.autoScanNet);
        editor.commit();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bt_left:
                finish();
                break;
            case R.id.btn_save:
                Editor e = sp.edit();
                e.putString("PREF_MOA_INTRA_ADDR", et_intranet.getText().toString());
                e.putString("PREF_MOA_INTERNET_ADDR", et_internet.getText().toString());
                e.putString("PREF_APN_DISPLAY_NAME", et_apn.getText().toString());
                e.putString("PREF_APN_NAME", et_apn_name.getText().toString());
                e.putString("PREF_MOA_INTRA_ADDR_FOR_APN", et_apn_addr.getText().toString());
                e.putInt("checked_position", checked_position);
                e.commit();
                switch (checked_position) {
                    case 1:
                        WebUtils.rootUrl = et_intranet.getText().toString();
                        break;
                    case 2:
                        WebUtils.rootUrl = et_internet.getText().toString();
                        break;
                    case 3:
                        WebUtils.rootUrl = sp.getString("PREF_MOA_INTRA_ADDR_FOR_APN", WebUtils.apnUrl);
                        try {
                            if (WebUtils.sdkVersion > 16) {//android4.1以上 做特殊处理
                                //添加修改APN设置界面
//						Intent intent = new Intent(
//								android.content.Intent.ACTION_INSERT);
//						intent.setClassName("com.android.settings",
//								"com.android.settings.ApnEditor");
//						intent.setData(Uri.parse("content://telephony/carriers"));
                                //
//						startActivity(intent);

                                Intent intent = new Intent(
                                        Intent.ACTION_MAIN);
                                intent.setClassName("com.android.settings",
                                        "com.android.settings.ApnSettings");
                                startActivity(intent);
                                Toast.makeText(mContext, "请手动添加燕山石化APN并选中！",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            // 检测
                            APNUtils.setContext(mContext);

                            int currApnId = APNUtils.fetchCurrentApnId();

                            int apnId = APNUtils.fetchAPNId(et_apn_name.getText().toString());
                            if (currApnId == apnId) {
                                Toast.makeText(mContext, "保存成功！", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            // -? 没有，则插入，获得ID
                            // -: 有，则获得ID
                            if (apnId != -1)
                                APNUtils.deleteAPN(apnId);
                            apnId = APNUtils.insertAPN(et_apn.getText().toString(), et_apn_name.getText().toString());
                            // 根据ID来启用
                            if (apnId != -1 && APNUtils.SetDefaultAPN(apnId))
                                Toast.makeText(mContext, "接入点设置成功！", Toast.LENGTH_SHORT)
                                        .show();
                            else
                                Toast.makeText(mContext, "接入点设置失败，请检查配置选项！",
                                        Toast.LENGTH_SHORT).show();
                            if (apnId != currApnId)
                                e.putInt("PREF_CURR_NORMAL_APN_ID", currApnId);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText("YSSH-DDN.BJ");
                            int ysAPN = APNUtils.fetchAPNId("YSSH-DDN.BJ");
                            if (ysAPN != -1) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.setClassName("com.android.settings",
                                        "com.android.settings.ApnSettings");
                                startActivity(intent);
                                Toast.makeText(mContext, "请选中燕山石化APN，以使用移动办公系统！",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_INSERT);
                                    intent.setClassName("com.android.settings",
                                            "com.android.settings.ApnEditor");
                                    intent.setData(Uri.parse("content://telephony/carriers"));

                                    startActivity(intent);
                                    Toast.makeText(mContext, "正确设置APN信息后，请按菜单键进行保存！",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.setClassName("com.android.settings",
                                            "com.android.settings.ApnSettings");
                                    startActivity(intent);

                                    Toast.makeText(mContext, "请按菜单键，新建燕山石化介入点！",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            finish();
                        }
                        e.commit();
                        break;

                    case 4:
                        netHandler.sendEmptyMessage(SHOW_PROGRESS);
                        new Thread(new ScanNetWorkTask(et_intranet.getText().toString().trim())) {
                        }.start();
                        new Thread(new ScanNetWorkTask(et_internet.getText().toString().trim())) {
                        }.start();
//				new Thread(new ScanNetWorkTask(et_apn.getText().toString().trim())){}.start();
//				 Constants.autoScanNet = true;
                        break;
                    default:
                        break;
                }
                //		Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();

                break;
            case R.id.cb_intranet:
                cb_intranet.setChecked(true);
                cb_internet.setChecked(false);
                cb_apn.setChecked(false);
                cb_auto.setChecked(false);
                //
                et_intranet.setEnabled(true);
                et_internet.setEnabled(false);
                et_apn.setEnabled(false);
                checked_position = 1;
                ll_apn.setVisibility(View.GONE);
                break;
            case R.id.cb_internet:
                cb_internet.setChecked(true);
                cb_intranet.setChecked(false);
                cb_apn.setChecked(false);
                cb_auto.setChecked(false);
                //
                et_intranet.setEnabled(false);
                et_internet.setEnabled(true);
                et_apn.setEnabled(false);

                checked_position = 2;
                ll_apn.setVisibility(View.GONE);
                break;
            case R.id.cb_apn:
                cb_apn.setChecked(true);
                cb_internet.setChecked(false);
                cb_intranet.setChecked(false);
                cb_auto.setChecked(false);
                //
                et_intranet.setEnabled(false);
                et_internet.setEnabled(false);
                et_apn.setEnabled(true);

                checked_position = 3;
                ll_apn.setVisibility(View.VISIBLE);

                break;
            case R.id.cb_auto:
                cb_auto.setChecked(true);
                cb_internet.setChecked(false);
                cb_apn.setChecked(false);
                cb_intranet.setChecked(false);
                //
                et_intranet.setEnabled(false);
                et_internet.setEnabled(false);
                et_apn.setEnabled(false);

                checked_position = 4;
                ll_apn.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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
                if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
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
}
