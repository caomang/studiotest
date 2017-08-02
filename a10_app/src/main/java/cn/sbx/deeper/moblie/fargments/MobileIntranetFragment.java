package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.domian.MobileNewLieb;
import cn.sbx.deeper.moblie.domian.NewsPaper;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SinopecMenuPage;
import cn.sbx.deeper.moblie.domian.TypeItem;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.ImageLoader;
import cn.sbx.deeper.moblie.util.MyHorizontalScrollView;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.MyListView;
import cn.sbx.deeper.moblie.view.MyListView.onScrollEndListener;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;


public class MobileIntranetFragment extends Fragment implements OnClickListener, MyListView.OnRefreshListener {
	private static final String TAG = MobileIntranetFragment.class.getSimpleName();
			
	private MyListView lv_intranet_list;
	private MyHorizontalScrollView horizontalScrollView;
	private Map<String, List<NewsPaper>> intranets = new HashMap<String, List<NewsPaper>>(); // 内网信息列表集合
	private List<TypeItem> list_title = new ArrayList<TypeItem>(); // 标题、列表地址、详细地址的集合
	boolean isPull = false;
	Activity mContext;
	ProgressDialog _pd;
	private String listPath = "";
	private SinopecMenuModule approvalItem;        
	private int screenHalf = 0;
	private String detailId = "";
	private String typeItem_name = "";
	
	//========================
	private ViewPager viewPager;
	private TextView title;
	private int[] imageIds;
	private ArrayList<ImageView> images;
//	private String[] titles;
	private ArrayList<View> dots;
	private MyPagerAdapter adapter;
	private View view;

	private int oldPosition = 0;// 上一次圆点的位置
	private ScheduledExecutorService scheduledExecutor;

	private int currentIndex = 0; // 当前页面的位置
	//========================

	private FinalBitmap fb;
	private IntranetNewLiebAdapter adapter2;
	
	/**
	 * 数据适配器集合的最后一条
	 */
	private int temId = 0;
	private String temID = "";
	/**
	 * 返回的list里面的数据
	 */
	private int rowNum = 0;
	/**
	 * 一次取多少数据
	 */
	private int  pageSize = 10;
	
	
	private List<MobileNewLieb> intrateSize;
	// 加载的状态flag
	private boolean loadingflag;
	private MobileListAsyncTask mobileListAsyncTask;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
//		skinManager = new SkinManager(getActivity());
		titles = new ArrayList<String>();
		fb = FinalBitmap.create(getActivity());//初始化FinalBitmap模块
		fb.configLoadingImage(R.drawable.a);
		
		Display d = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		d.getMetrics(dm);
		screenHalf = d.getWidth() / 2;
		if (bundle != null) {
			SinopecMenuModule item = (SinopecMenuModule) bundle.getSerializable("entry");
			approvalItem = item;
			initPathParams(item);
//			initPathParamsNew(item);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		loadInTranetList();
//		if(intranets != null || intranets.size()>0 ) {
//			if(list_title == null || list_title.size() == 0) {
//				filldata(typeName, typeId);
//			}
//		}
 	}
	/**
	 * pageView TODO
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		 view = inflater.inflate(R.layout.layout_intranet, container, false);
		imageLoader = new ImageLoader(mContext);
//		if (list_title.size() == 0 || intranets.size() == 0) {
////			loadInTranetList();
//		} else {
//			initRadioButtonTypes(list_title); //给标题赋值
//			lv_intranet_list.setAdapter(new IntranetListAdapter(intranets.get(typeItem_name)));
//		}
		setupView(view);
		loadInTranetNewLieb();
		initView();
		initData();
		mobile_footer = inflater.inflate(R.layout.layout_approve_list_footer, null);
		mobile_approve_list_footer = (RelativeLayout) mobile_footer.findViewById(R.id.ll_list_footer_loading);
//		lv_intranet_list.addFooterView(mobile_footer);
		return view;
	}
	
	Handler handleradHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(adapter2 == null){
				adapter2 = new IntranetNewLiebAdapter();
				lv_intranet_list.setAdapter(adapter2);
			}else{
//				 复用旧的的数据适配器 ,通知数据适配器数据更新了.
				adapter2.notifyDataSetChanged();
			}
		}
	};
	
	/**
	 * 准备初始化数据
	 */
	private void initData() {
			// 图片
//			imageIds = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,R.drawable.d, R.drawable.e };
			// 用来显示的图片
			images = new ArrayList<ImageView>();
			
			
//			for (int i = 0; i < imageIds.length; i++) {
//				ImageView imageView = new ImageView(getActivity().getApplicationContext());
//				imageView.setScaleType(ScaleType.CENTER_CROP);
//				imageView.setImageResource(imageIds[i]);
////				images.add(imageView);
//			}

		
			titleNames = new String[] { "a", "b","c", "d", "e" };
			// 圆点
			dots = new ArrayList<View>();
			
			dots.add(view.findViewById(R.id.dot_0));
			dots.add(view.findViewById(R.id.dot_1));
			dots.add(view.findViewById(R.id.dot_2));
			dots.add(view.findViewById(R.id.dot_3));
			dots.add(view.findViewById(R.id.dot_4));
		}
		
