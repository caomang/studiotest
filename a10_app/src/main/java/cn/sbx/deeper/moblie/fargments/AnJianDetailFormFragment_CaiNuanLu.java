package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.sbx.deeper.moblie.activity.TimePickerActivity;
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
public class AnJianDetailFormFragment_CaiNuanLu extends BaseFragment implements
		IRefreshButtonAndText, OnClickListener {
	private EditText et_guanjing;
	private Spinner sp_paifang;
	private String code_CZ;
	private String code_WZ;
	private TextView et_ksri;
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
		dicDate_PF = getDicDate(taskId, "cmScCnlPffs");
		mContext = getActivity();
		// 获取保存表中的数据,回显
		getUpDate(taskId);
	}

	@Override
	public void onResume() {
		super.onResume();
		getUpDate(taskId);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.layout_approve_detail_device_cainuanlu, container,
				false);
		et_guanjing = (EditText) view.findViewById(R.id.et_guanjing);
		sp_paifang = (Spinner) view.findViewById(R.id.sp_paifang);
		et_ksri = (TextView) view.findViewById(R.id.et_shijian_BJ);
		btn_next = (Button) mActivity.findViewById(R.id.btn_next);
		btn_next.setVisibility(View.VISIBLE);
		btn_next.setText("确定");
		btn_next.setOnClickListener(this);
		et_ksri.setOnClickListener(this);

		String[] strings = dicDate_PF.get(1);

		// 位置
		sp_paifang
				.setBackgroundResource(R.drawable.ic_approve_spinner_background);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_item, strings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_paifang.setAdapter(adapter);
		// 有下载数据时
		if (!TextUtils.isEmpty(custInfo.getCmScRsqPffs())
				&& custInfo.getCmScZjXhbh() != null
				&& !custInfo.getCmScZjXhbh().equals(null)) {
			// 如果保存表数据为"" ,显示下载数据,否则显示保存表数据
			if (TextUtils.isEmpty(save_fs)) {
				for (int a = 0; a < dicDate_PF.get(0).length; a++) {
					if (dicDate_PF.get(0)[a].equals(custInfo.getCmScZjXhbh())) {
						sp_paifang.setSelection(a);

					}
				}
			} else {
				// 保存表不为空,回显保存表说明改变类型了
				for (int a = 0; a < dicDate_PF.get(0).length; a++) {
					if (dicDate_PF.get(0)[a].equals(save_fs)) {
						sp_paifang.setSelection(a);

					}
				}
			}

		} else {
			// 没有下载数据,显示保存表,且保存表有数据
			if (!TextUtils.isEmpty(save_fs)) {
				for (int a = 0; a < dicDate_PF.get(0).length; a++) {
					if (dicDate_PF.get(0)[a].equals(save_fs)) {
						sp_paifang.setSelection(a);

					}
				}
			}

		}

		sp_paifang.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				code_WZ = dicDate_PF.get(0)[position];

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});


		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

//		if (save_pp==""||save_pp==null) {
//			if (custInfo.getCmScCnlPp().equals("null")) {
//				et_guanjing.setText("");
//			}
//			et_guanjing.setText(custInfo.getCmScCnlPp());
//		}
//		et_guanjing.setText(save_pp);
//		if (save_rq==""||save_rq==null) {
//			if (custInfo.getCmScCnlSyrq().equals("null")) {
//				et_ksri.setText("");
//			}
//			et_ksri.setText(custInfo.getCmScCnlSyrq());
//		}
//		et_ksri.setText(save_rq);
		
		
		
		
//		 保存表中数据
		if (!TextUtils.isEmpty(save_pp)&&!save_pp.equals("null")) {
			et_guanjing.setText(save_pp);
		}else if (!TextUtils.isEmpty(custInfo.getCmScCnlPp())&&!custInfo.getCmScCnlPp().equals("null") ) {
			// 保存表中无数据时,显示下载数据
			et_guanjing.setText(custInfo.getCmScCnlPp());
		}
//		 保存表中数据  日期
		if (!TextUtils.isEmpty(save_rq)&&!save_rq.equals("null")) {
			et_ksri.setText(save_rq);
		}else if (!TextUtils.isEmpty(custInfo.getCmScCnlSyrq())&&!custInfo.getCmScCnlSyrq().equals("null") ) {
			// 保存表中无数据时,显示下载数据
			et_ksri.setText(custInfo.getCmScCnlSyrq());
		}
		
	}

	// 查询待办数据
//	public Connection db;// 操作数据库的工具类
	StringBuilder sb;
