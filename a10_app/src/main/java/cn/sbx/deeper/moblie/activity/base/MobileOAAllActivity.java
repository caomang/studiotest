package cn.sbx.deeper.moblie.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.interfaces.INextButtonListener;
import petrochina.ghzy.a10fieldwork.R;


public class MobileOAAllActivity extends ActivityInTab implements INextButtonListener{
	protected static final String TAG = "MobileOAAllActivity";
	String typeCode = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		SinopecMenuModule module = (SinopecMenuModule) getIntent().getSerializableExtra("entry");
//		int pageIndex = getIntent().getIntExtra("pageIndex", 0);
//		typeCode = module.caption;
//		System.out.println("typeConde: "+typeCode);
//		Fragment fragment = findFragmentByCode(module.mClass);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("entry", module);
//		fragment.setArguments(bundle);
//		navigateTo(fragment);
//		
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(Constants.getBroadcastNamePairsString(pageIndex));
//		registerReceiver(modifyTypeReiceiver, intentFilter);
		
		String type = getIntent().getStringExtra("type");
		if("0".equals(type)){//group
//			SinopecMenuModule group = (SinopecMenuModule) getIntent().getSerializableExtra("entry");
//			MenuModule module = group.menuModules.get(1);
			SinopecMenuGroup group = (SinopecMenuGroup) getIntent().getSerializableExtra("entry");
			int pageIndex = getIntent().getIntExtra("pageIndex", 0);
//			typeCode = module.caption;
			Constants.menuModule  = null;
			Constants.isFirst = true;
			System.out.println("typeCode: " + typeCode);
			// navigateTo(MobileOAFragment.newInstance(module));
			
			Intent intent = new Intent(Constants.GET_APP_MENU_TAB);
			intent.putExtra("pageIndex", pageIndex);
			sendBroadcast(intent);
			
			//添加标题
			TextView tv_title = (TextView)findViewById(R.id.tv_title);
			if(Constants.menuModule==null){
				SinopecMenuModule module = (SinopecMenuModule) group.menuobjObjects.get(0);
				Fragment fragment = findFragmentByCode(module.mClass);
				Bundle bundle = new Bundle();
				bundle.putSerializable("entry", module);
				fragment.setArguments(bundle);
				navigateTo(fragment);
				tv_title.setText(module.caption);
				typeCode = module.caption;
			}else{
				Fragment fragment = findFragmentByCode(Constants.menuModule.mClass);
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("entry", Constants.menuModule);
				fragment.setArguments(bundle);
				navigateTo(fragment);
				tv_title.setText(Constants.menuModule.caption);
				typeCode = Constants.menuModule.caption;
			}
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Constants.getBroadcastNamePairsString(pageIndex));
			registerReceiver(modifyTypeReiceiver, intentFilter);
			

		}else{
			SinopecMenuModule module = (SinopecMenuModule) getIntent().getSerializableExtra("entry");
			int pageIndex = getIntent().getIntExtra("pageIndex", 0);
			typeCode = module.caption;
			System.out.println("typeConde: " + typeCode);
			Fragment fragment = findFragmentByCode(module.mClass);
			Bundle bundle = new Bundle();
			bundle.putSerializable("entry", module);
			fragment.setArguments(bundle);
			navigateTo(fragment);
			//添加标题
			TextView tv_title = (TextView)findViewById(R.id.tv_title);
			tv_title.setText(module.caption);
		}
		
		Button bt_left = (Button) findViewById(R.id.bt_left);
		if (bt_left != null) {
			bt_left.setVisibility(View.VISIBLE);
			bt_left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
			      if(getRequestedOrientation()==
		             ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
		          {
		            /* 若目前为横排，则更改为竖排呈现 */
		           setRequestedOrientation
		            (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		          }
				}
			});
		}
	}
	BroadcastReceiver modifyTypeReiceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "ActivityInTab.BroadcastReceiver");
			changeCurrentFragment(intent);
		}
	};
	public void changeCurrentFragment(Intent intent) {
		SinopecMenuModule module = (SinopecMenuModule) intent.getSerializableExtra("entry");
		if(module!=null){
			String code = module.caption;
			String classCode = module.mClass;
			Log.i(TAG, "code:" + code + "...classCode:" + classCode);
			Fragment fragment = findFragmentByCode(classCode);
			Bundle bundle = new Bundle();
			bundle.putSerializable("entry", module);
			fragment.setArguments(bundle);
			if(!code.equals(typeCode)) {
				clearFragmentAndFresh(fragment);
				typeCode = module.caption;
			}
		}
	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (KeyEvent.KEYCODE_BACK == keyCode) {// 按下返回键提醒用户是否退出系统
//			if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//				/* 若目前为横排，则更改为竖排呈现 */
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//
//	}
	@Override
	public void nextButton(String text,final SinopecMenuModule approvalItem) {
		// TODO Auto-generated method stub
		/*if(text.equals("EmailFragment")){
			Button btn_next = (Button)findViewById(R.id.btn_next);
			btn_next.setText("发邮件");
			btn_next.setVisibility(View.VISIBLE);
			btn_next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					NewMailFragment mailDetailFragment = new NewMailFragment();
					Bundle bundle = new Bundle();
					bundle.putString("title", "新邮件");
					bundle.putSerializable("entry", approvalItem);
					mailDetailFragment.setArguments(bundle);
					navigateTo(mailDetailFragment);
				}
			});
		}else if(text.equals("MailDetailFragment")){
			
		}*/
	}
}
