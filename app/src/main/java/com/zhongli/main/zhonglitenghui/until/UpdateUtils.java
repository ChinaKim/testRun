package com.zhongli.main.zhonglitenghui.until;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import java.io.File;
import java.util.List;

public class UpdateUtils {
	public static final String apkDownloadDir = "Download";
	public static final String action = "update";

	/**
	 * 获取软件版本号
	 * 
	 * @author lizhen 2014-2-18下午5:26:01
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取软件版本名称
	 * 
	 * @author lizhen 2014-2-18下午5:24:16
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取apk文件SD卡的父路径
	 * 
	 * @author lizhen 2014-3-14下午2:30:34
	 * @param context
	 * @return
	 */
	public static synchronized String getApkDir(Context context) {
		File apkDir = null;
		// 先获取手机SD卡根路径，没有SD卡就获取应用缓存路径
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			apkDir = new File(Environment.getExternalStorageDirectory()
					.getPath(), "Download");
		} else {
			apkDir = new File(context.getApplicationContext().getCacheDir()
					.getPath(), "Download");
		}
		if (!apkDir.exists()) {
			apkDir.mkdirs();
		}
		return apkDir.getPath();

	}

	/**
	 * @author lizhen 2014-5-9上午11:27:41 lizhen@touch-spring.com 判断服务是否开启
	 * @param serviceClassName
	 * @return
	 */
	public static boolean checkServiceIsStart(Context context,
			String serviceClassName) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = manager.getRunningServices(80);
		for (RunningServiceInfo info : serviceList) {
			if (info.service.getClassName().equals(serviceClassName)) {
				return true;
			}
		}
		return false;
	}

}
