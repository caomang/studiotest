package com.sunboxsoft.monitor.utils;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.OutPut;
import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.domian.SchedInfo;
import cn.sbx.deeper.moblie.domian.SchedInfoResidents;
import cn.sbx.deeper.moblie.service.WebServicesUtil;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.Base64;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.LogUtil;
import cn.sbx.deeper.moblie.util.ThreadUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;

public class UploadDate {
    private static final String TAG = "UploadDate";
    private ProgressHUD overlayProgress = null;
    private Context context;
    private ArrayList<SchedInfo> update_schedInfo = new ArrayList<SchedInfo>();// 存放已上传的任务
    // 抄表
    private ArrayList<SchedInfoResidents> update_schedInfo_AJ = new ArrayList<SchedInfoResidents>();// 存放已上传的任务安检
    public ArrayList<String> photoname_List = new ArrayList<String>();// //添加上传成功的图片
    // 存放已上传的用户的spMeterHistoryId
    private ArrayList<String> accountIdList = new ArrayList<String>();
    // 存放已上传的用户的spMeterHistoryId
    private ArrayList<String> schedList = new ArrayList<String>();
    //    private Connection db = null;
    private SQLiteDatabase state = null;
    private final int Filure = 10;
    private final int SUCCESS_PHOTO = 20;
    private DatabaseHelper allConnection;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ProgressHUD overlayProgress_AJ_A;
            switch (msg.what) {
                case Filure:
                    // overlayProgress = (ProgressHUD) msg.obj;
                    // if (overlayProgress != null) {
                    // overlayProgress.dismiss();
                    // }
                    // Toast.makeText(context, "抄表计划没有上传的任务或用户", 0).show();
                    break;
                case SUCCESS_PHOTO:


                    Toast.makeText(context, "图片上传完成", Toast.LENGTH_SHORT).show();

                    Bundle bundle = (Bundle) msg.obj;
                    overlayProgress_AJ_A = (ProgressHUD) bundle
                            .getSerializable("overlayProgress_AJ_A");
                    int account_up = bundle.getInt("account_up");
                    int account_all = bundle.getInt("account_all");

                    if (overlayProgress_AJ_A != null) {
                        overlayProgress_AJ_A.dismiss();
                    }
                    if (account_all == account_up) {
                        tv_des3.setText("安检图片 : 上传成功" + account_up + "张.");
                        tv_des3.setVisibility(View.VISIBLE);
                    } else {
                        tv_des3.setText("安检图片 : 成功上传" + account_up + "张,共计"
                                + account_all + "张.");
                        tv_des3.setVisibility(View.VISIBLE);
                    }

                    Intent intent = new Intent(
                            Constants.receive_upload_result_anjian);
                    // intent.putExtra("result", value);
                    context.sendBroadcast(intent);
                    break;

                default:
                    break;
            }
        }

        ;
    };
    private FTPUtils ftpUtils;
    private boolean flag;
    private int account_up;
    private TextView tv_des3;

    public UploadDate(Context context, TextView tv_des3) {
        this.context = context;
        this.tv_des3 = tv_des3;

    }

    // 获取抄表要上传的任务数据
    public ArrayList<SchedInfo> selectSchedInfos(String user_nameLogin) {
        // String userName_login = PerfUtils.getString(context, "userName", "");

        String sql_selectSchedInfos = "select rowId,cmMrRteDescr,meterReadCycle,scheduledSelectionDate,cmMrDate,meterReadRoute from schedInfo where userID = "
                + "'" + user_nameLogin + "'" + "";
        // Cursor cursor_schedInfos = db.rawQuery(sql_selectSchedInfos, null);
        ArrayList<SchedInfo> schedInfos_List = new ArrayList<SchedInfo>();

        try {
//            db = SQLiteData.openOrCreateDatabase();
            state = allConnection.getWritableDatabase();
            Cursor cursor_schedInfos = state
                    .rawQuery(sql_selectSchedInfos,null);
            SchedInfo schedInfo;
            if(cursor_schedInfos.moveToFirst()){
                select1(schedInfos_List, cursor_schedInfos);
                while (cursor_schedInfos.moveToNext()) {
                    select1(schedInfos_List, cursor_schedInfos);
                    // stat.close();
                }
            }


        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
//            if (db != null) {
//                try {
//                    db.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return schedInfos_List;
    }

    private void select1(ArrayList<SchedInfo> schedInfos_List, Cursor cursor_schedInfos) {
        SchedInfo schedInfo;
        schedInfo = new SchedInfo();
        schedInfo.setRowId(cursor_schedInfos.getString(0));
        schedInfo.setCmMrRteDescr(cursor_schedInfos.getString(1));
        schedInfo.setMeterReadCycle(cursor_schedInfos.getString(2));
        schedInfo.setScheduledSelectionDate(cursor_schedInfos
                .getString(3));
        schedInfo.setCmMrDate(cursor_schedInfos.getString(4));
        schedInfo.setMeterReadRoute(cursor_schedInfos
                .getString(cursor_schedInfos.getColumnIndex("meterReadRoute")));

        // 获取当前任务可上传的数量
        //
        // String sql_account =
        // "select distinct * from custInfo where schedInfoID = '"
        // + cursor_schedInfos.getString(1)
        // + "' and cmMrState = '3'";
        //
        // ResultSet executeQuery = stat.executeQuery(sql_account);
        // executeQuery.last();
        //
        // schedInfo.setAccount(executeQuery.getRow()); // 当前任务下的可上传客户数量
        // executeQuery.close();
        // 该任务列中还有用户未上传,需要显示该任务
        schedInfos_List.add(schedInfo);
    }

    // 执行上传任务
    public void upload_CB(ArrayList<SchedInfo> schedInfos, Context context,
                          String userName, TextView tv_des1) {
        new UploadAsynTask_CB(schedInfos, context, userName, tv_des1).execute();
    }

    // 上传抄表数据
    public class UploadAsynTask_CB extends AsyncTask<Void, integer, String> {

        private ArrayList<SchedInfo> sched_list;
        private Context context;
        private String string;
        private String user_nameLogin;
        private TextView tv_des1;

        public UploadAsynTask_CB(ArrayList<SchedInfo> schedInfos,
                                 Context context, String userName, TextView tv_des1) {
            this.tv_des1 = tv_des1;
            this.context = context;
            this.sched_list = schedInfos;
            this.user_nameLogin = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(context, null, null, false);
            tv_des1.setText("抄表 : 正在上传...");
            tv_des1.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (overlayProgress != null) {
                overlayProgress.dismiss();
            }
            // 解析返回值
            if (result == null) {
                Toast.makeText(context, "抄表所选计划没有上传的任务或用户", Toast.LENGTH_SHORT).show();
                tv_des1.setText("抄表 : 没有上传的任务或用户");
            } else {

                List<OutPut> list = getResult_CB(result);
                if (list != null && list.size() > 0 && list.get(0) != null) {

                    Toast.makeText(context,
                            "抄表所选计划上传" + list.get(0).getRespDescr().trim(),
                            Toast.LENGTH_LONG).show();
                    // 上传成功后 修改用户表 上传表状态,以及上传时间
                    if (schedList.size() > 0) {
                        try {
                            // 获取系统当前时间
                            long timeMillis = System.currentTimeMillis();

//                            db = SQLiteData.openOrCreateDatabase();
                             state = allConnection.getWritableDatabase();
                            for (String item : schedList) {
                                // 更改用户信息表状态
                                String sql_updateCustInfo = "update custInfo set cmMrState = "
                                        + "'"
                                        + 2
                                        + "'"
                                        + " where  spMeterHistoryId= '"
                                        + item
                                        + "'";
                                // 插入上传时间,首先判断该用户是否已上传过,是更新时间还是插入时间?
                                // 更新时间
                                String sql_insert = "update custInfo set cmMrDate = "
                                        + "'"
                                        + timeMillis
                                        + "'"
                                        + " where  spMeterHistoryId='"
                                        + item
                                        + "'";

                                state.execSQL(sql_insert);
                                state.execSQL(sql_updateCustInfo);

                                // 修改上传表中状态
                                String sql_upload = "update uploadcustInfo set cmMrState = "
                                        + "'"
                                        + 1
                                        + "'"
                                        + " where  spMeterHistoryId='"
                                        + item
                                        + "'";

                                String sql_insert_time = "update uploadcustInfo set cmMrDate = "
                                        + "'"
                                        + timeMillis
                                        + "'"
                                        + " where  spMeterHistoryId='"
                                        + item
                                        + "'";

                                state.execSQL(sql_insert_time);
                                state.execSQL(sql_upload);
                            }
                            // 将上传的任务插入时间
                            if (update_schedInfo.size() > 0) {
                                for (SchedInfo schedInfo : update_schedInfo) {

                                    String sql_schedInfo_time = "update schedInfo set cmMrDate = "
                                            + "'"
                                            + timeMillis
                                            + "'"
                                            + " where  meterReadRoute="
                                            + "'"
                                            + schedInfo.getMeterReadRoute()
                                            + "'"
                                            + " and meterReadCycle ="
                                            + "'"
                                            + schedInfo.getMeterReadCycle()
                                            + "'"
                                            + " and scheduledSelectionDate="
                                            + "'"
                                            + schedInfo
                                            .getScheduledSelectionDate()
                                            + "'";
                                    state.execSQL(sql_schedInfo_time);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (state != null) {
                                try {
                                    state.close();
                                } catch (SQLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
//                            if (db != null) {
//                                try {
//                                    db.close();
//                                } catch (SQLException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//
//                            }
                        }
                    }

                    if (Constants.anJianSched_NO) {
                        // 发送上传成功的广播
                        Intent intent = new Intent(
                                Constants.receive_upload_result_anjian);
                        // intent.putExtra("result", value);
                        context.sendBroadcast(intent);
                        Constants.anJianSched_NO = false;
                    }

                    // backPrecious();
                    tv_des1.setText("抄表 : 共上传计划" + update_schedInfo.size()
                            + "个、任务" + schedList.size() + "条。");
                } else {
                    Toast.makeText(context, "抄表所选计划上传失败", Toast.LENGTH_LONG).show();
                    if (overlayProgress != null) {
                        overlayProgress.dismiss();
                    }
                    tv_des1.setText("抄表 : 上传失败");
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            SQLiteDatabase state2= null;
            SQLiteDatabase state3= null;
//            Connection db = null;
            try {
//                db = SQLiteData.openOrCreateDatabase();

                // 标记 是否有要上传的用户 ,防止上传空得任务
                boolean isEmpty = false;
                // 存放任务数据
                String schedInfo = "";
                for (SchedInfo schedInfos : sched_list) {
                    if (schedInfos.isCheck) {
                        // 将上传的任务添加到集合,以插入上传时间
                        update_schedInfo.add(schedInfos);
                        // // 查询当前查询的任务
                        // while (schedInfos.moveToNext()) {
                        // 存放所有任务
                        String strDate = "";
                        String rowId = schedInfos.getRowId();
                        String meterReadRoute = schedInfos.getMeterReadRoute();
                        String meterReadCycle = schedInfos.getMeterReadCycle();
                        String scheduledSelectionDate = schedInfos
                                .getScheduledSelectionDate();

						/*
                         * String sql_uploadcustInfo =
						 * "select u.spMeterHistoryId,u.meterConfigurationId,u.cmMrDttm,u.cmMr,u.readType,u.cmMrRefVol,u.cmMrRefDebt,u.cmMrNotiPrtd,u.cmMrCommCd,u.cmMrRemark,c.accountId "
						 * +
						 * "from custInfo as c join uploadcustInfo as u on c.spMeterHistoryId = u.spMeterHistoryId "
						 * + " where c.schedInfoID=" + "'" + rowId + "'";
						 */
                        // ResultSet uploadcustInfo = state
                        // .executeQuery(sql_uploadcustInfo);
                        // PreparedStatement prepareStatement =
                        // db.prepareStatement(sql_uploadcustInfo);
                        // ResultSet uploadcustInfo =
                        // prepareStatement.executeQuery();
                        // 创建 客户上传JSON数据
                        String userInfo = "";

                        // 查询客户
                        String cust_his = "select spMeterHistoryId,accountId from custInfo where schedInfoID = '"
                                + rowId + "' and cmMrState = '3' ";
                        Cursor executeQuery = state.rawQuery(cust_his,null);

                        executeQuery.moveToLast();
                        int row = executeQuery.getCount();
                        if(executeQuery.moveToFirst()){
                            while (executeQuery.moveToNext()) {
                                String spMeterHistoryId = executeQuery.getString(0);
                                String accountId = executeQuery.getString(1);


                                state2 = allConnection.getWritableDatabase();


                                String up_sched = "select meterConfigurationId,cmMrDttm,cmMr,readType,cmMrRefVol,cmMrRefDebt,cmMrNotiPrtd,cmMrCommCd,cmMrRemark from uploadcustInfo where spMeterHistoryId = '"
                                        + spMeterHistoryId + "'";
                                Cursor uploadcust = state2
                                        .rawQuery(up_sched,null);
                                if(uploadcust.moveToFirst()){
                                    //+++++++++++++++++++++++++++++++++++++++++++
                                    // 将上传的Id存到数组
                                    schedList.add(spMeterHistoryId);
                                    String time1 = "";
                                    if (uploadcust.getString(1) != null) {

                                        time1 = uploadcust.getString(1)
                                                .replace(" ", "-")
                                                .replace(":", ".");
                                    }
                                    // for (int a = 0; a < 600; a++) {
                                    String str1 = String
                                            .format("{\"#comment\":[],\"spMeterHistoryId\":\""
                                                    + spMeterHistoryId
                                                    + "\","
                                                    + "\"meterConfigurationId\":\""
                                                    + uploadcust.getString(0)
                                                    + "\",\"cmMrDttm\":\""
                                                    + time1
                                                    + "\",\"cmMr\":\""
                                                    + uploadcust.getString(2)
                                                    + "\",\"readType\":\""
                                                    + uploadcust.getString(3)
                                                    + "\",\"cmMrRefVol\":\""
                                                    + uploadcust.getString(4)
                                                    + "\","
                                                    + "\"cmMrRefDebt\":\""
                                                    + uploadcust.getString(5)
                                                    + "\",\"cmMrNotiPrtd\":\""
                                                    + 'Y'
                                                    + "\",\"cmMrCommCd\":\""
                                                    + uploadcust.getString(7)
                                                    + "\",\"cmMrRemark\":\""
                                                    + uploadcust.getString(8)
                                                    + "\","
                                                    + "\"perPhone\":[ "
                                                    + "$phones$" + " ]}");

                                    // 原始
                                    String sql_perPhone1 = "select phoneType,sequence,phone,extension,cmPhoneOprtType  from  perPhone where  cmPhoneOprtType in('10','20','30') and accountId ="
                                            + "'" + accountId + "'";

                                    state3 = allConnection.getWritableDatabase();
                                    Cursor perPhone_cursor1 = state3
                                            .rawQuery(sql_perPhone1,null);
                                    // 创建变量
                                    String phones1 = "";
                                    // 遍历电话
                                    if(perPhone_cursor1.moveToFirst()){
                                        phones1 = select_perPhone_cursor(perPhone_cursor1, phones1);
                                        while (perPhone_cursor1.moveToNext()) {
                                            // 手机号码有修改

                                            phones1 = select_perPhone_cursor(perPhone_cursor1, phones1);
                                        }
                                    }

                                    userInfo += str1.replace("$phones$", phones1)
                                            + ",";
                                    // 第三层while循环结束
                                    perPhone_cursor1.close();
                                    if (!userInfo.equals("")) {
                                        isEmpty = true;
                                    }

                                    //+++++++++++++++++++++++++++++++++++++++++++++
                                    while (uploadcust.moveToNext()) {

                                        // 将上传的Id存到数组
//                                        schedList.add(spMeterHistoryId);
                                        String time = "";
                                        if (uploadcust.getString(1) != null) {

                                            time = uploadcust.getString(1)
                                                    .replace(" ", "-")
                                                    .replace(":", ".");
                                        }
                                        // for (int a = 0; a < 600; a++) {
                                        String str = String
                                                .format("{\"#comment\":[],\"spMeterHistoryId\":\""
                                                        + spMeterHistoryId
                                                        + "\","
                                                        + "\"meterConfigurationId\":\""
                                                        + uploadcust.getString(0)
                                                        + "\",\"cmMrDttm\":\""
                                                        + time
                                                        + "\",\"cmMr\":\""
                                                        + uploadcust.getString(2)
                                                        + "\",\"readType\":\""
                                                        + uploadcust.getString(3)
                                                        + "\",\"cmMrRefVol\":\""
                                                        + uploadcust.getString(4)
                                                        + "\","
                                                        + "\"cmMrRefDebt\":\""
                                                        + uploadcust.getString(5)
                                                        + "\",\"cmMrNotiPrtd\":\""
                                                        + 'Y'
                                                        + "\",\"cmMrCommCd\":\""
                                                        + uploadcust.getString(7)
                                                        + "\",\"cmMrRemark\":\""
                                                        + uploadcust.getString(8)
                                                        + "\","
                                                        + "\"perPhone\":[ "
                                                        + "$phones$" + " ]}");

                                        // 原始
                                        String sql_perPhone = "select phoneType,sequence,phone,extension,cmPhoneOprtType  from  perPhone where  cmPhoneOprtType in('10','20','30') and accountId ="
                                                + "'" + accountId + "'";

                                        state3 = allConnection.getWritableDatabase();
                                        Cursor perPhone_cursor = state3
                                                .rawQuery(sql_perPhone,null);
                                        // 创建变量
                                        String phones = "";
                                        // 遍历电话
                                        if(perPhone_cursor.moveToFirst()){
                                            phones = select_perPhone_cursor(perPhone_cursor, phones);
                                            while (perPhone_cursor.moveToNext()) {
                                                // 手机号码有修改

                                                phones = select_perPhone_cursor(perPhone_cursor, phones);
                                            }
                                        }

                                        userInfo += str.replace("$phones$", phones)
                                                + ",";
                                        // 第三层while循环结束
                                        perPhone_cursor.close();
                                        if (!userInfo.equals("")) {
                                            isEmpty = true;
                                        }
                                    }
                                }
                             // 第二层while循环结束
                                uploadcust.close();
                            }
                        }
                      // ------
                        strDate = "{\"#comment\":[],\"meterReadRoute\":\""
                                + meterReadRoute + "\",\"meterReadCycle\":\""
                                + meterReadCycle + "\","
                                + "\"scheduledSelectionDate\":\""
                                + scheduledSelectionDate + "\",\"custInfo\":[ "
                                + userInfo + " ]}";
                        schedInfo += strDate + ",";
                        // }// ------
                        executeQuery.close();
                    }
                }// 第一层while循环结束
                if (!schedInfo.equals("") && isEmpty) {

                    String key1 = "{\"root\":{\"list\":{\"CM_H_MRStUp\":{\"@faultStyle\":\"wsdl\""
                            + ",\"input\":{\"#comment\":[],\"user\":\""
                            + user_nameLogin
                            + "\",\"deviceId\":\""
                            + "deviceId"// 修改
                            + "\"," + "\"schedInfo\": [" + schedInfo + "]}}}}}";

                    System.out.println("----key1--------" + key1);

                    string = DataCollectionUtils.SynchronousData(
                            WebUtils.uploadchaobiaoUrl, key1);

                } else {
                    // Message msg = mHandler.obtainMessage(Filure);
                    // msg.obj = overlayProgress;
                    // mHandler.sendMessage(msg);
                    // Toast.makeText(context, "抄表计划没有上传的任务或用户", 0).show();aa
                }

                // 除了要去修改数据以外,还需要告知用户,所有的条目已经选中,刷新数据适配器
                // myAdapter.notifyDataSetChanged();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
//                if (db != null) {
//                    try {
//                        db.close();
//                    } catch (SQLException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
                if (state != null) {
                    try {
                        state.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (state2 != null) {
                    try {
                        state2.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (state3 != null) {
                    try {
                        state3.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return string;
        }


    }


    private String select_perPhone_cursor(Cursor perPhone_cursor, String phones) {
        String p = "{\"#comment\":[],\"phoneType\":\""
                + perPhone_cursor.getString(0)
                + "\",\"sequence\":\""
                + perPhone_cursor.getString(1)
                + "\",\"phone\":\""
                + perPhone_cursor.getString(2)
                + "\",\"extension\":\""
                + perPhone_cursor.getString(3)
                + "\",\"cmPhoneOprtType\":\""
                + perPhone_cursor.getString(4)
                + "\"}";
        phones += p + ",";
        p = null;
        return phones;
    }


    // 获取安检上传任务
    public ArrayList<SchedInfoResidents> selectSchedInfos_AJ(
            String user_nameLogin) {
        // String userName_login = PerfUtils.getString(mActivity, "userName",
        // "");

        // 获取系统当前时间
        long new_timeMillis = System.currentTimeMillis();

        String sql_selectSchedInfos = "select * from schedInfo_aj where userID = "
                + "'" + user_nameLogin + "'" + "";
        // Cursor cursor_schedInfos = db.rawQuery(sql_selectSchedInfos, null);
        ArrayList<SchedInfoResidents> schedInfos_List = new ArrayList<SchedInfoResidents>();
        try {
//            db = SQLiteData.openOrCreateDatabase();
            state = allConnection.getWritableDatabase();
//            stat = allConnection.createStatement();

            Cursor cursor_schedInfos = state
                    .rawQuery(sql_selectSchedInfos,null);
            SchedInfoResidents schedInfo;
            if(cursor_schedInfos.moveToFirst()){
                while (cursor_schedInfos.moveToNext()) {
                    getSchedInfoResList(schedInfos_List, cursor_schedInfos);
                }
            }


        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
//            if (db != null) {
//                try {
//                    db.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
        return schedInfos_List;

    }

    private void getSchedInfoResList(ArrayList<SchedInfoResidents> schedInfos_List, Cursor cursor_schedInfos) {
        SchedInfoResidents schedInfo;
        schedInfo = new SchedInfoResidents();
        schedInfo
                .setCmSchedId(cursor_schedInfos.getString(cursor_schedInfos.getColumnIndex("cmSchedId")));
        schedInfo.setDescription(cursor_schedInfos
                .getString(cursor_schedInfos.getColumnIndex("description")));
        schedInfo.setCmScTypeCd(cursor_schedInfos
                .getString(cursor_schedInfos.getColumnIndex("cmScTypeCd")));
        schedInfo.setSpType(cursor_schedInfos.getString(cursor_schedInfos.getColumnIndex("spType")));
        schedInfo.setScheduleDateTimeStart(cursor_schedInfos
                .getString(cursor_schedInfos.getColumnIndex("scheduleDateTimeStart")));
        schedInfo.setCmMrDate(cursor_schedInfos.getString(cursor_schedInfos.getColumnIndex("cmMrDate")));

        // 该任务列中还有用户未上传,需要显示该任务
        schedInfos_List.add(schedInfo);
    }

    // 安检上传任务
    public void upData_AJ(Context context, String userName,
                          ArrayList<SchedInfoResidents> sched_list, TextView tv_des2,
                          TextView tv_des3) {

        try {

            allConnection = new DatabaseHelper(context);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        new uploadDataAsynTask_AJ(context, userName, sched_list, tv_des2,
                tv_des3).execute();
    }

    public class uploadDataAsynTask_AJ extends AsyncTask<Void, Void, String> {

        private ArrayList<SchedInfoResidents> sched_list;
        private Context context;
        private String user_nameLogin;
        private SQLiteDatabase state4;
        private SQLiteDatabase state6;
        private SQLiteDatabase state5;
        private String string;
        private ProgressHUD overlayProgress_AJ;
        private SQLiteDatabase state_AJ;
        private TextView tv_des2;
        private TextView tv_des3;

        public uploadDataAsynTask_AJ(Context context, String userName,
                                     ArrayList<SchedInfoResidents> sched_list, TextView tv_des2,
                                     TextView tv_des3) {
            this.tv_des2 = tv_des2;
            this.tv_des3 = tv_des3;
            this.sched_list = sched_list;
            this.context = context;
            this.user_nameLogin = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress_AJ = AlertUtils.showDialog(context, null, null,
                    false);
            tv_des2.setText("安检 : 正在上传...");
            tv_des2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //
            // if (overlayProgress_AJ != null) {
            // overlayProgress_AJ.dismiss();
            // }
            // sched.setText("上传结果 ：");
            // sched.setVisibility(View.VISIBLE);
            if (result == null) {
                if (overlayProgress_AJ != null) {
                    overlayProgress_AJ.dismiss();
                }
                Toast.makeText(context, "安检所选计划没有上传的任务或用户", Toast.LENGTH_SHORT).show();
                tv_des2.setText("安检 : 没有上传的任务或用户");
            } else {
                List<OutPut> list = getResult_AJ(result);
                if (list != null && list.size() > 0 && list.get(0) != null) {

                    Toast.makeText(context,
                            "安检计划上传" + list.get(0).getRespDescr(), Toast.LENGTH_LONG).show();
                    // 获取系统当前时间
                    long timeMillis = System.currentTimeMillis();
                    // 上传成功后 修改用户表 上传表状态,以及上传时间
                    if (accountIdList.size() > 0) {
                        try {
//                            db = SQLiteData.openOrCreateDatabase();
                            state = allConnection.getWritableDatabase();
                            for (String item : accountIdList) {
                                // 更改用户信息表状态
                                String sql_updateCustInfo = "update custInfo_ju_aj set cmMrState = "
                                        + "'"
                                        + 2
                                        + "'"
                                        + " where  accountId= '" + item + "'";
                                // 插入上传时间,首先判断该用户是否已上传过,是更新时间还是插入时间?
                                // 更新时间
                                String sql_insert = "update custInfo_ju_aj set cmMrDate = "
                                        + "'"
                                        + timeMillis
                                        + "'"
                                        + " where  accountId='" + item + "'";
                                state.execSQL(sql_insert);
                                state.execSQL(sql_updateCustInfo);

                                // 修改上传表中状态
                                String sql_upload = "update uploadcustInfo_aj set cmMrState = "
                                        + "'"
                                        + 2
                                        + "'"
                                        + " where  accountId='"
                                        + item + "'";

                                String sql_insert_time = "update uploadcustInfo_aj set cmMrDate = "
                                        + "'"
                                        + timeMillis
                                        + "'"
                                        + " where  accountId='" + item + "'";

                                state.execSQL(sql_insert_time);
                                state.execSQL(sql_upload);
                            }
                            // 将上传的任务插入时间
                            if (update_schedInfo_AJ.size() > 0) {
                                for (SchedInfoResidents schedInfo : update_schedInfo_AJ) {

                                    String sql_schedInfo_time = "update schedInfo_aj set cmMrDate = "
                                            + "'"
                                            + timeMillis
                                            + "'"
                                            + " where  cmSchedId="
                                            + "'"
                                            + schedInfo.getCmSchedId()
                                            + "'"
                                            + " and cmScTypeCd ="
                                            + "'"
                                            + schedInfo.getCmScTypeCd()
                                            + "'"
                                            + " and scheduleDateTimeStart="
                                            + "'"
                                            + schedInfo
                                            .getScheduleDateTimeStart()
                                            + "'";
                                    state.execSQL(sql_schedInfo_time);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (state != null) {
                                try {
                                    state.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            }
//                            if (db != null) {
//                                try {
//                                    db.close();
//                                } catch (SQLException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        }
                    }

                    tv_des2.setText("安检 : 共上传计划" + update_schedInfo_AJ.size()
                            + "个、任务" + accountIdList.size() + "条。");
                    ArrayList<PhotoAudio> allPicture = new ArrayList<PhotoAudio>();
                    for (int a = 0; a < update_schedInfo_AJ.size(); a++) {
                        ArrayList<PhotoAudio> sched_Picture = getSched_Picture(
                                user_nameLogin, update_schedInfo_AJ.get(a)
                                        .getCmSchedId());
                        allPicture.addAll(sched_Picture);
                    }
                    if (overlayProgress_AJ != null) {
                        overlayProgress_AJ.dismiss();
                    }
                    if (allPicture.size() > 0) {
                        // 安检图片上传
//                        int upPhto_account = upPhto(allPicture, user_nameLogin);
                        upPhto_WebServices(allPicture, user_nameLogin);
                    } else {
                        if (overlayProgress_AJ != null) {
                            overlayProgress_AJ.dismiss();
                        } // 发送上传成功的广播 Intent

                        Intent intent = new Intent(
                                Constants.receive_upload_result_anjian);
//							 intent.putExtra("result", value);
                        context.sendBroadcast(intent);


                    }
                    // if (overlayProgress_AJ_photo != null) {
                    // overlayProgress_AJ_photo.dismiss();
                    // }
                    // 发送上传成功的广播
                    /*
                     * Intent intent = new Intent(
					 * Constants.receive_upload_result_anjian); //
					 * intent.putExtra("result", value);
					 * context.sendBroadcast(intent);
					 */

                } else {
                    if (overlayProgress_AJ != null) {
                        overlayProgress_AJ.dismiss();
                    }
                    tv_des2.setText("安检 : 上传失败.");

                    Toast.makeText(context, "上传失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            SQLiteDatabase state2= null;
            SQLiteDatabase state3 = null;
//             state=null;
//            Connection db_AJ = null;
            try {
//                db_AJ = SQLiteData.openOrCreateDatabase();
//                state_AJ = db_AJ.createStatement(
//                        ResultSet.TYPE_SCROLL_INSENSITIVE,
//                        ResultSet.CONCUR_READ_ONLY);
                 state_AJ = allConnection.getWritableDatabase();
//                state_AJ = allConnection.getWritableDatabase();
                // 标记 是否有要上传的用户 ,防止上传空得任务
                boolean isEmpty = false;
                // 存放任务数据
                String schedInfo = "";
                for (SchedInfoResidents schedInfos : sched_list) {
                    if (schedInfos.isCheck) {
                        // 将上传的任务添加到集合,以插入上传时间
                        update_schedInfo_AJ.add(schedInfos);
                        // 存放所有任务
                        String strDate = "";
                        String cmSchedId = schedInfos.getCmSchedId();
                        String cmScTypeCd = schedInfos.getCmScTypeCd();
                        String spType = schedInfos.getSpType();

                        // 创建 客户上传JSON数据
                        String userInfo = "";

                        // 查询客户
                        String cust_his = "select accountId, fieldActivityId  ,servicePointId,badgeNumber,meterConfigurationId from custInfo_ju_aj where cmSchedId = '"
                                // + cmSchedId +
                                // "' and cmMrState in ('3','2') ";
                                + cmSchedId + "' and cmMrState = '3' ";
                        // state = db.createStatement();
                        Cursor execute_set = state_AJ.rawQuery(cust_his,null);

                        execute_set.moveToLast();
                        int row = execute_set.getCount();
                        // while (execute_set.next()) {

                        if(execute_set.moveToFirst()){

                            String accountId1 = execute_set.getString(0);
                            String fieldActivityId1 = execute_set.getString(1);
                            String servicePointId1 = execute_set.getString(2);
                            String badgeNumber1 = execute_set.getString(3);
                            String meterConfigurationId1 = execute_set
                                    .getString(4);

                            state2 = allConnection.getWritableDatabase();

                            String up_sched1 = "select distinct * from uploadcustInfo_aj where cmSchedId = '"
                                    + cmSchedId
                                    + "' and accountId = '"
                                    + accountId1 + "'";
                            Cursor uploadcust1 = state2
                                    .rawQuery(up_sched1,null);

                            if(uploadcust1.moveToFirst()){
                                //________________________________________
                                // 将上传的Id存到数组
                                accountIdList.add(accountId1);
                                String time1 = "";
                                if (uploadcust1.getString(0) != null) {

                                    time1 = uploadcust1.getString(uploadcust1.getColumnIndex("cmScDttm"))
                                            .replace(" ", "-")
                                            .replace(":", ".");
                                }
                                // for (int a = 0; a < 600; a++) {
                                // 如果安检入户情况不是正常入户, 只提交入户类型,其他信息不需提价
                                String str11 = "{\"#comment\":[],\"fieldActivityId\":\""
                                        + fieldActivityId1
                                        + "\","
                                        + "\"servicePointId\":\""
                                        + servicePointId1
                                        + "\",\"badgeNumber\":\""
                                        + badgeNumber1
                                        + "\",\"meterConfigurationId\":\""
                                        + meterConfigurationId1
                                        + "\","
                                        + "\"cmScDttm\":\"" + time1 + "\",";
                                if (!uploadcust1.getString(uploadcust1.getColumnIndex("cmScAjrh")).equals(
                                        "ZCRH")) { // 安检入户类型 不为正常入户,其他信息不提交
                                    str11 += "\"cmScAjrh\":\""
                                            + uploadcust1.getString(uploadcust1.getColumnIndex("cmScAjrh"))
                                            + "\",";
                                } else {
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScAjrh")))) {
                                        str11 += "\"cmScAjrh\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScAjrh"))
                                                + "\",";
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScYhzg")))) {
                                        str11 += "\"cmScYhzg\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScYhzg"))
                                                + "\",";
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScZgtzd")))) {
                                        str11 += "\"cmScZgtzd\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZgtzd"))
                                                + "\",";
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScZtjs")))) {
                                        str11 += "\"cmScZtjs\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZtjs"))
                                                + "\",";
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmMr")))) {
                                        str11 += "\"cmMr\":\""
                                                + uploadcust1.getString(uploadcust1.getColumnIndex("cmMr"))
                                                + "\",";
                                        str11 += "\"readType\":\"" + 60 + "\","; // 读数类型默认为
                                        // 普通
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScSyql")))) {
                                        str11 += "\"cmScSyql\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScSyql"))
                                                + "\",";
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmMrCommCd")))) {
                                        str11 += "\"cmMrCommCd\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmMrCommCd"))
                                                + "\",";
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScRemark")))) {
                                        str11 += "\"cmScRemark\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRemark"))
                                                + "\",";
                                    }

                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScUserType")))) {
                                        str11 += "\"cmScUserType\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScUserType"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScResType")))) {
                                        str11 += "\"cmScResType\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScResType"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("meterType")))) {
                                        str11 += "\"meterType\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("meterType"))
                                                + "\",";// 设备信息
                                    }

                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("manufacturer")))) {
                                        str11 += "\"manufacturer\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("manufacturer"))
                                                + "\",";// 设备信息
                                    }

                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("model")))) {
                                        str11 += "\"model\":\""
                                                + uploadcust1.getString(uploadcust1.getColumnIndex("model"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("serialNumber")))) {
                                        str11 += "\"serialNumber\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("serialNumber"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmMrMtrBarCode")))) {
                                        str11 += "\"cmMrMtrBarCode\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmMrMtrBarCode"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmMlr")))) {
                                        str11 += "\"cmMlr\":\""
                                                + uploadcust1.getString(uploadcust1.getColumnIndex("cmMlr"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScLgfmGj")))) {
                                        str11 += "\"cmScLgfmGj\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLgfmGj"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScLgfmWz")))) {
                                        str11 += "\"cmScLgfmWz\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLgfmWz"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScLgfmCz")))) {
                                        str11 += "\"cmScLgfmCz\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLgfmCz"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScZjPp")))) {
                                        str11 += "\"cmScZjPp\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjPp"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScZjYs")))) {
                                        str11 += "\"cmScZjYs\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjYs"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScZjXhbh")))) {
                                        str11 += "\"cmScZjXhbh\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjXhbh"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScZjSyrq")))) {
                                        str11 += "\"cmScZjSyrq\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjSyrq"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScLjgCz")))) {
                                        str11 += "\"cmScLjgCz\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLjgCz"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScCnlPp")))) {
                                        str11 += "\"cmScCnlPp\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScCnlPp"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScCnlPffs")))) {
                                        str11 += "\"cmScCnlPffs\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScCnlPffs"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScCnlSyrq")))) {
                                        str11 += "\"cmScCnlSyrq\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScCnlSyrq"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScRsqPp")))) {
                                        str11 += "\"cmScRsqPp\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRsqPp"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScRsqPffs")))) {
                                        str11 += "\"cmScRsqPffs\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRsqPffs"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScRsqSyrq")))) {
                                        str11 += "\"cmScRsqSyrq\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRsqSyrq"))
                                                + "\",";// 设备信息
                                    }
                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScBjqSyrq")))) {
                                        str11 += "\"cmScBjqSyrq\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScBjqSyrq"))
                                                + "\",";// 设备信息
                                    }

                                    if (!TextUtils.isEmpty(uploadcust1
                                            .getString(uploadcust1.getColumnIndex("cmScBjqPp")))) {
                                        str11 += "\"cmScBjqPp\":\""
                                                + uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScBjqPp"))
                                                + "\",";// 设备信息
                                    }

                                }
                                str11 += "\"perPhone\":[ " + "$phones$" + " ],"
                                        + "\"perSh\":[ " + "$perSh$" + " ],"
                                        + "\"perFile\":[ " + "$perFile$"
                                        + " ]," + "\"perSp\":[ " + "$perSp$"
                                        + " ]}";

                                String str21 = String.format(str11);

                                // 电话
                                String sql_perPhone11 = "select phoneType,sequence,phone,extension,cmPhoneOprtType  from  perPhone where  cmPhoneOprtType in('10','20','30') and accountId ="
                                        + "'" + accountId1 + "'";
                                state3 = allConnection.getWritableDatabase();
                                Cursor perPhone_cursor1 = state3
                                        .rawQuery(sql_perPhone11,null);
                                // 创建变量
                                String phones1 = "";
                                // 遍历电话
                                if(perPhone_cursor1.moveToFirst()){
                                    phones1 = modifyPhone(perPhone_cursor1, phones1);
                                    while (perPhone_cursor1.moveToNext()) {
                                        // 手机号码有修改
                                        phones1 = modifyPhone(perPhone_cursor1, phones1);
                                    }
                                }


                                // 隐患信息
                                String sql_perSh11 = "select distinct cmScShType,cmScShItem,cmScShCheck  from  perSh_aj where  cmSchedId= "
                                        + cmSchedId
                                        + " and accountId ="
                                        + "'"
                                        + accountId1 + "' and cmScShIsOld = 'N'";

                                state4 = allConnection.getWritableDatabase();

                                Cursor perPh_cursor1 = state4
                                        .rawQuery(sql_perSh11,null);
                                // 创建变量
                                String perPh1 = "";
                                // 遍历电话

                                if(perPh_cursor1.moveToFirst()){
                                    perPh1 = getPhoneNum(perPh_cursor1, perPh1);
                                    while (perPh_cursor1.moveToNext()) {
                                        // 手机号码有修改

                                        perPh1 = getPhoneNum(perPh_cursor1, perPh1);
                                    }
                                }


                                // 图片文件
                                String sql_perile11 = "select distinct  *  from  perFile_aj where  cmSchedId= "
                                        + cmSchedId
                                        + " and accountId ="
                                        + "'"
                                        + accountId1 + "'";

                                state5 = allConnection.getWritableDatabase();
                                Cursor perFile_cursor1 = state5
                                        .rawQuery(sql_perile11,null);
                                // 创建变量
                                String perFile1 = "";
                                if(perFile_cursor1.moveToFirst()){
                                    perFile1 = getPerFile(perFile_cursor1, perFile1);
                                    while (perFile_cursor1.moveToNext()) {

                                        perFile1 = getPerFile(perFile_cursor1, perFile1);
                                    }
                                }


                                // 安全讲解
                                String sql_perSp11 = "select  *  from  perSp_aj where  cmSchedId= '"
                                        + cmSchedId
                                        + "' and accountId ="
                                        + "'"
                                        + accountId1 + "'";

                                state6 = allConnection.getWritableDatabase();
                                Cursor perSp_cursor1 = state6
                                        .rawQuery(sql_perSp11,null);
                                // 创建变量
                                String perSp1 = "";
                                if(perSp_cursor1.moveToFirst()){
                                    perSp1 = getPerSp(perSp_cursor1, perSp1);
                                    while (perSp_cursor1.moveToNext()) {

                                        perSp1 = getPerSp(perSp_cursor1, perSp1);
                                    }
                                }

                                userInfo += str21.replace("$phones$", phones1)
                                        .replace("$perSh$", perPh1)
                                        .replace("$perFile$", perFile1)
                                        .replace("$perSp$", perSp1)
                                        + ",";
                                // 第三层while循环结束
                                perPhone_cursor1.close();
                                if (!userInfo.equals("")) {
                                    isEmpty = true;
                                }

                                //__________________________________________
                                while (uploadcust1.moveToNext()) {
                                    // 将上传的Id存到数组
                                    accountIdList.add(accountId1);
                                    String time = "";
                                    if (uploadcust1.getString(1) != null) {

                                        time = uploadcust1.getString(uploadcust1.getColumnIndex("cmScDttm"))
                                                .replace(" ", "-")
                                                .replace(":", ".");
                                    }
                                    // for (int a = 0; a < 600; a++) {
                                    // 如果安检入户情况不是正常入户, 只提交入户类型,其他信息不需提价
                                    String str1 = "{\"#comment\":[],\"fieldActivityId\":\""
                                            + fieldActivityId1
                                            + "\","
                                            + "\"servicePointId\":\""
                                            + servicePointId1
                                            + "\",\"badgeNumber\":\""
                                            + badgeNumber1
                                            + "\",\"meterConfigurationId\":\""
                                            + meterConfigurationId1
                                            + "\","
                                            + "\"cmScDttm\":\"" + time + "\",";
                                    if (!uploadcust1.getString(uploadcust1.getColumnIndex("cmScAjrh")).equals(
                                            "ZCRH")) { // 安检入户类型 不为正常入户,其他信息不提交
                                        str1 += "\"cmScAjrh\":\""
                                                + uploadcust1.getString(uploadcust1.getColumnIndex("cmScAjrh"))
                                                + "\",";
                                    } else {
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScAjrh")))) {
                                            str1 += "\"cmScAjrh\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScAjrh"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScYhzg")))) {
                                            str1 += "\"cmScYhzg\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScYhzg"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZgtzd")))) {
                                            str1 += "\"cmScZgtzd\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScZgtzd"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZtjs")))) {
                                            str1 += "\"cmScZtjs\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScZtjs"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmMr")))) {
                                            str1 += "\"cmMr\":\""
                                                    + uploadcust1.getString(uploadcust1.getColumnIndex("cmMr"))
                                                    + "\",";
                                            str1 += "\"readType\":\"" + 60 + "\","; // 读数类型默认为
                                            // 普通
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScSyql")))) {
                                            str1 += "\"cmScSyql\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScSyql"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmMrCommCd")))) {
                                            str1 += "\"cmMrCommCd\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmMrCommCd"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRemark")))) {
                                            str1 += "\"cmScRemark\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScRemark"))
                                                    + "\",";
                                        }

                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScUserType")))) {
                                            str1 += "\"cmScUserType\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScUserType"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScResType")))) {
                                            str1 += "\"cmScResType\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScResType"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("meterType")))) {
                                            str1 += "\"meterType\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("meterType"))
                                                    + "\",";// 设备信息
                                        }

                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("manufacturer")))) {
                                            str1 += "\"manufacturer\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("manufacturer"))
                                                    + "\",";// 设备信息
                                        }

                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("model")))) {
                                            str1 += "\"model\":\""
                                                    + uploadcust1.getString(uploadcust1.getColumnIndex("model"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("serialNumber")))) {
                                            str1 += "\"serialNumber\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("serialNumber"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmMrMtrBarCode")))) {
                                            str1 += "\"cmMrMtrBarCode\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmMrMtrBarCode"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmMlr")))) {
                                            str1 += "\"cmMlr\":\""
                                                    + uploadcust1.getString(uploadcust1.getColumnIndex("cmMlr"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLgfmGj")))) {
                                            str1 += "\"cmScLgfmGj\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScLgfmGj"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLgfmWz")))) {
                                            str1 += "\"cmScLgfmWz\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScLgfmWz"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLgfmCz")))) {
                                            str1 += "\"cmScLgfmCz\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScLgfmCz"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjPp")))) {
                                            str1 += "\"cmScZjPp\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScZjPp"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjYs")))) {
                                            str1 += "\"cmScZjYs\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScZjYs"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjXhbh")))) {
                                            str1 += "\"cmScZjXhbh\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScZjXhbh"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScZjSyrq")))) {
                                            str1 += "\"cmScZjSyrq\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScZjSyrq"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScLjgCz")))) {
                                            str1 += "\"cmScLjgCz\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScLjgCz"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScCnlPp")))) {
                                            str1 += "\"cmScCnlPp\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScCnlPp"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScCnlPffs")))) {
                                            str1 += "\"cmScCnlPffs\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScCnlPffs"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScCnlSyrq")))) {
                                            str1 += "\"cmScCnlSyrq\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScCnlSyrq"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRsqPp")))) {
                                            str1 += "\"cmScRsqPp\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScRsqPp"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRsqPffs")))) {
                                            str1 += "\"cmScRsqPffs\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScRsqPffs"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScRsqSyrq")))) {
                                            str1 += "\"cmScRsqSyrq\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScRsqSyrq"))
                                                    + "\",";// 设备信息
                                        }


                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScBjqSyrq")))) {
                                            str1 += "\"cmScBjqSyrq\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScBjqSyrq"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust1
                                                .getString(uploadcust1.getColumnIndex("cmScBjqPp")))) {
                                            str1 += "\"cmScBjqPp\":\""
                                                    + uploadcust1
                                                    .getString(uploadcust1.getColumnIndex("cmScBjqPp"))
                                                    + "\",";// 设备信息
                                        }
                                    }
                                    str1 += "\"perPhone\":[ " + "$phones$" + " ],"
                                            + "\"perSh\":[ " + "$perSh$" + " ],"
                                            + "\"perFile\":[ " + "$perFile$"
                                            + " ]," + "\"perSp\":[ " + "$perSp$"
                                            + " ]}";

                                    String str = String.format(str1);

                                    // 电话
                                    String sql_perPhone1 = "select phoneType,sequence,phone,extension,cmPhoneOprtType  from  perPhone where  cmPhoneOprtType in('10','20','30') and accountId ="
                                            + "'" + accountId1 + "'";
                                    state3 = allConnection.getWritableDatabase();
                                    Cursor perPhone_cursor = state3
                                            .rawQuery(sql_perPhone1,null);
                                    // 创建变量
                                    String phones = "";
                                    // 遍历电话
                                    if(perPhone_cursor.moveToFirst()){
                                        phones = modifyPhone(perPhone_cursor, phones);
                                        while (perPhone_cursor.moveToNext()) {
                                            // 手机号码有修改
                                            phones = modifyPhone(perPhone_cursor, phones);
                                        }
                                    }


                                    // 隐患信息
                                    String sql_perSh1 = "select distinct cmScShType,cmScShItem,cmScShCheck  from  perSh_aj where  cmSchedId= "
                                            + cmSchedId
                                            + " and accountId ="
                                            + "'"
                                            + accountId1 + "' and cmScShIsOld = 'N'";

                                    state4 = allConnection.getWritableDatabase();

                                    Cursor perPh_cursor = state4
                                            .rawQuery(sql_perSh1,null);
                                    // 创建变量
                                    String perPh = "";
                                    // 遍历电话

                                    if(perPh_cursor.moveToFirst()){
                                        perPh = getPhoneNum(perPh_cursor, perPh);
                                        while (perPh_cursor.moveToNext()) {
                                            // 手机号码有修改

                                            perPh = getPhoneNum(perPh_cursor, perPh);
                                        }
                                    }


                                    // 图片文件
                                    String sql_perile1 = "select distinct  *  from  perFile_aj where  cmSchedId= "
                                            + cmSchedId
                                            + " and accountId ="
                                            + "'"
                                            + accountId1 + "'";

                                    state5 = allConnection.getWritableDatabase();
                                    Cursor perFile_cursor = state5
                                            .rawQuery(sql_perile1,null);
                                    // 创建变量
                                    String perFile = "";
                                    if(perFile_cursor.moveToFirst()){
                                        perFile = getPerFile(perFile_cursor, perFile);
                                        while (perFile_cursor.moveToNext()) {

                                            perFile = getPerFile(perFile_cursor, perFile);
                                        }
                                    }


                                    // 安全讲解
                                    String sql_perSp1 = "select  *  from  perSp_aj where  cmSchedId= '"
                                            + cmSchedId
                                            + "' and accountId ="
                                            + "'"
                                            + accountId1 + "'";

                                    state6 = allConnection.getWritableDatabase();
                                    Cursor perSp_cursor = state6
                                            .rawQuery(sql_perSp1,null);
                                    // 创建变量
                                    String perSp = "";
                                    if(perSp_cursor.moveToFirst()){
                                        perSp = getPerSp(perSp_cursor, perSp);
                                        while (perSp_cursor.moveToNext()) {

                                            perSp = getPerSp(perSp_cursor, perSp);
                                        }
                                    }

                                    userInfo += str.replace("$phones$", phones)
                                            .replace("$perSh$", perPh)
                                            .replace("$perFile$", perFile)
                                            .replace("$perSp$", perSp)
                                            + ",";
                                    // 第三层while循环结束
                                    perPhone_cursor.close();
                                    if (!userInfo.equals("")) {
                                        isEmpty = true;
                                    }
                                }// 第二层while循环结束
                            }

                            uploadcust1.close();

                            while(execute_set.moveToNext()){
                                String accountId = execute_set.getString(0);
                                String fieldActivityId = execute_set.getString(1);
                                String servicePointId = execute_set.getString(2);
                                String badgeNumber = execute_set.getString(3);
                                String meterConfigurationId = execute_set
                                        .getString(4);

                                state2 = allConnection.getWritableDatabase();

                                String up_sched = "select distinct * from uploadcustInfo_aj where cmSchedId = '"
                                        + cmSchedId
                                        + "' and accountId = '"
                                        + accountId + "'";
                                Cursor uploadcust = state2
                                        .rawQuery(up_sched,null);

                                if(uploadcust.moveToFirst()){
                                    //_+___+__+_+_+_+_+_+_+__+_+_+__+_+_+_+_+_+


                                    // 将上传的Id存到数组
                                    accountIdList.add(accountId);
                                    String timex = "";
                                    if (uploadcust.getString(0) != null) {

                                        timex = uploadcust.getString(uploadcust.getColumnIndex("cmScDttm"))
                                                .replace(" ", "-")
                                                .replace(":", ".");
                                    }
                                    // for (int a = 0; a < 600; a++) {
                                    // 如果安检入户情况不是正常入户, 只提交入户类型,其他信息不需提价
                                    String str1x = "{\"#comment\":[],\"fieldActivityId\":\""
                                            + fieldActivityId
                                            + "\","
                                            + "\"servicePointId\":\""
                                            + servicePointId
                                            + "\",\"badgeNumber\":\""
                                            + badgeNumber
                                            + "\",\"meterConfigurationId\":\""
                                            + meterConfigurationId
                                            + "\","
                                            + "\"cmScDttm\":\"" + timex + "\",";
                                    if (!uploadcust.getString(uploadcust.getColumnIndex("cmScAjrh")).equals(
                                            "ZCRH")) { // 安检入户类型 不为正常入户,其他信息不提交
                                        str1x += "\"cmScAjrh\":\""
                                                + uploadcust.getString(uploadcust.getColumnIndex("cmScAjrh"))
                                                + "\",";
                                    } else {
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScAjrh")))) {
                                            str1x += "\"cmScAjrh\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScAjrh"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScYhzg")))) {
                                            str1x += "\"cmScYhzg\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScYhzg"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScZgtzd")))) {
                                            str1x += "\"cmScZgtzd\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZgtzd"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScZtjs")))) {
                                            str1x += "\"cmScZtjs\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZtjs"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmMr")))) {
                                            str1x += "\"cmMr\":\""
                                                    + uploadcust.getString(uploadcust.getColumnIndex("cmMr"))
                                                    + "\",";
                                            str1x += "\"readType\":\"" + 60 + "\","; // 读数类型默认为
                                            // 普通
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScSyql")))) {
                                            str1x += "\"cmScSyql\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScSyql"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmMrCommCd")))) {
                                            str1x += "\"cmMrCommCd\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmMrCommCd"))
                                                    + "\",";
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScRemark")))) {
                                            str1x += "\"cmScRemark\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRemark"))
                                                    + "\",";
                                        }

                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScUserType")))) {
                                            str1x += "\"cmScUserType\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScUserType"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScResType")))) {
                                            str1x += "\"cmScResType\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScResType"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("meterType")))) {
                                            str1x += "\"meterType\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("meterType"))
                                                    + "\",";// 设备信息
                                        }

                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("manufacturer")))) {
                                            str1x += "\"manufacturer\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("manufacturer"))
                                                    + "\",";// 设备信息
                                        }

                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("model")))) {
                                            str1x += "\"model\":\""
                                                    + uploadcust.getString(uploadcust.getColumnIndex("model"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("serialNumber")))) {
                                            str1x += "\"serialNumber\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("serialNumber"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmMrMtrBarCode")))) {
                                            str1x += "\"cmMrMtrBarCode\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmMrMtrBarCode"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmMlr")))) {
                                            str1x += "\"cmMlr\":\""
                                                    + uploadcust.getString(uploadcust.getColumnIndex("cmMlr"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScLgfmGj")))) {
                                            str1x += "\"cmScLgfmGj\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLgfmGj"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScLgfmWz")))) {
                                            str1x += "\"cmScLgfmWz\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLgfmWz"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScLgfmCz")))) {
                                            str1x += "\"cmScLgfmCz\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLgfmCz"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScZjPp")))) {
                                            str1x += "\"cmScZjPp\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjPp"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScZjYs")))) {
                                            str1x += "\"cmScZjYs\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjYs"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScZjXhbh")))) {
                                            str1x += "\"cmScZjXhbh\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjXhbh"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScZjSyrq")))) {
                                            str1x += "\"cmScZjSyrq\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjSyrq"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScLjgCz")))) {
                                            str1x += "\"cmScLjgCz\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLjgCz"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScCnlPp")))) {
                                            str1x += "\"cmScCnlPp\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScCnlPp"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScCnlPffs")))) {
                                            str1x += "\"cmScCnlPffs\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScCnlPffs"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScCnlSyrq")))) {
                                            str1x += "\"cmScCnlSyrq\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScCnlSyrq"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScRsqPp")))) {
                                            str1x += "\"cmScRsqPp\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRsqPp"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScRsqPffs")))) {
                                            str1x += "\"cmScRsqPffs\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRsqPffs"))
                                                    + "\",";// 设备信息
                                        }
                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScRsqSyrq")))) {
                                            str1x += "\"cmScRsqSyrq\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRsqSyrq"))
                                                    + "\",";// 设备信息
                                        }


                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScBjqSyrq")))) {
                                            str1x += "\"cmScBjqSyrq\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScBjqSyrq"))
                                                    + "\",";// 设备信息
                                        }

                                        if (!TextUtils.isEmpty(uploadcust
                                                .getString(uploadcust.getColumnIndex("cmScBjqPp")))) {
                                            str1x += "\"cmScBjqPp\":\""
                                                    + uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScBjqPp"))
                                                    + "\",";// 设备信息
                                        }

                                    }
                                    str1x += "\"perPhone\":[ " + "$phones$" + " ],"
                                            + "\"perSh\":[ " + "$perSh$" + " ],"
                                            + "\"perFile\":[ " + "$perFile$"
                                            + " ]," + "\"perSp\":[ " + "$perSp$"
                                            + " ]}";

                                    String strx = String.format(str1x);

                                    // 电话
                                    String sql_perPhonex = "select phoneType,sequence,phone,extension,cmPhoneOprtType  from  perPhone where  cmPhoneOprtType in('10','20','30') and accountId ="
                                            + "'" + accountId + "'";
                                    state3 = allConnection.getWritableDatabase();
                                    Cursor perPhone_cursorx = state3
                                            .rawQuery(sql_perPhonex,null);
                                    // 创建变量
                                    String phonesx = "";
                                    // 遍历电话
                                    if(perPhone_cursorx.moveToFirst()){
                                        phonesx = modifyPhone(perPhone_cursorx, phonesx);
                                        while (perPhone_cursorx.moveToNext()) {
                                            // 手机号码有修改
                                            phonesx = modifyPhone(perPhone_cursorx, phonesx);
                                        }
                                    }


                                    // 隐患信息
                                    String sql_perShx = "select distinct cmScShType,cmScShItem,cmScShCheck  from  perSh_aj where  cmSchedId= "
                                            + cmSchedId
                                            + " and accountId ="
                                            + "'"
                                            + accountId + "' and cmScShIsOld = 'N'";

                                    state4 = allConnection.getWritableDatabase();

                                    Cursor perPh_cursorx = state4
                                            .rawQuery(sql_perShx,null);
                                    // 创建变量
                                    String perPhx = "";
                                    // 遍历电话

                                    if(perPh_cursorx.moveToFirst()){
                                        perPhx = getPhoneNum(perPh_cursorx, perPhx);
                                        while (perPh_cursorx.moveToNext()) {
                                            // 手机号码有修改

                                            perPhx = getPhoneNum(perPh_cursorx, perPhx);
                                        }
                                    }


                                    // 图片文件
                                    String sql_perilex = "select distinct  *  from  perFile_aj where  cmSchedId= "
                                            + cmSchedId
                                            + " and accountId ="
                                            + "'"
                                            + accountId + "'";

                                    state5 = allConnection.getWritableDatabase();
                                    Cursor perFile_cursorx = state5
                                            .rawQuery(sql_perilex,null);
                                    // 创建变量
                                    String perFilex = "";
                                    if(perFile_cursorx.moveToFirst()){
                                        perFilex = getPerFile(perFile_cursorx, perFilex);
                                        while (perFile_cursorx.moveToNext()) {

                                            perFilex = getPerFile(perFile_cursorx, perFilex);
                                        }
                                    }


                                    // 安全讲解
                                    String sql_perSpx = "select  *  from  perSp_aj where  cmSchedId= '"
                                            + cmSchedId
                                            + "' and accountId ="
                                            + "'"
                                            + accountId + "'";

                                    state6 = allConnection.getWritableDatabase();
                                    Cursor perSp_cursorx = state6
                                            .rawQuery(sql_perSpx,null);
                                    // 创建变量
                                    String perSpx = "";
                                    if(perSp_cursorx.moveToFirst()){
                                        perSpx = getPerSp(perSp_cursorx, perSpx);
                                        while (perSp_cursorx.moveToNext()) {

                                            perSpx = getPerSp(perSp_cursorx, perSpx);
                                        }
                                    }

                                    userInfo += strx.replace("$phones$", phonesx)
                                            .replace("$perSh$", perPhx)
                                            .replace("$perFile$", perFilex)
                                            .replace("$perSp$", perSpx)
                                            + ",";
                                    // 第三层while循环结束
                                    perPhone_cursorx.close();
                                    if (!userInfo.equals("")) {
                                        isEmpty = true;
                                    }

                                    //_+_+_+__+_+_+_+_+__+_+_+_+_+_+_+_+__+_+_+_+

                                    while (uploadcust.moveToNext()) {
                                        // 将上传的Id存到数组
                                        accountIdList.add(accountId);
                                        String time = "";
                                        if (uploadcust.getString(1) != null) {

                                            time = uploadcust.getString(uploadcust.getColumnIndex("cmScDttm"))
                                                    .replace(" ", "-")
                                                    .replace(":", ".");
                                        }
                                        // for (int a = 0; a < 600; a++) {
                                        // 如果安检入户情况不是正常入户, 只提交入户类型,其他信息不需提价
                                        String str1 = "{\"#comment\":[],\"fieldActivityId\":\""
                                                + fieldActivityId
                                                + "\","
                                                + "\"servicePointId\":\""
                                                + servicePointId
                                                + "\",\"badgeNumber\":\""
                                                + badgeNumber
                                                + "\",\"meterConfigurationId\":\""
                                                + meterConfigurationId
                                                + "\","
                                                + "\"cmScDttm\":\"" + time + "\",";
                                        if (!uploadcust.getString(uploadcust.getColumnIndex("cmScAjrh")).equals(
                                                "ZCRH")) { // 安检入户类型 不为正常入户,其他信息不提交
                                            str1 += "\"cmScAjrh\":\""
                                                    + uploadcust.getString(uploadcust.getColumnIndex("cmScAjrh"))
                                                    + "\",";
                                        } else {
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScAjrh")))) {
                                                str1 += "\"cmScAjrh\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScAjrh"))
                                                        + "\",";
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScYhzg")))) {
                                                str1 += "\"cmScYhzg\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScYhzg"))
                                                        + "\",";
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZgtzd")))) {
                                                str1 += "\"cmScZgtzd\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScZgtzd"))
                                                        + "\",";
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZtjs")))) {
                                                str1 += "\"cmScZtjs\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScZtjs"))
                                                        + "\",";
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmMr")))) {
                                                str1 += "\"cmMr\":\""
                                                        + uploadcust.getString(uploadcust.getColumnIndex("cmMr"))
                                                        + "\",";
                                                str1 += "\"readType\":\"" + 60 + "\","; // 读数类型默认为
                                                // 普通
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScSyql")))) {
                                                str1 += "\"cmScSyql\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScSyql"))
                                                        + "\",";
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmMrCommCd")))) {
                                                str1 += "\"cmMrCommCd\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmMrCommCd"))
                                                        + "\",";
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRemark")))) {
                                                str1 += "\"cmScRemark\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScRemark"))
                                                        + "\",";
                                            }

                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScUserType")))) {
                                                str1 += "\"cmScUserType\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScUserType"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScResType")))) {
                                                str1 += "\"cmScResType\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScResType"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("meterType")))) {
                                                str1 += "\"meterType\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("meterType"))
                                                        + "\",";// 设备信息
                                            }

                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("manufacturer")))) {
                                                str1 += "\"manufacturer\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("manufacturer"))
                                                        + "\",";// 设备信息
                                            }

                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("model")))) {
                                                str1 += "\"model\":\""
                                                        + uploadcust.getString(uploadcust.getColumnIndex("model"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("serialNumber")))) {
                                                str1 += "\"serialNumber\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("serialNumber"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmMrMtrBarCode")))) {
                                                str1 += "\"cmMrMtrBarCode\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmMrMtrBarCode"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmMlr")))) {
                                                str1 += "\"cmMlr\":\""
                                                        + uploadcust.getString(uploadcust.getColumnIndex("cmMlr"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLgfmGj")))) {
                                                str1 += "\"cmScLgfmGj\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScLgfmGj"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLgfmWz")))) {
                                                str1 += "\"cmScLgfmWz\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScLgfmWz"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLgfmCz")))) {
                                                str1 += "\"cmScLgfmCz\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScLgfmCz"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjPp")))) {
                                                str1 += "\"cmScZjPp\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScZjPp"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjYs")))) {
                                                str1 += "\"cmScZjYs\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScZjYs"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjXhbh")))) {
                                                str1 += "\"cmScZjXhbh\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScZjXhbh"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScZjSyrq")))) {
                                                str1 += "\"cmScZjSyrq\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScZjSyrq"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScLjgCz")))) {
                                                str1 += "\"cmScLjgCz\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScLjgCz"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScCnlPp")))) {
                                                str1 += "\"cmScCnlPp\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScCnlPp"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScCnlPffs")))) {
                                                str1 += "\"cmScCnlPffs\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScCnlPffs"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScCnlSyrq")))) {
                                                str1 += "\"cmScCnlSyrq\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScCnlSyrq"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRsqPp")))) {
                                                str1 += "\"cmScRsqPp\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScRsqPp"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRsqPffs")))) {
                                                str1 += "\"cmScRsqPffs\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScRsqPffs"))
                                                        + "\",";// 设备信息
                                            }
                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScRsqSyrq")))) {
                                                str1 += "\"cmScRsqSyrq\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScRsqSyrq"))
                                                        + "\",";// 设备信息
                                            }


                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScBjqSyrq")))) {
                                                str1 += "\"cmScBjqSyrq\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScBjqSyrq"))
                                                        + "\",";// 设备信息
                                            }

                                            if (!TextUtils.isEmpty(uploadcust
                                                    .getString(uploadcust.getColumnIndex("cmScBjqPp")))) {
                                                str1 += "\"cmScBjqPp\":\""
                                                        + uploadcust
                                                        .getString(uploadcust.getColumnIndex("cmScBjqPp"))
                                                        + "\",";// 设备信息
                                            }

                                        }
                                        str1 += "\"perPhone\":[ " + "$phones$" + " ],"
                                                + "\"perSh\":[ " + "$perSh$" + " ],"
                                                + "\"perFile\":[ " + "$perFile$"
                                                + " ]," + "\"perSp\":[ " + "$perSp$"
                                                + " ]}";

                                        String str = String.format(str1);

                                        // 电话
                                        String sql_perPhone = "select phoneType,sequence,phone,extension,cmPhoneOprtType  from  perPhone where  cmPhoneOprtType in('10','20','30') and accountId ="
                                                + "'" + accountId + "'";
                                         state3 = allConnection.getWritableDatabase();
                                        Cursor perPhone_cursor = state3
                                                .rawQuery(sql_perPhone,null);
                                        // 创建变量
                                        String phones = "";
                                        // 遍历电话
                                        if(perPhone_cursor.moveToFirst()){
                                            phones = modifyPhone(perPhone_cursor, phones);
                                            while (perPhone_cursor.moveToNext()) {
                                                // 手机号码有修改
                                                phones = modifyPhone(perPhone_cursor, phones);
                                            }
                                        }


                                        // 隐患信息
                                        String sql_perSh = "select distinct cmScShType,cmScShItem,cmScShCheck  from  perSh_aj where  cmSchedId= "
                                                + cmSchedId
                                                + " and accountId ="
                                                + "'"
                                                + accountId + "' and cmScShIsOld = 'N'";

                                        state4 = allConnection.getWritableDatabase();

                                        Cursor perPh_cursor = state4
                                                .rawQuery(sql_perSh,null);
                                        // 创建变量
                                        String perPh = "";
                                        // 遍历电话

                                        if(perPh_cursor.moveToFirst()){
                                            perPh = getPhoneNum(perPh_cursor, perPh);
                                            while (perPh_cursor.moveToNext()) {
                                                // 手机号码有修改

                                                perPh = getPhoneNum(perPh_cursor, perPh);
                                            }
                                        }


                                        // 图片文件
                                        String sql_perile = "select distinct  *  from  perFile_aj where  cmSchedId= "
                                                + cmSchedId
                                                + " and accountId ="
                                                + "'"
                                                + accountId + "'";

                                        state5 = allConnection.getWritableDatabase();
                                        Cursor perFile_cursor = state5
                                                .rawQuery(sql_perile,null);
                                        // 创建变量
                                        String perFile = "";
                                        if(perFile_cursor.moveToFirst()){
                                            perFile = getPerFile(perFile_cursor, perFile);
                                            while (perFile_cursor.moveToNext()) {

                                                perFile = getPerFile(perFile_cursor, perFile);
                                            }
                                        }


                                        // 安全讲解
                                        String sql_perSp = "select  *  from  perSp_aj where  cmSchedId= '"
                                                + cmSchedId
                                                + "' and accountId ="
                                                + "'"
                                                + accountId + "'";

                                        state6 = allConnection.getWritableDatabase();
                                        Cursor perSp_cursor = state6
                                                .rawQuery(sql_perSp,null);
                                        // 创建变量
                                        String perSp = "";
                                        if(perSp_cursor.moveToFirst()){
                                            perSp = getPerSp(perSp_cursor, perSp);
                                            while (perSp_cursor.moveToNext()) {

                                                perSp = getPerSp(perSp_cursor, perSp);
                                            }
                                        }

                                        userInfo += str.replace("$phones$", phones)
                                                .replace("$perSh$", perPh)
                                                .replace("$perFile$", perFile)
                                                .replace("$perSp$", perSp)
                                                + ",";
                                        // 第三层while循环结束
                                        perPhone_cursor.close();
                                        if (!userInfo.equals("")) {
                                            isEmpty = true;
                                        }
                                    }// 第二层while循环结束
                                }

                                uploadcust.close();
                            }
                        }

