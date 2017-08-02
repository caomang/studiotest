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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CmScYhzg;
import cn.sbx.deeper.moblie.domian.CmScheType;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.UploadcustInfo_AnJian;
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
public class AnJianWorkingConditionFragment extends BaseFragment implements
		OnClickListener, IRefreshButtonAndText {

	Context mContext;
	private SinopecMenuModule menuModule;
	private String taskId;
	private IApproveBackToList backToList;
	private int targetContainer;
	private LayoutInflater layoutInflater;
	BaseActivity activity;
	private TextView tv_plan_time, user_name ;
	private EditText m_phone, zc_phone, gs_phone,hidden_danger_list_no;
	private Connection conne;
	private Statement state;
	private CustInfo_AnJian custInfo;
	UploadcustInfo_AnJian uploadcustInfo = null;//上传表中单号

	private String plan;

	public AnJianWorkingConditionFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public AnJianWorkingConditionFragment() {
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
			custInfo = (CustInfo_AnJian) bundle.getSerializable("custinfo");
		}
		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("提交");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.VISIBLE);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("本次安检情况");
		layoutInflater = getActivity().getLayoutInflater();
		// accountInfos = getAccountInfos(taskId);
		uploadcustInfo=getcmScZgtzdDate();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.anjian_working_condition_layout,
				container, false);
		spinner_anjian_plan_type1 = (TextView) view
				.findViewById(R.id.spinner_anjian_plan_type1);
		spinner_hidden_danger_now = (Spinner) view
				.findViewById(R.id.spinner_hidden_danger_now);
		tv_plan_time = (TextView) view.findViewById(R.id.tv_plan_time);
		user_name = (TextView) view.findViewById(R.id.user_name);
		hidden_danger_list_no = (EditText) view
				.findViewById(R.id.hidden_danger_list_no);

		user_name.setText("14002");
		tv_plan_time.setText(getSchedTime(custInfo.getCmSchedId()).substring(0, 10).toString());
		if(uploadcustInfo!=null){
			hidden_danger_list_no.setText(uploadcustInfo.getCmScZgtzd());
		}
		
		spinner_anjian_plan_type1.setText(getJHdata()!=null?getJHdata():"常规计划安检");
		
