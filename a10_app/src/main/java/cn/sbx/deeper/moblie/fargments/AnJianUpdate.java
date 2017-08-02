package cn.sbx.deeper.moblie.fargments;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.sunboxsoft.monitor.utils.PerfUtils;
import com.sunboxsoft.monitor.utils.UploadDate;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.SchedInfoResidents;
import cn.sbx.deeper.moblie.view.ListViewForScorllView;
import petrochina.ghzy.a10fieldwork.R;

public class AnJianUpdate implements OnItemClickListener {

    private Context context;
    private View view;
    private ArrayList<SchedInfoResidents> schedInfos_list;
    private String userName;
    private UploadDate uploadDate;
    private ArrayList<SchedInfoResidents> schedInfos;

    public AnJianUpdate(Context context) {
        this.context = context;

        view = View.inflate(context, R.layout.item_anjain_uploaddate_anjian,
                null);
        ListViewForScorllView list_AJ = (ListViewForScorllView) view.findViewById(R.id.list_AJ);

        userName = Constants.loginName;
        schedInfos_list = selectSchedInfos(userName);
        list_AJ.setOnItemClickListener(this);
        list_AJ.setAdapter(new Adapter_UploadDateCB(schedInfos_list));
    }

    // 获取给模块view
    public View getView() {
        if (schedInfos_list.size() > 0) {
            return view;
        } else {
            return null;
        }
    }

    class Adapter_UploadDateCB extends BaseAdapter {

        private ArrayList<SchedInfoResidents> schedInfos_list;

        public Adapter_UploadDateCB(
                ArrayList<SchedInfoResidents> schedInfos_list) {
            this.schedInfos_list = schedInfos_list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return schedInfos_list.size();
        }

        @Override
        public SchedInfoResidents getItem(int position) {
            return schedInfos_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 判断是否有任务

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context,
                        R.layout.item_approve_updatelist, null);