		/**
		 * 初始化图片
		 */
		private void initView() {
			viewPager = (ViewPager)this.view.findViewById(R.id.viewPager);
			title = (TextView)this.view.findViewById(R.id.title);
			frameLayout = (RelativeLayout) view.findViewById(R.id.frameLayout);
//			frameLayout.setVisibility(View.GONE);
			adapter = new MyPagerAdapter();
			viewPager.setAdapter(adapter);
			viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		}
		
		/**
		 * 图片的监听
		 */
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// super.handleMessage(msg);
				// 切换viewPager当前页面
				viewPager.setCurrentItem(currentIndex);
			}
		};
		
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
					super.onStart();
					// 启动切换图片
//					scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
//					scheduledExecutor.scheduleAtFixedRate(new MyPagerTask(), 1, 3,TimeUnit.SECONDS);
		}
		
		/**
		 *  切换图片任务类
		 * @author admin
		 *
		 */
		private class MyPagerTask implements Runnable {

			@Override
			public void run() {
				// 切换页面
//				 currentIndex = (currentIndex + 1) % images.size();
				currentIndex++;

//				 viewPager.setCurrentItem(item);//设置当前页面

				// 更新UI Handler
				handler.sendEmptyMessage(0);
				
				synchronized (viewPager) {
					System.out.println("currentItem: " + currentIndex);
					currentIndex = (currentIndex) % images.size();
					handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
				}

			}
		}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		if (rg_group_types.getChildCount() != 0) {
//			int width = left - screenHalf;
//			horizontalScrollView.scroll(width);
//		}
//		loadInTranetList();
//		if(intranets != null || intranets.size()>0 ) {
//			if(list_title == null || list_title.size() == 0) {
//				filldata(typeName, typeId);
//			}
//		}
		
//		loadInTranetListDemo();
//		loadInTranetNewLieb();
	}

	int scrollX = 0; // 滚动的距离
	int left = 0; // 选中radiobutton距离左边的距离
	int leftScreen = 0; // 实际scrollview距离左边的距离
	private String typeName;
	private String typeId;
	private void loadInTranetList() {
		new AsyncTask<Void, Void, List<TypeItem>>() {
			private ProgressHUD overlayProgress; 
			private boolean pro=true;
			@Override
			protected void onPostExecute(List<TypeItem> result) {
				super.onPostExecute(result);
//				if (pb_loading != null) {
//					pb_loading.setVisibility(View.GONE);
//				}
				if(overlayProgress != null){
					overlayProgress.dismiss();
				}
				if (result != null && result.size()>0) {
					list_title = result;
					initRadioButtonTypes(result);
					TypeItem typeItem = result.get(0);
					detailId = typeItem.detailid;
					typeItem_name = typeItem.name;
					typeName = typeItem.name;
					typeId = typeItem.listid;
					filldata(typeItem.name, typeItem.listid);
				} else {
					Toast.makeText(mContext, "很抱歉，没有获取到数据", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
//				pb_loading.setVisibility(View.VISIBLE);
				overlayProgress = AlertUtils.showDialog(getActivity(), null, this, false);
				overlayProgress.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						pro = false;
					}
				});
			}

			@Override
			protected List<TypeItem> doInBackground(Void... params) {
				try {
					if (intranets.size() == 0) {
						List<TypeItem> items=DataCollectionUtils.receiverTypeItems(listPath);
						if (pro) {
							return items;
						}else {
							
							return null;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}
	
	
	private void loadInTranetNewLieb(){
		new AsyncTask<Void, Void, List<List<MobileNewLieb>>>(){
			private ProgressHUD overlayProgress;
			private boolean pro=true;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				overlayProgress = AlertUtils.showDialog(getActivity(), null, this,false);
				overlayProgress.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						pro = false;
					}
				});
			}
			
			protected void onPostExecute(List<List<MobileNewLieb>> result) {
				if (result != null)
					overlayProgress.dismiss();
				if(result != null && result.size()>0){
					List<MobileNewLieb> list = result.get(0);
					viewFlow(result.get(1));
					System.out.println("result 的长度 ==========>>>>>>>> "+result.size() + "rowNum 的长度" + rowNum + "temId最后一个元素" + temId);
					intrateSize = list;
					if(adapter2 == null){
						adapter2 = new IntranetNewLiebAdapter();
						lv_intranet_list.setAdapter(adapter2);
					}else{
						// 复用旧的的数据适配器 ,通知数据适配器数据更新了.
						adapter2 = new IntranetNewLiebAdapter();
						lv_intranet_list.setAdapter(adapter2);
					}
					loadingflag = false;
				}
				lv_intranet_list.onRefreshComplete();
			}
			
			@Override
			protected List<List<MobileNewLieb>> doInBackground(Void... params) {
				System.out.println("listPath====这是新闻获取的路径==>>>>>"+ listPath);
				rowNum = 0;
				temId = 0;
				List<List<MobileNewLieb>> list=DataCollectionUtils.receiverMobileNewLieb(listPath, newLeibId, temID,  rowNum, pageSize);
				if (pro) {
					return list;
				}else {
					return null;
				}
			}
			
		}.execute();
	}
	
	/**
	 * 填充listView列表的
	 * @author admin
	 *
	 */
	private class IntranetNewLiebAdapter extends BaseAdapter implements OnClickListener {
//		List<MobileNewLieb> intrates;
//		public IntranetNewLiebAdapter(List<MobileNewLieb> intrates) {
//			this.intrates = intrates;
//			intrateSize = intrates;
//		}
		
		@Override
		public int getCount() {
			System.out.println("intreates  ===========>>>>>>>" + intrateSize.size());
			return intrateSize.size();
		}

		@Override
		public Object getItem(int position) {
			return intrateSize.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		MobileNewLieb newsPaper;
		private ArrayList<String> imgePath;
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Headers headers = null;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_intranet_list_item, null);
				headers = new Headers();
				
				headers.iv_intranet_image = (ImageView) view.findViewById(R.id.iv_intranet_image);
				headers.tv_subject_mail_item = (TextView) view.findViewById(R.id.tv_subject_mail_item);
				headers.tv_mail_sender = (TextView) view.findViewById(R.id.tv_mail_sender);
				headers.ll_root_layout = (LinearLayout) view.findViewById(R.id.ll_root_layout);
				headers.moblieView  = (View)view.findViewById(R.id.view);
				view.setTag(headers);
			}else{
				headers = (Headers) view.getTag();
			}
