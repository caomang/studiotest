package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.CustInfo;
import cn.sbx.deeper.moblie.domian.PerPhone;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.DatabaseHelperInstance;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 电话详情界面
 * 
 * 
 * 1,获取数据,不为空的加入到集合 2,设置适配器 显示电话 修改(保存按钮): 将获取到的数据保存至数据库,修改操作类型,然后重复步骤 1,刷新适配器
 * 删除: 将获取到的数据在数据库中设置为"" ,修改操作类型,然后重复步骤 1,刷新适配器 3,新增联系电话后 返回该页面 重复步骤 1,刷新适配器
 * 
 * 
 * 
 * 
 * 在Activity1里面写一个方法，例如getList（int i），这里int i是基于你只改你的图片，图片1,2,3，
 * 在MyAdapter里面的点击事件中掉用这个方法，Activity1.getList（i），这里你单机里面选择了第几张图片，I就为几，
 * 然后回去Activity1里面写这个getList（int i），getList这个方法的功能是这样接收用户选择的是第几张图片，
 * 然后更改list里面这个position的图片的值，然后adapter.notifyDataSetChanged();
 * 
 * 
 * 
 * 
 * @author terry.C
 * 
 */
@SuppressLint("ValidFragment")
public class PollingUserPhoneDetailFragment_new extends BaseFragment implements
		OnClickListener, IRefreshButtonAndText {

	private static final String String = null;
	private Context mContext;
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

	ArrayList<PerPhone> phone_List = null;
	public DatabaseHelper db;// 操作数据库的工具类
	private CustInfo custInfo;
	private String sql;
	private String sql1;
	private String del_sql;
	private ListView listView;
	private MyAdapter adapter;
	private String state;
	private SQLiteDatabase statement;
	private SQLiteDatabase instance;

	public PollingUserPhoneDetailFragment_new(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public PollingUserPhoneDetailFragment_new() {
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
		DatabaseHelperInstance databaseHelperInstance=new DatabaseHelperInstance();
		instance = databaseHelperInstance.getInstance(mActivity);

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
		selectPhone(taskId,mContext);
		// 获取该用户的上传状态
		getUpdataState(taskId);
	}

	private String getUpdataState(String taskId2) {
		String sql_getStateString = "select cmMrState from custInfo where accountId="
				+ "'" + taskId + "'" + "";

		try {
//			db=new DatabaseHelper(this.mContext);
//			statement = db.getWritableDatabase();

//			db = SQLiteData.openOrCreateDatabase(this.mContext);
//			statement = db.createStatement();
			Cursor states = instance.rawQuery(sql_getStateString,null);
			while (states.moveToNext()) {
				state = states.getString(1);
			}

			states.close();
//			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//			if (statement != null) {
//				try {
//					statement.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			if (db != null) {
//				try {
//					db.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
		}
		return state;
	}

	/*
	 * public ArrayList<PerPhone> selectPhone(String taskId) { phone_List = new
	 * ArrayList<PerPhone>(); String select_phone =
	 * "select sequence,phoneType,phone,extension from perPhone where accountId="
	 * + "'" + taskId + "'"; // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂)) db =
	 * SQLiteDatabase.openOrCreateDatabase(Constants.db_path, null); //
	 * 查询数据返回游标对象 Cursor phones = db.rawQuery(select_phone, null);
	 * 
	 * while (phones.moveToNext()) { phone.setSequence(phones.getString(0));
	 * phone.setPhoneType(phones.getString(1));
	 * phone.setPhone(phones.getString(2));
	 * phone.setExtension(phones.getString(3)); // 电话不为空 加入到集合 if
	 * (!phones.getString(2).equals("")) { phone_List.add(phone); } }
	 * phones.close(); db.close(); return phone_List;
	 * 
	 * }
	 */

	public ArrayList<PerPhone> selectPhone(String taskId,Context m) {
		phone_List = new ArrayList<PerPhone>();
		String select_phone = "select sequence,phoneType,phone,extension from perPhone where accountId="
				+ "'" + taskId + "'";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		try {
			db = new DatabaseHelper(m);
			statement = db.getWritableDatabase();
			// 查询数据返回游标对象
			Cursor phones = statement.rawQuery(select_phone,null);
			PerPhone phone = null;
			if(phones.moveToFirst()){
//				phone = new PerPhone();
//				phone.setSequence(phones.getString(0));
//				phone.setPhoneType(phones.getString(1));
//				phone.setPhone(phones.getString(2));
//				phone.setExtension(""+phones.getString(3));
//				// 电话不为空 加入到集合
//				if (!phones.getString(2).equals("")) {
//					phone_List.add(phone);
//				}
				phone = new PerPhone();
				phone.setSequence(phones.getString(0));
				phone.setPhoneType(phones.getString(1));
				phone.setPhone(phones.getString(2));
//					if(phones.getString(4)!=null||phones.getString(4)!="null"){
				phone.setExtension(phones.getString(3));
//					}

				// 电话不为空 加入到集合
				if (!phones.getString(2).equals("")) {
					phone_List.add(phone);
				}
				while (phones.moveToNext()) {

					phone = new PerPhone();
					phone.setSequence(phones.getString(0));
					phone.setPhoneType(phones.getString(1));
					phone.setPhone(phones.getString(2));
//					if(phones.getString(4)!=null||phones.getString(4)!="null"){
						phone.setExtension(phones.getString(3));
//					}

					// 电话不为空 加入到集合
					if (!phones.getString(2).equals("")) {
						phone_List.add(phone);
					}
				}
			}


			phones.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
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
		return phone_List;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cb_user_phone_info_layout_mine,
				container, false);

		tv_client_num = (TextView) view.findViewById(R.id.tv_client_num);
		tv_client_name = (TextView) view.findViewById(R.id.tv_client_name);
//		tv_client_style = (TextView) view.findViewById(R.id.tv_client_style);
		bt_add_newphone = (Button) view.findViewById(R.id.bt_add_newphone);
		bt_add_newphone.setOnClickListener(this);
//		tv_client_style_des = (TextView) view
//				.findViewById(R.id.tv_client_style_des);
		all_address = (TextView) view.findViewById(R.id.all_address);

		// 如果该用户已上传 电话不能修改 添加
		if (state.equals("2")) {

			bt_add_newphone.setVisibility(View.GONE);
		}

		listView = (ListView) view.findViewById(R.id.listView);
		if (phone_List.size() > 0) {
			listView.setVisibility(View.VISIBLE);
			adapter = new MyAdapter(phone_List);
			listView.setAdapter(adapter);
		} else {
			listView.setVisibility(View.GONE);
		}
		// adapter.getView(position, null, null);

		return view;
	}

	class MyAdapter extends BaseAdapter {

		private ArrayList<PerPhone> phones;

		public MyAdapter(ArrayList<PerPhone> list) {
			this.phones = list;
		}

		public void setListData(ArrayList<PerPhone> list) {
			this.phones = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return phones.size();
		}

		@Override
		public PerPhone getItem(int position) {
			return phones.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mContext,
					R.layout.item_approve_detail_clientphone, null);
			final EditText m_phone, zc_phone, gs_phone;
			final EditText tv_home_phone_num;
			EditText tv_company_phone_num;
			PerPhone perPhone;

			Button bt_home_save, bt_home_delete, bt_company_save, bt_company_delete, bt_save, bt_add_newphone;
			// 控件
			tv_home_phone_num = (EditText) view
					.findViewById(R.id.tv_home_phone_num);
			bt_home_save = (Button) view.findViewById(R.id.bt_home_save);
			tv_home_phone = (TextView) view.findViewById(R.id.tv_home_phone);
			bt_home_delete = (Button) view.findViewById(R.id.bt_home_delete);
			tv_home_phone_num.setTag(position);
			bt_home_save.setTag(position);
			bt_home_delete.setTag(position);

			// 如果该用户已上传 电话不能修改 添加
			if (state.equals("2")) {
				bt_home_save.setVisibility(View.INVISIBLE);
				bt_home_delete.setVisibility(View.INVISIBLE);
				tv_home_phone_num.setEnabled(false);
				
			}

			// 设置数据
			PerPhone phone = getItem(position);

			if (!phone.getPhone().equals("null")
					&& !TextUtils.isEmpty(phone.getPhone())) {
				// 按照数据库中的电话顺序进行显示电话类型 及电话

				if (phone.getPhoneType() != null
						&& phone.getPhoneType().equals("HOME")) {
					tv_home_phone.setText("住宅电话");
					tv_home_phone_num.setText(phone.getPhone());
				} else if (phone.getPhoneType() != null
						&& phone.getPhoneType().equals("HOME1")) {
					tv_home_phone.setText("电话");
					tv_home_phone_num.setText(phone.getPhone());
				} else if (phone.getPhoneType() != null
						&& phone.getPhoneType().equals("HOMEPHN")) {
					tv_home_phone.setText("住宅电话");
					tv_home_phone_num.setText(phone.getPhone());
				} else if (phone.getPhoneType() != null
						&& phone.getPhoneType().equals("CELL")) {
					tv_home_phone.setText("手机");
					tv_home_phone_num.setText(phone.getPhone());
				} else if (phone.getPhoneType() != null
						&& phone.getPhoneType().equals("BUSN")) {
					tv_home_phone.setText("工作电话");
					tv_home_phone_num.setText(phone.getPhone());
				} else if (phone.getPhoneType() != null
						&& phone.getPhoneType().equals("BUSNPHN")) {
					tv_home_phone.setText("工作电话");
					tv_home_phone_num.setText(phone.getPhone());
				}
				// 当前电话的序号
				final String code = phone.getSequence();

				// 单击修改按钮 ,修改数据库中的内容,然后重新查询数据库,设置适配器数据

				final int temp = position;
				bt_home_save.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// VIEW VIEW2 = ADAPTER.GETVIEW(TEMP, NULL, NULL);
						// EDITTEXT TV_HOME_PHONE_NUM = (EDITTEXT)
						// VIEW2.FINDVIEWBYID(R.ID.TV_HOME_PHONE_NUM);
						// 获取修改后的内容
						String phone_num = tv_home_phone_num.getText()
								.toString().trim();
						savePhoneNum(phone_num, code);
						// 重新获取数据 并刷新
						ArrayList<PerPhone> datas = selectPhone(taskId,mContext);
						adapter.setListData(datas);
					}
				});
				// 单击删除按钮 集合中将数据删除 数据库中将数据设置为"" ,然后重新获取数据并刷新
				bt_home_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 获取数据，并保存到本地数据库
						String delete_phone = tv_home_phone_num.getText()
								.toString().trim();
						deletePhoneNum(delete_phone, code);
						Toast.makeText(getActivity(), "号码已删除", Toast.LENGTH_LONG).show();
						// 曾 删 改 都要刷新适配器
						// 重新获取数据 并刷新
						ArrayList<PerPhone> datas = selectPhone(taskId,mContext);
						adapter.setListData(datas);
					}
				});

			}
			return view;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*
		 * case R.id.btn_next: String string = updateUserInfo(taskId,
		 * m_phone.getText().toString() .trim(),
		 * zc_phone.getText().toString().trim(), gs_phone
		 * .getText().toString().trim()); if (string.equals("m")) {
		 * Toast.makeText(getActivity(), "手机号码格式不正确", 1).show(); } else if
		 * (string.equals("zc")) { Toast.makeText(getActivity(),
		 * "请输入住宅电话 例如：010-88888888", 1) .show();
		 * 
		 * } else if (string.equals("gs")) { Toast.makeText(getActivity(),
		 * "请输入公司电话 例如：010-88888888", 1) .show(); } else {
		 * Toast.makeText(getActivity(), "提交成功", 1).show(); //
		 * selectList(taskId); } break;
		 */
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

	// 保存号码
	public void savePhoneNum(String phone, String a) {
		try {
			// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//			db = new DatabaseHelper(this.mContext);
//			statement = db.getWritableDatabase();
			if (!TextUtils.isEmpty(phone)) {
				// 更新
				sql1 = "update perPhone set phone =" + "'" + phone + "'"
						+ ",cmPhoneOprtType='20' where accountId=" + "'"
						+ taskId + "'" + "and  sequence =" + "'" + a + "'";
				instance.execSQL(sql1);
				Toast.makeText(getActivity(), "号码已保存", Toast.LENGTH_LONG).show();
			} else {

				Toast.makeText(getActivity(), "号码能为空", Toast.LENGTH_LONG).show();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), "号码保存失败", Toast.LENGTH_LONG).show();
		} finally {
//			if (statement != null) {
//				try {
//					statement.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//			if (db != null) {
//				try {
//					db.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
		}
	}

	// 删除号码
	public void deletePhoneNum(String phone, String b) {
		try {
			// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//			db = new DatabaseHelper(this.mContext);
//			statement = db.getWritableDatabase();
			if (phone == null) {
				Toast.makeText(mContext, "该号码不存在",Toast.LENGTH_SHORT).show();
			} else {

				del_sql = "update perPhone set phone ='',cmPhoneOprtType='30'  where accountId="
						+ "'" + taskId + "'" + "and  sequence = '" + b + "'";

				instance.execSQL(del_sql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//			if (statement != null) {
//				try {
//					statement.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//			if (db != null) {
//				try {
//					db.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		tv_client_num.setText(taskId);
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

	/*
	 * // 更新电话号码 public String updateUserInfo(String id, String m_phone, String
	 * zc_phone, String gs_phone) { if (isMobileNO(m_phone)) { if
	 * (isPhoneNumberValid(zc_phone)) { if (isPhoneNumberValid(gs_phone)) {
	 * String sql = "update chaobiao_data set user_phone=" + m_phone +
	 * ", zc_phone=" + "\"" + zc_phone + "\"" + " ,gs_phone=" + "\"" + gs_phone
	 * + "\"" + " where user_id=" + id + ""; String path =
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

		phone_List = selectPhone(taskId,mContext);
		if (phone_List != null && phone_List.size() > 0) {
			adapter = new MyAdapter(phone_List);
			listView.setAdapter(adapter);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(instance!=null){
			try{
				instance.close();
			}catch (Exception e){
				e.printStackTrace();
			}


		}

	}
}
