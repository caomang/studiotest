package cn.sbx.deeper.moblie.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import net.arraynetworks.vpn.Common;
import net.arraynetworks.vpn.VPNManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sbx.deeper.moblie.activity.base.BaseTabActivity;
import cn.sbx.deeper.moblie.activity.base.MobileOAActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.LinkAppInfo;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SoftInfo;
import cn.sbx.deeper.moblie.interfaces.IApproveNumInterface;
import cn.sbx.deeper.moblie.logic.MainProcess;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.ImageLoader;
import cn.sbx.deeper.moblie.util.MenuTypeUtil;
import cn.sbx.deeper.moblie.view.SyncHorizontalScrollView;
import petrochina.ghzy.a10fieldwork.R;


/**
 * 底部和顶部菜单样式
 * 
 * @author terry.C
 * 
 */
public class MainActivity extends BaseTabActivity implements OnClickListener,
		OnTabChangeListener, IApproveNumInterface {
	private static final String TAG = "MainActivity";
	public static TabHost tabHost;
	public static SyncHorizontalScrollView horizontalScrollView;
	Context mContext;
	private ImageLoader imageLoader;
	private static Thread mThreadStopVpn = null;
	private SoftInfo info;

	public MainActivity() {
		mContext = MainActivity.this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageLoader = new ImageLoader(mContext);
		mainProcess = new MainProcess();
		if (DataCache.sinopecMenu != null) {
			menuDisplayType = DataCache.sinopecMenu.itemTemplate;
		}
		initMainView();

		IntentFilter intentFilter = new IntentFilter(
				Constants.MODIFY_APP_MENU_NUM);
		registerReceiver(mainReceiver, intentFilter);
	}
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mainReceiver != null)
			unregisterReceiver(mainReceiver);

		if (Constants.useVPN) {
			stopVpn();
		}
	}

	private View perpareIndicator(String icon1, String icon2, String text,
			String id) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.tab_main_nav_indicate, null);
		TextView textView = (TextView) view.findViewById(R.id.tv_syn_text);
		textView.setTag(id);
		textView.setText(text);
		ImageView iv_syn_icon = (ImageView) view.findViewById(R.id.iv_syn_icon);
		imageLoader
				.displayImage2(icon1, iv_syn_icon, R.drawable.ic_tab_default);

		switch (MenuTypeUtil.chooseMenuDisplayType(menuDisplayType)) {
		case PICTURE:
			textView.setVisibility(View.GONE);
			break;
		case WORD:
			iv_syn_icon.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		view.setBackgroundResource(R.drawable.bt_main_tab_default_selector);
		view.setLayoutParams(new FrameLayout.LayoutParams((int) getResources()
				.getDimension(R.dimen.main_bar_item_width),
				(int) getResources().getDimension(R.dimen.main_bar_item_height)));
		return view;
	}

	/**
	 * 切换Tab
	 */
	public static void setCurrentTab(int index) {
		tabHost.setCurrentTab(index);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.bt_menu_left:
//			tabHost.setCurrentTab(0);
//			horizontalScrollView.smoothScrollTo(0, 0);
//			break;
		case R.id.lv_left_scroll_image:
			tabHost.setCurrentTab(0);
			break;
		case R.id.lv_right_scroll_image:
			tabHost.setCurrentTab(tabHost.getTabWidget().getChildCount() - 1);
			horizontalScrollView.scrollTo(
					horizontalScrollView.getMeasuredWidth(), 0);
		default:
			break;

		}
	}

	private LinearLayout ll_open_container;
	public static List<LinkAppInfo> listAppInfos = new ArrayList<LinkAppInfo>();
	/**
	 * 匹配导航布局进入相应的fragment
	 */
	public void initMainView() {
		Log.i(TAG, "initMainView()");
		setContentView(R.layout.main);
		horizontalScrollView = (SyncHorizontalScrollView) findViewById(R.id.hl_scroll_main);
		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);
		List<Object> menus = DataCache.sinopecMenu.menuObject;
		for (int i = 0; i < menus.size(); i++) {
			Object object = menus.get(i);
			Constants.setBroadcastNamePairs(i);
			if (object instanceof SinopecMenuGroup) {
				SinopecMenuGroup group = (SinopecMenuGroup) object;
				Log.i(TAG, "group.barImg1:" + group.barImg1
						+ "...group.barImg2:" + group.barImg2);
				Log.i(TAG, "group.layout:" + group.layout);
				Log.i(TAG, "group.itemImg1:" + group.itemImg1
						+ "...group.itemImg2:..." + group.itemImg2 + "....");
				switch (MenuTypeUtil.chooseMenuType(group.layout)) {
				case TOP:
					tabHost.addTab(tabHost
							.newTabSpec(String.valueOf(i))
							.setIndicator(
									perpareIndicator(group.itemImg1,
											group.itemImg2, group.caption,
											"New"))
							.setContent(
									new Intent(this,
											MobileOAGroupActivity.class)
											.putExtra("entry", group).putExtra(
													"pageIndex", i)));
					break;
				case SQUARED:
					tabHost.addTab(tabHost
							.newTabSpec(String.valueOf(i))
							.setIndicator(
									perpareIndicator(group.itemImg1,
											group.itemImg2, group.caption,
											"New"))
							.setContent(
									new Intent(this,
											MobileOAGroupActivity.class)
											.putExtra("entry", group).putExtra(
													"pageIndex", i)));
					break;
				default:
					break;
				}
			} else if (object instanceof SinopecMenuModule) {
				SinopecMenuModule module = (SinopecMenuModule) object;
				Log.i(TAG, "module.barImg1:" + module.barImg1
						+ "...module.barImg2:" + module.barImg2);
				if (module.mClass.equalsIgnoreCase(Constants.FRAGMENT_TYPE_LINKAPP)) {
					tabHost.addTab(tabHost
							.newTabSpec(module.id)
							.setIndicator(
									perpareIndicator(module.barImg1.length()>0?module.barImg1.substring(0, module.barImg1.indexOf(".")):"",
											module.barImg2.length()>0?module.barImg2.substring(0, module.barImg2.indexOf(".")):"", module.caption, module.id))
							.setContent(
									new Intent(this, LinkAppActivity.class).putExtra("entry", module).putExtra("listAppInfos",
											(Serializable) listAppInfos)));

				} else {
				tabHost.addTab(tabHost
						.newTabSpec(String.valueOf(i))
						.setIndicator(
								perpareIndicator(module.itemImg1,
										module.itemImg2, module.caption,
										module.id))
						.setContent(
								new Intent(this, MobileOAActivity.class)
										.putExtra("entry", module).putExtra(
												"pageIndex", i)));
				}
			}

		}
		// tabHost.addTab(tabHost
		// .newTabSpec("MOBILE_OFFICE_MORE")
		// .setIndicator(
		// perpareIndicator("ic_more_nomal", "ic_more_selected",
		// getString(R.string.tv_main_more), ""))
		// .setContent(new Intent(this, MoreActivity.class)));

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			final int j = i;
			tabHost.getTabWidget().getChildAt(i)
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (tabHost.getCurrentTab() == j) {
								Object o = DataCache.sinopecMenu.menuObject
										.get(j);
								if (o instanceof SinopecMenuGroup) {
									Intent gotoModuleGroupIntent = new Intent(
											Constants
													.getBroadcastNamePairsString(j));
									gotoModuleGroupIntent.putExtra("type",
											"gotoModuleGroup");
									sendBroadcast(gotoModuleGroupIntent);
								} else {
									tabHost.setCurrentTab(j);
								}
							} else {
								tabHost.setCurrentTab(j);
							}

						}
					});
		}

		mainProcess.calApproveNum(mContext, this,
				DataCache.sinopecMenu.menuObject);
	}

	BroadcastReceiver mainReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "MainActivity.BroadcastReceiver");

			String type = intent.getStringExtra("type");
			if (null != type && "finish".equalsIgnoreCase(type)) {
				finish();
				return;
			}
			String groupId = intent.getStringExtra("id");
			List<String[]> numbers = (List<String[]>) intent
					.getSerializableExtra("value");
			if (numbers != null) {
				updateTabNums(groupId, calculateTabNumber(numbers));
			}
		}
	};

	public int calculateTabNumber(List<String[]> numbers) {
		int result = 0;
		for (String[] number : numbers) {
			int tempNumber = 0;
			try {
				tempNumber = Integer.parseInt(number[1]);
			} catch (Exception e) {
				tempNumber = 0;
				e.printStackTrace();
			}
			result += tempNumber;
		}
		return result;
	}

	private void updateTabNums(final String groupId, final int count) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				List<Object> menus = DataCache.sinopecMenu.menuObject;
				int tabIndex = 100;
				for (int i = 0; i < menus.size(); i++) {
					Object o = menus.get(i);
					if (o instanceof SinopecMenuGroup) {
						if (groupId
								.equalsIgnoreCase(((SinopecMenuGroup) (o)).id)
								&& "1".equalsIgnoreCase(((SinopecMenuGroup) (o)).notification)) {
							tabIndex = i;
							break;
						}
					} else if (o instanceof SinopecMenuModule) {
						if (groupId
								.equalsIgnoreCase(((SinopecMenuModule) (o)).id)
								&& "1".equalsIgnoreCase(((SinopecMenuModule) (o)).notification)) {
							tabIndex = i;
							break;
						}
					}
				}
				/*if (tabIndex < tabHost.getTabWidget().getChildCount()) {
					final View v = tabHost.getTabWidget().getChildAt(tabIndex);
					final TextView tv_text_num = (TextView) v
							.findViewById(R.id.tv_msg_count);
					if (count != 0) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tv_text_num.setText(TextUtils.parseText(String  
										.valueOf(count)));
								tv_text_num.setVisibility(View.VISIBLE);
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tv_text_num.setVisibility(View.GONE);
							}
						});
					}
				}*/
				return null;
			}
		}.execute();
	}

	private static boolean isExit = false;
	private MainProcess mainProcess;
	private String menuDisplayType;

	@Override
	public void onBackPressed() {
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					isExit = false;
				}
			}, 2000);
		} else {
			stopVpn();
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	public static int stopVpn() {
		Log.i(TAG, "stop vpn..............");
		if (Common.VpnStatus.IDLE == VPNManager.getInstance().getStatus()) {
			return 0;
		}
		Log.i(TAG, "stop l3vpn enter");
		if (mThreadStopVpn != null) {
			Log.w(TAG, "ThreadStopVpn is not null, will kill it.");
			mThreadStopVpn.interrupt();
			try {
				mThreadStopVpn.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mThreadStopVpn = null;
		}

		mThreadStopVpn = new Thread(new Runnable() {
			public void run() {
				VPNManager.getInstance().stopVPN();
				mThreadStopVpn = null;
			}
		});

		mThreadStopVpn.start();
		return 0;
	}

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshNumber(List<String[]> idNumbers) {
		if (idNumbers != null && idNumbers.size() > 0) {
			for (String[] imn : idNumbers) {
				updateTabNums(imn[0], Integer.parseInt(imn[1]));
			}
		}
	}
}