//
		// user_no = (TextView) view.findViewById(R.id.user_no);
		// user_name = (TextView) view.findViewById(R.id.user_name);
		return view;
	}

	private String getAccountInfos(String taskId2) {
		String sql = "select username from loginUser where userID = " + taskId2
				+ "";
		ResultSet set = null;
		try {
			conne = SQLiteData.openOrCreateDatabase(getActivity());
			state = conne.createStatement();
			set = state.executeQuery(sql);

			while (set.next()) {
				userName = set.getString("username");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (set != null) {
				try {
					set.close();
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
			if (conne != null) {
				try {
					conne.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}

		return userName;
	}
	
	// 查询上传表
		public UploadcustInfo_AnJian getcmScZgtzdDate() {
			String sql1 = "select distinct cmScZgtzd,cmScYhzg from uploadcustInfo_aj where accountId ='"
					+custInfo.getAccountId() + "' and cmSchedId = '" + custInfo.getCmSchedId()
					+ "' ";
			ResultSet DicSet = null;
			UploadcustInfo_AnJian uploadcustIn = null;
			try {
				conne = SQLiteData.openOrCreateDatabase(getActivity());
				state = conne.createStatement();
				DicSet = state.executeQuery(sql1);
				while (DicSet.next()) {
					uploadcustIn = new UploadcustInfo_AnJian();
					uploadcustIn.cmScZgtzd = DicSet.getString("cmScZgtzd");
					uploadcustIn.cmScYhzg = DicSet.getString("cmScYhzg");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				if (DicSet != null) {
					try {
						DicSet.close();
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
				if (conne != null) {
					try {
						conne.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			}
			return uploadcustIn;

		};

	
	private String getSchedTime(String schedid) {
		String sql = "select scheduleDateTimeStart from schedInfo_aj where cmSchedId = " + schedid
				+ "";
		ResultSet set = null;
		try {
			conne = SQLiteData.openOrCreateDatabase(getActivity());
			state = conne.createStatement();
			set = state.executeQuery(sql);

			while (set.next()) {
				schedTime = set.getString("scheduleDateTimeStart");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (set != null) {
				try {
					set.close();
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
			if (conne != null) {
				try {
					conne.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}

		return schedTime;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:

			String danhao = hidden_danger_list_no.getText().toString().trim();
			
			if(!TextUtils.isEmpty(danhao) && !TextUtils.isEmpty(code)){
			String sql_isSave = "select * from uploadcustInfo_aj where  accountId='" 
					+ custInfo.getAccountId() + "' and cmSchedId = '" + custInfo.getCmSchedId() + "'";
			ResultSet executeQuery = null;
			try {
				conne = SQLiteData.openOrCreateDatabase(getActivity());

				state = conne.createStatement();

				executeQuery = state.executeQuery(sql_isSave);

				if (!executeQuery.next()) {
					// 表中不存在该用户,插入

					String sql = "insert into uploadcustInfo_aj (accountId,cmSchedId,cmScYhzg,cmScZgtzd) "
							+ "values ('"+custInfo.getAccountId()+"','"+custInfo.getCmSchedId()+"','"+code+"',' "+danhao+"')";

					state.executeUpdate(sql);
				} else {
					// 表中存在该用户,更新

					String sql = "update  uploadcustInfo_aj set cmScYhzg = '"
							+ code + "' ,cmScZgtzd = '"+danhao+"' where accountId = '"+custInfo.getAccountId()+"' and  cmSchedId='"+custInfo.getCmSchedId()+"'";
					state.executeUpdate(sql);
				}
				Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
			} catch (SQLException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "提交失败",  Toast.LENGTH_SHORT).show();
			} finally {
				if (executeQuery != null) {
					try {
						executeQuery.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
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
				if (conne != null) {
					try {
						conne.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			}else{
				Toast.makeText(mContext, "请输入隐患整改通知单号",  Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	private String code;
	@Override
	public void onResume() {
		super.onResume();
		// selectList(taskId);
		cmScYhzgdata = getCmScYhzgdata();

		String[] options = new String[cmScYhzgdata.size()] ;
		for(int i= 0;i<cmScYhzgdata.size();i++){
			options[i]=cmScYhzgdata.get(i).getCmScYhzgDescr();
			if(uploadcustInfo!=null&& uploadcustInfo.getCmScYhzg().equals(cmScYhzgdata.get(i).getCmScYhzg())){
				isCheck = i;
			}
		}

		ArrayAdapter<String> adapter_isModification = new ArrayAdapter<String>(
				mContext, R.layout.spinner_item, options);

		adapter_isModification
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_hidden_danger_now
				.setBackgroundResource(R.drawable.ic_approve_spinner_background);

		spinner_hidden_danger_now.setAdapter(adapter_isModification);
		spinner_hidden_danger_now.setSelection(isCheck);
		spinner_hidden_danger_now.setOnItemSelectedListener(new OnItemSelectedListener() {

			

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				code = cmScYhzgdata.get(arg2).getCmScYhzg();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		

	}

	
	// 客户安全讲解情况字典
	public ArrayList<CmScYhzg> getCmScYhzgdata() {
		String selectDic = "select * from dictionaries where parentID = 'cmScYhzg' ";
		ResultSet DicSet = null;
		CmScYhzg cmScYhzg = null;
		ArrayList<CmScYhzg> cmScSpItem_list = new ArrayList<CmScYhzg>();
		try {
			conne = SQLiteData.openOrCreateDatabase(getActivity());
			state = conne.createStatement();
			DicSet = state.executeQuery(selectDic);
			while (DicSet.next()) {
				cmScYhzg = new CmScYhzg();
				cmScYhzg.cmScYhzg = DicSet.getString("dictionaryCode");
				cmScYhzg.cmScYhzgDescr = DicSet
						.getString("dictionaryDescr");
				cmScSpItem_list.add(cmScYhzg);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (DicSet != null) {
				try {
					DicSet.close();
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
			if (conne != null) {
				try {
					conne.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return cmScSpItem_list;

	};


	//安检计划类型字典
	public String getJHdata() {
		
		String plan = "select cmScTypeCd from schedInfo_aj where cmSchedId = '"+custInfo.getCmSchedId()+"' ";
		ResultSet DicSet = null;
		CmScYhzg cmScYhzg = null;
		ArrayList<CmScheType> cmScType_list = new ArrayList<CmScheType>();
		try {
			conne = SQLiteData.openOrCreateDatabase(getActivity());
			state = conne.createStatement();
			ResultSet resultSet = state.executeQuery(plan);
			if (resultSet.next()) {
				str = resultSet.getString("cmScTypeCd");
			}

			String selectDic = "select dictionaryDescr from dictionaries where parentID = 'cmScType' and cmScTypeCd = '"+str+"'";
			type_des = state.executeQuery(selectDic);
			if(type_des.next()){
				type = type_des.getString("dictionaryDescr");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (DicSet != null) {
				try {
					DicSet.close();
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
			if (conne != null) {
				try {
					conne.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return type;

	};
	
	// 查询待办数据
	public SQLiteDatabase db;// 操作数据库的工具类
	StringBuilder sb;
	private TextView spinner_anjian_plan_type1;
	private Spinner spinner_hidden_danger_now;
	private String logname;
	private String accountInfos;
	private String userName;
	private String schedTime;
	private ArrayList<CmScYhzg> cmScYhzgdata;
	private ArrayList<CmScheType> jHdata;
	private String str;
	private ResultSet type_des;
	private String type;
	private int isCheck;

	public void selectList(String user_id) {
		String sql = "select distinct * from anjian_data where user_id=" + "'"
				+ user_id + "'";
		String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";
//		String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		db = SQLiteDatabase.openOrCreateDatabase(path, null);
		// 查询数据返回游标对象
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			// 通过游标对象获取值

			tv_plan_time.setText(c.getString(0));
			user_name.setText(c.getString(1));
			if (c.getString(3).contains("手机")) {
				m_phone.setText(c.getString(3).substring(3,
						c.getString(3).length()));
			} else {
				m_phone.setText(c.getString(3));
			}
			zc_phone.setText(c.getString(46));
			gs_phone.setText(c.getString(47));
		}
		c.close();
		db.close();
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
}
