/*
 * Created Date: 2012-9-23 上午10:34:48
 * 
 */
package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.sbx.deeper.moblie.contrants.Constants;
import petrochina.ghzy.a10fieldwork.R;


/**
 * 
 */
public class OpenFolder {

	public static String TAG = "OpenFolder";
	/**
	 * folder animaltion execution time
	 */
	private static int ANIMALTION_TIME = 300;

	private Context mContext;
	private WindowManager mWindowManager;
	private boolean mWindowIsAdd = false;
	private int mWindowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

	private OpenFolderContainer container;
	private View mBackgroundView;
	private View mFolderView;
	private ImageView mTopView;
	private ImageView mBottomView;

	private int mUp_down;
	private int[] mAnchorLocation = new int[2];
	private int mSrceenwidth;
	private int mSrceenheigh;
	private int mFolderheigh;
	private int mFolderUpY;
	private int mFolderDownY;

	private int offsety;
	private int offsetyLast;
	private boolean mIsOpened = false;

	/**
	 * Listener that is called when this OpenFolder window is closed.
	 */
	public interface OnFolderClosedListener {
		/**
		 * Called when this OpenFolder window is closed.
		 */
		void onClosed();
	}

	private OnFolderClosedListener mOnFolderClosedListener;

	/**
	 * the folder open Animation Listener
	 */
	private Animation.AnimationListener mOpenAnimationListener = new Animation.AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mIsOpened = true;
		}
	};

	/**
	 * the folder colse Animation Listener
	 */
	private Animation.AnimationListener mClosedAnimationListener = new Animation.AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			container.post(new Runnable() {
				
				@Override
				public void run() {
					container.removeAllViews();
				}
			});
			
			mWindowManager.removeView(container);
			mWindowIsAdd = false;
			// 清空画图缓存区，否则获取的还是原来的图像
			mBackgroundView.setDrawingCacheEnabled(false);
			mIsOpened = false;
			if (mOnFolderClosedListener != null) {
				mOnFolderClosedListener.onClosed();
			}
		}
	};

	private static OpenFolder openFolder;

	/**
	 * @param context
	 * @return
	 */
	public static OpenFolder getInstance(Context context) {
		if (openFolder == null) {
			openFolder = new OpenFolder(context);
		}
		return openFolder;
	}

	private OpenFolder(Context context) {
		mContext = context;
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		container = new OpenFolderContainer(mContext);
		if(Constants.DEBUG) {

			Log.i(TAG, "container.background color:" + container.getBackground());
		}
	}
	View anchor;
	/**
	 * 
	 * @param anchor
	 *            folder展开参照容器
	 * @param backgroundView
	 *            当前用于显示folder的页面
	 * @param folderView
	 *            folder页面
	 * @param folderH
	 *            folder页面高度，dip
	 * @param up_down
	 *            0 为向上展开，否则向下展开
	 */
	public void openFolderView(View anchor, View backgroundView,int heigth,
			View folderView, int folderH, int up_down) {
		container.setBackgroundDrawable(backgroundView.getBackground());
		this.anchor = anchor;
		mUp_down = up_down;
		mBackgroundView = backgroundView;
		mFolderheigh = dip2px(mContext, folderH);
		mFolderView = folderView;
		mSrceenwidth = backgroundView.getWidth();
		mSrceenheigh =backgroundView.getHeight()-heigth;// backgroundView.getHeight();
		
		// 获取参照控件的位置
		anchor.getLocationInWindow(mAnchorLocation);
		if (up_down == 0) {
			offsety = mAnchorLocation[1];
			mFolderUpY = offsety - mFolderheigh;
			mFolderDownY = offsety;
			
		} else {
			offsety = anchor.getHeight() + mAnchorLocation[1];
			mFolderUpY = offsety;
			mFolderDownY = offsety + mFolderheigh;
		}

		prepareLayout();
		startOpenAnimation();
	}
	public void openFolderView2(View anchor, View backgroundView,int heigth,
			View folderView, int folderH, int up_down) {
		container.setBackgroundDrawable(backgroundView.getBackground());
		this.anchor = anchor;
		mUp_down = up_down;
		mBackgroundView = backgroundView;
		mFolderheigh = dip2px(mContext, folderH);
		mFolderView = folderView;
		mSrceenwidth = backgroundView.getWidth();
		mSrceenheigh =backgroundView.getHeight();// backgroundView.getHeight();
		
		// 获取参照控件的位置
		anchor.getLocationInWindow(mAnchorLocation);
		if (up_down == 0) {
			offsety = mAnchorLocation[1]-mAnchorLocation[0];
			mFolderUpY = offsety - mFolderheigh;
			mFolderDownY = offsety;
			
		} else {
//			mSrceenheigh=mSrceenheigh-mAnchorLocation[0];
			offsety = anchor.getHeight() + mAnchorLocation[1]-mAnchorLocation[0];
			mFolderUpY = offsety;
			mFolderDownY = offsety + mFolderheigh;
		}

		prepareLayout2(heigth);
		startOpenAnimation2();
	}
	/**
	 * 
	 * @param offy
	 *            folder在屏幕展开y轴位置 px
	 * @param backgroundView
	 *            当前用于显示folder的页面
	 * @param folderView
	 *            folder页面
	 * @param folderH
	 *            folder页面高度，dip
	 * @param up_down
	 *            0 为向上展开，否则向下展开
	 */
	public void openFolderView(int offy, View backgroundView, View folderView,
			int folderH, int up_down) {
		mUp_down = up_down;
		mBackgroundView = backgroundView;
		mFolderheigh = dip2px(mContext, folderH);
		mFolderView = folderView;
		mSrceenwidth = backgroundView.getWidth();
		mSrceenheigh = backgroundView.getHeight();

		offsety = offy;
		
		if(mUp_down == 0){
			mFolderUpY = offy - mFolderheigh;
			mFolderDownY = offy;
		}else{
			mFolderUpY = offy;
			mFolderDownY = offy + mFolderheigh;
		}
		
		prepareLayout();
		startOpenAnimation();

	}

	public void prepareLayout() {

		if(mWindowIsAdd){
			Log.e(TAG, "container view has already been added to the window manager!!!");
			return;
		}
		container.removeAllViews();
		
		//add  arrow up
		RelativeLayout.LayoutParams fp_arrow = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 10);
		if (mUp_down == 0) {
//			fp_arrow.setMargins(anchor.getLeft() + anchor.getWidth() + anchor.getWidth()/3, offsety - mFolderheigh + mFolderView.getHeight() -10, anchor.getRight(), 0);
//			ImageView arrow = new ImageView(mContext);
//			arrow.setBackgroundResource(R.drawable.arrow_down);
//			container.addView(arrow, fp_arrow);
		}else {
			fp_arrow.setMargins(anchor.getLeft() + anchor.getWidth()/2, offsety, anchor.getRight(), 0);
//			fp_arrow.leftMargin = anchor.getLeft();
			ImageView arrow = new ImageView(mContext);
			arrow.setBackgroundResource(R.drawable.ic_openfolder_up);
			container.addView(arrow, fp_arrow);
		}
		
		// add folderview
		RelativeLayout.LayoutParams fp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, mFolderheigh);
		if (mUp_down == 0) {
			fp.setMargins(0, offsety - mFolderheigh, 0, 0);
		} else {
			fp.setMargins(0, offsety + 10, 0, 0);
		}
		container.addView(mFolderView, fp);
		
		// 截当前view背景图，用于分割 topview bottomview
		mBackgroundView.setDrawingCacheEnabled(true);
		Bitmap srceen = mBackgroundView.getDrawingCache();

		// add topview
		// 截图控件以上部分
		Bitmap top = Bitmap.createBitmap(srceen, 0, 0, mSrceenwidth, offsety);
		mTopView = new ImageView(mContext);
