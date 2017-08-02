package cn.sbx.deeper.moblie.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.fargments.MobileOATypeSquaredFragment;
import cn.sbx.deeper.moblie.fargments.MobileOATypeTopFragment;
import cn.sbx.deeper.moblie.util.MenuTypeUtil;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 通往二级导航界面的通道
 * @author terry.C
 * 
 */
public class MobileOAGroupActivity extends ActivityInTab {
	protected static final String TAG = MobileOAGroupActivity.class.getSimpleName();

	Context mContext;
	public TextView tv_title;
	private SinopecMenuGroup group;

	public MobileOAGroupActivity() {
		mContext = MobileOAGroupActivity.this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		group = (SinopecMenuGroup) getIntent().getSerializableExtra("entry");
		String backprecious = getIntent().getStringExtra("backprecious");

		if (backprecious != null) {
			if ("sodoku".equalsIgnoreCase(backprecious)) {
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

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(group.caption);
		int pageIndex = getIntent().getIntExtra("pageIndex", 0);
		Fragment f = null;
		Bundle bundle = null;
		switch (MenuTypeUtil.chooseMenuType(group.layout)) {
		case TOP:
			f = new MobileOATypeTopFragment();
			bundle = new Bundle();
			bundle.putSerializable("entry", group);
			bundle.putInt("pageIndex", pageIndex);
			f.setArguments(bundle);
			navigateTo(f);
			break;
		case SQUARED:
			f = new MobileOATypeSquaredFragment();
			bundle = new Bundle();
			bundle.putSerializable("entry", group);
			bundle.putInt("pageIndex", pageIndex);
			f.setArguments(bundle);
			navigateTo(f);
			break;
		default:
			break;
		}
	}
}
