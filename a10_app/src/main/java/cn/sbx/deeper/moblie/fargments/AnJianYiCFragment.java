package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.CmScShType_Dic;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.view.FlowLayout;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 * 
 * @author terry.C
 * 
 */
public class AnJianYiCFragment {

	private View view;
	private Activity activity1;
	private ArrayList<CmScShType_Dic> list1;
	private CustInfo_AnJian custInfo1;
	private int width;
	private DatabaseHelper conne;
	private SQLiteDatabase state;
	private ArrayList<String> selectYCValues;
	// 当第一次加载有异常时,会自动打开异常界面,此处是控制条件,
	boolean isAutoSelect = true;
	private SpinnerAdapter spinnerAdapter;
	private Context mContext;

	public AnJianYiCFragment(Activity activity, Context context, int width,
			ArrayList<CmScShType_Dic> list, CustInfo_AnJian custInfo) {
		this.mContext=context;
		this.activity1 = activity;
		this.list1 = list;
		this.custInfo1 = custInfo;
		this.width = width;

		view = View.inflate(context, R.layout.flowlayout_container, null);
		LinearLayout ll_container_a = (LinearLayout) view
				.findViewById(R.id.ll_container_a);
		selectYCValues = selectYCValues(custInfo1);

		FlowLayout flowLayout = new FlowLayout(context);

		// 查询是否有异常

		final List<String> stringList = new ArrayList<String>();
		stringList.add("正常");
		stringList.add("异常");
		// 创建子view
		for (int i = 0; i < list1.size(); i++) {

			View childview = View.inflate(context, R.layout.childview, null);
			final Spinner aj_bj_spinner = (Spinner) childview
					.findViewById(R.id.aj_bj_spinner);
			TextView biaoju = (TextView) childview.findViewById(R.id.biaoju);
			biaoju.setText(list1.get(i).getCmScShTypeDescr());

			final int temp = i;
			spinnerAdapter = new SpinnerAdapter(context, stringList);
			aj_bj_spinner.setAdapter(spinnerAdapter);

			// 存在隐换信息,显示异常图片,list1 是要创建的所有异常类型 描述
			// 异常集合中是否包含当前的隐患类型
			if (selectYCValues.size() > 0
					&& selectYCValues.contains(list1.get(i)
							.getCmScShTypeDescr())) {
				aj_bj_spinner.setSelection(1);
			}

			aj_bj_spinner.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// 当手触摸spinner时 ,可以重复打开同一个item
					try {
						Field field = AdapterView.class
								.getDeclaredField("mOldSelectedPosition");
						field.setAccessible(true); // 设置mOldSelectedPosition可访问
						field.setInt(aj_bj_spinner,
								AdapterView.INVALID_POSITION); // 设置mOldSelectedPosition的值
					} catch (Exception e) {
						e.printStackTrace();
					}

					isAutoSelect = false;
					return false;
				}
			});
			aj_bj_spinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							// 初始化异常选项时,不自动打开异常界面,只有在触摸时才行
							if (!isAutoSelect) {
								// 选择正常时,清空异常类型选项
								if (stringList.get(arg2).equals("正常")) {

									// 1,当用户选择正常时,在数据库中查询该隐患类型是否存在隐患选项
									if (selectYCData(list1.get(temp)
											.getCmScShType(), custInfo1) > 0) {

										// 2,如果存在隐患选项,提醒用户,改类型存在隐患选项,如果选择正常,在保存时将清除该类型下的隐患信息隐患信息,
										Builder builder = new Builder(
												activity1);
										builder.setTitle("提示信息:");
										builder.setMessage("\""+list1.get(temp).getCmScShTypeDescr()+"\" 下存在隐患选项,如选择 \"正常\" 需先清除隐患选项.\n确定去清除吗?");
//										builder.setMessage("\""+list1.get(temp).getCmScShTypeDescr()+"\" 下存在隐患选项,选择 \"正常\" 将清除隐患选项.\n确定清除吗?");
										builder.setPositiveButton("确定",
												new OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														/*// 清空该隐患类型下的隐患信息
														try {
															deleteYCData(list1.get(temp).getCmScShType(),custInfo1);
														} catch (SQLException e) {
															e.printStackTrace();
															Toast.makeText(activity1,"清除失败", 1).show();
														}
														Toast.makeText(activity1,"已清除该隐患类型", 1).show();*/
														
//														打开当前隐患界面,让用户手动去除隐患
														Bundle bundle = new Bundle();
														// 隐患类型编码
														bundle.putString("code", list1.get(temp)
																.getCmScShType());
														bundle.putString("describe", list1
																.get(temp).getCmScShTypeDescr());
														bundle.putSerializable("custInfo",
																custInfo1);
														AnJianDeviceDetailFragment f = new AnJianDeviceDetailFragment();
														f.setArguments(bundle);
														((ActivityInTab) activity1).navigateTo(f);
													}

												});
										builder.setNegativeButton("取消",
												new OnClickListener() {
													@Override
													public void onClick(DialogInterface dialog,int which) {
//														选择正常,单击取消时,任然显示异常图标,
														aj_bj_spinner.setSelection(1);
//														设置为异常图标时,会自动进入隐患选项界面,防止其进入
														isAutoSelect = true;
													}
												});
										// 创建对话框对象
										AlertDialog dialog = builder.create();
										// 显示对话框
										dialog.show();
									}

								}
							}
							if (!isAutoSelect) {
								// 选择异常选项
								if (stringList.get(arg2).equals("异常")) {
									// tv_isYH.setText("是");
									// isAQ++;
									Bundle bundle = new Bundle();
									// 隐患类型编码
									bundle.putString("code", list1.get(temp)
											.getCmScShType());
									bundle.putString("describe", list1
											.get(temp).getCmScShTypeDescr());
									bundle.putSerializable("custInfo",
											custInfo1);
									AnJianDeviceDetailFragment f = new AnJianDeviceDetailFragment();
									f.setArguments(bundle);
									((ActivityInTab) activity1).navigateTo(f);
								}

								// 当打开后,使其同一个item 不能重复点击,要不然一直执行
								try {
									Field field = AdapterView.class
											.getDeclaredField("mOldSelectedPosition");
									field.setAccessible(false); // 设置mOldSelectedPosition可访问
									field.setInt(aj_bj_spinner,
											AdapterView.INVALID_POSITION); // 设置mOldSelectedPosition的值
								} catch (Exception e) {
									e.printStackTrace();
								}
								isAutoSelect = true;

							}
							/*
							 * else { if (isAQ > 0) { isAQ--; } if (isAQ == 0) {
							 * tv_isYH.setText("否"); } }
							 */
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});

			flowLayout.addView(childview);
		}

		ll_container_a.addView(flowLayout);
	}

	// 选择正常时 查询数据库中是否有异常选项,
	protected int selectYCData(String cmScShType, CustInfo_AnJian custInfo) {

		String sqlString = "select * from perSh_aj where  cmSchedId = '"
				+ custInfo.getCmSchedId() + "' and accountId = '"
				+ custInfo.getAccountId()
				+ "' and cmScShCheck = 'Y' and cmScShType = '" + cmScShType
				+ "'";
		Cursor resultSet = null;
		try {
			 conne = new DatabaseHelper(mContext);
			 state = conne.getWritableDatabase();
			 resultSet = state.rawQuery(sqlString, null);
//			resultSet.last();

			return resultSet.getCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (resultSet != null) {

				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (state != null) {

				try {
					state.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conne != null) {

				try {
					conne.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	public View getView() {

		return view;

	}

	// 用户选择正常时,清空隐患信息
	private void deleteYCData(String cmScShType, CustInfo_AnJian custInfo)
			throws SQLException {
		String sqlString = "delete from perSh_aj where  cmSchedId = '"
				+ custInfo.getCmSchedId() + "' and accountId = '"
				+ custInfo.getAccountId()
				+ "' and cmScShCheck = 'Y' and cmScShType = '" + cmScShType
				+ "'";
		conne = new DatabaseHelper(mContext);
		state = conne.getWritableDatabase();
		state.execSQL(sqlString);

		if (state != null) {

			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conne != null) {

			try {
				conne.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 查询当前用户的异常选项,初始化spinner
	public ArrayList<String> selectYCValues(CustInfo_AnJian custInfo) {

		String sqlString = "select * from perSh_aj where  cmSchedId = '"
				+ custInfo.getCmSchedId() + "' and accountId = '"
				+ custInfo.getAccountId() + "' and cmScShCheck = 'Y'";
		ArrayList<String> arrayList = new ArrayList<String>();
		Cursor result_set = null;
		try {
			conne = new DatabaseHelper(mContext);
			state = conne.getWritableDatabase();
			result_set = state.rawQuery(sqlString,null);
			if(result_set.moveToFirst()){
				arrayList.add(result_set.getString(result_set.getColumnIndex("cmScShTypeDescr")));
				while (result_set.moveToNext()) {
					arrayList.add(result_set.getString(result_set.getColumnIndex("cmScShTypeDescr")));
				}
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (result_set != null) {

				try {
					result_set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (state != null) {

				try {
					state.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conne != null) {

				try {
					conne.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return arrayList;
	}

	public class SpinnerAdapter extends BaseAdapter {
		private List<String> mList;
		private Context mContext;

		public SpinnerAdapter(Context pContext, List<String> pList) {
			this.mContext = pContext;
			this.mList = pList;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 下面是重要代码
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
			convertView = _LayoutInflater.inflate(R.layout.anjian_spinner_item,
					null);
			if (convertView != null) {
				TextView _TextView1 = (TextView) convertView
						.findViewById(R.id.spinner_item);
				ImageView _ImageView = (ImageView) convertView
						.findViewById(R.id.spinner_iv);
				_TextView1.setText(mList.get(position));
				if (mList.get(position).equals("正常")) {
					_ImageView.setBackgroundResource(R.drawable.zhengchang);
				} else {
					_ImageView.setBackgroundResource(R.drawable.yichang);
				}
				/*
				 * // 存在隐换信息,显示异常图片,list1 是所有的异常类型描述 // 异常集合中是否包含当前的隐患类型 if
				 * (selectYCValues.size() > 0&&
				 * selectYCValues.contains(list1.get
				 * (position).getCmScShTypeDescr())) {
				 * _ImageView.setBackgroundResource(R.drawable.yichang); }
				 */
			}

			return convertView;
		}
	}

}
