package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.SwitchMenuActivity;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.DataCache;
import petrochina.ghzy.a10fieldwork.R;

public class BaseFragment extends Fragment implements IRefreshButtonAndText{
	protected int screenWidth;
	protected int screenHeight;
	protected SharedPreferences sp;
	public ActivityInTab mActivity;
	public DisplayImageOptions options;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ActivityInTab) activity;
    }
//	public OnButtonSelectedListener mCallback;
	
//    public interface OnButtonSelectedListener {
//        /** Called by HeadlinesFragment when a list item is selected */
//        public void onArticleSelected(View description);
//    }
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception.
//        try {
//            mCallback = (OnButtonSelectedListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
//    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_tab_default)
				.cacheInMemory(true).cacheOnDisk(true).build();
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		sp = getActivity().getSharedPreferences("sys_config", Context.MODE_PRIVATE);
		//添加返回按钮事件
//		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
//		tv_title.setText("sdfdsfsd");
//		mCallback.onArticleSelected(btn_next);
	}
	
	protected Map<Integer, SinopecMenuModule> checkNewstModule(SinopecMenuGroup groups) {
		Map<Integer, SinopecMenuModule> firstModule = new HashMap<Integer, SinopecMenuModule>();
		int targetIndex=0;
		SinopecMenuModule module = null;
		List<Object> listRroup = groups.menuobjObjects;
		for (int i = 0; i < listRroup.size(); i++) {
			Object o = listRroup.get(i);
			if (o instanceof SinopecMenuModule) {
				SinopecMenuModule smm = (SinopecMenuModule) o;
				if(smm.isGroupModule) {
					module = smm;
					firstModule.put(i, module);
					break;
				}
			} 
//			else if (o instanceof SinopecMenuGroup) {
//				checkNewstModule((SinopecMenuGroup) o);
//			}
		}
		if(module != null) {
			return firstModule;
		}
		for (int i = 0; i < listRroup.size(); i++) {
			Object o = listRroup.get(i);
			if (o instanceof SinopecMenuModule) {
				firstModule.put(i, (SinopecMenuModule) o);
				return firstModule;
			} 
//			else if (o instanceof SinopecMenuGroup) {
//				checkNewstModule((SinopecMenuGroup) o);
//			}
		}
		if(module == null) {
			module = new SinopecMenuModule();
			firstModule.put(targetIndex, module);
		}
		return firstModule;
	}
	
	protected void navSquaredGroup(Bundle bundle) {
	/*	Fragment f = new MobileOATypeSquaredFragment();
		f.setArguments(bundle);
		((ActivityInTab)getActivity()).navigateTo(f);*/
	}
	
	protected void approveRouteSuccess(int targetContainer, IApproveBackToList backToList) {
		((ActivityInTab)getActivity()).clearFragmentAndFreshOAApprove(targetContainer, backToList);
	}
	protected void approveRouteSuccess1(int targetContainer, IApproveBackToList backToList) {
		((ActivityInTab)getActivity()).clearFragmentAndFreshOAApprove1(targetContainer, backToList);
	}
	
	protected void navdModule(SinopecMenuModule smd) {
		String classCode = smd.mClass;
		Fragment fragment = ((ActivityInTab)getActivity()).findFragmentByCode(classCode);
		Bundle bundle = new Bundle();
		bundle.putSerializable("entry", smd);
		fragment.setArguments(bundle);
		((ActivityInTab)getActivity()).navigateTo(fragment);
	}
	
	protected void backPrecious() {
		getActivity().onBackPressed();
	}

	@Override
	public void refreshButtonAndText() {//集中处理一些返回
		System.out.println("super=====");
	}
//static boolean b = true;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		long endTime = System.currentTimeMillis();
//		if (startTime != 0 && b ) {
//			b = false;
//			long timeMills = endTime - startTime;
//			if (timeMills > Constants.timeOfQuit) {
//				Intent intent = new Intent(getActivity(), LoginActivity.class);
////				intent.setFlags(Intent.f)
//				startActivity(intent);
////				ExitApplication.getInstance().exit();
//				getActivity().finish();
//		}
//		}else{
//			startTime = System.currentTimeMillis();
//			b = true;
//		}
		if(DataCache.sinopecMenu.menuObject==null || DataCache.sinopecMenu.menuObject.size()==0){
			Intent intent = new Intent(getActivity(), SwitchMenuActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	

//	private long startTime;
//	@Override
//	public void onPause() {
//		super.onPause();
//		startTime = System.currentTimeMillis();
//	}
}
