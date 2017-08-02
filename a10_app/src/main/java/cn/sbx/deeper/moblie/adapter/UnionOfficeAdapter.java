package cn.sbx.deeper.moblie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cn.sbx.deeper.moblie.domian.UnionOffice;
import cn.sbx.deeper.moblie.util.CacheUtils;
import petrochina.ghzy.a10fieldwork.R;


public class UnionOfficeAdapter extends BaseAdapter {
	Context mContext;
	List<UnionOffice> mUoContents;
	LayoutInflater mLin;
	String mCookie;

	public UnionOfficeAdapter(Context context, List<UnionOffice> uoContents,
			String cookie) {
		mContext = context;
		mUoContents = uoContents;
		mCookie = cookie;
		mLin = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mUoContents.size();
	}

	@Override
	public Object getItem(int position) {
		return mUoContents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View _v = CacheUtils.items.get(position);
		if (_v != null)
			return _v;
		final UnionOffice cuo = mUoContents.get(position);
		// if (convertView == null)
		// convertView = mLin.inflate(R.layout.layout_item_gv_union_office_w,
		// null);
		View view = mLin.inflate(R.layout.layout_item_gv_union_office_w, null);
		final WebView _wv = (WebView) view.findViewById(R.id.wv_uo_content);
		final ProgressBar _pbLoading = (ProgressBar) view
				.findViewById(R.id.pb_uo_web_content_loading);
		_wv.setClickable(false);
		_wv.setLongClickable(false);
		_wv.setFocusable(false);
		_wv.setFocusableInTouchMode(false);

		_wv.clearCache(true);
		_wv.clearView();
		_wv.getSettings().setJavaScriptEnabled(true);
//		_wv.getSettings().setPluginsEnabled(true);
		_wv.getSettings().setAppCacheEnabled(true);
		_wv.getSettings().setAllowFileAccess(true);
		_wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		_wv.getSettings().setDefaultZoom(ZoomDensity.FAR);
		_wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				_wv.loadDataWithBaseURL(
						null,
						"<div id='root'	style='width: 100%; min-height: 100%; text-align: center;'>		<div id='detail' style='margin-top: 50px; font-size: 20px'>点击查看详细</div>	</div>",
						"text/html", "utf-8", null);
			}
		});
		_wv.loadUrl(cuo.url);

		_wv.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				_pbLoading.setProgress(newProgress);
				if (newProgress == 100) {
					_pbLoading.setVisibility(View.INVISIBLE);
					_wv.invalidate();
					_wv.requestLayout();
				}
			}
		});

		_wv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				   if (event.getAction() == MotionEvent.ACTION_DOWN) { 
		                x1 = event.getX(); 
		                y1 = event.getY(); 
		            } 
		            if (event.getAction() == MotionEvent.ACTION_UP) { 
		                x2 = event.getX(); 
		                y2 = event.getY(); 
		                if (Math.abs(x1 - x2) < 6) { 
		                	/*DocumentsUnionOfficeDetailFragment fragment = new DocumentsUnionOfficeDetailFragment();
		    				Bundle bundle = new Bundle();
		    				bundle.putSerializable("UNION_OFFICE", cuo);
		    				bundle.putString("UNION_OFFICE_DETAIL_TITLE", cuo.title);
		    				bundle.putInt("UNION_OFFICE_DETAIL_POS", position);
		    				fragment.setArguments(bundle);
		    				((ActivityInTab) mContext).navigateTo(fragment);
		                    return false;// 距离较小，当作click事件来处理 */
		                } 
		                if(Math.abs(x1 - x2) >60){  // 真正的onTouch事件 
		                } 
		            } 
								return false;
			}
		});
		TextView _tvTitle = (TextView) view.findViewById(R.id.tv_uo_title);
		_tvTitle.setText(cuo.title);
		CacheUtils.items.put(position, view);
		return view;
	}
	float x1 = 0,y1=0,x2=0,y2=0;
}
