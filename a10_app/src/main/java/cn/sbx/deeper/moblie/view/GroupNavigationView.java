package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.MenuType;
import cn.sbx.deeper.moblie.util.MenuTypeUtil;
import petrochina.ghzy.a10fieldwork.R;

/**
 * show top navegation view
 * @author terry.C
 *
 */
public class GroupNavigationView extends ViewGroup {
	public static final String TAG = GroupNavigationView.class.getSimpleName();
	LinearLayout topTitle;
	FrameLayout content;
	LinearLayout bottomTitle;
	private int titleBarHeight;
	private List<View> topViews = new ArrayList<View>();
	private List<View> bottomViews = new ArrayList<View>();
	private int screenWidth;
	private int screenHeight;
	private int mainBarHeight;
	private int mainTitleBarHeight;
	
	public GroupNavigationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout();
	}

	public GroupNavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout();
	}

	public GroupNavigationView(Context context) {
		super(context);
		initLayout();
	}
	private void initLayout() {
		content = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_oa_group_custom, this, false);
		addView(content, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		titleBarHeight = getResources().getDimensionPixelOffset(
				R.dimen.mobile_title_height);
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		mainBarHeight = 0;
		mainTitleBarHeight = getResources().getDimensionPixelSize(R.dimen.mobile_title_height);
		MenuType mt = MenuTypeUtil.chooseMenuType(DataCache.sinopecMenu.layout);
		if (mt != null) {
			switch (mt) {
			case TOP:
				break;
			case BOTTOM:
				mainBarHeight = getResources().getDimensionPixelSize(R.dimen.main_bar_item_height);
				break;
			case SQUARED:
				break;
			case LEFT:
				break;
			default:
				break;
			}
		}
		
	}
	
	public void addTopTitle(View top) {
		topViews.add(top);
		requestLayout();
	}
	
	public void removeTopView() {
		removeView(topViews.get(0));
		topViews.clear();
		requestLayout();
	}
	
	public void addBottomTitle(View bottom) {
		bottomViews.add(bottom);
		requestLayout();
	}
	
	public int getTopViewSize() {
		return topViews.size();
	}
	
	public int getBottomViewSize() {
		return bottomViews.size();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for(int i=0; i<topViews.size(); i++) {
			View v = topViews.get(i);
			Log.i(TAG, "add top view");
			int widthSpec = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
			int heightSpec = MeasureSpec.makeMeasureSpec(titleBarHeight, MeasureSpec.EXACTLY);
			v.measure(widthSpec, heightSpec);
			addViewInLayout(v, -1, v.getLayoutParams());
			v.layout(l, i*titleBarHeight, r, titleBarHeight*(i+1));
		}
		for(int i=0; i<bottomViews.size(); i++) {
			View v = bottomViews.get(i);
			Log.i(TAG, "add bottom view");
			int widthSpec = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
			int heightSpec = MeasureSpec.makeMeasureSpec(titleBarHeight, MeasureSpec.EXACTLY);
			v.measure(widthSpec, heightSpec);
			addViewInLayout(v, -1, v.getLayoutParams());
			v.layout(l, b-(i+1)*titleBarHeight-90, r, titleBarHeight*(i+1));
		}
		int widthSpec = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
		int heightSpec = MeasureSpec.makeMeasureSpec(screenHeight-(topViews.size()+bottomViews.size())*titleBarHeight-mainBarHeight-mainTitleBarHeight, MeasureSpec.AT_MOST);
		content.measure(widthSpec, heightSpec);
		content.layout(l, topViews.size()*titleBarHeight, r, b-bottomViews.size()*titleBarHeight);
	}
}
