package cn.sbx.deeper.moblie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import cn.sbx.deeper.moblie.fargments.MainMenuFragment;
import petrochina.ghzy.a10fieldwork.R;


public class MainMenuActivity extends ActivityInTab{
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout rl_bottom_title = (RelativeLayout)findViewById(R.id.rl_bottom_title);
		rl_bottom_title.setVisibility(View.GONE);
		navigateTo(new MainMenuFragment());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
