package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sbx.deeper.moblie.contrants.Constants;
import petrochina.ghzy.a10fieldwork.R;
@SuppressLint("ValidFragment")
 public class ApplicationSchedInfoUpload extends BaseFragment {

	private LinearLayout ll_contener_AJ, ll_contener_CB;
	private TextView tv_schedinfo;
	private TextView tv_des1;
	private TextView tv_des2;
	private TextView tv_des;
	private TextView tv_des3;
	private EditText test_for_quantity;

	public ApplicationSchedInfoUpload(TextView tv_schedinfo, TextView tv_des,TextView tv_des1,
			TextView tv_des2,TextView tv_des3) {
		this.tv_schedinfo = tv_schedinfo;
		this.tv_des = tv_des;
		this.tv_des1 = tv_des1;
		this.tv_des2 = tv_des2;
		this.tv_des3 = tv_des3;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerBoradcastReceiver();

	}

	private void registerBoradcastReceiver() {
		myBroadcast = new MyBroadcast();

		IntentFilter intent = new IntentFilter(
				Constants.receive_upload_result_anjian);
		getActivity().registerReceiver(myBroadcast, intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.layout_approve_updatsched,
				null);
		ll_contener_CB = (LinearLayout) view.findViewById(R.id.ll_contener_CB);
		ll_contener_AJ = (LinearLayout) view.findViewById(R.id.ll_contener_AJ);
		test_for_quantity=(EditText)view.findViewById(R.id.test_for_quantity);

		TextView text_view = (TextView) view .findViewById(R.id.text_view);
		TextView text_view1 = (TextView) view .findViewById(R.id.text_view1);

		// 安检任务

		anJianUpdate = new AnJianUpdate(mActivity);
		if(anJianUpdate.getView()!=null){
			ll_contener_AJ.addView(anJianUpdate.getView());
		}else{
			text_view1.setVisibility(View.VISIBLE);
		}
		// 抄表任务

		chaoBiaoUpdate = new ChaoBiaoUpdate(mActivity);
		if(chaoBiaoUpdate.getView()!=null){
			ll_contener_CB.addView(chaoBiaoUpdate.getView());
		}else{
//			text_view.setVisibility(View.VISIBLE);
		}

		Button button_update = (Button) view.findViewById(R.id.button_update);
		button_update.setOnClickListener(Listener_upload);
		return view;
		// 任务列表布局

	}

	// 上传所选任务
	private OnClickListener Listener_upload = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			String trim = test_for_quantity.getText().toString().trim();
			tv_des.setVisibility(View.GONE);
			tv_des1.setVisibility(View.GONE);
			tv_des2.setVisibility(View.GONE);
			tv_des3.setVisibility(View.GONE);
//			抄表上传
			//抄表模块暂时隐藏
//			chaoBiaoUpdate.uploadCB(tv_des1,tv_des3);
//			安检上传
//			try{
//				int i = Integer.parseInt(trim);
//				if(i>0){
//					Constants.testForUpDataNum=i;
//				}else{
//					Toast.makeText(mActivity,"请输入想要上传的个数",Toast.LENGTH_SHORT).show();
//				}
//			}catch (Exception e){
//				e.printStackTrace();
//				Toast.makeText(mActivity,"请输入正确的数据",Toast.LENGTH_SHORT).show();
//			}



			anJianUpdate.uploadAnJian(tv_des2,tv_des3);
			tv_schedinfo.setText("上传结果 : ");
			tv_schedinfo.setVisibility(View.VISIBLE);
		}
	};
	private AnJianUpdate anJianUpdate;
	private ChaoBiaoUpdate chaoBiaoUpdate;
	private MyBroadcast myBroadcast;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// 通过广播接收到上传成功后的信息,关闭当前界面
	public class MyBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(
					Constants.receive_upload_result_anjian)) {

				// 返回成功 关闭当前页面
				backPrecious();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销广播
		getActivity().unregisterReceiver(myBroadcast);

	}

}