//			int i = position + 1;
			
//			if (i % 2 == 1) {
//				linearLayout.setBackgroundResource(R.drawable.layout_list_item_first);
//			} else {
//				linearLayout.setBackgroundResource(R.drawable.layout_list_item_second);
//			}
			newsPaper = intrateSize.get(position);
			headers.moblieView.setTag(newsPaper);
			imgePath = new ArrayList<String>();
//			String stringimage = newsPaper.getImg();
//			imgePath.add(stringimage);
//			viewFlow(imgePath);
//			System.out.println("stringimage==================================>>>>>>>==" + stringimage );
			
			headers.ll_root_layout.setOnClickListener(this);
			
//			TextView tv_people_name = (TextView) view.findViewById(R.id.tv_people_name);
			
			
			if (newsPaper.img == null || "".equals(newsPaper.img)) {
				headers.iv_intranet_image.setVisibility(View.GONE);
			} else {
				headers.iv_intranet_image.setVisibility(View.VISIBLE);
				imageLoader.displayImage2(newsPaper.img, headers.iv_intranet_image,R.drawable.ic_sunbox_launcher);
				String string = newsPaper.img;
				System.out.println("string  =====>>>> "+ string);
			}
			headers.tv_subject_mail_item.setText(newsPaper.title);
			headers.tv_mail_sender.setText(newsPaper.date);
