package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CustInfo;
import cn.sbx.deeper.moblie.domian.PerPhone;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.LogUtil;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 * 
 * @author terry.C
 * 
 */
@SuppressLint("ValidFragment")
public class PollingUserPhoneFragment extends BaseFragment implements
		OnClickListener, IRefreshButtonAndText {

	private static final String tag = "PollingUserPhoneFragment";
	Context mContext;
	private SinopecMenuModule menuModule;
	private IApproveBackToList backToList;
	private int targetContainer;
	private LayoutInflater layoutInflater;
	BaseActivity activity;
	private TextView user_no, user_name, all_address, user_phone,
			tv_accountPhone;
	private CustInfo custInfo;

	public Connection db;
	String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";// 电话表格路径
	private String phone;
	private PerPhone perPhone;
	private TextView tv_user_type;
	
	private String taskId;
	private Button btn_save;
	private ArrayList<PerPhone> arrayList_perPhone = null;
	private ArrayList<PerPhone> phone_arrayList;
	private Statement state;
	private TextView tv_title;

	public PollingUserPhoneFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public PollingUserPhoneFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.i(tag, "-------------onCreate---");

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
		Bundle bundle = getArguments();
		if (bundle != null) {
			custInfo = (CustInfo) bundle.getSerializable("mCustInfo");
			taskId = custInfo.getAccountId();
		}
		// getPerPhone(taskId);
		selectList(taskId);

		btn_save = (Button) getActivity().findViewById(R.id.btn_save);

		btn_save.setVisibility(View.GONE);

		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("提交");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.GONE);
		tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("客户信息");
		layoutInflater = getActivity().getLayoutInflater();

		// 初始化用户电话
		PollingUserPhoneDetailFragment_new userPhoneDetailFragmentnew = new PollingUserPhoneDetailFragment_new();
		phone_arrayList = userPhoneDetailFragmentnew.selectPhone(taskId,mContext);
	}

	// 查询号码
	public PerPhone getPerPhone(String taskId) {
		// 查询用户电话，根据accoundId
		String sql = "select phone from perPhone where accountId = " + taskId
				+ "";
		try {
			db = SQLiteData.openOrCreateDatabase(this.mContext);
			state = db.createStatement();
			ResultSet c = state.executeQuery(sql);
			// Cursor c = db.rawQuery(sql, null);
			perPhone = new PerPhone();
			if (c.next()) {
				perPhone.setPhone(c.getString(1));
			} else {
				perPhone.setPhone("");
			}

			c.close();
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

		return perPhone;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.i(tag, "-------------onCreateView---");

		View view = inflater.inflate(R.layout.cb_user_chaobiao_info_layout,
				container, false);
		user_no = (TextView) view.findViewById(R.id.user_no);
		user_name = (TextView) view.findViewById(R.id.user_name);
//		tv_user_type = (TextView) view.findViewById(R.id.tv_user_type);
		all_address = (TextView) view.findViewById(R.id.all_address);
		user_phone = (TextView) view.findViewById(R.id.user_phone);
		tv_accountPhone = (TextView) view.findViewById(R.id.tv_accountPhone);

		// user_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
//		tv_meterConfiguration = (TextView) view
//				.findViewById(R.id.tv_meterConfiguration);
//		tv_meterValue = (TextView) view.findViewById(R.id.tv_meterValue);
//		tv_meterMaxValue = (TextView) view.findViewById(R.id.tv_meterMaxValue);
//		tv_month_averageValue = (TextView) view
//				.findViewById(R.id.tv_month_averageValue);
//		tv_rateSchedule = (TextView) view.findViewById(R.id.tv_rateSchedule);
//		tv_price_describe = (TextView) view
//				.findViewById(R.id.tv_price_describe);
//		tv_arrearage = (TextView) view.findViewById(R.id.tv_arrearage);
//		tv_zhiNaJin = (TextView) view.findViewById(R.id.tv_zhiNaJin);
//		tv_arrearageDate = (TextView) view.findViewById(R.id.tv_arrearageDate);

		// 赋值
		user_no.setText(custInfo.getAccountId());
		user_name.setText(custInfo.getEntityName());
//		tv_user_type.setText(custInfo.getCustomerClass());
		all_address.setText(custInfo.getCmMrAddress());

		// 显示电话,默认显示HOME 电话 如没有HOme 电话 显示其他
		showAcconutPhone();

//		tv_meterConfiguration.setText(custInfo.getMeterConfigurationId());
//		// tv_meterValue.setText(custInfo.getCmMrMtrBarCode());
//		DataIsNull.setValue(tv_meterValue, custInfo.getCmMrMtrBarCode());
//		tv_meterMaxValue.setText(custInfo.getFullScale());
//		tv_month_averageValue.setText(custInfo.getCmMrAvgVol());
//		tv_rateSchedule.setText(custInfo.getRateSchedule());
//
//		tv_price_describe.setText(custInfo.getCmRsDescr());
//		tv_arrearage.setText(custInfo.getCmMrLastBal());
//
//		tv_zhiNaJin.setText(custInfo.getCmMrOverdueAmt());
//		// tv_arrearageDate.setText(custInfo.getCmMrDebtStatDt());
//
//		DataIsNull.setValue(tv_arrearage, custInfo.getCmMrLastBal());
//		DataIsNull.setValue(tv_arrearageDate, custInfo.getCmMrDebtStatDt());

		user_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment f = new PollingUserPhoneDetailFragment_new(backToList,
						targetContainer);
				Bundle bundle = new Bundle();
				bundle.putString("id", taskId);
				bundle.putSerializable("mCustInfo", custInfo);
				f.setArguments(bundle);
				((ActivityInTab) getActivity()).navigateTo(f);

			}
		});
		return view;
	}

	private void showAcconutPhone() {
		if (phone_arrayList != null && phone_arrayList.size() > 0) {
			for (int a = 0; a < phone_arrayList.size(); a++) {
				PerPhone phone = phone_arrayList.get(a);
				if (phone.getPhoneType().equals("HOME")
						&& !TextUtils.isEmpty(phone.getPhone())) {
					tv_accountPhone.setText("住宅电话");
					
					user_phone.setText(phone.getPhone());
					user_phone.setTextColor(Color.BLACK);
					break;
				} else if (phone.getPhoneType().equals("CELL")
						&& !TextUtils.isEmpty(phone.getPhone())) {
					tv_accountPhone.setText("移动电话");
					user_phone.setText(phone.getPhone());
					user_phone.setTextColor(Color.BLACK);
					break;
				} else if (phone.getPhoneType().equals("BUSN")
						&& !TextUtils.isEmpty(phone.getPhone())) {
					tv_accountPhone.setText("公司电话");
					user_phone.setText(phone.getPhone());
					user_phone.setTextColor(Color.BLACK);
					break;
				} else {
					// 尚无电话 点击添加
					user_phone.setText("尚无电话 点击添加");
					user_phone.setTextColor(Color.GRAY);
				}
			}
		} else {
			// 尚无电话 点击添加
			user_phone.setText("尚无电话 点击添加");
			user_phone.setTextColor(Color.GRAY);
		}
	}

	public PerPhone selectList(String user_id) {

		String sql = "select phone from perPhone where accountId=" + "'"
				+ user_id + "' and  phoneType = 'HOME' ";
		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		try {
			db = SQLiteData.openOrCreateDatabase(this.mContext);
			state = db.createStatement();
			// 查询数据返回游标对象
			// Cursor c = db.rawQuery(sql, null);
			ResultSet c = state.executeQuery(sql);
			arrayList_perPhone = new ArrayList<PerPhone>();
			while (c.next()) {
				perPhone = new PerPhone();
				// perPhone.setSequence(c.getString(1));
				// perPhone.setPhoneType(c.getString(2));
				perPhone.setPhone(c.getString(1));
				// perPhone.setExtension(c.getString(4));
				arrayList_perPhone.add(perPhone);
			}
			c.close();
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
		return perPhone;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:
			// String string = updateUserInfo(taskId,
			// m_phone.getText().toString()
			// .trim(), zc_phone.getText().toString().trim(), gs_phone
			// .getText().toString().trim());
			// if (string.equals("m")) {
			// Toast.makeText(getActivity(), "手机号码格式不正确", 1).show();
			// } else if (string.equals("zc")) {
			// Toast.makeText(getActivity(), "请输入住宅电话 例如：010-88888888", 1)
			// .show();
			//
			// } else if (string.equals("gs")) {
			// Toast.makeText(getActivity(), "请输入公司电话 例如：010-88888888", 1)
			// .show();
			// } else {
			// Toast.makeText(getActivity(), "提交成功", 1).show();
			// selectList(taskId);
			// }
			break;
		}
	}

	@Override
	public void onStart() {
		LogUtil.i(tag, "-------------onStart---");
		super.onStart();
		getPerPhone(taskId);
	}

	@Override
	public void onResume() {
		LogUtil.i(tag, "-------------onStart---");

		// TODO Auto-generated method stub
		super.onResume();
		// selectList(taskId);

	}

	/*
	 * 
	 * // 查询待办数据 public SQLiteDatabase db;// 操作数据库的工具类 StringBuilder sb; private
	 * CustInfo custInfo;
	 * 
	 * public void selectList(String user_id) { String sql =
	 * "select * from chaobiao_data where user_id=" + "'" + user_id + "'";
	 * String path =
	 * "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db"
	 * ; // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂)) db =
	 * SQLiteDatabase.openOrCreateDatabase(path, null); // 查询数据返回游标对象 Cursor c =
	 * db.rawQuery(sql, null); while (c.moveToNext()) { // 通过游标对象获取值
	 * 
	 * user_no.setText(c.getString(0)); if (c.getString(3).contains("手机")) {
	 * user_phone.setText(c.getString(3).substring(3, c.getString(3).length()));
	 * } else { user_phone.setText(c.getString(3)); }
	 * user_name.setText(c.getString(1)); // zc_phone.setText(c.getString(30));
	 * // gs_phone.setText(c.getString(31)); String string = c.getString(4) +
	 * c.getString(5) + c.getString(6) + c.getString(7) + "栋" + c.getString(8) +
	 * "单元" + c.getString(9) + "号"; all_address.setText(string); } c.close();
	 * db.close(); }
	 */
	/*
	 * // 更新电话号码 public String updateUserInfo(String id, String m_phone, String
	 * zc_phone, String gs_phone) { SQLiteDatabase db;// 操作数据库的工具类
	 * 
	 * if (isMobileNO(m_phone)) { if (isPhoneNumberValid(zc_phone)) { if
	 * (isPhoneNumberValid(gs_phone)) { String sql =
	 * "update chaobiao_data set user_phone=" + m_phone + ", zc_phone=" + "\"" +
	 * zc_phone + "\"" + " ,gs_phone=" + "\"" + gs_phone + "\"" +
	 * " where user_id=" + id + ""; String path =
	 * "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db"
	 * ; // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂)) db =
	 * SQLiteDatabase.openOrCreateDatabase(path, null); // 查询数据返回游标对象
	 * db.execSQL(sql); db.close(); return "true"; } else { return "gs"; } }
	 * else { return "zc"; } } else { return "m"; }
	 * 
	 * }
	 */

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
		// // getPerPhone(taskId);
		// selectList(taskId);
		// if (perPhone.getPhone() != null && perPhone.getPhone().equals("")) {
		//
		// user_phone.setText(perPhone.getPhone());
		// }
		tv_title.setText("客户信息");
		// 显示电话,默认显示HOME 电话 如没有HOme 电话 显示其他
		PollingUserPhoneDetailFragment_new userPhoneDetailFragmentnew = new PollingUserPhoneDetailFragment_new();
		phone_arrayList = userPhoneDetailFragmentnew.selectPhone(taskId,mContext);
		showAcconutPhone();

	}

}
