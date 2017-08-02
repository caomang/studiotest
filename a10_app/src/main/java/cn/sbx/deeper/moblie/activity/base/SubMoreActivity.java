package cn.sbx.deeper.moblie.activity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.fargments.MoreSubChildAccounts;
import petrochina.ghzy.a10fieldwork.R;



public class SubMoreActivity extends ActivityInTab {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MoreSubChildAccounts subChildAccounts = new MoreSubChildAccounts();
		Bundle bundle = new  Bundle();
		bundle.putString("username",getIntent().getStringExtra("username"));
		bundle.putString("moduleid",getIntent().getStringExtra("moduleid"));
		bundle.putString("adusername",getIntent().getStringExtra("adusername"));
		subChildAccounts.setArguments(bundle);
		navigateTo(subChildAccounts);
		findViewById(R.id.rl_bottom_title).setVisibility(View.GONE);
	}
	
	@Override
	public void onBackPressed() {
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 1) {
			if (manager.getBackStackEntryCount() == 2) {
				exitFragment();
				super.onBackPressed();
			} else {
				exitFragment();
				super.onBackPressed();
			}
		} else {
			finish();
			overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
		}
	}
}
