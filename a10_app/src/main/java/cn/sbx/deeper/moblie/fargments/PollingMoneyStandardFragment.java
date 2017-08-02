package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 服务点/计量表历史记录标识 详情
 * 
 * 
 * 
 */
@SuppressLint("ValidFragment")
public class PollingMoneyStandardFragment extends BaseFragment implements
		OnClickListener, IRefreshButtonAndText {

	Context mContext;
	private SinopecMenuModule menuModule;
	private String taskId;
	private IApproveBackToList backToList;
	private int targetContainer;
	private LayoutInflater layoutInflater;
	BaseActivity activity;
	private TextView user_no, user_name, all_address;
	private EditText m_phone, zc_phone, gs_phone;
	private String qiliang = "";
	private String qifei = "";

	public PollingMoneyStandardFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	public PollingMoneyStandardFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
		Bundle bundle = getArguments();

		if (bundle != null) {
			qiliang = bundle.getString("qiliang");
			qifei = bundle.getString("qifei");
		}

		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("提交");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.GONE);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("添加附件");
		layoutInflater = getActivity().getLayoutInflater();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_oplling_moneystandard,
				container, false);

		TextView tv_qifei = (TextView) view.findViewById(R.id.tv_qifei);
		TextView tv_qiliang = (TextView) view.findViewById(R.id.tv_qiliang);
		TextView tv_qiliang_heji = (TextView) view.findViewById(R.id.tv_qiliang_heji);
		TextView tv_qifei_heji = (TextView) view.findViewById(R.id.tv_qifei_heji);

		tv_qiliang.setText(qiliang);
		tv_qifei.setText(qifei);
		tv_qiliang_heji.setText(qiliang);
		tv_qifei_heji.setText(qifei);

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

}
