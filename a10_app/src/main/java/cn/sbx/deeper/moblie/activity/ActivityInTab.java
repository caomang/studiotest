package cn.sbx.deeper.moblie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.sunboxsoft.monitor.utils.PerfUtils;

import net.arraynetworks.vpn.Common;
import net.arraynetworks.vpn.VPNManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.fargments.AnJianSchedInfoFragment;
import cn.sbx.deeper.moblie.fargments.ApplicationNotOnLineFragment;
import cn.sbx.deeper.moblie.fargments.MobileIntranetFragment;
import cn.sbx.deeper.moblie.fargments.MoreFragment;
import cn.sbx.deeper.moblie.fargments.PollingApproveFragment;
import cn.sbx.deeper.moblie.fargments.SynchronousDataFragment;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.APNUtils;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.FileCache;
import petrochina.ghzy.a10fieldwork.R;

//import petrochina.ghzy.a10fieldwork.R;

public abstract class ActivityInTab extends BaseActivity {
	private static final String TAG = "ActivityInLeft";

	//这是个分支
	public SharedPreferences sp;
	public Map<String, Fragment> fragmentMap = Constants.getFragmentPairs();
	private List<Fragment> backFragments = new ArrayList<Fragment>();
	private static boolean isExit = false;
	public static Context mContext;
	public static FragmentActivity mActivityInTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = ActivityInTab.this;
		mActivityInTab = ActivityInTab.this;
		setContentView(R.layout.activity_in_bottom);
		sp = getSharedPreferences("sys_config", Context.MODE_PRIVATE);
		/*findViewById(R.id.iv_more).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityInTab.this,
						MoreActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fragment_slide_right_enter,
						R.anim.fragment_slide_left_exit);
			}
		});*/
		// fragmentsUtility("");
	}

	// fragment的替换跳转
	public void replaceNavigateTo(Fragment newFragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
				R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_left_enter,
				R.anim.fragment_slide_right_exit);
		ft.replace(R.id.content, newFragment);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
		enterFragment(newFragment);
	}

	public void navigateTo(Fragment newFragment) {
		Log.i(TAG, "addToBackStack()");
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		/*if (!(newFragment instanceof BiFragment)) {
			ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
					R.anim.fragment_slide_left_exit,
					R.anim.fragment_slide_left_enter,
					R.anim.fragment_slide_right_exit);
		}*/
		if (newFragment.getClass().getSimpleName()
				.equalsIgnoreCase("ContactFragment_trans2p0")) {
			ft.add(R.id.content, newFragment, newFragment.getClass()
					.getSimpleName());
		} else {
			ft.add(R.id.content, newFragment);
		}
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
		enterFragment(newFragment);
	}

	// 带有tag值的跳转
	public void navigateTo(Fragment newFragment, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
				R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_left_enter,
				R.anim.fragment_slide_right_exit);
		ft.add(R.id.content, newFragment, tag);
		// ft.replace(R.id.content, newFragment, tag);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
		// enterFragment(newFragment);//added by wangst
	}

	public void navigateToTag(Fragment newFragment, int id, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(id, newFragment, tag);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	protected void enterFragment(Fragment newFragment) {
		if (backFragments.size() >= 1) {
			Fragment topFragment = backFragments.get(backFragments.size() - 1);
			if (topFragment != null)
				getSupportFragmentManager().beginTransaction()
						.hide(topFragment).commitAllowingStateLoss();
		}
		backFragments.add(newFragment);
	}

	//
	protected void exitFragment() {
		if (backFragments.size() > 1) {
			Fragment previousFragment = backFragments
					.get(backFragments.size() - 2);
			backFragments.remove(backFragments.size() - 1);
			getSupportFragmentManager().beginTransaction()
					.show(previousFragment).commitAllowingStateLoss();
			// refreshFragmentButton((IRefreshButtonAndText) previousFragment);
			// if(previousFragment instanceof EmailFragment){
			// ((EmailFragment) previousFragment).refreshButtonAndText();
			// }else
			refreshFragmentButton((IRefreshButtonAndText) previousFragment);
		}
	}

	public void replaceFragment(Fragment newFragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
				R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_left_enter,
				R.anim.fragment_slide_right_exit);
		ft.replace(R.id.content, newFragment);
		ft.commitAllowingStateLoss();
	}

	public void slidmenuNavigaTo(Fragment newFragment) {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
		backFragments.clear();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.content, newFragment);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
		enterFragment(newFragment);
	}

	protected void clearStack() {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "onBackPressed()");
		FragmentManager manager = getSupportFragmentManager();
		if (Constants.DEBUG) {
			Log.i(TAG, "back stack count:" + manager.getBackStackEntryCount());
		}
		// 通讯录的判断