//                        for (int a = 0; a < row; a++) {
//
//                        }// ------
                        strDate = "{\"#comment\":[],\"cmSchedId\":\""
                                + cmSchedId + "\",\"cmScTypeCd\":\""
                                + cmScTypeCd + "\"," + "\"spType\":\"" + spType
                                + "\",\"custInfo\":[ " + userInfo + " ]}";
                        schedInfo += strDate + ",";
                        // }// ------
                        execute_set.close();
                    }
                }// 第一层while循环结束
                if (!schedInfo.equals("") && isEmpty) {

                    final String key1 = "{\"root\":{\"list\":{\"CM_H_SCStUp\":{\"@faultStyle\":\"wsdl\""
                            + ",\"input\":{\"#comment\":[],\"user\":\""
                            + user_nameLogin
                            + "\",\"deviceId\":\""
                            + "deviceId"// 修改
                            + "\","
                            + "\"schedInfoResidents\": ["
                            + schedInfo
                            + "]}}}}}";

                    System.out.println("----key1--------" + key1);
                    string = DataCollectionUtils.SynchronousData(
                            WebUtils.uploadUrl_anjian, key1);
                } else {
                    // Toast.makeText(context, "没有上传的任务或用户", 0).show();
                }
                // 除了要去修改数据以外,还需要告知用户,所有的条目已经选中,刷新数据适配器
                // myAdapter.notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
