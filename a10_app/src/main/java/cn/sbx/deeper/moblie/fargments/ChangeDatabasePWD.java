package cn.sbx.deeper.moblie.fargments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

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

import cn.sbx.deeper.moblie.activity.SinopecAllMenuActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.StreamUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * Created by caomang on 2017/2/21.
 */

public class ChangeDatabasePWD extends BaseFragment implements View.OnClickListener {
    private View view;
    private EditText et_password_login_change_psw,
            et_password_login_confirmation_change_psw;
    private Button btn_fetch_val_code_change_psw, bt_login_confirm_change_psw;
    private Context mContext;
    private String confirmPwd;
    ProgressHUD progressHUD;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 100:

                    progressHUD = AlertUtils.showDialog(getActivity(), null, null, false);
                    progressHUD.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                            SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
                            writableDatabase.changePassword(confirmPwd);

                            DatabaseHelper.PASSPHRASE = confirmPwd;

                            if (writableDatabase != null) {
                                try {
                                    writableDatabase.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            Message obtain = Message.obtain();
                            obtain.what = 200;
                            mHandler.sendMessage(obtain);

                        }
                    }).start();

                    break;
                case 200:
                    if(progressHUD!=null){
                        try {
                            progressHUD.dismiss();
                        }catch (Exception e){
                            progressHUD=null;
                            e.printStackTrace();
                        }

                    }
                    Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();



                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("请重新登录")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    SinopecAllMenuActivity.status = "0";// 将状态重置成0
                                    getActivity().finish();
                                    android.os.Process.killProcess(android.os.Process.myPid());

                                }
                            }).create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                    dialog.show();
                    dialog.setCancelable(false);
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                return false;
                        }
                    });





//                    backPrecious();
                    break;
                default:
                    break;
            }


        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
        tv_title.setText("修改数据库密码");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.changepsw, container, false);
        et_password_login_change_psw = (EditText) view.findViewById(R.id.et_password_login_change_psw);
        et_password_login_confirmation_change_psw = (EditText) view.findViewById(R.id.et_password_login_confirmation_change_psw);
        btn_fetch_val_code_change_psw = (Button) view.findViewById(R.id.btn_fetch_val_code_change_psw);
        bt_login_confirm_change_psw = (Button) view.findViewById(R.id.bt_login_confirm_change_psw);
        btn_fetch_val_code_change_psw.setOnClickListener(this);
        bt_login_confirm_change_psw.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fetch_val_code_change_psw:
                String newPwd = et_password_login_change_psw.getText().toString().trim();

                confirmPwd = et_password_login_confirmation_change_psw.getText().toString().trim();

                if ("".equals(newPwd)) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(confirmPwd)) {
                    Toast.makeText(mContext, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!((newPwd).equals(confirmPwd))) {
                    Toast.makeText(mContext, "两次输入的密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    et_password_login_change_psw.setText("");
                    et_password_login_confirmation_change_psw.setText("");
                    return;
                } else {

                    changePassward(newPwd);


                }


                break;
            case R.id.bt_login_confirm_change_psw:
                et_password_login_change_psw.setText("");
                et_password_login_confirmation_change_psw.setText("");


                break;
            default:
                break;


        }


    }


    private void changePassward(final String newPwd) {
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

                    if (result.equals("Success")) {

                        et_password_login_change_psw.setText("");
                        et_password_login_confirmation_change_psw.setText("");
                        Message obtain = Message.obtain();
                        obtain.what = 100;
                        mHandler.sendMessage(obtain);


                    } else {
                        Toast.makeText(mContext, "修改密码没有成功", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    return;

                }
            }

            protected void onCancelled() {
                running = false;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                overlayProgress = AlertUtils.showDialog(mContext,
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
                DataCollectionUtils.checkRole(Constants.loginName); // 检测开发者角色
                InputStream in = null;
                try {

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
                    nameValuePairs.add(new BasicNameValuePair("EquipType", "Android"));
                    nameValuePairs.add(new BasicNameValuePair("EquipSN", WebUtils.deviceId));

                    nameValuePairs.add(new BasicNameValuePair("RequestType", "add"));

                    nameValuePairs.add(new BasicNameValuePair("userId", Constants.loginName));
                    nameValuePairs.add(new BasicNameValuePair("userPwd", newPwd));

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




}
