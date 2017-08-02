package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sinopec.plugin.MyListView;
import com.sinopec.plugin.MyListView.OnRefreshListener;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.base.SubMoreActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.ApproveTab;
import cn.sbx.deeper.moblie.domian.ChildAccountsBean;
import cn.sbx.deeper.moblie.domian.SearchCondition;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDate;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDropOption;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDropOrderBy;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDropSelect;
import cn.sbx.deeper.moblie.domian.SearchCondition.UITextInput;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SinopecMenuPage;
import cn.sbx.deeper.moblie.domian.Task;
import cn.sbx.deeper.moblie.fargments.MoreSubChildAccounts.IRefresh;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.DatabaseHelperInstance;
import cn.sbx.deeper.moblie.util.LogUtil;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import cn.sbx.deeper.moblie.view.TagDigitalButton;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 石化审批/公文组件
 *
 * @author terry.C
 *
 */
@SuppressLint("ValidFragment")
public class AnJianApproveFragment extends BaseFragment implements
		IApproveBackToList, IRefresh, OnClickListener,IRefreshButtonAndText {
	private SQLiteDatabase instance;
	private HorizontalScrollView hs_approve_tab;
	private LinearLayout ll_condition;
	private MyListView ml_approve_list;
	private ApproveListAdapter listAdapter;
	private List<Task> tasks = new ArrayList<Task>();
	private RelativeLayout ll_approve_condition_container;
	private ImageView iv_search;

	private static boolean hasFootView = false;
	private static boolean isRefreshFoot = false;
	private static boolean isMoreLoad = false;
	private LinearLayout footView;// 加载更多
	private TextView moreText;//
	private ProgressBar moreProgress;//
	private static final int FLAG_LOAD_DATA = 0;// 加载标志
	private static final int FLAG_REFRESH = 1;// 刷新标志
	private static final int FLAG_MORE = 2;// 加载更多标志
	private static final int FLAG_SEARCH = 3;// 搜索标志
	protected static final int LOADING = 10;
	protected static final int Over = 20;

	private Context mContext;
	private SinopecMenuModule menuModule;
	private RadioGroup groupTabs;
	private int targetContainer;
	private int pageIndex;
	private String listPath = "";
	private String realListPath = "";
	private String numberPath = "";
	private String subsystemaccount = "";
	private String currentType = "";
	private String chercked = "";
	private String searchXML = "";
	private int pageSize = 10;
	private SinopecMenuGroup group;

	private ApproveTab currentTab;
	private Map<Object, View> conditionMap = new HashMap<Object, View>(); // 这个集合存放的搜索控件，在判断是否为必填项的时候用到，
	private Map<String, String> conditionParams = new HashMap<String, String>(); // 这个集合，用于存储搜索条件用的，在获取列表中用到
	private boolean spFlag = false;
	private boolean isEditChange = false;
	private Spinner spinner;
	private ArrayAdapter<String> adapterSpinner;
	// private CheckBox cb_left;
	private ProgressHUD loadProgress;
	private String tag = null;
	private EditText et_search_long;
	private HorizontalScrollView hsv;
	private int screenWidthDip;
	private Button btn_next;// 批量审批
	List<ApproveTab> batchTabs = null;
	private RelativeLayout rl_ck_batch_all;
	Button btn_save;
	private String date = "";
	private LinearLayout ll_dialog;
	private static ArrayList<String> StrCitys;
	MobileApplication mobileApplication;
//	public DatabaseHelper db;// 操作数据库的工具类
	StringBuilder sb;
//	String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
//	String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";

	private int dataSize = 10;
	private EditText searchKey = null;
//	private Connection connection_For_all;
	private DatabaseHelper helper;

	Handler handler = new Handler() {
		private ProgressHUD progress;

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADING:
				progress = AlertUtils.showDialog(mActivity, null, null, false);
				break;
			case Over:
				if (progress != null) {
					progress.dismiss();

				}
				break;

			default:
				break;
			}

		};
	};



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = mActivity;
		mobileApplication = new MobileApplication();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();
		DatabaseHelper.encrypt(mContext);
		DatabaseHelperInstance databaseHelperInstance=new DatabaseHelperInstance();
		instance = databaseHelperInstance.getInstance(mContext);

		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidthDip = dm.widthPixels;
		Bundle bundle = getArguments();
		if (bundle != null) {
			// 任务id
			cmSchedID = bundle.getString("cmSchedID");
			SinopecMenuModule item = (SinopecMenuModule) bundle
					.getSerializable("entry");
			menuModule = item;
			group = (SinopecMenuGroup) bundle.getSerializable("group");
			targetContainer = bundle.getInt("containerId");
			pageIndex = bundle.getInt("pageIndex");
			tag = bundle.getString("tag");
			initPathParams(item);
			TextView tv_title = (TextView) mActivity
					.findViewById(R.id.tv_title);
			tv_title.setText(menuModule.caption);

			ImageView imageView = (ImageView) mActivity
					.findViewById(R.id.iv_map);
			imageView.setBackgroundResource(R.drawable.a);

		}
		// 注册一个设置成功之后的广播
		IntentFilter intentFilter = new IntentFilter(Constants.GET_APP_MENU_NUM);
		mActivity.registerReceiver(broadcastReceiver, intentFilter);

		// new ApproveTabTask().execute();

	}

	// nav_icon_setting
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_approve, container, false);
		// View view = inflater.inflate(R.layout.anjian_approve_detail_child2,
		// container, false);

		rl_ck_batch_all = (RelativeLayout) view
				.findViewById(R.id.rl_ck_batch_all);
		rl_ck_batch_all.setVisibility(View.GONE);
		hsv = (HorizontalScrollView) view.findViewById(R.id.hsv);
		hs_approve_tab = (HorizontalScrollView) view
				.findViewById(R.id.hs_approve_tab);
		ll_condition = (LinearLayout) view.findViewById(R.id.ll_condition);
		ml_approve_list = (MyListView) view.findViewById(R.id.ml_approve_list);

		listAdapter = new ApproveListAdapter(mContext, tasks);
		ml_approve_list.setAdapter(listAdapter);
		ll_approve_condition_container = (RelativeLayout) view
				.findViewById(R.id.ll_approve_condition_container);

		footView = (LinearLayout) inflater.inflate(R.layout.list_foot, null);
		moreText = (TextView) footView.findViewById(R.id.text_tips);
		moreProgress = (ProgressBar) footView.findViewById(R.id.bar_progress);
		iv_search = (ImageView) view.findViewById(R.id.iv_search);
		et_search_long = (EditText) view.findViewById(R.id.et_search_long);
		// iv_search.setBackgroundResource(R.drawable.approve_list_search);
		// cb_left.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approve_back,
		// 0, 0, 0);

		// 为页面控件设置监听事件
		setListener();

		btn_save = (Button) getActivity().findViewById(R.id.btn_save);
		btn_save.setVisibility(View.GONE);
		btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_save.setVisibility(View.GONE);
		btn_next.setText("扫描");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.GONE);
		ll_dialog = (LinearLayout) view.findViewById(R.id.ll_dialog);

		// File f = new File(path);
		// if (f.exists()) {
		// new ApproveTabTask().execute();
		// new ApproveNumberTask().execute();
		// }

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private boolean setupRefresh = false;

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		if (loadProgress != null)
			loadProgress.dismiss();
	}

	@SuppressLint("SdCardPath")
	@Override
	public void onResume() {
		super.onResume();
		LogUtil.i("UploadDate","================>");
//		try {
//
//			helper = new DatabaseHelper(mContext);
////			connection_For_all = SQLiteData.openOrCreateDatabase(getActivity());
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

//		File f = new File(path);

//		if (f.exists()) {
			new ApproveTabTask().execute();
			new ApproveNumberTask().execute();
//		}

		// if (!(subsystemaccount == null || "".equals(subsystemaccount))) {//
		// 请求子帐号系统
		// new Subsystemaccount().execute();
		// } else {
		// // 获取标签的数据
		// if (groupTabs == null || groupTabs.getChildCount() == 0) {
		// new ApproveTabTask().execute();
		// }
		// // 如果是切换子帐号，那么也需要重新获取数据
		// if (setupRefresh) {
		// new ApproveTabTask().execute();
		// new ApproveNumberTask().execute();
		// setupRefresh = false;
		// }
		//
		// }
	}

	// 这个广播的作用是，在切换子帐号之后，需要刷新下数据
	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null) {
				setupRefresh = intent.getBooleanExtra("refresh", false);
			}
		}
	};

	/**
	 * 设置监听事件
	 */
	private void setListener() {
		iv_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				searchDataEdit();
			}
		});
		ml_approve_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// if ("1".equalsIgnoreCase(tasks.get(position -
				// 1).getCanopen())) {

