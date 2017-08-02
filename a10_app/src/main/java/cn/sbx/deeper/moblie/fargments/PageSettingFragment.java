package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;


public class PageSettingFragment extends Fragment {

	private Button bt_back;
	private ExpandableListView page_elv;
	private Map<Integer, String> map_group = new HashMap<Integer, String>();
	private Map<Integer, Map<Integer, Integer>> map_child = new HashMap<Integer, Map<Integer, Integer>>();
	private float child_index = 0f;
	private float child_index2 = 0f;
	SharedPreferences sp;
	private List<String> wallpaperList = new ArrayList<String>();
	private List<String> themeList = new ArrayList<String>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		sp = getActivity().getSharedPreferences("sys_config", Context.MODE_PRIVATE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map_group.put(0, "主题");
		map_group.put(1, "壁纸");
		Map<Integer, Integer> map1 = new HashMap<Integer, Integer>();
		map1.put(0, R.drawable.ic_theme_default);
		map1.put(1, R.drawable.ic_theme_red);
		map1.put(2, R.drawable.ic_theme_blue);
		map_child.put(0, map1);
		Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
		map2.put(0, R.drawable.small_00);
		map2.put(1, R.drawable.small_01);
		map2.put(2, R.drawable.small_02);
		map2.put(3, R.drawable.small_03);
		map2.put(4, R.drawable.small_04);
		map2.put(5, R.drawable.small_05);
		map2.put(6, R.drawable.small_06);
		map2.put(7, R.drawable.small_07);
		map2.put(8, R.drawable.small_08);
		map2.put(9, R.drawable.small_09);
		map2.put(10, R.drawable.small_10);
//		map2.put(11, R.drawable.small_10);
		map_child.put(1, map2);

		// 添加壁纸相对应大图的图片名称
		wallpaperList.add("bg_00.jpg");
		wallpaperList.add("bg_01.jpg");
		wallpaperList.add("bg_02.jpg");
		wallpaperList.add("bg_03.jpg");
		wallpaperList.add("bg_04.jpg");
		wallpaperList.add("bg_05.jpg");
		wallpaperList.add("bg_06.jpg");
		wallpaperList.add("bg_07.jpg");
		wallpaperList.add("bg_08.jpg");
		wallpaperList.add("bg_09.jpg");
		wallpaperList.add("bg_10.jpg");
//		wallpaperList.add("bg_11.png");
		
		//添加主题相对应大图的图片名称
		themeList.add("skin");
		themeList.add("skin-red");
		themeList.add("skin-blue");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mobile_office_pagesetting, null);
		bt_back = (Button) view.findViewById(R.id.bt_back);
		bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				getActivity().onBackPressed();
			}
		});

		Float theme = sp.getFloat("use_theme", child_index);
		Float bg = sp.getFloat("use_menu_background", child_index2);
		child_index = theme;
		child_index2 = bg;
		
		if("skin-blue".equals(Constants.skin_folder)) {
			child_index = 1.0f;
		}else if("skin-red".equals(Constants.skin_folder)) {
			child_index = 0.1f;
		}else {
			child_index = 0.0f;
		}
		
		page_elv = (ExpandableListView) view.findViewById(R.id.page_elv);
		page_elv.setGroupIndicator(null);
		page_elv.setAdapter(new MyExpandableListViewAdapter());
		for (int i = 0; i < map_group.size(); i++) {
			page_elv.expandGroup(i);
		}

		return view;
	}

	class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return map_group.size();
		}

		@Override
		public int getChildrenCount(int paramInt) {
			System.out.println(map_child.get(paramInt).size());
			int count = map_child.get(paramInt).size();
			if (count % 2 == 0) {
				return count / 2;
			} else {
				return count / 2 + 1;
			}
		}

		@Override
		public Object getGroup(int paramInt) {
			// TODO Auto-generated method stub
			return map_group.get(paramInt);
		}

		@Override
		public Object getChild(int paramInt1, int paramInt2) {
			// TODO Auto-generated method stub
			return map_child.get(paramInt1).get(paramInt2);
		}

		@Override
		public long getGroupId(int paramInt) {
			// TODO Auto-generated method stub
			return paramInt;
		}

		@Override
		public long getChildId(int paramInt1, int paramInt2) {
			return paramInt2;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(final int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
			// TODO Auto-generated method stub
			View view = paramView;
			if (view == null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.pagesetting_group_item, paramViewGroup, false);
			}
			TextView tv_themes = (TextView) view.findViewById(R.id.tv_themes);
			tv_themes.setText(map_group.get(paramInt));
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View paramView) {
					// TODO Auto-generated method stub
					// page_elv.expandGroup(paramInt);
				}
			});
			return view;
		}

		@Override
		public View getChildView(final int paramInt1, final int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
			// TODO Auto-generated method stub
			View view = paramView;
			if (view == null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.pagesetting_child_item, paramViewGroup, false);
			}
			ImageView iv = (ImageView) view.findViewById(R.id.iv_theme);
			TextView tv = (TextView) view.findViewById(R.id.tv_selected);
			ImageView iv2 = (ImageView) view.findViewById(R.id.iv_theme2);
			TextView tv2 = (TextView) view.findViewById(R.id.tv_selected2);
			iv.setImageResource(map_child.get(paramInt1).get(paramInt2 * 2));
			if (paramInt2 * 2 + 1 < map_child.get(paramInt1).size()) {
				iv2.setImageResource(map_child.get(paramInt1).get(paramInt2 * 2 + 1));
				iv2.setVisibility(View.VISIBLE);
			} else
				iv2.setVisibility(View.INVISIBLE);
			tv.setVisibility(View.INVISIBLE);
			tv2.setVisibility(View.INVISIBLE);
			if (paramInt1 == 0) {
				if (child_index == paramInt2) {
					tv.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.INVISIBLE);
				} else if (child_index > paramInt2 && child_index < paramInt2 + 1) {
					tv.setVisibility(View.INVISIBLE);
					tv2.setVisibility(View.VISIBLE);
				}
			} else {
				if (child_index2 == paramInt2) {
					tv.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.INVISIBLE);
				} else if (child_index2 > paramInt2 && child_index2 < paramInt2 + 1) {
					tv.setVisibility(View.INVISIBLE);
					tv2.setVisibility(View.VISIBLE);
				}
			}
			
			//点击第一张图片
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					// TODO Auto-generated method stub
					if (paramInt1 == 0) {
						child_index = paramInt2;
//						Toast.makeText(getActivity(), themeList.get(paramInt2 * 2), Toast.LENGTH_SHORT).show();
						if((paramInt2 * 2) == 0) {
							Constants.changeSkin = false;
							Constants.skin_folder = "skin";
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_SKIN, false);
							editor.putString(Constants.SKIN_FOLDER, "skin");
							editor.commit();
							showDialogMessage("已选择默认主题",0);
							getActivity().sendBroadcast(new Intent(Constants.MODIFY_APP_MAIN_SKIN));
							Constants.change_current_skin_statu = true;
						}else {
							String skinName = themeList.get(paramInt2 * 2);
							Constants.changeSkin = true;
							Constants.skin_folder = skinName;
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_SKIN, true);
							editor.putString(Constants.SKIN_FOLDER, skinName);
							try {
								String[] strs = getActivity().getAssets().list(skinName);
								if(strs != null && strs.length>0) {
									editor.commit();
									showDialogMessage("已更换主题",0);
									getActivity().sendBroadcast(new Intent(Constants.MODIFY_APP_MAIN_SKIN));
									Constants.change_current_skin_statu = true;
								}else {
									showDialogMessage("没有找到皮肤资源文件",0);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						child_index2 = paramInt2;
//						Toast.makeText(getActivity(), wallpaperList.get(paramInt2 * 2), Toast.LENGTH_SHORT).show();
						if(paramInt2 * 2 == 0) {
							Constants.changeBg = false;
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_MENU_BG, false);
							editor.commit();
							showDialogMessage("已选择默认背景", 1);
						}else {
							String bgName = wallpaperList.get(paramInt2 * 2);
							Constants.changeBg = true;
							Constants.bg_name = bgName;
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_MENU_BG, true);
							editor.putString(Constants.BG_FILENAME, bgName);
							editor.commit();
							showDialogMessage("已更换背景", 1);
						}
					}
					notifyDataSetChanged();
					if (paramInt1 == 0) {
						saveThemeSign("use_theme", child_index);
					} else {
						saveThemeSign("use_menu_background", child_index2);
					}
				}
			});
			
			//点击第二张图片
			iv2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View paramView) {
					// TODO Auto-generated method stub
					if (paramInt1 == 0) {
						child_index = (float) (paramInt2 + 0.5);
//						Toast.makeText(getActivity(), themeList.get(paramInt2 * 2 + 1), Toast.LENGTH_SHORT).show();
						if((paramInt2 * 2 + 1) == 0) {
							Constants.changeSkin = false;
							Constants.skin_folder = "skin";
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_SKIN, false);
							editor.putString(Constants.SKIN_FOLDER, "skin");
							editor.commit();
							showDialogMessage("已选择默认主题", 0);
							getActivity().sendBroadcast(new Intent(Constants.MODIFY_APP_MAIN_SKIN));
							Constants.change_current_skin_statu = true;
						}else {
							String skinName = themeList.get(paramInt2 * 2 + 1);
							Constants.changeSkin = true;
							Constants.skin_folder = skinName;
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_SKIN, true);
							editor.putString(Constants.SKIN_FOLDER, skinName);
							try {
								String[] strs = getActivity().getAssets().list(skinName);
								if(strs != null && strs.length>0) {
									editor.commit();
									showDialogMessage("已更换主题",0);
									getActivity().sendBroadcast(new Intent(Constants.MODIFY_APP_MAIN_SKIN));
									Constants.change_current_skin_statu = true;
								}else {
									showDialogMessage("没有找到皮肤资源文件",0);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						child_index2 = (float) (paramInt2 + 0.5);
//						Toast.makeText(getActivity(), wallpaperList.get(paramInt2 * 2 + 1), Toast.LENGTH_SHORT).show();
						
						if((paramInt2 * 2 + 1) == 0) {
							Constants.changeBg = false;
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_MENU_BG, false);
							editor.commit();
							showDialogMessage("已选择默认背景", 1);
						}else {
							String bgName = wallpaperList.get(paramInt2 * 2 + 1);
							Constants.changeBg = true;
							Constants.bg_name = bgName;
							Editor editor = sp.edit();
							editor.putBoolean(Constants.CHANGE_MENU_BG, true);
							editor.putString(Constants.BG_FILENAME, bgName);
							editor.commit();
							showDialogMessage("已更换背景",1);
						}
					}
					notifyDataSetChanged();
					if (paramInt1 == 0) {
						saveThemeSign("use_theme", child_index);
					} else {
						saveThemeSign("use_menu_background", child_index2);
					}
				}
			});

			return view;
		}

		@Override
		public boolean isChildSelectable(int paramInt1, int paramInt2) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public void showDialogMessage(final String message, final int type) {
		new AsyncTask<Void, Void, Void>() {
			private ProgressHUD overlayProgress;
			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if("0".equals(String.valueOf(type))) {
					overlayProgress = AlertUtils.showDialog(getActivity(), "正在更换主题...", this,
							false);
					
				}else {
					overlayProgress = AlertUtils.showDialog(getActivity(), "正在更换背景...", this,
							false);
				}
				overlayProgress.setCanceledOnTouchOutside(false);
			}

			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
			 */
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (overlayProgress != null)
					overlayProgress.dismiss();
				new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage(message).setPositiveButton("确定", null).show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}
	
	private void saveThemeSign(String type, Float f) {
		Editor editor = sp.edit();
		editor.putFloat(type, f);
		editor.commit();
	}
	
	
	
}
