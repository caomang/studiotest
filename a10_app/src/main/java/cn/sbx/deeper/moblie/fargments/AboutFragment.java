package cn.sbx.deeper.moblie.fargments;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.AboutDetail;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import petrochina.ghzy.a10fieldwork.R;

public class AboutFragment extends BaseFragment {
	private FinalBitmap fb;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("关于我们");
		fb = FinalBitmap.create(getActivity());//初始化FinalBitmap模块
//		fb.configLoadingImage(R.drawable.ic_sunbox_launcher);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.about, container, false);
		Button button = (Button) v.findViewById(R.id.about_iv_barck);
		about_abstract = (TextView) v.findViewById(R.id.about_abstract);
		verson_app = (TextView) v.findViewById(R.id.verson_app);
		about_tv = (TextView)v.findViewById(R.id.about_tv);
		about_tvs = (TextView)v.findViewById(R.id.about_tvs);
		about_iv = (ImageView) v.findViewById(R.id.about_iv);
//		Button bt_left = (Button) v.findViewById(R.id.bt_left);
//		bt_left.setVisibility(View.VISIBLE);
//		bt_left.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				backPrecious();
//			}
//		});
//		TextView tv_title = (TextView)v.findViewById(R.id.tv_title);
//		tv_title.setText("关于我们");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backPrecious();
			}
		});
		setupView(v);
		try {
			versionName = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0).versionName;
//			tv_version_name.setText("版本号：v" + versionName);
			verson_app.setText("版本信息 ：v" + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			verson_app.setText("版本信息 ：未知");
		}
		
//		MyAboutAsycTask aboutAsycTask = new MyAboutAsycTask();
//		aboutAsycTask.execute();
		return v;
	}

//	private TextView tv_version_name;
	private String versionName;
	// private Button bt_left;
	private TextView tv_title;
	private TextView about_abstract,verson_app;
	private TextView about_tv;
	private TextView about_tvs;
	private ImageView about_iv;

	private void setupView(View v) {
//		tv_version_name = (TextView) v.findViewById(R.id.tv_version_name);
		// bt_left = (Button) v.findViewById(R.id.bt_left);
		// bt_left.setVisibility(View.VISIBLE);
		// bt_left.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// getActivity().onBackPressed();
		// }
		// });
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	
	public class MyAboutAsycTask extends AsyncTask<Void, Void, AboutDetail> {
		@Override
		protected void onPostExecute(AboutDetail result) {
			super.onPostExecute(result);
			if (result != null) {
				String string = result.getBiglogo();
				String  version = result.getVersion();
				String name = result.getName();
				String abstracts = result.getAbstracts();
				about_tv.setText(name);
//				about_tv.setTextColor(R.color.black);
				about_tvs.setText("V" +version +"版");
//				about_tvs.setTextColor(R.color.black);
				about_abstract.setText(abstracts);
//				about_abstract.setTextColor(R.color.black);
				if(result.getBiglogo()!=null && !result.getBiglogo().equals("")){
					fb.display(about_iv, WebUtils.rootUrl+ result.getBiglogo());//"/DataMutualCenter" 
					about_iv.setVisibility(View.VISIBLE);
				}else{
					about_iv.setImageResource(R.drawable.ic_sunbox_launcher);
					about_iv.setVisibility(View.VISIBLE);
				}
				System.out.println("图片的路径=" + string + "minf ddd " + abstracts + "version=" + version +"nameString="+name);
				
			}
		}

		@Override
		protected AboutDetail doInBackground(Void... params) {
			 AboutDetail aboutDetail = getPostAbout();
			 return aboutDetail;
		}
	}

	public AboutDetail getPostAbout() {
		InputStream is = null;
		AboutDetail aboutDetail = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(String.format(WebUtils.rootUrl + URLUtils.getAppDetail));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("SCode", Constants.testPackage));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			setHeader(httpPost);
			HttpResponse response = hc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			if (200 == response.getStatusLine().getStatusCode()) {
				
//				int arr = 0;
//				byte[] leng = new byte[1024];
//				while ((arr = is.read(leng)) > 0) {
//					System.out.println(new String(leng, 0, arr) + "关于我们返回来的数据");;
//				}
				
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {
					switch (event) {
					case XmlPullParser.START_TAG:
						if("app".equalsIgnoreCase(parser.getName())){
							aboutDetail = new AboutDetail();
							aboutDetail.setName(parser.getAttributeValue(1));
							aboutDetail.setBiglogo(parser.getAttributeValue(5));
							aboutDetail.setVersion(parser.getAttributeValue(9));
						}else if("abstract".equalsIgnoreCase(parser.getName())){
							aboutDetail.setAbstracts(parser.nextText());
						}
						break;
					}
					event = parser.next();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
						e.printStackTrace();
				}
			}
		}
		return aboutDetail;
	}

	private static void setHeader(HttpPost httpPost) {
		httpPost.setHeader("EquipType", "Android");
		httpPost.setHeader("EquipSN", WebUtils.deviceId);
		httpPost.setHeader("Soft", WebUtils.packageName);
		httpPost.setHeader("Tel", WebUtils.phoneNumber);
		httpPost.setHeader("Cookie", WebUtils.cookie);
	}
	
//	01-06 10:05:53.746: I/System.out(14064): 图片的路径=/app/software/20131205101353321120131769.pngminf ddd 移动开发中心采用混合配置开发模式，通过原生开发+HMTL5开发的模式实现移动应用各类业务功能，快速实现多平台开发、部署和更新。

}
