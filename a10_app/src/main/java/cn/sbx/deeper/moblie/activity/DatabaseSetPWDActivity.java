package cn.sbx.deeper.moblie.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.SharedPreferenceUtil;
import cn.sbx.deeper.moblie.util.StreamUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;


/**
 * Created by caomang on 2016/11/17.
 */

public class DatabaseSetPWDActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_password_login_psw,
            et_password_login_confirmation_psw;
    private TextView et_username_login_psw;
    private Button btn_fetch_val_code_psw, bt_login_confirm_psw;
    String warningPushUsername;
    String warningPushPassword;
    private String passwordStr;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DatabaseHelper.encrypt(DatabaseSetPWDActivity.this);
        setContentView(R.layout.database_set_psw_layout);
        Intent intent = getIntent();
        if (intent != null) {
            warningPushUsername = intent.getStringExtra("warningPushUsername");
            warningPushPassword = intent.getStringExtra("warningPushPassword");
        }


        initView();


    }

    private void initView() {
        et_password_login_confirmation_psw = (EditText) findViewById(R.id.et_password_login_confirmation_psw);
        et_password_login_psw = (EditText) findViewById(R.id.et_password_login_psw);
        et_username_login_psw = (TextView) findViewById(R.id.et_username_login_psw);
        btn_fetch_val_code_psw = (Button) findViewById(R.id.btn_fetch_val_code_psw);
        bt_login_confirm_psw = (Button) findViewById(R.id.bt_login_confirm_psw);
        et_username_login_psw.setText(Constants.loginName);
        btn_fetch_val_code_psw.setOnClickListener(this);
        bt_login_confirm_psw.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fetch_val_code_psw:
                String confirmationPWDStr = et_password_login_confirmation_psw.getText().toString().trim();

                passwordStr = et_password_login_psw.getText().toString().trim();
//                String usernameStr = et_username_login_psw.getText().toString().trim();
                if ("".equals(Constants.loginName)) {
                    Toast.makeText(this, "帐号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(passwordStr)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(confirmationPWDStr)) {
                    Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!((passwordStr).equals(confirmationPWDStr))) {
                    Toast.makeText(this, "两次输入的密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    et_password_login_psw.setText("");
                    et_password_login_confirmation_psw.setText("");
                    return;
                } else {


//                    importInitDatabaseA10();
//                    createDB(passwordStr);

//                    setUserInfoToDB();

                    setPasswdForDatabase(et_username_login_psw.getText().toString(),passwordStr);

//                    String select_user = "select * from login_user where username = "
//                            + "'" + Constants.loginName + "'" + "";
//                    String sql_insert = "insert into login_user (userID,password,username) values ("
//                            + "'"
//                            + Constants.loginName
//                            + "'"
//                            + ","
//                            + "'"
//                            + passwordStr
//                            + "'"
//                            + "," + "'" + Constants.loginName + "'" + ")";
//                    String sql_update = "update login_user set userID = " + "'"
//                            + Constants.loginName + "'" + ",password = " + "'" + passwordStr
//                            + "'" + "";
////                    Connection db1 = null;
////                    Statement state = null;
////                    ResultSet set = null;
//
//                    DatabaseHelper db1 = null;
//                    SQLiteDatabase writableDatabase=null;
//                    boolean tagForExist=false;
//
//                    try {
////                        db1 = SQLiteData.openOrCreateDatabase(DatabaseSetPWDActivity.this);
////                        state = db1.createStatement();
//
//                        db1 = new DatabaseHelper(DatabaseSetPWDActivity.this);
//
//                        writableDatabase = db1.getWritableDatabase();
////                        Cursor cursor = writableDatabase.query(DatabaseHelper.LOGIN_USER, null, null, null, null, null, null);
//                        Cursor cursor = writableDatabase.query(DatabaseHelper.LOGIN_USER, new String[]{"ROWID AS _id",//
//                                DatabaseHelper.USERNAME,
//                                DatabaseHelper.PASSWORD}, null, null, null, null, null);
//
//                        //判断游标是否为空
//                        if (cursor.moveToLast()) {
//                        //遍历游标
//                            for (int i = 0; i < cursor.getCount(); i++) {
//                                cursor.move(i);
//                                String username = cursor.getString(1);
//                                if(username.equals(Constants.loginName)){
//                                    tagForExist=true;
//                                    break;
//                                }
//
//                            }
//                        }
//
//                        cursor.close();
//
//
//                    if (tagForExist) {
//                     writableDatabase.execSQL(sql_update);
//
//                    } else {
//                        writableDatabase.execSQL(sql_insert);
//                    }
//
//                }catch(SQLiteException e){
//                e.printStackTrace();
//            }finally{
//                if (db1 != null) {
//                    try {
//                        db1.close();
//                    } catch (SQLiteException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//                if (writableDatabase != null) {
//                    try {
//                        writableDatabase.close();
//                    } catch (SQLiteException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//
//            }




        }


        break;

        case R.id.bt_login_confirm_psw:
        et_password_login_psw.setText("");
        et_password_login_confirmation_psw.setText("");

        break;

    }

}

    private void setPasswdForDatabase(final String userId,final String secret) {


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String s = WebServicesUtil.connectWebServiceTest1(userId, secret);
//                if (s != null) {
//
//                    if(s.equals("Success")){
//                        DatabaseHelper.PASSPHRASE=secret;
//                        Intent intent = new Intent(DatabaseSetPWDActivity.this,
//                                SwitchMenuActivity.class).putExtra(
//                                "warningPushUsername", warningPushUsername)
//                                .putExtra("warningPushPassword",
//                                        warningPushPassword);
//                        startActivity(intent);
//                        DatabaseSetPWDActivity.this.finish();
//                        overridePendingTransition(
//                                R.anim.fragment_slide_right_enter,
//                                R.anim.fragment_slide_left_exit);
//
//
//                    }else{
//                        Toast.makeText(DatabaseSetPWDActivity.this, "添加密码没有成功", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                } else {
//                    Toast.makeText(DatabaseSetPWDActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
//                    return;
//
//                }
//
//            }
//        }).start();


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

                    if(result.equals("Success")){

                        SharedPreferenceUtil.saveData(DatabaseSetPWDActivity.this, "String", passwordStr);
                        SharedPreferenceUtil.saveData(DatabaseSetPWDActivity.this, "Boolean", true);
                        DatabaseHelper.PASSPHRASE=secret;
                        Intent intent = new Intent(DatabaseSetPWDActivity.this,
                                SwitchMenuActivity.class).putExtra(
                                "warningPushUsername", warningPushUsername)
                                .putExtra("warningPushPassword",
                                        warningPushPassword);
                        startActivity(intent);
                        DatabaseSetPWDActivity.this.finish();
                        overridePendingTransition(
                                R.anim.fragment_slide_right_enter,
                                R.anim.fragment_slide_left_exit);


                    }else{
                        Toast.makeText(DatabaseSetPWDActivity.this, "添加密码没有成功", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    Toast.makeText(DatabaseSetPWDActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    return;

                }
            }

            protected void onCancelled() {
                running = false;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                overlayProgress = AlertUtils.showDialog(DatabaseSetPWDActivity.this,
                        getString(R.string.ms_login_notice), this, running);
                overlayProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {

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
                DataCollectionUtils.checkRole(userId); // 检测开发者角色
                InputStream in = null;
                try {
                    MobileApplication application = (MobileApplication) getApplication();


                    HttpParams httpParameters = new BasicHttpParams();
                    DefaultHttpClient hc = HttpUtils
                            .initHttpClient(httpParameters);
                    HttpPost post = new HttpPost(WebUtils.rootUrl
                            + URLUtils.getDatabaseURL);
                    post.setHeader("Cookie", WebUtils.cookie);
//                    post.setHeader("EquipType","Android");
//
//                    post.setHeader("EquipSN", WebUtils.deviceId);
//                    post.setHeader("RequestType", "add");
//                    post.setHeader("userId", userId);
//                    post.setHeader("userPwd", secret);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("EquipType","Android"));
                    nameValuePairs.add(new BasicNameValuePair("EquipSN", WebUtils.deviceId));

                    nameValuePairs.add(new BasicNameValuePair("RequestType", "add"));

                    nameValuePairs.add(new BasicNameValuePair("userId", userId));
                    nameValuePairs.add(new BasicNameValuePair("userPwd", secret));

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


//    public void importInitDatabaseA10() {
//        // 数据库的目录
//        String dirPath = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
////		String dirPath = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/";
//
//        String dirPath_anjian = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
////		String dirPath_anjian = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/";
//
//        File dir2 = new File(dirPath_anjian);
//        if (!dir2.exists()) {
//            dir2.mkdir();
//        }
//
//        File dir = new File(dirPath);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        // 数据库文件
//        File dbfile = new File(dir, "a10_db.db");
//        try {
//            if (!dbfile.exists()) {
//                dbfile.createNewFile();
//                // 加载欲导入的数据库 chinacity.db
//                InputStream is = getApplicationContext().getResources()
//                        .openRawResource(R.raw.a10_db);
//                FileOutputStream fos = new FileOutputStream(dbfile);
//                byte[] buffere = new byte[is.available()];
//                is.read(buffere);
//                fos.write(buffere);
//                is.close();
//                fos.close();
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // openOrCreateDB();
//        // findprovince();
//        // 数据库文件
//
//        File dbfile2 = new File(dir2, "cb_db.db");
//        try {
//            if (!dbfile2.exists()) {
//                dbfile2.createNewFile();
//                // 加载欲导入的数据库 chinacity.db
//                InputStream is = this.getApplicationContext().getResources()
//                        .openRawResource(R.raw.cb_db);
//                FileOutputStream fos = new FileOutputStream(dbfile2);
//                byte[] buffere = new byte[is.available()];
//                is.read(buffere);
//                fos.write(buffere);
//                is.close();
//                fos.close();
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // openOrCreateDB();
//        // findprovince();
//    }

//    private void createDB(String pwd) {
//        // 判断数据库中是否存在表
//        Connection con = null;
//        Statement state = null;
//        try {
//            con = SQLiteData.openOrCreateDatabase(pwd);
//            state = con.createStatement();
//            ResultSet set = state
//                    .executeQuery("select * from dmedb_master where name = 'custInfo'");
//            // 客户信息表
//            if (!set.next()) {
//                state.executeUpdate("create table custInfo(schedInfoID varchar(128),"
//                        + " meterReadCycleRouteSequence varchar(128),"
//                        + "accountId varchar(128),"
//                        + "entityName varchar(128),"
//                        + "customerClass varchar(128),"
//                        + "cmCustClDescr varchar(128),"
//                        + "cmMrAddress varchar(128),"
//                        + "cmMrDistrict varchar(128),"
//                        + "cmMrStreet varchar(128),"
//                        + "cmMrCommunity varchar(128),"
//                        + "cmMrBuilding varchar(128),"
//                        + "cmMrUnit varchar(128),"
//                        + "cmMrRoomNum varchar(128),"
//                        + "spMeterHistoryId varchar(128),"
//                        + "meterConfigurationId varchar(128),"
//                        + "cmMrMtrBarCode varchar(128),"
//                        + "fullScale varchar(128),"
//                        + "cmMrAvgVol varchar(128),"
//                        + "rateSchedule varchar(128),"
//                        + "cmRsDescr varchar(128),"
//                        + "cmMrLastBal varchar(128),"
//                        + "cmMrOverdueAmt varchar(128),"
//                        + "cmMrDebtStatDt varchar(128),"
//                        + "cmMrLastMrDttm varchar(128),"
//                        + "readType varchar(128),"
//                        + "cmMrLastMr varchar(128),"
//                        + "cmMrLastVol varchar(128),"
//                        + "cmMrLastDebt varchar(128),"
//                        + "cmMrLastSecchkDt varchar(128),"
//                        + "cmMrRemark varchar(128),"
//                        + "cmMrState varchar(128)," + "cmMrDate varchar(128))");
//            }
//            set.close();
//            ResultSet set1 = state
//                    .executeQuery("select * from dmedb_master where name = 'attachment'");
//            if (!set1.next()) {
//                state.executeUpdate("create table attachment(bzId varchar(128),"
//
//                        + "bzType varchar(128),"
//                        + "userID varchar(128),"
//                        + "attachmentType varchar(128),"
//                        + "attachmenturl varchar(128),"
//                        + "status varchar(128))");
//            }
//            set1.close();
//
//            ResultSet set2 = state
//                    .executeQuery("select * from dmedb_master where name = 'dictionaries'");
//            if (!set2.next()) {
//
//                state.executeUpdate("create table dictionaries(dictionaryDescr varchar(128),"
//
//                        + "dictionaryCode varchar(128),"
//                        + "parentID varchar(128),"
//                        + "dictsequence varchar(128) default '0')");
//            }
//            set2.close();
//
//            ResultSet set3 = state
//                    .executeQuery("select * from dmedb_master where name = 'login_user'");
//            if (!set3.next()) {
//
//                state.executeUpdate("create table login_user(userID varchar(128),"
//
//                        + "username varchar(128),"
//                        + "password varchar(128),"
//                        + "deviceId varchar(128))");
//            }
//            set3.close();
//
//            ResultSet set4 = state
//                    .executeQuery("select * from dmedb_master where name = 'perPhone'");
//            if (!set4.next()) {
//                state.executeUpdate("create table perPhone(accountId varchar(128),"
//
//                        + "sequence varchar(128),"
//                        + "phoneType varchar(128),"
//                        + "phone varchar(128),"
//                        + "extension varchar(128),"
//                        + "cmPhoneOprtType varchar(128))");
//            }
//            set4.close();
//
//            ResultSet set5 = state
//                    .executeQuery("select * from dmedb_master where name = 'schedInfo'");
//            if (!set5.next()) {
//                state.executeUpdate("create table schedInfo(userID varchar(128),"
//
//                        + "meterReadRoute varchar(128),"
//                        + "cmMrRteDescr varchar(128),"
//                        + "meterReadCycle varchar(128),"
//                        + "cmMrCycDescr varchar(128),"
//                        + "scheduledSelectionDate varchar(128),"
//                        + "scheduledReadDate varchar(128),"
//                        + "cmMrDate varchar(128) default '0')");
//            }
//            set5.close();
//
//            ResultSet set6 = state
//                    .executeQuery("select * from dmedb_master where name = 'uploadcustInfo'");
//            if (!set6.next()) {
//                state.executeUpdate("create table uploadcustInfo(spMeterHistoryId varchar(128),"
//
//                        + "meterConfigurationId varchar(128),"
//                        + "cmMr varchar(128),"
//                        + "readType varchar(128),"
//                        + "cmMrDttm varchar(128),"
//                        + "cmMrRefVol varchar(128),"
//                        + "cmMrRefDebt varchar(128),"
//                        + "cmMrNotiPrtd varchar(128),"
//                        + "cmMrCommCd varchar(128),"
//                        + "cmMrRemark varchar(128),"
//                        + "cmMrState varchar(128)," + "cmMrDate varchar(128))");
//            }
//            set6.close();
//            ResultSet set7 = state
//                    .executeQuery("select * from dmedb_master where name = 'UserLadder'");
//            if (!set7.next()) {
//                state.executeUpdate("create table UserLadder(firstGasPrice varchar(128),"
//
//                        + "firstTolerance varchar(128),"
//                        + "firstGasFee varchar(128),"
//                        + "secondGasPrice varchar(128),"
//                        + "secondTolerance varchar(128),"
//                        + "secondGasFee varchar(128),"
//                        + "thirdGasPrice varchar(128),"
//                        + "thirdTolerance varchar(128),"
//                        + "thirdGasFee varchar(128)," + "UserNo varchar(128))");
//            }
//            set7.close();
//
//            // 安检数据表
//
//            // 任务表(schedInfo_aj)
//            ResultSet set8 = state
//                    .executeQuery("select * from dmedb_master where name = 'schedInfo_aj'");
//            if (!set8.next()) {
//                state.executeUpdate("create table schedInfo_aj (userID varchar(128),"
//
//                        + "cmSchedId varchar(128),"
//                        + "description varchar(128),"
//                        + "cmScTypeCd varchar(128),"
//                        + "spType   varchar(128),"
//                        + "scheduleDateTimeStart varchar(128),"
//                        + "cmMrDate varchar(128) default '0' ,"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set8.close();
//
//            // 下载客户信息表(custInfo_ju_aj)
//            ResultSet set9 = state
//                    .executeQuery("select * from dmedb_master where name = 'custInfo_ju_aj'");
//            if (!set9.next()) {
//                state.executeUpdate("create table custInfo_ju_aj ("
//                        + "cmSchedId varchar(128),"
//                        + "fieldActivityId varchar(128),"
//                        + "servicePointId varchar(128),"
//                        + "spType varchar(128)," + "badgeNumber varchar(128),"
//                        + "meterConfigurationId varchar(128),"
//                        + "accountId varchar(128),"
//                        + "entityName varchar(128),"
//                        + "customerClass varchar(128),"
//                        + "cmCustClDescr varchar(128),"
//                        + "cmMrAddress varchar(128),"
//                        + "cmMrDistrict varchar(128),"
//                        + "cmMrStreet varchar(128),"
//                        + "cmMrCommunity varchar(128),"
//                        + "cmMrBuilding varchar(128),"
//                        + "cmMrUnit varchar(128),"
//                        + "cmMrRoomNum varchar(128),"
//                        + "cmScOpenDttm varchar(128),"
//                        + "cmScResType varchar(128),"
//                        + "cmScUserType varchar(128),"
//                        + "meterType varchar(128),"
//                        + "manufacturer varchar(128)," + "model varchar(128),"
//                        + "serialNumber varchar(128),"
//                        + "cmMrMtrBarCode varchar(128),"
//                        + "cmMlr varchar(128)," + "cmScLgfmGj varchar(128),"
//                        + "cmScLgfmWz varchar(128),"
//                        + "cmScLgfmCz varchar(128)," + "cmScZjPp varchar(128),"
//                        + "cmScZjYs varchar(128)," + "cmScZjXhbh varchar(128),"
//                        + "cmScZjSyrq varchar(128),"
//                        + "cmScLjgCz varchar(128)," + "cmScCnlPp varchar(128),"
//                        + "cmScCnlPffs varchar(128),"
//                        + "cmScCnlSyrq varchar(128),"
//                        + "cmScRsqPp varchar(128),"
//                        + "cmScRsqPffs varchar(128),"
//                        + "cmScRsqSyrq varchar(128),"
//                        + "cmScBjqPp varchar(128),"
//                        + "cmScBjqSyrq varchar(128),"
//                        + "cmMrLastSecchkDt varchar(128),"
//                        + "cmScIntvl varchar(128)," + "cmScAqyh varchar(128),"
//                        + "cmScYhzg varchar(128)," + "cmScRemark varchar(128),"
//                        + "cmMrState varchar(128)," + "cmMrDate varchar(128),"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set9.close();
//            // 上传表
//            ResultSet set10 = state
//                    .executeQuery("select * from dmedb_master where name = 'uploadcustInfo_aj'");
//            if (!set10.next()) {
//                state.executeUpdate("create table uploadcustInfo_aj ("
//                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
//                        + "cmScResType varchar(128),"
//                        + "cmScUserType varchar(128),"
//                        + "cmScDttm varchar(128)," + "cmScAjrh varchar(128),"
//                        + "cmScYhzg varchar(128)," + "cmScZgtzd varchar(128),"
//                        + "cmScZtjs varchar(128)," + "cmMr varchar(128),"
//                        + "readType varchar(128)," + "cmScSyql varchar(128),"
//                        + "meterType varchar(128),"
//                        + "manufacturer varchar(128)," + "model varchar(128),"
//                        + "serialNumber varchar(128),"
//                        + "cmMrMtrBarCode varchar(128),"
//                        + "cmMlr varchar(128)," + "cmScLgfmGj varchar(128),"
//                        + "cmScLgfmWz varchar(128),"
//                        + "cmScLgfmCz varchar(128)," + "cmScZjPp varchar(128),"
//                        + "cmScZjYs varchar(128)," + "cmScZjXhbh varchar(128),"
//                        + "cmScZjSyrq varchar(128),"
//                        + "cmScLjgCz varchar(128)," + "cmScCnlPp varchar(128),"
//                        + "cmScCnlPffs varchar(128),"
//                        + "cmScCnlSyrq varchar(128),"
//                        + "cmScRsqPp varchar(128),"
//                        + "cmScRsqPffs varchar(128),"
//                        + "cmScRsqSyrq varchar(128),"
//                        + "cmScBjqPp varchar(128),"
//                        + "cmScBjqSyrq varchar(128),"
//                        + "cmScNotiPrtd varchar(128),"
//                        + "cmMrCommCd varchar(128),"
//                        + "cmScRemark varchar(128),"
//                        + "cmMrState varchar(128)," + "cmMrDate varchar(128),"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set10.close();
//
//            // 安全讲解(perSp_aj)
//            ResultSet set11 = state
//                    .executeQuery("select * from dmedb_master where name = 'perSp_aj'");
//            if (!set11.next()) {
//                state.executeUpdate("create table perSp_aj ("
//                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
//                        + "cmScSpItem varchar(128),"
//                        + "cmScSpCheck varchar(128),"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set11.close();
//
//            // 图片文件(perFile_aj)
//            ResultSet set12 = state
//                    .executeQuery("select * from dmedb_master where name = 'perFile_aj'");
//            if (!set12.next()) {
//                state.executeUpdate("create table perFile_aj ("
//                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
//                        + "cmScFileName varchar(128),"
//                        + "cmScFileTitle varchar(128),"
//                        + "cmScFileForm varchar(128),"
//                        + "cmScBusiType varchar(128),"
//                        + "cmScFileRoute varchar(128),"
//                        + "cmScFileSize varchar(128),"
//                        + "cmScFileDttm varchar(128),"
//                        + "cmScFileVar1 varchar(128),"
//                        + "cmScFileVar2 varchar(128),"
//                        + "cmScdate varchar(128) ,"
//                        + "cmMrState varchar(128) default 'N' ,"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set12.close();
//
//            // 隐患信息表(perSh_aj)
//            ResultSet set13 = state
//                    .executeQuery("select * from dmedb_master where name = 'perSh_aj'");
//            if (!set13.next()) {
//                state.executeUpdate("create table perSh_aj ("
//                        + "cmSchedId varchar(128)," + "accountId varchar(128),"
//                        + "cmScShType varchar(128),"
//                        + "cmScShTypeDescr varchar(128),"
//                        + "cmScShItem varchar(128),"
//                        + "cmScShItemDescr varchar(128),"
//                        + "cmScShCheck varchar(128),"
//                        + "cmScShIsOld varchar(128),"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set13.close();
//
//            // 数据字典_表具-型号(dic_modelInfo_aj)
//            ResultSet set14 = state
//                    .executeQuery("select * from dmedb_master where name = 'dic_modelInfo_aj'");
//            if (!set14.next()) {
//                state.executeUpdate("create table dic_modelInfo_aj ("
//                        + "manufacturer varchar(128),"
//                        + "manufacturerDescr varchar(128),"
//                        + "model varchar(128)," + "modelDescr varchar(128),"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set14.close();
//
//            // 数据字典_隐患选项(dic_cmScShItem_aj)
//            ResultSet set15 = state
//                    .executeQuery("select * from dmedb_master where name = 'dic_cmScShItem_aj'");
//            if (!set15.next()) {
//                state.executeUpdate("create table dic_cmScShItem_aj ("
//                        + "cmScShType varchar(128),"
//                        + "cmScShTypeDescr varchar(128),"
//                        + "cmScShNO varchar(128)," + "cmScShItem varchar(128),"
//                        + "cmScShItemDescr varchar(128),"
//                        + "Reserve_space1 varchar(128),"
//                        + "Reserve_space2 varchar(128),"
//                        + "Reserve_space3 varchar(128),"
//                        + "Reserve_space4 varchar(128),"
//                        + "Reserve_space5 varchar(128))");
//            }
//            set15.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // 抛异常处理,
//            Toast.makeText(getApplicationContext(), "数据库创建失败,", Toast.LENGTH_SHORT).show();
//            // System.exit(0);
//
//        } finally {
//            // 关闭连接
//            try {
//                if (state != null)
//                    state.close();
//                if (con != null)
//                    con.close();
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }


}