//		mTopView.setId(1000);
		mTopView.setBackgroundDrawable(new BitmapDrawable(mContext
				.getResources(), top));
		RelativeLayout.LayoutParams ft = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, offsety);
		container.addView(mTopView, ft);

		
		//add middle 截取控件下面10像素作为箭头的背景
		Bitmap middle = Bitmap.createBitmap(srceen, 0, offsety, mSrceenwidth, 10);
		container.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(),middle));
		
		// add bottomview
		// 截图控件以下部分
		Bitmap bottom = Bitmap.createBitmap(srceen, 0, offsety, mSrceenwidth,
				mSrceenheigh - offsety);
		mBottomView = new ImageView(mContext);
		mBottomView.setBackgroundDrawable(new BitmapDrawable(mContext
				.getResources(), bottom));
		RelativeLayout.LayoutParams fb = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, mSrceenheigh - offsety);
		fb.addRule(RelativeLayout.BELOW, 1000);
		container.addView(mBottomView, fb);
		if(!mWindowIsAdd){
			mWindowManager.addView(container, createPopupLayout(mBackgroundView.getWindowToken()));
			mWindowIsAdd = true;
		}
		
	}

	public void startOpenAnimation() {
		// 展开动画
		if (mUp_down == 0) {

			offsety = 0;
			offsetyLast = 0 - mFolderheigh;
			TranslateAnimation ta = new TranslateAnimation(0, 0, offsety,
					offsetyLast);
			ta.setInterpolator(new DecelerateInterpolator());
			ta.setDuration(ANIMALTION_TIME);
			ta.setFillAfter(true);
			ta.setAnimationListener(mOpenAnimationListener);
			mTopView.startAnimation(ta);

		} else {

			offsety = 0;
			offsetyLast = 0 + mFolderheigh;
			TranslateAnimation ta = new TranslateAnimation(0, 0, offsety,
					offsetyLast);
			ta.setInterpolator(new DecelerateInterpolator());
			ta.setDuration(ANIMALTION_TIME);
			ta.setFillAfter(true);
			ta.setAnimationListener(mOpenAnimationListener);
			mBottomView.startAnimation(ta);
		}
	}

	private WindowManager.LayoutParams createPopupLayout(IBinder token) {
		WindowManager.LayoutParams p = new WindowManager.LayoutParams();
		p.gravity = Gravity.LEFT | Gravity.TOP;
		p.width = mSrceenwidth;
		p.height = mSrceenheigh;
		p.format = PixelFormat.OPAQUE;
		p.token = token;
		p.type = mWindowLayoutType;
		p.setTitle("OpenFolder:" + Integer.toHexString(hashCode()));

		return p;
	}

	public  int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * Sets the listener to be called when the openFolder is colsed.
	 * 
	 * @param //mOnClosedListener
	 */
	public void setmOnFolderClosedListener(OnFolderClosedListener onFolderClosedListener) {
		this.mOnFolderClosedListener = onFolderClosedListener;
	}

	/**
	 * @return true if the folder is showing, false otherwise
	 */
	public boolean isOpened() {
		return mIsOpened;
	}

	/**
	 * colse the folder
	 */
	public void dismiss() {
		if (!mIsOpened) {
			return;
		}

		//关闭动画
		TranslateAnimation tra = new TranslateAnimation(0, 0, offsetyLast,
				offsety);
		tra.setInterpolator(new DecelerateInterpolator());
		tra.setDuration(ANIMALTION_TIME);
		tra.setFillAfter(true);
		tra.setAnimationListener(mClosedAnimationListener);

		if (mUp_down == 0) {
			mTopView.startAnimation(tra);
		} else {
			mBottomView.startAnimation(tra);
		}

	}

	/**
	 * 
	 *
	 */
	private class OpenFolderContainer extends RelativeLayout {

		long lasttime = 0;
		boolean isvalid = false;

		public OpenFolderContainer(Context context) {
			super(context);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.ViewGroup#dispatchKeyEvent(android.view.KeyEvent)
		 */
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (getKeyDispatcherState() == null) {
					return super.dispatchKeyEvent(event);
				}

				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getRepeatCount() == 0) {
					KeyEvent.DispatcherState state = getKeyDispatcherState();
					if (state != null) {
						state.startTracking(event, this);
					}
					return true;
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					KeyEvent.DispatcherState state = getKeyDispatcherState();
					if (state != null && state.isTracking(event)
							&& !event.isCanceled()) {
						dismiss();
						return true;
					}
				}
				return super.dispatchKeyEvent(event);
			} else {
				return super.dispatchKeyEvent(event);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			final int y = (int) event.getY();

			isvalid = System.currentTimeMillis() - lasttime > ANIMALTION_TIME;
			lasttime = System.currentTimeMillis();

			if ((event.getAction() == MotionEvent.ACTION_DOWN) && isvalid
					&& (y < mFolderUpY || y > mFolderDownY)) {
				dismiss();
				return true;
			} else {
				return super.onTouchEvent(event);
			}
		}
	}

	public void prepareLayout2(int heigth) {

		if(mWindowIsAdd){
			Log.e(TAG, "container view has already been added to the window manager!!!");
			return;
		}
		container.removeAllViews();
		
		//add  arrow up
		RelativeLayout.LayoutParams fp_arrow = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 10);
		if (mUp_down == 0) {
//			fp_arrow.setMargins(anchor.getLeft() + anchor.getWidth() + anchor.getWidth()/3, offsety - mFolderheigh + mFolderView.getHeight() -10, anchor.getRight(), 0);
//			ImageView arrow = new ImageView(mContext);
//			arrow.setBackgroundResource(R.drawable.arrow_down);
//			container.addView(arrow, fp_arrow);
		}else {
			fp_arrow.setMargins(anchor.getLeft() + anchor.getWidth()/2, offsety+mAnchorLocation[0], anchor.getRight(), 0);
//			fp_arrow.leftMargin = anchor.getLeft();
			ImageView arrow = new ImageView(mContext);
			arrow.setBackgroundResource(R.drawable.ic_openfolder_up);
			container.addView(arrow, fp_arrow);
		}
		
		// add folderview
		RelativeLayout.LayoutParams fp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, mFolderheigh);
		if (mUp_down == 0) {
			fp.setMargins(0, offsety - mFolderheigh, 0, 0);
		} else {
			fp.setMargins(0, offsety + 10+mAnchorLocation[0], 0, 0);
		}
		container.addView(mFolderView, fp);
		
		// 截当前view背景图，用于分割 topview bottomview
		mBackgroundView.setDrawingCacheEnabled(true);
		Bitmap srceen = mBackgroundView.getDrawingCache();