/*//				进度条
				new Thread(new Runnable() {
					@Override
					public void run() {

						handler.sendEmptyMessage(LOADING);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(Over);

					}
				}).start();
				*/

				Fragment f = new AnJianDetailFragment(
						AnJianApproveFragment.this, targetContainer);
				Bundle bundle = new Bundle();
				bundle.putSerializable("entry", menuModule);
				bundle.putString("id", tasks.get(position - 1).getId());
				bundle.putString("tabType", currentType);
				bundle.putString("cmSchedID", cmSchedID);
				bundle.putBoolean("isBatch", false);// 普通审批
				bundle.putString("currNote", tasks.get(position - 1)
						.getCurrNode());
				bundle.putString("displayDevice", tasks.get(position - 1)
						.getDisplayDevice());
				bundle.putString("deviceResult", tasks.get(position - 1)
						.getDeviceResult());
				f.setArguments(bundle);
				f.setTargetFragment(AnJianApproveFragment.this, 0);
				((ActivityInTab) mActivity).navigateTo(f);
				// }
			}
		});
		ml_approve_list.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 判断listview是否停止滑动并且处于底部
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& isRefreshFoot && (!isMoreLoad)) {
					isMoreLoad = true; // 用布尔作为开关，防止在加载数据时，出现多次启动线程加载数据
					moreText.setText("数据加载中...");
					moreProgress.setVisibility(View.VISIBLE);

					// 数据加载
					new Thread(new Runnable() {
						public void run() {
							if (tasks != null && tasks.size() > 0) {
								String itemId = tasks.get(tasks.size() - 1)
										.getId();
								String rownumber = tasks.size() + "";
								String path = realListPath + "&key1="
										+ currentType;
								InputStream inputStream = null;
								try {
									// inputStream = getActivity().getAssets()
									// .open("approvelist.xml");
									dataSize += 10;
									selectList(currentType, dataSize);
									inputStream = new ByteArrayInputStream(sb
											.toString().getBytes("UTF-8"));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								List<Task> temp = DataCollectionUtils
										.receiveApproveTabList1(inputStream,
												path, currentType, searchXML,
												rownumber, pageSize + "",
												itemId, getActivity());
								// 发送消息
								Message msg = mUIHandler
										.obtainMessage(FLAG_MORE);
								msg.obj = temp;
								msg.sendToTarget();
							}
						}
					}).start();
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				ml_approve_list.setFirstItemIndex(firstVisibleItem);

				// 判断是否滑动到底部
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					isRefreshFoot = true;
					Log.d("", "滑到底部");
				} else {
					isRefreshFoot = false;
				}
			}
		});
		ml_approve_list.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				isMoreLoad = true;
				new Thread(new Runnable() {
					public void run() {
						String itemId = "";
						String rownumber = "0";
						String path = realListPath + "&key1=" + currentType;
						InputStream inputStream = null;
						try {
							// inputStream = getActivity().getAssets().open(
							// "approvelist.xml");
							dataSize = 10;
							selectList(currentType, dataSize);
							inputStream = new ByteArrayInputStream(sb
									.toString().getBytes("UTF-8"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						List<Task> temp = DataCollectionUtils
								.receiveApproveTabList1(inputStream, path,
										currentType, searchXML, rownumber,
										pageSize + "", itemId, getActivity());
						// 发送消息
						Message msg = mUIHandler.obtainMessage(FLAG_REFRESH);
						msg.obj = temp;
						msg.sendToTarget();
					}
				}).start();
			}
		});
	}

	/**
	 * 获取标签数据
	 *
	 * @author WK
	 *
	 */

	private class ApproveTabTask extends
			AsyncTask<Void, Void, List<ApproveTab>> {
		// private ProgressHUD overlayProgress;
		private boolean pro = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (Constants.istab == true) {
				Constants.istab = false;
			} else {
				if (Constants.isFirst == true) {
					// if(loadProgress == null){
					// loadProgress = AlertUtils.showDialog(mActivity, null,
					// null, false);
					// }else if(!loadProgress.isShowing()){
					// loadProgress = AlertUtils.showDialog(mActivity, null,
					// null, false);
					// }
					// loadProgress.setOnCancelListener(new OnCancelListener() {
					//
					// @Override
					// public void onCancel(DialogInterface dialog) {
					// // TODO Auto-generated method stub
					// pro=false;
					// }
					// });
					ll_dialog.setVisibility(View.VISIBLE);
				}
			}

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			// ml_approve_list.onRefreshComplete();
		}

		@Override
		protected void onPostExecute(List<ApproveTab> result) {
			super.onPostExecute(result);
			// if (loadProgress != null)
			// loadProgress.dismiss();
			ll_dialog.setVisibility(View.GONE);
			if (result != null && result.size() > 0) {
				List<ApproveTab> tabs = new ArrayList<ApproveTab>();
				batchTabs = new ArrayList<ApproveTab>();
				// 判断批量审批是否存在
				for (ApproveTab tab : result) {
					if (!tab.isBatch) {
						tabs.add(tab);
					} else
						batchTabs.add(tab);
				}
				// if (batchTabs != null && batchTabs.size() > 0) {
				// btn_next.setVisibility(View.VISIBLE);
				// } else
				// btn_next.setVisibility(View.GONE);
				// //普通审批
				currentTab = tabs.get(0);
				initTabs(tabs); // 创建选项卡
				initConditions(tabs.get(0)); // 创建搜索控件
				// 获取完任务列表数据,设置list adapter
				listAdapter = new ApproveListAdapter(mContext, tasks);
				ml_approve_list.setAdapter(listAdapter);

			}
		}

		@Override
		protected List<ApproveTab> doInBackground(Void... params) {
			// if (!pro) {
			// return new ArrayList<ApproveTab>();
			// }
			InputStream inputStream = null;
			try {
				inputStream = getActivity().getAssets().open("tabs_aj.xml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<ApproveTab> list = DataCollectionUtils
					.receiveApproveTabCondition1(inputStream, listPath,
							getActivity());
			// if (pro) {
			return list;
			// }else {
			// return new ArrayList<ApproveTab>();
			// }

		}
	}

	/**
	 * 获取待处理事件数量
	 *
	 * @author Administrator ic_sunbox_launcher
	 */
	private class ApproveNumberTask extends
			AsyncTask<String, Void, List<String[]>> {
		@Override
		protected void onPostExecute(List<String[]> result) {
			super.onPostExecute(result);
			if (mActivity == null)
				return;
			if (mActivity.isFinishing() || !isAdded()) {
				return;
			}

			if (result != null && result.size() > 0 && isAdded()) {
				// //
				// Intent intent = new Intent(
				// Constants.getBroadcastNamePairsString(pageIndex));
				// intent.putExtra("type", "freshNum");
				// intent.putExtra("id", menuModule.id);
				// intent.putExtra("value", (Serializable) result);
				// mActivity.sendBroadcast(intent);
				if (groupTabs != null) {
					for (int i = 0; i < groupTabs.getChildCount(); i++) {
						TagDigitalButton rb = (TagDigitalButton) groupTabs
								.getChildAt(i);
						ApproveTab at = (ApproveTab) rb.getTag();
						for (String[] numbers : result) {
							String tabId = numbers[0];
							String count = numbers[1];
							if (at.id.equalsIgnoreCase(tabId)) {
								// added
								//
								if ("dbsy".equalsIgnoreCase(tabId)) {
									try {
										DataCache.taskCount.put(menuModule.id,
												Integer.parseInt(count));
										Intent intentNum = new Intent(
												Constants.MODIFY_APP_MAIN_SKIN);
										intentNum.putExtra("type",
												"updateTabNum");
										mActivity.sendBroadcast(intentNum);
										mActivity.sendBroadcast(new Intent(
												Constants.MODIFY_APP_MENU_NUM));
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
								if (!TextUtils.isEmpty(count)) {
									// if (Integer.parseInt(count) > 99) {
									// rb.setTabNumber(99);
									// } else {
									rb.setTabNumber(Integer.parseInt(count));
									// }

								} else {
									rb.setTabNumber(0);
								}
							}
						}
					}
				}
				if (group == null || group.menuobjObjects == null) {

				} else {
					// 如果有二级导航的话，
					for (int i = 0; i < group.menuobjObjects.size(); i++) {
						if (group.menuobjObjects.get(i) instanceof SinopecMenuModule) {
							SinopecMenuModule smm = (SinopecMenuModule) group.menuobjObjects
									.get(i);
							if (smm != menuModule) {
								if (DataCache.taskCount.get(smm.id) == null)
									continue;
								// 忽略当前的module值，计算个数
								int count = Integer.parseInt(result.get(0)[1])
										+ DataCache.taskCount.get(smm.id);
								result.get(0)[1] = count + "";
							}
						}
					}
					Intent intent2 = new Intent(Constants.Receiver_Group2);
					intent2.putExtra("smm", menuModule);
					mContext.sendBroadcast(intent2);
				}
				Intent intent = new Intent(Constants.MODIFY_APP_MENU_NUM);
				intent.putExtra("type", "freshNum");
				intent.putExtra("id", menuModule.id);
				intent.putExtra("value", (Serializable) result);

				int total = 0;
				for (String[] number : result) {
					int tempNumber = 0;
					try {
						if (!TextUtils.isEmpty(number[1])) {
							tempNumber = Integer.parseInt(number[1]);
						}
					} catch (Exception e) {
						tempNumber = 0;
						e.printStackTrace();
					}
					total += tempNumber;
				}
				DataCache.taskCount.put(menuModule.id, total);
				mActivity.sendBroadcast(intent);
				Intent intentNum = new Intent(Constants.MODIFY_APP_MAIN_SKIN);
				intentNum.putExtra("type", "updateTabNum");
				mActivity.sendBroadcast(intentNum);
				// added by wangst
			} else {
				if (mActivity.isFinishing() || !isAdded()) {
					return;
				}
				List<String[]> counts = new ArrayList<String[]>();
				Intent intent = new Intent(
						Constants.getBroadcastNamePairsString(pageIndex));
				intent.putExtra("type", "freshNum");
				intent.putExtra("id", menuModule.id);
				intent.putExtra("value", (Serializable) counts);
				mActivity.sendBroadcast(intent);
				mActivity.sendBroadcast(new Intent(
						Constants.MODIFY_APP_MENU_NUM));// added by wangst
				if (groupTabs != null) {
					for (int i = 0; i < groupTabs.getChildCount(); i++) {
						TagDigitalButton rb = (TagDigitalButton) groupTabs
								.getChildAt(i);
						rb.setTabNumber(0);
					}
				}
			}
		}

		// <?xml version="1.0" encoding="utf-8" ?>
		// <root>
		// <Tabs>
		// <Tab id="db" count="106"></Tab >
		// </Tabs>
		// </root>

		@Override
		protected List<String[]> doInBackground(String... params) {

			StringBuilder sbBuilder = new StringBuilder();
			sbBuilder
					.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><Root><Tabs>");
			sbBuilder.append("<Tab id=" + "\"1\"" + " title=" + "\"待抄\""
					+ " count=" + "\"" + selectSize("1") + "\"" + ">");
			sbBuilder.append("</Tab>");
			sbBuilder.append("<Tab id=" + "\"2\"" + " title=" + "\"已上传\""
					+ " count=" + "\"" + selectSize("2") + "\"" + ">");
			sbBuilder.append("</Tab>");
			sbBuilder.append("<Tab id=" + "\"3\"" + " title=" + "\"已保存\""
					+ " count=" + "\"" + selectSize("3") + "\"" + ">");
			sbBuilder.append("</Tab>");
			sbBuilder.append("</Tabs></Root>");
			System.out
					.println(sbBuilder.toString() + "===========size========");
			String path = numberPath;
			// if (null == path || "".equalsIgnoreCase(path)) {
			// return null;
			// }
			InputStream inputStream = null;
			try {
				inputStream = new ByteArrayInputStream(sbBuilder.toString()
						.getBytes("UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return DataCollectionUtils.receiveApproveNumberNew1(inputStream,
					path);
		}
	}

	/**
	 * 获取审批列表信息
	 *
	 */
	private void loadData() {
		ll_dialog.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			public void run() {
				String path = realListPath + "&key1=" + currentType;
				if ("true".equalsIgnoreCase(chercked)) {
					String str = collectSearchCondition();
					try {
						searchXML = URLEncoder.encode(str, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					searchXML = "";
				}
				String rownumber = "0";
				// pageSize = DataCollectionUtils.getPageSize();
				String itemId = "";
				InputStream inputStream = null;
				try {
					dataSize = 10;
					selectList(currentType, dataSize);
					inputStream = new ByteArrayInputStream(sb.toString()
							.getBytes("UTF-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				List<Task> temp = DataCollectionUtils.receiveApproveTabList1(
						inputStream, path, currentType, searchXML, rownumber,
						pageSize + "", itemId, getActivity());
				// 发送消息
				Message msg = mUIHandler.obtainMessage(FLAG_LOAD_DATA);
				msg.obj = temp;
				msg.sendToTarget();
			}
		}).start();
	}

	/**
	 * 搜索
	 */
	public void searchData() {
		ll_dialog.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			public void run() {
				String path = realListPath + "&key1=" + currentType;
				String str = collectSearchCondition();
				try {
					searchXML = URLEncoder.encode(str, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String rownumber = "0";
				pageSize = DataCollectionUtils.getPageSize();
				String itemId = "";
				InputStream inputStream = null;
				try {
					// inputStream = getActivity().getAssets().open(
					// "approvelist.xml");
					// selectList(currentType);

					String searchSPKey = "";
					for (Map.Entry<String, String> entry : conditionParams
							.entrySet()) {
						String name = entry.getKey();
						if (name.equals("spinner")) {
							searchSPKey = entry.getValue();
						}
					}
					if (!searchSPKey.equals("")) {
						searchSpinner(searchSPKey, currentType);
						inputStream = new ByteArrayInputStream(sb.toString()
								.getBytes("UTF-8"));
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<Task> temp = DataCollectionUtils.receiveApproveTabList1(
						inputStream, path, currentType, searchXML, rownumber,
						pageSize + "", itemId, getActivity());
				// 发送消息
				Message msg = mUIHandler.obtainMessage(FLAG_SEARCH);
				msg.obj = temp;
				msg.sendToTarget();
			}
		}).start();
	}

	/**
	 * 搜索
	 */
	public void searchDataDate(final String dateString) {
		ll_dialog.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			public void run() {
				String path = realListPath + "&key1=" + currentType;
				String str = collectSearchCondition();
				try {
					searchXML = URLEncoder.encode(str, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String rownumber = "0";
				pageSize = DataCollectionUtils.getPageSize();
				String itemId = "";
				InputStream inputStream = null;
				try {
					// inputStream = getActivity().getAssets().open(
					// "approvelist.xml");
					// selectList(currentType);

					searchDate(dateString, currentType);
					inputStream = new ByteArrayInputStream(sb.toString()
							.getBytes("UTF-8"));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<Task> temp = DataCollectionUtils.receiveApproveTabList1(
						inputStream, path, currentType, searchXML, rownumber,
						pageSize + "", itemId, getActivity());
				// 发送消息
				Message msg = mUIHandler.obtainMessage(FLAG_SEARCH);
				msg.obj = temp;
				msg.sendToTarget();
			}
		}).start();
	}

	/**
	 * 搜索
	 */
	public void searchDataEdit() {
		ll_dialog.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			public void run() {
				String path = realListPath + "&key1=" + currentType;
				String str = collectSearchCondition();
				try {
					searchXML = URLEncoder.encode(str, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String rownumber = "0";
				pageSize = DataCollectionUtils.getPageSize();
				String itemId = "";
				InputStream inputStream = null;
				try {
					// inputStream = getActivity().getAssets().open(
					// "approvelist.xml");
					// selectList(currentType);
					String searchEtKey = "";

					for (Map.Entry<String, String> entry : conditionParams
							.entrySet()) {
						String name = entry.getKey();
						if (name.equals("username")) {
							searchEtKey = entry.getValue();
						}
					}
					if (!searchKey.getText().toString().trim().equals("")) {

//						String selectStr=searchKey.getText().toString().trim();
//						String replaceAllStr = selectStr.replaceAll(" ", ".*");

						searchList(searchKey.getText().toString().trim(),
								currentType);
						inputStream = new ByteArrayInputStream(sb.toString()
								.getBytes("UTF-8"));
						List<Task> temp = DataCollectionUtils
								.receiveApproveTabList1(inputStream, path,
										currentType, searchXML, rownumber,
										pageSize + "", itemId, getActivity());
						// 发送消息
						Message msg = mUIHandler.obtainMessage(FLAG_SEARCH);
						msg.obj = temp;
						msg.sendToTarget();
					} else {
						loadData();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * 线程处理结果
	 */
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			// initConditions2(currentTab);
			switch (msg.what) {
			case FLAG_LOAD_DATA: {// 读取数据

				// if (loadProgress!=null) {
				// loadProgress.dismiss();
				// }
				ll_dialog.setVisibility(View.GONE);
				if (msg.obj != null) {
					List<Task> tempList = (List<Task>) msg.obj;
					if (tempList != null && tempList.size() > 0) {
						tasks.clear();
						tasks.addAll(tempList);
						moreText.setText("加载更多数据");
						if (hasFootView) {
							ml_approve_list.removeFooterView(footView);
						}
						if (tasks.size() >= pageSize) {
							ml_approve_list.addFooterView(footView);
							hasFootView = true;
							isMoreLoad = false;
						} else {
							isMoreLoad = true;
						}
						listAdapter.setListData(tasks);
					} else {
						if (hasFootView) {
							ml_approve_list.removeFooterView(footView);
							hasFootView = false;
						}
						tasks.clear();
						listAdapter.setListData(tasks);
						Toast.makeText(mActivity, "暂无信息，请尝试刷新！", Toast.LENGTH_SHORT).show();
					}

				}
				// 获取数字个数
				new ApproveNumberTask().execute();
				break;
			}
			case FLAG_REFRESH: {// 刷新数据
				isMoreLoad = false;
				if (msg.obj != null) {
					List<Task> tempList = (List<Task>) msg.obj;
					if (tempList != null && tempList.size() > 0) {
						tasks.clear();
						tasks.addAll(tempList);
						moreText.setText("加载更多数据");
						if (hasFootView) {
							ml_approve_list.removeFooterView(footView);
						}
						if (tasks.size() >= pageSize) {
							ml_approve_list.addFooterView(footView);
							hasFootView = true;
							isMoreLoad = false;
						} else {
							isMoreLoad = true;
						}
						listAdapter.setListData(tasks);
					} else {
						// 如果当前返回的就是没有数据 把tasks清空下
						tasks.clear();
						listAdapter.setListData(tasks);
					}
					ml_approve_list.onRefreshComplete();
				}
				new ApproveNumberTask().execute();
				break;
			}
			case FLAG_MORE: {// 加载更多
				if (msg.obj != null) {
					isMoreLoad = false;
					moreText.setText("加载更多数据");
					moreProgress.setVisibility(View.GONE);

					List<Task> templist = (List<Task>) msg.obj;
					if (templist != null && templist.size() >= 0) {
						tasks.clear();
						if (templist.isEmpty()) {// 列表数据为空
							// 改变按钮状态
							moreText.setText("没有更多数据");
							moreProgress.setVisibility(View.GONE);
						}
						tasks.addAll(templist);
						listAdapter.setListData(tasks);
					}

				}
				break;
			}
			case FLAG_SEARCH: {// 查询数据

				// if (loadProgress!=null) {
				// loadProgress.dismiss();
				// }
				ll_dialog.setVisibility(View.GONE);
				if (msg.obj != null) {
					List<Task> tempList = (List<Task>) msg.obj;
					if (tempList != null && tempList.size() > 0) {
						tasks.clear();
						tasks.addAll(tempList);
						moreText.setText("加载更多数据");
						if (hasFootView) {
							ml_approve_list.removeFooterView(footView);
						}
						if (tasks.size() >= pageSize) {
							ml_approve_list.addFooterView(footView);
							hasFootView = true;
							isMoreLoad = false;
						} else {
							isMoreLoad = true;
						}
						listAdapter.setListData(tasks);
					} else {
						if (hasFootView) {
							ml_approve_list.removeFooterView(footView);
							hasFootView = false;
						}
						listAdapter.setListData(tempList);
						Toast.makeText(mActivity, "没有符合条件的信息！", Toast.LENGTH_SHORT).show();
					}

				}
				break;

			}
			case REFRESH:
				conditionParamsIsNull();
				break;
			}
		}
	};

	/**
	 * 动态创建选项卡
	 *
	 * @param approveTabs
	 */
	public void initTabs(List<ApproveTab> approveTabs) {
		currentType = currentTab.id;
		hs_approve_tab.removeAllViews(); // 先清空数据
		RadioGroup rGroup = new RadioGroup(mContext);
		groupTabs = rGroup;
		rGroup.removeAllViews();
		rGroup.clearCheck();
		rGroup.setGravity(Gravity.CENTER);
		rGroup.setOrientation(RadioGroup.HORIZONTAL);
		hs_approve_tab.addView(rGroup);
		// RadioGroup.LayoutParams rll = new
		// RadioGroup.LayoutParams((screenWidthDip - 30) / 4,
		// getResources().getDimensionPixelOffset(
		// R.dimen.inbox_mail_height));
		RadioGroup.LayoutParams rll = new RadioGroup.LayoutParams(mContext
				.getResources().getDimensionPixelOffset(
						R.dimen.inbox_tabs_width), mContext.getResources()
				.getDimensionPixelOffset(R.dimen.inbox_mail_height));
		rll.setMargins(6, 0, 0, 0);
		for (int i = 0; i < approveTabs.size(); i++) {
			ApproveTab tab = approveTabs.get(i);
			final TagDigitalButton rButton = new TagDigitalButton(mContext);
			rButton.setTextAppearance(mContext, R.style.style_approve_tab_text);
			rButton.setBackgroundResource(R.drawable.rt_scroll_selected);// rt_approve_tab_selected
			rButton.setGravity(Gravity.CENTER);
			rButton.setLayoutParams(rll);
			rGroup.addView(rButton, i);
			rButton.setText(tab.title);
			rButton.setTextColor(Color.BLACK);
			rButton.setTextSize(18);
			rButton.setTag(tab);
			rButton.setId(i);

			// 设置默认选中的radiobutton
			if (currentTab == tab) {
				rButton.setChecked(true);
			} else {
				rButton.setChecked(false);
			}
			rButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					tasks.clear();
					listAdapter.setListData(tasks);
					ApproveTab ro = (ApproveTab) rButton.getTag();
					currentTab = ro;
					currentType = currentTab.id;
					initConditions(currentTab);
				}
			});

		}
	}

	private void loadTabListData() {
		SearchCondition searchCondition = currentTab.searchCondition;
		if (searchCondition.selects != null
				&& searchCondition.selects.size() > 0) {
			for (UIDropSelect select : searchCondition.selects) {
				for (UIDropOption option : select.options) {
					if ("true".equalsIgnoreCase(option.checked)) {
						chercked = option.checked;
					}
				}
			}
		}
		// 获取审批列表信息
		loadData();
	}

	// 获取完标签数据之后，看看有没有搜索条件，如果有，创建出对应的搜索控件
	public void initConditions(ApproveTab approveTab) {
		// 清除数据
		ll_condition.removeAllViews();
		conditionMap.clear();
		conditionParams.clear();

		if (approveTab == null || approveTab.equals("")) {
			return;
		}
		SearchCondition condition = approveTab.searchCondition;
		if (condition.dates != null && condition.dates.size() > 0) {
			// for (UIDate date : condition.dates) {
			// createDate(date);
			// }
			for (int i = 0; i < condition.dates.size(); i++) {
//				 createDate(condition.dates.get(i), i); //取消时间显示搜索控件
			}
		}

		// 这个先放下，以后在说，
		if (condition.orderBys != null && condition.orderBys.size() > 0) {
			for (UIDropOrderBy order : condition.orderBys) {
				createOrderBy(order);
			}
		}

		// 搜索条件中，有可能显示spinner跟edittext，有可能只显示edittext，这儿判断这两种情况
		if ((condition.selects == null || condition.selects.size() == 0)
				&& (condition.orderBys == null || condition.orderBys.size() == 0)
				&& condition.inputs != null && condition.inputs.size() > 0
				&& condition.dates != null && condition.dates.size() > 0) {
			isEditChange = true;
		} else {
			isEditChange = false;
		}
		// 创建spinner
		if (condition.selects != null && condition.selects.size() > 0) {
			for (UIDropSelect select : condition.selects) {
				createSpinner(select);
			}
		}
//		创建搜索输入框
		if (condition.inputs != null && condition.inputs.size() > 0) {
			for (UITextInput input : condition.inputs) {
				createInput(input);
			}
			ll_approve_condition_container.setVisibility(View.VISIBLE);
		} else {
			ll_approve_condition_container.setVisibility(View.GONE);
		}
		// 创建搜索控件之后，获取列表数据
		loadTabListData();
	}

	// 获取完标签数据之后，看看有没有搜索条件，如果有，创建出对应的搜索控件
	public void initConditions2(ApproveTab approveTab) {
		// 清除数据
		ll_condition.removeAllViews();
		conditionMap.clear();
		conditionParams.clear();

		if (approveTab == null || approveTab.equals("")) {
			return;
		}
		SearchCondition condition = approveTab.searchCondition;
		if (condition.dates != null && condition.dates.size() > 0) {
			// for (UIDate date : condition.dates) {
			// createDate(date);
			// }
			for (int i = 0; i < condition.dates.size(); i++) {
				createDate(condition.dates.get(i), i);
			}
		}
		// 创建spinner
		if (condition.selects != null && condition.selects.size() > 0) {
			for (UIDropSelect select : condition.selects) {
				createSpinner(select);
			}
		}

		// 这个先放下，以后在说，
		if (condition.orderBys != null && condition.orderBys.size() > 0) {
			for (UIDropOrderBy order : condition.orderBys) {
				createOrderBy(order);
			}
		}

		// 搜索条件中，有可能显示spinner跟edittext，有可能只显示edittext，这儿判断这两种情况
		if ((condition.selects == null || condition.selects.size() == 0)
				&& (condition.orderBys == null || condition.orderBys.size() == 0)
				&& condition.inputs != null && condition.inputs.size() > 0
				&& condition.dates != null && condition.dates.size() > 0) {
			isEditChange = true;
		} else {
			isEditChange = false;
		}
		if (condition.inputs != null && condition.inputs.size() > 0) {
			for (UITextInput input : condition.inputs) {
				createInput(input);
			}
			ll_approve_condition_container.setVisibility(View.VISIBLE);
		} else {
			ll_approve_condition_container.setVisibility(View.GONE);
		}

		// 创建搜索控件之后，获取列表数据
	}

	// 时间选择
	private void createDate(final UIDate input, final int i) {
		final Button button = new Button(mContext);
		button.setBackgroundResource(R.drawable.approve_list_edittext);
		LayoutParams params = new LayoutParams(
				cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 100),
				cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 42));

		// getResources()
		// .getDimensionPixelOffset(R.dimen.inbox_mail_width),
		// getResources().getDimensionPixelOffset(
		// R.dimen.inbox_mail_height));
		// params.setMargins(5, 0, 5, 0);
		button.setLayoutParams(params);
		button.setId(100 + i);
		ll_condition.addView(button);
		conditionMap.put(input, button);
		if (input.dateValue.equals("") || input.dateValue == null) {
			if (!date.equals("")) {
				button.setText(date);
			} else {
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				String date = sDateFormat.format(new java.util.Date());
				button.setText(date);
			}

		} else {
			button.setText(input.dateValue);
		}
		button.setTextColor(Color.BLACK);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// birth = new SelectBirthday(getActivity()
		// ,button.getId(),input.dateType);
		// birth.showAtLocation(MobileOfficeListFragmentBak.this.getView().findViewById(R.id.ll_root),
		// Gravity.BOTTOM, 0, 0);
		// }
		// });
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// button.setClickable(false);
				// TODO Auto-generated method stub
				/*if ("year".equalsIgnoreCase(input.dateType)) {
					startActivityForResult(new Intent(mContext,
							TimePickerYearActivity.class), v.getId());
				} else if ("year-month".equalsIgnoreCase(input.dateType)) {
					startActivityForResult(new Intent(mContext,
							TimePickerMonthActivity.class), v.getId());
				} else
					startActivityForResult(new Intent(mContext,
							TimePickerActivity.class), v.getId());*/
			}
		});
	}

	// 创建spinner
	private void createSpinner(final UIDropSelect select) {
		spFlag = false;
		// String[] options = new String[select.options.size()];
		//
		// int i = 0;
		// for (UIDropOption option : select.options) {
		// options[i] = option.name;
		// i++;
		// }
		final String[] options = selectSpinnerData();
		Spinner spinner = new Spinner(mContext);
		spinner.setBackgroundResource(R.drawable.ic_approve_spinner_background);
		LayoutParams params = new LayoutParams(getResources()
				.getDimensionPixelOffset(R.dimen.inbox_mail_width),
				cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 42));
		params.setMargins(3, 0, 0, 0);
		spinner.setLayoutParams(params);
		spinner.setPadding(5, 0, 0, 0);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!spFlag) {
					spFlag = true;
					// searchData();
					return;
				}
				conditionParams.put(select.name, options[arg2]);
				searchData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// conditionParams.put(select.name, select.options.get(0).value);
		conditionMap.put(select, spinner);
		ll_condition.addView(spinner);
	}

	private void createOrderBy(final UIDropOrderBy orderby) {
		String[] options = new String[orderby.options.size()];
		int i = 0;
		for (UIDropOption option : orderby.options) {
			options[i] = option.name;
			i++;
		}
		spinner = new Spinner(mContext);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!spFlag) {
					spFlag = true;
					return;
				}
				conditionParams.put(orderby.name,
						orderby.options.get(arg2).value);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		spinner.setBackgroundResource(R.drawable.ic_approve_spinner_background);
		LayoutParams params = new LayoutParams(getResources()
				.getDimensionPixelOffset(R.dimen.inbox_mail_width),
				getResources().getDimensionPixelOffset(
						R.dimen.inbox_mail_height));
		params.setMargins(3, 0, 0, 0);
		spinner.setLayoutParams(params);
		spinner.setPadding(5, 0, 0, 0);
		adapterSpinner = new ArrayAdapter<String>(mContext,
				R.layout.spinner_item, options);
		adapterSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapterSpinner);
		conditionMap.put(orderby, spinner);
		ll_condition.addView(spinner);
	}

	// 创建输入框
	private void createInput(UITextInput input) {
		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true);
		editText.setHint("请输入...");
		LayoutParams params = new LayoutParams(
				cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 100),
				cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 42));
		params.setMargins(10, 2, 10, 2);
		editText.setLayoutParams(params);
		if (isEditChange) { // 只有输入框
			et_search_long.setVisibility(View.VISIBLE);
			et_search_long.setText("");
			// editText = et_search_long;
			hsv.setVisibility(View.VISIBLE);
		} else {
			editText.setBackgroundResource(R.drawable.approve_list_edittext);
			et_search_long.setVisibility(View.GONE);
			ll_condition.addView(editText);
		}
		searchKey = editText;
		conditionMap.put(input, editText);

	}

	/**
	 * 组拼搜索XML
	 *
	 * @return
	 */
	private String collectSearchCondition() {
		// StringBuffer sb = new StringBuffer();
		// if (conditionParamsIsNull()) {
		// return sb.toString();
		// } else {
		// sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		// .append("<root>").append("<PageNumber/>")
		// .append("<PageSize>").append(pageSize)
		// .append("</PageSize>").append("<tab id=\"")
		// .append(currentType).append("\">");
		// for (Map.Entry<String, String> entry : conditionParams.entrySet()) {
		// String name = entry.getKey();
		// String value = entry.getValue();
		// sb.append("<item name=\"").append(name).append("\">")
		// .append(value).append("</item>");
		// }
		// sb.append("</tab></root>");
		// }
		// return sb.toString();
		return "";
	}

	/**
	 * 判断搜索条件是否为空
	 *
	 * @param //conditionParams
	 *            筛选条件Map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean conditionParamsIsNull() {

		conditionParams.clear();
		for (Map.Entry<Object, View> entry : conditionMap.entrySet()) {
			Object object = entry.getKey();
			View view = entry.getValue();
			if (object instanceof UIDropSelect) {
				UIDropSelect select = (UIDropSelect) object;
				Spinner spinner = (Spinner) view;
				int i = spinner.getSelectedItemPosition();
				String s = select.options.get(i).value;
				conditionParams.put(select.name, s);
				if ("true".equalsIgnoreCase(select.required)
						&& "".equalsIgnoreCase(s.toString())) {
					Toast.makeText(mContext, "请输入搜索条件:" + select.title,
							Toast.LENGTH_SHORT).show();
					return false;
				}
			} else if (object instanceof UIDropOrderBy) {
				UIDropOrderBy orderBy = (UIDropOrderBy) object;
				Spinner spinner = (Spinner) view;
				int i = spinner.getSelectedItemPosition();
				String s = orderBy.options.get(i).value;
				conditionParams.put(orderBy.name, s);
				if ("true".equalsIgnoreCase(orderBy.required)
						&& "".equalsIgnoreCase(s.toString())) {
					Toast.makeText(mContext, "请输入搜索条件:" + orderBy.title,
							Toast.LENGTH_SHORT).show();
					return false;
				}
			} else if (object instanceof UITextInput) {
				UITextInput input = (UITextInput) object;
				EditText editText = (EditText) view;
				conditionParams.put(input.name, editText.getText().toString());
				if ("true".equalsIgnoreCase(input.required)
						&& "".equalsIgnoreCase(editText.getText().toString())) {
					Toast.makeText(mContext, "请在输入框输入搜索条件", Toast.LENGTH_SHORT)
							.show();
					return false;
				}
			} else if (object instanceof UIDate) {
				UIDate input = (UIDate) object;
				Button button = (Button) view;
				// conditionDateParams.put(input.dateName,
				// button.getText().toString());
				conditionParams
						.put(input.dateName, button.getText().toString());
				if ("".equalsIgnoreCase(button.getText().toString())) {
					// Toast.makeText(mContext, "请在输入框输入搜索条件",
					// Toast.LENGTH_SHORT)
					// .show();
					return false;
				}
			}
		}

		List<String> keyList = new ArrayList<String>();
		List<String> valuesList = new ArrayList<String>();
		Iterator iterator = conditionParams.keySet().iterator();
		while (iterator.hasNext()) {
			keyList.add((String) iterator.next());
		}
		for (int i = 0; i < keyList.size(); i++) {
			if (!TextUtils.isEmpty(conditionParams.get(keyList.get(i)))) {
				valuesList.add(conditionParams.get(keyList.get(i)));
			}

		}
		if (valuesList.size() == 0 || valuesList == null) {
			return true;
		}
		return false;
	}

	private void initPathParams(SinopecMenuModule item) {
		for (SinopecMenuPage page : item.menuPages) {
			if ("tab".equalsIgnoreCase(page.code)) {
				listPath = WebUtils.rootUrl + URLUtils.baseContentUrl + page.id;
			} else if ("list".equalsIgnoreCase(page.code)) {
				realListPath = WebUtils.rootUrl + URLUtils.baseContentUrl
						+ page.id;
			} else if ("notification".equalsIgnoreCase(page.code)) {
				numberPath = WebUtils.rootUrl + URLUtils.baseContentUrl
						+ page.id;
			} else if ("subsystemaccount".equalsIgnoreCase(page.code)) {// 请求子帐号
				subsystemaccount = WebUtils.rootUrl + URLUtils.baseContentUrl
						+ page.id;
			}
		}
	}

	private class Subsystemaccount extends
			AsyncTask<Void, Void, ChildAccountsBean> {
		// private ProgressHUD overlayProgress;
		private boolean pro = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// loadProgress = AlertUtils.showDialog(mActivity, null, this,
			// false);
			// loadProgress.setOnCancelListener(new OnCancelListener() {
			//
			// @Override
			// public void onCancel(DialogInterface dialog) {
			// // TODO Auto-generated method stub
			// pro=false;
			// }
			// });
			ll_dialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			// ml_approve_list.onRefreshComplete();
		}

		@Override
		protected void onPostExecute(final ChildAccountsBean result) {
			super.onPostExecute(result);
			// if (loadProgress != null)
			// loadProgress.dismiss();
			ll_dialog.setVisibility(View.GONE);
			if (result != null) {
				if ("0".equals(result.resault)) {// 未配置，需手动配置
					// Bundle bundle = new Bundle();
					// // bundle.putSerializable("childList","");
					// if(result.size()>1){
					// bundle.putString("username",result.get(1));
					// }else
					// bundle.putString("username","");
					// bundle.putString("moduleid",menuModule.id);
					tasks.clear();

					if (listAdapter != null) {
						listAdapter.notifyDataSetChanged();
					}
					if (groupTabs != null)
						groupTabs.removeAllViews();
					ml_approve_list.setOnScrollListener(null);
					new AlertDialog.Builder(mActivity)
							.setTitle("提示")
							.setMessage("没有配置子账号,是否去配置?")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													mActivity,
													SubMoreActivity.class);
											intent.putExtra("username",
													result.detaname);
											intent.putExtra("moduleid",
													menuModule.id);
											intent.putExtra("adusername", sp
													.getString("username", ""));
											startActivity(intent);
											mActivity
													.overridePendingTransition(
															R.anim.fragment_slide_right_enter,
															R.anim.fragment_slide_left_exit);
										}
									}).setNegativeButton("取消", null).create()
							.show();

					// MoreSubChildAccounts childAccounts =
					// MoreSubChildAccounts.newInstance(bundle,
					// SinopecApproveFragment.this);
					// ((ActivityInTab) (mActivity)).navigateTo(childAccounts);
				} else {// 不需要配置

					isRefreshFoot = false;
					ml_approve_list.setOnScrollListener(new OnScrollListener() {
						public void onScrollStateChanged(AbsListView view,
								int scrollState) {
							// 判断listview是否停止滑动并且处于底部
							if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
									&& isRefreshFoot && (!isMoreLoad)) {
								isMoreLoad = true; // 用布尔作为开关，防止在加载数据时，出现多次启动线程加载数据
								moreText.setText("数据加载中...");
								moreProgress.setVisibility(View.VISIBLE);

								// 数据加载
								new Thread(new Runnable() {
									public void run() {
										if (tasks != null && tasks.size() > 0) {
											String itemId = tasks.get(
													tasks.size() - 1).getId();
											String rownumber = tasks.size()
													+ "";
											String path = realListPath
													+ "&key1=" + currentType;
											InputStream inputStream = null;
											try {
												// inputStream = getActivity()
												// .getAssets()
												// .open("approvelist.xml");
												dataSize = 10;
												selectList(currentType,
														dataSize);
												inputStream = new ByteArrayInputStream(
														sb.toString().getBytes(
																"UTF-8"));
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											List<Task> temp = DataCollectionUtils
													.receiveApproveTabList1(
															inputStream, path,
															currentType,
															searchXML,
															rownumber, pageSize
																	+ "",
															itemId,
															getActivity());
											// 发送消息
											Message msg = mUIHandler
													.obtainMessage(FLAG_MORE);
											msg.obj = temp;
											msg.sendToTarget();
										}
									}
								}).start();
							}
						}

						public void onScroll(AbsListView view,
								int firstVisibleItem, int visibleItemCount,
								int totalItemCount) {
							ml_approve_list.setFirstItemIndex(firstVisibleItem);

							// 判断是否滑动到底部
							if (firstVisibleItem + visibleItemCount == totalItemCount) {
								isRefreshFoot = true;
								Log.d("", "滑到底部");
							} else {
								isRefreshFoot = false;
							}
						}
					});
					// 获取标签的数据
					if (groupTabs == null || groupTabs.getChildCount() == 0) {
						new ApproveTabTask().execute();
					}
					// 如果是切换子帐号，那么也需要重新获取数据
					if (setupRefresh) {
						new ApproveTabTask().execute();
						new ApproveNumberTask().execute();
						setupRefresh = false;
					}
				}
			} else {
				System.out.println("子帐号加载失败");
			}
		}

		@Override
		protected ChildAccountsBean doInBackground(Void... params) {
			// sUsername = sp.getString("username", "");
			// sPassword = sp.getString("password", "");
			ChildAccountsBean accountsBean = DataCollectionUtils
					.checkChildAccounts(subsystemaccount,
							sp.getString("username", ""),
							sp.getString("password", ""), menuModule.id);
			// if (pro) {
			return accountsBean;
			// }else {
			// return null;
			// }
		}
	}

	@Override
	public void refreshOAApprove() {
		// TODO Auto-generated method stub
		initConditions(currentTab);
		// btn_next.setVisibility(View.GONE);
		if (batchTabs != null && batchTabs.size() > 0) {
			btn_next.setVisibility(View.VISIBLE);
		} else
			btn_next.setVisibility(View.GONE);
	}

	@Override
	public void getRefresh() {// 重新请求下数据
		// 获取标签的数据
		if (groupTabs == null || groupTabs.getChildCount() == 0) {
			new ApproveTabTask().execute();
		}
		// 如果是切换子帐号，那么也需要重新获取数据
		if (setupRefresh) {
			new ApproveTabTask().execute();
			new ApproveNumberTask().execute();
			setupRefresh = false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:
			// SinopecApproveBatchFragment fragment = new
			// SinopecApproveBatchFragment();
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("entry", menuModule);
			// bundle.putSerializable("batchtabs", (Serializable) batchTabs);
			// fragment.setArguments(bundle);
			// ((ActivityInTab) mContext).navigateTo(fragment);
			Toast.makeText(mContext, "暂时不支持扫描", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void refreshButtonAndText() {
		// TODO Auto-generated method stub
		super.refreshButtonAndText();
		btn_save.setVisibility(View.GONE);
		btn_next.setText("扫描");
		btn_next.setVisibility(View.GONE);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "暂时不支持扫描", Toast.LENGTH_LONG).show();
			}
		});
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText(menuModule.caption);
		// if(batchTabs!=null&&batchTabs.size()>0){
		// btn_next.setVisibility(View.GONE);
		// btn_next.setText("批量审批");
		// }else
		// btn_next.setVisibility(View.GONE);
		// btn_next.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// SinopecApproveBatchFragment fragment = new
		// SinopecApproveBatchFragment();
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("entry", menuModule);
		// bundle.putSerializable("batchtabs", (Serializable) batchTabs);
		// fragment.setArguments(bundle);
		// ((ActivityInTab) mContext).navigateTo(fragment);
		// }
		// });

//		if(instance==null){

//		}

		new ApproveNumberTask().execute();
//		从新加载当前界面
		loadData();
	}

	protected static final int REFRESH = 5;
//	private Connection conne;
//	private DatabaseHelper conne;
//	private Connection conne1;
//	private Statement stat;
//	private DatabaseHelper conne1;
//	private SQLiteDatabase stat;
	private String cmSchedID;

	@Override
	public void onActivityResult(final int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// switch (requestCode) {
		// case 0:
		if (resultCode == Activity.RESULT_OK) {
			if (data != null) {
				final String cheel_time = data.getStringExtra("CHEEL_TIME");
				if (null == cheel_time || "".equals(cheel_time)) {

					for (int i = 0; i < ll_condition.getChildCount(); i++) {
						if (ll_condition.getChildAt(i).getId() == requestCode) {
							final Button button = (Button) ll_condition
									.getChildAt(i);
							button.setClickable(true);
						}
					}
				} else {
					// Toast.makeText(mContext, cheel_time, 0).show();
					// date = cheel_time;
					// final Button button =
					// (Button)ll_condition.getChildAt(requestCode);
					//
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									for (int i = 0; i < ll_condition
											.getChildCount(); i++) {
										if (ll_condition.getChildAt(i).getId() == requestCode) {
											final Button button = (Button) ll_condition
													.getChildAt(i);
											button.setText(cheel_time);
											searchDataDate(cheel_time);
											button.setClickable(true);
										}
									}
									// mUIHandler.sendEmptyMessage(REFRESH);
								}
							}, 500);
						}
					});
				}
			}
			// }
			// break;
		}
	}

	/**
	 * 审批数据适配器
	 *
	 * @author
	 *
	 */
	public class ApproveListAdapter extends BaseAdapter {

		private LayoutInflater mLayoutInflater;
		private int mResource;
		private List<Task> mSelfData;

		public ApproveListAdapter(Context context, List<Task> data) {
			this.mSelfData = data;
			this.mResource = R.layout.item_approve_list;
			this.mLayoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setListData(List<Task> data) {
			this.mSelfData = data;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSelfData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSelfData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(mResource, null);
			}

			final Task task = mSelfData.get(position);

			if (task != null) {
				TextView tv_subject_pending_item = (TextView) convertView
						.findViewById(R.id.tv_subject_pending_item);
				TextView tv_list_field1 = (TextView) convertView
						.findViewById(R.id.tv_list_field1);
				TextView tv_list_field2 = (TextView) convertView
						.findViewById(R.id.tv_list_field2);
				TextView tv_list_field3 = (TextView) convertView
						.findViewById(R.id.tv_list_field3);
				Button btn_cz = (Button) convertView
						.findViewById(R.id.btn_list);
				LinearLayout layout = (LinearLayout) convertView
						.findViewById(R.id.rl_layout);
				if (task.isFlag()) {
					layout.setBackgroundColor(Color.rgb(239, 241, 248));
				} else {
					layout.setBackgroundColor(Color.rgb(250, 250, 250));
				}
				btn_cz.setBackgroundResource(R.drawable.chaobiao);
				if (!"1".equals(currentType)) {
					btn_cz.setVisibility(View.GONE);
				} else {
					if (task.getCurrNode() != null) {
						if (task.getCurrNode().equals("true")) {
							btn_cz.setVisibility(View.VISIBLE);
						} else {
							btn_cz.setVisibility(View.INVISIBLE);
						}
					}

				}
				btn_cz.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					/*	Fragment f = new PollingDetailFragment(
								AnJianApproveFragment.this, targetContainer);
						Bundle bundle = new Bundle();
						bundle.putSerializable("entry", menuModule);
						bundle.putString("id", task.getId());
						bundle.putString("tabType", currentType);
						bundle.putBoolean("isBatch", false);// 普通审批
						bundle.putString("currNote", task.getCurrNode());
						bundle.putString("displayDevice",
								task.getDisplayDevice());
						bundle.putString("deviceResult", task.getDeviceResult());
						f.setArguments(bundle);
						f.setTargetFragment(AnJianApproveFragment.this, 0);
						((ActivityInTab) mActivity).navigateTo(f);*/
					}
				});
				tv_subject_pending_item.setText(task.getTitle());
				tv_list_field1.setText(task.getField1());
				if (task.getField1() == null || task.getField1().equals("")) {
					tv_list_field1.setVisibility(View.GONE);
				}
				if(task.getField2().contains("null")){
					String aNull = task.getField2().replace("null-", "");
					tv_list_field2.setText(aNull);
				}else{
					tv_list_field2.setText(task.getField2());
				}

				if (task.getField2() == null || task.getField2().equals("")) {
					tv_list_field2.setVisibility(View.GONE);
				}
				tv_list_field3.setText(task.getField3());
				if (task.getField3() == null || task.getField3().equals("")) {
					tv_list_field3.setVisibility(View.GONE);
				}
				if ("0".equalsIgnoreCase(task.getCanopen())) {
					tv_list_field1.setTextColor(Color.rgb(212, 212, 212));
					tv_list_field2.setTextColor(Color.rgb(212, 212, 212));
					tv_list_field3.setTextColor(Color.rgb(212, 212, 212));
					tv_subject_pending_item.setTextColor(Color.rgb(212, 212,
							212));
				} else {
					tv_list_field1.setTextColor(Color.rgb(94, 94, 94));
					tv_list_field2.setTextColor(Color.rgb(94, 94, 94));
					tv_list_field3.setTextColor(Color.rgb(94, 94, 94));
					tv_subject_pending_item.setTextColor(Color.rgb(94, 94, 94));
				}

			}

			convertView.setTag(position);
			return convertView;
		}

	}

	// 查询待办数据
	public void selectList(String state, int size) {
		String sql = "";
		if (state.equals("1")) {
			sql = "select distinct accountId,entityName,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum from custInfo_ju_aj where cmMrState=? and cmSchedId = ? ORDER BY cmMrAddress ASC" + " limit 0 ,?";
		} else {
			sql = "select distinct accountId,entityName,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum from custInfo_ju_aj where cmMrState=? and cmSchedId = ?  ORDER BY cmMrAddress ASC" + " limit 0,?";
		}

		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		Cursor c = null;
		DatabaseHelper	conne=null;
		SQLiteDatabase stat=null;
		try {


			conne = new DatabaseHelper(mContext);
//
////			conne = SQLiteData.openOrCreateDatabase(getActivity());
//

			stat = conne.getWritableDatabase();

//			stat = conne.createStatement();
			// 查询数据返回游标对象
			// Cursor c = db.rawQuery(sql, null);
			c=stat.rawQuery(sql,new String[]{state,cmSchedID,String.valueOf(size)});
//			c = stat.executeQuery(sql);
			// 到了提交的时刻了
			sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><list>");
			while (c.moveToNext()) {
				// 通过游标对象获取值
				sb.append("<listitem>");
				sb.append("<itemid>" + c.getString(0) + "</itemid>");
				sb.append("<title>" + c.getString(1) + "</title>");
				sb.append("<field2>"
//				+ c.getString("cmMrDistrict")
//						+ c.getString("cmMrStreet")
						+ c.getString(2)
						+ c.getString(3) +"#"+c.getString(4)+"-"+c.getString(5)+"</field2>");
				// sb.append("<field3>" + c.getString(6) + "</field3>");
				sb.append("<displayDevice>" + "" + "</displayDevice>");
				// if (selectIsAnJian(c.getString(0))) {
				// sb.append("<currNode>" + "true" + "</currNode>");
				// } else {
				// sb.append("<currNode>" + "false" + "</currNode>");
				// }

				sb.append("<deviceResult>" + "" + "</deviceResult>");
				sb.append("</listitem>");

				/*
				 * // 通过游标对象获取值 sb.append("<listitem>"); sb.append("<itemid>" +
				 * c.getString(0) + "</itemid>"); sb.append("<title>" +
				 * c.getString(4) + c.getString(5) + c.getString(6) +
				 * c.getString(7) + "栋" + c.getString(8) + "单元" + c.getString(9)
				 * + "号" + "</title>"); sb.append("<field3>" + c.getString(1) +
				 * "</field3>"); sb.append("<displayDevice>" + "" +
				 * "</displayDevice>"); if (selectIsChaoBiao(c.getString(0))) {
				 * sb.append("<currNode>" + "true" + "</currNode>"); } else {
				 * sb.append("<currNode>" + "false" + "</currNode>"); }
				 * 
				 * sb.append("<deviceResult>" + "" + "</deviceResult>");
				 * sb.append("</listitem>");
				 */}
//			c.close();

			sb.append("</list></root>");
			System.out.println(sb.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conne != null) {
				try {
					conne.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 根据日期内容查询
	public void searchDate(String searchKey, String state) {

		String sql = "select accountId,entityName,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum from chaobiao_data where cmMrState=? and chaobiao_date=?";

		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		Cursor c = null;
		try {


//			conne1 = new DatabaseHelper(mContext);
////			conne1 = SQLiteData.openOrCreateDatabase(getActivity());
//			stat = conne1.getWritableDatabase();
			// 查询数据返回游标对象
			// Cursor c = db.rawQuery(sql, null);
			c = instance.rawQuery(sql,new String[]{state,searchKey});
			// 到了提交的时刻了
			sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><list>");
			while (c.moveToNext()) {
				// 通过游标对象获取值
				sb.append("<listitem>");
				sb.append("<itemid>" + c.getString(0) + "</itemid>");
				sb.append("<title>" + c.getString(1) + "</title>");
				sb.append("<field2>"
//				+ c.getString("cmMrDistrict")
//						+ c.getString("cmMrStreet")
						+ c.getString(2)
						+ c.getString(3) +"#"+c.getString(4)+"-"+c.getString(5)+"</field2>");
				// sb.append("<field3>" + c.getString(6) + "</field3>");
				sb.append("<displayDevice>" + "" + "</displayDevice>");
				// if (selectIsAnJian(c.getString(0))) {
				// sb.append("<currNode>" + "true" + "</currNode>");
				// } else {
				// sb.append("<currNode>" + "false" + "</currNode>");
				// }

				sb.append("<deviceResult>" + "" + "</deviceResult>");
				sb.append("</listitem>");

				/*
				 * // 通过游标对象获取值 sb.append("<listitem>"); sb.append("<itemid>" +
				 * c.getString(0) + "</itemid>"); sb.append("<title>" +
				 * c.getString(4) + c.getString(5) + c.getString(6) +
				 * c.getString(7) + "栋" + c.getString(8) + "单元" + c.getString(9)
				 * + "号" + "</title>"); sb.append("<field3>" + c.getString(1) +
				 * "</field3>"); sb.append("<displayDevice>" + "" +
				 * "</displayDevice>"); if (selectIsChaoBiao(c.getString(0))) {
				 * sb.append("<currNode>" + "true" + "</currNode>"); } else {
				 * sb.append("<currNode>" + "false" + "</currNode>"); }
				 * 
				 * sb.append("<deviceResult>" + "" + "</deviceResult>");
				 * sb.append("</listitem>");
				 */}
			c.close();

			sb.append("</list></root>");
			System.out.println(sb.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
//			if (stat != null) {
//				try {
//					stat.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (conne1 != null) {
//				try {
//					conne1.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
		}

	}

	// 查询
	public void searchList(String searchKey, String state) {

//		String sql = "select * from custInfo_ju_aj where " + "("
//				+ "entityName like " + "'%"
//				+ searchKey
//				+ "%'"
//				+ " or cmMrDistrict like "
//				+ "'%"
//				+ searchKey
//				+ "%'"
//				+ " or cmMrStreet like "
//				+ "'%"
//				+ searchKey
//				+ "%'"
//				+ " or cmMrCommunity like "
//				+ "'%"
//				+ searchKey
//
//				+ "%'"
//				+ " or cmMrBuilding like "
//				+ "'%"
//				+ searchKey
//				+ "%'"
//				+ " or cmMrUnit like "
//				+ "'%"
//				+ searchKey
//				+ "%'"
//				+ " or cmMrRoomNum like "
//				+ "'%"
//				+ searchKey
//
//				+ "%'"
//				+ ")"
//				+ " and cmMrState= "
//				+ "'"
//				+ state
//				+ "'"
//				+ " and  cmSchedId = " + cmSchedID + " ";


//		String sql="select * from custInfo_ju_aj where cmMrAddress REGEXP "+searchKey
//				+ " and cmMrState= "
//				+ "'"
//				+ state
//				+ "'"
//				+ " and  cmSchedId = " + cmSchedID + " ";

		String sql="select accountId,entityName,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum from custInfo_ju_aj where "+"cmMrAddress like " + "'%"+searchKey+ "%'"
				+ " and cmMrState= "
				+ "'"
				+ state
				+ "'"
				+ " and  cmSchedId = "+ "'" + cmSchedID + "'";

		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		Cursor c = null;
		try {
//			conne=new DatabaseHelper(mContext);
////			conne = SQLiteData.openOrCreateDatabase(getActivity());
//			stat = conne.getWritableDatabase();
			// 查询数据返回游标对象
			// Cursor c = db.rawQuery(sql, null);
			c = instance.rawQuery(sql,new String[]{});//searchKey,state,cmSchedID
			// 到了提交的时刻了
			sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><list>");
			if(c.moveToFirst()){
				for (int i = 0; i < c.getCount(); i++) {
					sb.append("<listitem>");
					sb.append("<itemid>" + c.getString(0) + "</itemid>");
					sb.append("<title>" + c.getString(1) + "</title>");
					sb.append("<field2>"
//				+ c.getString("cmMrDistrict")
//						+ c.getString("cmMrStreet")
							+ c.getString(2)
							+ c.getString(3) +"#"+c.getString(4)+"-"+c.getString(5)+"</field2>");
					// sb.append("<field3>" + c.getString(6) + "</field3>");
					sb.append("<displayDevice>" + "" + "</displayDevice>");
					// if (selectIsAnJian(c.getString(0))) {
					// sb.append("<currNode>" + "true" + "</currNode>");
					// } else {
					// sb.append("<currNode>" + "false" + "</currNode>");
					// }

					sb.append("<deviceResult>" + "" + "</deviceResult>");
					sb.append("</listitem>");
					c.moveToNext();
				}


			}
//			while (c.moveToNext()) {
//				// 通过游标对象获取值
//				sb.append("<listitem>");
//				sb.append("<itemid>" + c.getString(0) + "</itemid>");
//				sb.append("<title>" + c.getString(1) + "</title>");
//				sb.append("<field2>"
////				+ c.getString("cmMrDistrict")
////						+ c.getString("cmMrStreet")
//						+ c.getString(2)
//						+ c.getString(3) +"#"+c.getString(4)+"-"+c.getString(5)+"</field2>");
//				// sb.append("<field3>" + c.getString(6) + "</field3>");
//				sb.append("<displayDevice>" + "" + "</displayDevice>");
//				// if (selectIsAnJian(c.getString(0))) {
//				// sb.append("<currNode>" + "true" + "</currNode>");
//				// } else {
//				// sb.append("<currNode>" + "false" + "</currNode>");
//				// }
//
//				sb.append("<deviceResult>" + "" + "</deviceResult>");
//				sb.append("</listitem>");
//
//			}
			c.close();

			sb.append("</list></root>");
			System.out.println(sb.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
//			if (stat != null) {
//				try {
//					stat.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (conne != null) {
//				try {
//					conne.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
		}

	}

	// 根据下拉框内容查询
	public void searchSpinner(String searchKey, String state) {
		String sql;
		if (searchKey.equals("全部")) {

			sql = "select accountId,entityName,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum from custInfo_ju_aj where cmMrState=" + state
					+ " and cmSchedId = " + cmSchedID + " ";
		} else {
			sql = "select accountId,entityName,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum from custInfo_ju_aj where cmMrState=" + state
					+ " and cmMrCommunity=" + "'" + searchKey + "'"
					+ " and cmSchedId = " + cmSchedID + " ";

		}

		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
		// db = SQLiteDatabase.openOrCreateDatabase(path1, null);
		// 查询数据返回游标对象
		// Cursor c = db.rawQuery(sql, null);
//		DatabaseHelper db = null;
//		SQLiteDatabase stat = null;
		Cursor c=null;
		try {
//			db=new DatabaseHelper(mContext);
//			db = SQLiteData.openOrCreateDatabase(getActivity());

//			 stat = db.getWritableDatabase();


			c = instance.rawQuery(sql,new String[]{});

			// 到了提交的时刻了
			sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><list>");
			String str="";
			if(c.moveToFirst()){
				sb.append("<listitem>");
				sb.append("<itemid>" + c.getString(0) + "</itemid>");
				sb.append("<title>" + c.getString(1) + "</title>");
				 str = "<field2>"
//				+ c.getString("cmMrDistrict")
//						+ c.getString("cmMrStreet")
						+ c.getString(2)
						+ c.getString(3) +"#";
				if(c.getString(4)!=null||!c.getString(4).equals("null") ){
					str+=c.getString(4)+"-";
				}
				str+=c.getString(5)+"</field2>";
				sb.append(str);

				sb.append("<displayDevice>" + "" + "</displayDevice>");
				sb.append("<deviceResult>" + "" + "</deviceResult>");
				sb.append("</listitem>");

				while (c.moveToNext()) {

					sb.append("<listitem>");
					sb.append("<itemid>" + c.getString(0) + "</itemid>");
					sb.append("<title>" + c.getString(1) + "</title>");

					str = "<field2>"
//				+ c.getString("cmMrDistrict")
//						+ c.getString("cmMrStreet")
							+ c.getString(2)
							+ c.getString(3) +"#";
					if(c.getString(4)!=null||!c.getString(4).equals("null") ){
						str+=c.getString(4)+"-";
					}
					str+=c.getString(5)+"</field2>";
					sb.append(str);

					sb.append("<displayDevice>" + "" + "</displayDevice>");
					sb.append("<deviceResult>" + "" + "</deviceResult>");
					sb.append("</listitem>");

				}
			}


			sb.append("</list></root>");
			System.out.println(sb.toString());



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if(c!=null){
				try {
				c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
//			if (stat != null) {
//				try {
//					stat.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			if (db != null) {
//				try {
//					db.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}

	}

	// 查询
	public String[] selectSpinnerData() {
		String sql = "select distinct cmMrCommunity from custInfo_ju_aj where not cmMrCommunity  is null and cmSchedId = "
				+ cmSchedID + "  ";
		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//		DatabaseHelper db = null;
//		SQLiteDatabase state = null;
		Cursor c=null;
		try {

//			db = new DatabaseHelper(mContext);
//
//			state = db.getWritableDatabase();
			// 查询数据返回游标对象
			// Cursor c = db.rawQuery(sql, null);


			c = instance.rawQuery(sql,new String[]{});
//			c.moveToLast();
			String[] options = new String[c.getCount() + 1];
//			c.beforeFirst();
			int i = 0;
			options[0] = "全部";
			if(c.moveToFirst()){
				i=1;
				options[i] = c.getString(0);
				while (c.moveToNext()) {
					options[i + 1] = c.getString(0);
					i++;
				}
			}


			// 到了提交的时刻了



			return options;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(c!=null){
				try{
					c.close();
				}catch (SQLException e){
					e.printStackTrace();
				}

			}

//			if (state != null) {
//				try {
//					state.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			if (db != null) {
//				try {
//					db.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
		return null;

	}

	// 查询待办数量
	public String selectSize(String state) {
//		String sql = "select distinct * from custInfo_ju_aj where cmMrState="
//				+ state + " ";
		String sql = "select distinct * from custInfo_ju_aj where cmMrState= '"
				+ state + "' and cmSchedId = '" + cmSchedID + "' ";
		// 查询数据返回游标对象
		DatabaseHelper db = null;
		SQLiteDatabase stat = null;
		String string = null;
		try {
			db=new DatabaseHelper(mContext);
//			db = SQLiteData.openOrCreateDatabase(getActivity());
			stat = db.getWritableDatabase();
			Cursor c = stat.rawQuery(sql,new String[]{});
			int a = 0;
			// while(c.next()){
			// a++;
			// }
//			c.moveToLast();
			a = c.getCount();
			string = a + "";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (db != null) {
				try {
					db.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return string;

	}

	// 根据id 查询安检信息
	public boolean selectIsChaoBiao(String user_id) {

		String sql = "select * from chaobiao_data where user_id=" + "'"
				+ user_id + "'" + " and state='1'";

		// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//		 db = new DatabaseHelper(mContext);
//		SQLiteDatabase writableDatabase = db.getWritableDatabase();
//		db = SQLiteDatabase.openOrCreateDatabase(path, null);
		// 查询数据返回游标对象
		Cursor c = instance.rawQuery(sql, new String[]{});
		if (c.getCount() > 0) {
			c.close();
			return true;
		}
		c.close();
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(instance!=null){
			try{
				instance.close();
			}catch (Exception e){
				e.printStackTrace();
			}


		}

	}
}
