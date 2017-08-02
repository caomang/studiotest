package cn.sbx.deeper.moblie.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.activity.SinopecAllMenuActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.ResultInfo;
import cn.sbx.deeper.moblie.util.DataUtil;
import cn.sbx.deeper.moblie.util.UserInfo;

/**
 * 负责语音播报，拍照，数据库操作，数据同步处理等
 * 
 */
public class LocalService extends Service {

	private final IBinder mBinder = new LocalBinder();
	// private StringBuilder sbs = new StringBuilder("");
	private SharedPreferences sp;
	String provider;
	Location currentLocation;
	LocationManager locationManager;
	// private float ms = 0.8f;
	LocationListener locationListener;
	public List<String> submitList = new ArrayList<String>();
	Timer timerRefresh;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public void init() {
		sp = getSharedPreferences("sys_config", Context.MODE_PRIVATE);
		System.out.println("服务启动");
		// openGPSSettings();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 查找到服务信息
		Criteria criteria = new Criteria();
		// criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		// criteria.setAltitudeRequired(false);
		// criteria.setBearingRequired(false);
		// criteria.setCostAllowed(true);
		// criteria.setSpeedRequired(true);// 手机位置移动
		// criteria.setPowerRequirement(Criteria.POWER_HIGH); // 低功耗
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		// criteria.setSpeedRequired(true);// 手机位置移动
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(serviceName);
		provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
		/*currentLocation = locationManager.getLastKnownLocation(provider);*/
		// 测试用
		// new uploadStatus() {}.execute(MainActivity.locInfo,"0");//0
		// 开始，1，结束，2运动中

		// MainActivity.locInfo = updateMsg(currentLocation);
		// sbs.append(updateMsg(currentLocation));
		// sbs.append(";");

		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {

				if (location.hasSpeed()) {
					if (location.getSpeed() > Float.valueOf(sp.getString(
							Constants.Speed, "0.8"))) {
						SinopecAllMenuActivity.locInfo = updateMsg(location);
						// 增加开始坐标点
						if ("0".equals(SinopecAllMenuActivity.status)) {
							new startLoadTabs().execute();// 0
							// 开始，1，结束，2运动中
						} else if ("2".equals(SinopecAllMenuActivity.status)) {
							if (submitList.size() == 0) {// 如果没有值就放进去
								submitList.add(SinopecAllMenuActivity.locInfo);
							} else {
								String curloc = SinopecAllMenuActivity.locInfo
										.split("\\|")[0];
								String lastloc = submitList.get(
										submitList.size() - 1).split("\\|")[0];
								if (!curloc.equals(lastloc)) {
									submitList
											.add(SinopecAllMenuActivity.locInfo);
								}
								if (submitList.size() >= Integer.valueOf(sp
										.getString(Constants.Update_Num, "10"))) {
									new loadTabs().execute();// 等凑够了数组就提交
								}
							}
						}

					}
					currentLocation = location;
				}

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
				new loadTabs().execute();// 等凑够了数组就提交
			}

		};

		/*locationManager.requestLocationUpdates(provider, 1000, 10,
				locationListener);*/

