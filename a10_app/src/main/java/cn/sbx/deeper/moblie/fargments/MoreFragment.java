package cn.sbx.deeper.moblie.fargments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sunboxsoft.monitor.utils.PerfUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.LoginActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.AppAccount;
import cn.sbx.deeper.moblie.domian.TypeItem;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.ClientConfig;
import cn.sbx.deeper.moblie.util.UserInfo;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.util.ZipUtil;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

public class MoreFragment extends BaseFragment implements OnClickListener {

	private CheckBox ck_logout_more;
	private Button ll_about_layout;
	Context mContext;
	private LinearLayout ll_about_modify_password;
	private TextView tv_account_name_more;
	private LinearLayout ll_account;
	private LinearLayout ll_page_setting;
	private LinearLayout ll_account_name_more;
	private LinearLayout ll_clear;
	private List<AppAccount> appAccounts = new ArrayList<AppAccount>();
	private int listViewTouchAction = 0;

	private Button bt_change_skin;
	private Button bt_skin_default;
	private Button bt_genal_skin_source;
	private Button bt_left;
	private RelativeLayout developers_model_layout;// 开发者view

	private Spinner spnr_refresh;
	private SharedPreferences sps;
	private EditText et_update_num;
	private EditText et_speed;
	private String refresh_name[] = { "1s", "2s", "3s", "4s", "5s", "6s", "7s",
			"8s", "9s", "10s" };
	private int refresh_time[] = { 1 * 1000, 2 * 1000, 3 * 1000, 4 * 1000,
			5 * 1000, 6 * 1000, 7 * 1000, 8 * 1000, 9 * 1000, 10 * 1000 };

	private List<TypeItem> typeItems = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sps = getActivity().getSharedPreferences("sys_config",
				Context.MODE_PRIVATE);
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		tv_title.setText("应用设置");
		
