package cn.sbx.deeper.moblie.fargments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.sunboxsoft.monitor.utils.PerfUtils;
import com.sunboxsoft.monitor.utils.UploadDate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CustInfo;
import cn.sbx.deeper.moblie.domian.SchedInfo;
import petrochina.ghzy.a10fieldwork.R;

public class ChaoBiaoUpdate {

	private Context context;
	private View view;
	private ListView list_CB;

	private ArrayList<SchedInfo> schedInfos_list;

	public ChaoBiaoUpdate(Context context) {
		this.context = context;

		schedInfos_list = selectSchedInfos(Constants.loginName);

		view = View.inflate(context, R.layout.item_anjain_uploaddate_chaobiao,
				null);
		list_CB = (ListView) view.findViewById(R.id.list_CB);
		list_CB.setAdapter(new Adapter_CB(schedInfos_list));
		list_CB.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 点击条目 切换选中框的状态,

				SchedInfo schedInfo = schedInfos_list.get(position);

				if (schedInfo != null) {
					// 业务逻辑选中状态的切换
					schedInfo.isCheck = !schedInfo.isCheck;

					// UI做选中状态的切换
					CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
					// 修改checkBox当前状态
					cb_box.setChecked(schedInfo.isCheck);

				}
			}
		});
	}

	public class Adapter_CB extends BaseAdapter {

		private ArrayList<SchedInfo> schedInfos_list;

		public Adapter_CB(ArrayList<SchedInfo> schedInfos_list) {
			this.schedInfos_list = schedInfos_list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return schedInfos_list.size();
		}

		@Override
		public SchedInfo getItem(int position) {
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
			SchedInfo schedInfo = getItem(position);
			holder.tv_schedinfo_name.setText("抄表路径描述 : " 
					+ schedInfo.getCmMrRteDescr() + "\r\n" + "可上传任务量 : "
					+ schedInfo.getAccount() + "\r\n" + "计划抄表日期 : "
					+ schedInfo.getScheduledSelectionDate());
			holder.cb_box.setChecked(schedInfo.isCheck);

			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_schedinfo_name;
		CheckBox cb_box;
	}

	// 提供获取试图方法
	public View getView() {
		if(schedInfos_list.size()>0){
			return view;
		}else{
			return null;
		}

	}

	private ArrayList<SchedInfo> selectSchedInfos(String user_nameLogin) {
		String userName_login = PerfUtils.getString(context, "userName", "");
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

		String sql_selectSchedInfos = "select rowId,cmMrRteDescr,meterReadCycle,scheduledSelectionDate,cmMrDate,meterReadRoute from schedInfo where userID = "
				+ "'" + user_nameLogin + "'" + "";
		// Cursor cursor_schedInfos = db.rawQuery(sql_selectSchedInfos, null);
		ArrayList<SchedInfo> schedInfos_List = new ArrayList<SchedInfo>();
		Connection db = null;
		Statement state = null;
		Statement stat = null;
		try {
			db = SQLiteData.openOrCreateDatabase(context);
			state = db.createStatement();
			stat = db.createStatement();
			ResultSet cursor_schedInfos = state
					.executeQuery(sql_selectSchedInfos);
			SchedInfo schedInfo;
			while (cursor_schedInfos.next()) {
				schedInfo = new SchedInfo();
				schedInfo.setRowId(cursor_schedInfos.getString(1));
				schedInfo.setCmMrRteDescr(cursor_schedInfos.getString(2));
				schedInfo.setMeterReadCycle(cursor_schedInfos.getString(3));
				schedInfo.setScheduledSelectionDate(cursor_schedInfos
						.getString(4));
				schedInfo.setCmMrDate(cursor_schedInfos.getString(5));
				schedInfo.setMeterReadRoute(cursor_schedInfos
						.getString("meterReadRoute"));

				// 获取当前任务可上传的数量
				String sql_account = "select distinct * from custInfo where schedInfoID = '"
						+ cursor_schedInfos.getString(1)
						+ "' and cmMrState = '3'";

				ResultSet executeQuery = stat.executeQuery(sql_account);
				executeQuery.last();

				schedInfo.setAccount(executeQuery.getRow()); // 当前任务下的可上传客户数量
				executeQuery.close();
				// stat.close();
				long old_time = Long.parseLong(cursor_schedInfos.getString(5));
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

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return schedInfos_List;

	}

	private boolean delete(SchedInfo schedInfo) {
		// 标记该任务下的用户是否全部被删除
		boolean isClearAll = false;
		Connection db = null;
		Statement state = null;
		try {
			// 存放需要删除的用户
			ArrayList<CustInfo> cuInfos_List = new ArrayList<CustInfo>();
			db = SQLiteData.openOrCreateDatabase(context);
			state = db.createStatement();
			// 删除该任务下的所有数据
			String delete_schedInfo = "delete from schedInfo where meterReadRoute = "
					+ "'"
					+ schedInfo.getMeterReadRoute()
					+ "'"
					+ " and  meterReadCycle = "
					+ "'"
					+ schedInfo.getMeterReadCycle()
					+ "'"
					+ " and  scheduledSelectionDate = "
					+ "'"
					+ schedInfo.getScheduledSelectionDate() + "'";
			// 查询该任务下的客户,以及上传状态
			String selectAccount = "select accountId,spMeterHistoryId,cmMrState from custInfo where schedInfoID = "
					+ "'" + schedInfo.getRowId() + "'";
			ResultSet accounts = state.executeQuery(selectAccount);
			// Cursor accounts = db.rawQuery(selectAccount, null);
			CustInfo cuInfo = null;
			while (accounts.next()) {
				cuInfo = new CustInfo();
				cuInfo.setAccountId(accounts.getString(1));
				cuInfo.setSpMeterHistoryId(accounts.getString(2));
				cuInfo.setCmMrState(accounts.getString(3));
				cuInfos_List.add(cuInfo);
			}
			// 删除该任务下的客户,电话 ,上传表
			if (cuInfos_List.size() > 0) {
				for (int a = 0; a < cuInfos_List.size(); a++) {
					// 如果该用户已经上传过,则清除该用户
					if (cuInfos_List.get(a).getCmMrState().equals("2")) {
						String delete_account = "delete from custInfo where accountId ="
								+ "'"
								+ cuInfos_List.get(a).getAccountId()
								+ "'";
						state.executeUpdate(delete_account);
						// db.execSQL(delete_account);
						// 删除该用户的电话,
						String delete_phone = "delete from perPhone where accountId = "
								+ "'"
								+ cuInfos_List.get(a).getAccountId()
								+ "'";
						state.executeUpdate(delete_phone);
						// db.execSQL(delete_phone);
						// 删除该用户的上传表,
						String delete_upload = "delete from uploadcustInfo where spMeterHistoryId = "
								+ "'"
								+ cuInfos_List.get(a).getSpMeterHistoryId()
								+ "'";
						state.executeUpdate(delete_upload);
						// db.execSQL(delete_upload);
						// 从集合中移除该用户,以判断是否删除完毕
						cuInfos_List.remove(a);
					}
				}
				// 判断是否删除完毕
				if (cuInfos_List.size() == 0) {
					// 将;任务删除
					state.executeUpdate(delete_schedInfo);
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

	// 抄表数据上传
	public void uploadCB(TextView tv_des1,TextView tv_des3) {
		UploadDate uploadDate = new UploadDate(context,tv_des1);

		ArrayList<SchedInfo> schedInfos = new ArrayList<SchedInfo>();
		for (SchedInfo schedInfo : schedInfos_list) {
			if (schedInfo.isCheck) {
				schedInfos.add(schedInfo);
			}
		}
		if (schedInfos.size() > 0) {
			uploadDate.upload_CB(schedInfos, context, Constants.loginName,tv_des1);
//			标记抄表有选择上传任务
			Constants.chaoBiaoSched_Yes=true;
		}
	}
}