		// Timer timer = new Timer();
		// timer.schedule(cebiantask,
		// sp.getInt(Constants.Refresh_Time, 20 * 1000),
		// sp.getInt(Constants.Refresh_Time, 20 * 1000));
		if (timerRefresh != null) {
			timerRefresh.cancel();
		} else {
			timerRefresh = new Timer();
		}
		timerRefresh.schedule(refreshTask,
				sp.getInt(Constants.Refresh_Time, 1 * 1000),
				sp.getInt(Constants.Refresh_Time, 10* 1000));
	}

	/** * 显示定时器 */
	private TimerTask cebiantask = new TimerTask() {
		@Override
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

	/** * */
	private TimerTask refreshTask = new TimerTask() {
		@Override
		public void run() {
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
		}
	};
	/** * Handler */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			System.out.println(msg.what + "================msg.what");
			switch (msg.what) {
			case 1:
				new loadTabs().execute();
				break;
			case 2:// 1秒搜集一次坐标
				SinopecAllMenuActivity.locInfo = updateMsg(currentLocation);
				System.out.println("SinopecAllMenuActivity.locInfo==========="
						+ SinopecAllMenuActivity.locInfo);
				if ("0".equals(SinopecAllMenuActivity.status)) {
					new startLoadTabs().execute();
				} else {
					if (submitList.size() == 0) {// 如果没有值就放进去
						submitList.add(SinopecAllMenuActivity.locInfo);
					} else {
						String curloc = SinopecAllMenuActivity.locInfo
								.split("\\|")[0];
						String lastloc = submitList.get(submitList.size() - 1)
								.split("\\|")[0];
						if (!curloc.equals(lastloc)) {
							submitList.add(SinopecAllMenuActivity.locInfo);
						}
						if (submitList.size() >= Integer.valueOf(sp.getString(
								Constants.Update_Num, "10"))) {
							new loadTabs().execute();// 等凑够了数组就提交
						}
					}
				}

				break;
			default:
				break;
			}
		}

	};

	// 开始提交
	class startLoadTabs extends AsyncTask<Void, Void, ResultInfo> {

		@Override
		protected ResultInfo doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// 本地模拟数
			try {
				System.out.println("打开软件第一次提交");
				// asd
				final MobileApplication application = (MobileApplication) getApplication();
				final StringBuilder sb = new StringBuilder();
				sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root>");
				sb.append("<terminalCode>" + application.deviceId
						+ "</terminalCode>");
				sb.append("<coordinate>" + SinopecAllMenuActivity.locInfo
						+ "</coordinate>");
				sb.append("<status>" + "0" + "</status>");
				sb.append("<userName>" + UserInfo.getInstance().getUsername()
						+ "</userName>");
				sb.append("</root>");

				return DataUtil.sendXML(sb.toString());// 0
														// 开始，1，结束，2运动中

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				if ("1".equals(result.key)) {
					SinopecAllMenuActivity.status = "2";
					System.out.println("第一次成功:" + result.message);
				} else
					System.out.println("第一次error:" + result.message);
			} else
				System.out.println("error");
		}
	}

	// 运动中提交
	class loadTabs extends AsyncTask<Void, Void, ResultInfo> {

		@Override
		protected ResultInfo doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// 本地模拟数据
			try {
				System.out.println("运动中提交");
				String sbsr = "";
				StringBuilder sbs = new StringBuilder("");
				for (String item : submitList) {
					sbs.append(item);
					sbs.append(";");
				}
				if (sbs.toString().contains(";")) {
					sbsr = sbs.toString().substring(0,
							sbs.toString().length() - 1);
				}
				final MobileApplication application = (MobileApplication) getApplication();
				final StringBuilder sb = new StringBuilder();
				sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root>");
				sb.append("<terminalCode>" + application.deviceId
						+ "</terminalCode>");
				sb.append("<coordinate>" + sbsr.toString() + "</coordinate>");
				sb.append("<status>" + "2" + "</status>");
				sb.append("<userName>" + UserInfo.getInstance().getUsername()
						+ "</userName>");
				sb.append("</root>");
				System.out.println("运动中提交===========" + sb.toString());
				// asd
				return DataUtil.sendXML(sb.toString());// 0
				// 开始，1，结束，2运动中
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				if ("1".equals(result.key)) {
					submitList.clear();
					System.out.println("运动中成功:" + result.message);
				} else
					System.out.println("运动中error:" + result.message);
			} else
				System.out.println("error");
		}
	}

	private String updateMsg(Location loc) {
		StringBuilder sb = new StringBuilder("");
		if (loc != null) {
			double lat = loc.getLatitude();
			double lng = loc.getLongitude();

			// sb.append("纬度：" + lat + "\n经度：" + lng);
			sb.append(lat + "," + lng);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
			// 获取当前时间
			String time = sdf.format(new Date());
			sb.append("|" + time);

			// if (loc.hasAccuracy()) {
			// sb.append("\n精度：" + loc.getAccuracy());
			// }
			//
			// if (loc.hasAltitude()) {
			// sb.append("\n海拔：" + loc.getAltitude() + "m");
			// }
			//
			// if (loc.hasBearing()) {// 偏离正北方向的角度
			// sb.append("\n方向：" + loc.getBearing());
			// }
			//
			// if (loc.hasSpeed()) {
			// if (loc.getSpeed() * 3.6 < 5) {
			// sb.append("\n速度：0.0km/h");
			// } else {
			// sb.append("\n速度：" + loc.getSpeed() * 3.6 + "km/h");
			// }
			//
			// }
		} else {
			System.out.println("没有位置坐标");
		}
		// System.out.println("sb.toString():" + sb.toString());
		return sb.toString();
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public LocalService getService() {
			return LocalService.this;
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (timerRefresh != null) {
			timerRefresh.cancel();
		}

		super.onDestroy();
	}

	private static final int TWO_MINUTES = 1000;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;
		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}
