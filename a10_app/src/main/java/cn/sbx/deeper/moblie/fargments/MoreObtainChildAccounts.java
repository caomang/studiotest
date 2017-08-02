package cn.sbx.deeper.moblie.fargments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.ChildAccountsBean;
import cn.sbx.deeper.moblie.fargments.MoreChildAccounts.Refresh;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

public class MoreObtainChildAccounts extends BaseFragment implements Refresh {

	private ChildBaseAdapter adapter;
	private ListView listView;
	private ArrayList<ChildAccountsBean> moreUserInfoList;
	private List<String> moreUserInfoChild;
	private String bsName;
	private ChildAccountsBean accounts;
	private ChildAccounts childAccounts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.more_child_accounts, container,
				false);
		listView = (ListView) view.findViewById(R.id.childList);
		Button bt_left = (Button) view.findViewById(R.id.bt_left);
		bt_left.setVisibility(View.VISIBLE);
		bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backPrecious();
			}
		});
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("设置");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		moreUserInfoList = new ArrayList<ChildAccountsBean>();
		moreUserInfoChild = new ArrayList<String>();

		childAccounts = new ChildAccounts();
		childAccounts.execute();
	}

	public class ChildBaseAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return moreUserInfoChild.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_obtain_child_accounts, null);
			TextView textView = (TextView) view.findViewById(R.id.itme_tv);
			LinearLayout ll_click = (LinearLayout) view
					.findViewById(R.id.ll_click);
			String childUserInfo = moreUserInfoChild.get(position);

			textView.setText(childUserInfo);

			ll_click.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("childList",
							moreUserInfoList.get(position));
					MoreChildAccounts childAccounts = MoreChildAccounts
							.newInstance(bundle, MoreObtainChildAccounts.this);
					((ActivityInTab) (getActivity())).navigateTo(childAccounts);
				}
			});
			return view;
		}

	}

	class ChildAccounts extends AsyncTask<Void, Void, Void> {
		private ProgressHUD overlayProgress;
		private boolean pro = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			overlayProgress = AlertUtils.showDialog(getActivity(), null, this,
					false);
			overlayProgress.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					pro = false;
				}
			});
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (overlayProgress != null) {
				overlayProgress.dismiss();
			}
			if (pro) {
				if (adapter == null) {
					adapter = new ChildBaseAdapter();
					listView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			checkChildAccounts();
			return null;
		}
	}

	private void checkChildAccounts() {
		moreUserInfoChild = new ArrayList<String>();
		moreUserInfoList = new ArrayList<ChildAccountsBean>();
		// HttpClient client = new DefaultHttpClient();
		HttpParams httpParameters = new BasicHttpParams();
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(String.format(WebUtils.rootUrl
				+ URLUtils.US_GETCHILD));
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		setHeader(httpPost);
		nameValuePairs.add(new BasicNameValuePair("CVersion",
				DataCache.sinopecMenu.version));
		nameValuePairs.add(new BasicNameValuePair("SCode",
				Constants.testPackage));// WebUtils.packageName
		InputStream is = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = hc.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			is = entity.getContent();
//			 System.out.println(StreamUtils.retrieveContent(is));
			// int leng;
			// byte[] arr = new byte[1024];
			// while ((leng = is.read(arr)) != -1) {
			// System.out.println(new String(arr, 0, leng) + "返回来的数据");
			// }

			String username = "";
			String password = "";
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						if ("user".equalsIgnoreCase(parser.getName())) {
							username = (parser.getAttributeValue(0));
							password = (parser.getAttributeValue(1));
						} else if ("detail".equalsIgnoreCase(parser.getName())) {
							accounts = new ChildAccountsBean();
							accounts.setName(username);
							accounts.setPassWord(password);
							accounts.setDetaname(parser.getAttributeValue(0));
							accounts.setDetapassWord(parser
									.getAttributeValue(1));
							accounts.setId(parser.getAttributeValue(2));
							accounts.setBsId(parser.getAttributeValue(3));
							accounts.setBsName(parser.getAttributeValue(4));

							bsName = parser.getAttributeValue(4);

							moreUserInfoList.add(accounts);
							moreUserInfoChild.add(bsName);
						}
					}
					eventType = parser.next();
				}
			} else if (500 == httpResponse.getStatusLine().getStatusCode()) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static void setHeader(HttpPost httpPost) {
		httpPost.setHeader("EquipType", "Android");
		httpPost.setHeader("EquipSN", WebUtils.deviceId);
		httpPost.setHeader("Soft", WebUtils.packageName);
		httpPost.setHeader("Tel", WebUtils.phoneNumber);
		httpPost.setHeader("Cookie", WebUtils.cookie);
	}

	@Override
	public void getRefresh() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				childAccounts = new ChildAccounts();
				childAccounts.execute();
			}
		});
	}
}