                holder.tv_schedinfo_name = (TextView) convertView
                        .findViewById(R.id.tv_schedinfo_name);
                holder.cb_box = (CheckBox) convertView
                        .findViewById(R.id.cb_box);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置显示内容
            SchedInfoResidents schedInfo = getItem(position);
            holder.tv_schedinfo_name.setText("安检计划描述 : "
                    + schedInfo.getDescription() + "\r\n"
                    + "可上传任务量 : "
                    + schedInfo.getAccount()
                    + "\r\n"
                    + "计划安检时间 : "
                    // + "\r\n"
                    + schedInfo.getScheduleDateTimeStart().substring(0, 10)
                    .toString());
            holder.tv_schedinfo_name.setTextColor(Color.BLACK);
            holder.cb_box.setChecked(schedInfo.isCheck);
            return convertView;
        }

    }

    class ViewHolder {
        TextView tv_schedinfo_name;
        CheckBox cb_box;
    }

    private ArrayList<SchedInfoResidents> selectSchedInfos(String user_nameLogin) {
        // String userName_login = PerfUtils.getString(mActivity, "userName",
        // "");
        // 获取设置的清除时间
        String set_time = PerfUtils.getString(context, "clearTime", "");
        int setTime = 0;
        if (set_time.equals("一   周")) {
            setTime = 7;
        } else if (set_time.equals("二   周")) {
            setTime = 14;
        } else if (set_time.equals("三   周")) {
            setTime = 21;
        } else if (set_time.equals("一   月")) {
            setTime = 30;
        }

        // 获取系统当前时间
        long new_timeMillis = System.currentTimeMillis();

        String sql_selectSchedInfos = "select * from schedInfo_aj where userID = "
                + "'" + user_nameLogin + "'" + "";
        // Cursor cursor_schedInfos = db.rawQuery(sql_selectSchedInfos, null);
        ArrayList<SchedInfoResidents> schedInfos_List = new ArrayList<SchedInfoResidents>();
        DatabaseHelper db = null;
        SQLiteDatabase state= null;

        try {
            db = new DatabaseHelper(context);

            state = db.getWritableDatabase();


            Cursor cursor_schedInfos = state
                    .rawQuery(sql_selectSchedInfos,null);
            SchedInfoResidents schedInfo;


            if(cursor_schedInfos.moveToFirst()){
                comment_select(setTime, new_timeMillis, schedInfos_List, state, cursor_schedInfos);
                while (cursor_schedInfos.moveToNext()) {
                    comment_select(setTime, new_timeMillis, schedInfos_List, state, cursor_schedInfos);
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
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return schedInfos_List;

    }

    private void comment_select(int setTime, long new_timeMillis, ArrayList<SchedInfoResidents> schedInfos_List, SQLiteDatabase state, Cursor cursor_schedInfos) {
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

        // 获取当前任务可上传的数量

        String sql_account = "select distinct * from custInfo_ju_aj where cmSchedId = '"
                + cursor_schedInfos.getString(cursor_schedInfos.getColumnIndex("cmSchedId"))
                + "' and cmMrState = '3'";

        Cursor executeQuery = state.rawQuery(sql_account,null);
        executeQuery.moveToLast();

        schedInfo.setAccount(executeQuery.getCount()); // 当前任务下的可上传客户数量
        executeQuery.close();
        // stat.close();

        long old_time = Long.parseLong(cursor_schedInfos
                .getString(cursor_schedInfos.getColumnIndex("cmMrDate")));
        // 判断该任务的上传日期是否超过设置的期限
        if ((new_timeMillis - old_time >= setTime * 24 * 60 * 60 * 1000)
                && setTime != 0) {
            // 该任务已上传超过设置期限,清除该任务
            boolean isclear = delete(schedInfo);
            if (!isclear) {
                // 该任务列中还有用户未上传,需要显示该任务
                schedInfos_List.add(schedInfo);
            }
        } else {
            // 否则显示在任务列表
            schedInfos_List.add(schedInfo);
        }
    }

    private boolean delete(SchedInfoResidents schedInfo) {
        // 标记该任务下的用户是否全部被删除
        boolean isClearAll = false;
        DatabaseHelper db = null;
        SQLiteDatabase state = null;
        try {
            // 存放需要删除的用户
            ArrayList<CustInfo_AnJian> cuInfos_List = new ArrayList<CustInfo_AnJian>();
            db = new DatabaseHelper(context);

            state = db.getWritableDatabase();
            // 删除该任务下的所有数据
            String delete_schedInfo = "delete from schedInfo_aj where cmSchedId = "
                    + "'"
                    + schedInfo.getCmSchedId()
                    + "'"
                    + " and  description = "
                    + "'"
                    + schedInfo.getDescription()
                    + "'"
                    + " and  cmScTypeCd = "
                    + "'"
                    + schedInfo.getSpType()
                    + "'";
            // 查询该任务下的客户,以及上传状态
            String selectAccount = "select accountId,cmMrState from custInfo_ju_aj where cmSchedId = "
                    + "'" + schedInfo.getCmSchedId() + "'";

            Cursor accounts = state.rawQuery(selectAccount,null);
            // Cursor accounts = db.rawQuery(selectAccount, null);
            CustInfo_AnJian cuInfo = null;
            if(accounts.moveToFirst()){
                cuInfo = new CustInfo_AnJian();
                cuInfo.setAccountId(accounts.getString(0));
                cuInfo.setCmMrState(accounts.getString(1));
                cuInfos_List.add(cuInfo);
                while (accounts.moveToNext()) {
                    cuInfo = new CustInfo_AnJian();
                    cuInfo.setAccountId(accounts.getString(0));
                    cuInfo.setCmMrState(accounts.getString(1));
                    cuInfos_List.add(cuInfo);
                }
            }

            // 删除该任务下的客户,电话 ,上传表
            if (cuInfos_List.size() > 0) {
                for (int a = 0; a < cuInfos_List.size(); a++) {
                    // 如果该用户已经上传过,则清除该用户
                    if (cuInfos_List.get(a).getCmMrState().equals("2")) {
                        String delete_account = "delete from custInfo_ju_aj where accountId ="
                                + "'"
                                + cuInfos_List.get(a).getAccountId()
                                + "'";
                        state.execSQL(delete_account);
                        // db.execSQL(delete_account);
                        // 删除该用户的电话,
                        String delete_phone = "delete from perPhone where accountId = "
                                + "'"
                                + cuInfos_List.get(a).getAccountId()
                                + "'";
                        state.execSQL(delete_phone);
                        // db.execSQL(delete_phone);
                        // 删除该用户的上传表,
                        String delete_upload = "delete from uploadcustInfo_aj where cmSchedId = "
                                + "'"
                                + schedInfo.getCmSchedId()
                                + "' and accountId = "
                                + cuInfos_List.get(a).getAccountId() + "";
                        state.execSQL(delete_upload);
                        // db.execSQL(delete_upload);
                        // 从集合中移除该用户,以判断是否删除完毕
                        cuInfos_List.remove(a);
                    }
                }
                // 判断是否删除完毕
                if (cuInfos_List.size() == 0) {
                    // 将;任务删除
                    state.execSQL(delete_schedInfo);
                    // db.execSQL(delete_schedInfo);
                    isClearAll = true;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return isClearAll;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        // 点击条目 切换选中框的状态,

        SchedInfoResidents schedInfo = schedInfos_list.get(position);

        if (schedInfo != null) {
            // 业务逻辑选中状态的切换
            schedInfo.isCheck = !schedInfo.isCheck;

            // UI做选中状态的切换
            CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
            // 修改checkBox当前状态
            cb_box.setChecked(schedInfo.isCheck);

        }
    }

    // 安检数据上传
    public void uploadAnJian(TextView tv_des2, TextView tv_des3) {


        uploadDate = new UploadDate(context, tv_des3);
//        uploadDate.testForUploadImg();

        schedInfos = new ArrayList<SchedInfoResidents>();
//服务器进行压力测试的时候使用
//        for (int i = 0; i < Constants.testForUpDataNum; i++) {
            for (SchedInfoResidents schedInfo : schedInfos_list) { // 遍历所有任务
                if (schedInfo.isCheck()) {
                    // 添加到上传集合中
                    schedInfos.add(schedInfo); // 将选中的任务添加到上传集合中
                }
            }
//        }
        if (schedInfos.size() > 0) {
            uploadDate.upData_AJ(context, userName, schedInfos, tv_des2,
                    tv_des3);
        } else {
            // 标记安检未选择,
            Constants.anJianSched_NO = true;
        }
    }

    // 安检图片补传
    public void upload_Photo_Buchuan(TextView tv_schedinfo, TextView tv_des3) {

        if (uploadDate == null) {
            uploadDate = new UploadDate(context, tv_des3);
        }
        if (Constants.list_BC_Picture.size() > 0) {
            uploadDate.uploadpicture_Anjian_Buchuan(context, Constants.list_BC_Picture,
                    userName, tv_des3);
        } else {
            tv_schedinfo.setText("上传结果 :");
            tv_des3.setText("没有要上传的图片.");
            tv_des3.setVisibility(View.VISIBLE);
            Toast.makeText(context, "没有要上传的图片.", Toast.LENGTH_SHORT).show();
        }
    }

}
