package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunboxsoft.monitor.utils.PerfUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.OutPut;
import cn.sbx.deeper.moblie.domian.SchedInfoResidents;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

@SuppressLint("ValidFragment")
public class AnJianUpdateSchedInfosFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener {
	private String tag = "PollingUpdateSchedInfosFragment";
	private IApproveBackToList backToList;
	private int targetContainer;
	private String user_nameLogin;
	private Connection db;
	private ArrayList<SchedInfoResidents> schedInfos_list;
	private Button button_update, btn_selectall, bt_left1;
	private MyAdapter myAdapter;
	private ProgressHUD overlayProgress = null;
	private boolean isCheckAll = false;
	public static final int SUCCESS1 = 10;
	private ArrayList<SchedInfoResidents> update_schedInfo = new ArrayList<SchedInfoResidents>();// 存放已上传的任务
	// 存放已上传的用户的spMeterHistoryId
	private ArrayList<String> accountIdList = new ArrayList<String>();
	private Statement state;

	private Handler mHandle = new Handler() {

		public void handleMessage(Message msg) {
			if (overlayProgress != null) {
				overlayProgress.dismiss();
			}
			switch (msg.what) {
			case 10:
				String str = (String) msg.obj;
				System.out.println("=====str============"+str);
				// 解析返回值
				List<OutPut> list = getResult(str);
				if (list != null && list.size() > 0 && list.get(0) != null) {

					Toast.makeText(mActivity, list.get(0).getRespDescr(), Toast.LENGTH_LONG)
							.show();
					// 获取系统当前时间
					long timeMillis = System.currentTimeMillis();

					Log.i(tag, timeMillis + ":上传结束===========");
					Log.i(tag, (timeMillis - update_start) / 1000
							+ ":上传耗时===========");
					// 上传成功后 修改用户表 上传表状态,以及上传时间
					if (accountIdList.size() > 0) {
						try {
							db = SQLiteData.openOrCreateDatabase(getActivity());
							state = db.createStatement();
							for (String item : accountIdList) {
								// 更改用户信息表状态
								String sql_updateCustInfo = "update custInfo_ju_aj set cmMrState = "
										+ "'"
										+ 2
										+ "'"
										+ " where  accountId= '"
										+ item
										+ "'";
								// 插入上传时间,首先判断该用户是否已上传过,是更新时间还是插入时间?
								// 更新时间
								String sql_insert = "update custInfo_ju_aj set cmMrDate = "
										+ "'"
										+ timeMillis
										+ "'"
										+ " where  accountId='"
										+ item
										+ "'";
								state.executeUpdate(sql_insert);
								state.executeUpdate(sql_updateCustInfo);

								// 修改上传表中状态
								String sql_upload = "update uploadcustInfo_aj set cmMrState = "
										+ "'"
										+ 2
										+ "'"
										+ " where  accountId='"
										+ item
										+ "'";

								String sql_insert_time = "update uploadcustInfo_aj set cmMrDate = "
										+ "'"
										+ timeMillis
										+ "'"
										+ " where  accountId='"
										+ item
										+ "'";

								state.executeUpdate(sql_insert_time);
								state.executeUpdate(sql_upload);
							}
							// 将上传的任务插入时间
							if (update_schedInfo.size() > 0) {
								for (SchedInfoResidents schedInfo : update_schedInfo) {

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
											+ "'" + schedInfo
											 .getScheduleDateTimeStart()
											+ "'";
									state.executeUpdate(sql_schedInfo_time);
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
							if (db != null) {
								try {
									db.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}

							}
						}
					}
					backPrecious();
				
				} else {
					if (overlayProgress != null) {
						overlayProgress.dismiss();
					}
					Toast.makeText(mActivity, "上传失败", Toast.LENGTH_LONG).show();
				}

				break;

			default:
				break;
			}

		};
	};
	private long update_start;
	private Statement state2;
	private Statement state3;
	private Statement state4;
	private Statement state5;
	private Statement state6;
	private Statement stat;
	private View view;

	public AnJianUpdateSchedInfosFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public AnJianUpdateSchedInfosFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		if (bundle != null) {
			user_nameLogin = bundle.getString("user_name");
		}
		btn_selectall = (Button) getActivity().findViewById(R.id.btn_next);
		btn_selectall.setText("全选");
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("上传任务列表");
		// 查询该用户的所有任务
		String userName = Constants.loginName;
		schedInfos_list = selectSchedInfos(userName);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ListView list_schedInfos;
		TextView text_view;
		view = View.inflate(mActivity,
				R.layout.layout_approve_updatschedinfos_list, null);

		list_schedInfos = (ListView) view.findViewById(R.id.list_schedInfos);
		text_view = (TextView) view.findViewById(R.id.text_view);
		button_update = (Button) view.findViewById(R.id.button_update);
//		btn_selectall = (Button) view.findViewById(R.id.btn_selectall);
//		bt_left1 = (Button) view.findViewById(R.id.bt_left1);
//		bt_left1.setOnClickListener(this);

		btn_selectall.setOnClickListener(this);
		button_update.setOnClickListener(this);
		if (schedInfos_list.size() > 0 && schedInfos_list != null) {
			myAdapter = new MyAdapter();
			list_schedInfos.setAdapter(myAdapter);
			list_schedInfos.setOnItemClickListener(this);

		} else {
			// 没有上传任务 隐藏listview
			text_view.setVisibility(View.VISIBLE);
			list_schedInfos.setVisibility(View.GONE);
			button_update.setVisibility(View.GONE);
			btn_selectall.setVisibility(View.GONE);

		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public class MyAdapter extends BaseAdapter {

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
				convertView = View.inflate(mActivity,
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
					+ schedInfo.getDescription()
					+ "\r\n"
					+ "可上传任务量 : "
					+ schedInfo.getAccount()
					+ "\r\n"
					+ "计划安检时间 : "
//					+ "\r\n"
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
		String set_time = PerfUtils.getString(mActivity, "clearTime", "");
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
		try {
			db = SQLiteData.openOrCreateDatabase(getActivity());
			state = db.createStatement();
			stat = db.createStatement();
			
			ResultSet cursor_schedInfos = state
					.executeQuery(sql_selectSchedInfos);
			SchedInfoResidents schedInfo;
			while (cursor_schedInfos.next()) {
				schedInfo = new SchedInfoResidents();
				schedInfo
						.setCmSchedId(cursor_schedInfos.getString("cmSchedId"));
				schedInfo.setDescription(cursor_schedInfos
						.getString("description"));
				schedInfo.setCmScTypeCd(cursor_schedInfos
						.getString("cmScTypeCd"));
				schedInfo.setSpType(cursor_schedInfos.getString("spType"));
				schedInfo.setScheduleDateTimeStart(cursor_schedInfos
						.getString("scheduleDateTimeStart"));
				schedInfo.setCmMrDate(cursor_schedInfos.getString("cmMrDate"));
				
//				获取当前任务可上传的数量
				
				String  sql_account = "select distinct * from custInfo_ju_aj where cmSchedId = '"+cursor_schedInfos.getString("cmSchedId")+"' and cmMrState = '3'";
				
				ResultSet executeQuery = stat.executeQuery(sql_account);
				executeQuery.last();
				
				schedInfo.setAccount(executeQuery.getRow()); //当前任务下的可上传客户数量
				executeQuery.close();
//				stat.close();
				
				long old_time = Long.parseLong(cursor_schedInfos
						.getString("cmMrDate"));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stat != null) {
				try {
					stat.close();
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

	private boolean delete(SchedInfoResidents schedInfo) {
		// 标记该任务下的用户是否全部被删除
		boolean isClearAll = false;
		try {
			// 存放需要删除的用户
			ArrayList<CustInfo_AnJian> cuInfos_List = new ArrayList<CustInfo_AnJian>();
			db = SQLiteData.openOrCreateDatabase(getActivity());
			state = db.createStatement();
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
			ResultSet accounts = state.executeQuery(selectAccount);
			// Cursor accounts = db.rawQuery(selectAccount, null);
			CustInfo_AnJian cuInfo = null;
			while (accounts.next()) {
				cuInfo = new CustInfo_AnJian();
				cuInfo.setAccountId(accounts.getString(1));
				cuInfo.setCmMrState(accounts.getString(2));
				cuInfos_List.add(cuInfo);
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
						String delete_upload = "delete from uploadcustInfo_aj where cmSchedId = "
								+ "'"
								+ schedInfo.getCmSchedId()
								+ "' and accountId = "
								+ cuInfos_List.get(a).getAccountId() + "";
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

	@SuppressLint("LongLogTag")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_next:

			if (!isCheckAll) {
				// 第一次单击全选
				for (SchedInfoResidents schedInfo : schedInfos_list) {

					schedInfo.isCheck = true;
				}
				isCheckAll = !isCheckAll;
				// 所有的条目已经选中,刷新数据适配器
				myAdapter.notifyDataSetChanged();
				btn_selectall.setText("取消");
				Toast.makeText(mActivity, "再次单击取消选中", Toast.LENGTH_LONG).show();
			} else {
				// 第二次单击全选 全部取消选中
				for (SchedInfoResidents schedInfo : schedInfos_list) {

					schedInfo.isCheck = false;
				}
				isCheckAll = !isCheckAll;
				// 所有的条目已经选中,刷新数据适配器
				myAdapter.notifyDataSetChanged();
				btn_selectall.setText("全选");
			}

			break;
		case R.id.button_update:

			try {
				db = SQLiteData.openOrCreateDatabase(getActivity());
				state = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				update_start = System.currentTimeMillis();
				Log.i(tag, update_start + ":开始上传===========");

				
				// 标记 是否有要上传的用户 ,防止上传空得任务
				boolean isEmpty = false;
				// 存放任务数据
				String schedInfo = "";
				for (SchedInfoResidents schedInfos : schedInfos_list) {
					if (schedInfos.isCheck) {
						// 将上传的任务添加到集合,以插入上传时间
						update_schedInfo.add(schedInfos);
						// // 查询当前查询的任务
						// while (schedInfos.moveToNext()) {
						// 存放所有任务
						String strDate = "";
						String cmSchedId = schedInfos.getCmSchedId();
						String cmScTypeCd = schedInfos.getCmScTypeCd();
						String spType = schedInfos.getSpType();

						// 创建 客户上传JSON数据
						String userInfo = "";

						// 查询客户
						String cust_his = "select accountId, fieldActivityId  ,servicePointId,badgeNumber,meterConfigurationId from custInfo_ju_aj where cmSchedId = '"
//								+ cmSchedId + "' and cmMrState in ('3','2') ";
						+ cmSchedId + "' and cmMrState = '3' ";
						state = db.createStatement();
						ResultSet execute_set = state.executeQuery(cust_his);

						execute_set.last();
						  int row = execute_set.getRow();
						  execute_set.beforeFirst();
//						while (execute_set.next()) {
							for(int a =0;a<row;a++){
								execute_set.next();
							String accountId = execute_set.getString(1);
							String fieldActivityId = execute_set.getString(2);
							String servicePointId = execute_set.getString(3);
							String badgeNumber = execute_set.getString(4);
							String meterConfigurationId = execute_set.getString(5);
							state2 = db.createStatement();

							String up_sched = "select distinct * from uploadcustInfo_aj where cmSchedId = '"
									+ cmSchedId
									+ "' and accountId = '"
									+ accountId + "'";
							ResultSet uploadcust = state2
									.executeQuery(up_sched);
							while (uploadcust.next()) {
								// 将上传的Id存到数组
								accountIdList.add(accountId);
								String time = "";
								if (uploadcust.getString(2) != null) {

									time = uploadcust.getString("cmScDttm")
											.replace(" ", "-")
											.replace(":", ".");
								}
								// for (int a = 0; a < 600; a++) {
//								如果安检入户情况不是正常入户, 只提交入户类型,其他信息不需提价
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
										+ "\"cmScDttm\":\""
										+ time
										+ "\",";
								if(!uploadcust.getString("cmScAjrh").equals("ZCRH")){  //安检入户类型 不为正常入户,其他信息不提交
									str1  +="\"cmScAjrh\":\""    
											+ uploadcust.getString("cmScAjrh")
											+ "\",";
								}else{
									if(!TextUtils.isEmpty(uploadcust.getString("cmScAjrh"))){
										str1+="\"cmScAjrh\":\""    
												+ uploadcust.getString("cmScAjrh")
												+ "\",";
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScYhzg"))){
										str1+="\"cmScYhzg\":\""    
												+ uploadcust.getString("cmScYhzg")
												+ "\",";
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScZgtzd"))){
										str1+="\"cmScZgtzd\":\""    
												+ uploadcust.getString("cmScZgtzd")
												+ "\",";
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScZtjs"))){
										str1+="\"cmScZtjs\":\""    
												+ uploadcust.getString("cmScZtjs")
												+ "\",";
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmMr"))){
										str1+="\"cmMr\":\""    
												+ uploadcust.getString("cmMr")
												+ "\",";
										str1+="\"readType\":\"" +60+ "\",";  //读数类型默认为   普通
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScSyql"))){
										str1+="\"cmScSyql\":\""    
												+ uploadcust.getString("cmScSyql")
												+ "\",";
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmMrCommCd"))){
										str1+="\"cmMrCommCd\":\""    
												+ uploadcust.getString("cmMrCommCd")
												+ "\",";
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScRemark"))){
										str1+="\"cmScRemark\":\""    
												+ uploadcust.getString("cmScRemark")
												+ "\",";
									}
									
									if(!TextUtils.isEmpty(uploadcust.getString("cmScUserType"))){
										str1+="\"cmScUserType\":\""    
												+ uploadcust.getString("cmScUserType")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScResType"))){
										str1+="\"cmScResType\":\""    
												+ uploadcust.getString("cmScResType")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("meterType"))){
										str1+="\"meterType\":\""    
									+ uploadcust.getString("meterType")
									+ "\",";//设备信息
									}
									
									if(!TextUtils.isEmpty(uploadcust.getString("manufacturer"))){
										str1+="\"manufacturer\":\""    
												+ uploadcust.getString("manufacturer")
												+ "\",";//设备信息
									}
									
									if(!TextUtils.isEmpty(uploadcust.getString("model"))){
										str1+="\"model\":\""    
												+ uploadcust.getString("model")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("serialNumber"))){
										str1+="\"serialNumber\":\""    
												+ uploadcust.getString("serialNumber")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmMrMtrBarCode"))){
										str1+="\"cmMrMtrBarCode\":\""    
												+ uploadcust.getString("cmMrMtrBarCode")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmMlr"))){
										str1+="\"cmMlr\":\""    
												+ uploadcust.getString("cmMlr")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScLgfmGj"))){
										str1+="\"cmScLgfmGj\":\""    
												+ uploadcust.getString("cmScLgfmGj")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScLgfmWz"))){
										str1+="\"cmScLgfmWz\":\""    
												+ uploadcust.getString("cmScLgfmWz")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScLgfmCz"))){
										str1+="\"cmScLgfmCz\":\""    
												+ uploadcust.getString("cmScLgfmCz")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScZjPp"))){
										str1+="\"cmScZjPp\":\""    
												+ uploadcust.getString("cmScZjPp")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScZjYs"))){
										str1+="\"cmScZjYs\":\""    
												+ uploadcust.getString("cmScZjYs")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScZjXhbh"))){
										str1+="\"cmScZjXhbh\":\""    
												+ uploadcust.getString("cmScZjXhbh")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScZjSyrq"))){
										str1+="\"cmScZjSyrq\":\""    
												+ uploadcust.getString("cmScZjSyrq")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScLjgCz"))){
										str1+="\"cmScLjgCz\":\""    
												+ uploadcust.getString("cmScLjgCz")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScCnlPp"))){
										str1+="\"cmScCnlPp\":\""    
												+ uploadcust.getString("cmScCnlPp")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScCnlPffs"))){
										str1+="\"cmScCnlPffs\":\""    
												+ uploadcust.getString("cmScCnlPffs")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScCnlSyrq"))){
										str1+="\"cmScCnlSyrq\":\""    
												+ uploadcust.getString("cmScCnlSyrq")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScRsqPp"))){
										str1+="\"cmScRsqPp\":\""    
												+ uploadcust.getString("cmScRsqPp")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScRsqPffs"))){
										str1+="\"cmScRsqPffs\":\""    
												+ uploadcust.getString("cmScRsqPffs")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScRsqSyrq"))){
										str1+="\"cmScRsqSyrq\":\""    
												+ uploadcust.getString("cmScRsqSyrq")
												+ "\",";//设备信息
									}
									if(!TextUtils.isEmpty(uploadcust.getString("cmScBjqSyrq"))){
										str1+="\"cmScBjqSyrq\":\""    
												+ uploadcust.getString("cmScBjqSyrq")
												+ "\",";//设备信息
									}
								}
								
								str1+="\"perPhone\":[ "
												+ "$phones$"
												+ " ],"
												+ "\"perSh\":[ "
												+ "$perSh$"
												+ " ],"
												+ "\"perFile\":[ "
												+ "$perFile$"
												+ " ],"
												+ "\"perSp\":[ "
												+ "$perSp$" + " ]}";
								
								String str = String.format(str1);
								
								// 电话
								String sql_perPhone = "select phoneType,sequence,phone,extension,cmPhoneOprtType  from  perPhone where  cmPhoneOprtType in('10','20','30') and accountId ="
										+ "'" + accountId + "'";

								state3 = db.createStatement();
								ResultSet perPhone_cursor = state3
										.executeQuery(sql_perPhone);
								// 创建变量
								String phones = "";
								// 遍历电话
								while (perPhone_cursor.next()) {
									// 手机号码有修改
									String p = "{\"#comment\":[],\"phoneType\":\""
											+ perPhone_cursor.getString(1)
											+ "\",\"sequence\":\""
											+ perPhone_cursor.getString(2)
											+ "\",\"phone\":\""
											+ perPhone_cursor.getString(3)
											+ "\",\"extension\":\""
											+ perPhone_cursor.getString(4)
											+ "\",\"cmPhoneOprtType\":\""
											+ perPhone_cursor.getString(5)
											+ "\"}";
									phones += p + ",";
									p = null;
								}
								
								// 隐患信息
								String sql_perSh = "select distinct cmScShType,cmScShItem,cmScShCheck  from  perSh_aj where  cmSchedId= "+cmSchedId+" and accountId ="
										+ "'" + accountId + "' and cmScShIsOld = 'N'";
								state4 = db.createStatement();
								ResultSet perPh_cursor = state4
										.executeQuery(sql_perSh);
								// 创建变量
								String perPh = "";
								// 遍历电话
								while (perPh_cursor.next()) {
									// 手机号码有修改
									
									String p = "{\"#comment\":[],\"cmScShType\":\""
											+ perPh_cursor.getString(1)
											+ "\",\"cmScShItem\":\""
											+ perPh_cursor.getString(2)
											+ "\",\"cmScShCheck\":\""
											+ perPh_cursor.getString(3)
											+ "\"}";
									perPh += p + ",";
									p = null;
								}
								
								// 图片文件
								String sql_perile = "select distinct  *  from  perFile_aj where  cmSchedId= "+cmSchedId+" and accountId ="
										+ "'" + accountId + "'";
								
								state5 = db.createStatement();
								ResultSet perFile_cursor = state5
										.executeQuery(sql_perile);
								// 创建变量
								String perFile = "";
								while (perFile_cursor.next()) {
									
									String p = "{\"#comment\":[],\"cmScFileName\":\""
											+ perFile_cursor.getString("cmScFileName")
											+ "\"," +
//											"\"cmScFileTitle\":\""
//											+ perFile_cursor.getString(2)
//											+ "\"," +
											"\"cmScFileForm\":\""
											+ perFile_cursor.getString("cmScFileForm")
											+ "\"," +
//											"\"cmScBusiType\":\""
//											+ perFile_cursor.getString(4)
//											+ "\"," +
											"\"cmScFileRoute\":\""
											+ perFile_cursor.getString("cmScFileRoute")
											+ "\",\"cmScFileSize\":\""
											+ perFile_cursor.getString("cmScFileSize")
											+ "\",\"cmScFileDttm\":\""
											+ perFile_cursor.getString("cmScFileDttm")
											+ "\",\"cmScFileVar1\":\""
											+ perFile_cursor.getString("cmScFileVar1")
											+ "\",\"cmScFileVar2\":\""
											+ perFile_cursor.getString("cmScFileVar2")
											+ "\"";
//									+ "\"}";
									if( !TextUtils.isEmpty(perFile_cursor.getString("cmScBusiType")) && !perFile_cursor.getString("cmScBusiType").equals("null") ){
										p+=",\"cmScBusiType\":\""+ perFile_cursor.getString("cmScBusiType")+ "\"";
									}
									if(!TextUtils.isEmpty(perFile_cursor.getString("cmScFileTitle")) && !perFile_cursor.getString("cmScFileTitle").equals("null") ){
										p+=",\"cmScFileTitle\":\""+ perFile_cursor.getString("cmScFileTitle")+ "\"";
									}
									p+="}";
									perFile += p + ",";
									p = null;
								}
								// 安全讲解
								String sql_perSp = "select  *  from  perSp_aj where  cmSchedId= '"+cmSchedId+"' and accountId ="
										+ "'" + accountId + "'";
								
								state6 = db.createStatement();
								ResultSet perSp_cursor = state6
										.executeQuery(sql_perSp);
								// 创建变量
								String perSp = "";
								while (perSp_cursor.next()) {
									
									String p = "{\"#comment\":[],\"cmScSpItem\":\""
											+ perSp_cursor.getString("cmScSpItem")
											+ "\",\"cmScSpCheck\":\""
											+ perSp_cursor.getString("cmScSpCheck")
											+ "\"}";
									perSp += p + ",";
									p = null;
								}
								
								
								userInfo += str.replace("$phones$", phones).replace("$perSh$", perPh).replace("$perFile$", perFile).replace("$perSp$", perSp)
										+ ",";
								// 第三层while循环结束
								perPhone_cursor.close();
								if (!userInfo.equals("")) {
									isEmpty = true;
								}
							}// 第二层while循环结束
							uploadcust.close();
						}// ------
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
							+ "\"," + "\"schedInfoResidents\": [" + schedInfo + "]}}}}}";

					System.out.println("----key1--------" + key1);
					overlayProgress = AlertUtils.showDialog(getActivity(),
							null, null, false);
					new Thread() {
						public void run() {
							String string = DataCollectionUtils
									.SynchronousData(
											WebUtils.uploadUrl_anjian, key1);
							Message message = mHandle.obtainMessage();
							message.obj = string;
							message.what = SUCCESS1;
							mHandle.sendMessage(message);

						};
					}.start();
				} else {
					Toast.makeText(mActivity, "没有上传的任务或用户", Toast.LENGTH_LONG).show();
				}

				// 除了要去修改数据以外,还需要告知用户,所有的条目已经选中,刷新数据适配器
				// myAdapter.notifyDataSetChanged();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (db != null) {
					try {
						db.close();
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

			break;

//		case R.id.bt_left1:
//			backPrecious();
//			break;

		default:
			break;
		}
	}

	// 查询设备标识
	// deviceId

	public List<OutPut> getResult(String string) {
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
}
