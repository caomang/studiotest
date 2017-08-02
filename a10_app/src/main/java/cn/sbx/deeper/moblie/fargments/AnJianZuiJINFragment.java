package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTD;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTR;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTable;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIInput;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 * 
 * @author terry.C
 * 
 */
@SuppressLint("ValidFragment")
public class AnJianZuiJINFragment extends BaseFragment implements
		OnClickListener {

	private CheckBox cb_left;
	private ExpandableListView elv_details;
	Context mContext;
	private String formPath;
	private String htmlPath;
	private String filePath;
	private String taskId;
	private BaseActivity activity;
	String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
	private String device_name = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		activity = (BaseActivity) getActivity();
		Bundle bundle = getArguments();
		device_name = bundle.getString("device_name");
		taskId=bundle.getString("taskId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_approve_detail_device,
				container, false);
		cb_left = (CheckBox) view.findViewById(R.id.cb_left);
		cb_left.setOnClickListener(this);
		cb_left.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.approve_back, 0, 0, 0);
		elv_details = (ExpandableListView) view.findViewById(R.id.elv_details);
		Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setVisibility(View.GONE);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		new ApproveDetailTask().execute(); // 下载数据
	}

	// 查询待办数据
	public SQLiteDatabase db;// 操作数据库的工具类
	StringBuilder sb;

	// 4326310000
	public void selectList(String user_id, String isnext, String type,
			String name) {
		if (device_name.equals("安检")) {
			// (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
			String sql = "select distinct * from anjian_data where user_id='4326310000'";
			db = SQLiteDatabase.openOrCreateDatabase(path, null);
			// 查询数据返回游标对象
			Cursor c = db.rawQuery(sql, null);

			// 到了提交的时刻了
			sb = new StringBuilder();
			if (c.getCount() == 0) {
				return;
			}
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><form>");
			while (c.moveToNext()) {
				// 通过游标对象获取值
				if (type.equals("1")) {

					sb.append("<table id=" + "\"1\"" + " title=" + "\"基本信息\""
							+ " expand=" + "\"true\"" + " editfield="
							+ "\"true\"" + " columns=" + "\"2\"" + ">");
					sb.append("<tr parent=\"\"" + "><td>" + "客户名称"
							+ "</td><td>" + c.getString(1) + "(user)"
							+ "</td></tr>");
					sb.append("<tr parent=\"\"" + "><td>" + "地址" + "</td><td>"
							+ c.getString(4) + c.getString(5) + c.getString(6)
							+ c.getString(7) + "栋" + c.getString(8) + "单元"
							+ c.getString(9) + "号" + "</td></tr>");
					sb.append("</table>");
					sb.append("<table id=" + "\"1\"" + " title=" + "\"安检信息\""
							+ " expand=" + "\"true\"" + " editfield="
							+ "\"true\"" + " columns=" + "\"2\"" + ">");
					sb.append("<tr parent=\"\"" + "><td>" + "安检日期"
							+ "</td><td>" + c.getString(13) + "</td></tr>");
					sb.append("<tr parent=\"\"" + "><td>" + "完成时间"
							+ "</td><td>" + c.getString(15) + "</td></tr>");
					sb.append("<tr parent=\"\"" + "><td>" + "安检入户情况"
							+ "</td><td>" + c.getString(16) + "</td></tr>");
					sb.append("<tr parent=\"\"" + "><td>" + "计量表读数"
							+ "</td><td>" + c.getString(19) + "</td></tr>");
					sb.append("<tr parent=\"\"" + "><td>" + "是否存在安全隐患"
							+ "</td><td>" + c.getString(41) + "</td></tr>");
					sb.append("<tr parent=\"\"" + "><td>" + "安全隐患整改通知单编号"
							+ "</td><td>" + c.getString(42) + "</td></tr>");
					sb.append("<tr parent=\"\"" + "><td>" + "备注" + "</td><td>"
							+ c.getString(43) + "</td></tr>");
					sb.append("</table>");

				}

			}
			c.close();
			sb.append("</form></root>");
			System.out.println(sb.toString());
		} else {
			db = SQLiteDatabase.openOrCreateDatabase(path, null);

			String sql = "select * from chaobiao_data where user_id=" + "'"
					+ user_id + "'";

			// 查询数据返回游标对象
			Cursor c = db.rawQuery(sql, null);

			// 到了提交的时刻了
			sb = new StringBuilder();
			if (c.getCount() == 0) {
				return;
			}
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><form>");
			while (c.moveToNext()) {
				// 通过游标对象获取值
				sb.append("<table id=" + "\"1\"" + " title=" + "\"基本信息\""
						+ " expand=" + "\"true\"" + " editfield=" + "\"true\""
						+ " columns=" + "\"2\"" + ">");
				sb.append("<tr parent=\"\"" + "><td>" + "客户名称" + "</td><td>"
						+ c.getString(1) + "(user)" + "</td></tr>");
				sb.append("<tr parent=\"\"" + "><td>" + "地址" + "</td><td>"
						+ c.getString(4) + c.getString(5) + c.getString(6)
						+ c.getString(7) + "栋" + c.getString(8) + "单元"
						+ c.getString(9) + "号" + "</td></tr>");
				sb.append("<tr parent=\"\"" + "><td>" + "抄表日期" + "</td><td>"
						+ c.getString(19) + "</td></tr>");
				sb.append("<tr parent=\"\"" + "><td>" + "抄表时间" + "</td><td>"
						+ c.getString(19) + "</td></tr>");
				sb.append("<tr parent=\"\"" + "><td>" + "表字" + "</td><td>"
						+ c.getString(21) + "</td></tr>");
				sb.append("<tr parent=\"\"" + "><td>" + "表字类型" + "</td><td>"
						+ c.getString(20) + "</td></tr>");
				sb.append("<tr parent=\"\"" + "><td>" + "气量" + "</td><td>"
						+ c.getString(22) + "</td></tr>");
				sb.append("<tr parent=\"\"" + "><td>" + "气费" + "</td><td>"
						+ c.getString(23) + "</td></tr>");
				if (c.getString(25) != null
						&& c.getString(25).trim().length() > 0) {
					sb.append("<tr parent=\"\"" + "><td>" + "历史欠费"
							+ "</td><td>" + c.getString(25) + "</td></tr>");
				} else {
					sb.append("<tr parent=\"\"" + "><td>" + "历史欠费"
							+ "</td><td>" + "0.0" + "</td></tr>");
				}
				sb.append("<tr parent=\"\"" + "><td>" + "备注" + "</td><td>"
						+ c.getString(32) + "</td></tr>");
//				sb.append("<tr parent=\"\"" + "><td>" + "拍照" + "</td><td>"
//						+ c.getString(27) + "(photo)2" + "</td></tr>");
//				sb.append("<tr parent=\"\"" + "><td>" + "录音" + "</td><td>"
//						+ c.getString(28) + "(audio)2" + "</td></tr>");

				sb.append("</table>");
				 
			}
			c.close();
			sb.append("</form></root>");
			System.out.println(sb.toString());
		}

	}

	private class ApproveDetailTask extends
			AsyncTask<Void, Void, SinopecApproveDetailEntry> {
		private ProgressHUD overlayProgress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			overlayProgress = AlertUtils.showDialog(getActivity(), null, this,
					false);

		}

		@Override
		protected void onPostExecute(final SinopecApproveDetailEntry result) {
			super.onPostExecute(result);
			if (overlayProgress != null)
				overlayProgress.dismiss();
			if (result != null) {
				elv_details
						.setAdapter(new MyAdapter(result.form.sinopecTables));
				for (int i = 0; i < result.form.sinopecTables.size(); i++) {
					SinopecTable table = result.form.sinopecTables.get(i);
					if ("true".equalsIgnoreCase(table.expand)) {
						elv_details.expandGroup(i);
					}
				}
			}
		}

		@Override
		protected SinopecApproveDetailEntry doInBackground(Void... params) {
			InputStream inputStream = null;
			try {
				// inputStream =
				// getActivity().getAssets().open("7539000000.xml");

				selectList(taskId, "", "1", device_name);
				inputStream = new ByteArrayInputStream(sb.toString().getBytes(
						"UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return DataCollectionUtils.receiveApproveDetailForm1(inputStream,
					formPath, taskId);
		}
	}

	public class MyAdapter extends BaseExpandableListAdapter {

		List<SinopecTable> groupList = new ArrayList<SinopecTable>();
		List<List<SinopecTR>> childList = new ArrayList<List<SinopecTR>>();
		LinearLayout.LayoutParams fwParams;

		public MyAdapter(List<SinopecTable> sinopecTables) {
			fwParams = new LinearLayout.LayoutParams(-1, -2);
			groupList = sinopecTables;
			for (int i = 0; i < groupList.size(); i++) {
				SinopecTable sinopecTable = groupList.get(i);
				List<SinopecTR> subChildList = null;
				subChildList = new ArrayList<SinopecTR>();
				for (int j = 0; j < sinopecTable.trs.size(); j++) {
					SinopecTR sinopecTR = sinopecTable.trs.get(j);
					boolean bool = false;
					for (int k = 0; k < sinopecTR.tds.size(); k++) {
						SinopecTD sinopecTD = sinopecTR.tds.get(k);
						if (sinopecTD.sinopecViews == null
								|| sinopecTD.sinopecViews.size() == 0) {

						} else {
							if (sinopecTD.sinopecViews.get(0) instanceof UIInput) {
								UIInput uiInput = (UIInput) sinopecTD.sinopecViews
										.get(0);
								if ("hidden".equalsIgnoreCase(uiInput.type)) {
									bool = true;
								}
							}
						}
					}
					if (bool == false) {
						subChildList.add(sinopecTR);
					}
				}
				childList.add(subChildList);
			}
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			int columns = Integer
					.parseInt(groupList.get(groupPosition).columns);
			if (columns > 2 && childList.get(groupPosition).size() >= 1) {
				return 1;
			} else {
				return childList.get(groupPosition).size();
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.item_approve_detail_group, parent, false);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			tv_title.setText(groupList.get(groupPosition).title);

			// view.setBackgroundResource(R.drawable.approve_detail_group_bg);
			// if (isExpanded) {
			// iv_pic.setBackgroundResource(R.drawable.approve_detail_group_1);
			// } else {
			// iv_pic.setBackgroundResource(R.drawable.approve_detail_group_2);
			// }
			if (isExpanded) {
				iv_pic.setBackgroundResource(R.drawable.mail_noadd);
			} else {
				iv_pic.setBackgroundResource(R.drawable.mail_add);
			}
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = null;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.item_approve_detail_child2, parent, false);
				holder = new Holder();
				holder.ll_parent = (LinearLayout) convertView
						.findViewById(R.id.ll_parent);
				holder.ll_left = (LinearLayout) convertView
						.findViewById(R.id.ll_left);
				holder.ll_right = (LinearLayout) convertView
						.findViewById(R.id.ll_right);
				holder.ll_scroll = (LinearLayout) convertView
						.findViewById(R.id.ll_scroll);
				holder.ll_scrll_left = (LinearLayout) convertView
						.findViewById(R.id.ll_scrll_left);
				holder.hsv = (HorizontalScrollView) convertView
						.findViewById(R.id.hsv);
				holder.ll_content = (LinearLayout) convertView
						.findViewById(R.id.ll_content);
				holder.vv_height = (View) convertView
						.findViewById(R.id.vv_height);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			int columns = Integer
					.parseInt(groupList.get(groupPosition).columns);
			SinopecTR sinopecTR = childList.get(groupPosition).get(
					childPosition);
			// 获取列的集合
			List<SinopecTD> sinopecTDList = sinopecTR.tds;
			if (columns == 1) { // 如果是1列，表示内容为html格式
				holder.ll_parent.setVisibility(View.VISIBLE);
				holder.vv_height.setVisibility(View.GONE);
				holder.ll_left.setVisibility(View.VISIBLE);
				holder.ll_right.setVisibility(View.GONE); //
				holder.ll_scroll.setVisibility(View.GONE); // 隐藏滚动视图
				holder.ll_left.removeAllViews(); // 移除所有的视图
				for (int i = 0; i < sinopecTDList.size(); i++) {
					SinopecTD sinopecTD = sinopecTDList.get(i);
					if (sinopecTD.content.contains("(html)")) {
						createWebView(holder.ll_left, sinopecTD.content);
					} else {
						// 创建视图
						TextView textView = new TextView(getActivity());
						textView.setLayoutParams(new LinearLayout.LayoutParams(
								-1, -1));
						holder.ll_left.addView(textView);
						if ((sinopecTD.sinopecViews == null || sinopecTD.sinopecViews
								.size() == 0)) { // 表示没有input视图
							textView.setTextColor(Color.parseColor("#000000"));
							textView.setText((sinopecTD.content
									.contains("(html)") ? replaceHtml(sinopecTD.content)
									: sinopecTD.content));
							textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
							textView.setPadding(
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 20),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10));
							textView.setTag(null);
						} else {
							for (int j = 0; j < sinopecTD.sinopecViews.size(); j++) {
								if (sinopecTD.sinopecViews.get(j) instanceof UIInput) {
									UIInput uiInput = (UIInput) sinopecTD.sinopecViews
											.get(j);
									if (!"text".equalsIgnoreCase(uiInput.type)) {
										addInputView(textView, uiInput);
									}
								}
							}
						}
					}
				}
			} else if (columns == 2) { // 两列
				holder.ll_parent.setVisibility(View.VISIBLE);
				holder.vv_height.setVisibility(View.GONE);
				holder.ll_left.setVisibility(View.VISIBLE);
				holder.ll_right.setVisibility(View.VISIBLE); //
				holder.ll_scroll.setVisibility(View.GONE); // 隐藏滚动视图
				holder.ll_left.removeAllViews();
				holder.ll_right.removeAllViews();

				int count = 0;
				for (int i = 0; i < sinopecTDList.size(); i++) {
					if (TextUtils.isEmpty(sinopecTDList.get(i).content)
							&& sinopecTDList.get(i).sinopecViews.size() == 0) {
						count++;
					}
				}
				if (count == 2) {
					holder.ll_parent.setVisibility(View.GONE);
					holder.vv_height.setVisibility(View.VISIBLE);
					return convertView;
				}

				for (int i = 0; i < sinopecTDList.size(); i++) {
					SinopecTD sinopecTD = sinopecTDList.get(i); // 获取一个td
					if ((sinopecTD.sinopecViews == null || sinopecTD.sinopecViews
							.size() == 0)) { // 表示没有input视图
						if (i == 0) {
							// 创建视图
							TextView textView = new TextView(getActivity());
							textView.setLayoutParams(new LinearLayout.LayoutParams(
									-1, -1));
							holder.ll_left.addView(textView);
							textView.setTextColor(Color.parseColor("#3773c0"));
							textView.setText((sinopecTD.content
									.contains("(html)") ? replaceHtml(sinopecTD.content)
									: sinopecTD.content));
							textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
							textView.setPadding(
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 20),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10));
							textView.setTag(null);
						} else {
							if (sinopecTD.content.contains("(html)")) {
								createWebView(holder.ll_right,
										sinopecTD.content);
							} else if (sinopecTD.content.contains("(image)")) {
								createImageWebView(holder.ll_right,
										sinopecTD.content);
							} else {
								// 创建视图
								TextView textView = new TextView(getActivity());
								textView.setLayoutParams(new LinearLayout.LayoutParams(
										-1, -1));
								holder.ll_right.addView(textView);
								textView.setText((sinopecTD.content
										.contains("(html)") ? replaceHtml(sinopecTD.content)
										: sinopecTD.content));
								textView.setTextColor(Color.BLACK);
								textView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 16);
								textView.setPadding(
										cn.sbx.deeper.moblie.util.TextUtils
												.Dp2Px(getActivity(), 10),
										cn.sbx.deeper.moblie.util.TextUtils
												.Dp2Px(getActivity(), 10),
										cn.sbx.deeper.moblie.util.TextUtils
												.Dp2Px(getActivity(), 10),
										cn.sbx.deeper.moblie.util.TextUtils
												.Dp2Px(getActivity(), 10));
								textView.setTag(null);
							}
						}
					} else {
						for (int j = 0; j < sinopecTD.sinopecViews.size(); j++) {
							if (sinopecTD.sinopecViews.get(j) instanceof UIInput) {
								UIInput uiInput = (UIInput) sinopecTD.sinopecViews
										.get(j);
								if (i == 0) {
									if (!"text".equalsIgnoreCase(uiInput.type)) {
										// 创建视图
										TextView textView = new TextView(
												getActivity());
										textView.setLayoutParams(new LinearLayout.LayoutParams(
												-1, -2));
										holder.ll_left.addView(textView);
										addInputView(textView, uiInput);
									}
								} else {
									if ("text".equalsIgnoreCase(uiInput.type)) {
										EditText editText = new EditText(
												getActivity());
										editText.setLayoutParams(new LinearLayout.LayoutParams(
												-1, -2));
										holder.ll_right.addView(editText);
										addInputEditView(editText, uiInput);
									} else {
										TextView textView = new TextView(
												getActivity());
										textView.setLayoutParams(new LinearLayout.LayoutParams(
												-1, -2));
										holder.ll_right.addView(textView);
										addInputView(textView, uiInput);
									}
								}
							}
						}
					}
				}
			} else if (columns > 2) { // 三列或者三列以上的数据
				holder.ll_parent.setVisibility(View.VISIBLE);
				holder.vv_height.setVisibility(View.GONE);
				holder.ll_left.setVisibility(View.GONE);
				holder.ll_right.setVisibility(View.GONE); //
				holder.ll_scroll.setVisibility(View.VISIBLE); // 隐藏滚动视图
				creatViewThreadMoreView(holder.ll_scrll_left,
						holder.ll_content, childList.get(groupPosition));
			}
			return convertView;
		}

		// 创建出一个webview视图
		public void createWebView(LinearLayout layout, String content) {
			WebView webView = new WebView(getActivity());
			webView.setLayoutParams(new LinearLayout.LayoutParams(-1,
					cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(),
							100)));
			webView.setHorizontalScrollBarEnabled(false);
			webView.setVerticalScrollBarEnabled(false);
			webView.setPadding(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
					getActivity(), 10), cn.sbx.deeper.moblie.util.TextUtils
					.Dp2Px(getActivity(), 10),
					cn.sbx.deeper.moblie.util.TextUtils
							.Dp2Px(getActivity(), 10),
					cn.sbx.deeper.moblie.util.TextUtils
							.Dp2Px(getActivity(), 10));
			webView.setBackgroundColor(Color.parseColor("#f5f5f5"));
			webView.loadDataWithBaseURL(null, replaceHtmlWebView(content),
					"text/html", "UTF-8", null);
			webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			layout.addView(webView);
			webView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});
		}

		// 创建出一个imageWebview视图
		public void createImageWebView(LinearLayout layout, String content) {
			WebView webView = new WebView(getActivity());
			webView.setLayoutParams(new LinearLayout.LayoutParams(-1,
					cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(),
							100)));
			webView.setHorizontalScrollBarEnabled(false);
			webView.setVerticalScrollBarEnabled(false);
			webView.setPadding(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
					getActivity(), 10), cn.sbx.deeper.moblie.util.TextUtils
					.Dp2Px(getActivity(), 10),
					cn.sbx.deeper.moblie.util.TextUtils
							.Dp2Px(getActivity(), 10),
					cn.sbx.deeper.moblie.util.TextUtils
							.Dp2Px(getActivity(), 10));
			webView.setBackgroundColor(Color.parseColor("#f5f5f5"));
			webView.loadDataWithBaseURL(null, replaceImageWebView(content),
					"text/html", "UTF-8", null);
			webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			layout.addView(webView);
			webView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});
		}

		// 创建三列或者三列以上数据的滚动视图
		public void creatViewThreadMoreView(LinearLayout ll_left,
				final LinearLayout ll_content, List<SinopecTR> sinopecTRs) {
			int len = 0;
			// 创建之前先把控件清空下
			ll_left.removeAllViews();
			ll_content.removeAllViews();

			// 获取左右两边的数据之后，查看数据的个数，如果超过指定个数，需要设置高度
			for (int i = 0; i < sinopecTRs.size(); i++) {
				SinopecTR tr = sinopecTRs.get(i);
				for (int j = 0; j < tr.tds.size(); j++) {
					SinopecTD sinopecTD = tr.tds.get(j);
					if (TextUtils.isEmpty(sinopecTD.content)) {
						if (sinopecTD.sinopecViews.size() == 0) {
							continue;
						}
						if (sinopecTD.sinopecViews.get(0) instanceof UIInput) {
							if (((UIInput) sinopecTD.sinopecViews.get(0)).value
									.length() / 7 > 0) {
								if (len < ((UIInput) sinopecTD.sinopecViews
										.get(0)).value.length() / 7) {
									len = ((UIInput) sinopecTD.sinopecViews
											.get(0)).value.length() / 7;
								}
							}
						}
					} else {
						if (sinopecTD.content.length() / 7 > 0) {
							if (len < sinopecTD.content.length() / 7)
								len = sinopecTD.content.length() / 7;
						}
					}
				}
			}
			final int len0 = len;
			// 先把左边的数据给获取到
			List<SinopecTD> sinopectdList = new ArrayList<SinopecTD>();
			for (int i = 0; i < sinopecTRs.size(); i++) {
				SinopecTR sinopecTR = sinopecTRs.get(i);
				if (sinopecTR.tds.size() > 0) {
					sinopectdList.add(sinopecTR.tds.get(0));
				}
			}

			// 获取右边的数据
			final List<List<SinopecTD>> rightList = new ArrayList<List<SinopecTD>>();
			for (int i = 0; i < sinopecTRs.size(); i++) {
				SinopecTR sinopecTR = sinopecTRs.get(i);
				List<SinopecTD> list = new ArrayList<SinopecTD>();
				if (sinopecTR.tds.size() > 1) {
					for (int j = 1; j < sinopecTR.tds.size(); j++) {
						list.add(sinopecTR.tds.get(j));
					}
					rightList.add(list);
				}
			}

			// 创建左边的视图
			for (int i = 0; i < sinopectdList.size(); i++) {
				final int index = i;
				final SinopecTD sinopecTD = sinopectdList.get(i);
				final TextView textView = new TextView(getActivity());
				textView.setLayoutParams(new LinearLayout.LayoutParams(-1,
						cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
								getActivity(), 20 + 16 * (len0 + 1))));
				textView.setPadding(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
						getActivity(), 10), cn.sbx.deeper.moblie.util.TextUtils
						.Dp2Px(getActivity(), 10),
						cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
								getActivity(), 10),
						cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
								getActivity(), 10));
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				ll_left.addView(textView);
				if (sinopecTD.sinopecViews.size() == 0) { // 如果是普通文本
					textView.setText((sinopecTD.content.contains("(html)") ? replaceHtml(sinopecTD.content)
							: sinopecTD.content));
					textView.setBackgroundColor(Color.parseColor("#ebf1ff"));
					textView.setTextColor(Color.parseColor("#3773c0"));
					textView.setTag(null);
				} else {
					if (sinopecTD.sinopecViews.get(0) instanceof UIInput) { // 如果是非text的文本
						UIInput uiInput = (UIInput) sinopecTD.sinopecViews
								.get(0);
						if (!"text".equalsIgnoreCase(uiInput.type)) {
							addInputView(textView, uiInput);
						}
					}
				}
				// 先在这儿获取下宽高，然后赋值给scrollview的子元素
				textView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (textView.getWidth() != 0) { // 添加视图
							addScrollView(ll_content, rightList.get(index),
									textView.getMeasuredWidth(),
									textView.getMeasuredHeight(), index);
						}
					}
				});

				// 横线
				View lineView = new View(getActivity());
				lineView.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
				lineView.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
				ll_left.addView(lineView);
			}
		}

		public void addScrollView(LinearLayout layout, List<SinopecTD> list,
				int width, int height, int index) {
			// 添加第一个视图
			LinearLayout ll_first = new LinearLayout(getActivity());
			ll_first.setLayoutParams(new LinearLayout.LayoutParams(-2, height));
			ll_first.setOrientation(LinearLayout.HORIZONTAL);
			ll_first.setGravity(Gravity.CENTER);
			layout.addView(ll_first);
			for (int i = 0; i < list.size(); i++) {
				final SinopecTD sinopecTD = list.get(i);
				if (sinopecTD.sinopecViews.size() == 0) { // 如果是普通文本的话
					final TextView textView = new TextView(getActivity());
					textView.setLayoutParams(new LinearLayout.LayoutParams(
							width, -2));
					textView.setPadding(cn.sbx.deeper.moblie.util.TextUtils
							.Dp2Px(getActivity(), 10), 0,
							cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
									getActivity(), 10), 0);
					textView.setText((sinopecTD.content.contains("(html)") ? replaceHtml(sinopecTD.content)
							: sinopecTD.content));
					textView.setTextColor(Color.BLACK);
					textView.setTag(null);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					ll_first.addView(textView);
					textView.setMovementMethod(new ScrollingMovementMethod());
					// if (index != 0) {
					// textView.setMovementMethod(new
					// ScrollingMovementMethod());
					// textView.setOnTouchListener(new OnTouchListener() {
					//
					// @Override
					// public boolean onTouch(View v, MotionEvent event) {
					// // TODO Auto-generated method stub
					// textView.getParent().requestDisallowInterceptTouchEvent(true);
					// return false;
					// }
					// });
					// }

				} else {
					if (sinopecTD.sinopecViews.get(0) instanceof UIInput) {
						UIInput uiInput = (UIInput) sinopecTD.sinopecViews
								.get(0);
						if (!"text".equalsIgnoreCase(uiInput.type)) { // 如果不是文本的话
							final TextView textView = new TextView(
									getActivity());
							textView.setLayoutParams(new LinearLayout.LayoutParams(
									width, -1));
							textView.setPadding(
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10),
									cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
											getActivity(), 10));
							textView.setGravity(Gravity.CENTER_VERTICAL);
							textView.setText((sinopecTD.content
									.contains("(html)") ? replaceHtml(sinopecTD.content)
									: sinopecTD.content));
							textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
							textView.setTextColor(Color.BLACK);
							ll_first.addView(textView);
							addInputView(textView, uiInput);
							textView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (v.getTag() == null)
										return;
									startFragmentView((UIInput) v.getTag());
								}
							});
						} else {
							// 如果是文本的话，
							EditText editText = new EditText(getActivity());
							editText.setLayoutParams(new LinearLayout.LayoutParams(
									width, -1));
							editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
							editText.setText(uiInput.value);
							editText.setHint("请输入");
							editText.setTextColor(Color.BLACK);
							editText.setBackgroundColor(Color.WHITE);
							ll_first.addView(editText);
							editText.addTextChangedListener(new MyTextWatcher(
									uiInput));
						}
					}
				}

				// 竖线
				View verLine = new View(getActivity());
				verLine.setLayoutParams(new LinearLayout.LayoutParams(1, height));
				verLine.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
				ll_first.addView(verLine);
			}

			// 横线
			View horLine = new View(getActivity());
			horLine.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
			horLine.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
			layout.addView(horLine);
		}

		// 转换html为文本
		public String replaceHtml(String str) {
			str = str.replaceAll("\\(html\\)", "");
			return Html.fromHtml(str).toString();
		}

		// 转换html
		public String replaceHtmlWebView(String str) {
			str = str.replaceAll("\\(html\\)", "");
			return str;
		}

		// 转换image
		public String replaceImageWebView(String str) {
			str = str.replaceAll("\\(image\\)", "");
			return str;
		}

		/**
		 * 如果类型是input的话，需要创建出视图,需要给控件赋上tag值，为了给点击各自的点击事件使用,里面会赋值，赋大小、颜色
		 * 
		 * @param view
		 * @param uiInput
		 */
		public void addInputView(TextView view, UIInput uiInput) {
			if (view instanceof TextView) {
				TextView textView = (TextView) view;
				textView.setPadding(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
						getActivity(), 10), cn.sbx.deeper.moblie.util.TextUtils
						.Dp2Px(getActivity(), 10),
						cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
								getActivity(), 10),
						cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
								getActivity(), 10));
				textView.setText(uiInput.value.trim());
				textView.setTextColor(Color.BLACK);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				textView.setTag(null);
				if ("file".equalsIgnoreCase(uiInput.type)
						|| "form".equalsIgnoreCase(uiInput.type)
						|| "html".equalsIgnoreCase(uiInput.type)
						|| "url".equalsIgnoreCase(uiInput.type)) {
					textView.setTextColor(Color.BLUE);
					textView.setText(uiInput.title.trim());
					textView.setTag(uiInput);
				} else if ("label".equalsIgnoreCase(uiInput.type)) {
					textView.setTextColor(Color.parseColor("#3773c0"));
					textView.setText(uiInput.title.trim());
				}
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (v.getTag() == null)
							return;
						startFragmentView((UIInput) v.getTag());
					}
				});
			}
		}

		// 添加edittext控件
		public void addInputEditView(View view, UIInput uiInput) {
			EditText editText = (EditText) view;
			editText.setTextColor(Color.BLACK);
			editText.setBackgroundColor(Color.WHITE);
			editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			editText.setText(uiInput.value.trim());
			editText.setHint("请输入");
			editText.setTag(null);
			editText.addTextChangedListener(new MyTextWatcher(uiInput));
		}

		// 根据不同的类型跳转到不同的跳转界面上
		public void startFragmentView(final UIInput uiInput) {
			/*if ("form".equalsIgnoreCase(uiInput.type)) {
				// SinopecApproveOADetailFormFragment formFragment = new
				// SinopecApproveOADetailFormFragment();
				// Bundle bundle = new Bundle();
				// bundle.putString("formpath", formPath);
				// bundle.putString("htmlPath", htmlPath);
				// bundle.putString("filePath", filePath);
				// bundle.putString("key", uiInput.value);
				// formFragment.setArguments(bundle);
				// ((ActivityInTab) getActivity()).navigateTo(formFragment);
				Toast.makeText(getActivity(), "暂不支持", 1).show();
			} else if ("html".equalsIgnoreCase(uiInput.type)) {
				SinopecApproveOADetailHtmlFragment formFragment = new SinopecApproveOADetailHtmlFragment();
				Bundle bundle = new Bundle();
				bundle.putString("htmlPath", htmlPath);
				bundle.putString("key1", uiInput.name);
				bundle.putString("key2", uiInput.value);
				formFragment.setArguments(bundle);
				((ActivityInTab) getActivity()).navigateTo(formFragment);
			} else if ("url".equalsIgnoreCase(uiInput.type)) {
				SinopecApproveOADetailUrlFragment formFragment = new SinopecApproveOADetailUrlFragment();
				Bundle bundle = new Bundle();
				bundle.putString("urlPath", uiInput.value);
				formFragment.setArguments(bundle);
				((ActivityInTab) getActivity()).navigateTo(formFragment);
			} else if ("file".equalsIgnoreCase(uiInput.type)) {
				// DownloadUtils.downLoadFile(getActivity(), uiInput.value);
				DownloadUtils.downloadIntranetFile(getActivity(),
						uiInput.value, filePath);
			} else {

			}*/
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	public class Holder {
		public LinearLayout ll_parent;
		public LinearLayout ll_left;
		public LinearLayout ll_right;
		// 下面四个是滚动视图用的
		public LinearLayout ll_scroll;
		public LinearLayout ll_scrll_left;
		public HorizontalScrollView hsv;
		public LinearLayout ll_content;
		public View vv_height;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (cb_left.getId() == v.getId()) {
			backPrecious();
		}
	}

	// 文本监听
	public class MyTextWatcher implements TextWatcher {

		private Object object;

		public MyTextWatcher(Object object0) {
			object = object0;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (object instanceof UIInput) {
				UIInput uiInput = (UIInput) object;
				uiInput.value = s.toString();
				uiInput.tag.value = s.toString();

			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

	}
}