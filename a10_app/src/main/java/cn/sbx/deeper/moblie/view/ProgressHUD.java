package cn.sbx.deeper.moblie.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import petrochina.ghzy.a10fieldwork.R;


public class ProgressHUD extends Dialog implements Serializable {
	static Context mContext;
	static ImageView imageView;
	
	
	  private static ProgressHUD uniqueInstance = null;  
	   
	    public static ProgressHUD getInstance(Context context, int theme) {  
	       if (uniqueInstance == null) {  
	           uniqueInstance = new ProgressHUD(context, theme);  
	       }  
	       return uniqueInstance;  
	    } 
	
	
	

	private ProgressHUD(Context context) {
		super(context);
	}

	public ProgressHUD(Context context, int theme) {
		super(context, theme);
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		imageView = (ImageView) findViewById(R.id.spinnerImageView);
		// AnimationDrawable spinner = (AnimationDrawable)
		// imageView.getBackground();
		// spinner.start();

	}

	public void setMessage(CharSequence message) {
		if (message != null && message.length() > 0) {
			findViewById(R.id.message).setVisibility(View.VISIBLE);
			TextView txt = (TextView) findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}

	public static ProgressHUD show(Context context, CharSequence message,
			boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		mContext = context;
		Animation operatingAnim = AnimationUtils.loadAnimation(context,
				R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			imageView.startAnimation(operatingAnim);
		}
		ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
		dialog.setTitle("");
		dialog.setContentView(R.layout.fragment_overlay_progress);
		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);

		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();

		return dialog;
	}
}
