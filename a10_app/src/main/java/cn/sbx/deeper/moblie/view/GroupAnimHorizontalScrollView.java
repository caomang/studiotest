package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;

public class GroupAnimHorizontalScrollView extends HorizontalScrollView {

	public GroupAnimHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GroupAnimHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GroupAnimHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void scroll(int width) {
		MyOnGlobalLayoutListener listener = new MyOnGlobalLayoutListener(width);
		getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}

//	@Override
//	protected void onAttachedToWindow() {
//		super.onAttachedToWindow();
//		ScaleAnimation anim = new ScaleAnimation(1, 1, 0, 1);
//		anim.setDuration(240);
//		anim.setFillAfter(true);
//		this.startAnimation(anim);
//	}

	class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {
		int width = 0;

		public MyOnGlobalLayoutListener(int width) {
			this.width = width;
		}

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			getViewTreeObserver().removeGlobalOnLayoutListener(this);
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					GroupAnimHorizontalScrollView.this.scrollTo(width, 0);
				}
			});
		}

	}
}