		// 获取当前登录用户
		userName_login = PerfUtils.getString(mContext, "userName", "");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	
	// 初始化设置的时间
			String setTime = PerfUtils.getString(mActivity, "clearTime", "");
			if (!TextUtils.isEmpty(setTime)) {
				tv_showtime.setText("清除周期: \n" + setTime);
			}

	
		typeItems = new ArrayList<TypeItem>();
		for (int i = 0; i < refresh_name.length; i++) {// 将类型加入
			TypeItem type = new TypeItem();
			type.name = refresh_name[i];
			type.time = refresh_time[i];
			typeItems.add(type);
		}
		spnr_refresh.setAdapter(new SpinnerAdapter(getActivity(), typeItems));
		if (sps.getInt(Constants.Refresh_Time, refresh_time[0]) == refresh_time[0]) {
			spnr_refresh.setSelection(0);// 初始化
		} else {
			switch (sps.getInt(Constants.Refresh_Time, refresh_time[0])) {
			case 1 * 1000:
				spnr_refresh.setSelection(0);
				break;
			case 2 * 1000:
				spnr_refresh.setSelection(1);
				break;
			case 3 * 1000:
				spnr_refresh.setSelection(2);
				break;
			case 4 * 1000:
				spnr_refresh.setSelection(3);
				break;
			case 5 * 1000:
				spnr_refresh.setSelection(4);
				break;
			case 6 * 1000:
				spnr_refresh.setSelection(5);
				break;
			case 7 * 1000:
				spnr_refresh.setSelection(6);
				break;
			case 8 * 1000:
				spnr_refresh.setSelection(7);
				break;
			case 9 * 1000:
				spnr_refresh.setSelection(8);
				break;
			case 10 * 1000:
				spnr_refresh.setSelection(9);
				break;
			default:
				break;
			}
		}
		spnr_refresh.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				int time = ((TypeItem) parent.getItemAtPosition(position)).time;
				sps.edit().putInt(Constants.Refresh_Time, time).commit();

			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.mobile_office_more, container, false);

		setupView(v);
		useClientConfig();
		return v;
	}

	private void useClientConfig() {
		if (ClientConfig.getInstance().isShowRegarding()) {
			ll_about_layout.setVisibility(View.GONE);
		} else {
			ll_about_layout.setVisibility(View.GONE);
		}
		if (ClientConfig.getInstance().isShowHelp()) {
			ll_help_layout.setVisibility(View.GONE);
		} else {
			ll_help_layout.setVisibility(View.GONE);
		}
	}

	private LinearLayout ll_help_layout;
	private LinearLayout change_pswd;

	private void setupView(View v) {
//		清除任务周期
		tv_showtime = (TextView) v.findViewById(R.id.tv_showtime);
		ll_setclear_time = (LinearLayout) v.findViewById(R.id.ll_setclear_time);
		ll_setclear_time.setOnClickListener(this);

		change_pswd=(LinearLayout)v.findViewById(R.id.change_pswd);
		change_pswd.setOnClickListener(this);
		ll_help_layout = (LinearLayout) v.findViewById(R.id.ll_help_layout);
		ll_help_layout.setOnClickListener(this);
		spnr_refresh = (Spinner) v.findViewById(R.id.spnr_refresh);
		et_update_num = (EditText) v.findViewById(R.id.et_update_num);
		et_update_num.setOnClickListener(this);
		et_update_num.setText(sps.getString(Constants.Update_Num, "10"));
		et_speed = (EditText) v.findViewById(R.id.et_speed);
		et_speed.setOnClickListener(this);
		et_speed.setText(sps.getString(Constants.Speed, "0.8") + "m/s");
		// 开发者view
		developers_model_layout = (RelativeLayout) v
				.findViewById(R.id.developers_model_layout);
		ll_about_modify_password = (LinearLayout) v
				.findViewById(R.id.ll_about_modify_password);
		ll_about_modify_password.setOnClickListener(this);
		tv_account_name_more = (TextView) v
				.findViewById(R.id.tv_account_name_more);
		// tv_account_name_more.setText(UserInfo.getInstance().getUsername() !=
		// null ? UserInfo.getInstance().getUsername() : "无默认帐号");
		ck_logout_more = (CheckBox) v.findViewById(R.id.ck_logout_more);
		ll_account = (LinearLayout) v.findViewById(R.id.ll_account);
		ll_account.setOnClickListener(this);
		ll_clear = (LinearLayout) v.findViewById(R.id.ll_clear);
		ll_clear.setOnClickListener(this);
		ll_about_layout = (Button) v.findViewById(R.id.ll_about_layout);
		ll_about_layout.setOnClickListener(this);
		bt_change_skin = (Button) v.findViewById(R.id.bt_change_skin);
		bt_change_skin.setOnClickListener(this);
		bt_skin_default = (Button) v.findViewById(R.id.bt_skin_default);
		bt_skin_default.setOnClickListener(this);
		bt_genal_skin_source = (Button) v
				.findViewById(R.id.bt_genal_skin_source);
		bt_genal_skin_source.setOnClickListener(this);
		ll_page_setting = (LinearLayout) v.findViewById(R.id.ll_page_setting);
		ll_page_setting.setOnClickListener(this);
		ll_account_name_more = (LinearLayout) v
				.findViewById(R.id.ll_account_name_more);
		ll_account_name_more.setOnClickListener(this);
//		bt_left = (Button) v.findViewById(R.id.bt_left);
//		bt_left.setOnClickListener(this);
//		bt_left.setVisibility(View.VISIBLE);

//		TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
//		tv_title.setText("设置");
		// 判断是不是开发者 ，如果不是开发者的话把开发者的视图隐藏
		String developerMode = sp.getString(UserInfo.getInstance()
				.getUsername() + "developerMode", "");
		if (WebUtils.role.equalsIgnoreCase("1")) {
			developers_model_layout.setVisibility(View.VISIBLE);
			if (developerMode.equals("") || developerMode.equals("1")) {
				ck_logout_more.setChecked(true);
			} else {
				ck_logout_more.setChecked(false);
			}
		} else {
			developers_model_layout.setVisibility(View.GONE);
		}
		ck_logout_more
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						if (isChecked) {
							Toast.makeText(getActivity(), "已启用开发者模式", Toast.LENGTH_SHORT).show();
							editor.putString("developerMode", "1");
							Constants.developerMode = "1";
						} else {
							Toast.makeText(getActivity(), "已关闭开发者模式", Toast.LENGTH_SHORT).show();
							editor.putString("developerMode", "0");
							Constants.developerMode = "0";
						}
						editor.commit();
						Intent intent = new Intent(
								Constants.GET_APP_MENU_REFRESH);
						intent.putExtra("type", "refresh");
						getActivity().sendBroadcast(intent);
						getActivity().finish();

					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_about_layout:
			// AboutFragment aboutFragment = new AboutFragment();
			// ((ActivityInTab) (getActivity())).navigateTo(aboutFragment);

			new Builder(getActivity())
					.setTitle("提示")
					.setMessage("确定注销?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Editor editor = ((ActivityInTab) getActivity()).sp
											.edit();
									editor.putString("username", "");
									editor.putString("password", "");
									editor.putBoolean("remindPassword", false);
									editor.putBoolean("autoLogin", false);
									editor.putBoolean("_cbAutoLogon", false);
									editor.commit();

									getActivity().finish();
									Intent i1 = new Intent(
											Constants.MODIFY_APP_MENU_NUM);
									i1.putExtra("type", "finish");
									getActivity().sendBroadcast(i1);
									Intent i2 = new Intent(getActivity(),
											LoginActivity.class);
									getActivity().startActivity(i2);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();

			break;
//		case R.id.ll_about_modify_password:
//			ModifyLoginPasswordFragment modifyLoginPasswordFragment = new ModifyLoginPasswordFragment();
//			((ActivityInTab) (getActivity()))
//					.navigateTo(modifyLoginPasswordFragment);
//			break;

		case R.id.bt_change_skin:
			new Builder(getActivity())
					.setTitle("提示")
					.setMessage("是否要换肤?")
					.setPositiveButton("sure",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									SharedPreferences sp = getActivity()
											.getSharedPreferences("sys_config",
													Context.MODE_PRIVATE);
									Editor editor = sp.edit();
									editor.putBoolean("change_skin", true);
									editor.commit();
									getActivity()
											.sendBroadcast(
													new Intent(
															Constants.MODIFY_APP_MAIN_SKIN));
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.bt_skin_default:
			new Builder(getActivity())
					.setTitle("提示")
					.setMessage("是否还原换肤?")
					.setPositiveButton("sure",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									SharedPreferences sp = getActivity()
											.getSharedPreferences("sys_config",
													Context.MODE_PRIVATE);
									Editor editor = sp.edit();
									editor.putBoolean("change_skin", false);
									editor.commit();
									getActivity()
											.sendBroadcast(
													new Intent(
															Constants.MODIFY_APP_MAIN_SKIN));
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.bt_genal_skin_source:
			new AsyncTask<Void, Void, Void>() {
				private ProgressHUD overlayProgress;
				private boolean pro = true;

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					if (overlayProgress != null)
						overlayProgress.dismiss();
					File f = getActivity().getDir("skin", Context.MODE_PRIVATE);
					if (pro) {
						if (f.listFiles() != null && f.listFiles().length > 0) {
							new Builder(getActivity())
									.setTitle("提示").setMessage("下载成功!!!")
									.setPositiveButton("确定", null)
									.setNegativeButton("取消", null).show();
						} else {
							new Builder(getActivity())
									.setTitle("提示").setMessage("下载失败!!!")
									.setPositiveButton("确定", null)
									.setNegativeButton("取消", null).show();
						}
					}

				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					overlayProgress = AlertUtils.showDialog(getActivity(),
							null, this, false);
					overlayProgress.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface arg0) {
							// TODO Auto-generated method stub
							pro = false;
						}
					});
				}

				@Override
				protected Void doInBackground(Void... params) {
					ZipUtil zipp = new ZipUtil(2049);
					File f = getActivity().getDir("skin", Context.MODE_PRIVATE);
					zipp.unZip("/sdcard/skin.zip", f.getAbsolutePath());
					return null;
				}
			}.execute();
			break;
		case R.id.ll_account:
			// MoreEditFragment moreEditFragment = new MoreEditFragment();
			// ((ActivityInTab) getActivity()).navigateTo(moreEditFragment);
			AboutFragment aboutFragment = new AboutFragment();
			((ActivityInTab) (getActivity())).navigateTo(aboutFragment);
			break;
		case R.id.ll_page_setting:
			PageSettingFragment fragment = new PageSettingFragment();
			((ActivityInTab) (getActivity())).navigateTo(fragment);
			break;

		case R.id.ll_account_name_more:
			MoreObtainChildAccounts moreObtainChildAccounts = new MoreObtainChildAccounts();
			((ActivityInTab) (getActivity()))
					.navigateTo(moreObtainChildAccounts);
			break;
