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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
public class AnJianDetailFormFragment_BaoJngQi extends BaseFragment implements
		IRefreshButtonAndText, OnClickListener {
	private EditText et_pinpai;
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
//		dicDate_WZ = getDicDate(taskId, "cmScLgfmWz");
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
				R.layout.layout_approve_detail_device_baojingqi, container,
				false);
		et_pinpai = (EditText) view.findViewById(R.id.et_pinpai);
		et_shijian_BJ = (TextView) view.findViewById(R.id.et_shijian_BJ);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("设备信息");
		et_shijian_BJ.setOnClickListener(this);
		btn_next = (Button) mActivity.findViewById(R.id.btn_next);
		btn_next.setVisibility(View.VISIBLE);
		btn_next.setText("确定");
		btn_next.setOnClickListener(this);

		return view;
	}
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 品牌
		// 保存表中的数据不为 "" ,显示保存表数据
		if (!TextUtils.isEmpty(save_pinPai)&&!save_pinPai.equals("null")) {
			et_pinpai.setText(save_pinPai);
		}
		// 保存表中无数据时,显示下载数据
		if (!TextUtils.isEmpty(custInfo.getCmScBjqPp())&&!custInfo.getCmScBjqPp().equals("null") && TextUtils.isEmpty(save_pinPai)) {
			et_pinpai.setText(custInfo.getCmScBjqPp());
		}
		// 日期
		// 保存表中的数据不为 "" ,显示保存表数据
		if (!TextUtils.isEmpty(save_riQi)&&!save_riQi.equals("null")) {
			et_shijian_BJ.setText(save_riQi);
		}
		// 保存表中无数据时,显示下载数据
		if (!TextUtils.isEmpty(custInfo.getCmScBjqSyrq()) &&!custInfo.getCmScBjqSyrq().equals("null")&& TextUtils.isEmpty(save_riQi)) {
			et_shijian_BJ.setText(custInfo.getCmScBjqSyrq());
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

	private Context mContext;
	private CustInfo_AnJian custInfo;
	private Button btn_next;
	private String pinPai;
	private String riQi;
	private String save_pinPai;
	private String save_riQi;
	private TextView et_shijian_BJ;

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
							et_shijian_BJ.setText(time);
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

		String string = "select distinct cmScBjqPp,cmScBjqSyrq from uploadcustInfo_aj where cmSchedId = '"
				+ custInfo.getCmSchedId()
				+ "' and accountId = '"
				+ taskId
				+ "'";
		try {
//			conne = SQLiteData.openOrCreateDatabase(getActivity());
//			state = conne.createStatement();

			Cursor execute = instance.rawQuery(string,new String[]{});

			if(execute.moveToFirst()){
				for (int i = 0; i <execute.getCount() ; i++) {
					save_pinPai = execute.getString(0);
					save_riQi = execute.getString(1);
					execute.moveToNext();
				}

			}

//			while (execute.next()) {
//				save_pinPai = execute.getString("cmScBjqPp");
//				save_riQi = execute.getString("cmScBjqSyrq");
//			}

		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 保存数据,首先判断要保存的数据和 客户表中的数据是否一致,如不同 在进行保存
	public void saveData(String userid) throws SQLException {

		if (custInfo.getCmScBjqPp() != null &&!custInfo.getCmScBjqPp().equals("null")
				&& !custInfo.getCmScBjqPp().equals(
						et_pinpai.getText().toString().trim())) {
			pinPai = et_pinpai.getText().toString().trim();
		}else if(TextUtils.isEmpty(custInfo.getCmScBjqPp())||custInfo.getCmScBjqPp().equals("null")){//没有下载数据
			pinPai = et_pinpai.getText().toString().trim();
		}
		if (custInfo.getCmScBjqSyrq() != null &&!custInfo.getCmScBjqSyrq().equals("null")
				&& !custInfo.getCmScBjqSyrq().equals(
						et_pinpai.getText().toString().trim())) {
			riQi = et_shijian_BJ.getText().toString().trim();
		}else if(TextUtils.isEmpty(custInfo.getCmScBjqSyrq())||custInfo.getCmScBjqSyrq().equals("null")){
			riQi = et_shijian_BJ.getText().toString().trim();//没有下载数据
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

			String sql = "insert into uploadcustInfo_aj (cmSchedId,accountId,cmScBjqPp,cmScBjqSyrq) "
					+ "values ("
					+ "'"
					+ custInfo.getCmSchedId()
					+ "',"
					+ "'"
					+ taskId
					+ "',"
					+ "'"
					+ pinPai
					+ "',"
					+ "'"
					+ riQi
					+ "')";

			instance.execSQL(sql);
		} else {
			// 表中存在该用户,更新

			String sql = "update  uploadcustInfo_aj set cmScBjqPp = '"
					+ pinPai + "',cmScBjqSyrq= '" + riQi + "' where  accountId=" + taskId + " and cmSchedId = "
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
