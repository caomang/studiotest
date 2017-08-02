package cn.sbx.deeper.moblie.fargments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import petrochina.ghzy.a10fieldwork.R;


public class ModifyLoginPasswordFragment extends Fragment implements OnClickListener {
	Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_setup_modify_password, container, false);
		setupView(v);
		WebView wv_modify_content = (WebView) v.findViewById(R.id.wv_modify_content);
		wv_modify_content.getSettings().setJavaScriptEnabled(true);
		wv_modify_content.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wv_modify_content.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// System.out.println("url:" + url);
				if (url.endsWith("/close.aspx")) {
					return true;
				} else {
					return super.shouldOverrideUrlLoading(view, url);
				}
			}
			// @Override
			// public boolean onJsAlert(WebView view, String url, String
			// message, final JsResult result) {
			// AlertDialog.Builder b2 = new AlertDialog.Builder(this)
			// .setTitle(R.string.title).setMessage(message)
			// .setPositiveButton("ok",
			// new AlertDialog.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// result.confirm();
			// // MyWebView.this.finish();
			// }
			// });
			//
			// b2.setCancelable(false);
			// b2.create();
			// b2.show();
			// return true;
			// }
		});
		wv_modify_content.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});
		wv_modify_content.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//				AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage(message)
				AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setMessage(message)
						.setPositiveButton("确定", new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.confirm();
							}
						});

				b2.setCancelable(false);
				b2.create();
				b2.show();
				return true;
			}
		});

		// wv_modify_content.getSettings().setBuiltInZoomControls(true);
		// wv_modify_content.getSettings().setUseWideViewPort(true);
		// wv_modify_content.getSettings().setSupportZoom(true);
		// wv_modify_content.getSettings().
		wv_modify_content.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		//wv_modify_content.addJavascriptInterface(new JavaScriptInterface(), "android");
		wv_modify_content.loadUrl(WebUtils.rootUrl + URLUtils.mobileOfficeModifyLoginPwd);
		return v;
	}

	private void setupView(View v) {
		Button bt_left = (Button) v.findViewById(R.id.bt_left);
		bt_left.setVisibility(View.VISIBLE);
		bt_left.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	String script = "window.android.closeActivity()";

	final class JavaScriptInterface {
		public void closeActivity() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					getActivity().onBackPressed();
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_left:
			getActivity().onBackPressed();
			break;
		}
	}
}
