package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.domian.CmScShItemInfo;
import cn.sbx.deeper.moblie.domian.CmScShType_Dic;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.cmScShItem;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.DatabaseHelperInstance;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 * 
 * @author terry.C
 * 
 */
@SuppressLint("ValidFragment")
public class AnJianHistoryRecordFragment extends BaseFragment implements
		OnClickListener, IRefreshButtonAndText {
	DatabaseHelperInstance databaseHelperInstance;
	SQLiteDatabase instance;
	protected static final int GETDATA = 10;
	Context mContext;
	private SinopecMenuModule menuModule;
	private String taskId;
	private IApproveBackToList backToList;
	private int targetContainer;
	private LayoutInflater layoutInflater;
	BaseActivity activity;
	private TextView tv_lasttime_security_check_date, tv_time_interval,
			tv_yinhuandetail, spinner_hidden_danger;
	private EditText m_phone, et_safety_info, gs_phone;

	public AnJianHistoryRecordFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public AnJianHistoryRecordFragment() {
	}

	ArrayList<CmScShType_Dic> list_CmScShType_Dic = new ArrayList<CmScShType_Dic>();// 存放历史隐患数据

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETDATA:
				ArrayList<CmScShType_Dic> list_CmScShType_Dic = (ArrayList<CmScShType_Dic>) msg.obj;
				myAdapter.refreshlist(list_CmScShType_Dic);

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
		Bundle bundle = getArguments();
		if (bundle != null) {
			custInfo = (CustInfo_AnJian) bundle.getSerializable("custinfo");

			taskId = bundle.getString("id");
		}
		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("提交");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.GONE);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("上次安检信息");
		layoutInflater = getActivity().getLayoutInflater();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.anjian_history_record_layout,
				container, false);
		tv_lasttime_security_check_date = (TextView) view
				.findViewById(R.id.tv_lasttime_security_check_date);
		tv_time_interval = (TextView) view.findViewById(R.id.tv_time_interval);
		tv_yinhuandetail = (TextView) view.findViewById(R.id.tv_yinhuandetail);
		view_line = view.findViewById(R.id.view_line);
		
		
		listview = (ListView) view.findViewById(R.id.expan_listview);
		listview.setDivider(new ColorDrawable(getResources().getColor(R.color.littleGray)));
		listview.setDividerHeight(2);
		myAdapter = new myAdapter(list_CmScShType_Dic);
		listview.setAdapter(myAdapter);
		
		spinner_issafety = (TextView) view.findViewById(R.id.spinner_issafety);
		spinner_hidden_danger = (TextView) view
				.findViewById(R.id.spinner_hidden_danger);
		spinner_issafety
				.setText(custInfo.getCmScAqyh().equals("Y") ? "是" : "否");

		tv_lasttime_security_check_date
				.setText(custInfo.getCmMrLastSecchkDt() == null
						|| custInfo.getCmMrLastSecchkDt().equals("null") ? ""
						: custInfo.getCmMrLastSecchkDt());
		// tv_yinhuandetail.setText(custInfo.getCmScYhzg().equals("N")?"":custInfo.getCmScYhzg());
		// // 隐患信息需列表显示
		// tv_hidden_danger.setText(custInfo.getCmScYhzg());
		if (custInfo.getCmScYhzg() != null
				|| custInfo.getCmScYhzg().equals("null")) {
			spinner_hidden_danger
					.setText(custInfo.getCmScYhzg().equals("Y") ? "已整改" : "未整改");
		} else {
			spinner_hidden_danger.setText("未整改");
		}

		try {
			SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate = dd.parse(custInfo.getCmMrLastSecchkDt());
			int daysBetween = daysBetween(dd,beginDate, new Date());
			
			tv_time_interval.setText(Math.abs(daysBetween) + "天");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return view;

	}