//			tv_people_name.setText("发布人："+newsPaper.author);
			return view;
		}

		@Override
		public void onClick(View v) {
			if(v instanceof LinearLayout) {
				MobileNewLieb tag = (MobileNewLieb) v.findViewById(R.id.view).getTag();
				MobileIntranetNewsDetailFragment fragment = new MobileIntranetNewsDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString("id", tag.id);
//				bundle.putString("detailId", listNewdetail);
				bundle.putSerializable("entry", approvalItem);
				bundle.putString("path", approvalItem.menuPages.get(1).id);
				fragment.setArguments(bundle);
				((ActivityInTab) mContext).navigateTo(fragment);
			}
		}
	}
	/**
	 * 从集合里获取出图片
	 * @param //arrayList
	 */
	private void viewFlow(List<MobileNewLieb> imgList){
		for (MobileNewLieb imgLists : imgList) {
				frameLayout.setVisibility(View.VISIBLE);
				System.out.println("arrayList string:======>>>" + imgLists.getImg());
				imageView = new ImageView(getActivity());
				imageView.setScaleType(ScaleType.CENTER_CROP);
				String tile = imgLists.getTitle();
				titles.add(tile);
				fb.display(imageView, imgLists.getImg());
				images.add(imageView);
		}
		adapter.notifyDataSetChanged();
	}
	
	static class Headers{
		ImageView iv_intranet_image;
		TextView tv_subject_mail_item ;
		TextView tv_mail_sender;
		LinearLayout ll_root_layout ;
		View moblieView;
	}
	
	
	private void loadInTranetListDemo() {
		new AsyncTask<Void, Void, List<NewsPaper>>() {
			private ProgressHUD overlayProgress; 
			private boolean pro=true;
			@Override
			protected void onPostExecute(List<NewsPaper> result) {
				super.onPostExecute(result);
				if(overlayProgress != null){
					overlayProgress.dismiss();
				}
				if (result != null && result.size()>0) {
					lv_intranet_list.setAdapter(new IntranetListAdapter(result));
				} else {
					Toast.makeText(mContext, "很抱歉，没有获取到数据", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
//				pb_loading.setVisibility(View.VISIBLE);
				overlayProgress = AlertUtils.showDialog(getActivity(), null, this, false);
				overlayProgress.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						pro = false;
					}
				});
			}

			@Override
			protected List<NewsPaper> doInBackground(Void... params) {
				try {
					
					List<NewsPaper> newsPapers=DataCollectionUtils.receiverDemoItems(listPath);
					if (pro) {
						return newsPapers;
					}else {
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	RadioButton rt_type;
	RadioGroup rg_group_types = null;

	protected void initRadioButtonTypes(final List<TypeItem> result) {

		for (int i = 0; i < result.size(); i++) {
			TypeItem item = result.get(i);
			final String text = item.name;

			rt_type = (RadioButton) ((Activity) mContext).getLayoutInflater().inflate(R.layout.layout_intranet_rt_group_button, null);
			RadioGroup.LayoutParams ll = new RadioGroup.LayoutParams(getActivity().getResources().getDimensionPixelSize(R.dimen.inbox_mail_width),
					getActivity().getResources().getDimensionPixelSize(R.dimen.inbox_mail_height));
			ll.setMargins(3, 0, 0, 0);
			
			rt_type.setLayoutParams(ll);
			if (i != 0) {
				rt_type.setChecked(false);
				rt_type.setSelected(false);
				rt_type.setSelectAllOnFocus(false);
			} else {
				rt_type.setChecked(true);
			}
//			if(Constants.changeSkin) {
//				StateListDrawable bg = skinManager.newSelector(getActivity(), Constants.skin_folder + File.separator +("ic_oa_radio_type_nomal.png"), (Constants.skin_folder + File.separator +"ic_oa_radio_type_selected.png"), 400, 400);
//				if(bg != null) {
//					rt_type.setBackgroundDrawable(bg);
//				}
//			}
			rt_type.setId(i);
			rt_type.setText(text);
			rt_type.setTag(item);
			rg_group_types.addView(rt_type, i);
			rt_type.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RadioButton rb = (RadioButton)v;
					String id = ((TypeItem)rb.getTag()).listid;
					detailId = ((TypeItem)rb.getTag()).detailid;
					typeItem_name = ((TypeItem)rb.getTag()).name;
					typeName = text;
					typeId = id;
					refreshListView(text, id);
					scrollX = horizontalScrollView.getScrollX();
					left = v.getLeft();
					leftScreen = left - scrollX;
					horizontalScrollView.smoothScrollBy((leftScreen - screenHalf), 0);
					
				}
			});
		}
	}

	private void refreshListView(String name, String id) {
		List<NewsPaper> news = intranets.get(name);
		if (news != null && news.size() > 0) {
			lv_intranet_list.setAdapter(new IntranetListAdapter(news));
		} else {
			filldata(name, id);
		}
	}

	private void filldata(final String name, final String cu) {
		new AsyncTask<Void, Void, List<NewsPaper>>() {
			private ProgressHUD overlayProgress;
			private boolean pro=true;
			@Override
			protected void onPostExecute(List<NewsPaper> result) {
				super.onPostExecute(result);
//				pb_loading.setVisibility(View.GONE);
				if(overlayProgress != null){
					overlayProgress.dismiss();
				}
				if (result != null && result.size() > 0) {
					intranets.put(name, result);
					lv_intranet_list.setAdapter(new IntranetListAdapter(intranets.get(name)));
				} else {
					lv_intranet_list.setAdapter(new IntranetListAdapter(new ArrayList<NewsPaper>()));
				}
//				lv_intranet_list.onRefreshComplete();
				isPull = false;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// if (!isPull)
//				pb_loading.setVisibility(View.VISIBLE);
				overlayProgress = AlertUtils.showDialog(getActivity(), null, this, false);
				overlayProgress.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						pro = false;
					}
				});
			}

			@Override
			protected List<NewsPaper> doInBackground(Void... params) {
				try {
					String path = WebUtils.rootUrl + URLUtils.baseContentUrl + cu;
					List<NewsPaper> list=DataCollectionUtils.receiverIntranetsList(path);
					if (pro) {
						return list;
					}else {
						
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute();
	}

	
	
	private void setupView(View v) {
		lv_intranet_list = (MyListView) v.findViewById(R.id.lv_intranet_list);
		lv_intranet_list.setonRefreshListener(this);
//		pb_loading = (RelativeLayout) v.findViewById(R.id.pb_loading);
		horizontalScrollView = (MyHorizontalScrollView) v.findViewById(R.id.horizontalScrollView);
		rg_group_types = (RadioGroup) v.findViewById(R.id.rg_group_types);
		lv_intranet_list.setonScrollEndListener(new onScrollEndListener() {
			@Override
			public void scrollEnd() {
				synchronized(TAG){
					if(mobileListAsyncTask == null){
						mobileListAsyncTask = new MobileListAsyncTask();
						mobileListAsyncTask.execute();
					}
				}
			}
		});
	}

	public class MobileListAsyncTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			lv_intranet_list.addFooterView(mobile_footer);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			lv_intranet_list.removeFooterView(mobile_footer);
			mobileListAsyncTask = null;
		}

		@Override
		protected Void doInBackground(Void... params) {
				// 获取最后一个可见条目对应的集合里面数据的位置. 从0开始
				/*temId  = lv_intranet_list.getLastVisiblePosition();*/
				System.out.println("temid 的长度" + temId +"======>>>>");
						// 获取集合的大小 从1开始
//						if(intrateSize != null){
							rowNum  = intrateSize.size();
							temID = intrateSize.get(intrateSize.size() - 1).getId();
							System.out.println("rowNum 的长度" + rowNum +"======>>>>");
							if(temId == rowNum){
								rowNum += pageSize;
								System.out.println("rowNum 的长度" + rowNum +"======>>>>");
								loadingflag = true;
								List<List<MobileNewLieb>> allLists = DataCollectionUtils.receiverMobileNewLieb(listPath, newLeibId, temID,  rowNum, pageSize);
								if (intrateSize == null) {
									System.out.println("listPath" + listPath  + "newLeibId" + newLeibId +" temId " + temId +" rowNum "+rowNum+" pageSize "+pageSize+"+======>>>>");
									intrateSize = allLists.get(0);
								} else {
									if(allLists.get(0) != null)
									intrateSize.addAll(allLists.get(0));
								}
								handleradHandler.sendEmptyMessage(22);
							}
			return null;
		}
	}
	public String value;

	ImageLoader imageLoader;
	private String[] titleNames;
	public static String newLeibId;
	private View mobile_footer;
	private RelativeLayout mobile_approve_list_footer;
	private ImageView imageView;
	private List<String> titles;
	private RelativeLayout frameLayout;

	private class IntranetListAdapter extends BaseAdapter implements OnClickListener {
		List<NewsPaper> intrates;

		public IntranetListAdapter(List<NewsPaper> intrates) {
			this.intrates = intrates;
		}
		
		@Override
		public int getCount() {
			return intrates.size();
		}

		@Override
		public Object getItem(int position) {
			return intrates.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			NewsPaper newsPaper;
			if (view == null) {
				view = mContext.getLayoutInflater().inflate(R.layout.layout_intranet_list_item, null);
			}
			LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_root_layout);
			linearLayout.setTag(intrates.get(position));
			linearLayout.setOnClickListener(this);
			int i = position + 1;
//			if (i % 2 == 1) {
//				linearLayout.setBackgroundResource(R.drawable.layout_list_item_first);
//			} else {
//				linearLayout.setBackgroundResource(R.drawable.layout_list_item_second);
//			}
			newsPaper = intrates.get(position);
			if (newsPaper.imageAddress == null || "".equals(newsPaper.imageAddress)) {
				ImageView iv_intranet_image = (ImageView) view.findViewById(R.id.iv_intranet_image);
				TextView tv_subject_mail_item = (TextView) view.findViewById(R.id.tv_subject_mail_item);
				TextView tv_mail_sender = (TextView) view.findViewById(R.id.tv_mail_sender);
//				TextView tv_people_mail = (TextView) view.findViewById(R.id.tv_people_name);
				
				iv_intranet_image.setVisibility(View.GONE);
//				tv_people_mail.setText("会议时间：");
				tv_subject_mail_item.setText(newsPaper.title);
				tv_mail_sender.setText(newsPaper.date);
			} else {
				ImageView iv_intranet_image = (ImageView) view.findViewById(R.id.iv_intranet_image);
				TextView tv_subject_mail_item = (TextView) view.findViewById(R.id.tv_subject_mail_item);
				TextView tv_mail_sender = (TextView) view.findViewById(R.id.tv_mail_sender);
//				TextView tv_people_mail = (TextView) view.findViewById(R.id.tv_people_name);
				iv_intranet_image.setVisibility(View.VISIBLE);
				imageLoader.displayImage(newsPaper.imageAddress, iv_intranet_image);
				tv_subject_mail_item.setText(newsPaper.title);
//				tv_people_mail.setText(newsPaper.detailUrl);
				tv_mail_sender.setText(newsPaper.date.trim());
			}
			return view;
		}

		@Override
		public void onClick(View v) {
			NewsPaper tag = (NewsPaper) v.getTag();
			MobileIntranetNewsDetailFragment fragment = new MobileIntranetNewsDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString("id", tag.id);
			bundle.putString("detailId", detailId);
			bundle.putSerializable("entry", approvalItem);
			bundle.putString("path", approvalItem.menuPages.get(1).id);
			fragment.setArguments(bundle);
			((ActivityInTab) mContext).navigateTo(fragment);
		}
	}
	@Override
	public void onRefresh() {
//		isPull = true;
//		RadioButton radioButton = (RadioButton) rg_group_types.getChildAt(rg_group_types.getCheckedRadioButtonId());
//		TypeItem item = (TypeItem) radioButton.getTag();
//		filldata(item.name, item.listid);
//		intrateSize.clear();
		loadInTranetNewLieb();
		
//		new AsyncTask<Void, Void, Void>() {
//			@Override
//			protected Void doInBackground(Void... arg0) {
//				SystemClock.sleep(300);
//				return null;
//			}
//			@Override
//			protected void onPostExecute(Void result) {
//				super.onPostExecute(result);
////				lv_intranet_list.onRefreshComplete();
//			}
//		}.execute();
	}
	
	@Override
	public void onClick(View paramView) {

	}

	/**
	 * 在进行后台任务，例如下载任务时，使用进度条提示用户进行等待，一般在任务开始时使用，结束时关闭
	 * 
	 * @param title
	 * @param message
	 * @param task
	 */
	protected void showProgress(String title, String message, final AsyncTask<?, ?, ?> task) {
		if (_pd == null) {
			_pd = new ProgressDialog(mContext);
			_pd.setTitle(title);
			_pd.setMessage(message);
			_pd.setCanceledOnTouchOutside(false);
			_pd.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
						task.cancel(true);
						System.out.println("Task[" + task + "] is cancelled");
						_pd = null;
					}
				}
			});
			_pd.show();
		} else {
			_pd.setTitle(title);
			_pd.setMessage(message);
			_pd.show();
		}
	}

	protected void dismissProgress() {
		if (_pd != null && _pd.isShowing()) {
			_pd.cancel();
			_pd = null;
		}
	}
	
	/**
	 * 跑马灯的数据适配器
	 * @author admin
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (images==null) {
				return 0;
			}
			 return images.size();
//			return Integer.MAX_VALUE;
		}

		// 判断当前页面显示的数据 与 新页面的数据是否相同
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		// 初始化数据
		@Override
		public Object instantiateItem(ViewGroup viewPager,   final int position) {
			// TODO Auto-generated method stub
			// viewPager.addView(images.get(position));
				ImageView timeView = images.get(position);
				timeView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						Toast.makeText(getActivity(), "图片的ID" + position + "图片的名字" + titleNames[position], 0).show();
					}
				});
				title.setText(titles.get(position));
				viewPager.addView(images.get(position % images.size()));
				System.out.println("images.size() ==========>>>> "+images.size());
				return images.get(position % images.size());
		}

		// 销毁数据
		@Override
		public void destroyItem(ViewGroup viewPager, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) viewPager).removeView((View) object);
		}
	}
	
	/**
	 * 图片滑动的监听
	 * @author admin
	 *
	 */
	private class MyOnPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;
		@Override
		public void onPageScrollStateChanged(int arg0) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

	
		@Override
		public void onPageSelected(int position) {
			currentIndex = position;
			title.setText(titles.get(position));
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}
	}
	
	/**
	 * 获取新闻列表路径
	 * @param item detail
	 */
	private void initPathParams(SinopecMenuModule item) {
		// listPath = WebUtils.rootUrl + URLUtils.baseContentUrl
		// + item.menuPages.get(0).id;
		// load tab path
		for (SinopecMenuPage page : item.menuPages) {
			if ("list".equalsIgnoreCase(page.code)) {
				listPath = WebUtils.rootUrl + URLUtils.baseContentUrl + page.id;
				newLeibId = page.id;
			}
		}
	}
}
