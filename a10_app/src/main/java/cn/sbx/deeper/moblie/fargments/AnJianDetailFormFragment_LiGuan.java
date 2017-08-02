package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
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
public class AnJianDetailFormFragment_LiGuan extends BaseFragment implements
		IRefreshButtonAndText, OnClickListener {
	private EditText et_guanjing;
	private Spinner sp_location, sp_caizhi;
	private String code_CZ;
	private String code_WZ;
	DatabaseHelperInstance databaseHelperInstance;
	SQLiteDatabase instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			custInfo = (CustInfo_AnJian) bundle.getSerializable("custinfo");
			taskId = bundle.getString("id");
			device_name = bundle.getString("device_name");
		}
		databaseHelperInstance = new DatabaseHelperInstance();

		instance = databaseHelperInstance.getInstance(getActivity());
		// 数据字典
		dicDate_WZ = getDicDate(taskId, "cmScLgfmWz");
		mContext = getActivity();



		// 获取保存表中的数据,回显
		getUpDate(taskId);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		getUpDate(taskId);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.layout_approve_detail_device_liguanfamen, container,
				false);
		et_guanjing = (EditText) view.findViewById(R.id.et_guanjing);
		sp_location = (Spinner) view.findViewById(R.id.sp_location);
		sp_caizhi = (Spinner) view.findViewById(R.id.sp_caizhi);
		btn_next = (Button) mActivity.findViewById(R.id.btn_next);
		btn_next.setVisibility(View.VISIBLE);
		btn_next.setText("确定");
		btn_next.setOnClickListener(this);

		String[] strings = dicDate_WZ.get(1);

		// 位置
		sp_location
				.setBackgroundResource(R.drawable.ic_approve_spinner_background);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_item, strings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_location.setAdapter(adapter);
		sp_location.setSelection(1);
		// 有下载数据时
		if (!TextUtils.isEmpty(custInfo.getCmScLgfmWz())&&!custInfo.getCmScLgfmWz().equals("null")) {
			// 如果保存表数据为"" ,显示下载数据,否则显示保存表数据
			if (TextUtils.isEmpty(save_Wz)) {
				for (int a = 0; a < dicDate_WZ.get(0).length; a++) {
					if (dicDate_WZ.get(0)[a] .equals(custInfo.getCmScLgfmWz()) ) {
						sp_location.setSelection(a);

					}
				}
			} else {
				// 保存表不为空,回显保存表
				for (int a = 0; a < dicDate_WZ.get(0).length; a++) {
					if (dicDate_WZ.get(0)[a] .equals(save_Wz) ) {
						sp_location.setSelection(a);
					}
				}
			}

		} else {
			// 没有下载数据,显示保存表,且保存表有数据
			if (!TextUtils.isEmpty(save_Wz)) {
				for (int a = 0; a < dicDate_WZ.get(0).length; a++) {
					if (dicDate_WZ.get(0)[a].equals(save_Wz)) {
						sp_location.setSelection(a);

					}
				}
			}
		}

		sp_location.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				code_WZ = dicDate_WZ.get(0)[position];

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		// 材质
		final ArrayList<String[]> dicDate_CZ = getDicDate(taskId, "cmScLgfmCz");
		String[] strings2 = dicDate_CZ.get(1);
		// 位置
		sp_caizhi
				.setBackgroundResource(R.drawable.ic_approve_spinner_background);

		ArrayAdapter<String> adapter_caizhi = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_item, strings2);
		adapter_caizhi
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_caizhi.setAdapter(adapter_caizhi);

		// 有下载数据时
		if (!TextUtils.isEmpty(custInfo.getCmScLgfmCz())&&custInfo.getCmScLgfmCz()!=null&&!custInfo.getCmScLgfmCz().equals("null")) {
			// 如果保存表数据为"" ,显示下载数据,否则显示保存表数据
			if (TextUtils.isEmpty(save_Cz)) {
				for (int a = 0; a < dicDate_CZ.get(0).length; a++) {
					if (dicDate_CZ.get(0)[a].equals(custInfo.getCmScLgfmCz()) ) {
						sp_caizhi.setSelection(a);

					}
				}
			} else {
				// 保存表不为空,回显保存表说明改变类型了
				for (int a = 0; a < dicDate_CZ.get(0).length; a++) {
					if (dicDate_CZ.get(0)[a] .equals(save_Cz) ) {
						sp_caizhi.setSelection(a);

					}
				}
			}

		} else {
			// 没有下载数据,显示保存表,且保存表有数据
			if (!TextUtils.isEmpty(save_Cz)) {
				for (int a = 0; a < dicDate_CZ.get(0).length; a++) {
					if (dicDate_CZ.get(0)[a] .equals(save_Cz)) {
						sp_caizhi.setSelection(a);

					}
				}
			}

		}

		sp_caizhi.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				code_CZ = dicDate_CZ.get(0)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 管径
		// 保存表中的数据不为 "" ,显示保存表数据
		if (!TextUtils.isEmpty(save_GJ) && !save_GJ.equals("null")) {
			et_guanjing.setText(save_GJ);
		}
		// 保存表中无数据时,显示下载数据
		if (!TextUtils.isEmpty(custInfo.getCmScLgfmGj())&& !custInfo.getCmScBjqPp().equals("null")&& TextUtils.isEmpty(save_GJ)) {
			et_guanjing.setText(custInfo.getCmScLgfmGj());
		}
	}

	// 查询待办数据
	public Connection db;// 操作数据库的工具类
	StringBuilder sb;
