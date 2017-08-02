package cn.sbx.deeper.moblie.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import petrochina.ghzy.a10fieldwork.R;


public class MobileOAActivity extends ActivityInTab {
	protected static final String TAG = "MobileOAActivity";
	String typeCode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		module = (SinopecMenuModule) getIntent().getSerializableExtra("entry");
		int pageIndex = getIntent().getIntExtra("pageIndex", 0);
		typeCode = module.caption;
		System.out.println("typeConde: " + typeCode);
		// navigateTo(MobileOAFragment.newInstance(moFdule));

		Fragment fragment = findFragmentByCode(module.mClass);
		Bundle bundle = new Bundle();
		bundle.putSerializable("entry", module);
		bundle.putSerializable("pageIndex", pageIndex);
		/*fragment.setArguments(bundle);
		navigateTo(fragment);*/

		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(Constants.getBroadcastNamePairsString(pageIndex));
		registerReceiver(modifyTypeReiceiver, intentFilter);

		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		TextView tv_title1 = (TextView) findViewById(R.id.tv_title);
		if (tv_title != null) {
			tv_title.setText(module.caption);
		}
		String backprecious = getIntent().getStringExtra("backprecious");

		if (backprecious != null) {
			if ("squared".equalsIgnoreCase(backprecious)) {
				Button bt_left = (Button) findViewById(R.id.bt_left);
				if (bt_left != null) {
					bt_left.setVisibility(View.VISIBLE);
					bt_left.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onBackPressed();
						}
					});
				}
			}
		}
	}

	BroadcastReceiver modifyTypeReiceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "ActivityInTab.BroadcastReceiver");
			// changeCurrentFragment(intent);
			// String moduleId = intent.getStringExtra("id");
			List<String[]> numbers = (List<String[]>) intent
					.getSerializableExtra("value");
			Intent numIntent = new Intent(Constants.MODIFY_APP_MENU_NUM);
			numIntent.putExtra("id", module.id);
			numIntent.putExtra("value", (Serializable) numbers);
			sendBroadcast(numIntent);

		}
	};
	private SinopecMenuModule module;

	public void changeCurrentFragment(Intent intent) {
		SinopecMenuModule module = (SinopecMenuModule) intent
				.getSerializableExtra("entry");
		String code = module.caption;
		String classCode = module.mClass;
		Log.i(TAG, "code:" + code + "...classCode:" + classCode);
		Fragment fragment = findFragmentByCode(classCode);
		Bundle bundle = new Bundle();
		bundle.putSerializable("entry", module);
		fragment.setArguments(bundle);
		if (!code.equals(typeCode)) {
			clearFragmentAndFresh(fragment);
			typeCode = module.caption;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (modifyTypeReiceiver != null)
			unregisterReceiver(modifyTypeReiceiver);
	}
}
