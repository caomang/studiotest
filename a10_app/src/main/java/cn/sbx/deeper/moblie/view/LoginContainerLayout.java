package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import petrochina.ghzy.a10fieldwork.R;


/**
 * 登录界面的布局容器
 * 
 * @author terry.C
 * 
 */
public class LoginContainerLayout extends LinearLayout {

	private View ll_up_container;
	private View ll_top_log;
	private int marginBotton;
	private int screenWidth;
	private int screenHeight;
	private int contentTop;
	private int contentLeft;
	private int contentRight;
	private int contentBottom;

	public LoginContainerLayout(Context context) {
		this(context, null);
	}

	public LoginContainerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.LoginContainer);
		marginBotton = ta.getDimensionPixelOffset(
				R.styleable.LoginContainer_marginBottom, 0);
		int bg = ta
				.getResourceId(R.styleable.LoginContainer_rootBackground, -1);
		if (bg != -1) {
			setBackgroundResource(bg);
		}
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		ll_top_log = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_login_logo, this, false);

		ll_up_container = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_login_content, this, false);

		setOrientation(LinearLayout.VERTICAL);
//		addView(ll_top_log);
		addView(ll_up_container);
		ta.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		contentLeft = (screenWidth - ll_up_container.getMeasuredWidth()) / 2;
		contentTop = (screenHeight - ll_up_container.getMeasuredHeight()) / 2
				- marginBotton;
		contentRight = (screenWidth - ll_up_container.getMeasuredWidth()) / 2
				+ ll_up_container.getMeasuredWidth();
		contentBottom = screenHeight;
//		contentBottom = (screenHeight - ll_up_container.getMeasuredHeight())
//				/ 2 + ll_up_container.getMeasuredHeight() - marginBotton;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		ll_up_container.layout(contentLeft, contentTop, contentRight,
				contentBottom);
	}

	public int getContentMarginBottom() {
		return marginBotton;
	}

	public void viewToUp(int mb) {
		marginBotton = mb;
		requestLayout();
	}

	public void viewToDown() {
		marginBotton = 0;
		requestLayout();
	}
}