//		case R.id.bt_left:
//			backPrecious();
//			break;
		case R.id.ll_clear:

			new Builder(getActivity())
					.setTitle("提示")
					.setMessage("是否清除缓存")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									File file = new File("/sdcard/sunboxsoft");
									deleteFiles(file);
									new Builder(getActivity())
											.setTitle("提示")
											.setMessage("清除成功")
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO
															// Auto-generated
															// method stub
															Intent intent = new Intent(
																	Constants.GET_APP_MENU_REFRESH);
															intent.putExtra(
																	"type",
																	"refresh");
															getActivity()
																	.sendBroadcast(
																			intent);
															getActivity()
																	.finish();
														}
													}).show();
								}
							}).setNegativeButton("取消", null).show();
			break;

		case R.id.ll_setclear_time:   //设置清除任务周期
			// 首先判断该用户是否再线登录
//			 PerfUtils.getBoolean(mContext, "isOnline", false);
			boolean isOnline =PerfUtils.getBoolean(mContext,"loginState",false);
			if (isOnline) {

				// 清除上传任务计划
				Builder ad = new Builder(getActivity());
				ad.setTitle("选择清除已上传任务周期");// 设置对话框标题
				// ad.setMessage("操作");//设置对话框内容
				ad.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int i) {

							}
						});
				ad.setNegativeButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int i) {
							}
						});
				items = new String[] { "一   周", "二   周", "三   周", "一   月", };
				ad.setItems(items, new itemListonClick());
				ad.show();// 显示对话框
			}
			 else{
			 Toast.makeText(getActivity(), "请在线登录后操作", Toast.LENGTH_SHORT).show();
			 }

			break;

		case R.id.et_speed:
			setSpeed();//
			break;
		case R.id.et_update_num:
			setUpdateNum();//
			break;
		case R.id.change_pswd:
			boolean aBoolean = PerfUtils.getBoolean(getActivity(), "isOnline", false);
			if(aBoolean){
				ChangeDatabasePWD changeDatabasePWD = new ChangeDatabasePWD();
				((ActivityInTab) (getActivity())).navigateTo(changeDatabasePWD);
			}else{
				Toast.makeText(getActivity(),"修改密码只能在线修改",Toast.LENGTH_SHORT).show();
			}




			break;
		default:
			break;
		}
	}

