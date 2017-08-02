package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class SyncHorizontalScrollView extends HorizontalScrollView {

	public SyncHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		inAnimation = new AlphaAnimation(0.0f, 1.0f);
		inAnimation.setDuration(1000);
		outAnimation = new AlphaAnimation(1.0f, 0.0f);
		outAnimation.setDuration(1000);
	}

	private ImageView leftImage;
	
	private ImageView rightImage;

	private Animation inAnimation;

	private Animation outAnimation;
	
	
	
	public SyncHorizontalScrollView(Context context) {
		super(context);
		
		inAnimation = new AlphaAnimation(0.0f, 1.0f);
		inAnimation.setDuration(1000);
		outAnimation = new AlphaAnimation(1.0f, 0.0f);
		outAnimation.setDuration(1000);
	}

	public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		inAnimation = new AlphaAnimation(0.0f, 1.0f);
		inAnimation.setDuration(1000);
		outAnimation = new AlphaAnimation(1.0f, 0.0f);
		outAnimation.setDuration(1000);
	}
	
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
//		System.out.println("SyncHorizontalScrollView.onScrollChanged()");
//		System.out.println("computeHorizontalScrollExtent:" + computeHorizontalScrollExtent());
//		System.out.println("computeHorizontalScrollOffset:" + computeHorizontalScrollOffset());
//		System.out.println("computeHorizontalScrollRange:" + computeHorizontalScrollRange());
//		System.out.println("getMaxScrollAmount ():" + getMaxScrollAmount ());
		
//		System.out.println("getScrollX() :" + getScrollX());
//		System.out.println("getScrollY() :" + getScrollY());
		
//		System.out.println("l:" + l);
//		System.out.println("t:" + t);
//		System.out.println("oldl:" + oldl);
//		System.out.println("oldt:" + oldt);
		
		if(computeHorizontalScrollOffset() <=0) {
			if(rightImage!=null) {
//				leftImage.startAnimation(outAnimation);
//				rightImage.startAnimation(inAnimation);
				rightImage.setVisibility(View.VISIBLE);
			}
		}
		
		if(computeHorizontalScrollOffset() >10 && computeHorizontalScrollOffset() <((computeHorizontalScrollRange() - computeHorizontalScrollExtent())-20)) {
			if(rightImage!=null) {
//				leftImage.startAnimation(inAnimation);
//				rightImage.startAnimation(inAnimation);
				rightImage.setVisibility(View.VISIBLE);
			}
		}
		
		if(computeHorizontalScrollOffset() >= ((computeHorizontalScrollRange() - computeHorizontalScrollExtent()) -20)) {
			if(rightImage!=null) {
//				rightImage.startAnimation(outAnimation);
				rightImage.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void setScrollView(ImageView v1, ImageView v2) {
		this.leftImage = v1;
		this.rightImage = v2;
	}

}