//	计算日期间隔
	 public  int daysBetween(SimpleDateFormat sdf,Date smdate,Date bdate) throws ParseException    
	    {    
//	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        smdate=sdf.parse(sdf.format(smdate));  
	        bdate=sdf.parse(sdf.format(bdate));  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(smdate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(bdate);    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));           
	    }    
	
	
	public class myAdapter extends BaseAdapter {

		private ArrayList<CmScShType_Dic> list_CmScShType_Dic;
		private MyHolder holder;

		public myAdapter(ArrayList<CmScShType_Dic> list) {
			this.list_CmScShType_Dic = list;
			
		}

		public void refreshlist(ArrayList<CmScShType_Dic> list) {
			this.list_CmScShType_Dic = list;
			if(list_CmScShType_Dic.size()>0){
				tv_yinhuandetail.setVisibility(View.VISIBLE);
				view_line.setVisibility(View.VISIBLE);
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list_CmScShType_Dic.size();
		}

		@Override
		public Object getItem(int position) {
			return list_CmScShType_Dic.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_anjian_yinhuan_history, null);
				holder = new MyHolder();

				holder.tv_danger_type = (TextView) convertView
						.findViewById(R.id.tv_danger_type);
				holder.tv_danger_item1 = (TextView) convertView
						.findViewById(R.id.tv_danger_item1);
				convertView.setTag(holder);
			} else {
				holder = (MyHolder) convertView.getTag();
			}
			// 拼接隐患选项字符串
			String string = "";
			ArrayList<cmScShItem> cmScShItem = list_CmScShType_Dic
					.get(position).getCmScShItemInfo().get(position)
					.getCmScShItem();

			for (int i = 0; i < cmScShItem.size(); i++) {
				if (i != cmScShItem.size() - 1) {
					string += cmScShItem.get(i).getCmScShItemDescr() + "\r\n";
				} else {
					string += cmScShItem.get(i).getCmScShItemDescr();
				}

			}
			holder.tv_danger_type.setText(list_CmScShType_Dic.get(position)
					.getCmScShTypeDescr());
			holder.tv_danger_item1.setText(string);

			return convertView;
		}
	}

	public class MyHolder {
		TextView tv_danger_type;
		TextView tv_danger_item1;
		LinearLayout ll_contener_danger;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		 * switch (v.getId()) { case R.id.btn_next: String string =
		 * updateUserInfo(taskId, m_phone.getText().toString() .trim(),
		 * zc_phone.getText().toString().trim(), gs_phone
		 * .getText().toString().trim()); if (string.equals("m")) {
		 * Toast.makeText(getActivity(), "手机号码格式不正确", 1).show(); } else if
		 * (string.equals("zc")) { Toast.makeText(getActivity(),
		 * "请输入住宅电话 例如：010-88888888", 1) .show();
		 * 
		 * } else if (string.equals("gs")) { Toast.makeText(getActivity(),
		 * "请输入公司电话 例如：010-88888888", 1) .show(); } else {
		 * Toast.makeText(getActivity(), "提交成功", 1).show(); selectList(taskId);
		 * } break; }
		 */
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// selectList(taskId);

		String[] str = new String[] { "否", "是" };
		String[] options = new String[] { "请选择", "已整改", "整改中", "未整改" };

		ArrayAdapter<String> adapter_isSafety = new ArrayAdapter<String>(
				mContext, R.layout.spinner_item, str);
		ArrayAdapter<String> adapter_isModification = new ArrayAdapter<String>(
				mContext, R.layout.spinner_item, options);

		adapter_isSafety
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter_isModification
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// spinner_issafety
		// .setBackgroundResource(R.drawable.ic_approve_spinner_background);

		// spinner_issafety.setAdapter(adapter_isSafety);

		// 获取隐患数据信息

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String sql = "select distinct cmScShTypeDescr from perSh_aj1 where accountId = '"
							+ custInfo.getAccountId()
							+ "' and cmSchedId = '"
							+ custInfo.getCmSchedId()
							+ "' and cmScShIsOld = 'Y' ";


					ArrayList<CmScShItemInfo> list_CmScShItemInfo = new ArrayList<CmScShItemInfo>();

					databaseHelperInstance = new DatabaseHelperInstance();

					instance = databaseHelperInstance.getInstance(getActivity());
					Cursor cursor = instance.rawQuery(sql, new String[]{});


					if(cursor.moveToFirst()){
						for (int i = 0; i <cursor.getCount() ; i++) {
							CmScShType_Dic cmScShType_Dic = new CmScShType_Dic();

							cmScShType_Dic.setCmScShTypeDescr(cursor
									.getString(0));
							String sql1 = "select distinct cmScShItemDescr from perSh_aj1 where accountId = '"
									+ custInfo.getAccountId()
									+ "' and cmSchedId = '"
									+ custInfo.getCmSchedId()
									+ "' and cmScShIsOld = 'Y' and cmScShTypeDescr = '"+cmScShType_Dic.getCmScShTypeDescr()+"'";
							Cursor cursor1 = instance.rawQuery(sql1, new String[]{});

//							ps1.setString(1, cursor.getString(0));
//							ResultSet resultset1 = ps1.executeQuery();

							ArrayList<cmScShItem> list_CmscShItem = new ArrayList<cmScShItem>();// 存放每一个隐患类型下的所有隐患选项
							if(cursor1.moveToFirst()){
								for (int j = 0; j <cursor1.getCount() ; j++) {
									cmScShItem cmScShItem = new cmScShItem();// 每个隐患类型下的隐患选项
									cmScShItem.setCmScShItemDescr(cursor1
											.getString(0));
									list_CmscShItem.add(cmScShItem);// 将同一隐患类型下的所有隐患选项
									cursor1.moveToNext();
								}

								if (cursor1!=null){
									cursor1.close();
								}
							}

							CmScShItemInfo cmScShItemInfo = new CmScShItemInfo();// 将
							cmScShItemInfo.setCmScShItem(list_CmscShItem); // 将每一个隐患类型下的所有隐患选项
							// 封装到对象
							list_CmScShItemInfo.add(cmScShItemInfo); // 将上步骤的对象添加到集合
							cmScShType_Dic.setCmScShItemInfo(list_CmScShItemInfo); // 将上步骤的集合封装隐患类型对象中
							list_CmScShType_Dic.add(cmScShType_Dic); // 将上诉隐患类型添加到集合
							cursor.moveToNext();
						}
//						cursor.moveToNext();

					}

//					Connection conne = SQLiteData.openOrCreateDatabase(getActivity());
//					PreparedStatement ps = conne.prepareStatement(sql);
//					PreparedStatement ps1 = conne.prepareStatement(sql1);
//
//					ResultSet resultSet = ps.executeQuery();
//					while (resultSet.next()) {
//						CmScShType_Dic cmScShType_Dic = new CmScShType_Dic();
//
//						cmScShType_Dic.setCmScShTypeDescr(resultSet
//								.getString("cmScShTypeDescr"));
//
//						ps1.setString(1, resultSet.getString("cmScShTypeDescr"));
//						ResultSet resultset1 = ps1.executeQuery();
//
//						ArrayList<cmScShItem> list_CmscShItem = new ArrayList<cmScShItem>();// 存放每一个隐患类型下的所有隐患选项
//						while (resultset1.next()) {
//							cmScShItem cmScShItem = new cmScShItem();// 每个隐患类型下的隐患选项
//							cmScShItem.setCmScShItemDescr(resultset1
//									.getString("cmScShItemDescr"));
//							list_CmscShItem.add(cmScShItem);// 将同一隐患类型下的所有隐患选项
//															// 添加到集合
//						}
//						CmScShItemInfo cmScShItemInfo = new CmScShItemInfo();// 将
//						cmScShItemInfo.setCmScShItem(list_CmscShItem); // 将每一个隐患类型下的所有隐患选项
//																		// 封装到对象
//						list_CmScShItemInfo.add(cmScShItemInfo); // 将上步骤的对象添加到集合
//						cmScShType_Dic.setCmScShItemInfo(list_CmScShItemInfo); // 将上步骤的集合封装隐患类型对象中
//						list_CmScShType_Dic.add(cmScShType_Dic); // 将上诉隐患类型添加到集合
//					}
					if (cursor!=null){
						cursor.close();
					}

					// 数据获取后 刷新列表适配器
					Message msg = mHandler.obtainMessage();

					msg.obj = list_CmScShType_Dic;
					msg.what = GETDATA;
					mHandler.sendMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// 查询待办数据
//	public SQLiteDatabase db;// 操作数据库的工具类
	StringBuilder sb;
	private TextView spinner_issafety;
	private CustInfo_AnJian custInfo;
	private ListView listview;
	private myAdapter myAdapter;
	private View view_line;

	public void selectList(String user_id) {
		String sql = "select distinct * from anjian_data where user_id=" + "'"
				+ user_id + "'";
		String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";
//		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//		db = SQLiteDatabase.openOrCreateDatabase(path, null);
		Cursor c = instance.rawQuery(sql, null);
		// 查询数据返回游标对象
//		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			// 通过游标对象获取值
			/*
			 * user_no.setText(c.getString(0));
			 * user_name.setText(c.getString(1)); if
			 * (c.getString(3).contains("手机")) {
			 * m_phone.setText(c.getString(3).substring(3,
			 * c.getString(3).length())); } else {
			 * m_phone.setText(c.getString(3)); }
			 * zc_phone.setText(c.getString(46));
			 * gs_phone.setText(c.getString(47));
			 */
		}
		c.close();
//		db.close();
	}

	// 更新电话号码
	public String updateUserInfo(String id, String m_phone, String zc_phone,
			String gs_phone) {
		if (isMobileNO(m_phone)) {
			if (isPhoneNumberValid(zc_phone)) {
				if (isPhoneNumberValid(gs_phone)) {
					String sql = "update anjian_data set user_phone=" + m_phone
							+ ", zc_phone=" + "\"" + zc_phone + "\""
							+ " ,gs_phone=" + "\"" + gs_phone + "\""
							+ " where user_id=" + id + "";
//					String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
					String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";
					// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//					db = SQLiteDatabase.openOrCreateDatabase(path, null);
					instance.execSQL(sql);
					// 查询数据返回游标对象
//					db.execSQL(sql);
//					db.close();
					return "true";
				} else {
					return "gs";
				}
			} else {
				return "zc";
			}
		} else {
			return "m";
		}

	}

	public static boolean isPhoneNumberValid(String phoneNumber) {
		Pattern patternPhone = Pattern.compile(
				"^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcherPhone = patternPhone.matcher(phoneNumber);
		boolean booleanPhone = matcherPhone.matches();
		return booleanPhone;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	@Override
	public void refreshButtonAndText() {
		super.refreshButtonAndText();

		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setVisibility(View.GONE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(instance!=null){
			instance.close();
		}
	}
}
