package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.view.GroupAnimHorizontalScrollView;
import cn.sbx.deeper.moblie.view.GroupNavigationView;
import cn.sbx.deeper.moblie.view.TagDigitalButton;
import petrochina.ghzy.a10fieldwork.R;


/**
 * 顶部导航界面
 * @author terry.C
 *
 */
public class MobileOATypeTopFragment extends BaseFragment{

	protected static final String TAG = MobileOATypeTopFragment.class.getSimpleName();
	private GroupNavigationView group_container;
	private SinopecMenuGroup group;
	private RadioGroup modulesGroup;
	Context mContext;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = getActivity();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		group = (SinopecMenuGroup) bundle
				.getSerializable("entry");
		pageIndex = bundle.getInt("pageIndex");
		targertModule = checkNewstModule(group);
		targetIndex = targertModule.entrySet().iterator().next().getKey();
		module = targertModule.entrySet().iterator().next().getValue();
//		if(targertModule.get(targertModule.entrySet().iterator().next().getKey()).caption.equals("日程管理")){
//			targetIndex = 0;
//			module = targertModule.get(targetIndex);
//		}
		
		Constants.setBroadcastNamePairs(pageIndex);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(Constants.getBroadcastNamePairsString(pageIndex));
		getActivity().registerReceiver(modifyTypeReiceiver, intentFilter);
		replaceFragment(module);
		String ftitle = bundle.getString("ftitle");
		if(null != ftitle && !"".equalsIgnoreCase(ftitle)) {
			TextView tv = (TextView) getActivity().findViewById(R.id.tv_title);
			if(tv!=null) {
				tv.setText(ftitle);
			}
		}
	}
	
	BroadcastReceiver modifyTypeReiceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "onReceive");
			String type = intent.getStringExtra("type");
			if(type != null && "gotoModuleGroup".equalsIgnoreCase(type) && isAdded()) {
				if(module != null && module.isGroupModule) {
					replaceFragment(module);
					removeTopNav();
					showTopNavegation(group);
					return;
				}
			}
			
			if(type != null && "freshNum".equalsIgnoreCase(type) && isAdded()) {
				String moduleId = intent.getStringExtra("id");
				List<String[]> numbers = (List<String[]>) intent.getSerializableExtra("value");
				if(numbers == null) return;
				if(modulesGroup != null) {
					for(int i=0; i<modulesGroup.getChildCount(); i++) {
						TagDigitalButton rb = (TagDigitalButton) modulesGroup.getChildAt(i);
						Object obj = rb.getTag();
						if(obj instanceof SinopecMenuModule) {
							SinopecMenuModule smm = (SinopecMenuModule) obj;
							if(smm.id.equalsIgnoreCase(moduleId) && "1".equalsIgnoreCase(smm.notification)) {
								int count = calculateTabNumber(numbers);
								rb.setTabNumber(count);
							}
						}else if(obj instanceof SinopecMenuGroup) {
//						SinopecMenuGroup smg = (SinopecMenuGroup) obj;
						}
					}
				}
				if(isAdded()) {
					Intent numIntent = new Intent(Constants.MODIFY_APP_MENU_NUM);
					numIntent.putExtra("id", group.id);
					numIntent.putExtra("value", (Serializable)numbers);
					getActivity().sendBroadcast(numIntent);
				}
			}
			
		}
	};
	
	private void clearGroupCheckedStatu() {
		Log.i(TAG, "clearGroupCheckedStatu()");
		if(modulesGroup != null && modulesGroup.getChildCount()>0) {
			for(int i=0; i<modulesGroup.getChildCount(); i++) {
				TagDigitalButton rb = (TagDigitalButton) modulesGroup.getChildAt(i);
				if(rb.isChecked()) {
					rb.setChecked(false);
					modulesGroup.requestFocus();
				}
			}
		}
	}
	
	public int  calculateTabNumber(List<String[]> numbers) {
		int result = 0;
		for(String[] number : numbers) {
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
	private int pageIndex;
	private Map<Integer, SinopecMenuModule> targertModule;
	private int targetIndex;
	private SinopecMenuModule module;
	
	public void onDestroy() {
		super.onDestroy();
		if(modifyTypeReiceiver != null)
		getActivity().unregisterReceiver(modifyTypeReiceiver);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_oa_group_main, null);
		group_container = (GroupNavigationView) view.findViewById(R.id.group_container);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showTopNavegation(group);
	}
	
	/**
	 * 顶部导航样式
	 * @param g
	 */
	private void showTopNavegation(final SinopecMenuGroup g) {
		List<Object> groupModules = g.menuobjObjects;
		LinearLayout ll = new LinearLayout(mContext);
		ll.setBackgroundResource(R.color.b_gray);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER_VERTICAL);
		int topBarHeight = getResources().getDimensionPixelOffset(
				R.dimen.mobile_title_height);
		ll.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, topBarHeight));
		GroupAnimHorizontalScrollView hBar = new GroupAnimHorizontalScrollView(
				mContext);
		hBar.setHorizontalScrollBarEnabled(false);
		LinearLayout.LayoutParams hBarParams = new LinearLayout.LayoutParams(
				0, topBarHeight);
		hBarParams.weight = 1;
		hBar.setLayoutParams(hBarParams);
		RadioGroup rGroup = new RadioGroup(mContext);
		modulesGroup = rGroup;
		rGroup.setGravity(Gravity.CENTER);
		rGroup.setOrientation(RadioGroup.HORIZONTAL);
		hBar.addView(rGroup);
		RadioGroup.LayoutParams rll = new RadioGroup.LayoutParams(
				getResources()
						.getDimensionPixelOffset(R.dimen.inbox_mail_width),
				getResources().getDimensionPixelOffset(
						R.dimen.inbox_mail_height));
		rll.setMargins(6, 0, 0, 0);
		for (int i = 0; i < groupModules.size(); i++) {
			Object o = groupModules.get(i);
			final TagDigitalButton rButton = new TagDigitalButton(mContext);
			if (o instanceof SinopecMenuGroup) {
				SinopecMenuGroup smg = (SinopecMenuGroup) o;
				rButton.setText(smg.caption);
				rButton.setTag(smg);
			} else if (o instanceof SinopecMenuModule) {
				SinopecMenuModule smm = (SinopecMenuModule) o;
				if(smm.isGroupModule) {
					continue;
				}
				rButton.setText(smm.caption);
				rButton.setTag(smm);
			}
			rButton.setBackgroundResource(R.drawable.rt_scroll_selected);
			rButton.setTextAppearance(mContext, R.style.style_intranet_tab_text);
			rButton.setLayoutParams(rll);
			rGroup.addView(rButton, i);
			if(null != module && !module.isGroupModule) {
				if (i == targetIndex) {
					rButton.setChecked(true);
				} else {
					rButton.setChecked(false);
				}
			}
			rButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Object ro = rButton.getTag();
					rButton.setChecked(true);
					if (ro instanceof SinopecMenuGroup) {
						SinopecMenuGroup sg = (SinopecMenuGroup) ro;
//						showTopNavegation(sg);
						//根据约定此处只能是九宫格导航
//						Fragment f = new MobileOATypeSquaredFragment();
//						f.setArguments(bundle);
//						((ActivityInTab)getActivity()).navigateTo(f);
						Bundle bundle = new Bundle();
						bundle.putSerializable("entry", sg);
						bundle.putSerializable("ftitle", sg.caption);
						navSquaredGroup(bundle);
					} else if (ro instanceof SinopecMenuModule) {
						replaceFragment((SinopecMenuModule) ro);
					}
				}
			});
		}
		
//		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		imageParams.leftMargin = 10;
//		imageParams.rightMargin = 10;
//		ImageView imageView = new ImageView(mContext);
//		imageView.setLayoutParams(imageParams);
//		imageView.setImageResource(R.drawable.ic_refresh);
//		imageView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				Toast.makeText(mContext, "refresh button clicked", 0).show();
//			}
//		});
//		ll.addView(imageView);
		ll.addView(hBar);
		group_container.addTopTitle(ll);
	}
	
	public void removeTopNav() {
		group_container.removeTopView();
	}
	
	private void replaceFragment(SinopecMenuModule smd) {
		/*String classCode = smd.mClass;
		String id = smd.id;
		Fragment fragment = ((ActivityInTab)getActivity()).findFragmentByCode(classCode,id);
		Bundle bundle = new Bundle();
		bundle.putSerializable("entry", smd);
		bundle.putInt("containerId", R.id.fl_content);
		bundle.putInt("pageIndex", pageIndex);
		fragment.setArguments(bundle);
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_content, fragment).commit();*/
	}
}
