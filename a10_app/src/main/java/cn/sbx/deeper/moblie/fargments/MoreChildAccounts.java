package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.ChildAccountsBean;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

public class MoreChildAccounts extends BaseFragment implements OnClickListener {
	private ArrayList<ChildAccountsBean> arrayList;
	private EditText et_xitong;
	private EditText et_loginNmae;
	private String deatName;
	private String detapassWord;
	private String name;
	private EditText et_loginpad;
	private ChildAccountsBean accountsBean;
	private String type;
	private String message;
	private String userNmae;
	private String loginPad;
	private String xitong;
	private String deteleMessage;
	private String deteleType;
	private ChuildDelete chuildDelete;
	private ChildComment comment;
	private Refresh refres;
	private Button bt_delete;
	private ProgressHUD fragment;
	private Button btn_save;
	private Button btn_next;
	private TextView tv_title;
	private Button bt_left;

	public static MoreChildAccounts newInstance(Bundle bundle, Refresh refresh) {
		MoreChildAccounts accounts = new MoreChildAccounts();
		accounts.setArguments(bundle);
		accounts.refres = refresh;
		return accounts;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_more_child_accounts,
				container, false);

		init(view);
		bt_left = (Button) view.findViewById(R.id.bt_left);
		bt_left.setVisibility(View.VISIBLE);
		bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backPrecious();
			}
		});

		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setVisibility(View.VISIBLE);
		btn_next.setText("删除");
		btn_next.setOnClickListener(this);

		btn_save = (Button) view.findViewById(R.id.btn_save);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btn_save
				.getLayoutParams();
		params.addRule(RelativeLayout.LEFT_OF, R.id.btn_next);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
		params.setMargins(0, 0, 20, 0);
		btn_save.setLayoutParams(params);
		btn_save.setVisibility(View.VISIBLE);
		btn_save.setText("提交");
		btn_save.setOnClickListener(this);

		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("设置");
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_IN_PARENT);
		params1.addRule(RelativeLayout.LEFT_OF, R.id.btn_save);
		params1.setMargins(0, 0, 20, 0);
		tv_title.setLayoutParams(params1);

		if (deatName.trim() != null && detapassWord != null) {
			System.out.println("=============>>>" + deatName
					+ "=====================>>>>" + detapassWord);
			bt_delete.setBackgroundResource(R.drawable.rt_scroll_selected);
			// }
			if ("".equalsIgnoreCase(deatName.trim())
					&& "".equalsIgnoreCase(detapassWord.trim())) {
				bt_delete.setBackgroundResource(R.drawable.rt_text_nomarl_time);
				bt_delete.setEnabled(false);
			}
		}

		return view;
	}

	private void init(View view) {
		et_loginNmae = (EditText) view.findViewById(R.id.et_loginNmae);
		et_loginNmae.setText(deatName);
		et_loginpad = (EditText) view.findViewById(R.id.et_loginpad);
		et_loginpad.setText(detapassWord.trim());
		et_xitong = (EditText) view.findViewById(R.id.et_xitong);
		et_xitong.setText(name);
		Button bt_connmit = (Button) view.findViewById(R.id.bt_connmit);
		bt_delete = (Button) view.findViewById(R.id.bt_delete);
		bt_connmit.setOnClickListener(this);
		bt_delete.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Bundle bundle = getArguments();
		accountsBean = (ChildAccountsBean) bundle.getSerializable("childList");
		deatName = accountsBean.getDetaname();
		detapassWord = accountsBean.getDetapassWord();
		name = accountsBean.getBsName();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_left:
			backPrecious();
			refres.getRefresh();
			break;
		case R.id.btn_save:
			userNmae = et_loginNmae.getText().toString().trim();
			loginPad = et_loginpad.getText().toString().trim();
			xitong = et_xitong.getText().toString().trim();
			if (userNmae.equals("")) {
				Toast.makeText(getActivity(), "帐号不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (loginPad.equals("")) {
				Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			// bt_delete.setBackgroundResource(R.drawable.rt_scroll_selected);
			comment = new ChildComment();
			comment.execute();
			break;
		case R.id.btn_next:
			if (et_loginNmae.getText().toString().trim().equals("")
					|| et_loginpad.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "删除失败",  Toast.LENGTH_SHORT).show();
				return;
			}
			chuildDelete = new ChuildDelete();
			chuildDelete.execute();
			break;
		default:
			break;
		}
	}

	public class ChildComment extends AsyncTask<Void, Void, ChildAccountsBean> {

		@Override
		protected void onPostExecute(ChildAccountsBean result) {
			super.onPostExecute(result);
			if (fragment != null)
				fragment.dismiss();
			if (result != null) {
				if ("1".equals(type)) {
					if (message != null) {
						Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
						backPrecious();
						refres.getRefresh();
						// 发送广播，更新数字
						Intent intent = new Intent(Constants.GET_APP_MENU_NUM);
						intent.putExtra("refresh", true);
						getActivity().sendBroadcast(intent);
					}
				} else {
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}
			}
		}

		@Override
		protected ChildAccountsBean doInBackground(Void... params) {
			childComment();
			return accountsBean;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fragment = AlertUtils.showDialog(getActivity(), null, this, false);
			fragment.setCanceledOnTouchOutside(false);
			 
		}
	}

	public class ChuildDelete extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (fragment != null)
				fragment.dismiss();
			if ("0".equals(deteleType)) {
				Toast.makeText(getActivity(), deteleMessage, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), deteleMessage, Toast.LENGTH_SHORT).show();
				WebUtils.username = "";
				WebUtils.password = "";
				backPrecious();
				refres.getRefresh();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			chuildDelete();
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fragment = AlertUtils.showDialog(getActivity(), null, this, false);
			fragment.setCanceledOnTouchOutside(false);
		}
	}

	private void chuildDelete() {
		// HttpClient client = new DefaultHttpClient();
		HttpParams httpParameters = new BasicHttpParams();
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(String.format(WebUtils.rootUrl
				+ URLUtils.US_DeleteAccount));
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		setHeader(httpPost);
		nameValuePairs.add(new BasicNameValuePair("AccoutId", accountsBean
				.getId()));

		System.out.println("userNmae :" + accountsBean.getBsName());
		InputStream is = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = hc.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			is = entity.getContent();
			// int leng;
			// byte[] arr = new byte[1024];
			// while ((leng = is.read(arr)) != -1) {
			// System.out.println(new String(arr, 0, leng) + "返回来的数据");
			// }
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						String msg = parser.getName();
						if ("result".equals(msg)) {
							deteleType = parser.nextText();
						} else if ("message".equalsIgnoreCase(msg)) {
							deteleMessage = parser.nextText();
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

	private void childComment() {
		// HttpClient client = new DefaultHttpClient();
		HttpParams httpParameters = new BasicHttpParams();
		DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(String.format(WebUtils.rootUrl
				+ URLUtils.US_SAVEUSERACCOUNT));
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		setHeader(httpPost);
		nameValuePairs.add(new BasicNameValuePair("Account", userNmae));
		nameValuePairs.add(new BasicNameValuePair("Password", loginPad));
		nameValuePairs.add(new BasicNameValuePair("BsId", accountsBean
				.getBsId()));
		nameValuePairs.add(new BasicNameValuePair("BsName", URLEncoder
				.encode(xitong)));
		nameValuePairs.add(new BasicNameValuePair("SCode",
				Constants.testPackage));
		InputStream is = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = hc.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			is = entity.getContent();
			// int leng;
			// byte[] arr = new byte[1024];
			// while ((leng = is.read(arr)) != -1) {
			// System.out.println(new String(arr, 0, leng) + "返回来的数据");
			// }
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						String msg = parser.getName();
						if ("result".equals(msg)) {
							type = parser.nextText();

						} else if ("message".equalsIgnoreCase(msg)) {
							message = parser.nextText();
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

	public interface Refresh {
		void getRefresh();
	}
}
