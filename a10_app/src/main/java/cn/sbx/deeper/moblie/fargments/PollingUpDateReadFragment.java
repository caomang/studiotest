package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunboxsoft.monitor.utils.DataIsNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CustInfo;
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
public class PollingUpDateReadFragment extends BaseFragment implements
		OnClickListener, IRefreshButtonAndText {

	Context mContext;
	private SinopecMenuModule menuModule;
	private IApproveBackToList backToList;
	private int targetContainer;
	private LayoutInflater layoutInflater;
	BaseActivity activity;
	private TextView user_no, user_name, all_address, tv_lastTime_maintainDate,
			tv_lastTime_securityDate;
	private TextView tv_lastTime_chaobiao_date, tv_lastTime_reading_type,
			tv_lastTime_readData, tv_lastTime_airMargin, tv_lastTime_consume;

	private EditText m_phone, zc_phone, gs_phone;
	private CustInfo custInfo;
	private String taskId;
	private String[] readType_des;
	private String[] readType_codes;
	private TextView tv_lishiqianfei;

	public PollingUpDateReadFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public PollingUpDateReadFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
		Bundle bundle = getArguments();
		if (bundle != null) {
			custInfo = (CustInfo) bundle.getSerializable("Last_save");
			taskId = custInfo.getAccountId();
		}
		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("提交");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.GONE);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("上次抄表信息");
		layoutInflater = getActivity().getLayoutInflater();
		// 获取数据字典中读数类型
		selectZD();

	}

	private void selectZD() {
		Connection db = null;
		Statement stata = null;
		try {
			db = SQLiteData.openOrCreateDatabase(getActivity());

			stata = db.createStatement();
			String sql1 = "select dictionaryDescr,dictionaryCode from dictionaries where parentID = "
					+ "'readType'" + "";

			ResultSet cursor = stata.executeQuery(sql1);
			cursor.last();

			readType_des = new String[cursor.getRow()];
			readType_codes = new String[cursor.getRow()];
			cursor.beforeFirst();
			int a = 0;
			while (cursor.next()) {
				readType_des[a] = cursor.getString("dictionaryDescr");
				readType_codes[a] = cursor.getString("dictionaryCode");
				a++;
			}
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
			if (stata != null) {
				try {
					stata.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.up_date_read_layout, container,
				false);

		tv_lastTime_chaobiao_date = (TextView) view
				.findViewById(R.id.tv_lastTime_chaobiao_date);
		tv_lastTime_reading_type = (TextView) view
				.findViewById(R.id.tv_lastTime_reading_type);
		tv_lastTime_readData = (TextView) view
				.findViewById(R.id.tv_lastTime_readData);
		tv_lastTime_airMargin = (TextView) view
				.findViewById(R.id.tv_lastTime_airMargin);
		tv_lastTime_consume = (TextView) view
				.findViewById(R.id.tv_lastTime_consume);

		tv_lastTime_maintainDate = (TextView) view
				.findViewById(R.id.tv_lastTime_maintainDate);
		tv_lastTime_maintainDate.setOnClickListener(this);
		tv_lastTime_securityDate = (TextView) view
				.findViewById(R.id.tv_lastTime_securityDate);
		tv_lishiqianfei = (TextView) view.findViewById(R.id.tv_lishiqianfei);
		tv_lastTime_securityDate.setOnClickListener(this);
		tv_lastTime_securityDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		tv_lastTime_maintainDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 赋值
		tv_lastTime_chaobiao_date.setText(custInfo.getCmMrLastMrDttm().substring(0, 10));
		// 与属于字典匹配读数类型
		if (readType_des.length > 0) {

			for (int i = 0; i < readType_des.length; i++) {
				if (custInfo.getReadType().equals(readType_codes[i])) {
					tv_lastTime_reading_type.setText(readType_des[i]);
				}
			}
		} else {
			tv_lastTime_reading_type.setText("普通");
		}

		tv_lastTime_readData.setText(custInfo.getCmMrLastMr());
		tv_lastTime_airMargin.setText(custInfo.getCmMrLastVol());
		tv_lastTime_consume.setText(custInfo.getCmMrLastDebt());
		tv_lastTime_airMargin.setText(custInfo.getCmMrLastVol());
		String str = custInfo.getCmMrDebtStatDt();
		if (str == null || str.equals("null")) {
			str = "暂无数据";
		}
		// tv_lishiqianfei.setText("截至:"+str+","+"\n欠费金额："+custInfo.getCmMrLastBal()+","+"\n滞纳金:"+custInfo.getCmMrOverdueAmt());
		tv_lishiqianfei.setText(custInfo.getCmMrLastBal() + " <正为欠费,负为余额>");

		// tv_lastTime_securityDate.setText(custInfo.getCmMrLastSecchkDt());

		DataIsNull.setValue(tv_lastTime_securityDate,
				custInfo.getCmMrLastSecchkDt());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Fragment f = null;
		Bundle bundle = null;
		switch (v.getId()) {
		case R.id.tv_lastTime_maintainDate:
			f = new AnJianZuiJINFragment();
			bundle = new Bundle();
			bundle.putString("taskId", taskId);
			bundle.putString("device_name", "维修");
			f.setArguments(bundle);
			f.setTargetFragment(PollingUpDateReadFragment.this, 0);
			((ActivityInTab) mActivity).navigateTo(f);
			break;
		case R.id.tv_lastTime_securityDate:
			/*
			 * f = new AnJianZuiJINFragment(); bundle = new Bundle();
			 * bundle.putString("taskId", taskId);
			 * bundle.putString("device_name", "安检"); f.setArguments(bundle);
			 * f.setTargetFragment(PollingUpDateReadFragment.this, 0);
			 * ((ActivityInTab) mActivity).navigateTo(f);
			 */
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

}
