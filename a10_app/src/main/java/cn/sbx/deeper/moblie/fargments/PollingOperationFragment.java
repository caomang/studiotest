package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.activity.SinopecAllMenuActivity;
import cn.sbx.deeper.moblie.audio.SoundMeter;
import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.domian.ResultInfo;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DateUtils;
import cn.sbx.deeper.moblie.util.ImageUtils;
import cn.sbx.deeper.moblie.util.UploadFiles;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 这里和单独上传图片是有区别的 单独上传时的图片的数量要多一个，需要处理下。
 * 
 * 
 * 
 */
@SuppressLint("ValidFragment")
public class PollingOperationFragment extends BaseFragment implements
		OnClickListener {
	Builder attDialog;
	static Button btn_take_photo, btn_take_audio, btn_take_scanning;
	EditText polling_edt_bz, polling_edt_ma;
	static final int CAMERA_RESULT = 0x01;
	static final int CHOOSE_IMAGE = 0x99;
	String imagePath = null;
	Uri imageURI;
	List<PhotoAudio> imgList = new ArrayList<PhotoAudio>();
	private GridView imageGrid;
	private GridAdapter adapter;
	private Cursor cursor;
	private static final int MAX_PICS = 5;
	Button btn_submit;
	private SharedPreferences sp;
	public String taskId;
	public String currNote;
	public String displayDevice;
	public String deviceResult;
	private IApproveBackToList backToList;
	private int targetContainer;
	private Button btn_next;
	private LinearLayout scan_deviceid;// 扫码的layout
	private LinearLayout complete_state;// 完成情况
	private LinearLayout note_bz;// 备注
	/*
	 * -------------------
	 */
	private Button mBtnSend;
	// private TextView mBtnRcd;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private RelativeLayout mBottom;
	private ListView mListView;

	private boolean isShosrt = false;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
			voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private View rcChat_popup;
	private LinearLayout del_re;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = false;
	private int flag = 1;
	private Handler mHandler = new Handler();
	private String voiceName;
	private long startVoiceT, endVoiceT;
	private TextView device_time;
	private Handler mHandlerTiem = new Handler();// 全局handler
	int time = 0;// 时间差
	TextView tv_title;// title
	private String device_no;// 设备码
	private Spinner spinner;// 设备状态
	private String spinnerString;//
	private String mark_tz = "2";// 标示从列表（1）跳转过来的还是从详情（2）跳转过来的

	public PollingOperationFragment() {

	}

	public PollingOperationFragment(IApproveBackToList backToList,
			int targetContainer) {
		this.backToList = backToList;
		this.targetContainer = targetContainer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getActivity().getSharedPreferences("sys_config",
				Context.MODE_PRIVATE);
		Bundle bundle = getArguments();
		taskId = bundle.getString("taskId");
		currNote = bundle.getString("currNote");
		displayDevice = bundle.getString("displayDevice");
		deviceResult = bundle.getString("deviceResult");
		device_no = bundle.getString("device_no");
		mark_tz = bundle.getString("mark_tz");
		tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText(currNote);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.layout_polling_3, container, false);
		device_time = (TextView) v.findViewById(R.id.device_time);
		time = DateUtils.getAlarmTiqian("120");// 获取时间差
		new Thread(new TimeCount()).start();// 开启线程
		// 扫码layout
		scan_deviceid = (LinearLayout) v.findViewById(R.id.scan_deviceid);
		complete_state = (LinearLayout) v.findViewById(R.id.complete_state);
		note_bz = (LinearLayout) v.findViewById(R.id.note_bz);
		btn_take_scanning = (Button) v.findViewById(R.id.btn_take_scanning);
		btn_take_scanning.setOnClickListener(this);
		scan_deviceid = (LinearLayout) v.findViewById(R.id.scan_deviceid);
		polling_edt_bz = (EditText) v.findViewById(R.id.polling_edt);
		polling_edt_ma = (EditText) v.findViewById(R.id.polling_edt_ma);
		if ("1".equals(displayDevice)) {
			scan_deviceid.setVisibility(View.VISIBLE);
			complete_state.setVisibility(View.VISIBLE);
			note_bz.setVisibility(View.VISIBLE);
			if (!"".equals(device_no)) {
				polling_edt_ma.setText(device_no);
			}
		} else {
			scan_deviceid.setVisibility(View.GONE);
			complete_state.setVisibility(View.GONE);
			note_bz.setVisibility(View.GONE);
		}
		spinner = (Spinner) v.findViewById(R.id.spinner);

		final String[] options = deviceResult.split(";");
		// options[0] = "养护状态良好";
		// options[1] = "发现缺陷并克服";
		// options[2] = "发现缺陷并上报";
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(R.layout.spinner_text_layout);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);
		spinnerString = options[0];
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spinnerString = options[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		btn_take_photo = (Button) v.findViewById(R.id.btn_take_photo);
		btn_take_photo.setOnClickListener(this);
		btn_take_audio = (Button) v.findViewById(R.id.btn_take_audio);
		btn_take_audio.setClickable(false);
		btn_take_audio.setEnabled(false);
		// btn_take_audio.setOnTouchListener(new OnTouchListener() {
		//
		// public boolean onTouch(View v, MotionEvent event) {
		// // 按下语音录制按钮时返回false执行父类OnTouch
		//
		// System.out.println("1");
		// int[] location = new int[2];
		// // mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
		// int btn_rc_Y = location[1];
		// int btn_rc_X = location[0];
		// int[] del_location = new int[2];
		// del_re.getLocationInWindow(del_location);
		// int del_Y = del_location[1];
		// int del_x = del_location[0];
		// if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
		// if (!Environment.getExternalStorageDirectory().exists()) {
		// Toast.makeText(getActivity(), "No SDCard",
		// Toast.LENGTH_LONG).show();
		// return false;
		// }
		// System.out.println("2");
		// if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {//
		// 判断手势按下的位置是否是语音录制按钮的范围内
		// System.out.println("3");
		// btn_take_audio
		// .setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
		// rcChat_popup.setVisibility(View.VISIBLE);
		// voice_rcd_hint_loading.setVisibility(View.VISIBLE);
		// voice_rcd_hint_rcding.setVisibility(View.GONE);
		// voice_rcd_hint_tooshort.setVisibility(View.GONE);
		// mHandler.postDelayed(new Runnable() {
		// public void run() {
		// if (!isShosrt) {
		// voice_rcd_hint_loading
		// .setVisibility(View.GONE);
		// voice_rcd_hint_rcding
		// .setVisibility(View.VISIBLE);
		// }
		// }
		// }, 300);
		// img1.setVisibility(View.VISIBLE);
		// del_re.setVisibility(View.GONE);
		// startVoiceT = System.currentTimeMillis();
		// voiceName = startVoiceT + ".amr";
		// start(voiceName);
		// flag = 2;
		// }
		// } else if (event.getAction() == MotionEvent.ACTION_UP
		// && flag == 2) {// 松开手势时执行录制完成
		// System.out.println("4");
		// btn_take_audio.setBackgroundResource(R.drawable.anniu2);
		// if (event.getY() >= del_Y
		// && event.getY() <= del_Y + del_re.getHeight()
		// && event.getX() >= del_x
		// && event.getX() <= del_x + del_re.getWidth()) {
		// rcChat_popup.setVisibility(View.GONE);
		// img1.setVisibility(View.VISIBLE);
		// del_re.setVisibility(View.GONE);
		// stop();
		// flag = 1;
		// File file = new File(android.os.Environment
		// .getExternalStorageDirectory()
		// + "/"
		// + voiceName);
		// if (file.exists()) {
		// file.delete();
		// }
		// } else {
		// voice_rcd_hint_rcding.setVisibility(View.GONE);
		// stop();
		// endVoiceT = System.currentTimeMillis();
		// System.out
		// .println(endVoiceT + "========" + startVoiceT);
		// flag = 1;
		// int time = (int) ((endVoiceT - startVoiceT) / 1000);
		// if (time < 1) {
		// isShosrt = true;
		// voice_rcd_hint_loading.setVisibility(View.GONE);
		// voice_rcd_hint_rcding.setVisibility(View.GONE);
		// voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
		// mHandler.postDelayed(new Runnable() {
		// public void run() {
		// voice_rcd_hint_tooshort
		// .setVisibility(View.GONE);
		// rcChat_popup.setVisibility(View.GONE);
		// isShosrt = false;
		// }
		// }, 500);
		// return false;
		// }
		// PhotoAudio photoAudio = new PhotoAudio();
		// photoAudio.setUrl(voiceName);
		// photoAudio.setTimeLength(time + "");
		// imgList.add(photoAudio);
		// adapter.notifyDataSetChanged();
		// }
		// }
		// if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
		// System.out.println("5");
		// Animation mLitteAnimation = AnimationUtils.loadAnimation(
		// getActivity(), R.anim.cancel_rc);
		// Animation mBigAnimation = AnimationUtils.loadAnimation(
		// getActivity(), R.anim.cancel_rc2);
		// img1.setVisibility(View.GONE);
		// del_re.setVisibility(View.VISIBLE);
		// del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
		// if (event.getY() >= del_Y
		// && event.getY() <= del_Y + del_re.getHeight()
		// && event.getX() >= del_x
		// && event.getX() <= del_x + del_re.getWidth()) {
		// del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
		// sc_img1.startAnimation(mLitteAnimation);
		// sc_img1.startAnimation(mBigAnimation);
		// }
		// } else {
		//
		// img1.setVisibility(View.VISIBLE);
		// del_re.setVisibility(View.GONE);
		// del_re.setBackgroundResource(0);
		// }
		//
		// return true;
		// }
		// });
		btn_submit = (Button) v.findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		imageGrid = (GridView) v.findViewById(R.id.GridView01);
		btn_next = (Button) getActivity().findViewById(R.id.btn_next);
		btn_next.setText("下一步");
		btn_next.setOnClickListener(this);
		btn_next.setVisibility(View.VISIBLE);
		// changebtn(Constants.Change_Num);
		initView();
		volume = (ImageView) v.findViewById(R.id.volume);
		rcChat_popup = v.findViewById(R.id.rcChat_popup);
		img1 = (ImageView) v.findViewById(R.id.img1);
		sc_img1 = (ImageView) v.findViewById(R.id.sc_img1);
		del_re = (LinearLayout) v.findViewById(R.id.del_re);
		voice_rcd_hint_rcding = (LinearLayout) v
				.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) v
				.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) v
				.findViewById(R.id.voice_rcd_hint_tooshort);
		mSensor = new SoundMeter();
		return v;
	}

	private void initView() {
		adapter = new GridAdapter(getActivity(), MAX_PICS);
		imageGrid.setAdapter(adapter);
		// imageGrid.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// ImageView imageView = (ImageView) view;
		// if (!imageView.getTag().toString().contains(".amr")) {
		// if ((position != adapter.getCount() - 1)
		// || (adapter.isReachMax() && position == adapter
		// .getCount() - 1)) {
		// // Toast.makeText(Polling_3.this,
		// // "跳转图片放大！位置是：" + position, 5).show();
		// adapter.removePic(position);
		// return;
		// } else {
		// if (adapter.isReachMax()) {
		// Toast.makeText(getActivity(), "最多上传9张图片！", 5)
		// .show();
		// return;
		// }
		// // attDialog.show();
		// adapter.removePic(position);
		// }
		// } else {
		// playMusic(imageView.getTag().toString());
		// }
		//
		// }
		// });

	}

	// private void changebtn(String changenum) {
	// // TODO Auto-generated method stub
	// if (changenum != null) {
	// if (changenum.equals("1")) {
	// btn_next.setText("登记");
	// btn_next.setVisibility(View.VISIBLE);
	// } else if (changenum.equals("2")) {
	// btn_next.setText("工单执行");
	// btn_next.setVisibility(View.VISIBLE);
	// } else if (changenum.equals("3")) {
	// btn_next.setText("销记");
	// btn_next.setVisibility(View.VISIBLE);
	// } else if (changenum.equals("0")) {
	// btn_next.setVisibility(View.GONE);
	// }
	//
	// } else {
	// btn_next.setVisibility(View.GONE);
	// }
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_take_photo:
			// attDialog = new
			// AlertDialog.Builder(PollingDetailActivity.this).setTitle(
			// "添加附件").setItems(new String[] { "从图库中获取", "启动照相机拍摄" },
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// switch (which) {
			// case 0: // 打开图库，选择照片
			// // choose an image from MediaStore
			// Intent itnt = new Intent(
			// Intent.ACTION_PICK,
			// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// startActivityForResult(itnt, CHOOSE_IMAGE);
			// break;
			// case 1: // 启动照相机，并拍摄，存储图片，保存URI
			// //TODO 做好外存储设备状态的检查
			if (adapter != null) {

				if (adapter.getCount() == MAX_PICS) {
					Toast.makeText(getActivity(), "最多可上传" + MAX_PICS + "张照片", Toast.LENGTH_SHORT)
							.show();
				} else {
					imagePath = Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ "/sunboxsoft/polling/cmd_att_"
							+ System.currentTimeMillis() + ".jpg";
					File imageFile = new File(imagePath);
					if (!imageFile.getParentFile().exists())
						imageFile.getParentFile().mkdirs();
					imageURI = Uri.fromFile(imageFile);
					Intent i = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					i.putExtra(MediaStore.EXTRA_OUTPUT,
							imageURI);
					startActivityForResult(i, CAMERA_RESULT);
				}
			}
			//
			// }
			// }
			// });
			// attDialog.show();
			break;
		case R.id.btn_next:
			// if (Constants.Change_Num.equals("2")) {
			// 如果扫码layout是显示的 则应该输入设备码
			if (scan_deviceid.getVisibility() == View.VISIBLE) {
				if (polling_edt_ma.getText().toString() != null
						&& !polling_edt_ma.getText().toString().equals("")) {
					new loadTabs().execute();
				} else {
					Toast.makeText(mActivity, "请填写设备码", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				new loadTabs().execute();
			}

			// } else {
			// new loadTabs().execute();
			// }

			break;

		case R.id.btn_take_scanning:
			Toast.makeText(getActivity(), "暂不支持扫描，请手动输入", Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void refreshButtonAndText() {
		// TODO Auto-generated method stub
		super.refreshButtonAndText();
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Bitmap bm = null;
		PhotoAudio photoAudio = null;
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CHOOSE_IMAGE:
				imageURI = data.getData();
				imagePath = getImageRealPath(imageURI);
				photoAudio = new PhotoAudio();
				photoAudio.setUrl(imagePath);
				imgList.add(photoAudio);
				resetThumbnailGrid();
				String outPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/sunboxsoft/polling/cmd_att_"
						+ System.currentTimeMillis() + ".jpg";
				File tfile = new File(outPath);
				if (!tfile.getParentFile().exists())
					tfile.getParentFile().mkdirs();
				System.out.println(imagePath);
				ImageUtils.shrinkFromFile(imagePath, outPath, 480, 800);
				break;
			case CAMERA_RESULT: // 从照相机获取，显示缩略图，并添加路径到列表中
				String outPath1 = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/sunboxsoft/polling/cmd_att_"
						+ System.currentTimeMillis() + ".jpg";
				Bitmap bitmap = getimage(imagePath);
				System.out.println(bitmap.getByteCount());
				storeImageToSDCARD(bitmap, outPath1);
				photoAudio = new PhotoAudio();
				photoAudio.setUrl(outPath1);
				imgList.add(photoAudio);
				resetThumbnailGrid();
				// System.out.println(imagePath);
				// File tfile1 = new File(outPath1);
				// if (!tfile1.getParentFile().exists())
				// tfile1.getParentFile().mkdirs();
				// ImageUtils.shrinkFromFile(imagePath, imagePath, 400, 800);
				break;

			}

		}
	}

	protected String getImageRealPath(Uri imageURI) {
		String[] proj = { MediaStore.Images.Media.DATA };
		cursor = getActivity().managedQuery(imageURI, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private ViewHolder holder;

	static class ViewHolder {
		ImageView imageView;
		ImageView audio_play;
		TextView time_len;
	}

	class GridAdapter extends BaseAdapter {

		private Context mContext;
		private int maxPic;
		private boolean reachMax = false;

		public GridAdapter(Context mContext) {
			this.mContext = mContext;
		}

		public GridAdapter(Context mContext, int maxPic) {
			this(mContext);
			this.maxPic = maxPic;

			// addButton = BitmapFactory.decodeResource(
			// ((Polling_3) mContext).getResources(),
			// R.drawable.smiley_add_btn_pressed);
			//
		}

		public boolean isReachMax() {
			return reachMax;
		}

		public int getMaxPic() {
			return maxPic;
		}

		public void setMaxPic(int maxPic) {
			this.maxPic = maxPic;
		}

		@Override
		public int getCount() {
			return imgList.size();
		}

		@Override
		public Object getItem(int idx) {
			return imgList.get(idx);
		}

		@Override
		public long getItemId(int idx) {
			return idx;
		}

		@Override
		public View getView(final int idx, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = ((LayoutInflater) (getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
						.inflate(R.layout.polling_photo_item, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.polling_photo_iv);
				holder.audio_play = (ImageView) convertView
						.findViewById(R.id.polling_audio_paly);
				holder.time_len = (TextView) convertView
						.findViewById(R.id.time_len);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// ImageView iv = null;
			Bitmap btp = null;
			// if (convertView != null) {
			// iv = (ImageView) convertView;
			//
			// } else {
			// iv = new ImageView(mContext);
			// }
			// InputStream is;
			try {
				// is = new FileInputStream(imgList.get(idx));
				if (imgList.get(idx).getUrl().toString().contains(".amr")) {
					Resources res = getResources();
					Bitmap bmp = BitmapFactory.decodeResource(res,
							R.drawable.voice_rcd_btn_disable);
					holder.audio_play.setVisibility(View.VISIBLE);
					holder.time_len.setVisibility(View.VISIBLE);
					holder.time_len.setText(imgList.get(idx).getTimeLength()
							+ "\"");
					holder.imageView.setBackgroundColor(getResources()
							.getColor(R.color.list_top));
				} else {
					btp = ImageUtils.getBitmapImage(imgList.get(idx).getUrl());
					Drawable drawable = new BitmapDrawable(btp);
					holder.audio_play.setVisibility(View.GONE);
					holder.time_len.setVisibility(View.GONE);
					holder.imageView.setBackgroundDrawable(drawable);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (imgList.get(idx).getUrl().contains(".amr")) {
						playMusic(imgList.get(idx).getUrl().toString());
					}

				}
			});
			// 先判断是否已经回收

			// if(btp != null && !btp.isRecycled()){
			// // 回收并且置为null
			// btp.recycle();
			// btp = null;
			//
			// }
			// System.gc();
			return convertView;
		}

		private boolean shouldReplaceLastItem() {
			return getCount() == this.maxPic;
		}

		public void removePic(int idx) {
			imgList.remove(idx);
			if (idx + 1 == this.maxPic) {
				reachMax = false;
			} else if ((getCount() + 1 == this.maxPic) && isReachMax()) {
				reachMax = false;
			}
			notifyDataSetChanged();
		}

	}

	protected void resetThumbnailGrid() {
		// if (smsg.attachments.size() > 0) {
		// imageGrid.setVisibility(View.VISIBLE);
		// } else {
		// imageGrid.setVisibility(View.GONE);
		// }
		adapter.notifyDataSetChanged();

	}

	class loadTabs extends AsyncTask<Void, Void, ResultInfo> {
		private ProgressHUD overlayProgress;
		final MobileApplication application = (MobileApplication) getActivity()
				.getApplication();

		@Override
		protected ResultInfo doInBackground(Void... params) {
			// TODO Auto-generated method stub0
			// 本地模拟数据
			try {
				System.out.println("提交图片:========"
						+ SinopecAllMenuActivity.locInfo);
				ResultInfo info = null;
				Map<String, String> mapHeader = new HashMap<String, String>();
				mapHeader.put("status", "2");// 0 开始，1，结束，2运动中
				// MAPHEADER.PUT("BATCHID", SP.GETSTRING("BATCHID", ""));
				// mapHeader.put("RouteCode", application.routecode);
				Map<String, String> mapParams = new HashMap<String, String>();
				mapParams.put("Coordinate", SinopecAllMenuActivity.locInfo);
				mapParams.put("UserId", sp.getString("username", ""));
				mapParams.put("requestid", taskId);
				mapParams
						.put("deviceCode", polling_edt_ma.getText().toString());
				mapParams.put("TerminalCode", WebUtils.deviceId);
				mapParams.put("currNode", currNote);
				mapParams.put("deviceResult", spinnerString);
				mapParams.put("remark", polling_edt_bz.getText().toString());
				// "http://42.96.173.8:81/InspectionInterface/Inspection/SubmitWorkflow.aspx"
				info = UploadFiles.postFile(WebUtils.submitUrl// WebUtils.submitUrl+
																// URLUtils.gpsInterface
						, mapHeader, mapParams, "Img", // 表单文件字段名称
						imgList);
				return info;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (overlayProgress != null) {
				overlayProgress.dismiss();
			}
			if (result != null) {
				if ("1".equals(result.key)) {
					if (result.currNode.equals("")
							|| !currNote.equals(result.currNode)) {
						Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
						if (mark_tz.equals("1")) {
							approveRouteSuccess1(targetContainer, backToList);
						} else {
							approveRouteSuccess(targetContainer, backToList);
						}

					} else {
						dialogUpdate(getActivity(), "", result);
						tv_title.setText(result.currNode);
					}
				} else if ("2".equals(result.key)) {
					Toast.makeText(getActivity(), "未获取到位置信息,请检查GPS！", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "上传失败:" + result.message, Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
				System.out.println("error");
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			overlayProgress = AlertUtils.showDialog(getActivity(), null, this,
					false);
		}
	}

	private Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = comp(BitmapFactory.decodeFile(srcPath, newOpts));
		// return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		System.out.println("baos.toByteArray().length:"
				+ baos.toByteArray().length);
		while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			System.out.println("11baos.toByteArray().length:"
					+ baos.toByteArray().length);
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
	}

	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			System.out.println("baos.toByteArray().length:"
					+ baos.toByteArray().length);
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	private Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public void storeImageToSDCARD(Bitmap colorImage, String path) {
		File imagefile = new File(path);
		if (!imagefile.exists()) {
			try {
				imagefile.createNewFile();
				FileOutputStream fos = new FileOutputStream(imagefile);
				colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {// 图片所在SD卡的路径
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, 480, 800);// 自定义一个宽和高
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);// 压缩好比例大小后再进行质量压缩
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;// 获取图片的高
		final int width = options.outWidth;// 获取图片的框
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;// 求出缩放值
	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	private MediaPlayer mMediaPlayer = new MediaPlayer();

	/**
	 * @Description
	 * @param name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(Environment
					.getExternalStorageDirectory() + "/" + name);
			mMediaPlayer.prepare();
			// File file = new File(name);
			// FileInputStream fis = new FileInputStream(file);
			// mMediaPlayer.setDataSource(fis.getFD());
			// mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 定义按钮的响应,覆写系统的默认处理
	class ButtonHandler extends Handler {

		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						mDialog.get(), msg.what);
				break;
			}
		}
	}

	class MHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				softUpdateDialog.show();// 显示对话框
				break;
			}
			super.handleMessage(msg);
		}

	}

	AlertDialog softUpdateDialog;

	private void dialogUpdate(final Context mContext, String content,
			final ResultInfo result) {
		// 如果本地已经下载完了包
		LayoutInflater factoryHis = LayoutInflater.from(getActivity());// 提示框
		View viewDialog = null;
		viewDialog = factoryHis.inflate(R.layout.operation_dialog_layout, null);
		softUpdateDialog = new Builder(mContext)
				.setView(viewDialog).setCancelable(false).create();
		softUpdateDialog.setView(viewDialog, 0, 0, 0, 0);
		Button btn_dialog_ok = (Button) viewDialog
				.findViewById(R.id.btn_dialog_ok);
		TextView textView = (TextView) viewDialog.findViewById(R.id.content);
		// textView.setText("");
		btn_dialog_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgList.clear();
				polling_edt_bz.setText("");
				polling_edt_ma.setText("");
				adapter.notifyDataSetChanged();
				// 如果currnode为空的话 证明这个单据处理完成了
				if (result.currNode.equals("")) {
					// btn_next.setVisibility(View.GONE);
					approveRouteSuccess(targetContainer, backToList);
				} else {
					if (result.displayDevice.equals("1")) {
						scan_deviceid.setVisibility(View.VISIBLE);
						complete_state.setVisibility(View.VISIBLE);
						note_bz.setVisibility(View.VISIBLE);
					} else {
						scan_deviceid.setVisibility(View.GONE);
						complete_state.setVisibility(View.GONE);
						note_bz.setVisibility(View.GONE);
					}

				}
				// Toast.makeText(getActivity(), "确定", 1).show();
				btn_next.setVisibility(View.VISIBLE);
				softUpdateDialog.dismiss();
			}
		});
		Button btn_dialog_cancel = (Button) viewDialog
				.findViewById(R.id.btn_dialog_cancel);
		btn_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mark_tz.equals("1")) {
					approveRouteSuccess1(targetContainer, backToList);
				} else {
					approveRouteSuccess(targetContainer, backToList);
				}
				softUpdateDialog.dismiss();
			}
		});
		ButtonHandler buttonHandler = new ButtonHandler(softUpdateDialog);
		// 设定对话框的处理Handler
		try {
			Field field = softUpdateDialog.getClass()
					.getDeclaredField("mAlert");
			field.setAccessible(true);
			// 获得mAlert变量的值
			Object obj = field.get(softUpdateDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// 修改mHandler变量的值，使用新的ButtonHandler类
			field.set(obj, buttonHandler);
		} catch (Exception e) {
		}
		// 显示对话框
		Message msg = new Message();
		msg.what = 0;
		msg.setTarget(new MHandler());
		msg.sendToTarget();
	}

	class TimeCount implements Runnable {
		@Override
		public void run() {
			while (time > 0)// 整个倒计时执行的循环
			{
				time--;
				mHandlerTiem.post(new Runnable() // 通过它在UI主线程中修改显示的剩余时间
						{
							public void run() {
								device_time.setText(DateUtils.getInterval(time));// 显示剩余时间
							}
						});
				try {
					Thread.sleep(1000);// 线程休眠一秒钟 这个就是倒计时的间隔时间
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// 下面是倒计时结束逻辑
			mHandlerTiem.post(new Runnable() {
				@Override
				public void run() {
					device_time.setText("设定的时间到。");
				}
			});
		}
	}

	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 拍照键的键值
		if (keyCode == 27) {
			if (btn_take_photo != null) {
				btn_take_photo.performClick();
			}
		}
		return true;
	}

}