//                if (db_AJ != null) {
//                    try {
//                        db_AJ.close();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
                if (state_AJ != null) {
                    try {
                        state_AJ.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (state2 != null) {
                    try {
                        state2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (state3 != null) {
                    try {
                        state3.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (state4 != null) {
                    try {
                        state4.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (state5 != null) {
                    try {
                        state5.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (state6 != null) {
                    try {
                        state6.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return string;
        }

    }

    @NonNull
    private String getPerSp(Cursor perSp_cursor, String perSp) {
        String p = "{\"#comment\":[],\"cmScSpItem\":\""
                + perSp_cursor
                .getString(perSp_cursor.getColumnIndex("cmScSpItem"))
                + "\",\"cmScSpCheck\":\""
                + perSp_cursor
                .getString(perSp_cursor.getColumnIndex("cmScSpCheck"))
                + "\"}";
        perSp += p + ",";
        p = null;
        return perSp;
    }

    @NonNull
    private String getPerFile(Cursor perFile_cursor, String perFile) {
        String p = "{\"#comment\":[],\"cmScFileName\":\""
                + perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileName"))
                + "\","
                +
                // "\"cmScFileTitle\":\""
                // + perFile_cursor.getString(2)
                // + "\"," +
                "\"cmScFileForm\":\""
                + perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileForm"))
                + "\","
                +
                // "\"cmScBusiType\":\""
                // + perFile_cursor.getString(4)
                // + "\"," +
                "\"cmScFileRoute\":\""
                + perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileRoute"))
                + "\",\"cmScFileSize\":\""
                + perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileSize"))
                + "\",\"cmScFileDttm\":\""
                + perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileDttm"))
                + "\",\"cmScFileVar1\":\""
                + perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileVar1"))
                + "\",\"cmScFileVar2\":\""
                + perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileVar2"))
                + "\"";
        // + "\"}";
        if (!TextUtils.isEmpty(perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScBusiType")))
                && !perFile_cursor.getString(
                perFile_cursor.getColumnIndex("cmScBusiType")).equals(
                "null")) {
            p += ",\"cmScBusiType\":\""
                    + perFile_cursor
                    .getString(perFile_cursor.getColumnIndex("cmScBusiType"))
                    + "\"";
        }
        if (!TextUtils.isEmpty(perFile_cursor
                .getString(perFile_cursor.getColumnIndex("cmScFileTitle")))
                && !perFile_cursor.getString(
                perFile_cursor.getColumnIndex("cmScFileTitle")).equals(
                "null")) {
            p += ",\"cmScFileTitle\":\""
                    + perFile_cursor
                    .getString(perFile_cursor.getColumnIndex("cmScFileTitle"))
                    + "\"";
        }
        p += "}";
        perFile += p + ",";
        p = null;
        return perFile;
    }

    @NonNull
    private String getPhoneNum(Cursor perPh_cursor, String perPh) {
        String p = "{\"#comment\":[],\"cmScShType\":\""
                + perPh_cursor.getString(0)
                + "\",\"cmScShItem\":\""
                + perPh_cursor.getString(1)
                + "\",\"cmScShCheck\":\""
                + perPh_cursor.getString(2) + "\"}";
        perPh += p + ",";
        p = null;
        return perPh;
    }

    @NonNull
    private String modifyPhone(Cursor perPhone_cursor, String phones) {
        String p = "{\"#comment\":[],\"phoneType\":\""
                + perPhone_cursor.getString(0)
                + "\",\"sequence\":\""
                + perPhone_cursor.getString(1)
                + "\",\"phone\":\""
                + perPhone_cursor.getString(2)
                + "\",\"extension\":\""
                + ("" + perPhone_cursor.getString(3))
                + "\",\"cmPhoneOprtType\":\""
                + perPhone_cursor.getString(4)
                + "\"}";
        phones += p + ",";
        p = null;
        return phones;
    }

    public List<OutPut> getResult_CB(String string) {
        List<OutPut> list = new ArrayList<OutPut>();
        // 创建集合 存放所有数据
        OutPut outPut = null;
        // 解析
        if (!TextUtils.isEmpty(string)) {
            Gson gson = new Gson();
            JSONObject json = null;
            String dataStr = null;
            // -- 基本信息f
            try {
                json = new JSONObject(string);
                String string1 = json.optString("soapenv:Envelope");
                json = new JSONObject(string1);
                String string2 = json.optString("soapenv:Body");
                json = new JSONObject(string2);
                String string3 = json.optString("CM_H_MRStUp");
                if (string3 == null || string3.equals("")) {
                    return list;
                }
                json = new JSONObject(string3);
                // dataStr = json.optString("output");
                // json = new JSONObject(dataStr);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --

            if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {

                for (int y = 0; y < jsonArrayOutPut.length(); y++) {

                    String soutput;
                    try {
                        soutput = jsonArrayOutPut.get(y).toString();
                        outPut = new OutPut();
                        if (!"".equals(soutput.toString()) && soutput != null)
                            outPut = gson.fromJson(soutput.toString(),
                                    OutPut.class);
                        list.add(outPut);
                        json = new JSONObject(soutput);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
        return list;

    }

    public List<OutPut> getResult_AJ(String string) {
        List<OutPut> list = new ArrayList<OutPut>();
        // 创建集合 存放所有数据
        OutPut outPut = null;
        // 解析
        if (!TextUtils.isEmpty(string)) {
            Gson gson = new Gson();
            JSONObject json = null;
            String dataStr = null;
            // -- 基本信息f
            try {
                json = new JSONObject(string);
                String string1 = json.optString("soapenv:Envelope");
                json = new JSONObject(string1);
                String string2 = json.optString("soapenv:Body");
                json = new JSONObject(string2);
                String string3 = json.optString("CM_H_SCStUp");
                if (string3 == null || string3.equals("")) {
                    return list;
                }
                json = new JSONObject(string3);
                // dataStr = json.optString("output");
                // json = new JSONObject(dataStr);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --

            if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {

                for (int y = 0; y < jsonArrayOutPut.length(); y++) {

                    String soutput;
                    try {
                        soutput = jsonArrayOutPut.get(y).toString();
                        outPut = new OutPut();
                        if (!"".equals(soutput.toString()) && soutput != null)
                            outPut = gson.fromJson(soutput.toString(),
                                    OutPut.class);
                        list.add(outPut);
                        json = new JSONObject(soutput);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
        return list;

    }

    public void uploadpicture_Anjian_Buchuan(Context context,
                                             ArrayList<PhotoAudio> arraylist, String userName, TextView tv_des3) {
        new UploadUtilsAsync(context, arraylist, userName, tv_des3).execute();
//		upPhto(arraylist,userName);
    }

    // 图片上传任务(补传)
    public class UploadUtilsAsync extends AsyncTask<String, Integer, Integer> {

        private Context context;
        private String date;
        private String userid;
        private String FilePath;
        private String FileName;
        private ProgressDialog progressDialog;
        private ArrayList<PhotoAudio> photos;
        private String userName;
        private ProgressHUD overlayProgress_AJ;
        private int account_up;
        private TextView tv_des3;

        public UploadUtilsAsync(Context context,
                                ArrayList<PhotoAudio> arraylist, String userName,
                                TextView tv_des3) {
            this.context = context;
            this.photos = arraylist;
            this.userName = userName;
            this.tv_des3 = tv_des3;
        }

        @Override
        protected void onPreExecute() {// 执行前的初始化
            // TODO Auto-generated method stub
            // progressDialog = new ProgressDialog(context);
            // progressDialog.setTitle("请稍等...");
            // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // progressDialog.setCancelable(true);
            // progressDialog.show();
            tv_des3.setText("正在进行图片补传,共" + photos.size() + "张...");
            tv_des3.setVisibility(View.VISIBLE);
            overlayProgress_AJ = AlertUtils.showDialog(context, null, null,
                    false);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {// 执行结果
            // TODO Auto-generated method stub
            Toast.makeText(context, "成功上传图片共:" + result + "张", Toast.LENGTH_LONG).show();
            // progressDialog.dismiss();

            if (overlayProgress_AJ != null) {
                overlayProgress_AJ.dismiss();
            }
            if (result == account_up) {
                tv_des3.setText("图片补传 : 上传成功.");
                tv_des3.setVisibility(View.VISIBLE);
            } else {
                tv_des3.setText("图片补传 : 成功上传" + account_up + "张,共计" + result
                        + "张.");
                tv_des3.setVisibility(View.VISIBLE);
            }

            // 发送上传成功的广播,关闭当前界面
            /*Intent intent = new Intent(Constants.receive_upload_result_anjian);
            // intent.putExtra("result", value);
			context.sendBroadcast(intent);*/

            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(String... params) {
            final String sdPath = android.os.Environment
                    .getExternalStorageDirectory().toString();

            ArrayList<PhotoAudio> allPicture = new ArrayList<PhotoAudio>();
            allPicture.addAll(photos);
            //			清空原始上传失败的图片集合
            Constants.list_BC_Picture.clear();
            photoname_List = new ArrayList<String>();
            account_up = 0;
            if (allPicture.size() > 0) {
                for (int j = 0; j < allPicture.size(); j++) {
                    final PhotoAudio audio = allPicture.get(j);
                    final boolean isLast = (j == allPicture.size() - 1);
//                    ThreadUtils.getThreadPool_Instance().submit(new Runnable() {
//                        @Override
//                        public void run() {
                    try {
                        final String base64File = Base64.encodeBase64File(sdPath + audio.getCmScFileRoute());
                        WebServicesUtil.connectWebServiceTest(userName, audio.getCmScdate(), audio, base64File, isLast, new WebServicesUtil.UploadTask() {
                            @Override
                            public void uploadSuccess(PhotoAudio photoAudio) {
                                photoname_List.add(audio.getCmScFileName());
                                account_up++;
                            }

                            @Override
                            public void uploadFailed(PhotoAudio photoAudio) {
                                //上传失败,再次上传
                                LogUtil.i(TAG, "图片上传失败:" + audio.getCmScFileName());
                                //判断是否为最后一张失败
                                upoadPicAgain(userName, audio.getCmScdate(), audio, base64File, isLast);
                            }

                            @Override
                            public void uploadFinish() {
                                LogUtil.i(TAG, "最后一张图片");
//                                        UploadDate.this.uploadFinish(photos, overlayProgress_AJ_A);
                                try {
                                    changeState_Photo(photoname_List);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                        }

//                    });
                }
            }
            return allPicture.size();
        }
    }

    public void InitFTPServerSetting() {
        ftpUtils = FTPUtils.getInstance();
        flag = ftpUtils.initFTPSetting(WebUtils.upPhotoUrl, WebUtils.upPhotoHost, WebUtils.upPhotoName, WebUtils.upPhotoPass);
    }

    // 图片上传
    public int upPhto(final ArrayList<PhotoAudio> photos, final String userName) {
        // if (android.os.Build.VERSION.SDK_INT > 9) {
        // StrictMode.ThreadPolicy policy = new
        // StrictMode.ThreadPolicy.Builder()
        // .permitAll().build();
        // StrictMode.setThreadPolicy(policy);
        // }
        final ProgressHUD overlayProgress_AJ_A = AlertUtils.showDialog(context,
                null, null, false);
        Toast.makeText(context, "正在上传图片...", Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                InitFTPServerSetting();
                String sdPath = android.os.Environment
                        .getExternalStorageDirectory().toString();
                ArrayList<String> photoname_List = null;
                if (flag) {// 登录成功
                    if (photos.size() > 0) {
                        photoname_List = new ArrayList<String>();
                        account_up = 0;
                        for (int j = 0; j < photos.size(); j++) {
                            PhotoAudio audio = photos.get(j);
                            boolean uploadFile = ftpUtils.uploadFile(
                                    audio.getCmScdate(), userName, sdPath
                                            + audio.getCmScFileRoute(),
                                    audio.getCmScFileName());
                            // 上传成功后将其添加到集合,修改图片上传状态,并更新进度条
                            if (uploadFile) {
                                photoname_List.add(audio.getCmScFileName());
                                account_up++;
                            } else {
//								如果上传失败 添加至集合 进行图片补传
                                Constants.list_BC_Picture.add(audio);
                            }
                            // progressDialog.setProgress(100 / photos.size() *
                            // a);
                            // //更新进度条
                        }
                        // 关闭链接
                        ftpUtils.closeClient();
                    }
                }
                // 更新数据库图片上传状态
                try {
                    changeState_Photo(photoname_List);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Message message = mHandler.obtainMessage();
                message.what = SUCCESS_PHOTO;
                Bundle bundle = new Bundle();
                bundle.putInt("account_up", account_up);
                bundle.putInt("account_all", photos.size());
                bundle.putSerializable("overlayProgress_AJ_A",
                        overlayProgress_AJ_A);
                message.obj = bundle;
                mHandler.sendMessage(message);

            }
        }).start();

        return photos.size();

    }


    public void testForUploadImg() {

        Toast.makeText(context, "正在上传图片...", Toast.LENGTH_LONG).show();
        final String sdPath = android.os.Environment
                .getExternalStorageDirectory().toString();
//        if (photos.size() > 0) {
//            account_up = 0;

//            for (int j = 0; j < photos.size(); j++) {
//                final PhotoAudio audio = photos.get(j);
//                final boolean isLast = (j == photos.size() - 1);
                ThreadUtils.getThreadPool_Instance().submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PhotoAudio audio=new PhotoAudio();
                            audio.setCmScBusiType("jpg");
                            audio.setCmScdate("20161209");
                            audio.setCmScFileDttm("xxxxxxxxxxxx");
                            audio.setCmScFileForm("xxxxxxxxxxxx");
                            audio.setCmScFileRoute("xxxxxxxxxxxx");
                            audio.setCmScFileSize("xxxxxxxxxxxx");
                            audio.setCmScFileTitle("xxxxxxxxxxxx");
                            audio.setCmScFileVar1("xxxxxxxxxxxx");
                            audio.setCmScFileVar2("xxxxxxxxxxxx");
                            audio.setTimeLength("xxxxxxxxxxxx");
                            audio.setUrl("xxxxxxxxxxxx");
                            audio.setCmScFileName("123456");

                            String urlForUp=sdPath + "/sc/20161209/t1400001/9557700071_20161209111246.jpg";
                            final String base64File = Base64.encodeBase64File(urlForUp);
                            System.out.println("base64File:==" + base64File);
                            WebServicesUtil.connectWebServiceTest("t1400001", "xxxxxxxxxxxx", audio, base64File, true, new WebServicesUtil.UploadTask() {
                                @Override
                                public void uploadSuccess(PhotoAudio photoAudio) {
                                    LogUtil.i(TAG, "成功一张图片:" );
                                }

                                @Override
                                public void uploadFailed(PhotoAudio photoAudio) {
                                    LogUtil.i(TAG, "失败一张图片:");

                                }

                                @Override
                                public void uploadFinish() {
                                    LogUtil.i(TAG, "最后一张图片");
                                    //UploadDate.this.uploadFinish(photos, overlayProgress_AJ_A);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
//            }
//        }
    }

    /**
     * 图案片上传到webservices
     *
     * @param photos
     * @param userName
     * @return
     */


    public int upPhto_WebServices(final ArrayList<PhotoAudio> photos, final String userName) {

        final ProgressHUD overlayProgress_AJ_A = AlertUtils.showDialog(context,
                null, null, false);
        Toast.makeText(context, "正在上传图片...", Toast.LENGTH_LONG).show();
        final String sdPath = android.os.Environment
                .getExternalStorageDirectory().toString();
        if (photos.size() > 0) {
            account_up = 0;

            for (int j = 0; j < photos.size(); j++) {
                final PhotoAudio audio = photos.get(j);
                final boolean isLast = (j == photos.size() - 1);
                ThreadUtils.getThreadPool_Instance().submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String base64File = Base64.encodeBase64File(sdPath + audio.getCmScFileRoute());
                            System.out.println("base64File:==" + base64File);
                            WebServicesUtil.connectWebServiceTest(userName, audio.getCmScdate(), audio, base64File, isLast, new WebServicesUtil.UploadTask() {
                                @Override
                                public void uploadSuccess(PhotoAudio photoAudio) {
                                    photoname_List.add(audio.getCmScFileName());
                                    account_up++;
                                    LogUtil.i(TAG, "成功一张图片:" + "account_up:" + account_up + "photoname_List.size" + photoname_List.size());
                                }

                                @Override
                                public void uploadFailed(PhotoAudio photoAudio) {
                                    //上传失败,再次上传
                                    LogUtil.i(TAG, "图片上传失败:" + audio.getCmScFileName());
                                    //判断是否为最后一张失败
                                    upoadPicAgain(userName, audio.getCmScdate(), audio, base64File, isLast);
                                }

                                @Override
                                public void uploadFinish() {
                                    LogUtil.i(TAG, "最后一张图片");
                                    UploadDate.this.uploadFinish(photos, overlayProgress_AJ_A);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                //最后一张图片上传后
//                if (j == photos.size() - 1) {
//                    LogUtil.i(TAG, "最后一张图片");
//                    ThreadUtils.threadPoolExecutor(new Runnable() {
//                        @Override
//                        public void run() {
//                            // 更新数据库图片上传状态
//                            try {
//                                changeState_Photo(photoname_List);
//                                photoname_List.clear();
//                                photoname_List = null;
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                                LogUtil.i(TAG, "数据库__修改图片上传状态失败");
//                            }
//                        }
//                    });
//                    Message message = mHandler.obtainMessage();
//                    message.what = SUCCESS_PHOTO;
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("account_up", account_up);
//                    bundle.putInt("account_all", photos.size());
//                    bundle.putSerializable("overlayProgress_AJ_A",
//                            overlayProgress_AJ_A);
//                    message.obj = bundle;
//                    mHandler.sendMessage(message);
//
//                }
            }
        }
        return photos.size();
    }

    private void uploadFinish(ArrayList<PhotoAudio> photos, ProgressHUD overlayProgress_AJ_A) {
//        ThreadUtils.getSingleThreadExecutor().submit(new Runnable() {
//            @Override
//            public void run() {
        // 更新数据库图片上传状态
        try {
            changeState_Photo(photoname_List);

        } catch (SQLException e) {
            e.printStackTrace();
            LogUtil.i(TAG, "数据库__修改图片上传状态失败");
        }
//            }
//        });
        Message message = mHandler.obtainMessage();
        message.what = SUCCESS_PHOTO;
        Bundle bundle = new Bundle();
        bundle.putInt("account_up", account_up);
        bundle.putInt("account_all", photos.size());
        bundle.putSerializable("overlayProgress_AJ_A",
                overlayProgress_AJ_A);
        message.obj = bundle;
        mHandler.sendMessage(message);
    }

    /**
     * 上传过程中出现失败的情况再次上传一次
     *
     * @param userName
     * @param cmScdate
     * @param audio
     * @param base64File
     */
    private void upoadPicAgain(final String userName, String cmScdate, final PhotoAudio audio, final String base64File, boolean isLast) {
        WebServicesUtil.connectWebServiceTest(userName, audio.getCmScdate(), audio, base64File, isLast, new WebServicesUtil.UploadTask() {
            @Override
            public void uploadSuccess(PhotoAudio photoAudio) {
                photoname_List.add(audio.getCmScFileName());
                account_up++;
                LogUtil.i(TAG, "图片补传成功:" + audio.getCmScFileName() + "== account_up" + account_up + "==photoname_List.size" + photoname_List.size());
            }

            @Override
            public void uploadFailed(PhotoAudio photoAudio) {
                //上传失败,添加到图片补传集合
                Constants.list_BC_Picture.add(audio);
                LogUtil.i(TAG, "图片补传失败:" + audio.getCmScFileName() + "== account_up" + account_up + "  Constants.list_BC_Picture.size" + Constants.list_BC_Picture.size());
            }

            @Override
            public void uploadFinish() {

            }
        });
    }


    // 更新以上传图片状态
    public void changeState_Photo(final ArrayList<String> photoname_List2)
            throws SQLException {
        new Thread(new Runnable() {// ThreadUtils.getSingleThreadExecutor().submit
            @Override
            public void run() {
//                Connection conne = null;
                try {
//                    conne = SQLiteData.openOrCreateDatabase();
//                    conne.setAutoCommit(false);
                    if(allConnection==null){
                        allConnection = new DatabaseHelper(context);
                    }
                    SQLiteDatabase writableDatabase = allConnection.getWritableDatabase();

                    if(photoname_List2.size() > 0){
                        for (int a = 0; a < photoname_List2.size(); a++){
//                            writableDatabase.
                            String sqlString = "update  perFile_aj set cmMrState = 'Y' where cmScFileName = " + "'"+photoname_List2.get(a)  + "'";
                            writableDatabase.execSQL(sqlString);
                        }

                    }
//                    if (photoname_List2.size() > 0) {
//                        for (int a = 0; a < photoname_List2.size(); a++) {
//                            ps.setString(1, photoname_List2.get(a));
//                            ps.addBatch();
//                        }
//                    }
//                    ps.executeBatch();
                    if(writableDatabase!=null){
                        writableDatabase.close();
                    }

//                    conne.commit();
                    photoname_List.clear();
                    photoname_List = null;
//                    if (allConnection!=null){
//
//                    }

                    if (allConnection != null) {
                        try {
                            allConnection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

//                    conne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    // 获取对应任务下的图片
    protected ArrayList<PhotoAudio> getSched_Picture(String userName_login,
                                                     String cmSchedId) {

        String sql = "select p.cmScFileName,p.cmScFileRoute,p.cmScdate from perFile_aj as p left join schedInfo_aj  as s where s.userID = '"
                + userName_login
                + "' and  p.cmSchedId= '"
                + cmSchedId
                + "' and p.cmMrState = 'N'"
                + " and p.cmSchedId=s.cmSchedId";
        // 存放上传的图片
        ArrayList<PhotoAudio> photos = new ArrayList<PhotoAudio>();
        try {
//            Connection conne = SQLiteData.openOrCreateDatabase();
            SQLiteDatabase writableDatabase = allConnection.getWritableDatabase();
            Cursor resultSet = writableDatabase.rawQuery(sql,null);

            PhotoAudio photoAudio = null;
            if(resultSet.moveToFirst()){
                for (int i = 0; i < resultSet.getCount(); i++) {
                    addPhotoAudio(photos, resultSet);
                resultSet.moveToNext();
                }
//                addPhotoAudio(photos, resultSet);

//                while (resultSet.moveToNext()) {
//                    addPhotoAudio(photos, resultSet);
//                }
            }

            if(resultSet!=null){
                resultSet.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;

    }

    private void addPhotoAudio(ArrayList<PhotoAudio> photos, Cursor resultSet) {
        PhotoAudio photoAudio;
        photoAudio = new PhotoAudio();
        photoAudio.setCmScFileName(resultSet.getString(resultSet.getColumnIndex("cmScFileName")));
        photoAudio.setCmScFileRoute(resultSet
                .getString(resultSet.getColumnIndex("cmScFileRoute")));
        photoAudio.setCmScdate(resultSet.getString(resultSet.getColumnIndex("cmScdate")));
        photos.add(photoAudio);
    }


}
