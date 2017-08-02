package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.Attachment;
import cn.sbx.deeper.moblie.domian.MeetingPdf;
import cn.sbx.deeper.moblie.domian.NoticeDetail;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SinopecMenuPage;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.HttpUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;


public class MobileIntranetNewsDetailFragment extends BaseFragment implements OnClickListener, OnItemClickListener {

	private TextView tv_title_intranet_detail;
	private TextView tv_author_notice_detail;
	private TextView tv_timer_notice_detail;
	private LinearLayout ll_attachements;
	private ListView ll_notice_detail_attachements;
	private NoticeDetail detail;

	Activity mContext;
//	public static String id;
	private Display display;
	private String width;
	private View lineview_start;
	private View lineview_end;
	View _btnTextSizeInc;
	View _btnTextSizeDec;

	public SharedPreferences sp;
	ProgressDialog _pd;

	private String contentPath = "";
	public static  String detailId = "";
	private String pdfPath = "";
	public static String id = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundles) {
		mContext = getActivity();
		Bundle bundle = getArguments();
		View v = inflater.inflate(R.layout.layout_intranet_detail, container, false);
		
		detailId = bundle.getString("detailId");
		id = bundle.getString("id");
		SinopecMenuModule menuModule =  (SinopecMenuModule) bundle.getSerializable("entry");
		initPathParamsPage(menuModule);
		
		
		contentPath = WebUtils.rootUrl + URLUtils.baseContentUrlNew;
		
		System.out.println("contentPath =新闻详细列表== >>>>" + detailId);
//		pdfPath = WebUtils.rootUrl + URLUtils.baseContentUrl + bundle.getString("path");
		
		display = mContext.getWindowManager().getDefaultDisplay();
		setupView(v);
		
		if (screenWidth > 480 && screenWidth <=800) {
			// 800 * 1280
			width = "380";
		} else if (screenWidth > 320 && screenWidth <=480) {
			// 800 * 400
			width = "300";
		}  else if (screenWidth > 800 && screenWidth <=1080) {
			// 800 * 400
			width = "340";
		}else if (screenWidth <= 320) {
			// 320*480
			width = "300";
		}
		
		System.out.println("screenWidth　＝＝＝＝＝　＞＞＞＞" +screenWidth);
		
		_btnTextSizeInc = v.findViewById(R.id.btn_text_size_inc);
		_btnTextSizeInc.setOnClickListener(this);
		_btnTextSizeDec = v.findViewById(R.id.btn_text_size_dec);
		_btnTextSizeDec.setOnClickListener(this);
		loadData();
		return v;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 
	 */
	private void loadData() {
		new AsyncTask<Void, Void, NoticeDetail>() {
			private ProgressHUD overlayProgress; 
			private boolean pro=true;
			@Override
			protected void onPostExecute(NoticeDetail result) {
				super.onPostExecute(result);
//				pb_loading.setVisibility(View.GONE);
				if(overlayProgress != null){
					overlayProgress.dismiss();
				}
				if (result != null) {
					detail = result;
					fillData(result);
				} else {
					Toast.makeText(mContext, "没有获取到数据", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
//				pb_loading.setVisibility(View.VISIBLE);
				overlayProgress = AlertUtils.showDialog(getActivity(), null, this, false);
				overlayProgress.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						pro = false;
					}
				});
			}

			@Override
			protected NoticeDetail doInBackground(Void... params) {
				try {
					// InputStream is = null;
					// is = ConnectionManager.getInputStream2(WebUtils.cookie,
					// contentPath, WebUtils.deviceId, id, "");
					// // byte[] data1 = LoadUtils.load(is);
					// // System.out.println("new String(data, charset): " + new
					// // String(data1, "UTF-8"));
					// // 本地模拟数据
					// // InputStream is =
					// //
					// MobileIntranetNewsDetailActivity.class.getClassLoader().getResourceAsStream("intranet_content.xml");
					// return IntranetLogic.getNoticeDetail(is);
					// String path = WebUtils.rootUrl +
					// URLUtils.mobileOfficeIntranetMainDetail + currentPageId +
					// "&prm1=" + id;
					NoticeDetail detail=DataCollectionUtils.getNoticeDetail(contentPath);
					if (pro) {
						return detail;
					}else {
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	protected void fillData(NoticeDetail result) {
//		tv_title_intranet_detail.setText(com.sbx.deeper.moblie.util.TextUtils.Trim(detail.title));
//		tv_author_notice_detail.setText(com.sbx.deeper.moblie.util.TextUtils.Trim(detail.publisher));
//		tv_timer_notice_detail.setText(com.sbx.deeper.moblie.util.TextUtils.Trim(detail.publishDate));
		
		tv_title_intranet_detail.setText(detail.getTitle());
		tv_author_notice_detail.setText(detail.getAuthor());
		tv_timer_notice_detail.setText(detail.getPublishDate());
		// tv_content_notice_detail.setText(detail.content);

		wv_detailcontent.getSettings().setJavaScriptEnabled(true);
		if (!TextUtils.isEmpty(result.content)) {
			wv_detailcontent.setVisibility(View.VISIBLE);
			_btnTextSizeInc.setVisibility(View.VISIBLE);
			_btnTextSizeDec.setVisibility(View.VISIBLE);
			lineview_start.setVisibility(View.VISIBLE);
			lineview_end.setVisibility(View.VISIBLE);
		}
		System.out.println("result.content 这是thml的路径  ===》》。 "+result.getNewContent());
		System.out.println("result.content 这是thml的路径  ===》》。 "+ header + script + result.getNewContent() +"======>>>>>>");
		
		String content = result.getNewContent();
		content = content.replaceAll("&amp;", "");
		content = content.replaceAll("quot;", "\"");
		content = content.replaceAll("lt;", "<");
		content = content.replaceAll("gt;", ">");
		content = content.replaceAll("gt;", ">");
		content = content.replaceAll("nbsp;", " ");
		content = content.replaceAll("ldquo;", "“");
		content = content.replaceAll("rdquo;", "”");
		
		wv_detailcontent.loadDataWithBaseURL(null, header + sinopecScript + content , "text/html", "UTF-8", null);
//		wv_detailcontent.addJavascriptInterface(refreshUrl, "detail");
		
		if (detail.imgUrls != null && detail.imgUrls.size() > 0) {
//			asynloadImage(detail.imgUrls);
		}
		
		wv_detailcontent.setWebViewClient(new WebViewClient(){     
			/*   
			此处能拦截超链接的url,即拦截href请求的内容.   
			*/    
			public boolean shouldOverrideUrlLoading(WebView view, String url) {     
				view.loadUrl(url);   
				downLoadRealPdf(url);
				return true;     
			} 
			
		});
		
		List<Attachment> attachments = detail.attachment_list;
		if (attachments != null && attachments.size() > 0 && attachments.get(0).getPdf() != null && !"".equals(attachments.get(0).getPdf().trim())) {
			IntranetAttachementAdapter adapter = new IntranetAttachementAdapter();
			ll_notice_detail_attachements.setAdapter(adapter);
		} else {
			ll_attachements.setVisibility(View.GONE);
			ll_notice_detail_attachements.setVisibility(View.GONE);
		}
		
		new Thread(){
			public void run() {
				try {
					Thread.sleep(500);
					wv_detailcontent.loadUrl("javascript:DrawImage('"+width+"')");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}.start();
	}

	
	private void setupView(View v) {
		Button bt_left = (Button) v.findViewById(R.id.bt_left);
		bt_left.setVisibility(View.VISIBLE);
		bt_left.setOnClickListener(this);
		lineview_start = v.findViewById(R.id.lineview_start);
		lineview_end = v.findViewById(R.id.lineview_end);

		tv_title_intranet_detail = (TextView) v.findViewById(R.id.tv_title_intranet_detail);
		tv_author_notice_detail = (TextView) v.findViewById(R.id.tv_author_notice_detail);
		tv_timer_notice_detail = (TextView) v.findViewById(R.id.tv_timer_notice_detail);
		ll_attachements = (LinearLayout) v.findViewById(R.id.ll_attachements);
		ll_notice_detail_attachements = (ListView) v.findViewById(R.id.ll_notice_detail_attachements);
		ll_notice_detail_attachements.setOnItemClickListener(this);

		wv_detailcontent = (WebView) v.findViewById(R.id.wv_detailcontent);
		wv_detailcontent.getSettings().setBuiltInZoomControls(true);
		wv_detailcontent.getSettings().setSupportZoom(true);
	}

	String header = "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, user-scalable=no\">";
	String script = "<script type=\"text/javascript\"> function DrawImage(Imgid,imageURL, iwidth,iheight, width)"
			+ "{ var ImgD = document.getElementById(Imgid);document.getElementById(Imgid).src = imageURL; ImgD.setAttribute(\"width\", width );} </script>";
//	String sinopecScript = "<script type=\"text/javascript\"> function DrawImage(iwidth)"
//			+ "{ var myimg,oldwidth; var maxwidth=iwidth; for(i=0;i <document.images.length;i++){myimg = document.images[i];if(myimg.width > maxwidth){oldwidth = myimg.width;myimg.width = maxwidth;myimg.height = myimg.height * (maxwidth/oldwidth);}} } </script>";
	
	String sinopecScript = "<script type=\"text/javascript\"> function DrawImage(iwidth)"
	+ "{ var myimg,oldwidth; var maxwidth=iwidth; for(i=0;i <document.images.length;i++){myimg = document.images[i]; myimg.removeAttribute(\"height\"); if(myimg.width > maxwidth){myimg.setAttribute(\"width\", maxwidth );}} } </script>";
	
//	String sinopecScript = "<script type=\"text/javascript\">function ResizeImages(width) { "+
//                           "var myimg,oldwidth,oldheight;"+
//                           "var maxwidth=500px;"+
//                           "for(i=0;i <document.images.length;i++){" +
//                           "myimg = document.images[i];" +
//                           "if(myimg.width > maxwidth){" +
//                           "oldwidth = myimg.width;" +
//                           "oldheight = myimg.height;" +
//                           "myimg.width = maxwidth;" + 
//                           "myimg.height = oldheight * (maxwidth/oldwidth);" +
//                           "myimg.margin = oldheight * (maxwidth/oldwidth);" +
//                           "}}}</script>";
	
	RefreshImageURL refreshUrl = new RefreshImageURL();

	public final class RefreshImageURL {
		public void replaceImage(String id, String path) {
			wv_detailcontent.loadUrl("javascript:DrawImage()");
		}
	}

	private final static int REPLACEIMAGE = 10;
	protected static final String TAG = "MobileIntranetNewsDetailFragment";
	private Handler asynReplaceImageHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REPLACEIMAGE:
				Bundle bundle = msg.getData();
				String id = bundle.getString("id");
				String path = bundle.getString("path");
				wv_detailcontent.loadUrl("javascript:DrawImage()");
				break;
			default:
				break;
			}
		}
	};

	private void asynloadImage(List<MeetingPdf> imgs) {
		for (int i = 0; i < imgs.size(); i++) {
			final MeetingPdf img = imgs.get(i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String path = img.getValue();
						String id = img.getPdfKey();
//						String requestData = "pdfurl=" + URLEncoder.encode(path) + "&OriginalDoc=1";
//						byte[] data = requestData.getBytes();
//						String urlstr = WebUtils.rootUrl + URLUtils.mobileOfficehtDownload;
						String urlstr = pdfPath + "&prm1=" + URLEncoder.encode(path.trim()) + "&prm2=1";
						if (Constants.DEBUG) {
//							Log.i(TAG, "urlstr:" + urlstr);
						}
						URL url = new URL(urlstr);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.setRequestProperty("Cookie", WebUtils.cookie);
						conn.setRequestProperty("EquipType", "Android");
						conn.setRequestProperty("EquipSN", WebUtils.deviceId);
						conn.setRequestProperty("Soft", WebUtils.packageName);
						conn.connect();
						if (conn.getResponseCode() == 200) {
							InputStream in = conn.getInputStream();
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							byte[] buffer = new byte[1024];
							int len = 0;
							while ((len = in.read(buffer)) != -1) {
								baos.write(buffer, 0, len);
							}
							String realPath = new String(baos.toByteArray());
							if(Constants.DEBUG) {
//								Log.i(TAG, "realPath:" + realPath);
							}
							Message msg = new Message();
							msg.what = REPLACEIMAGE;
							Bundle bundle = new Bundle();
							bundle.putString("id", id);
							bundle.putString("path", realPath);
							msg.setData(bundle);
//							asynReplaceImageHandler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	boolean sizeInc = false;
	boolean sizeDec = false;
	@Override
	public void onClick(View v) {
		WebSettings settings = wv_detailcontent.getSettings();
		if (v.getId() == R.id.bt_left) {
			mContext.onBackPressed();
		} else if (v.getId() == R.id.btn_text_size_inc) {
			settings.setTextSize(TextSize.LARGER);//大
			if(!sizeInc){
				
				_btnTextSizeInc.setBackgroundResource(R.drawable.ic_btn_text_size_inc);
				_btnTextSizeDec.setBackgroundResource(R.drawable.ic_btn_text_size_incs);
				sizeInc = true;
			}
			else {
				_btnTextSizeInc.setBackgroundResource(R.drawable.ic_btn_text_size_inc);
				_btnTextSizeDec.setBackgroundResource(R.drawable.ic_btn_text_size_incs);
				sizeInc = false;
			}
//			_btnTextSizeDec.setBackgroundResource(R.drawable.ic_btn_text_size_dec);
		} else if (v.getId() == R.id.btn_text_size_dec) {
			settings.setTextSize(TextSize.NORMAL);//小
			if(!sizeDec){
				
				_btnTextSizeDec.setBackgroundResource(R.drawable.ic_btn_text_size_dec);
				_btnTextSizeInc.setBackgroundResource(R.drawable.ic_btn_text_size_decs);
				
//				_btnTextSizeInc.setBackgroundResource(R.drawable.ic_btn_text_size_inc);
				sizeDec = true;
			}
			else {
				_btnTextSizeDec.setBackgroundResource(R.drawable.ic_btn_text_size_dec);
				_btnTextSizeInc.setBackgroundResource(R.drawable.ic_btn_text_size_decs);
				sizeDec = false;
			}
//			_btnTextSizeInc.setBackgroundResource(R.drawable.ic_btn_text_size_inc);
		}
	}

	
//	_btnTextSizeInc.setOnClickListener(this);
//	_btnTextSizeDec = v.findViewById(R.id.btn_text_size_dec);
//	_btnTextSizeDec
	private class IntranetAttachementAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return detail.attachment_list.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			LayoutInflater inflater = mContext.getLayoutInflater();
			if (view == null) {
				view = inflater.inflate(R.layout.item_intranet_attachement, null);
			}
			TextView tv_attachement_name = (TextView) view.findViewById(R.id.tv_attachement_name);
			tv_attachement_name.setText(detail.attachment_list.get(position).getKey2());
			return view;
		}
	}

	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

//		downloadPdf(detail.getAttachment_list().get(arg2).getPdf());
//		myWebView.loadUrl(http://www.example.com);
		// downLoadRealPdf(pdfPath);
	}

	protected void downloadPdf(String path) {
		new AsyncTask<String, Void, String>() {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (result != null) {
					// 获取到的pdf地址
					// readPdf(Uri.fromFile(result));"http://adasd.doc" 不支持此格式文件
					System.out.println("解析完成后返回的附件下载地址:" + result);
//					downLoadRealPdf(result);
				} else {
					dismissProgress();
					// 下载失败
					Toast.makeText(mContext, "解析失败，请检查网络", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgress("提示", "正在解析地址...", this);
			}

			@Override
			protected String doInBackground(String... params) {
				String path = params[0];
				System.out.println("一开始传过来的路径:" + path);
				try {

//					return ConnectionManager.getHttpGetPDFUrl(path, pdfPath);//这是走内网的解析地址
					
					return path;//如果是外网直接解析
					
//					String cookie = WebUtils.cookie;
//					String requestData = "&prm1=" + URLEncoder.encode(path) + "&prm2=1";
//					byte[] data = requestData.getBytes();
//					URL url = new URL(pdfPath);
//					System.out.println("请求解析路径的地址："+pdfPath);
//					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setRequestProperty("Cookie", cookie);
//					conn.setRequestProperty("DeviceId", WebUtils.deviceId);
//					conn.setRequestProperty("pdfurl", path);
//					conn.setDoInput(true);
//					conn.setDoOutput(true);
//					conn.getOutputStream().write(data);
//
//					if (conn.getResponseCode() == 200) {
//						InputStream in = conn.getInputStream();
//
//						ByteArrayOutputStream baos = new ByteArrayOutputStream();
//						byte[] buffer = new byte[1024];
//						int len = 0;
//						while ((len = in.read(buffer)) != -1) {
//							baos.write(buffer, 0, len);
//						}
//
//						return new String(baos.toByteArray());
//					} else {
//						return null;
//					}

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute(path);
	}

	boolean isFirst = true;
	private WebView wv_detailcontent;
//	public static String listPath;
	public static String listPathpage;

	/**
	 * 下载具体的pdf的url
	 * @param path
	 */
	protected void downLoadRealPdf(String path) {

		new AsyncTask<String, Integer, File>() {
			private volatile boolean running = true;

			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				dismissProgress();
				if (result != null) {
					String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(result.getAbsolutePath()));
					readPdf(Uri.fromFile(result), type);
				} else {
					// 下载失败
					Toast.makeText(mContext, "下载失败，请检查网络", Toast.LENGTH_SHORT).show();
				}
			}

			protected void onCancelled() {
				running = false;
			}

			@Override
			protected File doInBackground(String... params) {
				String path = params[0];

				try {

					File file = null;
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters, 10*60*1000);
					HttpConnectionParams.setSoTimeout(httpParameters, 10*60*1000);
					DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParameters);
					HttpGet httpGet = new HttpGet(path);
					HttpResponse response = httpClient.execute(httpGet);
					InputStream in = response.getEntity().getContent();
					String fileName = path;
					String extraName = fileName.substring(fileName.lastIndexOf('.') + 1);
					file = new File(Environment.getExternalStorageDirectory(), Constants.home_cache+"/oa/temp." + extraName);

					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.close();

					while (!running) {
						return null;
					}
					return file;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute(path);
	}

	/**
	 * 打开查看pdf
	 * @param uri
	 * @param type
	 */
	private void readPdf(Uri uri, String type) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(/* uri */uri, type);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			// No application to view, ask to download one
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("文件打开失败");
			builder.setMessage("没有找到合适的PDF阅读器，是否到Android Market下载一个？");
			builder.setPositiveButton("立刻访问", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent marketIntent = new Intent(Intent.ACTION_VIEW);
					marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
					startActivity(marketIntent);
				}
			});
			builder.setNegativeButton("暂不访问", null);
			builder.create().show();
		}
	}

	/**
	 * 在进行后台任务，例如下载任务时，使用进度条提示用户进行等待，一般在任务开始时使用，结束时关闭
	 * 
	 * @param title
	 * @param message
	 * @param task
	 */
	protected void showProgress(String title, String message, final AsyncTask<?, ?, ?> task) {
		if (_pd == null) {
			_pd = new ProgressDialog(mContext);
			_pd.setTitle(title);
			_pd.setMessage(message);
			_pd.setCanceledOnTouchOutside(false);
			_pd.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
						task.cancel(true);
						System.out.println("Task[" + task + "] is cancelled");
						_pd = null;
					}
				}
			});
			_pd.show();
		} else {
			_pd.setTitle(title);
			_pd.setMessage(message);
			_pd.show();
		}
	}

	protected void dismissProgress() {
		if (_pd != null && _pd.isShowing()) {
			_pd.cancel();
			_pd = null;
		}
	}
	
	/**
	 * 获取新闻列表路径
	 * @param item detail
	 */
	private void initPathParamsPage(SinopecMenuModule item) {
		// listPath = WebUtils.rootUrl + URLUtils.baseContentUrl
		// + item.menuPages.get(0).id;
		// load tab path
		for (SinopecMenuPage page : item.menuPages) {
			if ("detail".equalsIgnoreCase(page.code)) {
				listPathpage = page.id;
			}
		}
	}
}