//	// 保存该用户设置的时间周期
//	class itemListonClick implements
//			android.content.DialogInterface.OnClickListener {
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			switch (which) {
//			case 0:
//				PerfUtils.putString(mActivity, userName_login + "clearTime",
//						items[0]);
//				break;
//			case 1:
//				PerfUtils.putString(mActivity, userName_login + "clearTime",
//						items[1]);
//				break;
//			case 2:
//				PerfUtils.putString(mActivity, userName_login + "clearTime",
//						items[2]);
//				break;
//			case 3:
//				PerfUtils.putString(mActivity, userName_login + "clearTime",
//						items[3]);
//				break;
//			}
//		}
//	}
//	
	// 保存该用户设置的时间周期
		class itemListonClick implements
				DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					PerfUtils.putString(mActivity, "clearTime", items[0]);
					tv_showtime.setText("清除周期: \n" + items[0]);

					break;
				case 1:
					PerfUtils.putString(mActivity, "clearTime", items[1]);
					tv_showtime.setText("清除周期: \n" + items[1]);
					break;
				case 2:
					PerfUtils.putString(mActivity, "clearTime", items[2]);
					tv_showtime.setText("清除周期: \n" + items[2]);
					break;
				case 3:
					PerfUtils.putString(mActivity, "clearTime", items[3]);
					tv_showtime.setText("清除周期: \n" + items[3]);
					break;
				}
			}
		}
	

	// 删除文件夹下面的文件
	public static void deleteFiles(File file) {
		try {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] fileChilds = file.listFiles();
				for (int i = 0; i < fileChilds.length; i++) {
					deleteFiles(fileChilds[i]);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}

	}

	private File cacheDir;

	public void FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"sunboxsoft");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

	private void setUpdateNum() {
		// 用户名为空
		LayoutInflater factory = LayoutInflater.from(getActivity());// 提示框
		final View view = factory.inflate(R.layout.editbox_layout, null);// 这里必须是final的
		final EditText et_name = (EditText) view.findViewById(R.id.editText1);// 获得输入框对象
		et_name.setHint("请输入上传坐标累计个数");
		// et_name.setHint("设置域名");// 输入框默认值
		dialog = new Builder(getActivity())
				.setTitle("设置上传坐标累计个数")
				.setView(view)
				.setCancelable(false)
				.setPositiveButton("确定",// 提示框的两个按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 事件处理
								if ("".equals(et_name.getText().toString())) {
									Toast.makeText(getActivity(), "数据不能为空！", Toast.LENGTH_SHORT)
											.show();
									return;
								} else {
									String regExp = "^[0-9]*$";// ^[0-9]*$
									Pattern p = Pattern.compile(regExp);
									Matcher m = p.matcher(et_name.getText()
											.toString());
									if (!m.find()) {
										Toast.makeText(getActivity(),
												"格式有误，请重新输入", Toast.LENGTH_SHORT).show();
										return;
									} else {
										sps.edit()
												.putString(
														Constants.Update_Num,
														et_name.getText()
																.toString())
												.commit();
										et_name.setInputType(InputType.TYPE_CLASS_NUMBER);// 设置进入的时候显示为number模式
										et_update_num.setText(et_name.getText()
												.toString());
										System.out.println("Update_Num=> "
												+ et_name.getText().toString());
									}
								}
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						dialog.cancel();
					}
				}).create();
		dialog.show();
	}

	class SpinnerAdapter extends BaseAdapter {
		private List<TypeItem> items;
		private Context mContext;

		public SpinnerAdapter(Context mcontext, List<TypeItem> list) {
			this.mContext = mcontext;
			this.items = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int paramInt) {
			// TODO Auto-generated method stub
			return items.get(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			// TODO Auto-generated method stub
			return paramInt;
		}

		@Override
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			TypeItem item = items.get(paramInt);
			LayoutInflater inflater = LayoutInflater.from(mContext);
			if (paramView == null) {
				paramView = inflater
						.inflate(R.layout.documents_type_item, null);
			}
			TextView textView2 = (TextView) paramView
					.findViewById(R.id.textView2);
			textView2.setText(item.name);
			return paramView;
		}
	}

	AlertDialog dialog;
	private TextView set_clear_updata_schedInfo;
	private String userName_login;
	private String[] items;
	private TextView tv_showtime;
	private LinearLayout ll_setclear_time;

	private void setSpeed() {
		// 用户名为空
		LayoutInflater factory = LayoutInflater.from(getActivity());// 提示框
		final View view = factory.inflate(R.layout.editbox_layout, null);// 这里必须是final的
		final EditText et_name = (EditText) view.findViewById(R.id.editText1);// 获得输入框对象
		et_name.setHint("请输入行驶速度(单位：m/s)");
		// et_name.setHint("设置域名");// 输入框默认值
		dialog = new Builder(getActivity())
				.setTitle("设置行驶速度")
				.setView(view)
				.setCancelable(false)
				.setPositiveButton("确定",// 提示框的两个按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 事件处理
								if ("".equals(et_name.getText().toString())) {
									Toast.makeText(getActivity(), "数据不能为空！", Toast.LENGTH_SHORT)
											.show();
									return;
								} else {
									String regExp = "^[0-9]+(\\.[0-9]+)?$";
									Pattern p = Pattern.compile(regExp);
									Matcher m = p.matcher(et_name.getText()
											.toString());
									if (!m.find()) {
										Toast.makeText(getActivity(),
												"格式有误，请重新输入", Toast.LENGTH_SHORT).show();
										return;
									} else {
										sps.edit()
												.putString(
														Constants.Speed,
														et_name.getText()
																.toString())
												.commit();
										et_name.setInputType(InputType.TYPE_CLASS_NUMBER);// 设置进入的时候显示为number模式
										et_speed.setText(et_name.getText()
												.toString() + "m/s");
										System.out.println("speed=> "
												+ et_name.getText().toString());
									}
								}
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						dialog.cancel();
					}
				}).create();
		dialog.show();
	}
}