//	private Connection conne;
//	private Statement state;
//	private ResultSet c;
	private String taskId;
	private String device_name;

	private ArrayList<String[]> arrayList;
	private ArrayList<String[]> dicDate_PF;
	private Context mContext;
	private CustInfo_AnJian custInfo;
	private Button btn_next;
	private String guanjing = "";
	private String wZ = "";
	private String cZ = "";
	private String save_GJ = "";
	private String save_Wz = "";
	private String save_Cz = "";
	private String save_pp= "";
	private String save_fs= "";
	private String save_rq= "";

	private ArrayList<String[]> getDicDate(String taskId, String parendID) {
		String[] codeStrings;
		String[] descStrings;
		String describe = "";
		Cursor set = null;
		arrayList = new ArrayList<String[]>();
		try {
			String sql_dic = "select dictionaryCode,dictionaryDescr from dictionaries where  parentID = '"
					+ parendID + "'";
			set = instance.rawQuery(sql_dic,new String[]{});
//			set.last();
			codeStrings = new String[set.getCount()];
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
				return;
			}
			Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
			break;

		case R.id.et_shijian_BJ:
			startActivityForResult(new Intent(mContext,
					TimePickerActivity.class), 100);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==100&&resultCode == Activity.RESULT_OK){
			if(data != null){
				String time = data.getStringExtra("CHEEL_TIME");
				 if(!TextUtils.isEmpty(time)){
				try {
					 SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd");
						Date beginDate=dd.parse(time);
						String format = dd.format(new Date());
						Date enDate = dd.parse(format);
						
						long day = (enDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
						if(day>=0){
							et_ksri.setText(time);
						}else{
							Toast.makeText(mContext, "所选时间已超出当前日期", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
				
					}
			}
		}
	}

	// 获取保存表中的数据
	public void getUpDate(String taskId) {

		String string = "select distinct cmScCnlPp,cmScCnlPffs,cmScCnlSyrq from uploadcustInfo_aj where cmSchedId = '"
				+ custInfo.getCmSchedId()
				+ "' and accountId = '"
				+ taskId
				+ "'";
		try {

			Cursor execute = instance.rawQuery(string,new String[]{});
			if(execute.moveToFirst()){
				for (int i = 0; i <execute.getCount() ; i++) {
					save_pp = execute.getString(0);
					save_fs = execute.getString(1);
					save_rq = execute.getString(2);
					execute.moveToNext();
				}
			}

//			while (execute.next()) {
//				save_pp = execute.getString("cmScCnlPp");
//				save_fs = execute.getString("cmScCnlPffs");
//				save_rq = execute.getString("cmScCnlSyrq");
//			}

		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 保存数据,首先判断要保存的数据和 客户表中的数据是否一致,如不同 在进行保存
	public void saveData(String userid) throws SQLException {

		if (custInfo.getCmScCnlPp() != null &&custInfo.getCmScCnlPp().equals("null")
				&& !custInfo.getCmScCnlPp().equals(
						et_guanjing.getText().toString().trim())) {
			guanjing = et_guanjing.getText().toString().trim();
		}
		if (custInfo.getCmScCnlPffs() != null&&custInfo.getCmScCnlPffs().equals("null")
				&& !custInfo.getCmScCnlPffs().equals(code_WZ)) {
			wZ = code_WZ;
		}
		if (custInfo.getCmScCnlSyrq() != null&&custInfo.getCmScCnlSyrq().equals("null")
				&& !custInfo.getCmScCnlSyrq().equals(code_CZ)) {
			cZ = et_ksri.getText().toString().trim();
		}

		String sql_isSave = "select * from uploadcustInfo_aj where  accountId='"
				+ taskId
				+ "' and cmSchedId = '"
				+ custInfo.getCmSchedId()
				+ "'";
		Cursor executeQuery = null;
//		conne = SQLiteData.openOrCreateDatabase(getActivity());
//
//		state = conne.createStatement();

		executeQuery = instance.rawQuery(sql_isSave,new String[]{});

		if (!executeQuery.moveToNext()) {
			// 表中不存在该用户信息,插入

			String sql = "insert into uploadcustInfo_aj (cmSchedId,accountId,cmScCnlPp,cmScCnlPffs,cmScCnlSyrq) "
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

			String sql = "update  uploadcustInfo_aj set cmScCnlPp = '"
					+ guanjing + "',cmScCnlPffs= '" + wZ + "', cmScCnlSyrq= '"
					+ cZ + "' where  accountId=" + taskId + " and cmSchedId = "
					+ custInfo.getCmSchedId() + "";
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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (instance != null) {
			instance.close();
		}
	}

}
