package cn.sbx.deeper.moblie.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import cn.sbx.deeper.moblie.domian.LinkAppInfo;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;



public class LinkAppActivity extends ActivityInTab {

	private SinopecMenuModule module;
	// private SharedPreferences preferences;
	private List<LinkAppInfo> listAppInfos;
	public static int index = 0;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// preferences = getSharedPreferences("sys_config", 0);
		super.onCreate(savedInstanceState);
		module = (SinopecMenuModule) getIntent().getSerializableExtra("entry");
		listAppInfos = (List<LinkAppInfo>) getIntent().getSerializableExtra("listAppInfos");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		index = 1;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (index != 0) {
			return;
		}

		try {
			// URI定义了通信协议
			Uri uri = Uri.parse(module.menuPages.get(0).urlpath + "://" + module.menuPages.get(0).urlpath);
			// 通过Action和URI调用第二个进程中的Activity，并传递数据
			Intent invokeIntent = new Intent(null, uri);
			for (int i = 0; i < listAppInfos.size(); i++) {
				invokeIntent.putExtra(listAppInfos.get(i).getKey(), listAppInfos.get(i).getValue());
				System.out.println("key:" + listAppInfos.get(i).getKey() + "---value:" + listAppInfos.get(i).getValue());
			}
			if (listAppInfos.size() > 0) {
				startActivity(invokeIntent);
			} else {
				Toast.makeText(this, "帐号或者密码错误", Toast.LENGTH_SHORT).show();
			}
			return;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Toast.makeText(this, "帐号或者密码错误", Toast.LENGTH_SHORT).show();
		}

		// try {
		// if (module == null || preferences == null)
		// return;
		// if ("".equals(preferences.getString(module.id, ""))) {
		// Toast.makeText(this, "用户名或密码未设置", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// String[] arr = preferences.getString(module.id, "").split(";");
		// // URI定义了通信协议
		// Uri uri = Uri.parse(module.menuPages.get(0).urlpath + "://" +
		// module.menuPages.get(0).urlpath);
		// // 通过Action和URI调用第二个进程中的Activity，并传递数据
		// Intent invokeIntent = new Intent(null, uri);
		// invokeIntent.putExtra("prm1", arr[0]);
		// invokeIntent.putExtra("prm2", arr[1]);
		// startActivity(invokeIntent);
		// finish();
		// return;
		// } catch (Exception e) {
		// // TODO: handle exception
		// Toast.makeText(this, "跳转设备出现异常", Toast.LENGTH_SHORT).show();
		// }

	}
}