//		 Bitmap zero = Bitmap.createBitmap(srceen, 0, heigth,mSrceenwidth, mSrceenheigh-heigth);  
		// add topview
		// 截图控件以上部分
		Bitmap top = Bitmap.createBitmap(srceen, 0, 0, mSrceenwidth, offsety);
		mTopView = new ImageView(mContext);
//		mTopView.setId(1000);
		mTopView.setBackgroundDrawable(new BitmapDrawable(mContext
				.getResources(), top));
		RelativeLayout.LayoutParams ft = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, offsety);
		if (mUp_down == 0) {
			
		}else {
			ft.setMargins(0, mAnchorLocation[0], 0, 0);
		}
		
		container.addView(mTopView, ft);

		
		//add middle 截取控件下面10像素作为箭头的背景
		Bitmap middle = Bitmap.createBitmap(srceen, 0, offsety, mSrceenwidth, 10);
		container.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(),middle));
		
		// add bottomview
		// 截图控件以下部分
		Bitmap bottom = Bitmap.createBitmap(srceen, 0, offsety, mSrceenwidth,
				mSrceenheigh - offsety);
		mBottomView = new ImageView(mContext);
		mBottomView.setBackgroundDrawable(new BitmapDrawable(mContext
				.getResources(), bottom));
		RelativeLayout.LayoutParams fb = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, mSrceenheigh - offsety);
		if (mUp_down == 0) {

		} else {
			fb.setMargins(0, 0, 0, mAnchorLocation[0]);
		}
		fb.addRule(RelativeLayout.BELOW, 1000);
		container.addView(mBottomView, fb);
		if(!mWindowIsAdd){
			mWindowManager.addView(container, createPopupLayout(mBackgroundView.getWindowToken()));
			mWindowIsAdd = true;
		}
		
	}
	public void startOpenAnimation2() {
		// 展开动画
		if (mUp_down == 0) {

			offsety = 0;
			offsetyLast = 0 - mFolderheigh;
			TranslateAnimation ta = new TranslateAnimation(0, 0, offsety,
					offsetyLast);
			ta.setInterpolator(new DecelerateInterpolator());
			ta.setDuration(0);
			ta.setFillAfter(true);
			ta.setAnimationListener(mOpenAnimationListener);
			mTopView.startAnimation(ta);

		} else {

			offsety = 0;
			offsetyLast = 0 + mFolderheigh;
			TranslateAnimation ta = new TranslateAnimation(0, 0, offsety,
					offsetyLast);
			ta.setInterpolator(new DecelerateInterpolator());
			ta.setDuration(0);
			ta.setFillAfter(true);
			ta.setAnimationListener(mOpenAnimationListener);
			mBottomView.startAnimation(ta);
		}
	}
}