//		if (!onBackContactsFragment(manager)) {
//			return;
//		}
		// WarningComponentFragment fragment = (WarningComponentFragment)
		// manager
		// .findFragmentByTag("EarlyWarning");
		// if(fragment!=null){
		// if(!fragment.onBack()){
		// exitFragment();
		// super.onBackPressed();
		// }
		//
		// }else{
		if (manager.getBackStackEntryCount() > 1) {
			// exitFragment(manager.beginTransaction());
			if (manager.getBackStackEntryCount() == 2) {
				BackStackEntry backStackEntry = manager.getBackStackEntryAt(1);
				if (Constants.DEBUG) {
					if (backStackEntry != null)
						Log.i(TAG,
								"backStackEntry.getName():"
										+ backStackEntry.getName());
					// Log.i(TAG,
					// "backStackEntry.getName():"
					// +
					// backFragments.get(manager.getBackStackEntryCount()-1).getTag());
				}
				// if(MonitorVedioFragment.TAG.equals(backStackEntry.getName()))
				// {
				// MainActivity.setCurrentTab(0);
				// MainActivity.horizontalScrollView.scrollTo(0, 0);
				// }else {
				// exitFragment();
				// super.onBackPressed();
				// }
				findViewById(R.id.btn_next).setVisibility(View.GONE);
				exitFragment();
				super.onBackPressed();
			} else {
				exitFragment();
				super.onBackPressed();
			}
			// super.onBackPressed();

		} else {
			if ("all".equals(DataCache.layoutType)) {
				if (SinopecAllMenuActivity.tabHost != null
						&& SinopecAllMenuActivity.tabHost.getCurrentTab() != 0) {
					SinopecAllMenuActivity.setCurrentTab(0);
					SinopecAllMenuActivity.horizontalScrollView.scrollTo(0, 0);
				} else {
					if (isExit == false) {
						isExit = true;
						Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT)
								.show();
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								isExit = false;
							}
						}, 2000);
					} else {
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							FileCache.deleteCacheFile(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ Constants.home_cache + "/oa");
						}
						if (Constants.useVPN) {
							stopVpn();
						}
						// restoreApn();// apn 非燕山项目注释掉
						// DataCollectionUtils.logoutSendBroadcast(mContext);
						finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				}
			} else {
				/*if (MainActivity.tabHost == null
						&& SinopecLeftMenuActivity.menu == null) {
					finish();
					return;
				}*/
				if (MainActivity.tabHost != null
						&& MainActivity.tabHost.getCurrentTab() != 0) {

					MainActivity.setCurrentTab(0);
					MainActivity.horizontalScrollView.scrollTo(0, 0);
				} else {
					if (isExit == false) {
						isExit = true;
						Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT)
								.show();
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								isExit = false;
							}
						}, 2000);
					} else {
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							FileCache.deleteCacheFile(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ Constants.home_cache + "/oa");
						}
						if (Constants.useVPN) {
							stopVpn();
						}
						// restoreApn();// apn
						// DataCollectionUtils.logoutSendBroadcast(mContext);
						finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
					// new
					// AlertDialog.Builder(this).setTitle("提示").setMessage("是否要退出系统?").setPositiveButton("确定",
					// new DialogInterface.OnClickListener() {
					// @Override
					// public void onClick(DialogInterface dialog, int which) {
					// finish();
					// android.os.Process.killProcess(android.os.Process.myPid());
					// }
					// }).setNegativeButton("取消", null).show();
				}
			}
		}
		// }
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			/* 若目前为横排，则更改为竖排呈现 */
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	private static Thread mThreadStopVpn = null;

	public static int stopVpn() {
		Log.i(TAG, "stop vpn................");
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

	public void clearFragmentAndFresh(Fragment newFragment) {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
		// backFragments.clear();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content, newFragment);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
		enterFragment(newFragment);
	}

	public void clearFragmentAndFreshOAApprove(int targetContainer,
			IApproveBackToList toList) {
		onBackPressed();
		onBackPressed();
		toList.refreshOAApprove();
	}

	public void clearFragmentAndFreshOAApprove1(int targetContainer,
			IApproveBackToList toList) {
		onBackPressed();
		toList.refreshOAApprove();
		findViewById(R.id.btn_next).setVisibility(View.VISIBLE);
	}

	public void refreshFragmentButton(IRefreshButtonAndText toList) {
		toList.refreshButtonAndText();
	}

	public Fragment findFragmentByCode(String classCode) {
		// classCode="ACC005";

		 /* if ("DocComponent".equalsIgnoreCase(classCode)) { return new
		 * AnalysisReportFragment();//2.0文档类 }else if
		 * ("AddressBook".equalsIgnoreCase(classCode)) { return new
		 * ContactFragment_trans2p0();//2.0通讯录类 }else if
		 * ("Schedules2".equalsIgnoreCase(classCode)) { return new
		 * ScheduleFragment();//2.0日程安排 }else if
		 * ("ACC003".equalsIgnoreCase(classCode)) { return new
		 * MobileIntranetFragment_trans2p0();//2.0门户类 }else if
		 * ("meeting".equalsIgnoreCase(classCode)) { // return new
		 * MettingFragment(); return new WireliessMettingFragment();//2.0会议 }
		 * else if ("vote".equalsIgnoreCase(classCode)) { return new
		 * VoteFragment();//2.0投票表决 }else if
		 * ("monitor".equalsIgnoreCase(classCode)) { return new
		 * MonitorVedioFragment();//2.0视频监控 }else if
		 * ("news".equalsIgnoreCase(classCode)) { return new
		 * MobileNewsPaperShelfFragment_trans2p0();//2.0手机报 }else if
		 * ("ACC001".equalsIgnoreCase(classCode)) { return new
		 * PageShowDataFragment2();//2.0页面展示 }
		 */
		if (Constants.FRAGMENT_TYPE_APPROVAL.equalsIgnoreCase(classCode)) {
//			return new MobileOAFragment(); // ....信用审批**
		}
// else if (Constants.FRAGMENT_TYPE_APPROVAL2
//				.equalsIgnoreCase(classCode)) {
//			return new NewOAFragment(); // 审批2.0
//		} else if (Constants.FRAGMENT_TYPE_SITE.equalsIgnoreCase(classCode)) {
//			return new MobileIntranetFragment();// ....移动用户
//		} else if ("ACC001".equalsIgnoreCase(classCode)) {
//			// return new QuickMessageFragment();
//			return new SinopecApproveFragment();// ....移动用户
//			// SinopecApproveFragment SinopecApproveOAFragment
//		} else if ("MessageSend".equalsIgnoreCase(classCode)) {
//			return new QuickMessageFragment();// 短信发送
//		} else if ("ApprovalTow".equalsIgnoreCase(classCode)) {
//			return new NewOAFragment();
//		} else if ("Schedules".equalsIgnoreCase(classCode)) {// 日程
//			// return new SchedulesDepartmentFragment();// 部门会议
//			return new SchedulesFragmentNew();// 新部门会议
//		} else if ("DocComponent".equalsIgnoreCase(classCode)) {
//			// return new DocumentFragment();// 公司会议
//			return new ManualFragment();// A3文档管理
//			// else if ("DocComponent".equalsIgnoreCase(classCode)) {
//			// return new SchedulesCompanyFragment();//公司会议
//			// }
//		} else if ("AddressBook".equalsIgnoreCase(classCode)) {
//			// return new FragementList();
//			return new ContactFragment_trans2p0();
//		} else if ("webview".equalsIgnoreCase(classCode)) {
//			return new WebComponentFragment();
//		} else if ("ACC003".equalsIgnoreCase(classCode)) {// 燕山公司活动
//			// return new MobileIntranetFragment();
//			return new MobileOfficeListFragment();
//		} else if ("ACC005".equals(classCode)) {// 2.0报表移植 (报表类)
//			return new BiFragment();
//		} else if ("SinglePageList".equals(classCode)) {// 单页列表
//			return new MobileOfficeSingleListFragment();
//		} else if ("Periodicals".equalsIgnoreCase(classCode)) {// 4.0期刊石化报
//			return new MobileNewsPaperShelfFragment();
//		} else if ("NewUninOffice".equals(classCode)) {// 新合署办公
//			return new UnionOfficeNewFragment();
//		} else if (Constants.FRAGMENT_TYPE_EMAIL.equalsIgnoreCase(classCode)) {
//			return new EmailFragment();// ..邮件**
//		} else if ("DocComponentSystem".equalsIgnoreCase(classCode)) {
//			return new DocumentsOfficeYSFragment();// ys文档管理
//		} else if ("EarlyWarning".equalsIgnoreCase(classCode)) {
//			return new WarningComponentFragment();// 预警指令
//		} else if ("ChairmanView".equalsIgnoreCase(classCode)) {
//			return new UnionOfficeNewDSZCXFragment();// 董事长视图
//		} else if ("WarningPush".equalsIgnoreCase(classCode)) {
//			return new MobileOfficeWarningMonitorFragment();// 预警推送
//		} else if ("DocumentTree".equalsIgnoreCase(classCode)) {
//			return new DocumentFragment();// 树形文档
//		}

		else if ("CustomComponent".equalsIgnoreCase(classCode)) {
//			return new ApplicationSetFragment();// 巡检 终端配置
			return new MoreFragment();// 巡检 终端配置
			
		}
		else if ("Meterreading".equalsIgnoreCase(classCode)) {
			return new PollingApproveFragment();// 抄表 巡检
		}
		else if ("A10Dic".equalsIgnoreCase(classCode)) {
//			return new ApplicationDictionariesFragment();// 字典
			//判断是不是离线登录的
//			boolean boolean_Login = PerfUtils.getBoolean(mContext, "isOnline", true);
			boolean boolean_Login =PerfUtils.getBoolean(mContext,"loginState",false);

			if(boolean_Login){
				return new SynchronousDataFragment();// (同步数据)
			}else{
				return new ApplicationNotOnLineFragment();
			}
		}
			else if ("SecurityCheck".equalsIgnoreCase(classCode)) {
//			return new AnJianApproveFragment();//巡检 安检
			return new AnJianSchedInfoFragment();//巡检 安检
			
		}
	/*	 else if ("AnJian".equalsIgnoreCase(classCode)) {
			return new AnJianApproveFragment();// 巡检
		}*/


	/*else if ("Map".equalsIgnoreCase(classCode)) {
			return new MapFragment();// 地图
		} */
	/*else if ("WeiXiu".equalsIgnoreCase(classCode)) {
			return new WeiXiuApproveFragment();// 维修
		}*/

		// else if(Constants.FRAGMENT_TYPE_CHART.equalsIgnoreCase(classCode)) {
		// return new BiFragment();//...报表应用**
		//
		// }else
		// if(Constants.FRAGMENT_TYPE_DOCUMENT.equalsIgnoreCase(classCode)) {
		// //..会议资料**
		// return new AnalysisReportFragment();
		// }else if(Constants.FRAGMENT_TYPE_SETTING.equalsIgnoreCase(classCode))
		// {
		// return new MoreFragment();//...设置**
		//
		// }else
		// if(Constants.FRAGMENT_TYPE_SCHEDULE.equalsIgnoreCase(classCode)) {
		// //...日程安排**
		// return new ScheduleFragment();
		// }else if(Constants.FRAGMENT_TYPE_NEWS.equalsIgnoreCase(classCode)) {
		// return new MobileNewsPaperShelfFragment();//..石化报**
		//
		// }else if(Constants.FRAGMENT_TYPE_DATA.equalsIgnoreCase(classCode)) {
		// // return new MettingFragment();
		// return new WireliessMettingFragment();
		// }else if(Constants.FRAGMENT_TYPE_MEETING.equalsIgnoreCase(classCode))
		// {
		// // return new MettingFragment();
		// return new WireliessMettingFragment(); //..无线会议**
		//
		// }else
		// if(Constants.FRAGMENT_TYPE_CONTACTS.equalsIgnoreCase(classCode)) {
		// return new ContactFragment(); //....企业通讯**
		//
		// }else if(Constants.FRAGMENT_TYPE_MONITOR.equalsIgnoreCase(classCode))
		// {
		// return new MonitorVedioFragment();//...视频监控**
		//
		// }else
		// if(Constants.FRAGMENT_TYPE_WPAGESHOW.equalsIgnoreCase(classCode)) {
		// return new PageShowFragment();//..业务集成树形**
		//
		// }else if(Constants.FRAGMENT_TYPE_VIDEO.equalsIgnoreCase(classCode)) {
		// return new OpenThirdAppFragment();
		// //..
		// }else
		// if(Constants.FRAGMENT_TYPE_PAGESHOW.equalsIgnoreCase(classCode)) {
		// //..
		// return new PageShowDataFragment();
		//
		// }
		// if (keys.contains(classCode)) {
		// try {
		// return (Fragment) Class.forName(
		// "com.sbx.deeper.moblie.fargments."
		// + getProperties(classCode)).newInstance();
		// } catch (InstantiationException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
		return new MobileIntranetFragment();
	}

	/*public Fragment findFragmentByCode(String classCode, String id) {

		if (Constants.FRAGMENT_TYPE_APPROVAL.equalsIgnoreCase(classCode)) {
			return new MobileOAFragment(); // ....信用审批**

		} else if (Constants.FRAGMENT_TYPE_SITE.equalsIgnoreCase(classCode)) {
			return new MobileIntranetFragment();// ....移动用户
		} else if ("ACC001".equalsIgnoreCase(classCode)) {
			// return new SinopecApproveOAFragment();// ....移动用户
		} else if ("approve2".equalsIgnoreCase(classCode)) {
			// return new SinopecApproveOAFragment();
		} else if ("Schedules".equalsIgnoreCase(classCode)) {// 日程
			if ("5211".equalsIgnoreCase(id)) {
				return new SchedulesDepartmentFragment();// 部门会议
			} else if ("5212".equalsIgnoreCase(id)) {
				return new SchedulesDirectorFragment();// 主任会议
			} else if ("5213".equalsIgnoreCase(id)) {
				return new SchedulesPersonalFragment();// 我的会议
			}
		} else if ("DocComponent".equalsIgnoreCase(classCode)) {
			return new SchedulesCompanyFragment();// 公司会议
		}
		return new MobileIntranetFragment();
	}*/

	// 通讯录的退出,true表示退出了
	// public boolean onBackContactsFragment(FragmentManager manager) {
	// boolean bool = false;
	// EmployeesFragments employeesFragment = (EmployeesFragments) manager
	// .findFragmentByTag("EmployeesFragment");
	// // if (employeesFragment != null && employeesFragment.bool) {
	// // manager.popBackStack();
	// // return false;
	// // }
	// ContactFragment fragment = (ContactFragment) manager
	// .findFragmentByTag("ContactFragment");
	//
	// ContactItemFragment fragment1 = (ContactItemFragment) manager
	// .findFragmentByTag("ContactFragment1");
	// if (fragment != null) {
	// if (!fragment.bool) { // 如果不是是当前活动的状态，返回true
	// return true;
	// }
	// }
	// // 如果是当前fragment的话，接着判断
	// if (fragment != null && fragment1 != null) {
	// bool = false;
	// // fragment.animViewShow();
	// } else {
	// bool = true;
	// }
	// return bool;
	// }

	// 通讯录的退出,true表示退出了
/*
	public boolean onBackContactsFragment(FragmentManager manager) {
		ContactFragment_trans2p0 fragment = (ContactFragment_trans2p0) manager
				.findFragmentByTag("ContactFragment_trans2p0");
		if (fragment != null && manager.getBackStackEntryCount() == 2) {
			System.out.println("222222222");
			exitFragment();
			super.onBackPressed();
			return true;
		}
		EmployeesFragment2_trans2p0 employeesFragment = (EmployeesFragment2_trans2p0) manager
				.findFragmentByTag("EmployeesFragment2_trans2p0");
		if (employeesFragment != null && employeesFragment.bool) {
			manager.popBackStack();
			return false;
		}
		ContactItemFragment_trans2p0 fragment1 = (ContactItemFragment_trans2p0) manager
				.findFragmentByTag("ContactFragment_trans2p01");
		if (fragment != null) {
			if (!fragment.bool) { // 如果不是是当前活动的状态，返回true
				return true;
			}
		}
		// 如果是当前fragment的话，接着判断
		if (fragment != null) {
			fragment.animViewShow();
			return false;
		} else {
			return true;
		}
	}
*/

	private Properties prop;
	private InputStream is;

	public String getProperties(String PropertyName) {
		return prop.getProperty(PropertyName);
	}

	public void fragmentsUtility(String filename) {
		AssetManager assetManager = getAssets();
		prop = new Properties();
		// is = getClass().getResourceAsStream(filename);//component.property
		try {
			is = assetManager.open("component.property");
			prop.load(is);
			showKeysAndValues(prop);
			if (is != null)
				is.close();
		} catch (IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}

	static List<String> keys = new ArrayList<String>();

	/**
	 * @param properties
	 */
	private static void showKeysAndValues(Properties properties) {
		Iterator<Entry<Object, Object>> it = properties
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			keys.add(key.toString());
			System.out.println("key   :" + key);
			System.out.println("value :" + value);
		}
	}

	public void restoreApn() {
		// APN设置
		// 将APN接入点还原
		System.out.println("===apn 还原==");
		int apnAsDefault = sp.getInt("checked_position", 0);
		if (apnAsDefault == 3) {
			int currApnId = sp.getInt("PREF_CURR_NORMAL_APN_ID", -1);
			int yapnId = -1;
			try {
				yapnId = APNUtils.fetchCurrentApnId();
				APNUtils.setContext(mContext);
				if (-1 != currApnId && currApnId != yapnId) {
					APNUtils.deleteAPN(yapnId);
					APNUtils.SetDefaultAPN(currApnId);
				} else {
					// APN出现错误，提示用户手动修改
					if (-1 != yapnId) {
						Intent intent = new Intent(
								Intent.ACTION_MAIN);
						intent.setClassName("com.android.settings",
								"com.android.settings.ApnSettings");
						startActivity(intent);
						Toast.makeText(mContext, "请将接入点置回，以正常访问网络！",
								Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				if (-1 != yapnId) {
					Intent intent = new Intent(
							Intent.ACTION_MAIN);
					intent.setClassName("com.android.settings",
							"com.android.settings.ApnSettings");
					startActivity(intent);
					Toast.makeText(mContext, "请将接入点置回，以正常访问网络！",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == 27) {
			if (currentShowFragment()) {
				System.out
						.println("==========PollingOperationFragment.onKeyDown============");
				/*PollingOperationFragment.onKeyDown(keyCode, event);*/
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean currentShowFragment() {
		if (backFragments.size() >= 1) {
			Fragment topFragment = backFragments.get(backFragments.size() - 1);
			/*if (topFragment != null)
				if (topFragment instanceof PollingOperationFragment) {
					return true;
				}*/
		}
		return false;
	}
}
