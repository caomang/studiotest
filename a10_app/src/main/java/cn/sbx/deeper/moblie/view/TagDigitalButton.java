package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import cn.sbx.deeper.moblie.util.BitmapUtils;
import petrochina.ghzy.a10fieldwork.R;


/**
 * approve oa tab radio include number
 * 
 * @author terry.C
 * 
 */

public class TagDigitalButton extends RadioButton {

	private Bitmap numberFrameBackground;
	private Paint paint;
	private int tabNumber;
	private int frameMarginleft;
	private int frameMarginTop;
	private int fragmeWidth;
	private int frameHeight;
	private Drawable frame;

	public TagDigitalButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initBackground();
	}

	public TagDigitalButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBackground();
	}

	public TagDigitalButton(Context context) {
		super(context);
		initBackground();
	}

	void initBackground() {
		// setTextAppearance(getContext(), R.style.style_approve_tab_text);
		setCompoundDrawables(null, null, null, null);
		setButtonDrawable(android.R.color.transparent);
		setPadding(0, 0, 0, 0);
		setGravity(Gravity.CENTER);
		setCompoundDrawablePadding(0);
		tabNumber = 0;
		initPaint();
		fragmeWidth = getResources().getDimensionPixelOffset(
				R.dimen.tab_frame_width);
		frameHeight = getResources().getDimensionPixelOffset(
				R.dimen.tab_frame_width);
		frame = getResources().getDrawable(R.drawable.frame_bg);
		numberFrameBackground = BitmapUtils.drawableToBitmap(frame,
				fragmeWidth, frameHeight);
		frameMarginleft = getResources().getDimensionPixelOffset(
				R.dimen.tab_radio_rect_x);
		frameMarginTop = getResources().getDimensionPixelOffset(
				R.dimen.tab_radio_rect_y);
	}

	void initPaint() {
		paint = new Paint();
		paint.setTextSize(getResources().getDimension(
				R.dimen.tab_radio_number_rect));
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (tabNumber != 0) {
			canvas.drawBitmap(numberFrameBackground, frameMarginleft,
					frameMarginTop, paint);
			canvas.drawText(String.valueOf(tabNumber), frameMarginleft
					+ fragmeWidth / 2
					- (getTextWidth(String.valueOf(tabNumber)) / 2),
					frameMarginTop + frameHeight / 2 + getTextHeight() / 2 + 2,
					paint);
		}else{
          initPaint();  
          canvas.drawPaint(paint);  
		}
	}

	private float getTextWidth(String text) {
		return paint.measureText(text);
	}

	private float getTextHeight() {
		return paint.measureText("0");
	}

	public void setTabNumber(int number) {
		this.tabNumber = number;
		if (tabNumber > 99) {
//			fragmeWidth = fragmeWidth + (int) getTextWidth("0");
			fragmeWidth = getResources().getDimensionPixelOffset(
					R.dimen.tab_frame_width) + (int) getTextWidth("0");
//			frameMarginleft -= (int) getTextWidth("0");
			frameMarginleft = getResources().getDimensionPixelOffset(
					R.dimen.tab_radio_rect_x)-(int) getTextWidth("0");
		} else {
			fragmeWidth = getResources().getDimensionPixelOffset(
					R.dimen.tab_frame_width);
		}
		numberFrameBackground = BitmapUtils.drawableToBitmap(frame,
				fragmeWidth, frameHeight);
		invalidate();
	}
}
