package cn.sbx.deeper.moblie.util;

import android.content.pm.PackageInfo;

import cn.sbx.deeper.moblie.MobileApplication;

public class VersionUtils {

	public static String getVersionName() {
		String s = "1.0";
		try {
			PackageInfo packInfo = MobileApplication.appContext.getPackageManager().getPackageInfo(
					MobileApplication.appContext.getPackageName(), 0);
			return packInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		}
	}
	
	public static String getPackageName() {
		String s = "";
		try {
			PackageInfo packInfo = MobileApplication.appContext.getPackageManager().getPackageInfo(
					MobileApplication.appContext.getPackageName(), 0);
			return packInfo.packageName;
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		}
	}
	
}
