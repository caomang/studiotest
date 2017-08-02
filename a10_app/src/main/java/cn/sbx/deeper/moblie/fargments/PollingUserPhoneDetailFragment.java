package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.domian.CustInfo;
import cn.sbx.deeper.moblie.domian.PerPhone;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 * 
 * @author terry.C
 * 
 */
@SuppressLint("ValidFragment")
public class PollingUserPhoneDetailFragment extends BaseFragment implements
		OnClickListener, IRefreshButtonAndText {

	private static final String String = null;
	Context mContext;
	private SinopecMenuModule menuModule;
	private String taskId;
	private IApproveBackToList backToList;
	private int targetContainer;
	private LayoutInflater layoutInflater;
	BaseActivity activity;
	private TextView tv_client_num, tv_client_name, all_address,
			tv_client_style, tv_client_style_des, tv_home_phone;
	private EditText m_phone, zc_phone, gs_phone, tv_home_phone_num,
			tv_company_phone_num;
	private PerPhone perPhone;

	private Button bt_home_save, bt_home_delete, bt_company_save,
			bt_company_delete, bt_save, bt_add_newphone;
	private Button btn_save;
	private PerPhone phone = null;
	ArrayList<PerPhone> phone_List = null;

	public PollingUserPhoneDetailFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public PollingUserPhoneDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
		Bundle bundle = getArguments();
		if (bundle != null) {
			taskId = bundle.getString("id");
			custInfo = (CustInfo) bundle.getSerializable("mCustInfo");
		}
		btn_save = (Button) getActivity().findViewById(R.id.btn_save);

		btn_save.setVisibility(View.GONE);

		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("提交");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.GONE);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("客户电话");
		layoutInflater = getActivity().getLayoutInflater();

		// selectList(taskId);// 获取数据

		// 查询当前客户的电话记录
		selectPhone(taskId);
	}

	public ArrayList<PerPhone> selectPhone(String taskId) {
		phone_List = new ArrayList<PerPhone>();
		String select_phone = "select sequence,phoneType,phone,extension from perPhone where accountId="
				+ "'" + taskId + "'";
//		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";
		String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/a10_db.db";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		db = SQLiteDatabase.openOrCreateDatabase(path, null);
		// 查询数据返回游标对象
		Cursor phones = db.rawQuery(select_phone, null);

		while (phones.moveToNext()) {
			// if (phones.moveToFirst()) {
			// for (int a = 0; a < phones.getCount(); a++) {

			phone = new PerPhone();
			phone.setSequence(phones.getString(0));
			phone.setPhoneType(phones.getString(1));
			phone.setPhone(phones.getString(2));
			phone.setExtension(""+phones.getString(3));
			phone_List.add(phone);
			// phones.moveToNext();
			// }
			// }
		}
		phones.close();
		db.close();
		return phone_List;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cb_user_phone_info_layout_mine,
				container, false);

		ll_phoneContainer = (LinearLayout) view
				.findViewById(R.id.ll_phoneContainer);

//		tv_client_num = (TextView) view.findViewById(R.id.tv_client_num);
		tv_client_name = (TextView) view.findViewById(R.id.tv_client_name);
//		tv_client_style = (TextView) view.findViewById(R.id.tv_client_style);
		bt_add_newphone = (Button) view.findViewById(R.id.bt_add_newphone);
		bt_add_newphone.setOnClickListener(this);
