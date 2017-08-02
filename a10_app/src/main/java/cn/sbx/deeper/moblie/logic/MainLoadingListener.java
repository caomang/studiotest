package cn.sbx.deeper.moblie.logic;

import cn.sbx.deeper.moblie.domian.SoftInfo;


public interface MainLoadingListener {
	void checkUpdate(SoftInfo softInfo);
	
	void initMainView();
	
}
