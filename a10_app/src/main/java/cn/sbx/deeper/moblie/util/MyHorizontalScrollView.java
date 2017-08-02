package cn.sbx.deeper.moblie.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {

	public MyHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void scroll(int width) {
		MyOnGlobalLayoutListener listener = new MyOnGlobalLayoutListener(width);
		getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}
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
					MyHorizontalScrollView.this.scrollTo(width, 0);
				}
			});
		}

	}
}