//	private Connection conne;
//	private Statement state;
//	private ResultSet c;
	private String taskId;
	private String device_name;

	private ArrayList<String[]> arrayList;
	private ArrayList<String[]> dicDate_WZ;
	private Context mContext;
	private CustInfo_AnJian custInfo;
	private Button btn_next;
	private String guanjing ;
	private String wZ ;
	private String cZ ;
	private String save_GJ;
	private String save_Wz ;
	private String save_Cz ;

	private ArrayList<String[]> getDicDate(String taskId, String parendID) {
		String[] codeStrings;
		String[] descStrings;
//		DatabaseHelperInstance openOrCreateDatabase = null;
//		SQLiteDatabase stat = null;
		String describe = "";
		Cursor set = null;
		arrayList = new ArrayList<String[]>();
		try {
//			 openOrCreateDatabase=new DatabaseHelperInstance();
//			 stat = openOrCreateDatabase.getInstance(getActivity());

//			openOrCreateDatabase = SQLiteData.openOrCreateDatabase(getActivity());
//			stat = openOrCreateDatabase.createStatement();
			String sql_dic = "select dictionaryCode,dictionaryDescr from dictionaries where  parentID = '"
					+ parendID + "'";
			set = instance.rawQuery(sql_dic,new String[]{});
//			set.last();
			codeStrings = new String[set.getCount()];
//			codeStrings =set.getString(0);
			descStrings = new String[set.getCount()];
//			set.beforeFirst();
			int a = 0;
			if(set.moveToFirst()){
				codeStrings[a] = set.getString(0);
				descStrings[a] = set.getString(1);
				a++;
				while (set.moveToNext()) {
					codeStrings[a] = set.getString(0);
					descStrings[a] = set.getString(1);
					a++;
				}
			}

			arrayList.add(codeStrings);
			arrayList.add(descStrings);
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (set != null) {
				try {
					set.close();
				} catch (SQLiteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return arrayList;
	}

	@Override
	public void onClick(View v) {

		// 确定,立管信息插入到保存表中
		switch (v.getId()) {
		case R.id.btn_next:
			try {
				saveData(taskId);
			} catch (SQLException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

	}

	// 获取保存表中的数据
	public void getUpDate(String taskId) {

		String string = "select distinct cmScLgfmGj,cmScLgfmWz,cmScLgfmCz from uploadcustInfo_aj where cmSchedId = '"
				+ custInfo.getCmSchedId()
				+ "' and accountId = '"
				+ taskId
				+ "'";
		try {
//			conne = SQLiteData.openOrCreateDatabase(getActivity());
//			state = conne.createStatement();
			Cursor execute = instance.rawQuery(string, new String[]{});



			if (execute.moveToFirst()){
				for (int i = 0; i < execute.getCount(); i++) {

					save_GJ = execute.getString(0);
					save_Wz = execute.getString(1);
					save_Cz = execute.getString(2);
					execute.moveToNext();
				}

			}
//			while (execute.next()) {
//				save_GJ = execute.getString("cmScLgfmGj");
//				save_Wz = execute.getString("cmScLgfmWz");
//				save_Cz = execute.getString("cmScLgfmCz");
//			}

		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 保存数据,首先判断要保存的数据和 客户表中的数据是否一致,如不同 在进行保存
	public void saveData(String userid) throws SQLException {

		if (custInfo.getCmScLgfmGj() != null &&!custInfo.getCmScLgfmGj().equals("null")
				&& !custInfo.getCmScLgfmGj().equals(
						et_guanjing.getText().toString().trim())) {
			guanjing = et_guanjing.getText().toString().trim();
		}else{
			guanjing = et_guanjing.getText().toString().trim(); //没有下载数据,直接保存该数据
		}
		
		
		if (custInfo.getCmScLgfmWz() != null&&!custInfo.getCmScLgfmWz().equals("null")
				&& !custInfo.getCmScLgfmWz().equals(code_WZ)) {
			wZ = code_WZ;
		}else{
			wZ = code_WZ;
		}
		
		if (custInfo.getCmScLgfmCz() != null&&custInfo.getCmScLgfmCz().equals("null")
				&& !custInfo.getCmScLgfmCz().equals(code_CZ)) {
			cZ = code_CZ;
		}else{
			cZ = code_CZ;
		}

		String sql_isSave = "select * from uploadcustInfo_aj where  accountId='"
				+ taskId
				+ "' and cmSchedId = '"
				+ custInfo.getCmSchedId()
				+ "'";
//		ResultSet executeQuery = null;
//		conne = SQLiteData.openOrCreateDatabase(getActivity());
//
//		state = conne.createStatement();

		Cursor executeQuery = instance.rawQuery(sql_isSave,new String[]{});

		if (!executeQuery.moveToNext()) {
			// 表中不存在该用户信息,插入

			String sql = "insert into uploadcustInfo_aj (cmSchedId,accountId,cmScLgfmGj,cmScLgfmWz,cmScLgfmCz) "
					+ "values ("
					+ "'"
					+ custInfo.getCmSchedId()
					+ "',"
					+ "'"
					+ taskId
					+ "',"
					+ "'"
					+ guanjing
					+ "',"
					+ "'"
					+ wZ
					+ "',"
					+ "'" + cZ + "')";

			instance.execSQL(sql);
		} else {
			// 表中存在该用户,更新

			String sql = "update  uploadcustInfo_aj set cmScLgfmGj = '"
					+ guanjing + "',cmScLgfmWz= '" + wZ + "', cmScLgfmCz= '"
					+ cZ + "' where  accountId='" + taskId + "' and cmSchedId = '"
					+ custInfo.getCmSchedId() + "'";
			instance.execSQL(sql);

		}
		if (executeQuery != null) {
			try {
				executeQuery.close();
			} catch (SQLiteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		if (state != null) {
//			try {
//				state.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if (conne != null) {
//			try {
//				conne.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(instance!=null){
			instance.close();
		}
	}
}
