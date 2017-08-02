package cn.sbx.deeper.moblie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.sunboxsoft.monitor.utils.PerfUtils;

import cn.sbx.deeper.moblie.util.SharedPreferenceUtil;
import petrochina.ghzy.a10fieldwork.R;

public class ChooseLoginMode extends BaseActivity implements
        OnItemSelectedListener, OnClickListener {

    private Spinner spinner_chooseloginmode;
    private String[] options;
    private String option;
    private Button button_login;
    private String warningPushPassword;
    private String warningPushUsername;
    private boolean isOnLineLogin = true;
    private boolean aBoolean;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_login_mode);

        options = new String[2];
        options[0] = "离线登录 <离线存储>";
        options[1] = "在线登录 <数据实时交互>";
        spinner_chooseloginmode = (Spinner) findViewById(R.id.spinner_chooseloginmode);
        button_login = (Button) findViewById(R.id.button_login);

        aBoolean = (boolean) SharedPreferenceUtil.getData(this, "Boolean", false);

        spinner_chooseloginmode
                .setBackgroundResource(R.drawable.ic_approve_spinner_background);
//
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, options);

//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//		spinner_chooseloginmode.setAdapter(new spinnerAdapter());
        spinner_chooseloginmode.setAdapter(adapter);

        spinner_chooseloginmode.setOnItemSelectedListener(this);
        button_login.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            warningPushUsername = intent.getStringExtra("warningPushUsername");
            warningPushPassword = intent.getStringExtra("warningPushPassword");
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        option = options[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class spinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return options.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return options[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View inflate = View.inflate(getApplicationContext(), R.layout.login_staly_spinner_item, null);

            TextView spinner_item_login = (TextView) inflate.findViewById(R.id.spinner_item_login);
            spinner_item_login.setText(options[position]);
            return inflate;
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // 单击确定按钮 进入对应的登录界面
            case R.id.button_login:

                // 判断选择的登录方式,普通登录包括在线和离线
                if (option.equals("离线登录 <离线存储>")) {
                    isOnLineLogin = false;
//				将登陆状态保存至SP
                        PerfUtils.putBoolean(this, "loginState", isOnLineLogin);

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("warningPushUsername", warningPushUsername);
                    intent.putExtra("warningPushPassword", warningPushPassword);
                    intent.putExtra("isOnLine", isOnLineLogin);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_left_exit);
                } else if (option.equals("在线登录 <数据实时交互>")) {
                    // 打开uKey界面登录,先按正常在线登录
                    isOnLineLogin = true;
//				将登陆状态保存至SP
                    PerfUtils.putBoolean(this, "loginState", isOnLineLogin);

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("warningPushUsername", warningPushUsername);
                    intent.putExtra("warningPushPassword", warningPushPassword);
                    intent.putExtra("isOnLine", isOnLineLogin);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_left_exit);


                 /*   Intent mintent = new Intent();

                    PackageManager packageManager = this.getPackageManager();

                    mintent = packageManager.getLaunchIntentForPackage("cn.com.petrochina.EnterpriseHall");

                    mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    this.startActivity(mintent);
*/


//                    Intent intent = getPackageManager().getLaunchIntentForPackage("cn.com.petrochina.EnterpriseHall");
//                    if (intent == null) {//未安装app
//                        Toast.makeText(this, "请先安装应用商城", Toast.LENGTH_LONG);
//                    } else {//安装了App
//                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        intent.setAction(Intent.ACTION_MAIN);
//                        startActivity(intent);
//                    }

                }

                break;

            default:
                break;
        }
    }

}