//		tv_client_style_des = (TextView) view
//				.findViewById(R.id.tv_client_style_des);

		// tv_company_phone_num = (EditText) view
		// .findViewById(R.id.tv_company_phone_num);

		// bt_company_save = (Button) view.findViewById(R.id.bt_company_save);
		// bt_company_delete = (Button)
		// view.findViewById(R.id.bt_company_delete);
		// bt_save = (Button) view.findViewById(R.id.bt_save);
		//
		// spin_style = (Spinner) view.findViewById(R.id.spin_style);

		all_address = (TextView) view.findViewById(R.id.all_address);

		// 添加电话列表
		addPhone();

		return view;
	}

	private void addPhone() {
		View phone_view;
		for (int i = 0; i < phone_List.size(); i++) {
			// 根据客户电话数量创建对应的显示框
			phone_view = View.inflate(mContext,
					R.layout.item_approve_detail_clientphone, null);
			// 控件
			tv_home_phone_num = (EditText) phone_view
					.findViewById(R.id.tv_home_phone_num);
			bt_home_save = (Button) phone_view.findViewById(R.id.bt_home_save);
			tv_home_phone = (TextView) phone_view
					.findViewById(R.id.tv_home_phone);
			bt_home_delete = (Button) phone_view
					.findViewById(R.id.bt_home_delete);
			// 判断该用户中的所有电话类型是否有电话,没有就不显示
			if (!phone_List.get(i).getPhone().equals("null")
					&& !TextUtils.isEmpty(phone_List.get(i).getPhone())) {
				// 按照数据库中的电话顺序进行显示电话类型 及电话

				if (phone_List.get(i).getPhoneType() != null
						&& phone_List.get(i).getPhoneType().equals("HOME")) {
					tv_home_phone.setText("住宅电话");
					tv_home_phone_num.setText(phone_List.get(i).getPhone());
					// tv_home_phone_num.setId(Integer.parseInt(phone_List.get(i).getSequence()));

//					phone_view.setTag(i, tv_home_phone_num);
				} else if (phone_List.get(i).getPhoneType() != null
						&& phone_List.get(i).getPhoneType().equals("HOME1")) {
					tv_home_phone.setText("电话");
					tv_home_phone_num.setText(phone_List.get(i).getPhone());
//					phone_view.setTag(i, tv_home_phone_num);
					tv_home_phone_num.setTag(i,tv_home_phone_num);
					
				} else if (phone_List.get(i).getPhoneType() != null
						&& phone_List.get(i).getPhoneType().equals("HOMEPHN")) {
					tv_home_phone.setText("住宅电话");
					tv_home_phone_num.setText(phone_List.get(i).getPhone());
//					phone_view.setTag(i, tv_home_phone_num);
					tv_home_phone_num.setTag(i,tv_home_phone_num);
				} else if (phone_List.get(i).getPhoneType() != null
						&& phone_List.get(i).getPhoneType().equals("CELL")) {
					tv_home_phone.setText("手机");
					tv_home_phone_num.setText(phone_List.get(i).getPhone());
//					phone_view.setTag(i, tv_home_phone_num);
					tv_home_phone_num.setTag(i,tv_home_phone_num);
				} else if (phone_List.get(i).getPhoneType() != null
						&& phone_List.get(i).getPhoneType().equals("BUSN")) {
					tv_home_phone.setText("工作电话");
					tv_home_phone_num.setText(phone_List.get(i).getPhone());
//					phone_view.setTag(i, tv_home_phone_num);
					tv_home_phone_num.setTag(i,tv_home_phone_num);
				} else if (phone_List.get(i).getPhoneType() != null
						&& phone_List.get(i).getPhoneType().equals("BUSNPHN")) {
					tv_home_phone.setText("工作电话");
					tv_home_phone_num.setText(phone_List.get(i).getPhone());
//					phone_view.setTag(i, tv_home_phone_num);
					tv_home_phone_num.setTag(i,tv_home_phone_num);
				} else {
					// 匹配不到电话类型 进入下一条循环
					continue;
				}
				final int temp = i;
				final View tempView = phone_view;
				bt_home_save.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						String phone = ((TextView) tv_home_phone_num.getTag()).getText().toString().trim();
						/*
						 * if (!isMobileNO(phone)) {
						 * Toast.makeText(getActivity(), "手机号码格式不正确", 1)s
						 * .show(); } else if (isPhoneNumberValid(phone) ||
						 * isMobileNO(phone)) {
						 */
						savePhoneNum(phone, temp);
						Toast.makeText(getActivity(), "号码已保存", Toast.LENGTH_SHORT).show();
						/*
						 * } else { Toast.makeText(getActivity(),
						 * "号码保存失败,电话格式有误", 1) .show(); }
						 */
					}
				});

				bt_home_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 获取数据，并保存到本地数据库
						String delete_phone = tv_home_phone_num.getText()
								.toString().trim();
						deletePhoneNum(delete_phone, temp);
						Toast.makeText(getActivity(), "号码已删除", Toast.LENGTH_SHORT).show();
						ll_phoneContainer.removeAllViews();
						selectPhone(taskId);
						addPhone();

					}
				});

				// 对电话的修改的操作根据改电话的序号 类型
				// 添加至电话容器
				ll_phoneContainer.addView(phone_view);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:
			String string = updateUserInfo(taskId, m_phone.getText().toString()
					.trim(), zc_phone.getText().toString().trim(), gs_phone
					.getText().toString().trim());
			if (string.equals("m")) {
				Toast.makeText(getActivity(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
			} else if (string.equals("zc")) {
				Toast.makeText(getActivity(), "请输入住宅电话 例如：010-88888888", Toast.LENGTH_SHORT)
						.show();

			} else if (string.equals("gs")) {
				Toast.makeText(getActivity(), "请输入公司电话 例如：010-88888888", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
				selectList(taskId);
			}
			break;
		case R.id.bt_add_newphone:
			// 添加电话界面
			PollingUserPhoneAddFragment userPhoneAddFragment = new PollingUserPhoneAddFragment();
			Bundle bundle = new Bundle();
			bundle.putString("id", taskId);
			userPhoneAddFragment.setArguments(bundle);
			((ActivityInTab) getActivity()).navigateTo(userPhoneAddFragment);
			break;
		}
	}

	// 插入号码需要获取到 序号的最大值，通过获取到最大id 来获取 对应的序号

	// 保存号码
	public void savePhoneNum(String phone, int a) {
//		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";
		String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/a10_db.db";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(path, null);
		if (!TextUtils.isEmpty(phone)) {
			/*
			 * // 插入电话 sql1 = " insert into perPhone  " + "(" +
			 * "accountId,phone ,cmPhoneOprtType,phoneType,sequence" + ")" +
			 * " values" + "(" + "'" + taskId + "'" + "," + phone +
			 * ",'10' ,'HOME','2'" + ")"; } else {
			 */
			// 更新
			sql1 = "update perPhone set phone =" + "'" + phone + "'"
					+ ",cmPhoneOprtType='20' where accountId=" + "'" + taskId
					+ "'" + "and  sequence = '"
					+ phone_List.get(a).getSequence() + "'";
		}

		// sql1 = "insert into perPhone ( phone) values("+phone+")";

		db1.execSQL(sql1);
		db1.close();
	}

	// 删除号码
	public void deletePhoneNum(String phone, int b) {
//		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";
		String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/a10_db.db";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(path, null);
		// del_sql =
		// "update perPhone set phone ='',cmPhoneOprtType=30  where accountId="
		// +"'"+ taskId+"'"
		// + "";
		if (phone == null) {
			Toast.makeText(mContext, "该号码不存在", Toast.LENGTH_SHORT).show();
		} else {

			del_sql = "update perPhone set phone ='',cmPhoneOprtType='30'  where accountId="
					+ "'"
					+ taskId
					+ "'"
					+ "and  sequence = '"
					+ phone_List.get(b).getSequence() + "'";

			db1.execSQL(del_sql);
			db1.close();

		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

//		tv_client_num.setText(taskId);
		tv_client_name.setText(custInfo.getEntityName());

//		tv_client_style.setText(custInfo.getCustomerClass());
//		tv_client_style_des.setText(custInfo.getCmCustClDescr());

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// selectList(taskId);
	}

	// 查询待办数据
	public SQLiteDatabase db;// 操作数据库的工具类
	private Spinner spin_style;
	private CustInfo custInfo;
	private ArrayList<PerPhone> arrayList_perPhone;
	private String sql;
	private String sql1;
	private String del_sql;
	private LinearLayout ll_phoneContainer;

	// StringBuilder sb;

	public PerPhone selectList(String user_id) {
		// String sql = "select * from perPhone where accountId=" + "'" +
		// user_id+"'"
		// + "" ;
		/*
		 * String sql = "select phone from perPhone where accountId=" + "'" +
		 * user_id + "' and  phoneType = 'HOME' "; String path =
		 * "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db"
		 * ; // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂)) db =
		 * SQLiteDatabase.openOrCreateDatabase(path, null); // 查询数据返回游标对象 Cursor
		 * c = db.rawQuery(sql, null); arrayList_perPhone = new
		 * ArrayList<PerPhone>(); while (c.moveToNext()) { // perPhone = new
		 * PerPhone(); // perPhone.setSequence(c.getString(1)); //
		 * perPhone.setPhoneType(c.getString(2));
		 * perPhone.setPhone(c.getString(0)); //
		 * perPhone.setExtension(c.getString(4));
		 * arrayList_perPhone.add(perPhone); }
		 */

		String sql = "select phone from perPhone where accountId=" + "'"
				+ user_id + "' and  phoneType = 'HOME' ";
//		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";
		String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/a10_db.db";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		db = SQLiteDatabase.openOrCreateDatabase(path, null);
		// 查询数据返回游标对象
		Cursor c = db.rawQuery(sql, null);
		arrayList_perPhone = new ArrayList<PerPhone>();
		while (c.moveToNext()) {
			perPhone = new PerPhone();
			// perPhone.setSequence(c.getString(1));
			// perPhone.setPhoneType(c.getString(2));
			perPhone.setPhone(c.getString(0));
			// perPhone.setExtension(c.getString(4));
			arrayList_perPhone.add(perPhone);
		}
		c.close();
		db.close();
		return perPhone;
	}

	// 更新电话号码
	public String updateUserInfo(String id, String m_phone, String zc_phone,
			String gs_phone) {
		if (isMobileNO(m_phone)) {
			if (isPhoneNumberValid(zc_phone)) {
				if (isPhoneNumberValid(gs_phone)) {
					String sql = "update chaobiao_data set user_phone="
							+ m_phone + ", zc_phone=" + "\"" + zc_phone + "\""
							+ " ,gs_phone=" + "\"" + gs_phone + "\""
							+ " where user_id=" + id + "";
					String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";
//					String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
					// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
					db = SQLiteDatabase.openOrCreateDatabase(path, null);
					// 查询数据返回游标对象
					db.execSQL(sql);
					db.close();
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

		// 显示电话,默认显示HOME 电话 如没有HOme 电话 显示其他
		phone_List = selectPhone(taskId);
		ll_phoneContainer.removeAllViews();
		addPhone();
	}

}
