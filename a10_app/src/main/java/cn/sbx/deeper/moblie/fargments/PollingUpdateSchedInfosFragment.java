package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
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

import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CustInfo;
import cn.sbx.deeper.moblie.domian.OutPut;
import cn.sbx.deeper.moblie.domian.SchedInfo;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

@SuppressLint("ValidFragment")
public class PollingUpdateSchedInfosFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener {
	private String tag = "PollingUpdateSchedInfosFragment";
	private IApproveBackToList backToList;
	private int targetContainer;
	private String user_nameLogin;
	private Connection db;
	private ArrayList<SchedInfo> schedInfos_list;
	private Button button_update, btn_selectall, bt_left1;
	private MyAdapter myAdapter;
	private ProgressHUD overlayProgress = null;
	private boolean isCheckAll = false;
	public static final int SUCCESS1 = 10;
	private ArrayList<SchedInfo> update_schedInfo = new ArrayList<SchedInfo>();// 存放已上传的任务
	// 存放已上传的用户的spMeterHistoryId
	private ArrayList<String> schedList = new ArrayList<String>();
	private Statement state;
	private Handler mHandle = new Handler() {

		public void handleMessage(Message msg) {
			if (overlayProgress != null) {
				overlayProgress.dismiss();
			}

			switch (msg.what) {
			case 10:
				String str = (String) msg.obj;
				// 解析返回值

				List<OutPut> list = getResult(str);
				if (list != null && list.size() > 0 && list.get(0) != null) {

					Toast.makeText(mActivity, list.get(0).getRespDescr().trim(), Toast.LENGTH_LONG)
							.show();
					// 获取系统当前时间
					long timeMillis = System.currentTimeMillis();

					Log.i(tag, timeMillis + ":上传结束===========");
					Log.i(tag, (timeMillis - update_start) / 1000
							+ ":上传耗时===========");

					// 上传成功后 修改用户表 上传表状态,以及上传时间
					if (schedList.size() > 0) {
						try {
							db = SQLiteData.openOrCreateDatabase(getActivity());
							state = db.createStatement();
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

								state.executeUpdate(sql_insert);
								state.executeUpdate(sql_updateCustInfo);

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

								state.executeUpdate(sql_insert_time);
								state.executeUpdate(sql_upload);
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
					}
					backPrecious();
				} else {
					Toast.makeText(mActivity, "上传失败", Toast.LENGTH_LONG).show();
					if (overlayProgress != null) {
						overlayProgress.dismiss();
					}
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
	private Statement stat;

	public PollingUpdateSchedInfosFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public PollingUpdateSchedInfosFragment() {
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

		schedInfos_list = selectSchedInfos(user_nameLogin);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ListView list_schedInfos;
		TextView text_view;
		// 任务列表布局
		View view = View.inflate(mActivity,
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
			SchedInfo schedInfo = getItem(position);
			holder.tv_schedinfo_name.setText("抄表路径描述 : "+"\r\n"+schedInfo.getCmMrRteDescr()  
					+ "\r\n" + "可上传任务量 : "+schedInfo.getAccount() + "\r\n"
					+"计划抄表日期 : "+ schedInfo.getScheduledSelectionDate());
			holder.cb_box.setChecked(schedInfo.isCheck);

			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_schedinfo_name;
		CheckBox cb_box;
	}

	private ArrayList<SchedInfo> selectSchedInfos(String user_nameLogin) {
		String userName_login = PerfUtils.getString(mActivity, "userName", "");
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

		String sql_selectSchedInfos = "select rowId,cmMrRteDescr,meterReadCycle,scheduledSelectionDate,cmMrDate,meterReadRoute from schedInfo where userID = "
				+ "'" + user_nameLogin + "'" + "";
		// Cursor cursor_schedInfos = db.rawQuery(sql_selectSchedInfos, null);
		ArrayList<SchedInfo> schedInfos_List = new ArrayList<SchedInfo>();
		try {
			db = SQLiteData.openOrCreateDatabase(getActivity());
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
				schedInfo.setMeterReadRoute(cursor_schedInfos.getString("meterReadRoute"));
				
//				获取当前任务可上传的数量
				
				String  sql_account = "select distinct * from custInfo where schedInfoID = '"+cursor_schedInfos.getString(1)+"' and cmMrState = '3'";
				
				ResultSet executeQuery = stat.executeQuery(sql_account);
				executeQuery.last();
				
				schedInfo.setAccount(executeQuery.getRow()); //当前任务下的可上传客户数量
				executeQuery.close();
//				stat.close();
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

	private boolean delete(SchedInfo schedInfo) {
		// 标记该任务下的用户是否全部被删除
		boolean isClearAll = false;
		try {
			// 存放需要删除的用户
			ArrayList<CustInfo> cuInfos_List = new ArrayList<CustInfo>();
			db = SQLiteData.openOrCreateDatabase(getActivity());
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_next:

			if (!isCheckAll) {
				// 第一次单击全选
				for (SchedInfo schedInfo : schedInfos_list) {

					schedInfo.isCheck = true;
				}
				isCheckAll = !isCheckAll;
				// 所有的条目已经选中,刷新数据适配器
				myAdapter.notifyDataSetChanged();
				btn_selectall.setText("取消");
				Toast.makeText(mActivity, "再次单击取消选中", Toast.LENGTH_SHORT).show();
			} else {
				// 第二次单击全选 全部取消选中
				for (SchedInfo schedInfo : schedInfos_list) {

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
				for (SchedInfo schedInfos : schedInfos_list) {
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
						ResultSet executeQuery = state.executeQuery(cust_his);

						executeQuery.last();
						int row = executeQuery.getRow();
						executeQuery.beforeFirst();

						while (executeQuery.next()) {
							String spMeterHistoryId = executeQuery.getString(1);
							String accountId = executeQuery.getString(2);

							state2 = db.createStatement();

							String up_sched = "select meterConfigurationId,cmMrDttm,cmMr,readType,cmMrRefVol,cmMrRefDebt,cmMrNotiPrtd,cmMrCommCd,cmMrRemark from uploadcustInfo where spMeterHistoryId = '"
									+ spMeterHistoryId + "'";
							ResultSet uploadcust = state2
									.executeQuery(up_sched);
							while (uploadcust.next()) {

								// 将上传的Id存到数组
								schedList.add(spMeterHistoryId);
								String time = "";
								if (uploadcust.getString(2) != null) {

									time = uploadcust.getString(2)
											.replace(" ", "-")
											.replace(":", ".");
								}
								// for (int a = 0; a < 600; a++) {
								String str = String
										.format("{\"#comment\":[],\"spMeterHistoryId\":\""
												+ spMeterHistoryId
												+ "\","
												+ "\"meterConfigurationId\":\""
												+ uploadcust.getString(1)
												+ "\",\"cmMrDttm\":\""
												+ time
												+ "\",\"cmMr\":\""
												+ uploadcust.getString(3)
												+ "\",\"readType\":\""
												+ uploadcust.getString(4)
												+ "\",\"cmMrRefVol\":\""
												+ uploadcust.getString(5)
												+ "\","
												+ "\"cmMrRefDebt\":\""
												+ uploadcust.getString(6)
												+ "\",\"cmMrNotiPrtd\":\""
												+ 'Y'
												+ "\",\"cmMrCommCd\":\""
												+ uploadcust.getString(8)
												+ "\",\"cmMrRemark\":\""
												+ uploadcust.getString(9)
												+ "\","
												+ "\"perPhone\":[ "
												+ "$phones$" + " ]}");

								// 原始
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
								userInfo += str.replace("$phones$", phones)
										+ ",";
								// 第三层while循环结束
								perPhone_cursor.close();
								if (!userInfo.equals("")) {
									isEmpty = true;
								}
							}// 第二层while循环结束
							uploadcust.close();
						}// ------
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

					final String key1 = "{\"root\":{\"list\":{\"CM_H_MRStUp\":{\"@faultStyle\":\"wsdl\""
							+ ",\"input\":{\"#comment\":[],\"user\":\""
							+ user_nameLogin
							+ "\",\"deviceId\":\""
							+ "deviceId"// 修改
							+ "\"," + "\"schedInfo\": [" + schedInfo + "]}}}}}";

					System.out.println("----key1--------" + key1);

					overlayProgress = AlertUtils.showDialog(getActivity(),
							null, null, false);
					new Thread() {
						public void run() {
							String string = DataCollectionUtils
									.SynchronousData(
											WebUtils.uploadchaobiaoUrl, key1);
							Message message = mHandle.obtainMessage();
							message.obj = string;
							message.what = SUCCESS1;
							mHandle.sendMessage(message);

						};
					}.start();
				} else {
					Toast.makeText(mActivity, "没有上传的任务或用户", Toast.LENGTH_SHORT).show();
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
}
