package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 服务点/计量表历史记录标识 详情
 * 
 * 
 * 
 */
@SuppressLint("ValidFragment")
public class ApplicationNotOnLineFragment extends BaseFragment implements
		 IRefreshButtonAndText {

	Context mContext;
	BaseActivity activity;
	private TextView tv_showtime;

//	public ApplicationNotOnLineFragment(IApproveBackToList backToList,
//			int targetContainer) {
//		this.backToList = backToList;
//		this.targetContainer = targetContainer;
//	}

	public ApplicationNotOnLineFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		mContext = getActivity();
		Bundle bundle = getArguments();
		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("提交");
		btn_next.setVisibility(View.GONE);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("同步数据");
		// 获取当前登录用户
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_application_not_internet_layout,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}



}
